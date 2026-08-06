package com.play.quiz.record;

import java.util.List;

import com.play.quiz.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

// What an admin fills in when adding or editing an account. The password is only read when the
// account is created: an existing one keeps the password its owner chose, and this endpoint is
// not a way to take it over.
public record ManagedAccountInput(@NotBlank
                                  @Email(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
                                  String email,

                                  String displayName,

                                  char[] password,

                                  @NotEmpty(message = "must give the account at least one role")
                                  List<UserRole> roles) {
}
