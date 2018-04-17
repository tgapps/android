package com.google.android.gms.common.wrappers;

import android.content.Context;
import com.google.android.gms.common.util.PlatformVersion;

public class InstantApps {
    private static Context zzaay;
    private static Boolean zzaaz;

    public static synchronized boolean isInstantApp(Context context) {
        synchronized (InstantApps.class) {
            Context applicationContext = context.getApplicationContext();
            boolean booleanValue;
            if (zzaay == null || zzaaz == null || zzaay != applicationContext) {
                Boolean valueOf;
                zzaaz = null;
                if (PlatformVersion.isAtLeastO()) {
                    valueOf = Boolean.valueOf(applicationContext.getPackageManager().isInstantApp());
                } else {
                    try {
                        context.getClassLoader().loadClass("com.google.android.instantapps.supervisor.InstantAppsRuntime");
                        zzaaz = Boolean.valueOf(true);
                    } catch (ClassNotFoundException e) {
                        valueOf = Boolean.valueOf(false);
                    }
                    zzaay = applicationContext;
                    booleanValue = zzaaz.booleanValue();
                    return booleanValue;
                }
                zzaaz = valueOf;
                zzaay = applicationContext;
                booleanValue = zzaaz.booleanValue();
                return booleanValue;
            }
            booleanValue = zzaaz.booleanValue();
            return booleanValue;
        }
    }
}
