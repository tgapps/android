package com.google.android.gms.measurement.internal;

import java.util.concurrent.Callable;

final class zzfe implements Callable<String> {
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzfa zzaty;

    zzfe(zzfa com_google_android_gms_measurement_internal_zzfa, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzaty = com_google_android_gms_measurement_internal_zzfa;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    public final /* synthetic */ Object call() throws Exception {
        zzg zza;
        if (this.zzaty.zzgq().zzbd(this.zzaqn.packageName)) {
            zza = this.zzaty.zzg(this.zzaqn);
        } else {
            zza = this.zzaty.zzjq().zzbl(this.zzaqn.packageName);
        }
        if (zza != null) {
            return zza.getAppInstanceId();
        }
        this.zzaty.zzgo().zzjg().zzbx("App info was null when attempting to get app instance id");
        return null;
    }
}
