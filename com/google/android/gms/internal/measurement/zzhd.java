package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.concurrent.Callable;

final class zzhd implements Callable<List<zzjz>> {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;

    zzhd(zzgn com_google_android_gms_internal_measurement_zzgn, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzanf = com_google_android_gms_internal_measurement_zzgn;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzanf.zzajp.zzkx();
        return this.zzanf.zzajp.zzix().zzbb(this.zzane.packageName);
    }
}
