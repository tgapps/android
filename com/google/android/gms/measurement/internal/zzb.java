package com.google.android.gms.measurement.internal;

final class zzb implements Runnable {
    private final /* synthetic */ String zzaet;
    private final /* synthetic */ long zzaft;
    private final /* synthetic */ zza zzafu;

    zzb(zza com_google_android_gms_measurement_internal_zza, String str, long j) {
        this.zzafu = com_google_android_gms_measurement_internal_zza;
        this.zzaet = str;
        this.zzaft = j;
    }

    public final void run() {
        this.zzafu.zza(this.zzaet, this.zzaft);
    }
}
