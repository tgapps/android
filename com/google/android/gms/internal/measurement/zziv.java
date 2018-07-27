package com.google.android.gms.internal.measurement;

final class zziv implements Runnable {
    private final /* synthetic */ zzdz zzano;
    private final /* synthetic */ zzjz zzanv;
    private final /* synthetic */ zzij zzapn;
    private final /* synthetic */ boolean zzapq;

    zziv(zzij com_google_android_gms_internal_measurement_zzij, boolean z, zzjz com_google_android_gms_internal_measurement_zzjz, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzapn = com_google_android_gms_internal_measurement_zzij;
        this.zzapq = z;
        this.zzanv = com_google_android_gms_internal_measurement_zzjz;
        this.zzano = com_google_android_gms_internal_measurement_zzdz;
    }

    public final void run() {
        zzez zzd = this.zzapn.zzaph;
        if (zzd == null) {
            this.zzapn.zzgf().zzis().log("Discarding data. Failed to set user attribute");
            return;
        }
        this.zzapn.zza(zzd, this.zzapq ? null : this.zzanv, this.zzano);
        this.zzapn.zzcu();
    }
}
