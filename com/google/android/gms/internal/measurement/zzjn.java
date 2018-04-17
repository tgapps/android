package com.google.android.gms.internal.measurement;

final class zzjn implements Runnable {
    private final /* synthetic */ long zzade;
    private final /* synthetic */ zzjk zzaqr;

    zzjn(zzjk com_google_android_gms_internal_measurement_zzjk, long j) {
        this.zzaqr = com_google_android_gms_internal_measurement_zzjk;
        this.zzade = j;
    }

    public final void run() {
        this.zzaqr.zzaf(this.zzade);
    }
}
