package com.google.android.gms.internal.measurement;

final class zzhq implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ long zzaoa;

    zzhq(zzhk com_google_android_gms_internal_measurement_zzhk, long j) {
        this.zzanw = com_google_android_gms_internal_measurement_zzhk;
        this.zzaoa = j;
    }

    public final void run() {
        boolean z = true;
        zzhg com_google_android_gms_internal_measurement_zzhg = this.zzanw;
        long j = this.zzaoa;
        com_google_android_gms_internal_measurement_zzhg.zzab();
        com_google_android_gms_internal_measurement_zzhg.zzch();
        com_google_android_gms_internal_measurement_zzhg.zzge().zzis().log("Resetting analytics data (FE)");
        com_google_android_gms_internal_measurement_zzhg.zzgc().zzkj();
        if (com_google_android_gms_internal_measurement_zzhg.zzgg().zzba(com_google_android_gms_internal_measurement_zzhg.zzfv().zzah())) {
            com_google_android_gms_internal_measurement_zzhg.zzgf().zzajz.set(j);
        }
        boolean isEnabled = com_google_android_gms_internal_measurement_zzhg.zzacw.isEnabled();
        if (!com_google_android_gms_internal_measurement_zzhg.zzgg().zzhg()) {
            com_google_android_gms_internal_measurement_zzhg.zzgf().zzh(!isEnabled);
        }
        com_google_android_gms_internal_measurement_zzhg.zzfx().resetAnalyticsData();
        if (isEnabled) {
            z = false;
        }
        com_google_android_gms_internal_measurement_zzhg.zzanu = z;
    }
}
