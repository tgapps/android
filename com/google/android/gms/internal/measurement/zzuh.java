package com.google.android.gms.internal.measurement;

final class zzuh extends zzum {
    private final int zzbud;
    private final int zzbue;

    zzuh(byte[] bArr, int i, int i2) {
        super(bArr);
        zzud.zzb(i, i + i2, bArr.length);
        this.zzbud = i;
        this.zzbue = i2;
    }

    public final byte zzal(int i) {
        int size = size();
        if (((size - (i + 1)) | i) >= 0) {
            return this.zzbug[this.zzbud + i];
        }
        if (i < 0) {
            throw new ArrayIndexOutOfBoundsException("Index < 0: " + i);
        }
        throw new ArrayIndexOutOfBoundsException("Index > length: " + i + ", " + size);
    }

    public final int size() {
        return this.zzbue;
    }

    protected final int zzud() {
        return this.zzbud;
    }
}
