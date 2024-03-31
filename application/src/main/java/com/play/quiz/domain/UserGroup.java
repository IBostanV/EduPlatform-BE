package com.play.quiz.domain;

import com.play.quiz.domain.helpers.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Q_USER_GROUP")
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_group_generator")
    @SequenceGenerator(name = "user_group_generator", sequenceName = "user_group_seq", allocationSize = 1)
    private Long id;
    @Column(name = "GROUP_ID")
    private Long groupId;
    private String name;
    private String theme;
    private boolean muted;

    @OneToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTICIPANT")
    private Account participant;
}
