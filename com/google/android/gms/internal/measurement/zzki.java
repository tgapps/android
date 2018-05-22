package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.messenger.exoplayer2.RendererCapabilities;

public final class zzki extends zzaby<zzki> {
    public Integer zzash;
    public String zzasi;
    public Boolean zzasj;
    public String[] zzask;

    public zzki() {
        this.zzash = null;
        this.zzasi = null;
        this.zzasj = null;
        this.zzask = zzach.zzbxq;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    private final zzki zze(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        while (true) {
            int zzuw = com_google_android_gms_internal_measurement_zzabv.zzuw();
            int position;
            switch (zzuw) {
                case 0:
                    break;
                case 8:
                    position = com_google_android_gms_internal_measurement_zzabv.getPosition();
                    try {
                        int zzuy = com_google_android_gms_internal_measurement_zzabv.zzuy();
                        if (zzuy < 0 || zzuy > 6) {
                            throw new IllegalArgumentException(zzuy + " is not a valid enum MatchType");
                        }
                        this.zzash = Integer.valueOf(zzuy);
                        continue;
                    } catch (IllegalArgumentException e) {
                        com_google_android_gms_internal_measurement_zzabv.zzam(position);
                        zza(com_google_android_gms_internal_measurement_zzabv, zzuw);
                        break;
                    }
                case 18:
                    this.zzasi = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                    this.zzasj = Boolean.valueOf(com_google_android_gms_internal_measurement_zzabv.zzux());
                    continue;
                case 34:
                    position = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 34);
                    zzuw = this.zzask == null ? 0 : this.zzask.length;
                    Object obj = new String[(position + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzask, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = com_google_android_gms_internal_measurement_zzabv.readString();
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = com_google_android_gms_internal_measurement_zzabv.readString();
                    this.zzask = obj;
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

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzki)) {
            return false;
        }
        zzki com_google_android_gms_internal_measurement_zzki = (zzki) obj;
        if (this.zzash == null) {
            if (com_google_android_gms_internal_measurement_zzki.zzash != null) {
                return false;
            }
        } else if (!this.zzash.equals(com_google_android_gms_internal_measurement_zzki.zzash)) {
            return false;
        }
        if (this.zzasi == null) {
            if (com_google_android_gms_internal_measurement_zzki.zzasi != null) {
                return false;
            }
        } else if (!this.zzasi.equals(com_google_android_gms_internal_measurement_zzki.zzasi)) {
            return false;
        }
        if (this.zzasj == null) {
            if (com_google_android_gms_internal_measurement_zzki.zzasj != null) {
                return false;
            }
        } else if (!this.zzasj.equals(com_google_android_gms_internal_measurement_zzki.zzasj)) {
            return false;
        }
        return !zzacc.equals(this.zzask, com_google_android_gms_internal_measurement_zzki.zzask) ? false : (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzki.zzbww == null || com_google_android_gms_internal_measurement_zzki.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzki.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((this.zzasj == null ? 0 : this.zzasj.hashCode()) + (((this.zzasi == null ? 0 : this.zzasi.hashCode()) + (((this.zzash == null ? 0 : this.zzash.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + zzacc.hashCode(this.zzask)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzash != null) {
            zza += zzabw.zzf(1, this.zzash.intValue());
        }
        if (this.zzasi != null) {
            zza += zzabw.zzc(2, this.zzasi);
        }
        if (this.zzasj != null) {
            this.zzasj.booleanValue();
            zza += zzabw.zzaq(3) + 1;
        }
        if (this.zzask == null || this.zzask.length <= 0) {
            return zza;
        }
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < this.zzask.length) {
            int zzfm;
            String str = this.zzask[i];
            if (str != null) {
                i3++;
                zzfm = zzabw.zzfm(str) + i2;
            } else {
                zzfm = i2;
            }
            i++;
            i2 = zzfm;
        }
        return (zza + i2) + (i3 * 1);
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        if (this.zzash != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(1, this.zzash.intValue());
        }
        if (this.zzasi != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(2, this.zzasi);
        }
        if (this.zzasj != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(3, this.zzasj.booleanValue());
        }
        if (this.zzask != null && this.zzask.length > 0) {
            for (String str : this.zzask) {
                if (str != null) {
                    com_google_android_gms_internal_measurement_zzabw.zzb(4, str);
                }
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        return zze(com_google_android_gms_internal_measurement_zzabv);
    }
}
