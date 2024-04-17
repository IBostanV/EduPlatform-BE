package com.play.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.play.quiz.domain.Language;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    private String theme;
    private byte[] avatar;
    private String surname;
    private String username;
    private boolean isEnabled;
    private Integer experience;
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
}
