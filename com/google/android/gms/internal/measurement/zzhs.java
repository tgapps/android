package com.google.android.gms.internal.measurement;

final class zzhs implements Runnable {
    private final /* synthetic */ zzhm zzaop;

    zzhs(zzhm com_google_android_gms_internal_measurement_zzhm) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
    }

    public final void run() {
        boolean z = true;
        zzhj com_google_android_gms_internal_measurement_zzhj = this.zzaop;
        com_google_android_gms_internal_measurement_zzhj.zzab();
        com_google_android_gms_internal_measurement_zzhj.zzch();
        com_google_android_gms_internal_measurement_zzhj.zzgg().zziq().log("Resetting analytics data (FE)");
        boolean isEnabled = com_google_android_gms_internal_measurement_zzhj.zzacr.isEnabled();
        if (!com_google_android_gms_internal_measurement_zzhj.zzgi().zzhi()) {
            com_google_android_gms_internal_measurement_zzhj.zzgh().zzh(!isEnabled);
        }
        com_google_android_gms_internal_measurement_zzhj.zzfx().resetAnalyticsData();
        if (isEnabled) {
            z = false;
        }
        com_google_android_gms_internal_measurement_zzhj.zzaon = z;
    }
}
