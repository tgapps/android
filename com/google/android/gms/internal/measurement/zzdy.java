package com.google.android.gms.internal.measurement;

final class zzdy implements Runnable {
    private final /* synthetic */ String zzadd;
    private final /* synthetic */ long zzade;
    private final /* synthetic */ zzdx zzadf;

    zzdy(zzdx com_google_android_gms_internal_measurement_zzdx, String str, long j) {
        this.zzadf = com_google_android_gms_internal_measurement_zzdx;
        this.zzadd = str;
        this.zzade = j;
    }

    public final void run() {
        this.zzadf.zza(this.zzadd, this.zzade);
    }
}
