package com.google.android.gms.internal.config;

import java.io.IOException;

public final class zzas extends zzbb<zzas> {
    public long timestamp;
    public zzav[] zzbf;
    public byte[][] zzbg;

    public zzas() {
        this.zzbf = zzav.zzv();
        this.timestamp = 0;
        this.zzbg = zzbk.zzdc;
        this.zzch = null;
        this.zzcq = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzas)) {
            return false;
        }
        zzas com_google_android_gms_internal_config_zzas = (zzas) obj;
        return !zzbf.equals(this.zzbf, com_google_android_gms_internal_config_zzas.zzbf) ? false : this.timestamp != com_google_android_gms_internal_config_zzas.timestamp ? false : !zzbf.zza(this.zzbg, com_google_android_gms_internal_config_zzas.zzbg) ? false : (this.zzch == null || this.zzch.isEmpty()) ? com_google_android_gms_internal_config_zzas.zzch == null || com_google_android_gms_internal_config_zzas.zzch.isEmpty() : this.zzch.equals(com_google_android_gms_internal_config_zzas.zzch);
    }

    public final int hashCode() {
        int hashCode = (((((((getClass().getName().hashCode() + 527) * 31) + zzbf.hashCode(this.zzbf)) * 31) + ((int) (this.timestamp ^ (this.timestamp >>> 32)))) * 31) + zzbf.zza(this.zzbg)) * 31;
        int hashCode2 = (this.zzch == null || this.zzch.isEmpty()) ? 0 : this.zzch.hashCode();
        return hashCode2 + hashCode;
    }

    public final /* synthetic */ zzbh zza(zzay com_google_android_gms_internal_config_zzay) throws IOException {
        while (true) {
            int zzx = com_google_android_gms_internal_config_zzay.zzx();
            int zzb;
            Object obj;
            switch (zzx) {
                case 0:
                    break;
                case 10:
                    zzb = zzbk.zzb(com_google_android_gms_internal_config_zzay, 10);
                    zzx = this.zzbf == null ? 0 : this.zzbf.length;
                    obj = new zzav[(zzb + zzx)];
                    if (zzx != 0) {
                        System.arraycopy(this.zzbf, 0, obj, 0, zzx);
                    }
                    while (zzx < obj.length - 1) {
                        obj[zzx] = new zzav();
                        com_google_android_gms_internal_config_zzay.zza(obj[zzx]);
                        com_google_android_gms_internal_config_zzay.zzx();
                        zzx++;
                    }
                    obj[zzx] = new zzav();
                    com_google_android_gms_internal_config_zzay.zza(obj[zzx]);
                    this.zzbf = obj;
                    continue;
                case 17:
                    this.timestamp = com_google_android_gms_internal_config_zzay.zzz();
                    continue;
                case 26:
                    zzb = zzbk.zzb(com_google_android_gms_internal_config_zzay, 26);
                    zzx = this.zzbg == null ? 0 : this.zzbg.length;
                    obj = new byte[(zzb + zzx)][];
                    if (zzx != 0) {
                        System.arraycopy(this.zzbg, 0, obj, 0, zzx);
                    }
                    while (zzx < obj.length - 1) {
                        obj[zzx] = com_google_android_gms_internal_config_zzay.readBytes();
                        com_google_android_gms_internal_config_zzay.zzx();
                        zzx++;
                    }
                    obj[zzx] = com_google_android_gms_internal_config_zzay.readBytes();
                    this.zzbg = obj;
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
        int i = 0;
        if (this.zzbf != null && this.zzbf.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbf) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    com_google_android_gms_internal_config_zzaz.zza(1, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        if (this.timestamp != 0) {
            com_google_android_gms_internal_config_zzaz.zza(2, this.timestamp);
        }
        if (this.zzbg != null && this.zzbg.length > 0) {
            while (i < this.zzbg.length) {
                byte[] bArr = this.zzbg[i];
                if (bArr != null) {
                    com_google_android_gms_internal_config_zzaz.zza(3, bArr);
                }
                i++;
            }
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzt() {
        int zzt = super.zzt();
        if (this.zzbf != null && this.zzbf.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbf) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    zzt += zzaz.zzb(1, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        if (this.timestamp != 0) {
            zzt += zzaz.zzl(2) + 8;
        }
        if (this.zzbg == null || this.zzbg.length <= 0) {
            return zzt;
        }
        int i = 0;
        int i2 = 0;
        for (byte[] bArr : this.zzbg) {
            if (bArr != null) {
                i2++;
                i += zzaz.zzb(bArr);
            }
        }
        return (zzt + i) + (i2 * 1);
    }
}
