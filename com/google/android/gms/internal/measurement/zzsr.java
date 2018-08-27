package com.google.android.gms.internal.measurement;

import android.util.Log;

final class zzsr extends zzsl<Boolean> {
    zzsr(zzsv com_google_android_gms_internal_measurement_zzsv, String str, Boolean bool) {
        super(com_google_android_gms_internal_measurement_zzsv, str, bool);
    }

    protected final /* synthetic */ Object zzfj(String str) {
        if (zzsg.zzbqe.matcher(str).matches()) {
            return Boolean.valueOf(true);
        }
        if (zzsg.zzbqf.matcher(str).matches()) {
            return Boolean.valueOf(false);
        }
        String str2 = this.zzbrc;
        Log.e("PhenotypeFlag", new StringBuilder((String.valueOf(str2).length() + 28) + String.valueOf(str).length()).append("Invalid boolean value for ").append(str2).append(": ").append(str).toString());
        return null;
    }
}
