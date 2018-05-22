package com.google.android.gms.internal.measurement;

final class zzgr implements Runnable {
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ zzed zzang;

    zzgr(zzgn com_google_android_gms_internal_measurement_zzgn, zzed com_google_android_gms_internal_measurement_zzed) {
        this.zzanf = com_google_android_gms_internal_measurement_zzgn;
        this.zzang = com_google_android_gms_internal_measurement_zzed;
    }

    public final void run() {
        this.zzanf.zzajp.zzkx();
        zzjr zza = this.zzanf.zzajp;
        zzed com_google_android_gms_internal_measurement_zzed = this.zzang;
        zzdz zzcb = zza.zzcb(com_google_android_gms_internal_measurement_zzed.packageName);
        if (zzcb != null) {
            zza.zzc(com_google_android_gms_internal_measurement_zzed, zzcb);
        }
    }
}
