package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.messenger.exoplayer2.RendererCapabilities;

public final class zzkf extends zzabd<zzkf> {
    public String zzadh;
    public Long zzask;
    private Integer zzasl;
    public zzkg[] zzasm;
    public zzke[] zzasn;
    public zzjy[] zzaso;

    public zzkf() {
        this.zzask = null;
        this.zzadh = null;
        this.zzasl = null;
        this.zzasm = zzkg.zzlb();
        this.zzasn = zzke.zzla();
        this.zzaso = zzjy.zzkw();
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkf)) {
            return false;
        }
        zzkf com_google_android_gms_internal_measurement_zzkf = (zzkf) obj;
        if (this.zzask == null) {
            if (com_google_android_gms_internal_measurement_zzkf.zzask != null) {
                return false;
            }
        } else if (!this.zzask.equals(com_google_android_gms_internal_measurement_zzkf.zzask)) {
            return false;
        }
        if (this.zzadh == null) {
            if (com_google_android_gms_internal_measurement_zzkf.zzadh != null) {
                return false;
            }
        } else if (!this.zzadh.equals(com_google_android_gms_internal_measurement_zzkf.zzadh)) {
            return false;
        }
        if (this.zzasl == null) {
            if (com_google_android_gms_internal_measurement_zzkf.zzasl != null) {
                return false;
            }
        } else if (!this.zzasl.equals(com_google_android_gms_internal_measurement_zzkf.zzasl)) {
            return false;
        }
        return !zzabh.equals(this.zzasm, com_google_android_gms_internal_measurement_zzkf.zzasm) ? false : !zzabh.equals(this.zzasn, com_google_android_gms_internal_measurement_zzkf.zzasn) ? false : !zzabh.equals(this.zzaso, com_google_android_gms_internal_measurement_zzkf.zzaso) ? false : (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzkf.zzbzh == null || com_google_android_gms_internal_measurement_zzkf.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkf.zzbzh);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((((((this.zzasl == null ? 0 : this.zzasl.hashCode()) + (((this.zzadh == null ? 0 : this.zzadh.hashCode()) + (((this.zzask == null ? 0 : this.zzask.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + zzabh.hashCode(this.zzasm)) * 31) + zzabh.hashCode(this.zzasn)) * 31) + zzabh.hashCode(this.zzaso)) * 31;
        if (!(this.zzbzh == null || this.zzbzh.isEmpty())) {
            i = this.zzbzh.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int i;
        int i2 = 0;
        int zza = super.zza();
        if (this.zzask != null) {
            zza += zzabb.zzc(1, this.zzask.longValue());
        }
        if (this.zzadh != null) {
            zza += zzabb.zzd(2, this.zzadh);
        }
        if (this.zzasl != null) {
            zza += zzabb.zzf(3, this.zzasl.intValue());
        }
        if (this.zzasm != null && this.zzasm.length > 0) {
            i = zza;
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzasm) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    i += zzabb.zzb(4, com_google_android_gms_internal_measurement_zzabj);
                }
            }
            zza = i;
        }
        if (this.zzasn != null && this.zzasn.length > 0) {
            i = zza;
            for (zzabj com_google_android_gms_internal_measurement_zzabj2 : this.zzasn) {
                if (com_google_android_gms_internal_measurement_zzabj2 != null) {
                    i += zzabb.zzb(5, com_google_android_gms_internal_measurement_zzabj2);
                }
            }
            zza = i;
        }
        if (this.zzaso != null && this.zzaso.length > 0) {
            while (i2 < this.zzaso.length) {
                zzabj com_google_android_gms_internal_measurement_zzabj3 = this.zzaso[i2];
                if (com_google_android_gms_internal_measurement_zzabj3 != null) {
                    zza += zzabb.zzb(6, com_google_android_gms_internal_measurement_zzabj3);
                }
                i2++;
            }
        }
        return zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        int i = 0;
        if (this.zzask != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(1, this.zzask.longValue());
        }
        if (this.zzadh != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(2, this.zzadh);
        }
        if (this.zzasl != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(3, this.zzasl.intValue());
        }
        if (this.zzasm != null && this.zzasm.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzasm) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(4, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        if (this.zzasn != null && this.zzasn.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj2 : this.zzasn) {
                if (com_google_android_gms_internal_measurement_zzabj2 != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(5, com_google_android_gms_internal_measurement_zzabj2);
                }
            }
        }
        if (this.zzaso != null && this.zzaso.length > 0) {
            while (i < this.zzaso.length) {
                zzabj com_google_android_gms_internal_measurement_zzabj3 = this.zzaso[i];
                if (com_google_android_gms_internal_measurement_zzabj3 != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(6, com_google_android_gms_internal_measurement_zzabj3);
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
                    this.zzask = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    continue;
                case 18:
                    this.zzadh = com_google_android_gms_internal_measurement_zzaba.readString();
                    continue;
                case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                    this.zzasl = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    continue;
                case 34:
                    zzb = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 34);
                    zzvo = this.zzasm == null ? 0 : this.zzasm.length;
                    obj = new zzkg[(zzb + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzasm, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = new zzkg();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = new zzkg();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                    this.zzasm = obj;
                    continue;
                case 42:
                    zzb = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 42);
                    zzvo = this.zzasn == null ? 0 : this.zzasn.length;
                    obj = new zzke[(zzb + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzasn, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = new zzke();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = new zzke();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                    this.zzasn = obj;
                    continue;
                case 50:
                    zzb = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 50);
                    zzvo = this.zzaso == null ? 0 : this.zzaso.length;
                    obj = new zzjy[(zzb + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzaso, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = new zzjy();
                        com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = new zzjy();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[zzvo]);
                    this.zzaso = obj;
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
