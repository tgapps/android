package com.google.android.gms.internal.measurement;

final class zzja implements Runnable {
    private final /* synthetic */ zzix zzapw;
    private final /* synthetic */ zzez zzapx;

    zzja(zzix com_google_android_gms_internal_measurement_zzix, zzez com_google_android_gms_internal_measurement_zzez) {
        this.zzapw = com_google_android_gms_internal_measurement_zzix;
        this.zzapx = com_google_android_gms_internal_measurement_zzez;
    }

    public final void run() {
        synchronized (this.zzapw) {
            this.zzapw.zzapt = false;
            if (!this.zzapw.zzapn.isConnected()) {
                this.zzapw.zzapn.zzgf().zziy().log("Connected to remote service");
                this.zzapw.zzapn.zza(this.zzapx);
            }
        }
    }
}
