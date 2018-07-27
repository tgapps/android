package com.google.android.gms.internal.measurement;

final class zzjm implements Runnable {
    private final /* synthetic */ long zzadj;
    private final /* synthetic */ zzji zzaqg;

    zzjm(zzji com_google_android_gms_internal_measurement_zzji, long j) {
        this.zzaqg = com_google_android_gms_internal_measurement_zzji;
        this.zzadj = j;
    }

    public final void run() {
        this.zzaqg.zzag(this.zzadj);
    }
}
