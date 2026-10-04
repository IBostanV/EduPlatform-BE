package com.play.quiz.repository;

import com.play.quiz.enums.ProfileVisibility;
import com.play.quiz.domain.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT f FROM Account a JOIN a.friends f WHERE a.accountId = :userId ORDER BY f.username")
    List<Account> findFriends(Long userId);

    Optional<Account> findByEmail(String email);

    // Another player's profile page reads these after the transaction is over (open-in-view is
    // off), so they come in the same query instead of as lazy proxies.
    @EntityGraph(attributePaths = {"occupations", "favoriteCategories"})
    Optional<Account> findProfileByAccountId(Long accountId);

    // The admin user list, with the roles in the same query rather than one SELECT per account.
    // No DISTINCT: Oracle refuses SELECT DISTINCT over the BLOB avatar column (ORA-00932).
    // Hibernate 6 already de-duplicates fetch-joined roots, so the repeated rows never surface.
    // ponytail: loads every account at once, avatars included; page it when the site outgrows
    // a single screenful of admins scrolling one table.
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.roles ORDER BY a.email")
    List<Account> findAllWithRoles();

    // One statement, so two quizzes finishing at once cannot read the same total and each write
    // their own back. COALESCE because EXPERIENCE is null on every account that predates this.
    @Modifying
    @Query("UPDATE Account a SET a.experience = COALESCE(a.experience, 0) + :amount WHERE a.accountId = :accountId")
    void addExperience(Long accountId, int amount);

    // Transactional for callers that have none of their own (a pairs win, paid from LiveService).
    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.coins = a.coins + :amount WHERE a.accountId = :accountId")
    void addCoins(Long accountId, int amount);

    /**
     * Takes {@code amount} coins if the player has them. The check and the write are one
     * statement, so two purchases at once cannot both spend the same coins. Returns 1 if paid.
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.coins = a.coins - :amount WHERE a.accountId = :accountId AND a.coins >= :amount")
    int spendCoins(Long accountId, int amount);

    /** One more streak freeze, unless the player already holds {@code max}. Returns 1 if added. */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.streakFreezes = a.streakFreezes + 1 WHERE a.accountId = :accountId AND a.streakFreezes < :max")
    int addStreakFreeze(Long accountId, int max);

    /** Uses {@code days} freezes to cover missed days, if there are that many. Returns 1 if used. */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.streakFreezes = a.streakFreezes - :days WHERE a.accountId = :accountId AND a.streakFreezes >= :days")
    int useStreakFreezes(Long accountId, int days);

    @Query("SELECT a.coins FROM Account a WHERE a.accountId = :accountId")
    int findCoins(Long accountId);

    /**
     * Sets the trophy shown beside a player's name.
     *
     * <p>One column, one statement, rather than saving the account: the account handed round this
     * application is assembled by hand from JDBC (UserRepositoryImpl), so merging it would write
     * its half-built favourite categories back over the real rows — and Oracle rightly refuses to
     * set their CREATED_DATE to null.
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.preferredTrophy = :code WHERE a.accountId = :accountId")
    void setPreferredTrophy(Long accountId, String code);

    /**
     * Marks a player as seen today, with the run of days they are on.
     *
     * <p>Conditional and in one statement, so the first request of the day writes the visit and
     * every other one that morning does nothing: two tabs opening together must not each pay for
     * the same day. Returns 1 to whichever got there first.
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.lastSeenDate = :today, a.loginStreak = :streak,"
            + " a.bestStreak = CASE WHEN a.bestStreak < :streak THEN :streak ELSE a.bestStreak END"
            + " WHERE a.accountId = :accountId AND (a.lastSeenDate IS NULL OR a.lastSeenDate < :today)")
    int recordVisit(Long accountId, LocalDate today, int streak);

    /**
     * The categories this player marked as favourites and every category under them, for the wiki
     * notifications: an article in "Europe" is news to somebody who follows "Geography".
     *
     * <p>Numbers rather than longs: Oracle hands a native NUMERIC back as a BigDecimal.
     */
    @Query(nativeQuery = true, value = """
            WITH RECURSIVE tree (CAT_ID) AS (
                SELECT c.CAT_ID FROM Q_CATEGORY c
                WHERE c.CAT_ID IN (SELECT uc.CAT_ID FROM Q_USER_CATEGORY uc WHERE uc.ACCOUNT_ID = :accountId)
                UNION
                SELECT c.CAT_ID FROM Q_CATEGORY c JOIN tree t ON c.SUBCATEGORY_ID = t.CAT_ID
            )
            SELECT CAT_ID FROM tree
            """)
    List<Number> findFavoriteCategoryTreeIds(Long accountId);

    @Query("SELECT a.notificationsReadAt FROM Account a WHERE a.accountId = :accountId")
    LocalDateTime findNotificationsReadAt(Long accountId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.notificationsReadAt = :readAt WHERE a.accountId = :accountId")
    void setNotificationsReadAt(Long accountId, LocalDateTime readAt);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.equippedFrame = :code WHERE a.accountId = :accountId")
    void setEquippedFrame(Long accountId, String code);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.equippedNameColor = :code WHERE a.accountId = :accountId")
    void setEquippedNameColor(Long accountId, String code);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.profileVisibility = :visibility WHERE a.accountId = :accountId")
    void setProfileVisibility(Long accountId, ProfileVisibility visibility);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.tourSeen = true WHERE a.accountId = :accountId")
    void markTourSeen(Long accountId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.conquestTeam = :groupId WHERE a.accountId = :accountId")
    void setConquestTeam(Long accountId, Long groupId);

    /** Every active player with some experience or a streak: the level and streak leaderboards. */
    @Query(nativeQuery = true, value = """
            SELECT ACCOUNT_ID AS accountId, COALESCE(EXPERIENCE, 0) AS experience, COALESCE(BEST_STREAK, 0) AS bestStreak
            FROM Q_USER
            WHERE NOT COALESCE(IS_BLOCKED, FALSE) AND (COALESCE(EXPERIENCE, 0) > 0 OR COALESCE(BEST_STREAK, 0) > 0)
            """)
    List<PlayerStanding> findStandings();

    interface PlayerStanding {
        Long getAccountId();
        Long getExperience();
        Long getBestStreak();
    }

    @Query("SELECT a.hiddenNews FROM Account a WHERE a.accountId = :accountId")
    String findHiddenNews(Long accountId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.hiddenNews = :hiddenNews WHERE a.accountId = :accountId")
    void setHiddenNews(Long accountId, String hiddenNews);

    @Query("SELECT a.occupationQuizzes FROM Account a WHERE a.email = :email")
    boolean findOccupationQuizzes(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.occupationQuizzes = :enabled WHERE a.email = :email")
    void setOccupationQuizzes(String email, boolean enabled);

    @Query("SELECT a.appearance FROM Account a WHERE a.accountId = :accountId")
    String findAppearance(Long accountId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Account a SET a.appearance = :appearance WHERE a.accountId = :accountId")
    void setAppearance(Long accountId, String appearance);

    /** Read straight after {@link #addExperience}, in its transaction, to see whether a level was crossed. */
    @Query("SELECT a.experience FROM Account a WHERE a.accountId = :accountId")
    Integer findExperience(Long accountId);

    // Native, not through Account.friends: the join table's CREATED_DATE is NOT NULL and a
    // @ManyToMany insert cannot fill it. NOT EXISTS keeps a repeated add from hitting the PK.
    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO Q_USER_FRIEND (USER_ID, FRIEND_ID, CREATED_BY, CREATED_DATE)
            SELECT :userId, :friendId, :userId, LOCALTIMESTAMP
            WHERE NOT EXISTS (SELECT 1 FROM Q_USER_FRIEND WHERE USER_ID = :userId AND FRIEND_ID = :friendId)
            """)
    void addFriend(Long userId, Long friendId);

    // Friendship is mutual, so both directions go.
    @Modifying
    @Query(nativeQuery = true, value = """
            DELETE FROM Q_USER_FRIEND
            WHERE (USER_ID = :userId AND FRIEND_ID = :friendId) OR (USER_ID = :friendId AND FRIEND_ID = :userId)
            """)
    void removeFriend(Long userId, Long friendId);
}
