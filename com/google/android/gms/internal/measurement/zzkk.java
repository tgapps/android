package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.messenger.exoplayer2.RendererCapabilities;

public final class zzkk extends zzaby<zzkk> {
    public String zzadm;
    public Long zzasp;
    private Integer zzasq;
    public zzkl[] zzasr;
    public zzkj[] zzass;
    public zzkd[] zzast;

    public zzkk() {
        this.zzasp = null;
        this.zzadm = null;
        this.zzasq = null;
        this.zzasr = zzkl.zzlj();
        this.zzass = zzkj.zzli();
        this.zzast = zzkd.zzle();
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkk)) {
            return false;
        }
        zzkk com_google_android_gms_internal_measurement_zzkk = (zzkk) obj;
        if (this.zzasp == null) {
            if (com_google_android_gms_internal_measurement_zzkk.zzasp != null) {
                return false;
            }
        } else if (!this.zzasp.equals(com_google_android_gms_internal_measurement_zzkk.zzasp)) {
            return false;
        }
        if (this.zzadm == null) {
            if (com_google_android_gms_internal_measurement_zzkk.zzadm != null) {
                return false;
            }
        } else if (!this.zzadm.equals(com_google_android_gms_internal_measurement_zzkk.zzadm)) {
            return false;
        }
        if (this.zzasq == null) {
            if (com_google_android_gms_internal_measurement_zzkk.zzasq != null) {
                return false;
            }
        } else if (!this.zzasq.equals(com_google_android_gms_internal_measurement_zzkk.zzasq)) {
            return false;
        }
        return !zzacc.equals(this.zzasr, com_google_android_gms_internal_measurement_zzkk.zzasr) ? false : !zzacc.equals(this.zzass, com_google_android_gms_internal_measurement_zzkk.zzass) ? false : !zzacc.equals(this.zzast, com_google_android_gms_internal_measurement_zzkk.zzast) ? false : (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzkk.zzbww == null || com_google_android_gms_internal_measurement_zzkk.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzkk.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((((((this.zzasq == null ? 0 : this.zzasq.hashCode()) + (((this.zzadm == null ? 0 : this.zzadm.hashCode()) + (((this.zzasp == null ? 0 : this.zzasp.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + zzacc.hashCode(this.zzasr)) * 31) + zzacc.hashCode(this.zzass)) * 31) + zzacc.hashCode(this.zzast)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int i;
        int i2 = 0;
        int zza = super.zza();
        if (this.zzasp != null) {
            zza += zzabw.zzc(1, this.zzasp.longValue());
        }
        if (this.zzadm != null) {
            zza += zzabw.zzc(2, this.zzadm);
        }
        if (this.zzasq != null) {
            zza += zzabw.zzf(3, this.zzasq.intValue());
        }
        if (this.zzasr != null && this.zzasr.length > 0) {
            i = zza;
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzasr) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    i += zzabw.zzb(4, com_google_android_gms_internal_measurement_zzace);
                }
            }
            zza = i;
        }
        if (this.zzass != null && this.zzass.length > 0) {
            i = zza;
            for (zzace com_google_android_gms_internal_measurement_zzace2 : this.zzass) {
                if (com_google_android_gms_internal_measurement_zzace2 != null) {
                    i += zzabw.zzb(5, com_google_android_gms_internal_measurement_zzace2);
                }
            }
            zza = i;
        }
        if (this.zzast != null && this.zzast.length > 0) {
            while (i2 < this.zzast.length) {
                zzace com_google_android_gms_internal_measurement_zzace3 = this.zzast[i2];
                if (com_google_android_gms_internal_measurement_zzace3 != null) {
                    zza += zzabw.zzb(6, com_google_android_gms_internal_measurement_zzace3);
                }
                i2++;
            }
        }
        return zza;
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        int i = 0;
        if (this.zzasp != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(1, this.zzasp.longValue());
        }
        if (this.zzadm != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(2, this.zzadm);
        }
        if (this.zzasq != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(3, this.zzasq.intValue());
        }
        if (this.zzasr != null && this.zzasr.length > 0) {
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzasr) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(4, com_google_android_gms_internal_measurement_zzace);
                }
            }
        }
        if (this.zzass != null && this.zzass.length > 0) {
            for (zzace com_google_android_gms_internal_measurement_zzace2 : this.zzass) {
                if (com_google_android_gms_internal_measurement_zzace2 != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(5, com_google_android_gms_internal_measurement_zzace2);
                }
            }
        }
        if (this.zzast != null && this.zzast.length > 0) {
            while (i < this.zzast.length) {
                zzace com_google_android_gms_internal_measurement_zzace3 = this.zzast[i];
                if (com_google_android_gms_internal_measurement_zzace3 != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(6, com_google_android_gms_internal_measurement_zzace3);
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
                    this.zzasp = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 18:
                    this.zzadm = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                    this.zzasq = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 34:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 34);
                    zzuw = this.zzasr == null ? 0 : this.zzasr.length;
                    obj = new zzkl[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzasr, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkl();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkl();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzasr = obj;
                    continue;
                case 42:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 42);
                    zzuw = this.zzass == null ? 0 : this.zzass.length;
                    obj = new zzkj[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzass, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkj();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkj();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzass = obj;
                    continue;
                case 50:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 50);
                    zzuw = this.zzast == null ? 0 : this.zzast.length;
                    obj = new zzkd[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzast, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkd();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkd();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzast = obj;
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
