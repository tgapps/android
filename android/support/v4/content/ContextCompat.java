package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.TypedValue;
import java.io.File;

public class ContextCompat {
    private static final Object sLock = new Object();
    private static TypedValue sTempValue;

    public static void startActivity(Context context, Intent intent, Bundle options) {
        if (VERSION.SDK_INT >= 16) {
            context.startActivity(intent, options);
        } else {
            context.startActivity(intent);
        }
    }

    public static File[] getExternalFilesDirs(Context context, String type) {
        if (VERSION.SDK_INT >= 19) {
            return context.getExternalFilesDirs(type);
        }
        return new File[]{context.getExternalFilesDir(type)};
    }

    public static File[] getExternalCacheDirs(Context context) {
        if (VERSION.SDK_INT >= 19) {
            return context.getExternalCacheDirs();
        }
        return new File[]{context.getExternalCacheDir()};
    }

    public static Drawable getDrawable(Context context, int id) {
        if (VERSION.SDK_INT >= 21) {
            return context.getDrawable(id);
        }
        if (VERSION.SDK_INT >= 16) {
            return context.getResources().getDrawable(id);
        }
        int resolvedId;
        synchronized (sLock) {
            if (sTempValue == null) {
                sTempValue = new TypedValue();
            }
            context.getResources().getValue(id, sTempValue, true);
            resolvedId = sTempValue.resourceId;
        }
        return context.getResources().getDrawable(resolvedId);
    }

    public static int getColor(Context context, int id) {
        if (VERSION.SDK_INT >= 23) {
            return context.getColor(id);
        }
        return context.getResources().getColor(id);
    }

    public static File getNoBackupFilesDir(Context context) {
        if (VERSION.SDK_INT >= 21) {
            return context.getNoBackupFilesDir();
        }
        return createFilesDir(new File(context.getApplicationInfo().dataDir, "no_backup"));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized java.io.File createFilesDir(java.io.File r4) {
        /*
        r0 = android.support.v4.content.ContextCompat.class;
        monitor-enter(r0);
        r1 = r4.exists();	 Catch:{ all -> 0x0036 }
        if (r1 != 0) goto L_0x0034;
    L_0x0009:
        r1 = r4.mkdirs();	 Catch:{ all -> 0x0036 }
        if (r1 != 0) goto L_0x0034;
    L_0x000f:
        r1 = r4.exists();	 Catch:{ all -> 0x0036 }
        if (r1 == 0) goto L_0x0017;
    L_0x0015:
        monitor-exit(r0);
        return r4;
    L_0x0017:
        r1 = "ContextCompat";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0036 }
        r2.<init>();	 Catch:{ all -> 0x0036 }
        r3 = "Unable to create files subdir ";
        r2.append(r3);	 Catch:{ all -> 0x0036 }
        r3 = r4.getPath();	 Catch:{ all -> 0x0036 }
        r2.append(r3);	 Catch:{ all -> 0x0036 }
        r2 = r2.toString();	 Catch:{ all -> 0x0036 }
        android.util.Log.w(r1, r2);	 Catch:{ all -> 0x0036 }
        r1 = 0;
        monitor-exit(r0);
        return r1;
    L_0x0034:
        monitor-exit(r0);
        return r4;
    L_0x0036:
        r4 = move-exception;
        monitor-exit(r0);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.content.ContextCompat.createFilesDir(java.io.File):java.io.File");
    }

    public static boolean isDeviceProtectedStorage(Context context) {
        if (VERSION.SDK_INT >= 24) {
            return context.isDeviceProtectedStorage();
        }
        return false;
    }
}
