package com.play.quiz.domain;

import java.time.LocalDate;
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
import lombok.AllArgsConstructor;
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
    private String theme;
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

    @OneToOne(targetEntity = Trophy.class)
    @JoinColumn(name = "TROPHY_ID")
    private Trophy mainTrophy;

    @Column(name = "IS_ENABLED")
    private boolean isEnabled;

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

    @Override
    public Long getId() { return this.accountId; }
}
