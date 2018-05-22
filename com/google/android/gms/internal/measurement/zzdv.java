package com.google.android.gms.internal.measurement;

final class zzdv implements Runnable {
    private final /* synthetic */ String zzadi;
    private final /* synthetic */ long zzadj;
    private final /* synthetic */ zzdu zzadk;

    zzdv(zzdu com_google_android_gms_internal_measurement_zzdu, String str, long j) {
        this.zzadk = com_google_android_gms_internal_measurement_zzdu;
        this.zzadi = str;
        this.zzadj = j;
    }

    public final void run() {
        this.zzadk.zza(this.zzadi, this.zzadj);
    }
}
