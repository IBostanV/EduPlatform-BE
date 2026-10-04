package com.play.quiz.domain;

import com.play.quiz.domain.helpers.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * An error a player's browser hit and sent in. Whose browser and when are the audited CREATED_BY
 * (empty for a guest) and CREATED_DATE.
 */
@Entity
@Table(name = "Q_CLIENT_ERROR")
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientError extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_error_generator")
    @SequenceGenerator(name = "client_error_generator", sequenceName = "client_error_seq", allocationSize = 1)
    @Column(name = "CLIENT_ERROR_ID")
    private Long clientErrorId;

    private String kind;

    private String message;

    @ToString.Exclude
    private String stack;

    private String page;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @Override
    public Long getId() {
        return this.clientErrorId;
    }
}
