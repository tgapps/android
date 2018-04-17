package com.google.android.gms.internal.measurement;

final class zzib implements Runnable {
    private final /* synthetic */ zzhm zzaop;
    private final /* synthetic */ boolean zzaou;

    zzib(zzhm com_google_android_gms_internal_measurement_zzhm, boolean z) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaou = z;
    }

    public final void run() {
        this.zzaop.zzj(this.zzaou);
    }
}
