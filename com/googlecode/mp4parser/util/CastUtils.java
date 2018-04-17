package com.googlecode.mp4parser.util;

public class CastUtils {
    public static int l2i(long l) {
        if (l <= 2147483647L) {
            if (l >= -2147483648L) {
                return (int) l;
            }
        }
        StringBuilder stringBuilder = new StringBuilder("A cast to int has gone wrong. Please contact the mp4parser discussion group (");
        stringBuilder.append(l);
        stringBuilder.append(")");
        throw new RuntimeException(stringBuilder.toString());
    }
}
