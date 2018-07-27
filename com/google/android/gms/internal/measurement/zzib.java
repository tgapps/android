package com.google.android.gms.internal.measurement;

final class zzib implements Runnable {
    private final /* synthetic */ zzhl zzaog;
    private final /* synthetic */ long zzaon;

    zzib(zzhl com_google_android_gms_internal_measurement_zzhl, long j) {
        this.zzaog = com_google_android_gms_internal_measurement_zzhl;
        this.zzaon = j;
    }

    public final void run() {
        this.zzaog.zzgg().zzakr.set(this.zzaon);
        this.zzaog.zzgf().zziy().zzg("Minimum session duration set", Long.valueOf(this.zzaon));
    }
}
