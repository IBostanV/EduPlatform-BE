package com.play.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.play.quiz.domain.Language;
import com.play.quiz.record.PlayerLevel;
import com.play.quiz.trophy.TrophyCatalog;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import com.play.quiz.enums.ProfileVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String name;
    private byte[] avatar;
    private String surname;
    private String username;
    private boolean isEnabled;
    private Integer experience;
    private int loginStreak;
    private int bestStreak;
    // Read-only for the same reason as the level below: the profile form posts this shape back.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int coins;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int streakFreezes;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean tourSeen;
    // Changed through its own endpoint, not the profile form.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProfileVisibility profileVisibility;
    // What they wear, changed in the shop rather than on the profile form.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String equippedFrame;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String equippedNameColor;

    /** The colour their name is drawn in, ready to use; null for the usual one. */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getNameColor() {
        return com.play.quiz.cosmetic.Cosmetic.colorOf(equippedNameColor);
    }
    private String preferredTrophy;
    /** The chosen trophy as it is drawn, so the bar needs no second request to show it. */
    private TrophyCatalog.TrophyFace trophy;
    private Language language;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long updatedById;
    private String updatedByName;
    private Long createdById;
    private String createdByName;

    private Set<CategoryDto> favoriteCategories = Collections.emptySet();
    private Set<AccountDto> friends = Collections.emptySet();
    private Set<UserOccupationDto> occupations = Collections.emptySet();

    @NotBlank
    @Email(regexp = ".+@.+\\..+", message="Please provide a valid email address")
    private String email;

    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private char[] password;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthday;

    @Builder.Default
    private List<RoleDto> roles = Collections.emptyList();

    /**
     * What the experience above adds up to, so the browser draws the bar instead of working the
     * formula out a second time. Read-only: it is derived, and the profile form posts this same
     * shape back, which must not be a way to hand yourself a level.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public PlayerLevel getPlayerLevel() {
        return PlayerLevel.of(experience);
    }
}
