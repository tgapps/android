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
        if (!zzbf.equals(this.zzbu, com_google_android_gms_internal_config_zzaw.zzbu)) {
            return false;
        }
        if (this.zzci != null) {
            if (!this.zzci.isEmpty()) {
                return this.zzci.equals(com_google_android_gms_internal_config_zzaw.zzci);
            }
        }
        return com_google_android_gms_internal_config_zzaw.zzci == null || com_google_android_gms_internal_config_zzaw.zzci.isEmpty();
    }

    public final int hashCode() {
        int hashCode = 527 + getClass().getName().hashCode();
        zzas com_google_android_gms_internal_config_zzas = this.zzbq;
        int i = 0;
        hashCode = (hashCode * 31) + (com_google_android_gms_internal_config_zzas == null ? 0 : com_google_android_gms_internal_config_zzas.hashCode());
        com_google_android_gms_internal_config_zzas = this.zzbr;
        hashCode = (hashCode * 31) + (com_google_android_gms_internal_config_zzas == null ? 0 : com_google_android_gms_internal_config_zzas.hashCode());
        com_google_android_gms_internal_config_zzas = this.zzbs;
        hashCode = (hashCode * 31) + (com_google_android_gms_internal_config_zzas == null ? 0 : com_google_android_gms_internal_config_zzas.hashCode());
        zzau com_google_android_gms_internal_config_zzau = this.zzbt;
        hashCode = ((((hashCode * 31) + (com_google_android_gms_internal_config_zzau == null ? 0 : com_google_android_gms_internal_config_zzau.hashCode())) * 31) + zzbf.hashCode(this.zzbu)) * 31;
        if (this.zzci != null) {
            if (!this.zzci.isEmpty()) {
                i = this.zzci.hashCode();
            }
        }
        return hashCode + i;
    }

    public final /* synthetic */ zzbh zza(zzay com_google_android_gms_internal_config_zzay) throws IOException {
        while (true) {
            int zzy = com_google_android_gms_internal_config_zzay.zzy();
            if (zzy == 0) {
                return this;
            }
            zzbh com_google_android_gms_internal_config_zzbh;
            if (zzy == 10) {
                if (this.zzbq == null) {
                    this.zzbq = new zzas();
                }
                com_google_android_gms_internal_config_zzbh = this.zzbq;
            } else if (zzy == 18) {
                if (this.zzbr == null) {
                    this.zzbr = new zzas();
                }
                com_google_android_gms_internal_config_zzbh = this.zzbr;
            } else if (zzy == 26) {
                if (this.zzbs == null) {
                    this.zzbs = new zzas();
                }
                com_google_android_gms_internal_config_zzbh = this.zzbs;
            } else if (zzy == 34) {
                if (this.zzbt == null) {
                    this.zzbt = new zzau();
                }
                com_google_android_gms_internal_config_zzbh = this.zzbt;
            } else if (zzy == 42) {
                zzy = zzbk.zzb(com_google_android_gms_internal_config_zzay, 42);
                int length = this.zzbu == null ? 0 : this.zzbu.length;
                Object obj = new zzax[(zzy + length)];
                if (length != 0) {
                    System.arraycopy(this.zzbu, 0, obj, 0, length);
                }
                while (length < obj.length - 1) {
                    obj[length] = new zzax();
                    com_google_android_gms_internal_config_zzay.zza(obj[length]);
                    com_google_android_gms_internal_config_zzay.zzy();
                    length++;
                }
                obj[length] = new zzax();
                com_google_android_gms_internal_config_zzay.zza(obj[length]);
                this.zzbu = obj;
            } else if (!super.zza(com_google_android_gms_internal_config_zzay, zzy)) {
                return this;
            }
            com_google_android_gms_internal_config_zzay.zza(com_google_android_gms_internal_config_zzbh);
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
        if (this.zzbu != null && this.zzbu.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbu) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    zzu += zzaz.zzb(5, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        return zzu;
    }
}
