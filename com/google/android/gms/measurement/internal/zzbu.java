package com.google.android.gms.measurement.internal;

final class zzbu implements Runnable {
    private final /* synthetic */ zzcr zzaqj;
    private final /* synthetic */ zzbt zzaqk;

    zzbu(zzbt com_google_android_gms_measurement_internal_zzbt, zzcr com_google_android_gms_measurement_internal_zzcr) {
        this.zzaqk = com_google_android_gms_measurement_internal_zzbt;
        this.zzaqj = com_google_android_gms_measurement_internal_zzcr;
    }

    public final void run() {
        this.zzaqk.zza(this.zzaqj);
        this.zzaqk.start();
    }
}
