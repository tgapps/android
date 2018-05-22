package com.google.android.gms.internal.config;

import java.io.IOException;

public final class zzav extends zzbb<zzav> {
    private static volatile zzav[] zzbn;
    public String namespace;
    public zzat[] zzbo;

    public zzav() {
        this.namespace = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzbo = zzat.zzu();
        this.zzch = null;
        this.zzcq = -1;
    }

    public static zzav[] zzv() {
        if (zzbn == null) {
            synchronized (zzbf.zzcp) {
                if (zzbn == null) {
                    zzbn = new zzav[0];
                }
            }
        }
        return zzbn;
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
        return !zzbf.equals(this.zzbo, com_google_android_gms_internal_config_zzav.zzbo) ? false : (this.zzch == null || this.zzch.isEmpty()) ? com_google_android_gms_internal_config_zzav.zzch == null || com_google_android_gms_internal_config_zzav.zzch.isEmpty() : this.zzch.equals(com_google_android_gms_internal_config_zzav.zzch);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((this.namespace == null ? 0 : this.namespace.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + zzbf.hashCode(this.zzbo)) * 31;
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
                    this.namespace = com_google_android_gms_internal_config_zzay.readString();
                    continue;
                case 18:
                    int zzb = zzbk.zzb(com_google_android_gms_internal_config_zzay, 18);
                    zzx = this.zzbo == null ? 0 : this.zzbo.length;
                    Object obj = new zzat[(zzb + zzx)];
                    if (zzx != 0) {
                        System.arraycopy(this.zzbo, 0, obj, 0, zzx);
                    }
                    while (zzx < obj.length - 1) {
                        obj[zzx] = new zzat();
                        com_google_android_gms_internal_config_zzay.zza(obj[zzx]);
                        com_google_android_gms_internal_config_zzay.zzx();
                        zzx++;
                    }
                    obj[zzx] = new zzat();
                    com_google_android_gms_internal_config_zzay.zza(obj[zzx]);
                    this.zzbo = obj;
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
        if (!(this.namespace == null || this.namespace.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_config_zzaz.zza(1, this.namespace);
        }
        if (this.zzbo != null && this.zzbo.length > 0) {
            for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbo) {
                if (com_google_android_gms_internal_config_zzbh != null) {
                    com_google_android_gms_internal_config_zzaz.zza(2, com_google_android_gms_internal_config_zzbh);
                }
            }
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzt() {
        int zzt = super.zzt();
        if (!(this.namespace == null || this.namespace.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            zzt += zzaz.zzb(1, this.namespace);
        }
        if (this.zzbo == null || this.zzbo.length <= 0) {
            return zzt;
        }
        int i = zzt;
        for (zzbh com_google_android_gms_internal_config_zzbh : this.zzbo) {
            if (com_google_android_gms_internal_config_zzbh != null) {
                i += zzaz.zzb(2, com_google_android_gms_internal_config_zzbh);
            }
        }
        return i;
    }
}
