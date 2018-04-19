package com.google.android.gms.internal.measurement;

import java.io.IOException;

public abstract class zzabd<M extends zzabd<M>> extends zzabj {
    protected zzabf zzbzh;

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzabd com_google_android_gms_internal_measurement_zzabd = (zzabd) super.zzvz();
        zzabh.zza(this, com_google_android_gms_internal_measurement_zzabd);
        return com_google_android_gms_internal_measurement_zzabd;
    }

    protected int zza() {
        if (this.zzbzh == null) {
            return 0;
        }
        int i = 0;
        int i2 = 0;
        while (i < this.zzbzh.size()) {
            i++;
            i2 = this.zzbzh.zzaw(i).zza() + i2;
        }
        return i2;
    }

    public void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzbzh != null) {
            for (int i = 0; i < this.zzbzh.size(); i++) {
                this.zzbzh.zzaw(i).zza(com_google_android_gms_internal_measurement_zzabb);
            }
        }
    }

    protected final boolean zza(zzaba com_google_android_gms_internal_measurement_zzaba, int i) throws IOException {
        int position = com_google_android_gms_internal_measurement_zzaba.getPosition();
        if (!com_google_android_gms_internal_measurement_zzaba.zzam(i)) {
            return false;
        }
        int i2 = i >>> 3;
        zzabl com_google_android_gms_internal_measurement_zzabl = new zzabl(i, com_google_android_gms_internal_measurement_zzaba.zzc(position, com_google_android_gms_internal_measurement_zzaba.getPosition() - position));
        zzabg com_google_android_gms_internal_measurement_zzabg = null;
        if (this.zzbzh == null) {
            this.zzbzh = new zzabf();
        } else {
            com_google_android_gms_internal_measurement_zzabg = this.zzbzh.zzav(i2);
        }
        if (com_google_android_gms_internal_measurement_zzabg == null) {
            com_google_android_gms_internal_measurement_zzabg = new zzabg();
            this.zzbzh.zza(i2, com_google_android_gms_internal_measurement_zzabg);
        }
        com_google_android_gms_internal_measurement_zzabg.zza(com_google_android_gms_internal_measurement_zzabl);
        return true;
    }

    public final /* synthetic */ zzabj zzvz() throws CloneNotSupportedException {
        return (zzabd) clone();
    }
}
