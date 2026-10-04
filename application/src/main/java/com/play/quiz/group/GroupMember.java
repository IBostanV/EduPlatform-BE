package com.play.quiz.group;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A player in a group, or asking to be: the owner has a row too, so membership is one table. */
@Entity
@Table(name = "Q_SOCIAL_GROUP_MEMBER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember {

    public enum Status { MEMBER, PENDING }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "social_group_member_generator")
    @SequenceGenerator(name = "social_group_member_generator", sequenceName = "social_group_member_seq", allocationSize = 1)
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @Column(name = "JOINED_DATE")
    private LocalDateTime joinedDate;
}
