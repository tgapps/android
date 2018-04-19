package com.google.android.gms.internal.config;

import java.io.IOException;

public final class zzaw extends zzbb<zzaw> {
    public zzas zzbq;
    public zzas zzbr;
    public zzas zzbs;
    public zzau zzbt;
    public zzax[] zzbu;

    public zzaw() {
        this.zzbq = null;
        this.zzbr = null;
        this.zzbs = null;
        this.zzbt = null;
        this.zzbu = zzax.zzx();
        this.zzci = null;
        this.zzcr = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzaw)) {
            return false;
        }
        zzaw com_google_android_gms_internal_config_zzaw = (zzaw) obj;
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
        if (this.zzbt == null) {
            if (com_google_android_gms_internal_config_zzaw.zzbt != null) {
                return false;
            }
        } else if (!this.zzbt.equals(com_google_android_gms_internal_config_zzaw.zzbt)) {
            return false;
        }
        return !zzbf.equals(this.zzbu, com_google_android_gms_internal_config_zzaw.zzbu) ? false : (this.zzci == null || this.zzci.isEmpty()) ? com_google_android_gms_internal_config_zzaw.zzci == null || com_google_android_gms_internal_config_zzaw.zzci.isEmpty() : this.zzci.equals(com_google_android_gms_internal_config_zzaw.zzci);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = getClass().getName().hashCode() + 527;
        zzas com_google_android_gms_internal_config_zzas = this.zzbq;
        hashCode = (com_google_android_gms_internal_config_zzas == null ? 0 : com_google_android_gms_internal_config_zzas.hashCode()) + (hashCode * 31);
        com_google_android_gms_internal_config_zzas = this.zzbr;
        hashCode = (com_google_android_gms_internal_config_zzas == null ? 0 : com_google_android_gms_internal_config_zzas.hashCode()) + (hashCode * 31);
        com_google_android_gms_internal_config_zzas = this.zzbs;
        hashCode = (com_google_android_gms_internal_config_zzas == null ? 0 : com_google_android_gms_internal_config_zzas.hashCode()) + (hashCode * 31);
        zzau com_google_android_gms_internal_config_zzau = this.zzbt;
        hashCode = ((((com_google_android_gms_internal_config_zzau == null ? 0 : com_google_android_gms_internal_config_zzau.hashCode()) + (hashCode * 31)) * 31) + zzbf.hashCode(this.zzbu)) * 31;
        if (!(this.zzci == null || this.zzci.isEmpty())) {
            i = this.zzci.hashCode();
        }
        return hashCode + i;
    }

    public final /* synthetic */ zzbh zza(zzay com_google_android_gms_internal_config_zzay) throws IOException {
        while (true) {
            int zzy = com_google_android_gms_internal_config_zzay.zzy();
            switch (zzy) {
                case 0:
                    break;
                case 10:
                    if (this.zzbq == null) {
                        this.zzbq = new zzas();
                    }
                    com_google_android_gms_internal_config_zzay.zza(this.zzbq);
                    continue;
                case 18:
                    if (this.zzbr == null) {
                        this.zzbr = new zzas();
                    }
                    com_google_android_gms_internal_config_zzay.zza(this.zzbr);
                    continue;
                case 26:
                    if (this.zzbs == null) {
                        this.zzbs = new zzas();
                    }
                    com_google_android_gms_internal_config_zzay.zza(this.zzbs);
                    continue;
                case 34:
                    if (this.zzbt == null) {
                        this.zzbt = new zzau();
                    }
                    com_google_android_gms_internal_config_zzay.zza(this.zzbt);
                    continue;
                case 42:
                    int zzb = zzbk.zzb(com_google_android_gms_internal_config_zzay, 42);
                    zzy = this.zzbu == null ? 0 : this.zzbu.length;
                    Object obj = new zzax[(zzb + zzy)];
                    if (zzy != 0) {
                        System.arraycopy(this.zzbu, 0, obj, 0, zzy);
                    }
                    while (zzy < obj.length - 1) {
                        obj[zzy] = new zzax();
                        com_google_android_gms_internal_config_zzay.zza(obj[zzy]);
                        com_google_android_gms_internal_config_zzay.zzy();
                        zzy++;
                    }
                    obj[zzy] = new zzax();
                    com_google_android_gms_internal_config_zzay.zza(obj[zzy]);
                    this.zzbu = obj;
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_config_zzay, zzy)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }

    public final void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
        if (this.zzbq != null) {
            com_google_android_gms_internal_config_zzaz.zza(1, this.zzbq);
        }
        if (this.zzbr != null) {
            com_google_android_gms_internal_config_zzaz.zza(2, this.zzbr);
        }
        if (this.zzbs != null) {
            com_google_android_gms_internal_config_zzaz.zza(3, this.zzbs);
        }
        if (this.zzbt != null) {
            com_google_android_gms_internal_config_zzaz.zza(4, this.zzbt);
        }
        if (this.zzbu != null && this.zzbu.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbu) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    com_google_android_gms_internal_config_zzaz.zza(5, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzu() {
        int zzu = super.zzu();
        if (this.zzbq != null) {
            zzu += zzaz.zzb(1, this.zzbq);
        }
        if (this.zzbr != null) {
            zzu += zzaz.zzb(2, this.zzbr);
        }
        if (this.zzbs != null) {
            zzu += zzaz.zzb(3, this.zzbs);
        }
        if (this.zzbt != null) {
            zzu += zzaz.zzb(4, this.zzbt);
        }
        if (this.zzbu == null || this.zzbu.length <= 0) {
            return zzu;
        }
        int i = zzu;
        for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbu) {
            if (com_google_android_gms_internal_config_zzbh != null) {
                i += zzaz.zzb(5, com_google_android_gms_internal_config_zzbh);
            }
        }
        return i;
    }
}
