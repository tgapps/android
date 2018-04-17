package com.google.android.gms.internal.config;

import java.io.IOException;

public final class zzau extends zzbb<zzau> {
    public int zzbl;
    public boolean zzbm;
    public long zzbn;

    public zzau() {
        this.zzbl = 0;
        this.zzbm = false;
        this.zzbn = 0;
        this.zzci = null;
        this.zzcr = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzau)) {
            return false;
        }
        zzau com_google_android_gms_internal_config_zzau = (zzau) obj;
        if (this.zzbl != com_google_android_gms_internal_config_zzau.zzbl || this.zzbm != com_google_android_gms_internal_config_zzau.zzbm || this.zzbn != com_google_android_gms_internal_config_zzau.zzbn) {
            return false;
        }
        if (this.zzci != null) {
            if (!this.zzci.isEmpty()) {
                return this.zzci.equals(com_google_android_gms_internal_config_zzau.zzci);
            }
        }
        return com_google_android_gms_internal_config_zzau.zzci == null || com_google_android_gms_internal_config_zzau.zzci.isEmpty();
    }

    public final int hashCode() {
        int hashCode;
        int hashCode2 = (((((((527 + getClass().getName().hashCode()) * 31) + this.zzbl) * 31) + (this.zzbm ? 1231 : 1237)) * 31) + ((int) (this.zzbn ^ (this.zzbn >>> 32)))) * 31;
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
            if (zzy == 8) {
                this.zzbl = com_google_android_gms_internal_config_zzay.zzz();
            } else if (zzy == 16) {
                this.zzbm = com_google_android_gms_internal_config_zzay.zzz() != 0;
            } else if (zzy == 25) {
                this.zzbn = com_google_android_gms_internal_config_zzay.zzaa();
            } else if (!super.zza(com_google_android_gms_internal_config_zzay, zzy)) {
                return this;
            }
        }
    }

    public final void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
        if (this.zzbl != 0) {
            com_google_android_gms_internal_config_zzaz.zzc(1, this.zzbl);
        }
        if (this.zzbm) {
            boolean z = this.zzbm;
            com_google_android_gms_internal_config_zzaz.zze(2, 0);
            com_google_android_gms_internal_config_zzaz.zza((byte) z);
        }
        if (this.zzbn != 0) {
            com_google_android_gms_internal_config_zzaz.zza(3, this.zzbn);
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzu() {
        int zzu = super.zzu();
        if (this.zzbl != 0) {
            zzu += zzaz.zzd(1, this.zzbl);
        }
        if (this.zzbm) {
            zzu += zzaz.zzl(2) + 1;
        }
        return this.zzbn != 0 ? zzu + (zzaz.zzl(3) + 8) : zzu;
    }
}
