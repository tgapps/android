package com.google.android.gms.internal.measurement;

import java.io.IOException;

public abstract class zzzg {
    protected volatile int zzcfm = -1;

    public abstract zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException;

    public final int zzza() {
        if (this.zzcfm < 0) {
            zzvu();
        }
        return this.zzcfm;
    }

    public final int zzvu() {
        int zzf = zzf();
        this.zzcfm = zzf;
        return zzf;
    }

    protected int zzf() {
        return 0;
    }

    public void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
    }

    public String toString() {
        return zzzh.zzc(this);
    }

    public zzzg zzyu() throws CloneNotSupportedException {
        return (zzzg) super.clone();
    }

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzyu();
    }
}
