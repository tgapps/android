package com.google.android.gms.internal.measurement;

final class zzen implements Runnable {
    private final /* synthetic */ zzgl zzafi;
    private final /* synthetic */ zzem zzafj;

    zzen(zzem com_google_android_gms_internal_measurement_zzem, zzgl com_google_android_gms_internal_measurement_zzgl) {
        this.zzafj = com_google_android_gms_internal_measurement_zzem;
        this.zzafi = com_google_android_gms_internal_measurement_zzgl;
    }

    public final void run() {
        this.zzafi.zzgf();
        if (zzgg.isMainThread()) {
            this.zzafj.zzacr.zzgf().zzc((Runnable) this);
            return;
        }
        boolean zzef = this.zzafj.zzef();
        this.zzafj.zzxz = 0;
        if (zzef && this.zzafj.enabled) {
            this.zzafj.run();
        }
    }
}
