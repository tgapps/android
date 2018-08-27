package com.google.android.gms.measurement.internal;

final class zzca implements Runnable {
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ zzl zzaqp;

    zzca(zzbv com_google_android_gms_measurement_internal_zzbv, zzl com_google_android_gms_measurement_internal_zzl) {
        this.zzaqo = com_google_android_gms_measurement_internal_zzbv;
        this.zzaqp = com_google_android_gms_measurement_internal_zzl;
    }

    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zze(this.zzaqp);
    }
}
