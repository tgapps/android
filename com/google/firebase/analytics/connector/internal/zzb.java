package com.google.firebase.analytics.connector.internal;

import android.os.Bundle;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import java.util.Arrays;
import java.util.List;

public final class zzb {
    private static final List<String> zzboj = Arrays.asList(new String[]{"_e", "_f", "_iap", "_s", "_au", "_ui", "_cd", "app_open"});
    private static final List<String> zzbok = Arrays.asList(new String[]{"auto", "app", "am"});
    private static final List<String> zzbol = Arrays.asList(new String[]{"_r", "_dbg"});
    private static final List<String> zzbom = Arrays.asList((String[]) ArrayUtils.concat(UserProperty.zzadb, UserProperty.zzadc));
    private static final List<String> zzbon = Arrays.asList(new String[]{"^_ltv_[A-Z]{3}$", "^_cc[1-5]{1}$"});

    public static boolean zza(String str, Bundle bundle) {
        if (zzboj.contains(str)) {
            return false;
        }
        if (bundle != null) {
            for (String containsKey : zzbol) {
                if (bundle.containsKey(containsKey)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean zzb(String str, String str2, Bundle bundle) {
        if (!"_cmp".equals(str2)) {
            return true;
        }
        if (!zzfd(str)) {
            return false;
        }
        if (bundle == null) {
            return false;
        }
        for (String containsKey : zzbol) {
            if (bundle.containsKey(containsKey)) {
                return false;
            }
        }
        Object obj = -1;
        switch (str.hashCode()) {
            case 101200:
                if (str.equals(AppMeasurement.FCM_ORIGIN)) {
                    obj = null;
                    break;
                }
                break;
            case 101230:
                if (str.equals("fdl")) {
                    int i = 1;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                bundle.putString("_cis", "fcm_integration");
                return true;
            case 1:
                bundle.putString("_cis", "fdl_integration");
                return true;
            default:
                return false;
        }
    }

    public static boolean zzfd(String str) {
        return !zzbok.contains(str);
    }

    public static boolean zzfe(String str) {
        if (zzbom.contains(str)) {
            return false;
        }
        for (String matches : zzbon) {
            if (str.matches(matches)) {
                return false;
            }
        }
        return true;
    }
}
