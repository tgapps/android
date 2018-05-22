package com.google.android.gms.internal.measurement;

final class zzia implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ long zzaod;

    zzia(zzhk com_google_android_gms_internal_measurement_zzhk, long j) {
        this.zzanw = com_google_android_gms_internal_measurement_zzhk;
        this.zzaod = j;
    }

    public final void run() {
        this.zzanw.zzgf().zzaki.set(this.zzaod);
        this.zzanw.zzge().zzis().zzg("Minimum session duration set", Long.valueOf(this.zzaod));
    }
}
