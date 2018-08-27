package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.Arrays;
import org.telegram.tgnet.ConnectionsManager;

final class zzuq extends zzuo {
    private final byte[] buffer;
    private int limit;
    private int pos;
    private final boolean zzbum;
    private int zzbun;
    private int zzbuo;
    private int zzbup;
    private int zzbuq;

    private zzuq(byte[] bArr, int i, int i2, boolean z) {
        super();
        this.zzbuq = ConnectionsManager.DEFAULT_DATACENTER_ID;
        this.buffer = bArr;
        this.limit = i + i2;
        this.pos = i;
        this.zzbuo = this.pos;
        this.zzbum = z;
    }

    public final int zzug() throws IOException {
        if (zzuw()) {
            this.zzbup = 0;
            return 0;
        }
        this.zzbup = zzuy();
        if ((this.zzbup >>> 3) != 0) {
            return this.zzbup;
        }
        throw new zzvt("Protocol message contained an invalid tag (zero).");
    }

    public final void zzan(int i) throws zzvt {
        if (this.zzbup != i) {
            throw zzvt.zzwn();
        }
    }

    public final boolean zzao(int i) throws IOException {
        int i2 = 0;
        switch (i & 7) {
            case 0:
                if (this.limit - this.pos >= 10) {
                    while (i2 < 10) {
                        byte[] bArr = this.buffer;
                        int i3 = this.pos;
                        this.pos = i3 + 1;
                        if (bArr[i3] >= (byte) 0) {
                            return true;
                        }
                        i2++;
                    }
                    throw zzvt.zzwm();
                }
                while (i2 < 10) {
                    if (zzvd() >= (byte) 0) {
                        return true;
                    }
                    i2++;
                }
                throw zzvt.zzwm();
            case 1:
                zzas(8);
                return true;
            case 2:
                zzas(zzuy());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzas(4);
                return true;
            default:
                throw zzvt.zzwo();
        }
        do {
            i2 = zzug();
            if (i2 != 0) {
            }
            zzan(((i >>> 3) << 3) | 4);
            return true;
        } while (zzao(i2));
        zzan(((i >>> 3) << 3) | 4);
        return true;
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(zzvb());
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(zzva());
    }

    public final long zzuh() throws IOException {
        return zzuz();
    }

    public final long zzui() throws IOException {
        return zzuz();
    }

    public final int zzuj() throws IOException {
        return zzuy();
    }

    public final long zzuk() throws IOException {
        return zzvb();
    }

    public final int zzul() throws IOException {
        return zzva();
    }

    public final boolean zzum() throws IOException {
        return zzuz() != 0;
    }

