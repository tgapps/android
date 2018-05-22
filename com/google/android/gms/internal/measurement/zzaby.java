package com.google.android.gms.internal.measurement;

import java.io.IOException;

public abstract class zzaby<M extends zzaby<M>> extends zzace {
    protected zzaca zzbww;

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzaby com_google_android_gms_internal_measurement_zzaby = (zzaby) super.zzvf();
        zzacc.zza(this, com_google_android_gms_internal_measurement_zzaby);
        return com_google_android_gms_internal_measurement_zzaby;
    }

    protected int zza() {
        if (this.zzbww == null) {
            return 0;
        }
        int i = 0;
        int i2 = 0;
        while (i < this.zzbww.size()) {
            i++;
            i2 = this.zzbww.zzau(i).zza() + i2;
        }
        return i2;
    }

    public void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        if (this.zzbww != null) {
            for (int i = 0; i < this.zzbww.size(); i++) {
                this.zzbww.zzau(i).zza(com_google_android_gms_internal_measurement_zzabw);
            }
        }
    }

    protected final boolean zza(zzabv com_google_android_gms_internal_measurement_zzabv, int i) throws IOException {
        int position = com_google_android_gms_internal_measurement_zzabv.getPosition();
        if (!com_google_android_gms_internal_measurement_zzabv.zzak(i)) {
            return false;
        }
        int i2 = i >>> 3;
        zzacg com_google_android_gms_internal_measurement_zzacg = new zzacg(i, com_google_android_gms_internal_measurement_zzabv.zzc(position, com_google_android_gms_internal_measurement_zzabv.getPosition() - position));
        zzacb com_google_android_gms_internal_measurement_zzacb = null;
        if (this.zzbww == null) {
            this.zzbww = new zzaca();
        } else {
            com_google_android_gms_internal_measurement_zzacb = this.zzbww.zzat(i2);
        }
        if (com_google_android_gms_internal_measurement_zzacb == null) {
            com_google_android_gms_internal_measurement_zzacb = new zzacb();
            this.zzbww.zza(i2, com_google_android_gms_internal_measurement_zzacb);
        }
        com_google_android_gms_internal_measurement_zzacb.zza(com_google_android_gms_internal_measurement_zzacg);
        return true;
    }

    public final /* synthetic */ zzace zzvf() throws CloneNotSupportedException {
        return (zzaby) clone();
    }
}
