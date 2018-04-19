package com.google.android.gms.internal.config;

import java.io.IOException;
import java.util.Arrays;

public final class zzat extends zzbb<zzat> {
    private static volatile zzat[] zzbi;
    public String zzbj;
    public byte[] zzbk;

    public zzat() {
        this.zzbj = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzbk = zzbk.zzde;
        this.zzci = null;
        this.zzcr = -1;
    }

    public static zzat[] zzv() {
        if (zzbi == null) {
            synchronized (zzbf.zzcq) {
                if (zzbi == null) {
                    zzbi = new zzat[0];
                }
            }
        }
        return zzbi;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzat)) {
            return false;
        }
        zzat com_google_android_gms_internal_config_zzat = (zzat) obj;
        if (this.zzbj == null) {
            if (com_google_android_gms_internal_config_zzat.zzbj != null) {
                return false;
            }
        } else if (!this.zzbj.equals(com_google_android_gms_internal_config_zzat.zzbj)) {
            return false;
        }
        return !Arrays.equals(this.zzbk, com_google_android_gms_internal_config_zzat.zzbk) ? false : (this.zzci == null || this.zzci.isEmpty()) ? com_google_android_gms_internal_config_zzat.zzci == null || com_google_android_gms_internal_config_zzat.zzci.isEmpty() : this.zzci.equals(com_google_android_gms_internal_config_zzat.zzci);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((this.zzbj == null ? 0 : this.zzbj.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + Arrays.hashCode(this.zzbk)) * 31;
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
                    this.zzbj = com_google_android_gms_internal_config_zzay.readString();
                    continue;
                case 18:
                    this.zzbk = com_google_android_gms_internal_config_zzay.readBytes();
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
        if (!(this.zzbj == null || this.zzbj.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_config_zzaz.zza(1, this.zzbj);
        }
        if (!Arrays.equals(this.zzbk, zzbk.zzde)) {
            com_google_android_gms_internal_config_zzaz.zza(2, this.zzbk);
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzu() {
        int zzu = super.zzu();
        if (!(this.zzbj == null || this.zzbj.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zzu += zzaz.zzb(1, this.zzbj);
        }
        if (Arrays.equals(this.zzbk, zzbk.zzde)) {
            return zzu;
        }
        byte[] bArr = this.zzbk;
        return zzu + (zzaz.zzb(bArr) + zzaz.zzl(2));
    }
}
