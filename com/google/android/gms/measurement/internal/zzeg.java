package com.google.android.gms.measurement.internal;

final class zzeg implements Runnable {
    private final /* synthetic */ zzag zzaso;
    private final /* synthetic */ zzef zzasp;

    zzeg(zzef com_google_android_gms_measurement_internal_zzef, zzag com_google_android_gms_measurement_internal_zzag) {
        this.zzasp = com_google_android_gms_measurement_internal_zzef;
        this.zzaso = com_google_android_gms_measurement_internal_zzag;
    }

    public final void run() {
        synchronized (this.zzasp) {
            this.zzasp.zzasm = false;
            if (!this.zzasp.zzasg.isConnected()) {
                this.zzasp.zzasg.zzgo().zzjl().zzbx("Connected to service");
                this.zzasp.zzasg.zza(this.zzaso);
            }
        }
    }
}
