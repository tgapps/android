package com.google.android.gms.common.util;

import android.os.Build.VERSION;

public final class PlatformVersion {
    public static boolean isAtLeastIceCreamSandwich() {
        return true;
    }

    public static boolean isAtLeastIceCreamSandwichMR1() {
        return VERSION.SDK_INT >= 15;
    }

    public static boolean isAtLeastJellyBean() {
        return VERSION.SDK_INT >= 16;
    }

    public static boolean isAtLeastJellyBeanMR2() {
        return VERSION.SDK_INT >= 18;
    }

    public static boolean isAtLeastKitKat() {
        return VERSION.SDK_INT >= 19;
    }

    public static boolean isAtLeastKitKatWatch() {
        return VERSION.SDK_INT >= 20;
    }

    public static boolean isAtLeastLollipop() {
        return VERSION.SDK_INT >= 21;
    }

    public static boolean isAtLeastN() {
        return VERSION.SDK_INT >= 24;
    }

    public static boolean isAtLeastO() {
        return VERSION.SDK_INT >= 26;
    }
}
