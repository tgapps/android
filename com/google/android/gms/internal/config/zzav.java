package com.google.android.gms.internal.config;

import java.io.IOException;

public final class zzav extends zzbb<zzav> {
    private static volatile zzav[] zzbo;
    public String namespace;
    public zzat[] zzbp;

    public zzav() {
        this.namespace = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzbp = zzat.zzv();
        this.zzci = null;
        this.zzcr = -1;
    }

    public static zzav[] zzw() {
        if (zzbo == null) {
            synchronized (zzbf.zzcq) {
                if (zzbo == null) {
                    zzbo = new zzav[0];
                }
            }
        }
        return zzbo;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzav)) {
            return false;
        }
        zzav com_google_android_gms_internal_config_zzav = (zzav) obj;
        if (this.namespace == null) {
            if (com_google_android_gms_internal_config_zzav.namespace != null) {
                return false;
            }
        } else if (!this.namespace.equals(com_google_android_gms_internal_config_zzav.namespace)) {
            return false;
        }
        if (!zzbf.equals(this.zzbp, com_google_android_gms_internal_config_zzav.zzbp)) {
            return false;
        }
        if (this.zzci != null) {
            if (!this.zzci.isEmpty()) {
                return this.zzci.equals(com_google_android_gms_internal_config_zzav.zzci);
            }
        }
        return com_google_android_gms_internal_config_zzav.zzci == null || com_google_android_gms_internal_config_zzav.zzci.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((527 + getClass().getName().hashCode()) * 31) + (this.namespace == null ? 0 : this.namespace.hashCode())) * 31) + zzbf.hashCode(this.zzbp)) * 31;
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
                this.namespace = com_google_android_gms_internal_config_zzay.readString();
            } else if (zzy == 18) {
                zzy = zzbk.zzb(com_google_android_gms_internal_config_zzay, 18);
                int length = this.zzbp == null ? 0 : this.zzbp.length;
                Object obj = new zzat[(zzy + length)];
                if (length != 0) {
                    System.arraycopy(this.zzbp, 0, obj, 0, length);
                }
                while (length < obj.length - 1) {
                    obj[length] = new zzat();
                    com_google_android_gms_internal_config_zzay.zza(obj[length]);
                    com_google_android_gms_internal_config_zzay.zzy();
                    length++;
                }
                obj[length] = new zzat();
                com_google_android_gms_internal_config_zzay.zza(obj[length]);
                this.zzbp = obj;
            } else if (!super.zza(com_google_android_gms_internal_config_zzay, zzy)) {
                return this;
            }
        }
    }

    public final void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
        if (!(this.namespace == null || this.namespace.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_config_zzaz.zza(1, this.namespace);
        }
        if (this.zzbp != null && this.zzbp.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbp) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    com_google_android_gms_internal_config_zzaz.zza(2, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzu() {
        int zzu = super.zzu();
        if (!(this.namespace == null || this.namespace.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zzu += zzaz.zzb(1, this.namespace);
        }
        if (this.zzbp != null && this.zzbp.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbp) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    zzu += zzaz.zzb(2, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        return zzu;
    }
}
