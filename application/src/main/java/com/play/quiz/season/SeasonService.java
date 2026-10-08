package com.play.quiz.season;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import com.play.quiz.cosmetic.Cosmetic;
import com.play.quiz.cosmetic.CosmeticService;
import com.play.quiz.daily.DailyTaskClaimRepository;
import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.duel.DuelService;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.social.DailyChallengeRepository;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The season pass: the week's quests and the season's track. Quests are paid as they are read,
 * like the daily tasks; tiers are claimed by the player, which is the moment worth having.
 * A season's points are its quest points plus {@value Season#DAILY_TASK_POINTS} for every daily
 * task finished in it, both read from what was already paid.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonClaimRepository claimRepository;
    private final DailyTaskClaimRepository dailyTaskClaimRepository;
    private final UserQuizHistoryRepository historyRepository;
    private final DailyChallengeRepository dailyChallengeRepository;
    private final DuelService duelService;
    private final CosmeticService cosmeticService;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    public record QuestView(String code, int progress, int target, int points, boolean completed, boolean awarded) {}

    /** {@code item} is a cosmetic's code for the two tiers that pay one, else null and {@code coins} pays. */
    public record TierView(int tier, int points, int coins, String item, boolean unlocked, boolean claimed) {}

    public record Status(int number, long startsAt, long endsAt, long weekEndsAt, long points, int tierPoints,
                         List<QuestView> quests, List<TierView> tiers) {}

    // Not read-only: reading the quests is what pays them.
    @Transactional
    public Status status() {
        Long me = currentAccountId();
        LocalDate today = LocalDate.now();
        Season season = Season.of(today);
        LocalDate weekStart = Season.weekStart(today);

        Set<String> paidThisWeek = Set.copyOf(claimRepository.findClaimedCodes(me, weekStart));
        List<QuestView> quests = java.util.Arrays.stream(WeeklyQuest.values())
                .map(quest -> quest(quest, me, weekStart, paidThisWeek))
                .toList();

        long points = claimRepository.sumQuestPoints(me, season.start(), season.end())
                + (long) Season.DAILY_TASK_POINTS * dailyTaskClaimRepository.countByAccountIdAndTaskDayBetween(
                        me, season.start(), season.end().minusDays(1));
        Set<String> claimedTiers = Set.copyOf(claimRepository.findClaimedCodes(me, season.start()));
        List<TierView> tiers = IntStream.rangeClosed(1, Season.TIERS)
                .mapToObj(tier -> new TierView(tier, tier * Season.TIER_POINTS, Season.coinsFor(tier),
                        Season.itemFor(tier).map(Cosmetic::name).orElse(null),
                        points >= (long) tier * Season.TIER_POINTS, claimedTiers.contains(tierCode(tier))))
                .toList();

        return new Status(season.number(), millis(season.start()), millis(season.end()), millis(weekStart.plusWeeks(1)),
                points, Season.TIER_POINTS, quests, tiers);
    }

    /** Takes a tier's reward, once, when the season's points have reached it. */
    @Transactional
    public Status claim(final int tier) {
        if (tier < 1 || tier > Season.TIERS) {
            throw new IllegalArgumentException(ServerText.t("err_season_no_tier", "There is no tier {{tier}}", "tier", tier));
        }
        Status status = status();
        TierView view = status.tiers().get(tier - 1);
        if (!view.unlocked()) {
            throw new IllegalArgumentException(ServerText.t("err_season_tier_locked", "Tier {{tier}} needs {{points}} season points",
                    "tier", tier, "points", view.points()));
        }
        Long me = currentAccountId();
        if (claimRepository.claim(me, tierCode(tier), Season.of(LocalDate.now()).start(), 0) == 1) {
            Optional<Cosmetic> item = Season.itemFor(tier);
            if (item.isPresent() && cosmeticService.grant(me, item.get())) {
                log.info("Account {} claimed season tier {}: {}", me, tier, item.get());
            } else {
                int coins = item.isPresent() ? Season.OWNED_REWARD_COINS : Season.coinsFor(tier);
                accountRepository.addCoins(me, coins);
                log.info("Account {} claimed season tier {}: {} coins", me, tier, coins);
            }
        }
        return status();
    }

    private QuestView quest(final WeeklyQuest quest, final Long me, final LocalDate weekStart, final Set<String> paid) {
        int progress = progressOf(quest, me, weekStart);
        boolean completed = progress >= quest.getTarget();
        boolean awarded = completed && !paid.contains(quest.claimCode())
                && claimRepository.claim(me, quest.claimCode(), weekStart, quest.getPoints()) == 1;
        if (awarded) log.info("Account {} finished weekly quest {}: {} season points", me, quest, quest.getPoints());
        return new QuestView(quest.name(), Math.min(progress, quest.getTarget()), quest.getTarget(), quest.getPoints(),
                completed, awarded);
    }

    private int progressOf(final WeeklyQuest quest, final Long me, final LocalDate weekStart) {
        LocalDateTime from = weekStart.atStartOfDay();
        LocalDateTime to = weekStart.plusWeeks(1).atStartOfDay();
        return switch (quest) {
            case PLAY_QUIZZES -> countedRuns(me, from, to).size();
            case RIGHT_ANSWERS -> countedRuns(me, from, to).stream().mapToInt(UserQuizHistory::getRightAnswers).sum();
            case DAILY_PUZZLES -> (int) dailyChallengeRepository.findPlayedDays(me).stream()
                    .filter(day -> !day.isBefore(weekStart) && day.isBefore(weekStart.plusWeeks(1)))
                    .count();
            case DUEL_ROUNDS -> duelService.roundsPlayedBetween(me, from, to);
        };
    }

    // As for the daily tasks: scored runs only, and custom quizzes count for nothing.
    private List<UserQuizHistory> countedRuns(final Long me, final LocalDateTime from, final LocalDateTime to) {
        return historyRepository.findRunsBetween(me, from, to).stream()
                .filter(run -> Objects.nonNull(run.getRightAnswers()) && Objects.nonNull(run.getTotalAnswers()))
                .filter(run -> !run.getQuiz().isCustom())
                .toList();
    }

    private static String tierCode(final int tier) {
        return "TIER:" + tier;
    }

    private static long millis(final LocalDate day) {
        return day.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }
}
