package com.google.android.gms.measurement.internal;

final class zzcg implements Runnable {
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ zzad zzaqr;

    zzcg(zzbv com_google_android_gms_measurement_internal_zzbv, zzad com_google_android_gms_measurement_internal_zzad, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzaqo = com_google_android_gms_measurement_internal_zzbv;
        this.zzaqr = com_google_android_gms_measurement_internal_zzad;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    public final void run() {
        zzad zzb = this.zzaqo.zzb(this.zzaqr, this.zzaqn);
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzc(zzb, this.zzaqn);
    }
}
