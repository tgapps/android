package com.google.android.gms.internal.measurement;

final class zzjo implements Runnable {
    private final /* synthetic */ long zzade;
    private final /* synthetic */ zzjk zzaqr;

    zzjo(zzjk com_google_android_gms_internal_measurement_zzjk, long j) {
        this.zzaqr = com_google_android_gms_internal_measurement_zzjk;
        this.zzade = j;
    }

    public final void run() {
        this.zzaqr.zzag(this.zzade);
    }
}
