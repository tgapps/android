package com.google.android.gms.internal.measurement;

final class zzjf implements Runnable {
    private final /* synthetic */ Runnable zzabt;
    private final /* synthetic */ zzjr zzapt;

    zzjf(zzjc com_google_android_gms_internal_measurement_zzjc, zzjr com_google_android_gms_internal_measurement_zzjr, Runnable runnable) {
        this.zzapt = com_google_android_gms_internal_measurement_zzjr;
        this.zzabt = runnable;
    }

    public final void run() {
        this.zzapt.zzkx();
        this.zzapt.zzg(this.zzabt);
        this.zzapt.zzks();
    }
}
