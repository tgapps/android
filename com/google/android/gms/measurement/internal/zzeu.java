package com.google.android.gms.measurement.internal;

final class zzeu implements Runnable {
    private final /* synthetic */ long zzafv;
    private final /* synthetic */ zzeq zzasz;

    zzeu(zzeq com_google_android_gms_measurement_internal_zzeq, long j) {
        this.zzasz = com_google_android_gms_measurement_internal_zzeq;
        this.zzafv = j;
    }

    public final void run() {
        this.zzasz.zzan(this.zzafv);
    }
}
