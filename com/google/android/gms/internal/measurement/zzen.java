package com.google.android.gms.internal.measurement;

final class zzen implements Runnable {
    private final /* synthetic */ zzhi zzafk;
    private final /* synthetic */ zzem zzafl;

    zzen(zzem com_google_android_gms_internal_measurement_zzem, zzhi com_google_android_gms_internal_measurement_zzhi) {
        this.zzafl = com_google_android_gms_internal_measurement_zzem;
        this.zzafk = com_google_android_gms_internal_measurement_zzhi;
    }

    public final void run() {
        this.zzafk.zzgd();
        if (zzgg.isMainThread()) {
            this.zzafk.zzgd().zzc((Runnable) this);
            return;
        }
        boolean zzef = this.zzafl.zzef();
        this.zzafl.zzye = 0;
        if (zzef) {
            this.zzafl.run();
        }
    }
}
