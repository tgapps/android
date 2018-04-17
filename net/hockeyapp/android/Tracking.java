package net.hockeyapp.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Tracking {
    public static long getUsageTime(Context context) {
        if (!checkVersion(context)) {
            return 0;
        }
        SharedPreferences preferences = getPreferences(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("usageTime");
        stringBuilder.append(Constants.APP_VERSION);
        long sum = preferences.getLong(stringBuilder.toString(), 0);
        if (sum >= 0) {
            return sum / 1000;
        }
        Editor edit = preferences.edit();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("usageTime");
        stringBuilder2.append(Constants.APP_VERSION);
        edit.remove(stringBuilder2.toString()).apply();
        return 0;
    }

    private static boolean checkVersion(Context context) {
        if (Constants.APP_VERSION == null) {
            Constants.loadFromContext(context);
            if (Constants.APP_VERSION == null) {
                return false;
            }
        }
        return true;
    }

    protected static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("HockeyApp", 0);
    }
}
