package com.google.android.gms.measurement.internal;

final class zzch implements Runnable {
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzad zzaqr;

    zzch(zzbv com_google_android_gms_measurement_internal_zzbv, zzad com_google_android_gms_measurement_internal_zzad, String str) {
        this.zzaqo = com_google_android_gms_measurement_internal_zzbv;
        this.zzaqr = com_google_android_gms_measurement_internal_zzad;
        this.zzaqq = str;
    }

    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzc(this.zzaqr, this.zzaqq);
    }
}
