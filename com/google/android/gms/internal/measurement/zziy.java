package com.google.android.gms.internal.measurement;

final class zziy implements Runnable {
    private final /* synthetic */ zzez zzapv;
    private final /* synthetic */ zzix zzapw;

    zziy(zzix com_google_android_gms_internal_measurement_zzix, zzez com_google_android_gms_internal_measurement_zzez) {
        this.zzapw = com_google_android_gms_internal_measurement_zzix;
        this.zzapv = com_google_android_gms_internal_measurement_zzez;
    }

    public final void run() {
        synchronized (this.zzapw) {
            this.zzapw.zzapt = false;
            if (!this.zzapw.zzapn.isConnected()) {
                this.zzapw.zzapn.zzgf().zziz().log("Connected to service");
                this.zzapw.zzapn.zza(this.zzapv);
            }
        }
    }
}
