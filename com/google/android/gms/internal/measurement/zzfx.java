package com.google.android.gms.internal.measurement;

import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;

public final class zzfx extends zzza<zzfx> {
    public Integer zzavo;
    public Boolean zzavp;
    public String zzavq;
    public String zzavr;
    public String zzavs;

    public zzfx() {
        this.zzavo = null;
        this.zzavp = null;
        this.zzavq = null;
        this.zzavr = null;
        this.zzavs = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfx)) {
            return false;
        }
        zzfx com_google_android_gms_internal_measurement_zzfx = (zzfx) obj;
        if (this.zzavo == null) {
            if (com_google_android_gms_internal_measurement_zzfx.zzavo != null) {
                return false;
            }
        } else if (!this.zzavo.equals(com_google_android_gms_internal_measurement_zzfx.zzavo)) {
            return false;
        }
        if (this.zzavp == null) {
            if (com_google_android_gms_internal_measurement_zzfx.zzavp != null) {
                return false;
            }
        } else if (!this.zzavp.equals(com_google_android_gms_internal_measurement_zzfx.zzavp)) {
            return false;
        }
        if (this.zzavq == null) {
            if (com_google_android_gms_internal_measurement_zzfx.zzavq != null) {
                return false;
            }
        } else if (!this.zzavq.equals(com_google_android_gms_internal_measurement_zzfx.zzavq)) {
            return false;
        }
        if (this.zzavr == null) {
            if (com_google_android_gms_internal_measurement_zzfx.zzavr != null) {
                return false;
            }
        } else if (!this.zzavr.equals(com_google_android_gms_internal_measurement_zzfx.zzavr)) {
            return false;
        }
        if (this.zzavs == null) {
            if (com_google_android_gms_internal_measurement_zzfx.zzavs != null) {
                return false;
            }
        } else if (!this.zzavs.equals(com_google_android_gms_internal_measurement_zzfx.zzavs)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzfx.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzfx.zzcfc == null || com_google_android_gms_internal_measurement_zzfx.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzavs == null ? 0 : this.zzavs.hashCode()) + (((this.zzavr == null ? 0 : this.zzavr.hashCode()) + (((this.zzavq == null ? 0 : this.zzavq.hashCode()) + (((this.zzavp == null ? 0 : this.zzavp.hashCode()) + (((this.zzavo == null ? 0 : this.zzavo.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzavo != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(1, this.zzavo.intValue());
        }
        if (this.zzavp != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(2, this.zzavp.booleanValue());
        }
        if (this.zzavq != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(3, this.zzavq);
        }
        if (this.zzavr != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(4, this.zzavr);
        }
        if (this.zzavs != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(5, this.zzavs);
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzavo != null) {
            zzf += zzyy.zzh(1, this.zzavo.intValue());
        }
        if (this.zzavp != null) {
            this.zzavp.booleanValue();
            zzf += zzyy.zzbb(2) + 1;
        }
        if (this.zzavq != null) {
            zzf += zzyy.zzc(3, this.zzavq);
        }
        if (this.zzavr != null) {
            zzf += zzyy.zzc(4, this.zzavr);
        }
        if (this.zzavs != null) {
            return zzf + zzyy.zzc(5, this.zzavs);
        }
        return zzf;
    }

    private final zzfx zzc(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            switch (zzug) {
                case 0:
                    break;
                case 8:
                    int position = com_google_android_gms_internal_measurement_zzyx.getPosition();
                    try {
                        int zzuy = com_google_android_gms_internal_measurement_zzyx.zzuy();
                        if (zzuy < 0 || zzuy > 4) {
                            throw new IllegalArgumentException(zzuy + " is not a valid enum ComparisonType");
                        }
                        this.zzavo = Integer.valueOf(zzuy);
                        continue;
                    } catch (IllegalArgumentException e) {
                        com_google_android_gms_internal_measurement_zzyx.zzby(position);
                        zza(com_google_android_gms_internal_measurement_zzyx, zzug);
                        break;
                    }
                case 16:
                    this.zzavp = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
                    continue;
                case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                    this.zzavq = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 34:
                    this.zzavr = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 42:
                    this.zzavs = com_google_android_gms_internal_measurement_zzyx.readString();
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

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        return zzc(com_google_android_gms_internal_measurement_zzyx);
    }
}
