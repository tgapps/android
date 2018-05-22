package com.google.android.gms.internal.measurement;

final class zzgz implements Runnable {
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ zzeu zzank;

    zzgz(zzgn com_google_android_gms_internal_measurement_zzgn, zzeu com_google_android_gms_internal_measurement_zzeu, String str) {
        this.zzanf = com_google_android_gms_internal_measurement_zzgn;
        this.zzank = com_google_android_gms_internal_measurement_zzeu;
        this.zzanj = str;
    }

    public final void run() {
        this.zzanf.zzajp.zzkx();
        this.zzanf.zzajp.zzc(this.zzank, this.zzanj);
    }
}
