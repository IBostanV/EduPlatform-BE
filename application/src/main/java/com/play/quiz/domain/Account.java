package com.play.quiz.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.play.quiz.domain.helpers.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import com.play.quiz.enums.ProfileVisibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Q_USER")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "users_seq", allocationSize = 1)
    private Long accountId;
    private String name;
    private String email;
    @ToString.Exclude
    private byte[] avatar;
    private String surname;
    private String username;
    @ToString.Exclude
    private char[] password;
    private LocalDate birthday;
    private Integer experience;

    @OneToOne(targetEntity = Language.class)
    @JoinColumn(name = "LANG_ID")
    private Language language;

    /** The trophy this player has chosen to show beside their name; a trophy code, or null. */
    @Column(name = "PREFERRED_TROPHY")
    private String preferredTrophy;

    // Visiting on consecutive days is worth experience and a trophy, so the run is kept here
    // rather than worked out from a log: one date and two counters answer it in a read.
    @Column(name = "LAST_SEEN_DATE")
    private LocalDate lastSeenDate;

    @Column(name = "LOGIN_STREAK")
    private int loginStreak;

    @Column(name = "BEST_STREAK")
    private int bestStreak;

    /** The site's currency: earned with experience, spent in the shop and in quizzes (com.play.quiz.coin). */
    @Column(name = "COINS")
    private int coins;

    /** Freezes bought and not yet used; each one covers a single missed day of the streak. */
    @Column(name = "STREAK_FREEZES")
    private int streakFreezes;

    /** Notifications from after this moment are unread; null means none has been read yet. */
    /** The cosmetics worn, by their com.play.quiz.cosmetic.Cosmetic code; null for none. */
    @Column(name = "EQUIPPED_FRAME")
    private String equippedFrame;

    @Column(name = "EQUIPPED_NAME_COLOR")
    private String equippedNameColor;

    /** Who may see this player's activity on their profile (history, posts, friends, groups…). */
    @Enumerated(EnumType.STRING)
    @Column(name = "PROFILE_VISIBILITY")
    @Builder.Default
    private ProfileVisibility profileVisibility = ProfileVisibility.FRIENDS;

    /** Whether the player has been through the site tour; it is shown until they have. */
    @Column(name = "TOUR_SEEN")
    private boolean tourSeen;

    @Column(name = "NOTIFICATIONS_READ_AT")
    private LocalDateTime notificationsReadAt;

    /** News kinds the player switched off, comma-separated FeedItem.Type names; null for none. */
    @Column(name = "HIDDEN_NEWS")
    private String hiddenNews;

    /** The player's own look of the site, as JSON (appearance.Appearance); null for the site as it comes. */
    @Column(name = "APPEARANCE")
    private String appearance;

    /** The chat group this player plays Conquest for; null for playing on their own. */
    @Column(name = "CONQUEST_TEAM")
    private Long conquestTeam;

    /** Whether express quizzes lean to this player's occupations (QuestionRepository.findOccupationQuestions). */
    @Column(name = "OCCUPATION_QUIZZES")
    @Builder.Default
    private boolean occupationQuizzes = true;

    @Column(name = "IS_ENABLED")
    private boolean isEnabled;

    // Shut out by an admin, which is not IS_ENABLED: that one only says whether the verification
    // email was answered, and unblocking must not verify an address nobody ever confirmed.
    @Column(name = "IS_BLOCKED")
    private boolean isBlocked;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "Q_USER_ROLES",
            joinColumns = @JoinColumn(name = "ACCOUNT_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    @ToString.Exclude
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "Q_USER_CATEGORY",
            joinColumns = @JoinColumn(name = "ACCOUNT_ID"),
            inverseJoinColumns = @JoinColumn(name = "CAT_ID"))
    @ToString.Exclude
    private Set<Category> favoriteCategories = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "Q_USER_OCCUPATION",
            joinColumns = @JoinColumn(name = "ACCOUNT_ID"),
            inverseJoinColumns = @JoinColumn(name = "OCCUPATION_ID"))
    @ToString.Exclude
    private Set<UserOccupation> occupations = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "Q_USER_FRIEND",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "FRIEND_ID"))
    @ToString.Exclude
    private Set<Account> friends = new HashSet<>();

    public void enable() { this.isEnabled = true; }

    public void setIsEnabled(final boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    // Named for the column, like setIsEnabled: BeanPropertyRowMapper reads IS_BLOCKED through it.
    public void setIsBlocked(final boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    @Override
    public Long getId() { return this.accountId; }
}
