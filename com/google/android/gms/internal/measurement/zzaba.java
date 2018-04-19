package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.tgnet.ConnectionsManager;

public final class zzaba {
    private final byte[] buffer;
    private int zzbtp = 64;
    private int zzbtq = ConnectionsManager.FileTypeFile;
    private int zzbtu;
    private int zzbtw = ConnectionsManager.DEFAULT_DATACENTER_ID;
    private final int zzbza;
    private final int zzbzb;
    private int zzbzc;
    private int zzbzd;
    private int zzbze;
    private int zzbzf;

    private zzaba(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzbza = i;
        int i3 = i + i2;
        this.zzbzc = i3;
        this.zzbzb = i3;
        this.zzbzd = i;
    }

    public static zzaba zza(byte[] bArr, int i, int i2) {
        return new zzaba(bArr, 0, i2);
    }

    private final void zzap(int i) throws IOException {
        if (i < 0) {
            throw zzabi.zzwc();
        } else if (this.zzbzd + i > this.zzbtw) {
            zzap(this.zzbtw - this.zzbzd);
            throw zzabi.zzwb();
        } else if (i <= this.zzbzc - this.zzbzd) {
            this.zzbzd += i;
        } else {
            throw zzabi.zzwb();
        }
    }

    public static zzaba zzj(byte[] bArr) {
        return zza(bArr, 0, bArr.length);
    }

    private final void zzts() {
        this.zzbzc += this.zzbtu;
        int i = this.zzbzc;
        if (i > this.zzbtw) {
            this.zzbtu = i - this.zzbtw;
            this.zzbzc -= this.zzbtu;
            return;
        }
        this.zzbtu = 0;
    }

    private final byte zzvx() throws IOException {
        if (this.zzbzd == this.zzbzc) {
            throw zzabi.zzwb();
        }
        byte[] bArr = this.buffer;
        int i = this.zzbzd;
        this.zzbzd = i + 1;
        return bArr[i];
    }

    public final int getPosition() {
        return this.zzbzd - this.zzbza;
    }

    public final String readString() throws IOException {
        int zzvs = zzvs();
        if (zzvs < 0) {
            throw zzabi.zzwc();
        } else if (zzvs > this.zzbzc - this.zzbzd) {
            throw zzabi.zzwb();
        } else {
            String str = new String(this.buffer, this.zzbzd, zzvs, zzabh.UTF_8);
            this.zzbzd = zzvs + this.zzbzd;
            return str;
        }
    }

    public final void zza(zzabj com_google_android_gms_internal_measurement_zzabj) throws IOException {
        int zzvs = zzvs();
        if (this.zzbzf >= this.zzbtp) {
            throw zzabi.zzwe();
        }
        zzvs = zzah(zzvs);
        this.zzbzf++;
        com_google_android_gms_internal_measurement_zzabj.zzb(this);
        zzal(0);
        this.zzbzf--;
        zzan(zzvs);
    }

    public final void zza(zzabj com_google_android_gms_internal_measurement_zzabj, int i) throws IOException {
        if (this.zzbzf >= this.zzbtp) {
            throw zzabi.zzwe();
        }
        this.zzbzf++;
        com_google_android_gms_internal_measurement_zzabj.zzb(this);
        zzal((i << 3) | 4);
        this.zzbzf--;
    }

    public final int zzah(int i) throws zzabi {
        if (i < 0) {
            throw zzabi.zzwc();
        }
        int i2 = this.zzbzd + i;
        int i3 = this.zzbtw;
        if (i2 > i3) {
            throw zzabi.zzwb();
        }
        this.zzbtw = i2;
        zzts();
        return i3;
    }

    public final void zzal(int i) throws zzabi {
        if (this.zzbze != i) {
            throw new zzabi("Protocol message end-group tag did not match expected tag.");
        }
    }

