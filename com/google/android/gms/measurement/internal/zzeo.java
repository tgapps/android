package com.google.android.gms.measurement.internal;

final class zzeo implements Runnable {
    private final /* synthetic */ Runnable zzacf;
    private final /* synthetic */ zzfa zzasv;

    zzeo(zzel com_google_android_gms_measurement_internal_zzel, zzfa com_google_android_gms_measurement_internal_zzfa, Runnable runnable) {
        this.zzasv = com_google_android_gms_measurement_internal_zzfa;
        this.zzacf = runnable;
    }

    public final void run() {
        this.zzasv.zzly();
        this.zzasv.zzg(this.zzacf);
        this.zzasv.zzlt();
    }
}
