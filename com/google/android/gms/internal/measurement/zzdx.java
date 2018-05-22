package com.google.android.gms.internal.measurement;

final class zzdx implements Runnable {
    private final /* synthetic */ long zzadj;
    private final /* synthetic */ zzdu zzadk;

    zzdx(zzdu com_google_android_gms_internal_measurement_zzdu, long j) {
        this.zzadk = com_google_android_gms_internal_measurement_zzdu;
        this.zzadj = j;
    }

    public final void run() {
        this.zzadk.zzl(this.zzadj);
    }
}
