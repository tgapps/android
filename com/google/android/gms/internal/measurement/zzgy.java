package com.google.android.gms.internal.measurement;

final class zzgy implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ zzeu zzank;

    zzgy(zzgn com_google_android_gms_internal_measurement_zzgn, zzeu com_google_android_gms_internal_measurement_zzeu, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzanf = com_google_android_gms_internal_measurement_zzgn;
        this.zzank = com_google_android_gms_internal_measurement_zzeu;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    public final void run() {
        this.zzanf.zzajp.zzkx();
        this.zzanf.zzajp.zzb(this.zzank, this.zzane);
    }
}
