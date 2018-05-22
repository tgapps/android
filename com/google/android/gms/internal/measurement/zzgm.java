package com.google.android.gms.internal.measurement;

final class zzgm implements Runnable {
    private final /* synthetic */ zzhj zzana;
    private final /* synthetic */ zzgl zzanb;

    zzgm(zzgl com_google_android_gms_internal_measurement_zzgl, zzhj com_google_android_gms_internal_measurement_zzhj) {
        this.zzanb = com_google_android_gms_internal_measurement_zzgl;
        this.zzana = com_google_android_gms_internal_measurement_zzhj;
    }

    public final void run() {
        this.zzanb.zza(this.zzana);
        this.zzanb.start();
    }
}
