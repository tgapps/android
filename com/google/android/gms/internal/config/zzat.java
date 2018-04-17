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
        if (!Arrays.equals(this.zzbk, com_google_android_gms_internal_config_zzat.zzbk)) {
            return false;
        }
        if (this.zzci != null) {
            if (!this.zzci.isEmpty()) {
                return this.zzci.equals(com_google_android_gms_internal_config_zzat.zzci);
            }
        }
        return com_google_android_gms_internal_config_zzat.zzci == null || com_google_android_gms_internal_config_zzat.zzci.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((527 + getClass().getName().hashCode()) * 31) + (this.zzbj == null ? 0 : this.zzbj.hashCode())) * 31) + Arrays.hashCode(this.zzbk)) * 31;
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
            if (zzy == 10) {
                this.zzbj = com_google_android_gms_internal_config_zzay.readString();
            } else if (zzy == 18) {
                this.zzbk = com_google_android_gms_internal_config_zzay.readBytes();
            } else if (!super.zza(com_google_android_gms_internal_config_zzay, zzy)) {
                return this;
            }
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
        return zzu + (zzaz.zzl(2) + zzaz.zzb(this.zzbk));
    }
}
