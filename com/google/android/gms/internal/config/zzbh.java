package com.google.android.gms.internal.config;

import java.io.IOException;

public abstract class zzbh {
    protected volatile int zzcr = -1;

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzae();
    }

    public String toString() {
        return zzbi.zzb(this);
    }

    public abstract zzbh zza(zzay com_google_android_gms_internal_config_zzay) throws IOException;

    public void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
    }

    public zzbh zzae() throws CloneNotSupportedException {
        return (zzbh) super.clone();
    }

    public final int zzai() {
        int zzu = zzu();
        this.zzcr = zzu;
        return zzu;
    }

    protected int zzu() {
        return 0;
    }
}
