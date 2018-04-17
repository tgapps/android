package net.hockeyapp.android.utils;

import android.content.Context;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;

public class PermissionsUtil {
    public static int[] permissionsState(Context context, String... permissions) {
        if (permissions == null) {
            return null;
        }
        int[] state = new int[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            state[i] = context.checkCallingOrSelfPermission(permissions[i]);
        }
        return state;
    }

    public static boolean permissionsAreGranted(int[] permissionsState) {
        for (int permissionState : permissionsState) {
            if (permissionState != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isUnknownSourcesEnabled(Context context) {
        if (VERSION.SDK_INT >= 26) {
            boolean z;
            if (context.getApplicationInfo().targetSdkVersion >= 26) {
                if (!context.getPackageManager().canRequestPackageInstalls()) {
                    z = false;
                    return z;
                }
            }
            z = true;
            return z;
        } else if (VERSION.SDK_INT < 17 || VERSION.SDK_INT >= 21) {
            return "1".equals(Secure.getString(context.getContentResolver(), "install_non_market_apps"));
        } else {
            return "1".equals(Global.getString(context.getContentResolver(), "install_non_market_apps"));
        }
    }
}
