package com.google.android.gms.internal.measurement;

import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;

public final class zzgg extends zzza<zzgg> {
    private static volatile zzgg[] zzaww;
    public String name;
    public String zzamp;
    private Float zzaug;
    public Double zzauh;
    public Long zzawx;

    public static zzgg[] zzmr() {
        if (zzaww == null) {
            synchronized (zzze.zzcfl) {
                if (zzaww == null) {
                    zzaww = new zzgg[0];
                }
            }
        }
        return zzaww;
    }

    public zzgg() {
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
        if (!(obj instanceof zzgg)) {
            return false;
        }
        zzgg com_google_android_gms_internal_measurement_zzgg = (zzgg) obj;
        if (this.name == null) {
            if (com_google_android_gms_internal_measurement_zzgg.name != null) {
                return false;
            }
        } else if (!this.name.equals(com_google_android_gms_internal_measurement_zzgg.name)) {
            return false;
        }
        if (this.zzamp == null) {
            if (com_google_android_gms_internal_measurement_zzgg.zzamp != null) {
                return false;
            }
        } else if (!this.zzamp.equals(com_google_android_gms_internal_measurement_zzgg.zzamp)) {
            return false;
        }
        if (this.zzawx == null) {
            if (com_google_android_gms_internal_measurement_zzgg.zzawx != null) {
                return false;
            }
        } else if (!this.zzawx.equals(com_google_android_gms_internal_measurement_zzgg.zzawx)) {
            return false;
        }
        if (this.zzaug == null) {
            if (com_google_android_gms_internal_measurement_zzgg.zzaug != null) {
                return false;
            }
        } else if (!this.zzaug.equals(com_google_android_gms_internal_measurement_zzgg.zzaug)) {
            return false;
        }
        if (this.zzauh == null) {
            if (com_google_android_gms_internal_measurement_zzgg.zzauh != null) {
                return false;
            }
        } else if (!this.zzauh.equals(com_google_android_gms_internal_measurement_zzgg.zzauh)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgg.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgg.zzcfc == null || com_google_android_gms_internal_measurement_zzgg.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzauh == null ? 0 : this.zzauh.hashCode()) + (((this.zzaug == null ? 0 : this.zzaug.hashCode()) + (((this.zzawx == null ? 0 : this.zzawx.hashCode()) + (((this.zzamp == null ? 0 : this.zzamp.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.name != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(1, this.name);
        }
        if (this.zzamp != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(2, this.zzamp);
        }
        if (this.zzawx != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(3, this.zzawx.longValue());
        }
        if (this.zzaug != null) {
            com_google_android_gms_internal_measurement_zzyy.zza(4, this.zzaug.floatValue());
        }
        if (this.zzauh != null) {
            com_google_android_gms_internal_measurement_zzyy.zza(5, this.zzauh.doubleValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.name != null) {
            zzf += zzyy.zzc(1, this.name);
        }
        if (this.zzamp != null) {
            zzf += zzyy.zzc(2, this.zzamp);
        }
        if (this.zzawx != null) {
            zzf += zzyy.zzd(3, this.zzawx.longValue());
        }
        if (this.zzaug != null) {
            this.zzaug.floatValue();
            zzf += zzyy.zzbb(4) + 4;
        }
        if (this.zzauh == null) {
            return zzf;
        }
        this.zzauh.doubleValue();
        return zzf + (zzyy.zzbb(5) + 8);
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            switch (zzug) {
                case 0:
                    break;
                case 10:
                    this.name = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 18:
                    this.zzamp = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 24:
                    this.zzawx = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case NalUnitTypes.NAL_TYPE_EOB_NUT /*37*/:
                    this.zzaug = Float.valueOf(Float.intBitsToFloat(com_google_android_gms_internal_measurement_zzyx.zzva()));
                    continue;
                case 41:
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
