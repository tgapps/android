package com.google.android.gms.measurement.internal;

final class zzei implements Runnable {
    private final /* synthetic */ zzef zzasp;
    private final /* synthetic */ zzag zzasq;

    zzei(zzef com_google_android_gms_measurement_internal_zzef, zzag com_google_android_gms_measurement_internal_zzag) {
        this.zzasp = com_google_android_gms_measurement_internal_zzef;
        this.zzasq = com_google_android_gms_measurement_internal_zzag;
    }

    public final void run() {
        synchronized (this.zzasp) {
            this.zzasp.zzasm = false;
            if (!this.zzasp.zzasg.isConnected()) {
                this.zzasp.zzasg.zzgo().zzjk().zzbx("Connected to remote service");
                this.zzasp.zzasg.zza(this.zzasq);
            }
        }
    }
}
