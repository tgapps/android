package com.google.android.gms.internal.measurement;

final class zzhr implements Runnable {
    private final /* synthetic */ zzhl zzaog;
    private final /* synthetic */ long zzaok;

    zzhr(zzhl com_google_android_gms_internal_measurement_zzhl, long j) {
        this.zzaog = com_google_android_gms_internal_measurement_zzhl;
        this.zzaok = j;
    }

    public final void run() {
        boolean z = true;
        zzhh com_google_android_gms_internal_measurement_zzhh = this.zzaog;
        long j = this.zzaok;
        com_google_android_gms_internal_measurement_zzhh.zzab();
        com_google_android_gms_internal_measurement_zzhh.zzfs();
        com_google_android_gms_internal_measurement_zzhh.zzch();
        com_google_android_gms_internal_measurement_zzhh.zzgf().zziy().log("Resetting analytics data (FE)");
        com_google_android_gms_internal_measurement_zzhh.zzgd().zzks();
        if (com_google_android_gms_internal_measurement_zzhh.zzgh().zzaz(com_google_android_gms_internal_measurement_zzhh.zzfw().zzah())) {
            com_google_android_gms_internal_measurement_zzhh.zzgg().zzaki.set(j);
        }
        boolean isEnabled = com_google_android_gms_internal_measurement_zzhh.zzacw.isEnabled();
        if (!com_google_android_gms_internal_measurement_zzhh.zzgh().zzhj()) {
            com_google_android_gms_internal_measurement_zzhh.zzgg().zzh(!isEnabled);
        }
        com_google_android_gms_internal_measurement_zzhh.zzfy().resetAnalyticsData();
        if (isEnabled) {
            z = false;
        }
        com_google_android_gms_internal_measurement_zzhh.zzaoe = z;
    }
}
