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
        zzhk zzgh = this.zzajc.zzacr.zzgh();
        if (zzgh.isInitialized()) {
            char c;
            if (this.zzajc.zzaim == '\u0000') {
                zzfg com_google_android_gms_internal_measurement_zzfg;
                if (this.zzajc.zzgi().zzds()) {
                    com_google_android_gms_internal_measurement_zzfg = this.zzajc;
                    c = 'C';
                } else {
                    com_google_android_gms_internal_measurement_zzfg = this.zzajc;
                    c = 'c';
                }
                com_google_android_gms_internal_measurement_zzfg.zzaim = c;
            }
            if (this.zzajc.zzadp < 0) {
                this.zzajc.zzadp = 12451;
            }
            char charAt = "01VDIWEA?".charAt(this.zzaix);
            c = this.zzajc.zzaim;
            long zzb = this.zzajc.zzadp;
            String zza = zzfg.zza(true, this.zzaiy, this.zzaiz, this.zzaja, this.zzajb);
            StringBuilder stringBuilder = new StringBuilder(24 + String.valueOf(zza).length());
            stringBuilder.append("2");
            stringBuilder.append(charAt);
            stringBuilder.append(c);
            stringBuilder.append(zzb);
            stringBuilder.append(":");
            stringBuilder.append(zza);
            String stringBuilder2 = stringBuilder.toString();
            if (stringBuilder2.length() > 1024) {
                stringBuilder2 = this.zzaiy.substring(0, 1024);
            }
            zzgh.zzajs.zzc(stringBuilder2, 1);
            return;
        }
        this.zzajc.zza(6, "Persisted config not initialized. Not logging error/warn");
    }
}
