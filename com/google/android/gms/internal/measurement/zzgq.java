package com.google.android.gms.internal.measurement;

final class zzgq implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ zzed zzang;

    zzgq(zzgn com_google_android_gms_internal_measurement_zzgn, zzed com_google_android_gms_internal_measurement_zzed, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzanf = com_google_android_gms_internal_measurement_zzgn;
        this.zzang = com_google_android_gms_internal_measurement_zzed;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    public final void run() {
        this.zzanf.zzajp.zzkx();
        this.zzanf.zzajp.zzb(this.zzang, this.zzane);
    }
}
