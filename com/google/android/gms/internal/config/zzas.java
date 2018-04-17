package com.google.android.gms.internal.config;

import java.io.IOException;

public final class zzas extends zzbb<zzas> {
    public long timestamp;
    public zzav[] zzbg;
    public byte[][] zzbh;

    public zzas() {
        this.zzbg = zzav.zzw();
        this.timestamp = 0;
        this.zzbh = zzbk.zzdd;
        this.zzci = null;
        this.zzcr = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzas)) {
            return false;
        }
        zzas com_google_android_gms_internal_config_zzas = (zzas) obj;
        if (!zzbf.equals(this.zzbg, com_google_android_gms_internal_config_zzas.zzbg) || this.timestamp != com_google_android_gms_internal_config_zzas.timestamp || !zzbf.zza(this.zzbh, com_google_android_gms_internal_config_zzas.zzbh)) {
            return false;
        }
        if (this.zzci != null) {
            if (!this.zzci.isEmpty()) {
                return this.zzci.equals(com_google_android_gms_internal_config_zzas.zzci);
            }
        }
        return com_google_android_gms_internal_config_zzas.zzci == null || com_google_android_gms_internal_config_zzas.zzci.isEmpty();
    }

    public final int hashCode() {
        int hashCode;
        int hashCode2 = (((((((527 + getClass().getName().hashCode()) * 31) + zzbf.hashCode(this.zzbg)) * 31) + ((int) (this.timestamp ^ (this.timestamp >>> 32)))) * 31) + zzbf.zza(this.zzbh)) * 31;
        if (this.zzci != null) {
            if (!this.zzci.isEmpty()) {
                hashCode = this.zzci.hashCode();
                return hashCode2 + hashCode;
            }
        }
        hashCode = 0;
        return hashCode2 + hashCode;
    }

    public final /* synthetic */ zzbh zza(zzay com_google_android_gms_internal_config_zzay) throws IOException {
        while (true) {
            int zzy = com_google_android_gms_internal_config_zzay.zzy();
            if (zzy == 0) {
                return this;
            }
            int length;
            Object obj;
            if (zzy == 10) {
                zzy = zzbk.zzb(com_google_android_gms_internal_config_zzay, 10);
                length = this.zzbg == null ? 0 : this.zzbg.length;
                obj = new zzav[(zzy + length)];
                if (length != 0) {
                    System.arraycopy(this.zzbg, 0, obj, 0, length);
                }
                while (length < obj.length - 1) {
                    obj[length] = new zzav();
                    com_google_android_gms_internal_config_zzay.zza(obj[length]);
                    com_google_android_gms_internal_config_zzay.zzy();
                    length++;
                }
                obj[length] = new zzav();
                com_google_android_gms_internal_config_zzay.zza(obj[length]);
                this.zzbg = obj;
            } else if (zzy == 17) {
                this.timestamp = com_google_android_gms_internal_config_zzay.zzaa();
            } else if (zzy == 26) {
                zzy = zzbk.zzb(com_google_android_gms_internal_config_zzay, 26);
                length = this.zzbh == null ? 0 : this.zzbh.length;
                obj = new byte[(zzy + length)][];
                if (length != 0) {
                    System.arraycopy(this.zzbh, 0, obj, 0, length);
                }
                while (length < obj.length - 1) {
                    obj[length] = com_google_android_gms_internal_config_zzay.readBytes();
                    com_google_android_gms_internal_config_zzay.zzy();
                    length++;
                }
                obj[length] = com_google_android_gms_internal_config_zzay.readBytes();
                this.zzbh = obj;
            } else if (!super.zza(com_google_android_gms_internal_config_zzay, zzy)) {
                return this;
            }
        }
    }

    public final void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
        int i = 0;
        if (this.zzbg != null && this.zzbg.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbg) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    com_google_android_gms_internal_config_zzaz.zza(1, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        if (this.timestamp != 0) {
            com_google_android_gms_internal_config_zzaz.zza(2, this.timestamp);
        }
        if (this.zzbh != null && this.zzbh.length > 0) {
            while (i < this.zzbh.length) {
                byte[] bArr = this.zzbh[i];
                if (bArr != null) {
                    com_google_android_gms_internal_config_zzaz.zza(3, bArr);
                }
                i++;
            }
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzu() {
        int i;
        int zzu = super.zzu();
        int i2 = 0;
        if (this.zzbg != null && this.zzbg.length > 0) {
            i = zzu;
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbg) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    i += zzaz.zzb(1, com_google_android_gms_internal_config_zzbh);
                }
            }
            zzu = i;
        }
        if (this.timestamp != 0) {
            zzu += zzaz.zzl(2) + 8;
        }
        if (this.zzbh == null || this.zzbh.length <= 0) {
            return zzu;
        }
        i = 0;
        int i3 = i;
        while (i2 < this.zzbh.length) {
            byte[] bArr = this.zzbh[i2];
            if (bArr != null) {
                i3++;
                i += zzaz.zzb(bArr);
            }
            i2++;
        }
        return (zzu + i) + (1 * i3);
    }
}
