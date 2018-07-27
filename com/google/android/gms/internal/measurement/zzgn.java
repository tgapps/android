package com.google.android.gms.internal.measurement;

final class zzgn implements Runnable {
    private final /* synthetic */ zzhk zzank;
    private final /* synthetic */ zzgm zzanl;

    zzgn(zzgm com_google_android_gms_internal_measurement_zzgm, zzhk com_google_android_gms_internal_measurement_zzhk) {
        this.zzanl = com_google_android_gms_internal_measurement_zzgm;
        this.zzank = com_google_android_gms_internal_measurement_zzhk;
    }

    public final void run() {
        this.zzanl.zza(this.zzank);
        this.zzanl.start();
    }
}
