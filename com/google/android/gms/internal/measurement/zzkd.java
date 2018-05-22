package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkd extends zzaby<zzkd> {
    private static volatile zzkd[] zzark;
    public Integer zzarl;
    public zzkh[] zzarm;
    public zzke[] zzarn;

    public zzkd() {
        this.zzarl = null;
        this.zzarm = zzkh.zzlh();
        this.zzarn = zzke.zzlf();
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkd[] zzle() {
        if (zzark == null) {
            synchronized (zzacc.zzbxg) {
                if (zzark == null) {
                    zzark = new zzkd[0];
                }
            }
        }
        return zzark;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkd)) {
            return false;
        }
        zzkd com_google_android_gms_internal_measurement_zzkd = (zzkd) obj;
        if (this.zzarl == null) {
            if (com_google_android_gms_internal_measurement_zzkd.zzarl != null) {
                return false;
            }
        } else if (!this.zzarl.equals(com_google_android_gms_internal_measurement_zzkd.zzarl)) {
            return false;
        }
        return !zzacc.equals(this.zzarm, com_google_android_gms_internal_measurement_zzkd.zzarm) ? false : !zzacc.equals(this.zzarn, com_google_android_gms_internal_measurement_zzkd.zzarn) ? false : (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzkd.zzbww == null || com_google_android_gms_internal_measurement_zzkd.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzkd.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((((this.zzarl == null ? 0 : this.zzarl.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + zzacc.hashCode(this.zzarm)) * 31) + zzacc.hashCode(this.zzarn)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int i = 0;
        int zza = super.zza();
        if (this.zzarl != null) {
            zza += zzabw.zzf(1, this.zzarl.intValue());
        }
        if (this.zzarm != null && this.zzarm.length > 0) {
            int i2 = zza;
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzarm) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    i2 += zzabw.zzb(2, com_google_android_gms_internal_measurement_zzace);
                }
            }
            zza = i2;
        }
        if (this.zzarn != null && this.zzarn.length > 0) {
            while (i < this.zzarn.length) {
                zzace com_google_android_gms_internal_measurement_zzace2 = this.zzarn[i];
                if (com_google_android_gms_internal_measurement_zzace2 != null) {
                    zza += zzabw.zzb(3, com_google_android_gms_internal_measurement_zzace2);
                }
                i++;
            }
        }
        return zza;
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        int i = 0;
        if (this.zzarl != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(1, this.zzarl.intValue());
        }
        if (this.zzarm != null && this.zzarm.length > 0) {
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzarm) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(2, com_google_android_gms_internal_measurement_zzace);
                }
            }
        }
        if (this.zzarn != null && this.zzarn.length > 0) {
            while (i < this.zzarn.length) {
                zzace com_google_android_gms_internal_measurement_zzace2 = this.zzarn[i];
                if (com_google_android_gms_internal_measurement_zzace2 != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(3, com_google_android_gms_internal_measurement_zzace2);
                }
                i++;
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        while (true) {
            int zzuw = com_google_android_gms_internal_measurement_zzabv.zzuw();
            int zzb;
            Object obj;
            switch (zzuw) {
                case 0:
                    break;
                case 8:
                    this.zzarl = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 18:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 18);
                    zzuw = this.zzarm == null ? 0 : this.zzarm.length;
                    obj = new zzkh[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzarm, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkh();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkh();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzarm = obj;
                    continue;
                case 26:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 26);
                    zzuw = this.zzarn == null ? 0 : this.zzarn.length;
                    obj = new zzke[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzarn, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzke();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzke();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzarn = obj;
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
