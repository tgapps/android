package com.google.android.gms.measurement.internal;

final class zzdq implements Runnable {
    private final /* synthetic */ zzdo zzarx;
    private final /* synthetic */ zzdn zzary;

    zzdq(zzdo com_google_android_gms_measurement_internal_zzdo, zzdn com_google_android_gms_measurement_internal_zzdn) {
        this.zzarx = com_google_android_gms_measurement_internal_zzdo;
        this.zzary = com_google_android_gms_measurement_internal_zzdn;
    }

    public final void run() {
        this.zzarx.zza(this.zzary);
        this.zzarx.zzaro = null;
        this.zzarx.zzgg().zzb(null);
    }
}
