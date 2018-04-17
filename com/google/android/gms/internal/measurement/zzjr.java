package com.google.android.gms.internal.measurement;

final class zzjr extends zzem {
    private final /* synthetic */ zzgl zzafi;
    private final /* synthetic */ zzjq zzaqt;

    zzjr(zzjq com_google_android_gms_internal_measurement_zzjq, zzgl com_google_android_gms_internal_measurement_zzgl, zzgl com_google_android_gms_internal_measurement_zzgl2) {
        this.zzaqt = com_google_android_gms_internal_measurement_zzjq;
        this.zzafi = com_google_android_gms_internal_measurement_zzgl2;
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    public final void run() {
        this.zzaqt.cancel();
        this.zzaqt.zzgg().zzir().log("Starting upload from DelayedRunnable");
        this.zzafi.zzjw();
    }
}
