package com.google.android.gms.measurement.internal;

final class zzed implements Runnable {
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzfh zzaqs;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ boolean zzasj;

    zzed(zzdr com_google_android_gms_measurement_internal_zzdr, boolean z, zzfh com_google_android_gms_measurement_internal_zzfh, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzasg = com_google_android_gms_measurement_internal_zzdr;
        this.zzasj = z;
        this.zzaqs = com_google_android_gms_measurement_internal_zzfh;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    public final void run() {
        zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Discarding data. Failed to set user attribute");
            return;
        }
        this.zzasg.zza(zzd, this.zzasj ? null : this.zzaqs, this.zzaqn);
        this.zzasg.zzcy();
    }
}
