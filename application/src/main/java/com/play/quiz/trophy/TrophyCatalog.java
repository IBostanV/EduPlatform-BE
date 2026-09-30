package com.play.quiz.trophy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.play.quiz.domain.Category;
import com.play.quiz.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Every trophy the app knows about: the fixed ones, written out here, and one per category, built
 * from the categories themselves.
 *
 * <p>Category trophies are made rather than seeded so that a category added next month brings its
 * trophy with it, instead of waiting for somebody to remember a migration. Their code carries the
 * category id, which is what ties an earned row back to a category that may since have been
 * renamed.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class TrophyCatalog {

    /** What a category trophy's code starts with; the rest is the category id. */
    static final String CATEGORY_PREFIX = "CATEGORY_";

    private final CategoryRepository categoryRepository;

    /** The fixed trophies, in the order they are shown. */
    private static final List<TrophyDefinition> FIXED = List.of(
            // Quizzes played: the ladder everybody is on from their first run.
            quizzes("FIRST_QUIZ", "First steps", "Finish your first quiz", "flag", 1),
            quizzes("TEN_QUIZZES", "Getting the hang of it", "Finish 10 quizzes", "medal", 10),
            quizzes("FIFTY_QUIZZES", "Regular", "Finish 50 quizzes", "award", 50),
            quizzes("HUNDRED_QUIZZES", "Centurion", "Finish 100 quizzes", "trophy", 100),
            quizzes("FIVE_HUNDRED_QUIZZES", "Encyclopaedia", "Finish 500 quizzes", "crown", 500),
            new TrophyDefinition("QUICK_OFF_THE_MARK", TrophyGroup.QUIZZES, "Quick off the mark",
                    "Finish a quiz of 10 questions or more in under a minute", "bolt", false, 1, null,
                    PlayerStanding::swiftRuns),

            // Conquest: holding a country means winning a round, not merely entering one.
            new TrophyDefinition("CONQUEROR", TrophyGroup.CONQUEST, "Conqueror",
                    "Hold a country when a conquest round closes", "globe", false, 1, null,
                    PlayerStanding::countriesHeld),
            new TrophyDefinition("WARLORD", TrophyGroup.CONQUEST, "Warlord",
                    "Conquer 5 countries", "globe", false, 5, null, PlayerStanding::countriesHeld),
            new TrophyDefinition("EMPEROR", TrophyGroup.CONQUEST, "Emperor",
                    "Conquer 10 countries", "crown", false, 10, null, PlayerStanding::countriesHeld),

            // The IQ test.
            new TrophyDefinition("MEASURED", TrophyGroup.MIND, "Measured",
                    "Sit the IQ test through to the end", "brain", false, 1, null,
                    PlayerStanding::iqTests),

            // Turning up, day after day.
            new TrophyDefinition("CLEAN_SWEEP", TrophyGroup.DEVOTION, "Clean sweep",
                    "Finish all of a day's tasks in one day", "award", false, 1, null,
                    PlayerStanding::fullTaskDays),
            // Playing every day is a harder thing than looking in every day, and its own trophy.
            new TrophyDefinition("PLAY_STREAK_7", TrophyGroup.DEVOTION, "Seven days of play",
                    "Finish a quiz on 7 days in a row", "fire", false, 7, null,
                    standing -> standing.playStreak()),
            streak("STREAK_3", "Three in a row", 3),
            streak("STREAK_7", "A full week", 7),
            streak("STREAK_30", "A month of days", 30),

            // Secret: no name and no condition until it is earned, so they are found rather
            // than worked through.
            new TrophyDefinition("FLAWLESS", TrophyGroup.SECRET, "Flawless",
                    "Finish a quiz without a single wrong answer", "star", true, 1, null,
                    PlayerStanding::flawlessRuns),
            new TrophyDefinition("NIGHT_OWL", TrophyGroup.SECRET, "Night owl",
                    "Finish a quiz between two and five in the morning", "moon", true, 1, null,
                    PlayerStanding::nightRuns),
            new TrophyDefinition("SHARP", TrophyGroup.SECRET, "Sharp",
                    "Score 130 or more on the IQ test", "brain", true, 130, null,
                    standing -> standing.bestIq()),
            new TrophyDefinition("STREAK_100", TrophyGroup.SECRET, "Unbroken",
                    "Visit on 100 days in a row", "fire", true, 100, null,
                    standing -> standing.bestStreak())
    );

    /**
     * What one trophy looks like, by its code — the little that is needed to draw it beside a
     * player's name.
     *
     * <p>A fixed trophy is answered from the list above without touching the database; only a
     * category trophy costs a read, and only for players who have chosen one.
     */
    public Optional<TrophyFace> faceOf(final String code) {
        if (Objects.isNull(code) || code.isBlank()) {
            return Optional.empty();
        }
        if (code.startsWith(CATEGORY_PREFIX)) {
            return categoryId(code)
                    .flatMap(categoryRepository::findById)
                    .map(category -> new TrophyFace(code, category.getName(), "category", category.getCatId()));
        }

        return FIXED.stream()
                .filter(definition -> definition.code().equals(code))
                .findFirst()
                .map(definition -> new TrophyFace(code, definition.title(), definition.icon(), null));
    }

    private static Optional<Long> categoryId(final String code) {
        try {
            return Optional.of(Long.parseLong(code.substring(CATEGORY_PREFIX.length())));
        } catch (NumberFormatException exception) {
            // A code from an older shape of this list; nothing to draw for it.
            log.warn("Unreadable category trophy code {}, drawing nothing", code);
            return Optional.empty();
        }
    }

    /** Enough of a trophy to draw it: its name, its icon, and the category it wears, if any. */
    public record TrophyFace(String code, String title, String icon, Long categoryId) {
    }

    /** Every trophy there is, for this app as it stands today. */
    public List<TrophyDefinition> all() {
        List<Category> categories = categoryRepository.findAllActive(Sort.by("name"));

        List<TrophyDefinition> definitions = new ArrayList<>(FIXED);
        // The capstone first, then the categories it is made of.
        definitions.add(everyCategory(categories));
        categories.stream().map(TrophyCatalog::forCategory).forEach(definitions::add);

        return List.copyOf(definitions);
    }

    /**
     * A quiz finished in every category there is.
     *
     * <p>Its target is however many categories there are today, so a category added next month
     * moves the post — for everyone still working toward it. Anyone who had already finished it
     * keeps it: the earned row is what says so, and rows are never taken back.
     */
    private static TrophyDefinition everyCategory(final List<Category> categories) {
        Set<Long> ids = categories.stream().map(Category::getCatId).collect(Collectors.toSet());

        return new TrophyDefinition("EVERY_CATEGORY", TrophyGroup.CATEGORIES, "Round the houses",
                "Finish a quiz in every category", "crown", false, Math.max(1, ids.size()), null,
                standing -> ids.stream().filter(id -> standing.inCategory(id) > 0).count());
    }

    /**
     * A category's own trophy: finish a quiz in it.
     *
     * <p>The title is the category's name, which is the point — the shelf reads as the places and
     * subjects somebody has been through, not as a list of identical medals.
     */
    private static TrophyDefinition forCategory(final Category category) {
        return new TrophyDefinition(CATEGORY_PREFIX + category.getCatId(), TrophyGroup.CATEGORIES,
                category.getName(), "Finish a quiz in " + category.getName(), "category", false, 1,
                category.getCatId(), standing -> standing.inCategory(category.getCatId()));
    }

    private static TrophyDefinition quizzes(final String code, final String title,
                                            final String description, final String icon, final long target) {
        return new TrophyDefinition(code, TrophyGroup.QUIZZES, title, description, icon, false, target,
                null, PlayerStanding::quizzes);
    }

    private static TrophyDefinition streak(final String code, final String title, final long days) {
        return new TrophyDefinition(code, TrophyGroup.DEVOTION, title,
                "Visit on " + days + " days in a row", "fire", false, days, null,
                // The best run ever, not the one running now: a trophy earned in March is not
                // taken away by a quiet April.
                standing -> standing.bestStreak());
    }
}
