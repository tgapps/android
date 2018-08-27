package com.google.android.gms.measurement.internal;

final class zzbw implements Runnable {
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;

    zzbw(zzbv com_google_android_gms_measurement_internal_zzbv, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzaqo = com_google_android_gms_measurement_internal_zzbv;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zze(this.zzaqn);
    }
}
