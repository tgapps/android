package com.google.android.gms.internal.measurement;

import java.io.IOException;

public abstract class zzace {
    protected volatile int zzbxh = -1;

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzvf();
    }

    public String toString() {
        return zzacf.zzc(this);
    }

    protected int zza() {
        return 0;
    }

    public void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
    }

    public abstract zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException;

    public zzace zzvf() throws CloneNotSupportedException {
        return (zzace) super.clone();
    }

    public final int zzvl() {
        if (this.zzbxh < 0) {
            zzvm();
        }
        return this.zzbxh;
    }

    public final int zzvm() {
        int zza = zza();
        this.zzbxh = zza;
        return zza;
    }
}
