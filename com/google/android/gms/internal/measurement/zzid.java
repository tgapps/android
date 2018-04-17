package com.google.android.gms.internal.measurement;

final class zzid implements Runnable {
    private final /* synthetic */ zzhm zzaop;
    private final /* synthetic */ long zzaov;

    zzid(zzhm com_google_android_gms_internal_measurement_zzhm, long j) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaov = j;
    }

    public final void run() {
        this.zzaop.zzgh().zzaki.set(this.zzaov);
        this.zzaop.zzgg().zziq().zzg("Session timeout duration set", Long.valueOf(this.zzaov));
    }
}