    public final String readString() throws IOException {
        int zzuy = zzuy();
        if (zzuy > 0 && zzuy <= this.limit - this.pos) {
            String str = new String(this.buffer, this.pos, zzuy, zzvo.UTF_8);
            this.pos = zzuy + this.pos;
            return str;
        } else if (zzuy == 0) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        } else {
            if (zzuy < 0) {
                throw zzvt.zzwl();
            }
            throw zzvt.zzwk();
        }
    }

    public final String zzun() throws IOException {
        int zzuy = zzuy();
        if (zzuy > 0 && zzuy <= this.limit - this.pos) {
            String zzh = zzyj.zzh(this.buffer, this.pos, zzuy);
            this.pos = zzuy + this.pos;
            return zzh;
        } else if (zzuy == 0) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        } else {
            if (zzuy <= 0) {
                throw zzvt.zzwl();
            }
            throw zzvt.zzwk();
        }
    }

    public final <T extends zzwt> T zza(zzxd<T> com_google_android_gms_internal_measurement_zzxd_T, zzuz com_google_android_gms_internal_measurement_zzuz) throws IOException {
        int zzuy = zzuy();
        if (this.zzbuh >= this.zzbui) {
            throw zzvt.zzwp();
        }
        int zzaq = zzaq(zzuy);
        this.zzbuh++;
        zzwt com_google_android_gms_internal_measurement_zzwt = (zzwt) com_google_android_gms_internal_measurement_zzxd_T.zza(this, com_google_android_gms_internal_measurement_zzuz);
        zzan(0);
        this.zzbuh--;
        zzar(zzaq);
        return com_google_android_gms_internal_measurement_zzwt;
    }

    public final zzud zzuo() throws IOException {
        int zzuy = zzuy();
        if (zzuy > 0 && zzuy <= this.limit - this.pos) {
            zzud zzb = zzud.zzb(this.buffer, this.pos, zzuy);
            this.pos = zzuy + this.pos;
            return zzb;
        } else if (zzuy == 0) {
            return zzud.zzbtz;
        } else {
            byte[] copyOfRange;
            if (zzuy > 0 && zzuy <= this.limit - this.pos) {
                int i = this.pos;
                this.pos = zzuy + this.pos;
                copyOfRange = Arrays.copyOfRange(this.buffer, i, this.pos);
            } else if (zzuy > 0) {
                throw zzvt.zzwk();
            } else if (zzuy == 0) {
                copyOfRange = zzvo.zzbzj;
            } else {
                throw zzvt.zzwl();
            }
            return zzud.zzi(copyOfRange);
        }
    }

    public final int zzup() throws IOException {
        return zzuy();
    }

    public final int zzuq() throws IOException {
        return zzuy();
    }

    public final int zzur() throws IOException {
        return zzva();
    }

    public final long zzus() throws IOException {
        return zzvb();
    }

    public final int zzut() throws IOException {
        int zzuy = zzuy();
        return (-(zzuy & 1)) ^ (zzuy >>> 1);
    }

    public final long zzuu() throws IOException {
        long zzuz = zzuz();
        return (-(zzuz & 1)) ^ (zzuz >>> 1);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int zzuy() throws java.io.IOException {
        /*
        r5 = this;
        r0 = r5.pos;
        r1 = r5.limit;
        if (r1 == r0) goto L_0x006c;
    L_0x0006:
        r3 = r5.buffer;
        r2 = r0 + 1;
        r0 = r3[r0];
        if (r0 < 0) goto L_0x0011;
    L_0x000e:
        r5.pos = r2;
    L_0x0010:
        return r0;
    L_0x0011:
        r1 = r5.limit;
        r1 = r1 - r2;
        r4 = 9;
        if (r1 < r4) goto L_0x006c;
    L_0x0018:
        r1 = r2 + 1;
        r2 = r3[r2];
        r2 = r2 << 7;
        r0 = r0 ^ r2;
        if (r0 >= 0) goto L_0x0026;
    L_0x0021:
        r0 = r0 ^ -128;
    L_0x0023:
        r5.pos = r1;
        goto L_0x0010;
    L_0x0026:
        r2 = r1 + 1;
        r1 = r3[r1];
        r1 = r1 << 14;
        r0 = r0 ^ r1;
        if (r0 < 0) goto L_0x0033;
    L_0x002f:
        r0 = r0 ^ 16256;
        r1 = r2;
        goto L_0x0023;
    L_0x0033:
        r1 = r2 + 1;
        r2 = r3[r2];
        r2 = r2 << 21;
        r0 = r0 ^ r2;
        if (r0 >= 0) goto L_0x0041;
    L_0x003c:
        r2 = -2080896; // 0xffffffffffe03f80 float:NaN double:NaN;
        r0 = r0 ^ r2;
        goto L_0x0023;
    L_0x0041:
        r2 = r1 + 1;
        r1 = r3[r1];
        r4 = r1 << 28;
        r0 = r0 ^ r4;
        r4 = 266354560; // 0xfe03f80 float:2.2112565E-29 double:1.315966377E-315;
        r0 = r0 ^ r4;
        if (r1 >= 0) goto L_0x0072;
    L_0x004e:
        r1 = r2 + 1;
        r2 = r3[r2];
        if (r2 >= 0) goto L_0x0023;
    L_0x0054:
        r2 = r1 + 1;
        r1 = r3[r1];
        if (r1 >= 0) goto L_0x0072;
    L_0x005a:
        r1 = r2 + 1;
        r2 = r3[r2];
        if (r2 >= 0) goto L_0x0023;
    L_0x0060:
        r2 = r1 + 1;
        r1 = r3[r1];
        if (r1 >= 0) goto L_0x0072;
    L_0x0066:
        r1 = r2 + 1;
        r2 = r3[r2];
        if (r2 >= 0) goto L_0x0023;
    L_0x006c:
        r0 = r5.zzuv();
        r0 = (int) r0;
        goto L_0x0010;
    L_0x0072:
        r1 = r2;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzuq.zzuy():int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final long zzuz() throws java.io.IOException {
        /*
        r10 = this;
        r8 = 0;
        r0 = r10.pos;
        r1 = r10.limit;
        if (r1 == r0) goto L_0x00b4;
    L_0x0008:
        r4 = r10.buffer;
        r1 = r0 + 1;
        r0 = r4[r0];
        if (r0 < 0) goto L_0x0014;
    L_0x0010:
        r10.pos = r1;
        r0 = (long) r0;
    L_0x0013:
        return r0;
    L_0x0014:
        r2 = r10.limit;
        r2 = r2 - r1;
        r3 = 9;
        if (r2 < r3) goto L_0x00b4;
    L_0x001b:
        r2 = r1 + 1;
        r1 = r4[r1];
        r1 = r1 << 7;
        r0 = r0 ^ r1;
        if (r0 >= 0) goto L_0x002a;
    L_0x0024:
        r0 = r0 ^ -128;
        r0 = (long) r0;
    L_0x0027:
        r10.pos = r2;
        goto L_0x0013;
    L_0x002a:
        r3 = r2 + 1;
        r1 = r4[r2];
        r1 = r1 << 14;
        r0 = r0 ^ r1;
        if (r0 < 0) goto L_0x0038;
    L_0x0033:
        r0 = r0 ^ 16256;
        r0 = (long) r0;
        r2 = r3;
        goto L_0x0027;
    L_0x0038:
        r2 = r3 + 1;
        r1 = r4[r3];
        r1 = r1 << 21;
        r0 = r0 ^ r1;
        if (r0 >= 0) goto L_0x0047;
    L_0x0041:
        r1 = -2080896; // 0xffffffffffe03f80 float:NaN double:NaN;
        r0 = r0 ^ r1;
        r0 = (long) r0;
        goto L_0x0027;
    L_0x0047:
        r0 = (long) r0;
        r3 = r2 + 1;
        r2 = r4[r2];
        r6 = (long) r2;
        r2 = 28;
        r6 = r6 << r2;
        r0 = r0 ^ r6;
        r2 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
        if (r2 < 0) goto L_0x005b;
    L_0x0055:
        r4 = 266354560; // 0xfe03f80 float:2.2112565E-29 double:1.315966377E-315;
        r0 = r0 ^ r4;
        r2 = r3;
        goto L_0x0027;
    L_0x005b:
        r2 = r3 + 1;
        r3 = r4[r3];
        r6 = (long) r3;
        r3 = 35;
        r6 = r6 << r3;
        r0 = r0 ^ r6;
        r3 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
        if (r3 >= 0) goto L_0x006f;
    L_0x0068:
        r4 = -34093383808; // 0xfffffff80fe03f80 float:2.2112565E-29 double:NaN;
        r0 = r0 ^ r4;
        goto L_0x0027;
    L_0x006f:
        r3 = r2 + 1;
        r2 = r4[r2];
        r6 = (long) r2;
        r2 = 42;
        r6 = r6 << r2;
        r0 = r0 ^ r6;
        r2 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
        if (r2 < 0) goto L_0x0084;
    L_0x007c:
        r4 = 4363953127296; // 0x3f80fe03f80 float:2.2112565E-29 double:2.1560793202584E-311;
        r0 = r0 ^ r4;
        r2 = r3;
        goto L_0x0027;
    L_0x0084:
        r2 = r3 + 1;
        r3 = r4[r3];
        r6 = (long) r3;
        r3 = 49;
        r6 = r6 << r3;
        r0 = r0 ^ r6;
        r3 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
        if (r3 >= 0) goto L_0x0098;
    L_0x0091:
        r4 = -558586000294016; // 0xfffe03f80fe03f80 float:2.2112565E-29 double:NaN;
        r0 = r0 ^ r4;
        goto L_0x0027;
    L_0x0098:
        r3 = r2 + 1;
        r2 = r4[r2];
        r6 = (long) r2;
        r2 = 56;
        r6 = r6 << r2;
        r0 = r0 ^ r6;
        r6 = 71499008037633920; // 0xfe03f80fe03f80 float:2.2112565E-29 double:6.838959413692434E-304;
        r0 = r0 ^ r6;
        r2 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
        if (r2 >= 0) goto L_0x00ba;
    L_0x00ab:
        r2 = r3 + 1;
        r3 = r4[r3];
        r4 = (long) r3;
        r3 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r3 >= 0) goto L_0x0027;
    L_0x00b4:
        r0 = r10.zzuv();
        goto L_0x0013;
    L_0x00ba:
        r2 = r3;
        goto L_0x0027;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzuq.zzuz():long");
    }

    final long zzuv() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte zzvd = zzvd();
            j |= ((long) (zzvd & 127)) << i;
            if ((zzvd & 128) == 0) {
                return j;
            }
        }
        throw zzvt.zzwm();
    }

    private final int zzva() throws IOException {
        int i = this.pos;
        if (this.limit - i < 4) {
            throw zzvt.zzwk();
        }
        byte[] bArr = this.buffer;
        this.pos = i + 4;
        return ((bArr[i + 3] & 255) << 24) | (((bArr[i] & 255) | ((bArr[i + 1] & 255) << 8)) | ((bArr[i + 2] & 255) << 16));
    }

    private final long zzvb() throws IOException {
        int i = this.pos;
        if (this.limit - i < 8) {
            throw zzvt.zzwk();
        }
        byte[] bArr = this.buffer;
        this.pos = i + 8;
        return ((((long) bArr[i + 7]) & 255) << 56) | (((((((((long) bArr[i]) & 255) | ((((long) bArr[i + 1]) & 255) << 8)) | ((((long) bArr[i + 2]) & 255) << 16)) | ((((long) bArr[i + 3]) & 255) << 24)) | ((((long) bArr[i + 4]) & 255) << 32)) | ((((long) bArr[i + 5]) & 255) << 40)) | ((((long) bArr[i + 6]) & 255) << 48));
    }

    public final int zzaq(int i) throws zzvt {
        if (i < 0) {
            throw zzvt.zzwl();
        }
        int zzux = zzux() + i;
        int i2 = this.zzbuq;
        if (zzux > i2) {
            throw zzvt.zzwk();
        }
        this.zzbuq = zzux;
        zzvc();
        return i2;
    }

    private final void zzvc() {
        this.limit += this.zzbun;
        int i = this.limit - this.zzbuo;
        if (i > this.zzbuq) {
            this.zzbun = i - this.zzbuq;
            this.limit -= this.zzbun;
            return;
        }
        this.zzbun = 0;
    }

    public final void zzar(int i) {
        this.zzbuq = i;
        zzvc();
    }

    public final boolean zzuw() throws IOException {
        return this.pos == this.limit;
    }

    public final int zzux() {
        return this.pos - this.zzbuo;
    }

    private final byte zzvd() throws IOException {
        if (this.pos == this.limit) {
            throw zzvt.zzwk();
        }
        byte[] bArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i];
    }

    public final void zzas(int i) throws IOException {
        if (i >= 0 && i <= this.limit - this.pos) {
            this.pos += i;
        } else if (i < 0) {
            throw zzvt.zzwl();
        } else {
            throw zzvt.zzwk();
        }
    }
}
