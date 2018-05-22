package com.google.android.gms.internal.measurement;

import android.util.Log;

final class zzwx extends zzws<Integer> {
    zzwx(zzxc com_google_android_gms_internal_measurement_zzxc, String str, Integer num) {
        super(com_google_android_gms_internal_measurement_zzxc, str, num);
    }

    private final Integer zzfa(String str) {
        try {
            return Integer.valueOf(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            String str2 = this.zzbnh;
            Log.e("PhenotypeFlag", new StringBuilder((String.valueOf(str2).length() + 28) + String.valueOf(str).length()).append("Invalid integer value for ").append(str2).append(": ").append(str).toString());
            return null;
        }
    }

    protected final /* synthetic */ Object zzey(String str) {
        return zzfa(str);
    }
}
