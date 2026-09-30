package com.play.quiz.util;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Glossary values read as numbers, for the quiz types played on them (values range, in order).
 * Thousands may be grouped with spaces or commas ("68 000 000", "1,500"); the decimal mark is a dot.
 * Same shape as the REGEXP_LIKE in QuestionRepository.getByCategoryAndParams.
 */
public final class Numbers {
    private static final Pattern NUMBER = Pattern.compile("^-?[0-9][0-9 ,]*(\\.[0-9]+)?$");

    private Numbers() {
    }

    /** The value as a number, or null when it is not one. */
    public static Double parse(final String value) {
        if (Objects.isNull(value) || !NUMBER.matcher(value.trim()).matches()) return null;
        return Double.parseDouble(value.trim().replaceAll("[ ,]", ""));
    }

    /** 1500.0 as "1500", 2.5 as "2.5". */
    public static String format(final double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
}
