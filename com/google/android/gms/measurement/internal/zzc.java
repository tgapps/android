package com.google.android.gms.measurement.internal;

final class zzc implements Runnable {
    private final /* synthetic */ String zzaet;
    private final /* synthetic */ long zzaft;
    private final /* synthetic */ zza zzafu;

    zzc(zza com_google_android_gms_measurement_internal_zza, String str, long j) {
        this.zzafu = com_google_android_gms_measurement_internal_zza;
        this.zzaet = str;
        this.zzaft = j;
    }

    public final void run() {
        this.zzafu.zzb(this.zzaet, this.zzaft);
    }
}
