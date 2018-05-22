package com.google.android.gms.internal.measurement;

final class zzix implements Runnable {
    private final /* synthetic */ zzey zzapm;
    private final /* synthetic */ zziw zzapn;

    zzix(zziw com_google_android_gms_internal_measurement_zziw, zzey com_google_android_gms_internal_measurement_zzey) {
        this.zzapn = com_google_android_gms_internal_measurement_zziw;
        this.zzapm = com_google_android_gms_internal_measurement_zzey;
    }

    public final void run() {
        synchronized (this.zzapn) {
            this.zzapn.zzapk = false;
            if (!this.zzapn.zzape.isConnected()) {
                this.zzapn.zzape.zzge().zzit().log("Connected to service");
                this.zzapn.zzape.zza(this.zzapm);
            }
        }
    }
}
