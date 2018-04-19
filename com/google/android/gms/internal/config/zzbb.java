package com.google.android.gms.internal.config;

import java.io.IOException;

public abstract class zzbb<M extends zzbb<M>> extends zzbh {
    protected zzbd zzci;

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzbb com_google_android_gms_internal_config_zzbb = (zzbb) super.zzae();
        zzbf.zza(this, com_google_android_gms_internal_config_zzbb);
        return com_google_android_gms_internal_config_zzbb;
    }

    public void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
        if (this.zzci != null) {
            for (int i = 0; i < this.zzci.size(); i++) {
                this.zzci.zzp(i).zza(com_google_android_gms_internal_config_zzaz);
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
        if (this.zzci == null) {
            this.zzci = new zzbd();
        } else {
            com_google_android_gms_internal_config_zzbe = this.zzci.zzo(i2);
        }
        if (com_google_android_gms_internal_config_zzbe == null) {
            com_google_android_gms_internal_config_zzbe = new zzbe();
            this.zzci.zza(i2, com_google_android_gms_internal_config_zzbe);
        }
        com_google_android_gms_internal_config_zzbe.zza(com_google_android_gms_internal_config_zzbj);
        return true;
    }

    public final /* synthetic */ zzbh zzae() throws CloneNotSupportedException {
        return (zzbb) clone();
    }

    protected int zzu() {
        if (this.zzci == null) {
            return 0;
        }
        int i = 0;
        int i2 = 0;
        while (i < this.zzci.size()) {
            i++;
            i2 = this.zzci.zzp(i).zzu() + i2;
        }
        return i2;
    }
}
