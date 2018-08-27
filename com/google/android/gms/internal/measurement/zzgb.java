package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzgb extends zzza<zzgb> {
    public String zzafx;
    public Long zzawe;
    private Integer zzawf;
    public zzgc[] zzawg;
    public zzga[] zzawh;
    public zzfu[] zzawi;
    private String zzawj;

    public zzgb() {
        this.zzawe = null;
        this.zzafx = null;
        this.zzawf = null;
        this.zzawg = zzgc.zzmn();
        this.zzawh = zzga.zzmm();
        this.zzawi = zzfu.zzmi();
        this.zzawj = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgb)) {
            return false;
        }
        zzgb com_google_android_gms_internal_measurement_zzgb = (zzgb) obj;
        if (this.zzawe == null) {
            if (com_google_android_gms_internal_measurement_zzgb.zzawe != null) {
                return false;
            }
        } else if (!this.zzawe.equals(com_google_android_gms_internal_measurement_zzgb.zzawe)) {
            return false;
        }
        if (this.zzafx == null) {
            if (com_google_android_gms_internal_measurement_zzgb.zzafx != null) {
                return false;
            }
        } else if (!this.zzafx.equals(com_google_android_gms_internal_measurement_zzgb.zzafx)) {
            return false;
        }
        if (this.zzawf == null) {
            if (com_google_android_gms_internal_measurement_zzgb.zzawf != null) {
                return false;
            }
        } else if (!this.zzawf.equals(com_google_android_gms_internal_measurement_zzgb.zzawf)) {
            return false;
        }
        if (!zzze.equals(this.zzawg, com_google_android_gms_internal_measurement_zzgb.zzawg)) {
            return false;
        }
        if (!zzze.equals(this.zzawh, com_google_android_gms_internal_measurement_zzgb.zzawh)) {
            return false;
        }
        if (!zzze.equals(this.zzawi, com_google_android_gms_internal_measurement_zzgb.zzawi)) {
            return false;
        }
        if (this.zzawj == null) {
            if (com_google_android_gms_internal_measurement_zzgb.zzawj != null) {
                return false;
            }
        } else if (!this.zzawj.equals(com_google_android_gms_internal_measurement_zzgb.zzawj)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgb.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgb.zzcfc == null || com_google_android_gms_internal_measurement_zzgb.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzawj == null ? 0 : this.zzawj.hashCode()) + (((((((((this.zzawf == null ? 0 : this.zzawf.hashCode()) + (((this.zzafx == null ? 0 : this.zzafx.hashCode()) + (((this.zzawe == null ? 0 : this.zzawe.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + zzze.hashCode(this.zzawg)) * 31) + zzze.hashCode(this.zzawh)) * 31) + zzze.hashCode(this.zzawi)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        int i = 0;
        if (this.zzawe != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(1, this.zzawe.longValue());
        }
        if (this.zzafx != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(2, this.zzafx);
        }
        if (this.zzawf != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(3, this.zzawf.intValue());
        }
        if (this.zzawg != null && this.zzawg.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzawg) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(4, com_google_android_gms_internal_measurement_zzzg);
                }
            }
        }
        if (this.zzawh != null && this.zzawh.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg2 : this.zzawh) {
                if (com_google_android_gms_internal_measurement_zzzg2 != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(5, com_google_android_gms_internal_measurement_zzzg2);
                }
            }
        }
        if (this.zzawi != null && this.zzawi.length > 0) {
            while (i < this.zzawi.length) {
                zzzg com_google_android_gms_internal_measurement_zzzg3 = this.zzawi[i];
                if (com_google_android_gms_internal_measurement_zzzg3 != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(6, com_google_android_gms_internal_measurement_zzzg3);
                }
                i++;
            }
        }
        if (this.zzawj != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(7, this.zzawj);
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int i;
        int i2 = 0;
        int zzf = super.zzf();
        if (this.zzawe != null) {
            zzf += zzyy.zzd(1, this.zzawe.longValue());
        }
        if (this.zzafx != null) {
            zzf += zzyy.zzc(2, this.zzafx);
        }
        if (this.zzawf != null) {
            zzf += zzyy.zzh(3, this.zzawf.intValue());
        }
        if (this.zzawg != null && this.zzawg.length > 0) {
            i = zzf;
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzawg) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    i += zzyy.zzb(4, com_google_android_gms_internal_measurement_zzzg);
                }
            }
            zzf = i;
        }
        if (this.zzawh != null && this.zzawh.length > 0) {
            i = zzf;
            for (zzzg com_google_android_gms_internal_measurement_zzzg2 : this.zzawh) {
                if (com_google_android_gms_internal_measurement_zzzg2 != null) {
                    i += zzyy.zzb(5, com_google_android_gms_internal_measurement_zzzg2);
                }
            }
            zzf = i;
        }
        if (this.zzawi != null && this.zzawi.length > 0) {
            while (i2 < this.zzawi.length) {
                zzzg com_google_android_gms_internal_measurement_zzzg3 = this.zzawi[i2];
                if (com_google_android_gms_internal_measurement_zzzg3 != null) {
                    zzf += zzyy.zzb(6, com_google_android_gms_internal_measurement_zzzg3);
                }
                i2++;
            }
        }
        if (this.zzawj != null) {
            return zzf + zzyy.zzc(7, this.zzawj);
        }
        return zzf;
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            int zzb;
            Object obj;
            switch (zzug) {
                case 0:
                    break;
                case 8:
                    this.zzawe = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 18:
                    this.zzafx = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 24:
                    this.zzawf = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 34:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 34);
                    zzug = this.zzawg == null ? 0 : this.zzawg.length;
                    obj = new zzgc[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzawg, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzgc();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzgc();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzawg = obj;
                    continue;
                case 42:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 42);
                    zzug = this.zzawh == null ? 0 : this.zzawh.length;
                    obj = new zzga[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzawh, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzga();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzga();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzawh = obj;
                    continue;
                case 50:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 50);
                    zzug = this.zzawi == null ? 0 : this.zzawi.length;
                    obj = new zzfu[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzawi, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzfu();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzfu();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzawi = obj;
                    continue;
                case 58:
                    this.zzawj = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzyx, zzug)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
