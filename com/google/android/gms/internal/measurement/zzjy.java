package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzjy extends zzabd<zzjy> {
    private static volatile zzjy[] zzarf;
    public Integer zzarg;
    public zzkc[] zzarh;
    public zzjz[] zzari;

    public zzjy() {
        this.zzarg = null;
        this.zzarh = zzkc.zzkz();
        this.zzari = zzjz.zzkx();
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzjy[] zzkw() {
        if (zzarf == null) {
            synchronized (zzabh.zzbzr) {
                if (zzarf == null) {
                    zzarf = new zzjy[0];
                }
            }
        }
        return zzarf;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzjy)) {
            return false;
        }
        zzjy com_google_android_gms_internal_measurement_zzjy = (zzjy) obj;
        if (this.zzarg == null) {
            if (com_google_android_gms_internal_measurement_zzjy.zzarg != null) {
                return false;
            }
        } else if (!this.zzarg.equals(com_google_android_gms_internal_measurement_zzjy.zzarg)) {
            return false;
        }
        return !zzabh.equals(this.zzarh, com_google_android_gms_internal_measurement_zzjy.zzarh) ? false : !zzabh.equals(this.zzari, com_google_android_gms_internal_measurement_zzjy.zzari) ? false : (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzjy.zzbzh == null || com_google_android_gms_internal_measurement_zzjy.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzjy.zzbzh);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((((this.zzarg == null ? 0 : this.zzarg.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + zzabh.hashCode(this.zzarh)) * 31) + zzabh.hashCode(this.zzari)) * 31;
        if (!(this.zzbzh == null || this.zzbzh.isEmpty())) {
            i = this.zzbzh.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int i = 0;
        int zza = super.zza();
        if (this.zzarg != null) {
            zza += zzabb.zzf(1, this.zzarg.intValue());
        }
        if (this.zzarh != null && this.zzarh.length > 0) {
            int i2 = zza;
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzarh) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    i2 += zzabb.zzb(2, com_google_android_gms_internal_measurement_zzabj);
                }
            }
            zza = i2;
        }
        if (this.zzari != null && this.zzari.length > 0) {
            while (i < this.zzari.length) {
                zzabj com_google_android_gms_internal_measurement_zzabj2 = this.zzari[i];
                if (com_google_android_gms_internal_measurement_zzabj2 != null) {
                    zza += zzabb.zzb(3, com_google_android_gms_internal_measurement_zzabj2);
                }
                i++;
            }
        }
        return zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        int i = 0;
        if (this.zzarg != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(1, this.zzarg.intValue());
        }
        if (this.zzarh != null && this.zzarh.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzarh) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(2, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        if (this.zzari != null && this.zzari.length > 0) {
            while (i < this.zzari.length) {
                zzabj com_google_android_gms_internal_measurement_zzabj2 = this.zzari[i];
                if (com_google_android_gms_internal_measurement_zzabj2 != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(3, com_google_android_gms_internal_measurement_zzabj2);
                }
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
            switch (zzvo) {
                case 0:
                    break;
                case 8:
                    this.zzarg = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    continue;
                case 18:
                    zzb = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 18);
                    zzvo = this.zzarh == null ? 0 : this.zzarh.length;
                    obj = new zzkc[(zzb + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzarh, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = new zzkc();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = new zzkc();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                    this.zzarh = obj;
                    continue;
                case 26:
                    zzb = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 26);
                    zzvo = this.zzari == null ? 0 : this.zzari.length;
                    obj = new zzjz[(zzb + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzari, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = new zzjz();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = new zzjz();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                    this.zzari = obj;
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
