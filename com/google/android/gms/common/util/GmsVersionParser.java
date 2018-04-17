package com.google.android.gms.common.util;

import java.util.regex.Pattern;

public final class GmsVersionParser {
    private static Pattern zzzy = null;

    public static int parseBuildVersion(int i) {
        return i == -1 ? -1 : i / 1000;
    }
}
