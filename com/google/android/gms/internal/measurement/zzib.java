package com.google.android.gms.internal.measurement;

final class zzib implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ long zzaod;

    zzib(zzhk com_google_android_gms_internal_measurement_zzhk, long j) {
        this.zzanw = com_google_android_gms_internal_measurement_zzhk;
        this.zzaod = j;
    }

    public final void run() {
        this.zzanw.zzgf().zzakj.set(this.zzaod);
        this.zzanw.zzge().zzis().zzg("Session timeout duration set", Long.valueOf(this.zzaod));
    }
}
