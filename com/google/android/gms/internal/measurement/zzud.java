package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Iterator;

public abstract class zzud implements Serializable, Iterable<Byte> {
    public static final zzud zzbtz = new zzum(zzvo.zzbzj);
    private static final zzui zzbua = (zzua.zzty() ? new zzun() : new zzug());
    private static final Comparator<zzud> zzbub = new zzuf();
    private int zzbry = 0;

    zzud() {
    }

    public abstract boolean equals(Object obj);

    public abstract int size();

    protected abstract int zza(int i, int i2, int i3);

    protected abstract String zza(Charset charset);

    abstract void zza(zzuc com_google_android_gms_internal_measurement_zzuc) throws IOException;

    public abstract byte zzal(int i);

    public abstract zzud zzb(int i, int i2);

    public abstract boolean zzub();

    private static int zza(byte b) {
        return b & 255;
    }

    public static zzud zzb(byte[] bArr, int i, int i2) {
        zzb(i, i + i2, bArr.length);
        return new zzum(zzbua.zzc(bArr, i, i2));
    }

    static zzud zzi(byte[] bArr) {
        return new zzum(bArr);
    }

    public static zzud zzfv(String str) {
        return new zzum(str.getBytes(zzvo.UTF_8));
    }

    public final String zzua() {
        return size() == 0 ? TtmlNode.ANONYMOUS_REGION_ID : zza(zzvo.UTF_8);
    }

    public final int hashCode() {
        int i = this.zzbry;
        if (i == 0) {
            i = size();
            i = zza(i, 0, i);
            if (i == 0) {
                i = 1;
            }
            this.zzbry = i;
        }
        return i;
    }

    static zzuk zzam(int i) {
        return new zzuk(i);
    }

    protected final int zzuc() {
        return this.zzbry;
    }

    static int zzb(int i, int i2, int i3) {
        int i4 = i2 - i;
        if ((((i | i2) | i4) | (i3 - i2)) >= 0) {
            return i4;
        }
        if (i < 0) {
            throw new IndexOutOfBoundsException("Beginning index: " + i + " < 0");
        } else if (i2 < i) {
            throw new IndexOutOfBoundsException("Beginning index larger than ending index: " + i + ", " + i2);
        } else {
            throw new IndexOutOfBoundsException("End index: " + i2 + " >= " + i3);
        }
    }

    public final String toString() {
        return String.format("<ByteString@%s size=%d>", new Object[]{Integer.toHexString(System.identityHashCode(this)), Integer.valueOf(size())});
    }

    public /* synthetic */ Iterator iterator() {
        return new zzue(this);
    }
}
