package com.google.android.gms.internal.measurement;

final class zzhc implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ zzjx zzanl;

    zzhc(zzgn com_google_android_gms_internal_measurement_zzgn, zzjx com_google_android_gms_internal_measurement_zzjx, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzanf = com_google_android_gms_internal_measurement_zzgn;
        this.zzanl = com_google_android_gms_internal_measurement_zzjx;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    public final void run() {
        this.zzanf.zzajp.zzkx();
        this.zzanf.zzajp.zzb(this.zzanl, this.zzane);
    }
}
