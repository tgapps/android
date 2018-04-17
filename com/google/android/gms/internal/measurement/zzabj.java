package com.google.android.gms.internal.measurement;

import java.io.IOException;

public abstract class zzabj {
    protected volatile int zzbzs = -1;

    public static final <T extends zzabj> T zza(T t, byte[] bArr) throws zzabi {
        return zzb(t, bArr, 0, bArr.length);
    }

    private static final <T extends zzabj> T zzb(T t, byte[] bArr, int i, int i2) throws zzabi {
        try {
            zzaba zza = zzaba.zza(bArr, 0, i2);
            t.zzb(zza);
            zza.zzal(0);
            return t;
        } catch (zzabi e) {
            throw e;
        } catch (Throwable e2) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", e2);
        }
    }

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzvz();
    }

    public String toString() {
        return zzabk.zzc(this);
    }

    protected int zza() {
        return 0;
    }

    public void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
    }

    public abstract zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException;

    public zzabj zzvz() throws CloneNotSupportedException {
        return (zzabj) super.clone();
    }

    public final int zzwf() {
        if (this.zzbzs < 0) {
            zzwg();
        }
        return this.zzbzs;
    }

    public final int zzwg() {
        int zza = zza();
        this.zzbzs = zza;
        return zza;
    }
}
