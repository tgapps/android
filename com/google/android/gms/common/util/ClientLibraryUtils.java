package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.google.android.gms.common.wrappers.Wrappers;

public class ClientLibraryUtils {
    public static boolean isPackageStopped(Context context, String str) {
        "com.google.android.gms".equals(str);
        try {
            return (Wrappers.packageManager(context).getApplicationInfo(str, 0).flags & 2097152) != 0;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
