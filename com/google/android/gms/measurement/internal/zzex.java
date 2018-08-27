package com.google.android.gms.measurement.internal;

final class zzex extends zzv {
    private final /* synthetic */ zzfa zzasv;
    private final /* synthetic */ zzew zzatb;

    zzex(zzew com_google_android_gms_measurement_internal_zzew, zzcq com_google_android_gms_measurement_internal_zzcq, zzfa com_google_android_gms_measurement_internal_zzfa) {
        this.zzatb = com_google_android_gms_measurement_internal_zzew;
        this.zzasv = com_google_android_gms_measurement_internal_zzfa;
        super(com_google_android_gms_measurement_internal_zzcq);
    }

    public final void run() {
        this.zzatb.cancel();
        this.zzatb.zzgo().zzjl().zzbx("Starting upload from DelayedRunnable");
        this.zzasv.zzlt();
    }
}
