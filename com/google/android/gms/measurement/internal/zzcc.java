package com.google.android.gms.measurement.internal;

import java.util.List;
import java.util.concurrent.Callable;

final class zzcc implements Callable<List<zzfj>> {
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ String zzaeo;
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ String zzaqq;

    zzcc(zzbv com_google_android_gms_measurement_internal_zzbv, String str, String str2, String str3) {
        this.zzaqo = com_google_android_gms_measurement_internal_zzbv;
        this.zzaqq = str;
        this.zzaeh = str2;
        this.zzaeo = str3;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzaqo.zzamz.zzly();
        return this.zzaqo.zzamz.zzjq().zzb(this.zzaqq, this.zzaeh, this.zzaeo);
    }
}
