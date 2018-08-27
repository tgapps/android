package com.google.android.gms.measurement.internal;

final class zzbx implements Runnable {
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ zzl zzaqp;

    zzbx(zzbv com_google_android_gms_measurement_internal_zzbv, zzl com_google_android_gms_measurement_internal_zzl, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzaqo = com_google_android_gms_measurement_internal_zzbv;
        this.zzaqp = com_google_android_gms_measurement_internal_zzl;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzc(this.zzaqp, this.zzaqn);
    }
}
