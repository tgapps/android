package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkk extends zzabd<zzkk> {
    public zzkl[] zzata;

    public zzkk() {
        this.zzata = zzkl.zzlf();
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkk)) {
            return false;
        }
        zzkk com_google_android_gms_internal_measurement_zzkk = (zzkk) obj;
        return !zzabh.equals(this.zzata, com_google_android_gms_internal_measurement_zzkk.zzata) ? false : (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzkk.zzbzh == null || com_google_android_gms_internal_measurement_zzkk.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkk.zzbzh);
    }

    public final int hashCode() {
        int hashCode = (((getClass().getName().hashCode() + 527) * 31) + zzabh.hashCode(this.zzata)) * 31;
        int hashCode2 = (this.zzbzh == null || this.zzbzh.isEmpty()) ? 0 : this.zzbzh.hashCode();
        return hashCode2 + hashCode;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzata != null && this.zzata.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzata) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    zza += zzabb.zzb(1, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        return zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzata != null && this.zzata.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzata) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(1, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            switch (zzvo) {
                case 0:
                    break;
                case 10:
                    int zzb = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 10);
                    zzvo = this.zzata == null ? 0 : this.zzata.length;
                    Object obj = new zzkl[(zzb + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzata, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = new zzkl();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = new zzkl();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                    this.zzata = obj;
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
