package com.google.android.gms.internal.measurement;

final class zzjo extends zzem {
    private final /* synthetic */ zzjr zzapt;
    private final /* synthetic */ zzjn zzapz;

    zzjo(zzjn com_google_android_gms_internal_measurement_zzjn, zzhi com_google_android_gms_internal_measurement_zzhi, zzjr com_google_android_gms_internal_measurement_zzjr) {
        this.zzapz = com_google_android_gms_internal_measurement_zzjn;
        this.zzapt = com_google_android_gms_internal_measurement_zzjr;
        super(com_google_android_gms_internal_measurement_zzhi);
    }

    public final void run() {
        this.zzapz.cancel();
        this.zzapz.zzge().zzit().log("Starting upload from DelayedRunnable");
        this.zzapt.zzks();
    }
}
