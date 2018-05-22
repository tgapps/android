package com.google.android.gms.internal.measurement;

final class zzfq implements Runnable {
    private final /* synthetic */ boolean zzajq;
    private final /* synthetic */ zzfp zzajr;

    zzfq(zzfp com_google_android_gms_internal_measurement_zzfp, boolean z) {
        this.zzajr = com_google_android_gms_internal_measurement_zzfp;
        this.zzajq = z;
    }

    public final void run() {
        this.zzajr.zzajp.zzm(this.zzajq);
    }
}
