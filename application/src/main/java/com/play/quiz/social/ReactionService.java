package com.play.quiz.social;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Reactions on news lines: one of each kind per player per line, pressed again to take back. */
@Log4j2
@Service
@RequiredArgsConstructor
public class ReactionService {

    /** A feed key: its type, a dash, then ids ("FRIEND_CONQUEST-34-12"). Nothing else is stored. */
    private static final Pattern KEY = Pattern.compile("^[A-Z_]{1,40}-[A-Za-z0-9_-]{1,120}$");
    /** How many lines one read may ask about: a page of news. */
    private static final int MAX_KEYS = 100;

    /** A kind on a line: how many, and whether the reader is one of them. */
    public record Tally(Reaction.Kind kind, long count, boolean mine) {}

    private final ReactionRepository reactionRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    /** Each line's tallies, every kind listed in the same order, kinds nobody used included. */
    @Transactional(readOnly = true)
    public Map<String, List<Tally>> tallies(final Collection<String> keys) {
        List<String> asked = keys.stream().filter(key -> KEY.matcher(key).matches()).distinct().limit(MAX_KEYS).toList();
        if (asked.isEmpty()) return Map.of();
        Long me = currentAccountId();
        Map<String, List<Reaction>> byKey = reactionRepository.findByItemKeyIn(asked).stream()
                .collect(Collectors.groupingBy(Reaction::getItemKey));

        Map<String, List<Tally>> result = new LinkedHashMap<>();
        asked.forEach(key -> {
            List<Reaction> onLine = byKey.getOrDefault(key, List.of());
            result.put(key, Arrays.stream(Reaction.Kind.values())
                    .map(kind -> new Tally(kind,
                            onLine.stream().filter(reaction -> reaction.getKind() == kind).count(),
                            onLine.stream().anyMatch(reaction -> reaction.getKind() == kind && me.equals(reaction.getAccountId()))))
                    .toList());
        });
        return result;
    }

    /** Adds the reader's reaction, or takes it back if it was there; the line's tallies after. */
    @Transactional
    public List<Tally> toggle(final String key, final Reaction.Kind kind) {
        if (!KEY.matcher(key).matches()) {
            throw new IllegalArgumentException("Not a news line: " + key);
        }
        Long me = currentAccountId();
        reactionRepository.findByAccountIdAndItemKeyAndKind(me, key, kind).ifPresentOrElse(
                reaction -> {
                    reactionRepository.delete(reaction);
                    log.info("Account {} took back {} on {}", me, kind, key);
                },
                () -> {
                    reactionRepository.save(Reaction.builder()
                            .accountId(me)
                            .itemKey(key)
                            .kind(kind)
                            .createdDate(LocalDateTime.now())
                            .build());
                    log.info("Account {} reacted {} on {}", me, kind, key);
                });
        reactionRepository.flush();
        return tallies(List.of(key)).get(key);
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }
}
