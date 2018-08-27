package com.google.android.gms.measurement.internal;

final class zzbk implements Runnable {
    private final /* synthetic */ zzbt zzaoj;
    private final /* synthetic */ zzap zzaok;

    zzbk(zzbj com_google_android_gms_measurement_internal_zzbj, zzbt com_google_android_gms_measurement_internal_zzbt, zzap com_google_android_gms_measurement_internal_zzap) {
        this.zzaoj = com_google_android_gms_measurement_internal_zzbt;
        this.zzaok = com_google_android_gms_measurement_internal_zzap;
    }

    public final void run() {
        if (this.zzaoj.zzkg() == null) {
            this.zzaok.zzjd().zzbx("Install Referrer Reporter is null");
            return;
        }
        zzbg zzkg = this.zzaoj.zzkg();
        zzkg.zzadj.zzgb();
        zzkg.zzcd(zzkg.zzadj.getContext().getPackageName());
    }
}
