package com.play.quiz.repository;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Language;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    Account save(final Account account);

    Optional<Account> findUserByEmail(final String email);

    List<Account> findAll();

    void enableAccount(final Long accountId);

    int updateUserPassword(String userEmail, char[] password);

    Boolean changeLanguage(Language language, String username);

    Set<Account> findByUserIds(Set<Long> userIdList);
}
