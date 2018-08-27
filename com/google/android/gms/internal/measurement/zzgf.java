package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzgf extends zzza<zzgf> {
    private static volatile zzgf[] zzaws;
    public Integer count;
    public String name;
    public zzgg[] zzawt;
    public Long zzawu;
    public Long zzawv;

    public static zzgf[] zzmq() {
        if (zzaws == null) {
            synchronized (zzze.zzcfl) {
                if (zzaws == null) {
                    zzaws = new zzgf[0];
                }
            }
        }
        return zzaws;
    }

    public zzgf() {
        this.zzawt = zzgg.zzmr();
        this.name = null;
        this.zzawu = null;
        this.zzawv = null;
        this.count = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgf)) {
            return false;
        }
        zzgf com_google_android_gms_internal_measurement_zzgf = (zzgf) obj;
        if (!zzze.equals(this.zzawt, com_google_android_gms_internal_measurement_zzgf.zzawt)) {
            return false;
        }
        if (this.name == null) {
            if (com_google_android_gms_internal_measurement_zzgf.name != null) {
                return false;
            }
        } else if (!this.name.equals(com_google_android_gms_internal_measurement_zzgf.name)) {
            return false;
        }
        if (this.zzawu == null) {
            if (com_google_android_gms_internal_measurement_zzgf.zzawu != null) {
                return false;
            }
        } else if (!this.zzawu.equals(com_google_android_gms_internal_measurement_zzgf.zzawu)) {
            return false;
        }
        if (this.zzawv == null) {
            if (com_google_android_gms_internal_measurement_zzgf.zzawv != null) {
                return false;
            }
        } else if (!this.zzawv.equals(com_google_android_gms_internal_measurement_zzgf.zzawv)) {
            return false;
        }
        if (this.count == null) {
            if (com_google_android_gms_internal_measurement_zzgf.count != null) {
                return false;
            }
        } else if (!this.count.equals(com_google_android_gms_internal_measurement_zzgf.count)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgf.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgf.zzcfc == null || com_google_android_gms_internal_measurement_zzgf.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.count == null ? 0 : this.count.hashCode()) + (((this.zzawv == null ? 0 : this.zzawv.hashCode()) + (((this.zzawu == null ? 0 : this.zzawu.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + zzze.hashCode(this.zzawt)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzawt != null && this.zzawt.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzawt) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(1, com_google_android_gms_internal_measurement_zzzg);
                }
            }
        }
        if (this.name != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(2, this.name);
        }
        if (this.zzawu != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(3, this.zzawu.longValue());
        }
        if (this.zzawv != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(4, this.zzawv.longValue());
        }
        if (this.count != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(5, this.count.intValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzawt != null && this.zzawt.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzawt) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    zzf += zzyy.zzb(1, com_google_android_gms_internal_measurement_zzzg);
                }
            }
        }
        if (this.name != null) {
            zzf += zzyy.zzc(2, this.name);
        }
        if (this.zzawu != null) {
            zzf += zzyy.zzd(3, this.zzawu.longValue());
        }
        if (this.zzawv != null) {
            zzf += zzyy.zzd(4, this.zzawv.longValue());
        }
        if (this.count != null) {
            return zzf + zzyy.zzh(5, this.count.intValue());
        }
        return zzf;
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            switch (zzug) {
                case 0:
                    break;
                case 10:
                    int zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 10);
                    zzug = this.zzawt == null ? 0 : this.zzawt.length;
                    Object obj = new zzgg[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzawt, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzgg();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzgg();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzawt = obj;
                    continue;
                case 18:
                    this.name = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 24:
                    this.zzawu = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 32:
                    this.zzawv = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
                    continue;
                case 40:
                    this.count = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
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
