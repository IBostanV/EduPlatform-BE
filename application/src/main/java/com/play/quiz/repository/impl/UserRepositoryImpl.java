package com.play.quiz.repository.impl;

import com.play.quiz.domain.Account;
import com.play.quiz.domain.Category;
import com.play.quiz.domain.Language;
import com.play.quiz.domain.Role;
import com.play.quiz.domain.UserOccupation;
import com.play.quiz.domain.helpers.BaseEntity;
import com.play.quiz.enums.UserRole;
import com.play.quiz.exception.UserNotFoundException;
import com.play.quiz.record.UserAdditionalInfo;
import com.play.quiz.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ONE;
import static java.util.stream.Collectors.groupingBy;

@Log4j2
@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final String activateAccountSql;
    private final String findAllSql;
    private final String findByEmailSql;
    private final String findUserRolesSql;
    private final String findUserFavoriteCategoriesSql;
    private final String findUserOccupationsSql;
    private final String findUsersByIdsSql;
    private final String deleteFavoriteCategoriesSql;
    private final String deleteUserOccupationsSql;
    private final String saveSql;
    private final String saveFavoriteCategorySql;
    private final String saveUserRolesSql;
    private final String saveUserOccupationSql;
    private final String saveNoAvatarSql;
    private final String userSequenceNextVal;
    private final String updateUserPasswordSql;
    private final String updateUserLanguageSql;

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public static final String EMAIL = "email";
    public static final String ACCOUNT_ID = "accountId";
    public static final String ACCOUNT_IDS = "accountIds";

    @Override
    public Account save(final Account account) {
        return Objects.nonNull(account.getAccountId())
                ? handleUpdate(account)
                : handleInsert(account);
    }

    private Account handleInsert(final Account account) {
        final Long nextUserId = jdbcTemplate.queryForObject(userSequenceNextVal, Long.class);
        log.info("Execute INSERT USER with id: {}", nextUserId);
        return executeSave(nextUserId, account);
    }

    private Account handleUpdate(final Account account) {
        log.info("Execute UPDATE USER with id: {}", account.getAccountId());
        return executeSave(account.getAccountId(), account);
    }

    private Account executeSave(final Long accountId, final Account account) {
        int rows = namedJdbcTemplate.update(
                Objects.nonNull(account.getAvatar()) ? saveSql : saveNoAvatarSql,
                getProperties(accountId, account));
        if (rows == 0) {
            log.warn("Saving user with id: {} affected no rows", accountId);
        }

        if (Objects.isNull(account.getAccountId())) {
            Function<Role, Long> roleIdFunction = Role::getRoleId;
            saveUserAdditionalInfo(new UserAdditionalInfo<>(
                    account.getRoles(), saveUserRolesSql, accountId, "roleId", roleIdFunction));
        } else {
            deleteUncheckedFavoriteCategories(account);

            Function<Category, Long> categoryIdFnc = Category::getCatId;
            saveUserAdditionalInfo(new UserAdditionalInfo<>(
                    account.getFavoriteCategories(), saveFavoriteCategorySql, accountId, "catId", categoryIdFnc));

            deleteUncheckedOccupations(account);

            Function<UserOccupation, Long> occupationIdFunction = UserOccupation::getId;
            saveUserAdditionalInfo(new UserAdditionalInfo<>(
                    account.getOccupations(), saveUserOccupationSql, accountId, "occupationId", occupationIdFunction));
        }

        log.info("User with id: {} successfully saved", accountId);
        return account.toBuilder()
                .accountId(accountId)
                .build();
    }

    private void deleteUncheckedOccupations(Account account) {
        Set<UserOccupation> userOccupationsToDelete = handleUserOccupations(account);
        userOccupationsToDelete.removeAll(account.getOccupations());

        if (!userOccupationsToDelete.isEmpty()) {
            namedJdbcTemplate.update(deleteUserOccupationsSql, Map.of(
                    "ids", userOccupationsToDelete.stream().map(UserOccupation::getId).toList(),
                    "accountId", account.getAccountId()));
        }
    }

    private void deleteUncheckedFavoriteCategories(final Account account) {
        Set<Category> favoriteCategoriesToDelete = handleFavoriteCategories(account);
        favoriteCategoriesToDelete.removeAll(account.getFavoriteCategories());

        if (!favoriteCategoriesToDelete.isEmpty()) {
            namedJdbcTemplate.update(deleteFavoriteCategoriesSql, Map.of(
                    "ids", favoriteCategoriesToDelete.stream().map(Category::getCatId).toList(),
                    "accountId", account.getAccountId()));
        }
    }

    private <T extends Collection<?>, E extends BaseEntity> void saveUserAdditionalInfo(UserAdditionalInfo<T, E> info) {
        info.collection().forEach(object -> namedJdbcTemplate.update(
                info.sql(),
                Map.of(ACCOUNT_ID, info.accountId(), info.key(), info.entityIdFunction().apply((E) object))));
    }

    private Map<String, Object> getProperties(final Long accountId, final Account account) {
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(ACCOUNT_ID, accountId);
        propertyMap.put(EMAIL, account.getEmail());
        propertyMap.put("avatar", account.getAvatar());
        propertyMap.put("isEnabled", account.isEnabled());
        propertyMap.put("name", mapNull().apply(account.getName()));
        propertyMap.put("surname", mapNull().apply(account.getSurname()));
        propertyMap.put("birthday", mapNull().apply(account.getBirthday()));
        propertyMap.put("username", mapNull().apply(account.getUsername()));
        propertyMap.put("createdDate", account.getCreatedDate());
        propertyMap.put("updatedDate", account.getUpdatedDate());

        propertyMap.put("createdBy", Optional.ofNullable(account.getCreatedBy())
                .map(Account::getAccountId)
                        .orElse(null));

        propertyMap.put("updatedBy", Optional.ofNullable(account.getUpdatedBy())
                .map(Account::getAccountId)
                .orElse(null));

        propertyMap.put("language", Optional.ofNullable(account.getLanguage())
                .map(Language::getLangId)
                .orElse(null));

        propertyMap.put("password", Objects.nonNull(account.getPassword())
                ? String.copyValueOf(account.getPassword()) : null);
        return propertyMap;
    }

    private Function<Object, Object> mapNull() {
        return input -> Objects.nonNull(input) ? input : "";
    }

    @Override
    public Optional<Account> findUserByEmail(String email) {
        return executeQueryForObject(email)
                .map(this::setAccountRoles)
                .map(this::setFavoriteCategories)
                .map(this::setOccupation);
    }

    private Optional<Account> executeQueryForObject(String email) {
        try {
            AccountRowMapper accountRowMapper = new AccountRowMapper();
            Account account = namedJdbcTemplate.queryForObject(findByEmailSql, Map.of(EMAIL, email), accountRowMapper);
            log.debug("Found user with email: {}", email);

            return Optional.ofNullable(account);
        } catch (EmptyResultDataAccessException exception) {
            log.debug("No user found with email: {}", email);
            return Optional.empty();
        }
    }

    private Account setAccountRoles(final Account account) {
        List<Role> roles = handleRoles(account);
        log.debug("Found roles: {} for user: {}", roles, account.getEmail());
        account.setRoles(roles);
        return account;
    }

    private List<Role> handleRoles(final Account account) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ACCOUNT_ID, account.getAccountId());
        List<Map<String, Object>> rolesMap = namedJdbcTemplate.queryForList(findUserRolesSql, parameterSource);
        return getRoleList(rolesMap);
    }

    private static List<Role> getRoleList(final List<Map<String, Object>> rolesMap) {
        return rolesMap.stream()
                .map(UserRepositoryImpl::mapRole)
                .toList();
    }

    private static Role mapRole(final Map<String, Object> item) {
        return Role.builder()
                .roleId(((BigDecimal) item.get("ROLE_ID")).longValue())
                .name(UserRole.valueOf((String) item.get("NAME")))
                .build();
    }

    private Account setFavoriteCategories(final Account account) {
        Set<Category> favoriteCategories = handleFavoriteCategories(account);
        account.setFavoriteCategories(favoriteCategories);

        return account;
    }

    private Set<Category> handleFavoriteCategories(final Account account) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ACCOUNT_ID, account.getAccountId());
        List<Map<String, Object>> categories = namedJdbcTemplate.queryForList(
                findUserFavoriteCategoriesSql, parameterSource);

        return getFavoriteCategoriesList(categories);
    }

    private Set<Category> getFavoriteCategoriesList(List<Map<String, Object>> categories) {
        return categories.stream()
                .map(UserRepositoryImpl::mapCategory)
                .collect(Collectors.toSet());
    }

    private static Category mapCategory(final Map<String, Object> item) {
        return Category.builder()
                .catId(((BigDecimal) item.get("CAT_ID")).longValue())
                .name(((String) item.get("NAME")))
                .build();
    }

    private Account setOccupation(final Account account) {
        Set<UserOccupation> userOccupations = handleUserOccupations(account);
        account.setOccupations(userOccupations);

        return account;
    }

    private Set<UserOccupation> handleUserOccupations(final Account account) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ACCOUNT_ID, account.getAccountId());
        List<Map<String, Object>> occupations = namedJdbcTemplate.queryForList(findUserOccupationsSql, parameterSource);

        return getUserOccupationList(occupations);
    }

    private Set<UserOccupation> getUserOccupationList(List<Map<String, Object>> occupations) {
        return occupations.stream()
                .map(UserRepositoryImpl::mapOccupations)
                .collect(Collectors.toSet());
    }

    private static UserOccupation mapOccupations(final Map<String, Object> item) {
        return UserOccupation.builder()
                .id(((BigDecimal) item.get("ID")).longValue())
                .name(((String) item.get("NAME")))
                .domain((String)item.get("DOMAIN"))
                .status((String)item.get("STATUS"))
                .build();
    }

    @Override
    public List<Account> findAll() {
        List<Map<String, Object>> accountMap = jdbcTemplate.queryForList(findAllSql);
        return mapResultSetRows(accountMap);
    }

    @Override
    public void enableAccount(final Long accountId) {
        if (namedJdbcTemplate.update(activateAccountSql, Map.of(ACCOUNT_ID, accountId)) == 0) {
            log.warn("Activating account id: {} affected no rows", accountId);
        }
    }

    @Override
    public int updateUserPassword(String userEmail, char[] password) {
        return namedJdbcTemplate.update(updateUserPasswordSql,
                Map.of(EMAIL, userEmail,
                        "password", String.copyValueOf((char[]) mapNull().apply(password))));
    }

    private List<Account> mapResultSetRows(final List<Map<String, Object>> accountMap) {
        return accountMap.stream()
                .map(UserRepositoryImpl::mapAccount)
                .collect(groupingBy(Account::getAccountId))
                .values().stream()
                .map(UserRepositoryImpl::getAccount)
                .toList();
    }

    private static Account mapAccount(final Map<String, Object> accountRow) {
        return Account.builder()
                .birthday(getBirthday(accountRow))
                .name((String) accountRow.get("NAME"))
                .surname((String) accountRow.get("SURNAME"))
                .username((String) accountRow.get("USERNAME"))
                .email((String) accountRow.get(EMAIL.toUpperCase()))
                .isEnabled(ONE.equals((accountRow.get("IS_ENABLED"))))
                .accountId(((BigDecimal) accountRow.get("ACCOUNT_ID")).longValue())
                .roles(Collections.singletonList(getUserRole(accountRow)))
                .build();
    }

    private static Account getAccount(final List<Account> accountGroup) {
        return accountGroup.stream()
                .map(account -> {
                    account.setRoles(getRoles(accountGroup));
                    return account;
                })
                .findFirst()
                .orElse(new Account());
    }

    private static List<Role> getRoles(final List<Account> accountGroup) {
        return accountGroup.stream()
                .flatMap(account -> account.getRoles().stream())
                .toList();
    }

    private static LocalDate getBirthday(final Map<String, Object> accountRow) {
        return Optional.ofNullable(((Timestamp) accountRow.get("BIRTHDAY")))
                .map(Timestamp::toLocalDateTime)
                .map(LocalDateTime::toLocalDate)
                .orElse(null);
    }

    private static Role getUserRole(final Map<String, Object> accountRow) {
        return Role.builder()
                .roleId(((BigDecimal) accountRow.get("ROLE_ID")).longValue())
                .name(UserRole.valueOf(accountRow.get("ROLE_NAME").toString()))
                .build();
    }

    private static class AccountRowMapper implements RowMapper<Account> {

        @Override
        public Account mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            BeanPropertyRowMapper<Account> rowMapper = new BeanPropertyRowMapper<>(Account.class);
            Language language = new Language(resultSet.getLong("lang_id"),
                    resultSet.getString("lang_code"),
                    resultSet.getString("lang_name"));

            return Optional.ofNullable(rowMapper.mapRow(resultSet, rowNum))
                    .orElseThrow(() -> new UserNotFoundException("No user found for the given email"))
                    .toBuilder().language(language).build();
        }
    }

    @Override
    public Boolean changeLanguage(Language language, String email) {
        int result = namedJdbcTemplate.update(updateUserLanguageSql,
                Map.of("langId", language.getLangId(),
                        "email", email));
        if (result != 1) {
            log.warn("Changing language to {} for {} affected {} rows", language.getLangId(), email, result);
        }
        return Objects.equals(result, 1);
    }

    @Override
    public Set<Account> findByUserIds(Set<Long> userIdList) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ACCOUNT_IDS, userIdList);
        List<Map<String, Object>> nameValueMapList = namedJdbcTemplate.queryForList(findUsersByIdsSql, parameterSource);

        return getMappedUsersEntity(nameValueMapList);
    }

    private Set<Account> getMappedUsersEntity(List<Map<String, Object>> users) {
        return users.stream()
                .map(UserRepositoryImpl::mapUserFieldsToEntity)
                .collect(Collectors.toSet());
    }

    private static Account mapUserFieldsToEntity(final Map<String, Object> userFieldsMap) {
        return Account.builder()
                .accountId(((BigDecimal) userFieldsMap.get("ACCOUNT_ID")).longValue())
                .name(((String) userFieldsMap.get("NAME")))
                .email((String) userFieldsMap.get("EMAIL"))
                .username((String) userFieldsMap.get("USERNAME"))
                .build();
    }
}
