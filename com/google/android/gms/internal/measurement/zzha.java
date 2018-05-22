package com.google.android.gms.internal.measurement;

import java.util.concurrent.Callable;

final class zzha implements Callable<byte[]> {
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ zzeu zzank;

    zzha(zzgn com_google_android_gms_internal_measurement_zzgn, zzeu com_google_android_gms_internal_measurement_zzeu, String str) {
        this.zzanf = com_google_android_gms_internal_measurement_zzgn;
        this.zzank = com_google_android_gms_internal_measurement_zzeu;
        this.zzanj = str;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzanf.zzajp.zzkx();
        return this.zzanf.zzajp.zza(this.zzank, this.zzanj);
    }
}
