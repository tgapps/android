package com.google.android.gms.internal.measurement;

final class zzic implements Runnable {
    private final /* synthetic */ zzhm zzaop;
    private final /* synthetic */ long zzaov;

    zzic(zzhm com_google_android_gms_internal_measurement_zzhm, long j) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaov = j;
    }

    public final void run() {
        this.zzaop.zzgh().zzakh.set(this.zzaov);
        this.zzaop.zzgg().zziq().zzg("Minimum session duration set", Long.valueOf(this.zzaov));
    }
}
