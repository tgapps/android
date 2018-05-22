package com.google.android.gms.internal.config;

import java.io.IOException;

public abstract class zzbb<M extends zzbb<M>> extends zzbh {
    protected zzbd zzch;

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzbb com_google_android_gms_internal_config_zzbb = (zzbb) super.zzad();
        zzbf.zza(this, com_google_android_gms_internal_config_zzbb);
        return com_google_android_gms_internal_config_zzbb;
    }

    public void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
        if (this.zzch != null) {
            for (int i = 0; i < this.zzch.size(); i++) {
                this.zzch.zzp(i).zza(com_google_android_gms_internal_config_zzaz);
            }
        }
    }

    protected final boolean zza(zzay com_google_android_gms_internal_config_zzay, int i) throws IOException {
        int position = com_google_android_gms_internal_config_zzay.getPosition();
        if (!com_google_android_gms_internal_config_zzay.zzh(i)) {
            return false;
        }
        int i2 = i >>> 3;
        zzbj com_google_android_gms_internal_config_zzbj = new zzbj(i, com_google_android_gms_internal_config_zzay.zza(position, com_google_android_gms_internal_config_zzay.getPosition() - position));
        zzbe com_google_android_gms_internal_config_zzbe = null;
        if (this.zzch == null) {
            this.zzch = new zzbd();
        } else {
            com_google_android_gms_internal_config_zzbe = this.zzch.zzo(i2);
        }
        if (com_google_android_gms_internal_config_zzbe == null) {
            com_google_android_gms_internal_config_zzbe = new zzbe();
            this.zzch.zza(i2, com_google_android_gms_internal_config_zzbe);
        }
        com_google_android_gms_internal_config_zzbe.zza(com_google_android_gms_internal_config_zzbj);
        return true;
    }

    public final /* synthetic */ zzbh zzad() throws CloneNotSupportedException {
        return (zzbb) clone();
    }

    protected int zzt() {
        if (this.zzch == null) {
            return 0;
        }
        int i = 0;
        int i2 = 0;
        while (i < this.zzch.size()) {
            i++;
            i2 = this.zzch.zzp(i).zzt() + i2;
        }
        return i2;
    }
}
