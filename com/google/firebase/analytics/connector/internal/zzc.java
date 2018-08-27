package com.google.firebase.analytics.connector.internal;

import android.os.Bundle;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class zzc {
    private static final Set<String> zzbsm = new HashSet(Arrays.asList(new String[]{"_in", "_xa", "_xu", "_aq", "_aa", "_ai", "_ac", "campaign_details", "_ug", "_iapx", "_exp_set", "_exp_clear", "_exp_activate", "_exp_timeout", "_exp_expire"}));
    private static final List<String> zzbsn = Arrays.asList(new String[]{"_e", "_f", "_iap", "_s", "_au", "_ui", "_cd", "app_open"});
    private static final List<String> zzbso = Arrays.asList(new String[]{"auto", "app", "am"});
    private static final List<String> zzbsp = Arrays.asList(new String[]{"_r", "_dbg"});
    private static final List<String> zzbsq = Arrays.asList((String[]) ArrayUtils.concat(UserProperty.zzado, UserProperty.zzadp));
    private static final List<String> zzbsr = Arrays.asList(new String[]{"^_ltv_[A-Z]{3}$", "^_cc[1-5]{1}$"});

    public static boolean zzfo(String str) {
        return !zzbso.contains(str);
    }

    public static boolean zza(String str, Bundle bundle) {
        if (zzbsn.contains(str)) {
            return false;
        }
        if (bundle != null) {
            for (String containsKey : zzbsp) {
                if (bundle.containsKey(containsKey)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean zzy(String str, String str2) {
        if ("_ce1".equals(str2) || "_ce2".equals(str2)) {
            return str.equals("fcm") || str.equals("frc");
        } else {
            if ("_ln".equals(str2)) {
                return str.equals("fcm") || str.equals("fiam");
            } else {
                if (zzbsq.contains(str2)) {
                    return false;
                }
                for (String matches : zzbsr) {
                    if (str2.matches(matches)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public static boolean zzb(String str, String str2, Bundle bundle) {
        if (!"_cmp".equals(str2)) {
            return true;
        }
        if (!zzfo(str)) {
            return false;
        }
        if (bundle == null) {
            return false;
        }
        for (String containsKey : zzbsp) {
            if (bundle.containsKey(containsKey)) {
                return false;
            }
        }
        Object obj = -1;
        switch (str.hashCode()) {
            case 101200:
                if (str.equals("fcm")) {
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
            case 3142703:
                if (str.equals("fiam")) {
                    obj = 2;
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
            case 2:
                bundle.putString("_cis", "fiam_integration");
                return true;
            default:
                return false;
        }
    }
}
