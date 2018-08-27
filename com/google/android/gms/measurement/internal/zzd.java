package com.google.android.gms.measurement.internal;

final class zzd implements Runnable {
    private final /* synthetic */ zza zzafu;
    private final /* synthetic */ long zzafv;

    zzd(zza com_google_android_gms_measurement_internal_zza, long j) {
        this.zzafu = com_google_android_gms_measurement_internal_zza;
        this.zzafv = j;
    }

    public final void run() {
        this.zzafu.zzr(this.zzafv);
    }
}
