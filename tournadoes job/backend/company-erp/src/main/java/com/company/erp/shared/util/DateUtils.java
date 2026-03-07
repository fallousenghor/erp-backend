package com.company.erp.shared.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    private DateUtils() {}

    public static long daysBetween(LocalDate from, LocalDate to) {
        return ChronoUnit.DAYS.between(from, to);
    }

    public static boolean isBusinessDay(LocalDate date) {
        return date.getDayOfWeek().getValue() < 6;
    }
}
