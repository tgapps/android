package com.google.android.gms.internal.measurement;

final class zzea implements Runnable {
    private final /* synthetic */ long zzade;
    private final /* synthetic */ zzdx zzadf;

    zzea(zzdx com_google_android_gms_internal_measurement_zzdx, long j) {
        this.zzadf = com_google_android_gms_internal_measurement_zzdx;
        this.zzade = j;
    }

    public final void run() {
        this.zzadf.zzl(this.zzade);
    }
}
