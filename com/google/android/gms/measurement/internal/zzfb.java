package com.google.android.gms.measurement.internal;

final class zzfb implements Runnable {
    private final /* synthetic */ zzff zzatx;
    private final /* synthetic */ zzfa zzaty;

    zzfb(zzfa com_google_android_gms_measurement_internal_zzfa, zzff com_google_android_gms_measurement_internal_zzff) {
        this.zzaty = com_google_android_gms_measurement_internal_zzfa;
        this.zzatx = com_google_android_gms_measurement_internal_zzff;
    }

    public final void run() {
        this.zzaty.zza(this.zzatx);
        this.zzaty.start();
    }
}
