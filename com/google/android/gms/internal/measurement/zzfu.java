package com.google.android.gms.internal.measurement;

import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;

public final class zzfu extends zzza<zzfu> {
    private static volatile zzfu[] zzaux;
    public Integer zzauy;
    public zzfy[] zzauz;
    public zzfv[] zzava;
    private Boolean zzavb;
    private Boolean zzavc;

    public static zzfu[] zzmi() {
        if (zzaux == null) {
            synchronized (zzze.zzcfl) {
                if (zzaux == null) {
                    zzaux = new zzfu[0];
                }
            }
        }
        return zzaux;
    }

    public zzfu() {
        this.zzauy = null;
        this.zzauz = zzfy.zzml();
        this.zzava = zzfv.zzmj();
        this.zzavb = null;
        this.zzavc = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfu)) {
            return false;
        }
        zzfu com_google_android_gms_internal_measurement_zzfu = (zzfu) obj;
        if (this.zzauy == null) {
            if (com_google_android_gms_internal_measurement_zzfu.zzauy != null) {
                return false;
            }
        } else if (!this.zzauy.equals(com_google_android_gms_internal_measurement_zzfu.zzauy)) {
            return false;
        }
        if (!zzze.equals(this.zzauz, com_google_android_gms_internal_measurement_zzfu.zzauz)) {
            return false;
        }
        if (!zzze.equals(this.zzava, com_google_android_gms_internal_measurement_zzfu.zzava)) {
            return false;
        }
        if (this.zzavb == null) {
            if (com_google_android_gms_internal_measurement_zzfu.zzavb != null) {
                return false;
            }
        } else if (!this.zzavb.equals(com_google_android_gms_internal_measurement_zzfu.zzavb)) {
            return false;
        }
        if (this.zzavc == null) {
            if (com_google_android_gms_internal_measurement_zzfu.zzavc != null) {
                return false;
            }
        } else if (!this.zzavc.equals(com_google_android_gms_internal_measurement_zzfu.zzavc)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzfu.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzfu.zzcfc == null || com_google_android_gms_internal_measurement_zzfu.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzavc == null ? 0 : this.zzavc.hashCode()) + (((this.zzavb == null ? 0 : this.zzavb.hashCode()) + (((((((this.zzauy == null ? 0 : this.zzauy.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + zzze.hashCode(this.zzauz)) * 31) + zzze.hashCode(this.zzava)) * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        int i = 0;
        if (this.zzauy != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(1, this.zzauy.intValue());
        }
        if (this.zzauz != null && this.zzauz.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzauz) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(2, com_google_android_gms_internal_measurement_zzzg);
                }
            }
        }
        if (this.zzava != null && this.zzava.length > 0) {
            while (i < this.zzava.length) {
                zzzg com_google_android_gms_internal_measurement_zzzg2 = this.zzava[i];
                if (com_google_android_gms_internal_measurement_zzzg2 != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(3, com_google_android_gms_internal_measurement_zzzg2);
                }
                i++;
            }
        }
        if (this.zzavb != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(4, this.zzavb.booleanValue());
        }
        if (this.zzavc != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(5, this.zzavc.booleanValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int i = 0;
        int zzf = super.zzf();
        if (this.zzauy != null) {
            zzf += zzyy.zzh(1, this.zzauy.intValue());
        }
        if (this.zzauz != null && this.zzauz.length > 0) {
            int i2 = zzf;
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzauz) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    i2 += zzyy.zzb(2, com_google_android_gms_internal_measurement_zzzg);
                }
            }
            zzf = i2;
        }
        if (this.zzava != null && this.zzava.length > 0) {
            while (i < this.zzava.length) {
                zzzg com_google_android_gms_internal_measurement_zzzg2 = this.zzava[i];
                if (com_google_android_gms_internal_measurement_zzzg2 != null) {
                    zzf += zzyy.zzb(3, com_google_android_gms_internal_measurement_zzzg2);
                }
                i++;
            }
        }
        if (this.zzavb != null) {
            this.zzavb.booleanValue();
            zzf += zzyy.zzbb(4) + 1;
        }
        if (this.zzavc == null) {
            return zzf;
        }
        this.zzavc.booleanValue();
        return zzf + (zzyy.zzbb(5) + 1);
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
                    this.zzauy = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 18:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 18);
                    zzug = this.zzauz == null ? 0 : this.zzauz.length;
                    obj = new zzfy[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzauz, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzfy();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzfy();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzauz = obj;
                    continue;
                case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 26);
                    zzug = this.zzava == null ? 0 : this.zzava.length;
                    obj = new zzfv[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzava, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzfv();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzfv();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzava = obj;
                    continue;
                case 32:
                    this.zzavb = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
                    continue;
                case 40:
                    this.zzavc = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
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
