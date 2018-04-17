package com.google.android.gms.internal.measurement;

final class zzix implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ zzjs zzaoe;
    private final /* synthetic */ zzil zzapy;
    private final /* synthetic */ boolean zzaqc;

    zzix(zzil com_google_android_gms_internal_measurement_zzil, boolean z, zzjs com_google_android_gms_internal_measurement_zzjs, zzec com_google_android_gms_internal_measurement_zzec) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzaqc = z;
        this.zzaoe = com_google_android_gms_internal_measurement_zzjs;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
    }

    public final void run() {
        zzey zzd = this.zzapy.zzaps;
        if (zzd == null) {
            this.zzapy.zzgg().zzil().log("Discarding data. Failed to set user attribute");
            return;
        }
        this.zzapy.zza(zzd, this.zzaqc ? null : this.zzaoe, this.zzanq);
        this.zzapy.zzcu();
    }
}
