package com.play.quiz.util;

import com.play.quiz.dto.QuestionDto;
import com.play.quiz.enums.QuestionType;
import com.play.quiz.exception.AccountDisabledException;
import com.play.quiz.exception.DuplicateUserException;
import com.play.quiz.exception.IllegalTemplateQuestionException;
import org.springframework.util.Assert;

import java.util.Objects;

public abstract class SystemAssert extends Assert {

    public static void isAccountEnabled(boolean isEnabled, String userEmail) {
        if (!isEnabled) {
            throw new AccountDisabledException(ServerText.t("err_account_disabled", "Account {{email}} is disabled", "email", userEmail));
        }
    }

    // Blocked by an admin, which the owner must be able to tell from an unverified address: this
    // one is not something they can put right themselves.
    public static void isAccountNotBlocked(boolean isBlocked, String userEmail) {
        if (isBlocked) {
            throw new AccountDisabledException(ServerText.t("err_account_blocked", "Account {{email}} has been blocked by an administrator", "email", userEmail));
        }
    }

    public static void isAccountUnique(boolean userExists, String userEmail) {
        if (userExists) {
            throw new DuplicateUserException(ServerText.t("err_user_exists", "User {{email}} already exists", "email", userEmail));
        }
    }

    public static void isTemplateQuestion(final QuestionDto questionDto) {
        if (!Objects.equals(QuestionType.TEMPLATE, questionDto.getType())) {
            throw new IllegalTemplateQuestionException(ServerText.t("err_question_not_template", "Question [{{id}}] is not of type TEMPLATE", "id", questionDto.getId()));
        }
    }
}
