package com.google.android.gms.internal.config;

import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;

public final class zzaw extends zzbb<zzaw> {
    public zzas zzbp;
    public zzas zzbq;
    public zzas zzbr;
    public zzau zzbs;
    public zzax[] zzbt;

    public zzaw() {
        this.zzbp = null;
        this.zzbq = null;
        this.zzbr = null;
        this.zzbs = null;
        this.zzbt = zzax.zzw();
        this.zzch = null;
        this.zzcq = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzaw)) {
            return false;
        }
        zzaw com_google_android_gms_internal_config_zzaw = (zzaw) obj;
        if (this.zzbp == null) {
            if (com_google_android_gms_internal_config_zzaw.zzbp != null) {
                return false;
            }
        } else if (!this.zzbp.equals(com_google_android_gms_internal_config_zzaw.zzbp)) {
            return false;
        }
        if (this.zzbq == null) {
            if (com_google_android_gms_internal_config_zzaw.zzbq != null) {
                return false;
            }
        } else if (!this.zzbq.equals(com_google_android_gms_internal_config_zzaw.zzbq)) {
            return false;
        }
        if (this.zzbr == null) {
            if (com_google_android_gms_internal_config_zzaw.zzbr != null) {
                return false;
            }
        } else if (!this.zzbr.equals(com_google_android_gms_internal_config_zzaw.zzbr)) {
            return false;
        }
        if (this.zzbs == null) {
            if (com_google_android_gms_internal_config_zzaw.zzbs != null) {
                return false;
            }
        } else if (!this.zzbs.equals(com_google_android_gms_internal_config_zzaw.zzbs)) {
            return false;
        }
        return !zzbf.equals(this.zzbt, com_google_android_gms_internal_config_zzaw.zzbt) ? false : (this.zzch == null || this.zzch.isEmpty()) ? com_google_android_gms_internal_config_zzaw.zzch == null || com_google_android_gms_internal_config_zzaw.zzch.isEmpty() : this.zzch.equals(com_google_android_gms_internal_config_zzaw.zzch);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = getClass().getName().hashCode() + 527;
        zzas com_google_android_gms_internal_config_zzas = this.zzbp;
        hashCode = (com_google_android_gms_internal_config_zzas == null ? 0 : com_google_android_gms_internal_config_zzas.hashCode()) + (hashCode * 31);
        com_google_android_gms_internal_config_zzas = this.zzbq;
        hashCode = (com_google_android_gms_internal_config_zzas == null ? 0 : com_google_android_gms_internal_config_zzas.hashCode()) + (hashCode * 31);
        com_google_android_gms_internal_config_zzas = this.zzbr;
        hashCode = (com_google_android_gms_internal_config_zzas == null ? 0 : com_google_android_gms_internal_config_zzas.hashCode()) + (hashCode * 31);
        zzau com_google_android_gms_internal_config_zzau = this.zzbs;
        hashCode = ((((com_google_android_gms_internal_config_zzau == null ? 0 : com_google_android_gms_internal_config_zzau.hashCode()) + (hashCode * 31)) * 31) + zzbf.hashCode(this.zzbt)) * 31;
        if (!(this.zzch == null || this.zzch.isEmpty())) {
            i = this.zzch.hashCode();
        }
        return hashCode + i;
    }

    public final /* synthetic */ zzbh zza(zzay com_google_android_gms_internal_config_zzay) throws IOException {
        while (true) {
            int zzx = com_google_android_gms_internal_config_zzay.zzx();
            switch (zzx) {
                case 0:
                    break;
                case 10:
                    if (this.zzbp == null) {
                        this.zzbp = new zzas();
                    }
                    com_google_android_gms_internal_config_zzay.zza(this.zzbp);
                    continue;
                case 18:
                    if (this.zzbq == null) {
                        this.zzbq = new zzas();
                    }
                    com_google_android_gms_internal_config_zzay.zza(this.zzbq);
                    continue;
                case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                    if (this.zzbr == null) {
                        this.zzbr = new zzas();
                    }
                    com_google_android_gms_internal_config_zzay.zza(this.zzbr);
                    continue;
                case 34:
                    if (this.zzbs == null) {
                        this.zzbs = new zzau();
                    }
                    com_google_android_gms_internal_config_zzay.zza(this.zzbs);
                    continue;
                case 42:
                    int zzb = zzbk.zzb(com_google_android_gms_internal_config_zzay, 42);
                    zzx = this.zzbt == null ? 0 : this.zzbt.length;
                    Object obj = new zzax[(zzb + zzx)];
                    if (zzx != 0) {
                        System.arraycopy(this.zzbt, 0, obj, 0, zzx);
                    }
                    while (zzx < obj.length - 1) {
                        obj[zzx] = new zzax();
                        com_google_android_gms_internal_config_zzay.zza(obj[zzx]);
                        com_google_android_gms_internal_config_zzay.zzx();
                        zzx++;
                    }
                    obj[zzx] = new zzax();
                    com_google_android_gms_internal_config_zzay.zza(obj[zzx]);
                    this.zzbt = obj;
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_config_zzay, zzx)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }

    public final void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
        if (this.zzbp != null) {
            com_google_android_gms_internal_config_zzaz.zza(1, this.zzbp);
        }
        if (this.zzbq != null) {
            com_google_android_gms_internal_config_zzaz.zza(2, this.zzbq);
        }
        if (this.zzbr != null) {
            com_google_android_gms_internal_config_zzaz.zza(3, this.zzbr);
        }
        if (this.zzbs != null) {
            com_google_android_gms_internal_config_zzaz.zza(4, this.zzbs);
        }
        if (this.zzbt != null && this.zzbt.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbt) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    com_google_android_gms_internal_config_zzaz.zza(5, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzt() {
        int zzt = super.zzt();
        if (this.zzbp != null) {
            zzt += zzaz.zzb(1, this.zzbp);
        }
        if (this.zzbq != null) {
            zzt += zzaz.zzb(2, this.zzbq);
        }
        if (this.zzbr != null) {
            zzt += zzaz.zzb(3, this.zzbr);
        }
        if (this.zzbs != null) {
            zzt += zzaz.zzb(4, this.zzbs);
        }
        if (this.zzbt == null || this.zzbt.length <= 0) {
            return zzt;
        }
        int i = zzt;
        for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbt) {
            if (com_google_android_gms_internal_config_zzbh != null) {
                i += zzaz.zzb(5, com_google_android_gms_internal_config_zzbh);
            }
        }
        return i;
    }
}
