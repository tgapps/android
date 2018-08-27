package com.google.android.gms.measurement.internal;

final class zzaq implements Runnable {
    private final /* synthetic */ int zzamh;
    private final /* synthetic */ String zzami;
    private final /* synthetic */ Object zzamj;
    private final /* synthetic */ Object zzamk;
    private final /* synthetic */ Object zzaml;
    private final /* synthetic */ zzap zzamm;

    zzaq(zzap com_google_android_gms_measurement_internal_zzap, int i, String str, Object obj, Object obj2, Object obj3) {
        this.zzamm = com_google_android_gms_measurement_internal_zzap;
        this.zzamh = i;
        this.zzami = str;
        this.zzamj = obj;
        this.zzamk = obj2;
        this.zzaml = obj3;
    }

    public final void run() {
        zzcp zzgp = this.zzamm.zzadj.zzgp();
        if (zzgp.isInitialized()) {
            if (this.zzamm.zzalw == '\u0000') {
                zzap com_google_android_gms_measurement_internal_zzap;
                if (this.zzamm.zzgq().zzdw()) {
                    com_google_android_gms_measurement_internal_zzap = this.zzamm;
                    this.zzamm.zzgr();
                    com_google_android_gms_measurement_internal_zzap.zzalw = 'C';
                } else {
                    com_google_android_gms_measurement_internal_zzap = this.zzamm;
                    this.zzamm.zzgr();
                    com_google_android_gms_measurement_internal_zzap.zzalw = 'c';
                }
            }
            if (this.zzamm.zzadt < 0) {
                this.zzamm.zzadt = this.zzamm.zzgq().zzhc();
            }
            char charAt = "01VDIWEA?".charAt(this.zzamh);
            char zza = this.zzamm.zzalw;
            long zzb = this.zzamm.zzadt;
            String zza2 = zzap.zza(true, this.zzami, this.zzamj, this.zzamk, this.zzaml);
            String stringBuilder = new StringBuilder(String.valueOf(zza2).length() + 24).append("2").append(charAt).append(zza).append(zzb).append(":").append(zza2).toString();
            if (stringBuilder.length() > 1024) {
                stringBuilder = this.zzami.substring(0, 1024);
            }
            zzgp.zzand.zzc(stringBuilder, 1);
            return;
        }
        this.zzamm.zza(6, "Persisted config not initialized. Not logging error/warn");
    }
}
