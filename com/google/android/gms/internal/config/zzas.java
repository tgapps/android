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
        return !zzbf.equals(this.zzbg, com_google_android_gms_internal_config_zzas.zzbg) ? false : this.timestamp != com_google_android_gms_internal_config_zzas.timestamp ? false : !zzbf.zza(this.zzbh, com_google_android_gms_internal_config_zzas.zzbh) ? false : (this.zzci == null || this.zzci.isEmpty()) ? com_google_android_gms_internal_config_zzas.zzci == null || com_google_android_gms_internal_config_zzas.zzci.isEmpty() : this.zzci.equals(com_google_android_gms_internal_config_zzas.zzci);
    }

    public final int hashCode() {
        int hashCode = (((((((getClass().getName().hashCode() + 527) * 31) + zzbf.hashCode(this.zzbg)) * 31) + ((int) (this.timestamp ^ (this.timestamp >>> 32)))) * 31) + zzbf.zza(this.zzbh)) * 31;
        int hashCode2 = (this.zzci == null || this.zzci.isEmpty()) ? 0 : this.zzci.hashCode();
        return hashCode2 + hashCode;
    }

    public final /* synthetic */ zzbh zza(zzay com_google_android_gms_internal_config_zzay) throws IOException {
        while (true) {
            int zzy = com_google_android_gms_internal_config_zzay.zzy();
            int zzb;
            Object obj;
            switch (zzy) {
                case 0:
                    break;
                case 10:
                    zzb = zzbk.zzb(com_google_android_gms_internal_config_zzay, 10);
                    zzy = this.zzbg == null ? 0 : this.zzbg.length;
                    obj = new zzav[(zzb + zzy)];
                    if (zzy != 0) {
                        System.arraycopy(this.zzbg, 0, obj, 0, zzy);
                    }
                    while (zzy < obj.length - 1) {
                        obj[zzy] = new zzav();
                        com_google_android_gms_internal_config_zzay.zza(obj[zzy]);
                        com_google_android_gms_internal_config_zzay.zzy();
                        zzy++;
                    }
                    obj[zzy] = new zzav();
                    com_google_android_gms_internal_config_zzay.zza(obj[zzy]);
                    this.zzbg = obj;
                    continue;
                case 17:
                    this.timestamp = com_google_android_gms_internal_config_zzay.zzaa();
                    continue;
                case 26:
                    zzb = zzbk.zzb(com_google_android_gms_internal_config_zzay, 26);
                    zzy = this.zzbh == null ? 0 : this.zzbh.length;
                    obj = new byte[(zzb + zzy)][];
                    if (zzy != 0) {
                        System.arraycopy(this.zzbh, 0, obj, 0, zzy);
                    }
                    while (zzy < obj.length - 1) {
                        obj[zzy] = com_google_android_gms_internal_config_zzay.readBytes();
                        com_google_android_gms_internal_config_zzay.zzy();
                        zzy++;
                    }
                    obj[zzy] = com_google_android_gms_internal_config_zzay.readBytes();
                    this.zzbh = obj;
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
        int zzu = super.zzu();
        if (this.zzbg != null && this.zzbg.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbg) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    zzu += zzaz.zzb(1, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        if (this.timestamp != 0) {
            zzu += zzaz.zzl(2) + 8;
        }
        if (this.zzbh == null || this.zzbh.length <= 0) {
            return zzu;
        }
        int i = 0;
        int i2 = 0;
        for (byte[] bArr : this.zzbh) {
            if (bArr != null) {
                i2++;
                i += zzaz.zzb(bArr);
            }
        }
        return (zzu + i) + (i2 * 1);
    }
}
