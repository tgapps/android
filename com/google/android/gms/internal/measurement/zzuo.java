package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.tgnet.ConnectionsManager;

public abstract class zzuo {
    int zzbuh;
    int zzbui;
    private int zzbuj;
    zzur zzbuk;
    private boolean zzbul;

    public static zzuo zzd(byte[] bArr, int i, int i2) {
        return zza(bArr, i, i2, false);
    }

    public abstract double readDouble() throws IOException;

    public abstract float readFloat() throws IOException;

    public abstract String readString() throws IOException;

    public abstract <T extends zzwt> T zza(zzxd<T> com_google_android_gms_internal_measurement_zzxd_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException;

    public abstract void zzan(int i) throws zzvt;

    public abstract boolean zzao(int i) throws IOException;

    public abstract int zzaq(int i) throws zzvt;

    public abstract void zzar(int i);

    public abstract void zzas(int i) throws IOException;

    public abstract int zzug() throws IOException;

    public abstract long zzuh() throws IOException;

    public abstract long zzui() throws IOException;

    public abstract int zzuj() throws IOException;

    public abstract long zzuk() throws IOException;

    public abstract int zzul() throws IOException;

    public abstract boolean zzum() throws IOException;

    public abstract String zzun() throws IOException;

    public abstract zzud zzuo() throws IOException;

    public abstract int zzup() throws IOException;

    public abstract int zzuq() throws IOException;

    public abstract int zzur() throws IOException;

    public abstract long zzus() throws IOException;

    public abstract int zzut() throws IOException;

    public abstract long zzuu() throws IOException;

    abstract long zzuv() throws IOException;

    public abstract boolean zzuw() throws IOException;

    public abstract int zzux();

    static zzuo zza(byte[] bArr, int i, int i2, boolean z) {
        zzuo com_google_android_gms_internal_measurement_zzuq = new zzuq(bArr, i, i2, false, null);
        try {
            com_google_android_gms_internal_measurement_zzuq.zzaq(i2);
            return com_google_android_gms_internal_measurement_zzuq;
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }

    private zzuo() {
        this.zzbui = 100;
        this.zzbuj = ConnectionsManager.DEFAULT_DATACENTER_ID;
        this.zzbul = false;
    }

    public final int zzap(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Recursion limit cannot be negative: " + i);
        }
        int i2 = this.zzbui;
        this.zzbui = i;
        return i2;
    }
}
