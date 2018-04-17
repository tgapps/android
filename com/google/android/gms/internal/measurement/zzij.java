package com.google.android.gms.internal.measurement;

final class zzij implements Runnable {
    private final /* synthetic */ zzih zzapo;
    private final /* synthetic */ zzik zzapp;

    zzij(zzih com_google_android_gms_internal_measurement_zzih, zzik com_google_android_gms_internal_measurement_zzik) {
        this.zzapo = com_google_android_gms_internal_measurement_zzih;
        this.zzapp = com_google_android_gms_internal_measurement_zzik;
    }

    public final void run() {
        this.zzapo.zza(this.zzapp);
        this.zzapo.zzapc = null;
        this.zzapo.zzfx().zza(null);
    }
}
