package com.google.android.gms.measurement.internal;

final class zzw implements Runnable {
    private final /* synthetic */ zzcq zzahx;
    private final /* synthetic */ zzv zzahy;

    zzw(zzv com_google_android_gms_measurement_internal_zzv, zzcq com_google_android_gms_measurement_internal_zzcq) {
        this.zzahy = com_google_android_gms_measurement_internal_zzv;
        this.zzahx = com_google_android_gms_measurement_internal_zzcq;
    }

    public final void run() {
        this.zzahx.zzgr();
        if (zzk.isMainThread()) {
            this.zzahx.zzgn().zzc((Runnable) this);
            return;
        }
        boolean zzej = this.zzahy.zzej();
        this.zzahy.zzyp = 0;
        if (zzej) {
            this.zzahy.run();
        }
    }
}
