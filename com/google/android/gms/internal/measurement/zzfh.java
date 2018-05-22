package com.google.android.gms.internal.measurement;

final class zzfh implements Runnable {
    private final /* synthetic */ int zzaix;
    private final /* synthetic */ String zzaiy;
    private final /* synthetic */ Object zzaiz;
    private final /* synthetic */ Object zzaja;
    private final /* synthetic */ Object zzajb;
    private final /* synthetic */ zzfg zzajc;

    zzfh(zzfg com_google_android_gms_internal_measurement_zzfg, int i, String str, Object obj, Object obj2, Object obj3) {
        this.zzajc = com_google_android_gms_internal_measurement_zzfg;
        this.zzaix = i;
        this.zzaiy = str;
        this.zzaiz = obj;
        this.zzaja = obj2;
        this.zzajb = obj3;
    }

    public final void run() {
        zzhh zzgf = this.zzajc.zzacw.zzgf();
        if (zzgf.isInitialized()) {
            if (this.zzajc.zzaim == '\u0000') {
                if (this.zzajc.zzgg().zzds()) {
                    this.zzajc.zzaim = 'C';
                } else {
                    this.zzajc.zzaim = 'c';
                }
            }
            if (this.zzajc.zzadu < 0) {
                this.zzajc.zzadu = 12451;
            }
            char charAt = "01VDIWEA?".charAt(this.zzaix);
            char zza = this.zzajc.zzaim;
            long zzb = this.zzajc.zzadu;
            String zza2 = zzfg.zza(true, this.zzaiy, this.zzaiz, this.zzaja, this.zzajb);
            String stringBuilder = new StringBuilder(String.valueOf(zza2).length() + 24).append("2").append(charAt).append(zza).append(zzb).append(":").append(zza2).toString();
            if (stringBuilder.length() > 1024) {
                stringBuilder = this.zzaiy.substring(0, 1024);
            }
            zzgf.zzajt.zzc(stringBuilder, 1);
            return;
        }
        this.zzajc.zza(6, "Persisted config not initialized. Not logging error/warn");
    }
}
