package com.google.android.gms.internal.measurement;

import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import java.nio.ByteBuffer;

final class zzym extends zzyl {
    zzym() {
    }

    final int zzb(int i, byte[] bArr, int i2, int i3) {
        int i4 = i2;
        while (i4 < i3 && bArr[i4] >= (byte) 0) {
            i4++;
        }
        if (i4 >= i3) {
            return 0;
        }
        while (i4 < i3) {
            int i5 = i4 + 1;
            i4 = bArr[i4];
            if (i4 >= 0) {
                i4 = i5;
            } else if (i4 < -32) {
                if (i5 >= i3) {
                    return i4;
                }
                if (i4 >= -62) {
                    i4 = i5 + 1;
                    if (bArr[i5] > (byte) -65) {
                    }
                }
                return -1;
            } else if (i4 < -16) {
                if (i5 >= i3 - 1) {
                    return zzyj.zzg(bArr, i5, i3);
                }
                r4 = i5 + 1;
                r3 = bArr[i5];
                if (r3 <= (byte) -65 && ((i4 != -32 || r3 >= (byte) -96) && (i4 != -19 || r3 < (byte) -96))) {
                    i4 = r4 + 1;
                    if (bArr[r4] > (byte) -65) {
                    }
                }
                return -1;
            } else if (i5 >= i3 - 2) {
                return zzyj.zzg(bArr, i5, i3);
            } else {
                r4 = i5 + 1;
                r3 = bArr[i5];
                if (r3 <= (byte) -65 && (((i4 << 28) + (r3 + 112)) >> 30) == 0) {
                    i5 = r4 + 1;
                    if (bArr[r4] <= (byte) -65) {
                        i4 = i5 + 1;
                        if (bArr[i5] > (byte) -65) {
                        }
                    }
                }
                return -1;
            }
        }
        return 0;
    }

    final String zzh(byte[] bArr, int i, int i2) throws zzvt {
        if (((i | i2) | ((bArr.length - i) - i2)) < 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", new Object[]{Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)}));
        }
        int i3 = i + i2;
        char[] cArr = new char[i2];
        int i4 = 0;
        int i5 = i;
        while (i5 < i3) {
            byte b = bArr[i5];
            if (!zzyk.zzd(b)) {
                break;
            }
            i5++;
            int i6 = i4 + 1;
            zzyk.zza(b, cArr, i4);
            i4 = i6;
        }
        int i7 = i5;
        while (i7 < i3) {
            i5 = i7 + 1;
            byte b2 = bArr[i7];
            byte b3;
            if (zzyk.zzd(b2)) {
                i7 = i4 + 1;
                zzyk.zza(b2, cArr, i4);
                i6 = i7;
                while (i5 < i3) {
                    b3 = bArr[i5];
                    if (!zzyk.zzd(b3)) {
                        break;
                    }
                    i5++;
                    i7 = i6 + 1;
                    zzyk.zza(b3, cArr, i6);
                    i6 = i7;
                }
            } else if (zzyk.zze(b2)) {
                if (i5 >= i3) {
                    throw zzvt.zzwr();
                }
                i7 = i5 + 1;
                b3 = bArr[i5];
                i5 = i4 + 1;
                zzyk.zza(b2, b3, cArr, i4);
                i4 = i5;
            } else if (zzyk.zzf(b2)) {
                if (i5 >= i3 - 1) {
                    throw zzvt.zzwr();
                }
                r3 = i5 + 1;
                i7 = r3 + 1;
                i5 = i4 + 1;
                zzyk.zza(b2, bArr[i5], bArr[r3], cArr, i4);
                i4 = i5;
            } else if (i5 >= i3 - 2) {
                throw zzvt.zzwr();
            } else {
                i7 = i5 + 1;
                r3 = i7 + 1;
                int i8 = r3 + 1;
                int i9 = i4 + 1;
                zzyk.zza(b2, bArr[i5], bArr[i7], bArr[r3], cArr, i4);
                i6 = i9 + 1;
                i5 = i8;
            }
            i4 = i6;
            i7 = i5;
        }
        return new String(cArr, 0, i4);
    }

    final int zzb(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int length = charSequence.length();
        int i3 = 0;
        int i4 = i + i2;
        while (i3 < length && i3 + i < i4) {
            char charAt = charSequence.charAt(i3);
            if (charAt >= '') {
                break;
            }
            bArr[i + i3] = (byte) charAt;
            i3++;
        }
        if (i3 == length) {
            return i + length;
        }
        int i5 = i + i3;
        while (i3 < length) {
            int i6;
            char charAt2 = charSequence.charAt(i3);
            if (charAt2 < '' && i5 < i4) {
                i6 = i5 + 1;
                bArr[i5] = (byte) charAt2;
            } else if (charAt2 < 'ࠀ' && i5 <= i4 - 2) {
                r6 = i5 + 1;
                bArr[i5] = (byte) ((charAt2 >>> 6) | 960);
                i6 = r6 + 1;
                bArr[r6] = (byte) ((charAt2 & 63) | 128);
            } else if ((charAt2 < '?' || '?' < charAt2) && i5 <= i4 - 3) {
                i6 = i5 + 1;
                bArr[i5] = (byte) ((charAt2 >>> 12) | 480);
                i5 = i6 + 1;
                bArr[i6] = (byte) (((charAt2 >>> 6) & 63) | 128);
                i6 = i5 + 1;
                bArr[i5] = (byte) ((charAt2 & 63) | 128);
            } else if (i5 <= i4 - 4) {
                if (i3 + 1 != charSequence.length()) {
                    i3++;
                    charAt = charSequence.charAt(i3);
                    if (Character.isSurrogatePair(charAt2, charAt)) {
                        int toCodePoint = Character.toCodePoint(charAt2, charAt);
                        i6 = i5 + 1;
                        bArr[i5] = (byte) ((toCodePoint >>> 18) | PsExtractor.VIDEO_STREAM_MASK);
                        i5 = i6 + 1;
                        bArr[i6] = (byte) (((toCodePoint >>> 12) & 63) | 128);
                        r6 = i5 + 1;
                        bArr[i5] = (byte) (((toCodePoint >>> 6) & 63) | 128);
                        i6 = r6 + 1;
                        bArr[r6] = (byte) ((toCodePoint & 63) | 128);
                    }
                }
                throw new zzyn(i3 - 1, length);
            } else if ('?' > charAt2 || charAt2 > '?' || (i3 + 1 != charSequence.length() && Character.isSurrogatePair(charAt2, charSequence.charAt(i3 + 1)))) {
                throw new ArrayIndexOutOfBoundsException("Failed writing " + charAt2 + " at index " + i5);
            } else {
                throw new zzyn(i3, length);
            }
            i3++;
            i5 = i6;
        }
        return i5;
    }

    final void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        zzyl.zzc(charSequence, byteBuffer);
    }
}
