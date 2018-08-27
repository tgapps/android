package com.google.android.gms.internal.measurement;

import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;

public final class zzgd extends zzza<zzgd> {
    private static volatile zzgd[] zzawl;
    public Integer zzauy;
    public zzgj zzawm;
    public zzgj zzawn;
    public Boolean zzawo;

    public static zzgd[] zzmo() {
        if (zzawl == null) {
            synchronized (zzze.zzcfl) {
                if (zzawl == null) {
                    zzawl = new zzgd[0];
                }
            }
        }
        return zzawl;
    }

    public zzgd() {
        this.zzauy = null;
        this.zzawm = null;
        this.zzawn = null;
        this.zzawo = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgd)) {
            return false;
        }
        zzgd com_google_android_gms_internal_measurement_zzgd = (zzgd) obj;
        if (this.zzauy == null) {
            if (com_google_android_gms_internal_measurement_zzgd.zzauy != null) {
                return false;
            }
        } else if (!this.zzauy.equals(com_google_android_gms_internal_measurement_zzgd.zzauy)) {
            return false;
        }
        if (this.zzawm == null) {
            if (com_google_android_gms_internal_measurement_zzgd.zzawm != null) {
                return false;
            }
        } else if (!this.zzawm.equals(com_google_android_gms_internal_measurement_zzgd.zzawm)) {
            return false;
        }
        if (this.zzawn == null) {
            if (com_google_android_gms_internal_measurement_zzgd.zzawn != null) {
                return false;
            }
        } else if (!this.zzawn.equals(com_google_android_gms_internal_measurement_zzgd.zzawn)) {
            return false;
        }
        if (this.zzawo == null) {
            if (com_google_android_gms_internal_measurement_zzgd.zzawo != null) {
                return false;
            }
        } else if (!this.zzawo.equals(com_google_android_gms_internal_measurement_zzgd.zzawo)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgd.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgd.zzcfc == null || com_google_android_gms_internal_measurement_zzgd.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (this.zzauy == null ? 0 : this.zzauy.hashCode()) + ((getClass().getName().hashCode() + 527) * 31);
        zzgj com_google_android_gms_internal_measurement_zzgj = this.zzawm;
        hashCode = (com_google_android_gms_internal_measurement_zzgj == null ? 0 : com_google_android_gms_internal_measurement_zzgj.hashCode()) + (hashCode * 31);
        com_google_android_gms_internal_measurement_zzgj = this.zzawn;
        hashCode = ((this.zzawo == null ? 0 : this.zzawo.hashCode()) + (((com_google_android_gms_internal_measurement_zzgj == null ? 0 : com_google_android_gms_internal_measurement_zzgj.hashCode()) + (hashCode * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzauy != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(1, this.zzauy.intValue());
        }
        if (this.zzawm != null) {
            com_google_android_gms_internal_measurement_zzyy.zza(2, this.zzawm);
        }
        if (this.zzawn != null) {
            com_google_android_gms_internal_measurement_zzyy.zza(3, this.zzawn);
        }
        if (this.zzawo != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(4, this.zzawo.booleanValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzauy != null) {
            zzf += zzyy.zzh(1, this.zzauy.intValue());
        }
        if (this.zzawm != null) {
            zzf += zzyy.zzb(2, this.zzawm);
        }
        if (this.zzawn != null) {
            zzf += zzyy.zzb(3, this.zzawn);
        }
        if (this.zzawo == null) {
            return zzf;
        }
        this.zzawo.booleanValue();
        return zzf + (zzyy.zzbb(4) + 1);
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            switch (zzug) {
                case 0:
                    break;
                case 8:
                    this.zzauy = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 18:
                    if (this.zzawm == null) {
                        this.zzawm = new zzgj();
                    }
                    com_google_android_gms_internal_measurement_zzyx.zza(this.zzawm);
                    continue;
                case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                    if (this.zzawn == null) {
                        this.zzawn = new zzgj();
                    }
                    com_google_android_gms_internal_measurement_zzyx.zza(this.zzawn);
                    continue;
                case 32:
                    this.zzawo = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
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