    public final boolean zzam(int i) throws IOException {
        switch (i & 7) {
            case 0:
                zzvs();
                return true;
            case 1:
                zzvv();
                return true;
            case 2:
                zzap(zzvs());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzvu();
                return true;
            default:
                throw new zzabi("Protocol message tag had invalid wire type.");
        }
        int zzvo;
        do {
            zzvo = zzvo();
            if (zzvo != 0) {
            }
            zzal(((i >>> 3) << 3) | 4);
            return true;
        } while (zzam(zzvo));
        zzal(((i >>> 3) << 3) | 4);
        return true;
    }

    public final void zzan(int i) {
        this.zzbtw = i;
        zzts();
    }

    public final void zzao(int i) {
        zzd(i, this.zzbze);
    }

    public final byte[] zzc(int i, int i2) {
        if (i2 == 0) {
            return zzabm.zzcae;
        }
        Object obj = new byte[i2];
        System.arraycopy(this.buffer, this.zzbza + i, obj, 0, i2);
        return obj;
    }

    final void zzd(int i, int i2) {
        if (i > this.zzbzd - this.zzbza) {
            throw new IllegalArgumentException("Position " + i + " is beyond current " + (this.zzbzd - this.zzbza));
        } else if (i < 0) {
            throw new IllegalArgumentException("Bad position " + i);
        } else {
            this.zzbzd = this.zzbza + i;
            this.zzbze = i2;
        }
    }

    public final int zzvo() throws IOException {
        if (this.zzbzd == this.zzbzc) {
            this.zzbze = 0;
            return 0;
        }
        this.zzbze = zzvs();
        if (this.zzbze != 0) {
            return this.zzbze;
        }
        throw new zzabi("Protocol message contained an invalid tag (zero).");
    }

    public final long zzvp() throws IOException {
        return zzvt();
    }

    public final int zzvq() throws IOException {
        return zzvs();
    }

    public final boolean zzvr() throws IOException {
        return zzvs() != 0;
    }

    public final int zzvs() throws IOException {
        byte zzvx = zzvx();
        if (zzvx >= (byte) 0) {
            return zzvx;
        }
        int i = zzvx & 127;
        byte zzvx2 = zzvx();
        if (zzvx2 >= (byte) 0) {
            return i | (zzvx2 << 7);
        }
        i |= (zzvx2 & 127) << 7;
        zzvx2 = zzvx();
        if (zzvx2 >= (byte) 0) {
            return i | (zzvx2 << 14);
        }
        i |= (zzvx2 & 127) << 14;
        zzvx2 = zzvx();
        if (zzvx2 >= (byte) 0) {
            return i | (zzvx2 << 21);
        }
        i |= (zzvx2 & 127) << 21;
        zzvx2 = zzvx();
        i |= zzvx2 << 28;
        if (zzvx2 >= (byte) 0) {
            return i;
        }
        for (int i2 = 0; i2 < 5; i2++) {
            if (zzvx() >= (byte) 0) {
                return i;
            }
        }
        throw zzabi.zzwd();
    }

    public final long zzvt() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte zzvx = zzvx();
            j |= ((long) (zzvx & 127)) << i;
            if ((zzvx & 128) == 0) {
                return j;
            }
        }
        throw zzabi.zzwd();
    }

    public final int zzvu() throws IOException {
        return (((zzvx() & 255) | ((zzvx() & 255) << 8)) | ((zzvx() & 255) << 16)) | ((zzvx() & 255) << 24);
    }

    public final long zzvv() throws IOException {
        byte zzvx = zzvx();
        byte zzvx2 = zzvx();
        return ((((((((((long) zzvx2) & 255) << 8) | (((long) zzvx) & 255)) | ((((long) zzvx()) & 255) << 16)) | ((((long) zzvx()) & 255) << 24)) | ((((long) zzvx()) & 255) << 32)) | ((((long) zzvx()) & 255) << 40)) | ((((long) zzvx()) & 255) << 48)) | ((((long) zzvx()) & 255) << 56);
    }

    public final int zzvw() {
        if (this.zzbtw == ConnectionsManager.DEFAULT_DATACENTER_ID) {
            return -1;
        }
        return this.zzbtw - this.zzbzd;
    }
}
