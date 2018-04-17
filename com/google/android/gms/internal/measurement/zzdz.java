package com.google.android.gms.internal.measurement;

final class zzdz implements Runnable {
    private final /* synthetic */ String zzadd;
    private final /* synthetic */ long zzade;
    private final /* synthetic */ zzdx zzadf;

    zzdz(zzdx com_google_android_gms_internal_measurement_zzdx, String str, long j) {
        this.zzadf = com_google_android_gms_internal_measurement_zzdx;
        this.zzadd = str;
        this.zzade = j;
    }

    public final void run() {
        this.zzadf.zzb(this.zzadd, this.zzade);
    }
}
