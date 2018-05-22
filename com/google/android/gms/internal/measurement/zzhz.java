package com.google.android.gms.internal.measurement;

final class zzhz implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ boolean zzaoc;

    zzhz(zzhk com_google_android_gms_internal_measurement_zzhk, boolean z) {
        this.zzanw = com_google_android_gms_internal_measurement_zzhk;
        this.zzaoc = z;
    }

    public final void run() {
        this.zzanw.zzi(this.zzaoc);
    }
}
