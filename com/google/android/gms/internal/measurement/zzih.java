package com.google.android.gms.internal.measurement;

final class zzih implements Runnable {
    private final /* synthetic */ zzif zzaov;
    private final /* synthetic */ zzie zzaow;

    zzih(zzif com_google_android_gms_internal_measurement_zzif, zzie com_google_android_gms_internal_measurement_zzie) {
        this.zzaov = com_google_android_gms_internal_measurement_zzif;
        this.zzaow = com_google_android_gms_internal_measurement_zzie;
    }

    public final void run() {
        this.zzaov.zza(this.zzaow);
        this.zzaov.zzaol = null;
        this.zzaov.zzfx().zzb(null);
    }
}
