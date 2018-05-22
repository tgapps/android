package com.google.android.gms.internal.config;

import java.io.IOException;
import java.util.Arrays;

public final class zzat extends zzbb<zzat> {
    private static volatile zzat[] zzbh;
    public String zzbi;
    public byte[] zzbj;

    public zzat() {
        this.zzbi = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzbj = zzbk.zzdd;
        this.zzch = null;
        this.zzcq = -1;
    }

    public static zzat[] zzu() {
        if (zzbh == null) {
            synchronized (zzbf.zzcp) {
                if (zzbh == null) {
                    zzbh = new zzat[0];
                }
            }
        }
        return zzbh;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzat)) {
            return false;
        }
        zzat com_google_android_gms_internal_config_zzat = (zzat) obj;
        if (this.zzbi == null) {
            if (com_google_android_gms_internal_config_zzat.zzbi != null) {
                return false;
            }
        } else if (!this.zzbi.equals(com_google_android_gms_internal_config_zzat.zzbi)) {
            return false;
        }
        return !Arrays.equals(this.zzbj, com_google_android_gms_internal_config_zzat.zzbj) ? false : (this.zzch == null || this.zzch.isEmpty()) ? com_google_android_gms_internal_config_zzat.zzch == null || com_google_android_gms_internal_config_zzat.zzch.isEmpty() : this.zzch.equals(com_google_android_gms_internal_config_zzat.zzch);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((this.zzbi == null ? 0 : this.zzbi.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + Arrays.hashCode(this.zzbj)) * 31;
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
                    this.zzbi = com_google_android_gms_internal_config_zzay.readString();
                    continue;
                case 18:
                    this.zzbj = com_google_android_gms_internal_config_zzay.readBytes();
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
        if (!(this.zzbi == null || this.zzbi.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_config_zzaz.zza(1, this.zzbi);
        }
        if (!Arrays.equals(this.zzbj, zzbk.zzdd)) {
            com_google_android_gms_internal_config_zzaz.zza(2, this.zzbj);
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzt() {
        int zzt = super.zzt();
        if (!(this.zzbi == null || this.zzbi.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zzt += zzaz.zzb(1, this.zzbi);
        }
        if (Arrays.equals(this.zzbj, zzbk.zzdd)) {
            return zzt;
        }
        byte[] bArr = this.zzbj;
        return zzt + (zzaz.zzb(bArr) + zzaz.zzl(2));
    }
}
