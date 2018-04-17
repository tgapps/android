package com.google.android.gms.internal.measurement;

final class zzfq implements Runnable {
    private final /* synthetic */ boolean zzajp;
    private final /* synthetic */ zzfp zzajq;

    zzfq(zzfp com_google_android_gms_internal_measurement_zzfp, boolean z) {
        this.zzajq = com_google_android_gms_internal_measurement_zzfp;
        this.zzajp = z;
    }

    public final void run() {
        this.zzajq.zzacr.zzi(this.zzajp);
    }
}
