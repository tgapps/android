package com.google.android.gms.common.wrappers;

import android.content.Context;

public class Wrappers {
    private static Wrappers zzabb = new Wrappers();
    private PackageManagerWrapper zzaba = null;

    public static PackageManagerWrapper packageManager(Context context) {
        return zzabb.getPackageManagerWrapper(context);
    }

    public synchronized PackageManagerWrapper getPackageManagerWrapper(Context context) {
        if (this.zzaba == null) {
            if (context.getApplicationContext() != null) {
                context = context.getApplicationContext();
            }
            this.zzaba = new PackageManagerWrapper(context);
        }
        return this.zzaba;
    }
}
