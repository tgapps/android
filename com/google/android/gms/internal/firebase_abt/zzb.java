package com.google.android.gms.internal.firebase_abt;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class zzb {
    private final ByteBuffer zzr;

    private zzb(ByteBuffer byteBuffer) {
        this.zzr = byteBuffer;
        this.zzr.order(ByteOrder.LITTLE_ENDIAN);
    }

    private zzb(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, 0, i2));
    }

    public static zzb zzb(byte[] bArr) {
        return new zzb(bArr, 0, bArr.length);
    }

    private final void zzd(int i) throws IOException {
        byte b = (byte) i;
        if (this.zzr.hasRemaining()) {
            this.zzr.put(b);
            return;
        }
        throw new zzc(this.zzr.position(), this.zzr.limit());
    }

    public static int zzf(int i) {
        return (i & -128) == 0 ? 1 : (i & -16384) == 0 ? 2 : (-2097152 & i) == 0 ? 3 : (i & -268435456) == 0 ? 4 : 5;
    }

    public final void zzc(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (this.zzr.remaining() >= length) {
            this.zzr.put(bArr, 0, length);
            return;
        }
        throw new zzc(this.zzr.position(), this.zzr.limit());
    }

    public final void zze(int i) throws IOException {
        while ((i & -128) != 0) {
            zzd((i & 127) | 128);
            i >>>= 7;
        }
        zzd(i);
    }
}
