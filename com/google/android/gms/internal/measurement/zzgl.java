package com.google.android.gms.internal.measurement;

import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;

public final class zzgl extends zzza<zzgl> {
    private static volatile zzgl[] zzayk;
    public String name;
    public String zzamp;
    private Float zzaug;
    public Double zzauh;
    public Long zzawx;
    public Long zzayl;

    public static zzgl[] zzmu() {
        if (zzayk == null) {
            synchronized (zzze.zzcfl) {
                if (zzayk == null) {
                    zzayk = new zzgl[0];
                }
            }
        }
        return zzayk;
    }

    public zzgl() {
        this.zzayl = null;
        this.name = null;
        this.zzamp = null;
        this.zzawx = null;
        this.zzaug = null;
        this.zzauh = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgl)) {
            return false;
        }
        zzgl com_google_android_gms_internal_measurement_zzgl = (zzgl) obj;
        if (this.zzayl == null) {
            if (com_google_android_gms_internal_measurement_zzgl.zzayl != null) {
                return false;
            }
        } else if (!this.zzayl.equals(com_google_android_gms_internal_measurement_zzgl.zzayl)) {
            return false;
        }
        if (this.name == null) {
            if (com_google_android_gms_internal_measurement_zzgl.name != null) {
                return false;
            }
        } else if (!this.name.equals(com_google_android_gms_internal_measurement_zzgl.name)) {
            return false;
        }
        if (this.zzamp == null) {
            if (com_google_android_gms_internal_measurement_zzgl.zzamp != null) {
                return false;
            }
        } else if (!this.zzamp.equals(com_google_android_gms_internal_measurement_zzgl.zzamp)) {
            return false;
        }
        if (this.zzawx == null) {
            if (com_google_android_gms_internal_measurement_zzgl.zzawx != null) {
                return false;
            }
        } else if (!this.zzawx.equals(com_google_android_gms_internal_measurement_zzgl.zzawx)) {
            return false;
        }
        if (this.zzaug == null) {
            if (com_google_android_gms_internal_measurement_zzgl.zzaug != null) {
                return false;
            }
        } else if (!this.zzaug.equals(com_google_android_gms_internal_measurement_zzgl.zzaug)) {
            return false;
        }
        if (this.zzauh == null) {
            if (com_google_android_gms_internal_measurement_zzgl.zzauh != null) {
                return false;
            }
        } else if (!this.zzauh.equals(com_google_android_gms_internal_measurement_zzgl.zzauh)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgl.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgl.zzcfc == null || com_google_android_gms_internal_measurement_zzgl.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzauh == null ? 0 : this.zzauh.hashCode()) + (((this.zzaug == null ? 0 : this.zzaug.hashCode()) + (((this.zzawx == null ? 0 : this.zzawx.hashCode()) + (((this.zzamp == null ? 0 : this.zzamp.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + (((this.zzayl == null ? 0 : this.zzayl.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzayl != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(1, this.zzayl.longValue());
        }
        if (this.name != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(2, this.name);
        }
        if (this.zzamp != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(3, this.zzamp);
        }
        if (this.zzawx != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(4, this.zzawx.longValue());
        }
        if (this.zzaug != null) {
            com_google_android_gms_internal_measurement_zzyy.zza(5, this.zzaug.floatValue());
        }
        if (this.zzauh != null) {
            com_google_android_gms_internal_measurement_zzyy.zza(6, this.zzauh.doubleValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzayl != null) {
            zzf += zzyy.zzd(1, this.zzayl.longValue());
        }
        if (this.name != null) {
            zzf += zzyy.zzc(2, this.name);
        }
        if (this.zzamp != null) {
            zzf += zzyy.zzc(3, this.zzamp);
        }
        if (this.zzawx != null) {
            zzf += zzyy.zzd(4, this.zzawx.longValue());
        }
        if (this.zzaug != null) {
            this.zzaug.floatValue();
            zzf += zzyy.zzbb(5) + 4;
        }
        if (this.zzauh == null) {
            return zzf;
        }
        this.zzauh.doubleValue();
        return zzf + (zzyy.zzbb(6) + 8);
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            switch (zzug) {
                case 0:
                    break;
                case 8:
                    this.zzayl = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 18:
                    this.name = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                    this.zzamp = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 32:
                    this.zzawx = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 45:
                    this.zzaug = Float.valueOf(Float.intBitsToFloat(com_google_android_gms_internal_measurement_zzyx.zzva()));
                    continue;
                case 49:
                    this.zzauh = Double.valueOf(Double.longBitsToDouble(com_google_android_gms_internal_measurement_zzyx.zzvb()));
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
