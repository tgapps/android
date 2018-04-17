package com.stripe.android.util;

import com.stripe.android.time.Clock;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {
    public static boolean hasYearPassed(int year) {
        return normalizeYear(year) < Clock.getCalendarInstance().get(1);
    }

    public static boolean hasMonthPassed(int year, int month) {
        boolean z = true;
        if (hasYearPassed(year)) {
            return true;
        }
        Calendar now = Clock.getCalendarInstance();
        if (normalizeYear(year) != now.get(1) || month >= now.get(2) + 1) {
            z = false;
        }
        return z;
    }

    private static int normalizeYear(int year) {
        if (year >= 100 || year < 0) {
            return year;
        }
        String currentYear = String.valueOf(Clock.getCalendarInstance().get(1));
        String prefix = currentYear.substring(0, currentYear.length() - 2);
        return Integer.parseInt(String.format(Locale.US, "%s%02d", new Object[]{prefix, Integer.valueOf(year)}));
    }
}
