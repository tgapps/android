package com.google.android.gms.internal.measurement;

final class zzgm implements Runnable {
    private final /* synthetic */ zzhl zzano;
    private final /* synthetic */ zzgl zzanp;

    zzgm(zzgl com_google_android_gms_internal_measurement_zzgl, zzhl com_google_android_gms_internal_measurement_zzhl) {
        this.zzanp = com_google_android_gms_internal_measurement_zzgl;
        this.zzano = com_google_android_gms_internal_measurement_zzhl;
    }

    public final void run() {
        this.zzanp.zza(this.zzano);
        this.zzanp.start();
    }
}
