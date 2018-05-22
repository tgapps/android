package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkm extends zzaby<zzkm> {
    private static volatile zzkm[] zzasv;
    public Integer zzarl;
    public zzkr zzasw;
    public zzkr zzasx;
    public Boolean zzasy;

    public zzkm() {
        this.zzarl = null;
        this.zzasw = null;
        this.zzasx = null;
        this.zzasy = null;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkm[] zzlk() {
        if (zzasv == null) {
            synchronized (zzacc.zzbxg) {
                if (zzasv == null) {
                    zzasv = new zzkm[0];
                }
            }
        }
        return zzasv;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkm)) {
            return false;
        }
        zzkm com_google_android_gms_internal_measurement_zzkm = (zzkm) obj;
        if (this.zzarl == null) {
            if (com_google_android_gms_internal_measurement_zzkm.zzarl != null) {
                return false;
            }
        } else if (!this.zzarl.equals(com_google_android_gms_internal_measurement_zzkm.zzarl)) {
            return false;
        }
        if (this.zzasw == null) {
            if (com_google_android_gms_internal_measurement_zzkm.zzasw != null) {
                return false;
            }
        } else if (!this.zzasw.equals(com_google_android_gms_internal_measurement_zzkm.zzasw)) {
            return false;
        }
        if (this.zzasx == null) {
            if (com_google_android_gms_internal_measurement_zzkm.zzasx != null) {
                return false;
            }
        } else if (!this.zzasx.equals(com_google_android_gms_internal_measurement_zzkm.zzasx)) {
            return false;
        }
        if (this.zzasy == null) {
            if (com_google_android_gms_internal_measurement_zzkm.zzasy != null) {
                return false;
            }
        } else if (!this.zzasy.equals(com_google_android_gms_internal_measurement_zzkm.zzasy)) {
            return false;
        }
        return (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzkm.zzbww == null || com_google_android_gms_internal_measurement_zzkm.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzkm.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (this.zzarl == null ? 0 : this.zzarl.hashCode()) + ((getClass().getName().hashCode() + 527) * 31);
        zzkr com_google_android_gms_internal_measurement_zzkr = this.zzasw;
        hashCode = (com_google_android_gms_internal_measurement_zzkr == null ? 0 : com_google_android_gms_internal_measurement_zzkr.hashCode()) + (hashCode * 31);
        com_google_android_gms_internal_measurement_zzkr = this.zzasx;
        hashCode = ((this.zzasy == null ? 0 : this.zzasy.hashCode()) + (((com_google_android_gms_internal_measurement_zzkr == null ? 0 : com_google_android_gms_internal_measurement_zzkr.hashCode()) + (hashCode * 31)) * 31)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzarl != null) {
            zza += zzabw.zzf(1, this.zzarl.intValue());
        }
        if (this.zzasw != null) {
            zza += zzabw.zzb(2, this.zzasw);
        }
        if (this.zzasx != null) {
            zza += zzabw.zzb(3, this.zzasx);
        }
        if (this.zzasy == null) {
            return zza;
        }
        this.zzasy.booleanValue();
        return zza + (zzabw.zzaq(4) + 1);
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        if (this.zzarl != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(1, this.zzarl.intValue());
        }
        if (this.zzasw != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(2, this.zzasw);
        }
        if (this.zzasx != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(3, this.zzasx);
        }
        if (this.zzasy != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(4, this.zzasy.booleanValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        while (true) {
            int zzuw = com_google_android_gms_internal_measurement_zzabv.zzuw();
            switch (zzuw) {
                case 0:
                    break;
                case 8:
                    this.zzarl = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 18:
                    if (this.zzasw == null) {
                        this.zzasw = new zzkr();
                    }
                    com_google_android_gms_internal_measurement_zzabv.zza(this.zzasw);
                    continue;
                case 26:
                    if (this.zzasx == null) {
                        this.zzasx = new zzkr();
                    }
                    com_google_android_gms_internal_measurement_zzabv.zza(this.zzasx);
                    continue;
                case 32:
                    this.zzasy = Boolean.valueOf(com_google_android_gms_internal_measurement_zzabv.zzux());
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
