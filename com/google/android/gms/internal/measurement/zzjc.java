package com.google.android.gms.internal.measurement;

final class zzjc implements Runnable {
    private final /* synthetic */ zziz zzaqi;
    private final /* synthetic */ zzey zzaqj;

    zzjc(zziz com_google_android_gms_internal_measurement_zziz, zzey com_google_android_gms_internal_measurement_zzey) {
        this.zzaqi = com_google_android_gms_internal_measurement_zziz;
        this.zzaqj = com_google_android_gms_internal_measurement_zzey;
    }

    public final void run() {
        synchronized (this.zzaqi) {
            this.zzaqi.zzaqf = false;
            if (!this.zzaqi.zzapy.isConnected()) {
                this.zzaqi.zzapy.zzgg().zziq().log("Connected to remote service");
                this.zzaqi.zzapy.zza(this.zzaqj);
            }
        }
    }
}
