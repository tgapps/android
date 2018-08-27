package com.google.android.gms.measurement.internal;

final class zzck implements Runnable {
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ zzfh zzaqs;

    zzck(zzbv com_google_android_gms_measurement_internal_zzbv, zzfh com_google_android_gms_measurement_internal_zzfh, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzaqo = com_google_android_gms_measurement_internal_zzbv;
        this.zzaqs = com_google_android_gms_measurement_internal_zzfh;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzb(this.zzaqs, this.zzaqn);
    }
}
