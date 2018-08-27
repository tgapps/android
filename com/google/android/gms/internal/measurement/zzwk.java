package com.google.android.gms.internal.measurement;

final class zzwk implements zzws {
    private zzws[] zzcaq;

    zzwk(zzws... com_google_android_gms_internal_measurement_zzwsArr) {
        this.zzcaq = com_google_android_gms_internal_measurement_zzwsArr;
    }

    public final boolean zze(Class<?> cls) {
        for (zzws zze : this.zzcaq) {
            if (zze.zze(cls)) {
                return true;
            }
        }
        return false;
    }

    public final zzwr zzf(Class<?> cls) {
        for (zzws com_google_android_gms_internal_measurement_zzws : this.zzcaq) {
            if (com_google_android_gms_internal_measurement_zzws.zze(cls)) {
                return com_google_android_gms_internal_measurement_zzws.zzf(cls);
            }
        }
        String str = "No factory is available for message type: ";
        String valueOf = String.valueOf(cls.getName());
        throw new UnsupportedOperationException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }
}
