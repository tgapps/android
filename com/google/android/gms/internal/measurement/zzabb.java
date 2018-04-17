package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.extractor.ts.PsExtractor;

public final class zzabb {
    private final ByteBuffer zzbzg;

    private zzabb(ByteBuffer byteBuffer) {
        this.zzbzg = byteBuffer;
        this.zzbzg.order(ByteOrder.LITTLE_ENDIAN);
    }

    private zzabb(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, i, i2));
    }

    private static int zza(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        int i2 = 0;
        while (i2 < length && charSequence.charAt(i2) < '') {
            i2++;
        }
        int i3 = length;
        while (i2 < length) {
            StringBuilder stringBuilder;
            char charAt = charSequence.charAt(i2);
            if (charAt < 'ࠀ') {
                i3 += (127 - charAt) >>> 31;
                i2++;
            } else {
                int length2 = charSequence.length();
                while (i2 < length2) {
                    char charAt2 = charSequence.charAt(i2);
                    if (charAt2 < 'ࠀ') {
                        i += (127 - charAt2) >>> 31;
                    } else {
                        i += 2;
                        if ('?' <= charAt2 && charAt2 <= '?') {
                            if (Character.codePointAt(charSequence, i2) < C.DEFAULT_BUFFER_SEGMENT_SIZE) {
                                stringBuilder = new StringBuilder(39);
                                stringBuilder.append("Unpaired surrogate at index ");
                                stringBuilder.append(i2);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            }
                            i2++;
                        }
                    }
                    i2++;
                }
                i3 += i;
                if (i3 < length) {
                    return i3;
                }
                long j = ((long) i3) + 4294967296L;
                stringBuilder = new StringBuilder(54);
                stringBuilder.append("UTF-8 length does not fit in int: ");
                stringBuilder.append(j);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        if (i3 < length) {
            return i3;
        }
        long j2 = ((long) i3) + 4294967296L;
        stringBuilder = new StringBuilder(54);
        stringBuilder.append("UTF-8 length does not fit in int: ");
        stringBuilder.append(j2);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static void zza(CharSequence charSequence, ByteBuffer byteBuffer) {
        CharSequence charSequence2 = charSequence;
        ByteBuffer byteBuffer2 = byteBuffer;
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        char c = 'ࠀ';
        int i;
        char charAt;
        int i2;
        char charAt2;
        if (byteBuffer.hasArray()) {
            try {
                int i3;
                byte[] array = byteBuffer.array();
                int arrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
                int remaining = byteBuffer.remaining();
                int length = charSequence.length();
                remaining += arrayOffset;
                i = 0;
                while (i < length) {
                    i3 = i + arrayOffset;
                    if (i3 >= remaining) {
                        break;
                    }
                    char charAt3 = charSequence2.charAt(i);
                    if (charAt3 >= '') {
                        break;
                    }
                    array[i3] = (byte) charAt3;
                    i++;
                }
                if (i == length) {
                    arrayOffset += length;
                } else {
                    arrayOffset += i;
                    while (i < length) {
                        int i4;
                        charAt = charSequence2.charAt(i);
                        if (charAt >= '' || arrayOffset >= remaining) {
                            if (charAt < c && arrayOffset <= remaining - 2) {
                                i4 = arrayOffset + 1;
                                array[arrayOffset] = (byte) (960 | (charAt >>> 6));
                                i2 = i4 + 1;
                                array[i4] = (byte) ((charAt & 63) | 128);
                            } else if ((charAt < '?' || '?' < charAt) && arrayOffset <= remaining - 3) {
                                i4 = arrayOffset + 1;
                                array[arrayOffset] = (byte) (480 | (charAt >>> 12));
                                i2 = i4 + 1;
                                array[i4] = (byte) (((charAt >>> 6) & 63) | 128);
                                i4 = i2 + 1;
                                array[i2] = (byte) ((charAt & 63) | 128);
                            } else if (arrayOffset <= remaining - 4) {
                                i4 = i + 1;
                                if (i4 != charSequence.length()) {
                                    charAt2 = charSequence2.charAt(i4);
                                    if (Character.isSurrogatePair(charAt, charAt2)) {
                                        i = Character.toCodePoint(charAt, charAt2);
                                        i3 = arrayOffset + 1;
                                        array[arrayOffset] = (byte) (PsExtractor.VIDEO_STREAM_MASK | (i >>> 18));
                                        i2 = i3 + 1;
                                        array[i3] = (byte) (((i >>> 12) & 63) | 128);
                                        i3 = i2 + 1;
                                        array[i2] = (byte) (((i >>> 6) & 63) | 128);
                                        i2 = i3 + 1;
                                        array[i3] = (byte) ((i & 63) | 128);
                                        i = i4;
                                    } else {
                                        i = i4;
                                    }
                                }
                                i--;
                                StringBuilder stringBuilder = new StringBuilder(39);
                                stringBuilder.append("Unpaired surrogate at index ");
                                stringBuilder.append(i);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            } else {
                                StringBuilder stringBuilder2 = new StringBuilder(37);
                                stringBuilder2.append("Failed writing ");
                                stringBuilder2.append(charAt);
                                stringBuilder2.append(" at index ");
                                stringBuilder2.append(arrayOffset);
                                throw new ArrayIndexOutOfBoundsException(stringBuilder2.toString());
                            }
                            arrayOffset = i2;
                            i++;
                            c = 'ࠀ';
                        } else {
                            i4 = arrayOffset + 1;
                            array[arrayOffset] = (byte) charAt;
                        }
                        arrayOffset = i4;
                        i++;
                        c = 'ࠀ';
                    }
                }
                byteBuffer2.position(arrayOffset - byteBuffer.arrayOffset());
                return;
            } catch (Throwable e) {
                Throwable th = e;
                BufferOverflowException bufferOverflowException = new BufferOverflowException();
                bufferOverflowException.initCause(th);
                throw bufferOverflowException;
            }
        }
        int length2 = charSequence.length();
        i = 0;
        while (i < length2) {
            charAt = charSequence2.charAt(i);
            if (charAt < '') {
                byteBuffer2.put((byte) charAt);
            } else if (charAt < 'ࠀ') {
                byteBuffer2.put((byte) ((charAt >>> 6) | 960));
                byteBuffer2.put((byte) ((charAt & 63) | 128));
            } else {
                if (charAt >= '?') {
                    if ('?' >= charAt) {
                        i2 = i + 1;
                        if (i2 != charSequence.length()) {
                            charAt2 = charSequence2.charAt(i2);
                            if (Character.isSurrogatePair(charAt, charAt2)) {
                                i = Character.toCodePoint(charAt, charAt2);
                                byteBuffer2.put((byte) ((i >>> 18) | PsExtractor.VIDEO_STREAM_MASK));
                                byteBuffer2.put((byte) (((i >>> 12) & 63) | 128));
                                byteBuffer2.put((byte) (((i >>> 6) & 63) | 128));
                                byteBuffer2.put((byte) ((i & 63) | 128));
                                i = i2;
                                i++;
                            } else {
                                i = i2;
                            }
                        }
                        i--;
                        stringBuilder = new StringBuilder(39);
                        stringBuilder.append("Unpaired surrogate at index ");
                        stringBuilder.append(i);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                }
                byteBuffer2.put((byte) ((charAt >>> 12) | 480));
                byteBuffer2.put((byte) (((charAt >>> 6) & 63) | 128));
                byteBuffer2.put((byte) ((charAt & 63) | 128));
                i++;
            }
            i++;
        }
    }

    private final void zzao(long j) throws IOException {
        while ((j & -128) != 0) {
            zzar((((int) j) & 127) | 128);
            j >>>= 7;
        }
        zzar((int) j);
    }

    public static int zzap(long j) {
        return (j & -128) == 0 ? 1 : (j & -16384) == 0 ? 2 : (j & -2097152) == 0 ? 3 : (j & -268435456) == 0 ? 4 : (j & -34359738368L) == 0 ? 5 : (j & -4398046511104L) == 0 ? 6 : (j & -562949953421312L) == 0 ? 7 : (j & -72057594037927936L) == 0 ? 8 : (j & Long.MIN_VALUE) == 0 ? 9 : 10;
    }

    public static int zzaq(int i) {
        return i >= 0 ? zzau(i) : 10;
    }

    private final void zzar(int i) throws IOException {
        byte b = (byte) i;
        if (this.zzbzg.hasRemaining()) {
            this.zzbzg.put(b);
            return;
        }
        throw new zzabc(this.zzbzg.position(), this.zzbzg.limit());
    }

    public static int zzas(int i) {
        return zzau(i << 3);
    }

    public static int zzau(int i) {
        return (i & -128) == 0 ? 1 : (i & -16384) == 0 ? 2 : (-2097152 & i) == 0 ? 3 : (i & -268435456) == 0 ? 4 : 5;
    }

    public static int zzb(int i, zzabj com_google_android_gms_internal_measurement_zzabj) {
        i = zzas(i);
        int zzwg = com_google_android_gms_internal_measurement_zzabj.zzwg();
        return i + (zzau(zzwg) + zzwg);
    }

    public static zzabb zzb(byte[] bArr, int i, int i2) {
        return new zzabb(bArr, 0, i2);
    }

    public static int zzc(int i, long j) {
        return zzas(i) + zzap(j);
    }

    public static int zzd(int i, String str) {
        return zzas(i) + zzfp(str);
    }

    public static int zzf(int i, int i2) {
        return zzas(i) + zzaq(i2);
    }

    public static int zzfp(String str) {
        int zza = zza(str);
        return zzau(zza) + zza;
    }

    public static zzabb zzk(byte[] bArr) {
        return zzb(bArr, 0, bArr.length);
    }

    public final void zza(int i, double d) throws IOException {
        zzg(i, 1);
        long doubleToLongBits = Double.doubleToLongBits(d);
        if (this.zzbzg.remaining() < 8) {
            throw new zzabc(this.zzbzg.position(), this.zzbzg.limit());
        }
        this.zzbzg.putLong(doubleToLongBits);
    }

    public final void zza(int i, float f) throws IOException {
        zzg(i, 5);
        i = Float.floatToIntBits(f);
        if (this.zzbzg.remaining() < 4) {
            throw new zzabc(this.zzbzg.position(), this.zzbzg.limit());
        }
        this.zzbzg.putInt(i);
    }

    public final void zza(int i, long j) throws IOException {
        zzg(i, 0);
        zzao(j);
    }

    public final void zza(int i, zzabj com_google_android_gms_internal_measurement_zzabj) throws IOException {
        zzg(i, 2);
        zzb(com_google_android_gms_internal_measurement_zzabj);
    }

    public final void zza(int i, boolean z) throws IOException {
        zzg(i, 0);
        byte b = (byte) z;
        if (this.zzbzg.hasRemaining()) {
            this.zzbzg.put(b);
            return;
        }
        throw new zzabc(this.zzbzg.position(), this.zzbzg.limit());
    }

    public final void zzat(int i) throws IOException {
        while ((i & -128) != 0) {
            zzar((i & 127) | 128);
            i >>>= 7;
        }
        zzar(i);
    }

    public final void zzb(int i, long j) throws IOException {
        zzg(i, 0);
        zzao(j);
    }

    public final void zzb(zzabj com_google_android_gms_internal_measurement_zzabj) throws IOException {
        zzat(com_google_android_gms_internal_measurement_zzabj.zzwf());
        com_google_android_gms_internal_measurement_zzabj.zza(this);
    }

    public final void zzc(int i, String str) throws IOException {
        zzg(i, 2);
        try {
            i = zzau(str.length());
            if (i == zzau(str.length() * 3)) {
                int position = this.zzbzg.position();
                if (this.zzbzg.remaining() < i) {
                    throw new zzabc(position + i, this.zzbzg.limit());
                }
                this.zzbzg.position(position + i);
                zza((CharSequence) str, this.zzbzg);
                int position2 = this.zzbzg.position();
                this.zzbzg.position(position);
                zzat((position2 - position) - i);
                this.zzbzg.position(position2);
                return;
            }
            zzat(zza(str));
            zza((CharSequence) str, this.zzbzg);
        } catch (Throwable e) {
            zzabc com_google_android_gms_internal_measurement_zzabc = new zzabc(this.zzbzg.position(), this.zzbzg.limit());
            com_google_android_gms_internal_measurement_zzabc.initCause(e);
            throw com_google_android_gms_internal_measurement_zzabc;
        }
    }

    public final void zze(int i, int i2) throws IOException {
        zzg(i, 0);
        if (i2 >= 0) {
            zzat(i2);
        } else {
            zzao((long) i2);
        }
    }

    public final void zzg(int i, int i2) throws IOException {
        zzat((i << 3) | i2);
    }

    public final void zzl(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (this.zzbzg.remaining() >= length) {
            this.zzbzg.put(bArr, 0, length);
            return;
        }
        throw new zzabc(this.zzbzg.position(), this.zzbzg.limit());
    }

    public final void zzvy() {
        if (this.zzbzg.remaining() != 0) {
            throw new IllegalStateException(String.format("Did not write as much data as expected, %s bytes remaining.", new Object[]{Integer.valueOf(this.zzbzg.remaining())}));
        }
    }
}
