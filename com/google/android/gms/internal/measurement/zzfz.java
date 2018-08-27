package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzfz extends zzza<zzfz> {
    public Integer zzavw;
    public String zzavx;
    public Boolean zzavy;
    public String[] zzavz;

    public zzfz() {
        this.zzavw = null;
        this.zzavx = null;
        this.zzavy = null;
        this.zzavz = zzzj.zzcfv;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfz)) {
            return false;
        }
        zzfz com_google_android_gms_internal_measurement_zzfz = (zzfz) obj;
        if (this.zzavw == null) {
            if (com_google_android_gms_internal_measurement_zzfz.zzavw != null) {
                return false;
            }
        } else if (!this.zzavw.equals(com_google_android_gms_internal_measurement_zzfz.zzavw)) {
            return false;
        }
        if (this.zzavx == null) {
            if (com_google_android_gms_internal_measurement_zzfz.zzavx != null) {
                return false;
            }
        } else if (!this.zzavx.equals(com_google_android_gms_internal_measurement_zzfz.zzavx)) {
            return false;
        }
        if (this.zzavy == null) {
            if (com_google_android_gms_internal_measurement_zzfz.zzavy != null) {
                return false;
            }
        } else if (!this.zzavy.equals(com_google_android_gms_internal_measurement_zzfz.zzavy)) {
            return false;
        }
        if (!zzze.equals(this.zzavz, com_google_android_gms_internal_measurement_zzfz.zzavz)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzfz.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzfz.zzcfc == null || com_google_android_gms_internal_measurement_zzfz.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((this.zzavy == null ? 0 : this.zzavy.hashCode()) + (((this.zzavx == null ? 0 : this.zzavx.hashCode()) + (((this.zzavw == null ? 0 : this.zzavw.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + zzze.hashCode(this.zzavz)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzavw != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(1, this.zzavw.intValue());
        }
        if (this.zzavx != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(2, this.zzavx);
        }
        if (this.zzavy != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(3, this.zzavy.booleanValue());
        }
        if (this.zzavz != null && this.zzavz.length > 0) {
            for (String str : this.zzavz) {
                if (str != null) {
                    com_google_android_gms_internal_measurement_zzyy.zzb(4, str);
                }
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzavw != null) {
            zzf += zzyy.zzh(1, this.zzavw.intValue());
        }
        if (this.zzavx != null) {
            zzf += zzyy.zzc(2, this.zzavx);
        }
        if (this.zzavy != null) {
            this.zzavy.booleanValue();
            zzf += zzyy.zzbb(3) + 1;
        }
        if (this.zzavz == null || this.zzavz.length <= 0) {
            return zzf;
        }
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < this.zzavz.length) {
            int zzfx;
            String str = this.zzavz[i];
            if (str != null) {
                i3++;
                zzfx = zzyy.zzfx(str) + i2;
            } else {
                zzfx = i2;
            }
            i++;
            i2 = zzfx;
        }
        return (zzf + i2) + (i3 * 1);
    }

    private final zzfz zzd(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        int position;
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            switch (zzug) {
                case 0:
                    break;
                case 8:
                    position = com_google_android_gms_internal_measurement_zzyx.getPosition();
                    try {
                        int zzuy = com_google_android_gms_internal_measurement_zzyx.zzuy();
                        if (zzuy < 0 || zzuy > 6) {
                            throw new IllegalArgumentException(zzuy + " is not a valid enum MatchType");
                        }
                        this.zzavw = Integer.valueOf(zzuy);
                        continue;
                    } catch (IllegalArgumentException e) {
                        com_google_android_gms_internal_measurement_zzyx.zzby(position);
                        zza(com_google_android_gms_internal_measurement_zzyx, zzug);
                        break;
                    }
                case 18:
                    this.zzavx = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 24:
                    this.zzavy = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
                    continue;
                case 34:
                    position = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 34);
                    zzug = this.zzavz == null ? 0 : this.zzavz.length;
                    Object obj = new String[(position + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzavz, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = com_google_android_gms_internal_measurement_zzyx.readString();
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = com_google_android_gms_internal_measurement_zzyx.readString();
                    this.zzavz = obj;
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
        return zzd(com_google_android_gms_internal_measurement_zzyx);
    }
}
