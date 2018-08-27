package com.google.android.gms.internal.measurement;

import java.io.IOException;

public abstract class zzza<M extends zzza<M>> extends zzzg {
    protected zzzc zzcfc;

    protected int zzf() {
        if (this.zzcfc == null) {
            return 0;
        }
        int i = 0;
        int i2 = 0;
        while (i < this.zzcfc.size()) {
            i++;
            i2 = this.zzcfc.zzcc(i).zzf() + i2;
        }
        return i2;
    }

    public void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzcfc != null) {
            for (int i = 0; i < this.zzcfc.size(); i++) {
                this.zzcfc.zzcc(i).zza(com_google_android_gms_internal_measurement_zzyy);
            }
        }
    }

    protected final boolean zza(zzyx com_google_android_gms_internal_measurement_zzyx, int i) throws IOException {
        int position = com_google_android_gms_internal_measurement_zzyx.getPosition();
        if (!com_google_android_gms_internal_measurement_zzyx.zzao(i)) {
            return false;
        }
        int i2 = i >>> 3;
        zzzi com_google_android_gms_internal_measurement_zzzi = new zzzi(i, com_google_android_gms_internal_measurement_zzyx.zzs(position, com_google_android_gms_internal_measurement_zzyx.getPosition() - position));
        zzzd com_google_android_gms_internal_measurement_zzzd = null;
        if (this.zzcfc == null) {
            this.zzcfc = new zzzc();
        } else {
            com_google_android_gms_internal_measurement_zzzd = this.zzcfc.zzcb(i2);
        }
        if (com_google_android_gms_internal_measurement_zzzd == null) {
            com_google_android_gms_internal_measurement_zzzd = new zzzd();
            this.zzcfc.zza(i2, com_google_android_gms_internal_measurement_zzzd);
        }
        com_google_android_gms_internal_measurement_zzzd.zza(com_google_android_gms_internal_measurement_zzzi);
        return true;
    }

    public final /* synthetic */ zzzg zzyu() throws CloneNotSupportedException {
        return (zzza) clone();
    }

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzza com_google_android_gms_internal_measurement_zzza = (zzza) super.zzyu();
        zzze.zza(this, com_google_android_gms_internal_measurement_zzza);
        return com_google_android_gms_internal_measurement_zzza;
    }
}
