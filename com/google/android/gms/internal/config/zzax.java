package com.google.android.gms.internal.config;

import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;

public final class zzax extends zzbb<zzax> {
    private static volatile zzax[] zzbu;
    public String namespace;
    public int resourceId;
    public long zzbv;

    public zzax() {
        this.resourceId = 0;
        this.zzbv = 0;
        this.namespace = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzch = null;
        this.zzcq = -1;
    }

    public static zzax[] zzw() {
        if (zzbu == null) {
            synchronized (zzbf.zzcp) {
                if (zzbu == null) {
                    zzbu = new zzax[0];
                }
            }
        }
        return zzbu;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzax)) {
            return false;
        }
        zzax com_google_android_gms_internal_config_zzax = (zzax) obj;
        if (this.resourceId != com_google_android_gms_internal_config_zzax.resourceId) {
            return false;
        }
        if (this.zzbv != com_google_android_gms_internal_config_zzax.zzbv) {
            return false;
        }
        if (this.namespace == null) {
            if (com_google_android_gms_internal_config_zzax.namespace != null) {
                return false;
            }
        } else if (!this.namespace.equals(com_google_android_gms_internal_config_zzax.namespace)) {
            return false;
        }
        return (this.zzch == null || this.zzch.isEmpty()) ? com_google_android_gms_internal_config_zzax.zzch == null || com_google_android_gms_internal_config_zzax.zzch.isEmpty() : this.zzch.equals(com_google_android_gms_internal_config_zzax.zzch);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.namespace == null ? 0 : this.namespace.hashCode()) + ((((((getClass().getName().hashCode() + 527) * 31) + this.resourceId) * 31) + ((int) (this.zzbv ^ (this.zzbv >>> 32)))) * 31)) * 31;
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
                case 8:
                    this.resourceId = com_google_android_gms_internal_config_zzay.zzy();
                    continue;
                case 17:
                    this.zzbv = com_google_android_gms_internal_config_zzay.zzz();
                    continue;
                case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                    this.namespace = com_google_android_gms_internal_config_zzay.readString();
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
        if (this.resourceId != 0) {
            com_google_android_gms_internal_config_zzaz.zzc(1, this.resourceId);
        }
        if (this.zzbv != 0) {
            com_google_android_gms_internal_config_zzaz.zza(2, this.zzbv);
        }
        if (!(this.namespace == null || this.namespace.equals(TtmlNode.ANONYMOUS_REGION_ID))) {
            com_google_android_gms_internal_config_zzaz.zza(3, this.namespace);
        }
        super.zza(com_google_android_gms_internal_config_zzaz);
    }

    protected final int zzt() {
        int zzt = super.zzt();
        if (this.resourceId != 0) {
            zzt += zzaz.zzd(1, this.resourceId);
        }
        if (this.zzbv != 0) {
            zzt += zzaz.zzl(2) + 8;
        }
        return (this.namespace == null || this.namespace.equals(TtmlNode.ANONYMOUS_REGION_ID)) ? zzt : zzt + zzaz.zzb(3, this.namespace);
    }
}
