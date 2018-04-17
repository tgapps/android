package com.google.android.gms.internal.measurement;

final class zzja implements Runnable {
    private final /* synthetic */ zzey zzaqh;
    private final /* synthetic */ zziz zzaqi;

    zzja(zziz com_google_android_gms_internal_measurement_zziz, zzey com_google_android_gms_internal_measurement_zzey) {
        this.zzaqi = com_google_android_gms_internal_measurement_zziz;
        this.zzaqh = com_google_android_gms_internal_measurement_zzey;
    }

    public final void run() {
        synchronized (this.zzaqi) {
            this.zzaqi.zzaqf = false;
            if (!this.zzaqi.zzapy.isConnected()) {
                this.zzaqi.zzapy.zzgg().zzir().log("Connected to service");
                this.zzaqi.zzapy.zza(this.zzaqh);
            }
        }
    }
}
