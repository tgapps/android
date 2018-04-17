package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.content.Context;

public final class DeviceProperties {
    private static Boolean zzzn;
    private static Boolean zzzo;
    private static Boolean zzzr;

    public static boolean isIoT(Context context) {
        if (zzzr == null) {
            boolean z;
            if (!context.getPackageManager().hasSystemFeature("android.hardware.type.iot")) {
                if (!context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                    z = false;
                    zzzr = Boolean.valueOf(z);
                }
            }
            z = true;
            zzzr = Boolean.valueOf(z);
        }
        return zzzr.booleanValue();
    }

    @TargetApi(21)
    public static boolean isSidewinder(Context context) {
        if (zzzo == null) {
            boolean z = PlatformVersion.isAtLeastLollipop() && context.getPackageManager().hasSystemFeature("cn.google");
            zzzo = Boolean.valueOf(z);
        }
        return zzzo.booleanValue();
    }

    @TargetApi(20)
    public static boolean isWearable(Context context) {
        if (zzzn == null) {
            boolean z = PlatformVersion.isAtLeastKitKatWatch() && context.getPackageManager().hasSystemFeature("android.hardware.type.watch");
            zzzn = Boolean.valueOf(z);
        }
        return zzzn.booleanValue();
    }

    @TargetApi(24)
    public static boolean isWearableWithoutPlayStore(Context context) {
        return (!PlatformVersion.isAtLeastN() || isSidewinder(context)) && isWearable(context);
    }
}
