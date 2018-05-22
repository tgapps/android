package com.google.android.gms.internal.measurement;

final class zzhf implements Runnable {
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ String zzanm;
    private final /* synthetic */ String zzann;
    private final /* synthetic */ long zzano;

    zzhf(zzgn com_google_android_gms_internal_measurement_zzgn, String str, String str2, String str3, long j) {
        this.zzanf = com_google_android_gms_internal_measurement_zzgn;
        this.zzanm = str;
        this.zzanj = str2;
        this.zzann = str3;
        this.zzano = j;
    }

    public final void run() {
        if (this.zzanm == null) {
            this.zzanf.zzajp.zzla().zzfy().zza(this.zzanj, null);
            return;
        }
        this.zzanf.zzajp.zzla().zzfy().zza(this.zzanj, new zzie(this.zzann, this.zzanm, this.zzano));
    }
}
