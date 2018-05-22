package com.google.android.gms.internal.measurement;

final class zziz implements Runnable {
    private final /* synthetic */ zziw zzapn;
    private final /* synthetic */ zzey zzapo;

    zziz(zziw com_google_android_gms_internal_measurement_zziw, zzey com_google_android_gms_internal_measurement_zzey) {
        this.zzapn = com_google_android_gms_internal_measurement_zziw;
        this.zzapo = com_google_android_gms_internal_measurement_zzey;
    }

    public final void run() {
        synchronized (this.zzapn) {
            this.zzapn.zzapk = false;
            if (!this.zzapn.zzape.isConnected()) {
                this.zzapn.zzape.zzge().zzis().log("Connected to remote service");
                this.zzapn.zzape.zza(this.zzapo);
            }
        }
    }
}
