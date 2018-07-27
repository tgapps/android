package com.google.android.gms.internal.measurement;

final class zzii implements Runnable {
    private final /* synthetic */ zzig zzape;
    private final /* synthetic */ zzif zzapf;

    zzii(zzig com_google_android_gms_internal_measurement_zzig, zzif com_google_android_gms_internal_measurement_zzif) {
        this.zzape = com_google_android_gms_internal_measurement_zzig;
        this.zzapf = com_google_android_gms_internal_measurement_zzif;
    }

    public final void run() {
        this.zzape.zza(this.zzapf);
        this.zzape.zzaov = null;
        this.zzape.zzfy().zzb(null);
    }
}
