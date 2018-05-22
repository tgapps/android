package com.google.android.gms.internal.measurement;

final class zzjl implements Runnable {
    private final /* synthetic */ long zzadj;
    private final /* synthetic */ zzjh zzapx;

    zzjl(zzjh com_google_android_gms_internal_measurement_zzjh, long j) {
        this.zzapx = com_google_android_gms_internal_measurement_zzjh;
        this.zzadj = j;
    }

    public final void run() {
        this.zzapx.zzag(this.zzadj);
    }
}
