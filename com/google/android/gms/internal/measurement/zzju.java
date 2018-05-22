package com.google.android.gms.internal.measurement;

import java.util.concurrent.Callable;

final class zzju implements Callable<String> {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzjr zzaqu;

    zzju(zzjr com_google_android_gms_internal_measurement_zzjr, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzaqu = com_google_android_gms_internal_measurement_zzjr;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    public final /* synthetic */ Object call() throws Exception {
        zzdy zza = this.zzaqu.zzgg().zzaz(this.zzane.packageName) ? this.zzaqu.zzg(this.zzane) : this.zzaqu.zzix().zzbc(this.zzane.packageName);
        if (zza != null) {
            return zza.getAppInstanceId();
        }
        this.zzaqu.zzge().zzip().log("App info was null when attempting to get app instance id");
        return null;
    }
}
