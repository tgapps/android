package com.google.android.gms.internal.measurement;

import android.util.Log;

final class zzss extends zzsl<Double> {
    zzss(zzsv com_google_android_gms_internal_measurement_zzsv, String str, Double d) {
        super(com_google_android_gms_internal_measurement_zzsv, str, d);
    }

    private final Double zzfm(String str) {
        try {
            return Double.valueOf(Double.parseDouble(str));
        } catch (NumberFormatException e) {
            String str2 = this.zzbrc;
            Log.e("PhenotypeFlag", new StringBuilder((String.valueOf(str2).length() + 27) + String.valueOf(str).length()).append("Invalid double value for ").append(str2).append(": ").append(str).toString());
            return null;
        }
    }

    protected final /* synthetic */ Object zzfj(String str) {
        return zzfm(str);
    }
}
