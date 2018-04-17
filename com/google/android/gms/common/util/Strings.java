package com.google.android.gms.common.util;

import java.util.regex.Pattern;

public class Strings {
    private static final Pattern zzaak = Pattern.compile("\\$\\{(.*?)\\}");

    public static boolean isEmptyOrWhitespace(String str) {
        if (str != null) {
            if (!str.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
