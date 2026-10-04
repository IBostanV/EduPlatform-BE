package com.play.quiz.conquest;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.domain.MessageGroup;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.CategoryRepository;
import com.play.quiz.repository.MessageGroupRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ExperiencePayout;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ConquestServiceImpl implements ConquestService {

    /** The category whose children are the countries; each child's natural id is its ISO3 code. */
    static final String COUNTRIES_PARENT = "COUNTRIES";

    private final AuthenticationFacade authenticationFacade;
    private final CategoryRepository categoryRepository;
    private final ConquestAttemptRepository attemptRepository;
    private final UserQuizHistoryRepository historyRepository;
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final MessageGroupRepository messageGroupRepository;
    private final UserGroupRepository userGroupRepository;
    private final Clock clock;

    // Not read-only: reading the map is also when a closed round's winners are found, and that
    // is the only moment there is to pay them. Nothing is stored about who holds what, so there
    // is no settling job to do it instead.
    @Override
    @Transactional
    public ConquestState getState() {
        return stateAt(LocalDateTime.now(clock));
    }

    @Override
    @Transactional
    public ConquestState setTeam(final Long groupId) {
        Account player = currentAccount();
        if (Objects.nonNull(groupId) && !userGroupRepository.isMember(groupId, player.getEmail())) {
            throw new IllegalArgumentException("You can only play for a group you are in");
        }
        accountRepository.setConquestTeam(player.getAccountId(), groupId);
        log.info("Account {} now plays conquest for group {}", player.getAccountId(), groupId);
        return stateAt(LocalDateTime.now(clock));
    }

    @Override
    @Transactional
    public ConquestState recordAttempt(final Long countryId, final Long historyId) {
        LocalDateTime now = LocalDateTime.now(clock);
        long round = ConquestSchedule.roundAt(now);
        Account player = currentAccount();
        Category country = country(countryId);

        if (!ConquestSchedule.isOpenAt(now)) {
            throw new IllegalArgumentException("Conquest is closed today; it opens again "
                    + ConquestSchedule.nextOpenAt(now));
        }
        if (!openCountryIds(round).contains(countryId)) {
            throw new IllegalArgumentException(country.getName() + " is not open this round");
        }
        cooldownLeft(countryId, player.getAccountId(), now).ifPresent(left -> {
            throw new IllegalArgumentException("Another go at " + country.getName() + " in "
                    + left.toHours() + "h " + left.toMinutesPart() + "m");
        });

        UserQuizHistory run = historyRepository.findById(historyId)
                .orElseThrow(() -> new RecordNotFoundException("No quiz run with id: " + historyId));
        if (!Objects.equals(run.getAccount().getAccountId(), player.getAccountId())) {
            throw new IllegalArgumentException("That quiz run is not yours");
        }
        if (Objects.isNull(run.getTotalAnswers())) {
            throw new IllegalArgumentException("That quiz run has no score to enter");
        }
        // One run counts once, whatever the client reports; the unique key says the same thing.
        if (attemptRepository.findByHistoryId(historyId).isPresent()) {
            throw new IllegalArgumentException("That quiz run has already been entered");
        }

        attemptRepository.save(ConquestAttempt.builder()
                .roundNo(round)
                .country(country)
                .account(player)
                .historyId(historyId)
                .rightAnswers(run.getRightAnswers())
                .totalAnswers(run.getTotalAnswers())
                .spentTime(Optional.ofNullable(run.getSpentTime()).orElse(0.0))
                .createdDate(now)
                .build());

        // A go at a country is worth more than the same quiz taken on its own. The run has
        // already been paid for at the ordinary rate by the time it is entered here, so what is
        // owed is the difference.
        userService.addExperience(player.getAccountId(),
                ExperiencePayout.conquestTopUp(run.getRightAnswers(), run.getTotalAnswers()));

        log.info("Account {} attempted {} in round {}: {}/{}", player.getAccountId(), country.getNaturalId(),
                round, run.getRightAnswers(), run.getTotalAnswers());

        return stateAt(now);
    }

    private ConquestState stateAt(final LocalDateTime now) {
        long round = ConquestSchedule.roundAt(now);
        boolean open = ConquestSchedule.isOpenAt(now);
        List<Category> countries = categoryRepository.findCountries(COUNTRIES_PARENT);
        Set<Long> openIds = openCountryIds(round, countries);

        // A run taken in a round that has not ended does not count until it does, so the winners
        // appear together rather than the map shifting under the players still taking it — and a
        // closed day in the middle of a round is not the end of it.
        boolean ended = !now.isBefore(ConquestSchedule.roundClosesAt(round));
        Map<Long, ConquestAttempt> holders = holdersOf(countries, ended ? round + 1 : round);
        payConquerors(holders.values());
        Long accountId = currentAccountId();

        List<ConquestCountry> mapped = countries.stream()
                .map(country -> {
                    boolean isOpen = openIds.contains(country.getCatId());
                    String blocked = blockedReason(country, isOpen, open, accountId, now);
                    return ConquestCountry.of(country, isOpen, holders.get(country.getCatId()),
                            isOpen && Objects.isNull(blocked), blocked);
                })
                .toList();

        // Teams: a country counts for the chat group its holder plays for.
        Long yourTeamId = Objects.isNull(accountId) ? null
                : accountRepository.findById(accountId).map(Account::getConquestTeam).orElse(null);
        Set<Long> teamIds = holders.values().stream()
                .map(holder -> holder.getAccount().getConquestTeam())
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(java.util.HashSet::new));
        if (Objects.nonNull(yourTeamId)) teamIds.add(yourTeamId);
        Map<Long, ConquestState.Team> teams = messageGroupRepository.findAllById(teamIds).stream()
                .collect(Collectors.toMap(MessageGroup::getGroupId,
                        group -> new ConquestState.Team(group.getGroupId(), group.getName()), (first, second) -> first));

        List<ConquestCountry> withTeams = mapped.stream()
                .map(country -> Optional.ofNullable(holders.get(country.categoryId()))
                        .map(holder -> holder.getAccount().getConquestTeam())
                        .map(teams::get)
                        .map(country::withTeam)
                        .orElse(country))
                .toList();
        List<ConquestState.TeamStanding> standings = withTeams.stream()
                .filter(country -> Objects.nonNull(country.team()))
                .collect(Collectors.groupingBy(ConquestCountry::team, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new ConquestState.TeamStanding(entry.getKey(), entry.getValue()))
                .sorted(java.util.Comparator.comparingLong(ConquestState.TeamStanding::countries).reversed()
                        .thenComparing(standing -> String.valueOf(standing.team().name())))
                .toList();

        return new ConquestState(round, open, ConquestSchedule.roundOpensAt(round),
                ConquestSchedule.roundClosesAt(round), ConquestSchedule.roundOpensAt(round + 1),
                open ? ConquestSchedule.openUntil(now) : null, open ? null : ConquestSchedule.nextOpenAt(now),
                withTeams, standings, Objects.isNull(yourTeamId) ? null : teams.get(yourTeamId));
    }

    /**
     * Pays whoever is holding a country and has not been paid for it yet.
     *
     * <p>Once each: the attempt itself carries the flag, and the claim is one conditional update,
     * so two readers landing on the same unpaid winner cannot both pay it. A conqueror who keeps
     * a country because nobody beat them is the same attempt still, and is not paid again — the
     * bonus is for taking a country, not for holding one.
     */
    private void payConquerors(final Collection<ConquestAttempt> holders) {
        holders.stream()
                .filter(holder -> !holder.isBonusPaid())
                .filter(holder -> attemptRepository.claimBonus(holder.getAttemptId()) == 1)
                .forEach(holder -> {
                    log.info("Account {} conquered {}: {} experience",
                            holder.getAccount().getAccountId(), holder.getCountry().getNaturalId(),
                            ExperiencePayout.CONQUEST_BONUS);
                    userService.addExperience(holder.getAccount().getAccountId(), ExperiencePayout.CONQUEST_BONUS);
                });
    }

    // Read-only: the bonus is paid where the map is read, not here.
    @Override
    @Transactional(readOnly = true)
    public List<ConquestChange> changesSince(final LocalDateTime since) {
        LocalDateTime now = LocalDateTime.now(clock);
        List<Category> countries = categoryRepository.findCountries(COUNTRIES_PARENT);
        List<ConquestChange> changes = new ArrayList<>();

        for (long round = Math.max(0, ConquestSchedule.roundAt(since)); round <= ConquestSchedule.roundAt(now); round++) {
            LocalDateTime closed = ConquestSchedule.roundClosesAt(round);
            if (!closed.isAfter(since) || closed.isAfter(now)) {
                continue;
            }
            // Only the countries a round opened can change hands in it.
            List<Category> opened = ConquestSchedule.countriesFor(round, countries);
            Map<Long, ConquestAttempt> before = holdersOf(opened, round);
            for (ConquestAttempt holder : holdersOf(opened, round + 1).values()) {
                ConquestAttempt previous = before.get(holder.getCountry().getCatId());
                boolean taken = holder.getRoundNo() == round && (Objects.isNull(previous)
                        || !previous.getAccount().getAccountId().equals(holder.getAccount().getAccountId()));
                if (taken) {
                    changes.add(new ConquestChange(round, closed, holder.getCountry(), holder.getAccount(),
                            Objects.isNull(previous) ? null : previous.getAccount()));
                }
            }
        }
        return changes;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConquestRound> roundsOpenedSince(final LocalDateTime since) {
        LocalDateTime now = LocalDateTime.now(clock);
        List<Category> countries = categoryRepository.findCountries(COUNTRIES_PARENT);
        List<ConquestRound> rounds = new ArrayList<>();

        for (long round = Math.max(0, ConquestSchedule.roundAt(since)); round <= ConquestSchedule.roundAt(now); round++) {
            LocalDateTime opened = ConquestSchedule.roundOpensAt(round);
            if (opened.isAfter(since) && !opened.isAfter(now)) {
                rounds.add(new ConquestRound(round, opened, ConquestSchedule.countriesFor(round, countries)));
            }
        }
        return rounds;
    }

    /** Why this player cannot go at this country now, or null when they can. */
    private String blockedReason(final Category country, final boolean isOpen, final boolean roundOpen,
                                 final Long accountId, final LocalDateTime now) {
        if (!roundOpen) {
            return "Conquest is closed today";
        }
        if (!isOpen) {
            return "Not open this round";
        }
        if (Objects.isNull(accountId)) {
            return "Sign in to take part";
        }
        return cooldownLeft(country.getCatId(), accountId, now)
                .map(left -> "Another go in " + left.toHours() + "h " + left.toMinutesPart() + "m")
                .orElse(null);
    }

    /**
     * How long is left of this player's wait on this country, or empty when they may go now.
     *
     * <p>One go every {@link ConquestSchedule#ATTEMPT_COOLDOWN}, so a round is worth a couple of
     * tries and the record goes to whoever knows the answers rather than whoever grinds hardest.
     */
    private Optional<Duration> cooldownLeft(final Long countryId, final Long accountId, final LocalDateTime now) {
        return attemptRepository.findLatestAttempts(countryId, accountId, PageRequest.of(0, 1)).stream()
                .findFirst()
                .map(ConquestAttempt::takenAt)
                .map(taken -> Duration.between(now, taken.plus(ConquestSchedule.ATTEMPT_COOLDOWN)))
                .filter(left -> !left.isNegative() && !left.isZero());
    }

    /**
     * Who holds each country: the best attempt from a round before {@code beforeRound} — most
     * right, then fastest, then whoever got there first. Nobody is installed or unseated by a job,
     * so a conqueror keeps the country until a better run turns up, however many rounds that takes.
     */
    private Map<Long, ConquestAttempt> holdersOf(final List<Category> countries, final long beforeRound) {
        List<Long> ids = countries.stream().map(Category::getCatId).toList();
        if (ids.isEmpty()) {
            return Map.of();
        }

        Map<Long, ConquestAttempt> best = new HashMap<>();
        // Ordered best-first within each country, so the first of each is the one that stands.
        attemptRepository.findStandingAttempts(ids, beforeRound)
                .forEach(attempt -> best.putIfAbsent(attempt.getCountry().getCatId(), attempt));

        return best;
    }

    private Set<Long> openCountryIds(final long round) {
        return openCountryIds(round, categoryRepository.findCountries(COUNTRIES_PARENT));
    }

    private Set<Long> openCountryIds(final long round, final List<Category> countries) {
        return ConquestSchedule.countriesFor(round, countries).stream()
                .map(Category::getCatId)
                .collect(Collectors.toSet());
    }

    // Only a child of the countries category is a country: any other id is not a thing to conquer.
    private Category country(final Long countryId) {
        return categoryRepository.findById(countryId)
                .filter(category -> Objects.nonNull(category.getParent())
                        && COUNTRIES_PARENT.equals(category.getParent().getNaturalId()))
                .orElseThrow(() -> new RecordNotFoundException("No country with id: " + countryId));
    }

    private Account currentAccount() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername());
    }

    // The map reads signed out too, so there may be nobody to work a cooldown out for.
    private Long currentAccountId() {
        try {
            return currentAccount().getAccountId();
        } catch (RuntimeException exception) {
            return null;
        }
    }
}
