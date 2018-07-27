package com.google.android.gms.internal.measurement;

final class zzia implements Runnable {
    private final /* synthetic */ zzhl zzaog;
    private final /* synthetic */ boolean zzaom;

    zzia(zzhl com_google_android_gms_internal_measurement_zzhl, boolean z) {
        this.zzaog = com_google_android_gms_internal_measurement_zzhl;
        this.zzaom = z;
    }

    public final void run() {
        this.zzaog.zzi(this.zzaom);
    }
}
