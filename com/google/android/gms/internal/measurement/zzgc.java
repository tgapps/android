package com.google.android.gms.internal.measurement;

final class zzgc implements Runnable {
    private final /* synthetic */ zzgl zzalb;
    private final /* synthetic */ zzfg zzalc;

    zzgc(zzgb com_google_android_gms_internal_measurement_zzgb, zzgl com_google_android_gms_internal_measurement_zzgl, zzfg com_google_android_gms_internal_measurement_zzfg) {
        this.zzalb = com_google_android_gms_internal_measurement_zzgl;
        this.zzalc = com_google_android_gms_internal_measurement_zzfg;
    }

    public final void run() {
        if (this.zzalb.zzjp() == null) {
            this.zzalc.zzim().log("Install Referrer Reporter is null");
        } else {
            this.zzalb.zzjp().zzjh();
        }
    }
}
