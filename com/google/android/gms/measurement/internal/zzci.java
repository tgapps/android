package com.google.android.gms.measurement.internal;

import java.util.concurrent.Callable;

final class zzci implements Callable<byte[]> {
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzad zzaqr;

    zzci(zzbv com_google_android_gms_measurement_internal_zzbv, zzad com_google_android_gms_measurement_internal_zzad, String str) {
        this.zzaqo = com_google_android_gms_measurement_internal_zzbv;
        this.zzaqr = com_google_android_gms_measurement_internal_zzad;
        this.zzaqq = str;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzaqo.zzamz.zzly();
        return this.zzaqo.zzamz.zza(this.zzaqr, this.zzaqq);
    }
}
