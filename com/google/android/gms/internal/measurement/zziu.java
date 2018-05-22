package com.google.android.gms.internal.measurement;

final class zziu implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzjx zzanl;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ boolean zzaph;

    zziu(zzii com_google_android_gms_internal_measurement_zzii, boolean z, zzjx com_google_android_gms_internal_measurement_zzjx, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzape = com_google_android_gms_internal_measurement_zzii;
        this.zzaph = z;
        this.zzanl = com_google_android_gms_internal_measurement_zzjx;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    public final void run() {
        zzey zzd = this.zzape.zzaoy;
        if (zzd == null) {
            this.zzape.zzge().zzim().log("Discarding data. Failed to set user attribute");
            return;
        }
        this.zzape.zza(zzd, this.zzaph ? null : this.zzanl, this.zzane);
        this.zzape.zzcu();
    }
}
