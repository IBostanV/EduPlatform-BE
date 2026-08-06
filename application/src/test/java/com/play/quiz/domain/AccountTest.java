package com.play.quiz.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class AccountTest {
    private static final String GETTER = "get";
    private static final String SETTER = "set";
    // A boolean already named "isX" is read as isX(), never getIsX().
    private static final List<String> GETTERS_TO_SKIP = List.of(
            "isEnabled", "isBlocked");

    @Test
    void given_account_then_all_fields_have_setters_getters() {
        Class<Account> clazz = Account.class;

        Set<String> fields = getAllFields(clazz);
        Set<String> getters = getAllMethodsStartingWith(clazz, GETTER);
        Set<String> setters = getAllMethodsStartingWith(clazz, SETTER);

        for (String field : fields) {
            String setter = SETTER + capitalize(field);
            assertTrue(setters.contains(setter), "Missing setter for field: " + field);

            if (GETTERS_TO_SKIP.contains(field)) {
                continue;
            }

            String getter = GETTER + capitalize(field);
            assertTrue(getters.contains(getter), "Missing getter for field: " + field);
        }
    }

    private static Set<String> getAllFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

    private static Set<String> getAllMethodsStartingWith(Class<?> clazz, String prefix) {
        return Arrays.stream(clazz.getDeclaredMethods()).map(Method::getName)
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toSet());
    }

    private static String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
