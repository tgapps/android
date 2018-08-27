package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.tgnet.ConnectionsManager;

public final class zzyx {
    private final byte[] buffer;
    private int zzbuh;
    private int zzbui = 64;
    private int zzbuj = ConnectionsManager.FileTypeFile;
    private int zzbun;
    private int zzbup;
    private int zzbuq = ConnectionsManager.DEFAULT_DATACENTER_ID;
    private final int zzcev;
    private final int zzcew;
    private int zzcex;
    private int zzcey;
    private zzuo zzcez;

    public static zzyx zzn(byte[] bArr) {
        return zzj(bArr, 0, bArr.length);
    }

    public static zzyx zzj(byte[] bArr, int i, int i2) {
        return new zzyx(bArr, 0, i2);
    }

    public final int zzug() throws IOException {
        if (this.zzcey == this.zzcex) {
            this.zzbup = 0;
            return 0;
        }
        this.zzbup = zzuy();
        if (this.zzbup != 0) {
            return this.zzbup;
        }
        throw new zzzf("Protocol message contained an invalid tag (zero).");
    }

    public final void zzan(int i) throws zzzf {
        if (this.zzbup != i) {
            throw new zzzf("Protocol message end-group tag did not match expected tag.");
        }
    }

    public final boolean zzao(int i) throws IOException {
        switch (i & 7) {
            case 0:
                zzuy();
                return true;
            case 1:
                zzvb();
                return true;
            case 2:
                zzas(zzuy());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzva();
                return true;
            default:
                throw new zzzf("Protocol message tag had invalid wire type.");
        }
        int zzug;
        do {
            zzug = zzug();
            if (zzug != 0) {
            }
            zzan(((i >>> 3) << 3) | 4);
            return true;
        } while (zzao(zzug));
        zzan(((i >>> 3) << 3) | 4);
        return true;
    }

    public final boolean zzum() throws IOException {
        return zzuy() != 0;
    }

    public final String readString() throws IOException {
        int zzuy = zzuy();
        if (zzuy < 0) {
            throw zzzf.zzyx();
        } else if (zzuy > this.zzcex - this.zzcey) {
            throw zzzf.zzyw();
        } else {
            String str = new String(this.buffer, this.zzcey, zzuy, zzze.UTF_8);
            this.zzcey = zzuy + this.zzcey;
            return str;
        }
    }

    public final void zza(zzzg com_google_android_gms_internal_measurement_zzzg, int i) throws IOException {
        if (this.zzbuh >= this.zzbui) {
            throw zzzf.zzyz();
        }
        this.zzbuh++;
        com_google_android_gms_internal_measurement_zzzg.zza(this);
        zzan((i << 3) | 4);
        this.zzbuh--;
    }

    public final void zza(zzzg com_google_android_gms_internal_measurement_zzzg) throws IOException {
        int zzuy = zzuy();
        if (this.zzbuh >= this.zzbui) {
            throw zzzf.zzyz();
        }
        zzuy = zzaq(zzuy);
        this.zzbuh++;
        com_google_android_gms_internal_measurement_zzzg.zza(this);
        zzan(0);
        this.zzbuh--;
        zzar(zzuy);
    }

    public final int zzuy() throws IOException {
        byte zzvd = zzvd();
        if (zzvd >= (byte) 0) {
            return zzvd;
        }
        int i = zzvd & 127;
        byte zzvd2 = zzvd();
        if (zzvd2 >= (byte) 0) {
            return i | (zzvd2 << 7);
        }
        i |= (zzvd2 & 127) << 7;
        zzvd2 = zzvd();
        if (zzvd2 >= (byte) 0) {
            return i | (zzvd2 << 14);
        }
        i |= (zzvd2 & 127) << 14;
        zzvd2 = zzvd();
        if (zzvd2 >= (byte) 0) {
            return i | (zzvd2 << 21);
        }
        i |= (zzvd2 & 127) << 21;
        zzvd2 = zzvd();
        i |= zzvd2 << 28;
        if (zzvd2 >= (byte) 0) {
            return i;
        }
        for (int i2 = 0; i2 < 5; i2++) {
            if (zzvd() >= (byte) 0) {
                return i;
            }
        }
        throw zzzf.zzyy();
    }

