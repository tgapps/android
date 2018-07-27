package com.google.android.gms.internal.measurement;

final class zzep implements Runnable {
    private final /* synthetic */ zzhj zzafl;
    private final /* synthetic */ zzeo zzafm;

    zzep(zzeo com_google_android_gms_internal_measurement_zzeo, zzhj com_google_android_gms_internal_measurement_zzhj) {
        this.zzafm = com_google_android_gms_internal_measurement_zzeo;
        this.zzafl = com_google_android_gms_internal_measurement_zzhj;
    }

    public final void run() {
        this.zzafl.zzgi();
        if (zzec.isMainThread()) {
            this.zzafl.zzge().zzc((Runnable) this);
            return;
        }
        boolean zzef = this.zzafm.zzef();
        this.zzafm.zzye = 0;
        if (zzef) {
            this.zzafm.run();
        }
    }
}
