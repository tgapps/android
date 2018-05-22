package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkp extends zzaby<zzkp> {
    public zzkq[] zzatf;

    public zzkp() {
        this.zzatf = zzkq.zzln();
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkp)) {
            return false;
        }
        zzkp com_google_android_gms_internal_measurement_zzkp = (zzkp) obj;
        return !zzacc.equals(this.zzatf, com_google_android_gms_internal_measurement_zzkp.zzatf) ? false : (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzkp.zzbww == null || com_google_android_gms_internal_measurement_zzkp.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzkp.zzbww);
    }

    public final int hashCode() {
        int hashCode = (((getClass().getName().hashCode() + 527) * 31) + zzacc.hashCode(this.zzatf)) * 31;
        int hashCode2 = (this.zzbww == null || this.zzbww.isEmpty()) ? 0 : this.zzbww.hashCode();
        return hashCode2 + hashCode;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzatf != null && this.zzatf.length > 0) {
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzatf) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    zza += zzabw.zzb(1, com_google_android_gms_internal_measurement_zzace);
                }
            }
        }
        return zza;
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        if (this.zzatf != null && this.zzatf.length > 0) {
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzatf) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(1, com_google_android_gms_internal_measurement_zzace);
                }
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        while (true) {
            int zzuw = com_google_android_gms_internal_measurement_zzabv.zzuw();
            switch (zzuw) {
                case 0:
                    break;
                case 10:
                    int zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 10);
                    zzuw = this.zzatf == null ? 0 : this.zzatf.length;
                    Object obj = new zzkq[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzatf, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkq();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkq();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzatf = obj;
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzabv, zzuw)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
