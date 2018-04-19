package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzjz extends zzabd<zzjz> {
    private static volatile zzjz[] zzarj;
    public Integer zzark;
    public String zzarl;
    public zzka[] zzarm;
    private Boolean zzarn;
    public zzkb zzaro;

    public zzjz() {
        this.zzark = null;
        this.zzarl = null;
        this.zzarm = zzka.zzky();
        this.zzarn = null;
        this.zzaro = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzjz[] zzkx() {
        if (zzarj == null) {
            synchronized (zzabh.zzbzr) {
                if (zzarj == null) {
                    zzarj = new zzjz[0];
                }
            }
        }
        return zzarj;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzjz)) {
            return false;
        }
        zzjz com_google_android_gms_internal_measurement_zzjz = (zzjz) obj;
        if (this.zzark == null) {
            if (com_google_android_gms_internal_measurement_zzjz.zzark != null) {
                return false;
            }
        } else if (!this.zzark.equals(com_google_android_gms_internal_measurement_zzjz.zzark)) {
            return false;
        }
        if (this.zzarl == null) {
            if (com_google_android_gms_internal_measurement_zzjz.zzarl != null) {
                return false;
            }
        } else if (!this.zzarl.equals(com_google_android_gms_internal_measurement_zzjz.zzarl)) {
            return false;
        }
        if (!zzabh.equals(this.zzarm, com_google_android_gms_internal_measurement_zzjz.zzarm)) {
            return false;
        }
        if (this.zzarn == null) {
            if (com_google_android_gms_internal_measurement_zzjz.zzarn != null) {
                return false;
            }
        } else if (!this.zzarn.equals(com_google_android_gms_internal_measurement_zzjz.zzarn)) {
            return false;
        }
        if (this.zzaro == null) {
            if (com_google_android_gms_internal_measurement_zzjz.zzaro != null) {
                return false;
            }
        } else if (!this.zzaro.equals(com_google_android_gms_internal_measurement_zzjz.zzaro)) {
            return false;
        }
        return (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzjz.zzbzh == null || com_google_android_gms_internal_measurement_zzjz.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzjz.zzbzh);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (this.zzarn == null ? 0 : this.zzarn.hashCode()) + (((((this.zzarl == null ? 0 : this.zzarl.hashCode()) + (((this.zzark == null ? 0 : this.zzark.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31) + zzabh.hashCode(this.zzarm)) * 31);
        zzkb com_google_android_gms_internal_measurement_zzkb = this.zzaro;
        hashCode = ((com_google_android_gms_internal_measurement_zzkb == null ? 0 : com_google_android_gms_internal_measurement_zzkb.hashCode()) + (hashCode * 31)) * 31;
        if (!(this.zzbzh == null || this.zzbzh.isEmpty())) {
            i = this.zzbzh.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzark != null) {
            zza += zzabb.zzf(1, this.zzark.intValue());
        }
        if (this.zzarl != null) {
            zza += zzabb.zzd(2, this.zzarl);
        }
        if (this.zzarm != null && this.zzarm.length > 0) {
            int i = zza;
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzarm) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    i += zzabb.zzb(3, com_google_android_gms_internal_measurement_zzabj);
                }
            }
            zza = i;
        }
        if (this.zzarn != null) {
            this.zzarn.booleanValue();
            zza += zzabb.zzas(4) + 1;
        }
        return this.zzaro != null ? zza + zzabb.zzb(5, this.zzaro) : zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzark != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(1, this.zzark.intValue());
        }
        if (this.zzarl != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(2, this.zzarl);
        }
        if (this.zzarm != null && this.zzarm.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzarm) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(3, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        if (this.zzarn != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(4, this.zzarn.booleanValue());
        }
        if (this.zzaro != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(5, this.zzaro);
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            switch (zzvo) {
                case 0:
                    break;
                case 8:
                    this.zzark = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    continue;
                case 18:
                    this.zzarl = com_google_android_gms_internal_measurement_zzaba.readString();
                    continue;
                case 26:
                    int zzb = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 26);
                    zzvo = this.zzarm == null ? 0 : this.zzarm.length;
                    Object obj = new zzka[(zzb + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzarm, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = new zzka();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = new zzka();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                    this.zzarm = obj;
                    continue;
                case 32:
                    this.zzarn = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
                    continue;
                case 42:
                    if (this.zzaro == null) {
                        this.zzaro = new zzkb();
                    }
                    com_google_android_gms_internal_measurement_zzaba.zza(this.zzaro);
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
