package com.google.android.gms.internal.measurement;

final class zzgo implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;

    zzgo(zzgn com_google_android_gms_internal_measurement_zzgn, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzanf = com_google_android_gms_internal_measurement_zzgn;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    public final void run() {
        this.zzanf.zzajp.zzkx();
        this.zzanf.zzajp.zze(this.zzane);
    }
}
