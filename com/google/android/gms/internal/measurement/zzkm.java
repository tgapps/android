package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkm extends zzabd<zzkm> {
    public long[] zzauf;
    public long[] zzaug;

    public zzkm() {
        this.zzauf = zzabm.zzbzy;
        this.zzaug = zzabm.zzbzy;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkm)) {
            return false;
        }
        zzkm com_google_android_gms_internal_measurement_zzkm = (zzkm) obj;
        return !zzabh.equals(this.zzauf, com_google_android_gms_internal_measurement_zzkm.zzauf) ? false : !zzabh.equals(this.zzaug, com_google_android_gms_internal_measurement_zzkm.zzaug) ? false : (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzkm.zzbzh == null || com_google_android_gms_internal_measurement_zzkm.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkm.zzbzh);
    }

    public final int hashCode() {
        int hashCode = (((((getClass().getName().hashCode() + 527) * 31) + zzabh.hashCode(this.zzauf)) * 31) + zzabh.hashCode(this.zzaug)) * 31;
        int hashCode2 = (this.zzbzh == null || this.zzbzh.isEmpty()) ? 0 : this.zzbzh.hashCode();
        return hashCode2 + hashCode;
    }

    protected final int zza() {
        int i;
        int i2;
        int zza = super.zza();
        if (this.zzauf == null || this.zzauf.length <= 0) {
            i = zza;
        } else {
            i2 = 0;
            for (long zzap : this.zzauf) {
                i2 += zzabb.zzap(zzap);
            }
            i = (zza + i2) + (this.zzauf.length * 1);
        }
        if (this.zzaug == null || this.zzaug.length <= 0) {
            return i;
        }
        zza = 0;
        for (long zzap2 : this.zzaug) {
            zza += zzabb.zzap(zzap2);
        }
        return (i + zza) + (this.zzaug.length * 1);
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        int i = 0;
        if (this.zzauf != null && this.zzauf.length > 0) {
            for (long zza : this.zzauf) {
                com_google_android_gms_internal_measurement_zzabb.zza(1, zza);
            }
        }
        if (this.zzaug != null && this.zzaug.length > 0) {
            while (i < this.zzaug.length) {
                com_google_android_gms_internal_measurement_zzabb.zza(2, this.zzaug[i]);
                i++;
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            int zzb;
            Object obj;
            int zzah;
            Object obj2;
            switch (zzvo) {
                case 0:
                    break;
                case 8:
                    zzb = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 8);
                    zzvo = this.zzauf == null ? 0 : this.zzauf.length;
                    obj = new long[(zzb + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzauf, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                    this.zzauf = obj;
                    continue;
                case 10:
                    zzah = com_google_android_gms_internal_measurement_zzaba.zzah(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    zzb = com_google_android_gms_internal_measurement_zzaba.getPosition();
                    zzvo = 0;
                    while (com_google_android_gms_internal_measurement_zzaba.zzvw() > 0) {
                        com_google_android_gms_internal_measurement_zzaba.zzvt();
                        zzvo++;
                    }
                    com_google_android_gms_internal_measurement_zzaba.zzao(zzb);
                    zzb = this.zzauf == null ? 0 : this.zzauf.length;
                    obj2 = new long[(zzvo + zzb)];
                    if (zzb != 0) {
                        System.arraycopy(this.zzauf, 0, obj2, 0, zzb);
                    }
                    while (zzb < obj2.length) {
                        obj2[zzb] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                        zzb++;
                    }
                    this.zzauf = obj2;
                    com_google_android_gms_internal_measurement_zzaba.zzan(zzah);
                    continue;
                case 16:
                    zzb = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 16);
                    zzvo = this.zzaug == null ? 0 : this.zzaug.length;
                    obj = new long[(zzb + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzaug, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                    this.zzaug = obj;
                    continue;
                case 18:
                    zzah = com_google_android_gms_internal_measurement_zzaba.zzah(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    zzb = com_google_android_gms_internal_measurement_zzaba.getPosition();
                    zzvo = 0;
                    while (com_google_android_gms_internal_measurement_zzaba.zzvw() > 0) {
                        com_google_android_gms_internal_measurement_zzaba.zzvt();
                        zzvo++;
                    }
                    com_google_android_gms_internal_measurement_zzaba.zzao(zzb);
                    zzb = this.zzaug == null ? 0 : this.zzaug.length;
                    obj2 = new long[(zzvo + zzb)];
                    if (zzb != 0) {
                        System.arraycopy(this.zzaug, 0, obj2, 0, zzb);
                    }
                    while (zzb < obj2.length) {
                        obj2[zzb] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                        zzb++;
                    }
                    this.zzaug = obj2;
                    com_google_android_gms_internal_measurement_zzaba.zzan(zzah);
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