    public final long zzuz() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte zzvd = zzvd();
            j |= ((long) (zzvd & 127)) << i;
            if ((zzvd & 128) == 0) {
                return j;
            }
        }
        throw zzzf.zzyy();
    }

    public final int zzva() throws IOException {
        return (((zzvd() & 255) | ((zzvd() & 255) << 8)) | ((zzvd() & 255) << 16)) | ((zzvd() & 255) << 24);
    }

    public final long zzvb() throws IOException {
        byte zzvd = zzvd();
        byte zzvd2 = zzvd();
        return ((((((((((long) zzvd2) & 255) << 8) | (((long) zzvd) & 255)) | ((((long) zzvd()) & 255) << 16)) | ((((long) zzvd()) & 255) << 24)) | ((((long) zzvd()) & 255) << 32)) | ((((long) zzvd()) & 255) << 40)) | ((((long) zzvd()) & 255) << 48)) | ((((long) zzvd()) & 255) << 56);
    }

    private zzyx(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzcev = i;
        int i3 = i + i2;
        this.zzcex = i3;
        this.zzcew = i3;
        this.zzcey = i;
    }

    public final <T extends zzvm<T, ?>> T zza(zzxd<T> com_google_android_gms_internal_measurement_zzxd_T) throws IOException {
        try {
            if (this.zzcez == null) {
                this.zzcez = zzuo.zzd(this.buffer, this.zzcev, this.zzcew);
            }
            int zzux = this.zzcez.zzux();
            int i = this.zzcey - this.zzcev;
            if (zzux > i) {
                throw new IOException(String.format("CodedInputStream read ahead of CodedInputByteBufferNano: %s > %s", new Object[]{Integer.valueOf(zzux), Integer.valueOf(i)}));
            }
            this.zzcez.zzas(i - zzux);
            this.zzcez.zzap(this.zzbui - this.zzbuh);
            zzvm com_google_android_gms_internal_measurement_zzvm = (zzvm) this.zzcez.zza(com_google_android_gms_internal_measurement_zzxd_T, zzuz.zzvp());
            zzao(this.zzbup);
            return com_google_android_gms_internal_measurement_zzvm;
        } catch (Exception e) {
            throw new zzzf(TtmlNode.ANONYMOUS_REGION_ID, e);
        }
    }

    public final int zzaq(int i) throws zzzf {
        if (i < 0) {
            throw zzzf.zzyx();
        }
        int i2 = this.zzcey + i;
        int i3 = this.zzbuq;
        if (i2 > i3) {
            throw zzzf.zzyw();
        }
        this.zzbuq = i2;
        zzvc();
        return i3;
    }

    private final void zzvc() {
        this.zzcex += this.zzbun;
        int i = this.zzcex;
        if (i > this.zzbuq) {
            this.zzbun = i - this.zzbuq;
            this.zzcex -= this.zzbun;
            return;
        }
        this.zzbun = 0;
    }

    public final void zzar(int i) {
        this.zzbuq = i;
        zzvc();
    }

    public final int zzyr() {
        if (this.zzbuq == ConnectionsManager.DEFAULT_DATACENTER_ID) {
            return -1;
        }
        return this.zzbuq - this.zzcey;
    }

    public final int getPosition() {
        return this.zzcey - this.zzcev;
    }

    public final byte[] zzs(int i, int i2) {
        if (i2 == 0) {
            return zzzj.zzcfx;
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.buffer, this.zzcev + i, bArr, 0, i2);
        return bArr;
    }

    public final void zzby(int i) {
        zzt(i, this.zzbup);
    }

    final void zzt(int i, int i2) {
        if (i > this.zzcey - this.zzcev) {
            throw new IllegalArgumentException("Position " + i + " is beyond current " + (this.zzcey - this.zzcev));
        } else if (i < 0) {
            throw new IllegalArgumentException("Bad position " + i);
        } else {
            this.zzcey = this.zzcev + i;
            this.zzbup = i2;
        }
    }

    private final byte zzvd() throws IOException {
        if (this.zzcey == this.zzcex) {
            throw zzzf.zzyw();
        }
        byte[] bArr = this.buffer;
        int i = this.zzcey;
        this.zzcey = i + 1;
        return bArr[i];
    }

    private final void zzas(int i) throws IOException {
        if (i < 0) {
            throw zzzf.zzyx();
        } else if (this.zzcey + i > this.zzbuq) {
            zzas(this.zzbuq - this.zzcey);
            throw zzzf.zzyw();
        } else if (i <= this.zzcex - this.zzcey) {
            this.zzcey += i;
        } else {
            throw zzzf.zzyw();
        }
    }
}
