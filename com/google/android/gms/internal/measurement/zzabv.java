package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.tgnet.ConnectionsManager;

public final class zzabv {
    private final byte[] buffer;
    private int zzbrd = 64;
    private int zzbre = ConnectionsManager.FileTypeFile;
    private int zzbri;
    private int zzbrk = ConnectionsManager.DEFAULT_DATACENTER_ID;
    private final int zzbwp;
    private final int zzbwq;
    private int zzbwr;
    private int zzbws;
    private int zzbwt;
    private int zzbwu;

    private zzabv(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzbwp = i;
        int i3 = i + i2;
        this.zzbwr = i3;
        this.zzbwq = i3;
        this.zzbws = i;
    }

    public static zzabv zza(byte[] bArr, int i, int i2) {
        return new zzabv(bArr, 0, i2);
    }

    private final void zzan(int i) throws IOException {
        if (i < 0) {
            throw zzacd.zzvi();
        } else if (this.zzbws + i > this.zzbrk) {
            zzan(this.zzbrk - this.zzbws);
            throw zzacd.zzvh();
        } else if (i <= this.zzbwr - this.zzbws) {
            this.zzbws += i;
        } else {
            throw zzacd.zzvh();
        }
    }

    public static zzabv zzi(byte[] bArr) {
        return zza(bArr, 0, bArr.length);
    }

    private final void zzta() {
        this.zzbwr += this.zzbri;
        int i = this.zzbwr;
        if (i > this.zzbrk) {
            this.zzbri = i - this.zzbrk;
            this.zzbwr -= this.zzbri;
            return;
        }
        this.zzbri = 0;
    }

    private final byte zzvd() throws IOException {
        if (this.zzbws == this.zzbwr) {
            throw zzacd.zzvh();
        }
        byte[] bArr = this.buffer;
        int i = this.zzbws;
        this.zzbws = i + 1;
        return bArr[i];
    }

    public final int getPosition() {
        return this.zzbws - this.zzbwp;
    }

    public final String readString() throws IOException {
        int zzuy = zzuy();
        if (zzuy < 0) {
            throw zzacd.zzvi();
        } else if (zzuy > this.zzbwr - this.zzbws) {
            throw zzacd.zzvh();
        } else {
            String str = new String(this.buffer, this.zzbws, zzuy, zzacc.UTF_8);
            this.zzbws = zzuy + this.zzbws;
            return str;
        }
    }

    public final void zza(zzace com_google_android_gms_internal_measurement_zzace) throws IOException {
        int zzuy = zzuy();
        if (this.zzbwu >= this.zzbrd) {
            throw zzacd.zzvk();
        }
        zzuy = zzaf(zzuy);
        this.zzbwu++;
        com_google_android_gms_internal_measurement_zzace.zzb(this);
        zzaj(0);
        this.zzbwu--;
        zzal(zzuy);
    }

    public final void zza(zzace com_google_android_gms_internal_measurement_zzace, int i) throws IOException {
        if (this.zzbwu >= this.zzbrd) {
            throw zzacd.zzvk();
        }
        this.zzbwu++;
        com_google_android_gms_internal_measurement_zzace.zzb(this);
        zzaj((i << 3) | 4);
        this.zzbwu--;
    }

    public final int zzaf(int i) throws zzacd {
        if (i < 0) {
            throw zzacd.zzvi();
        }
        int i2 = this.zzbws + i;
        int i3 = this.zzbrk;
        if (i2 > i3) {
            throw zzacd.zzvh();
        }
        this.zzbrk = i2;
        zzta();
        return i3;
    }

    public final void zzaj(int i) throws zzacd {
        if (this.zzbwt != i) {
            throw new zzacd("Protocol message end-group tag did not match expected tag.");
        }
    }

    public final boolean zzak(int i) throws IOException {
        switch (i & 7) {
            case 0:
                zzuy();
                return true;
            case 1:
                zzvb();
                return true;
            case 2:
                zzan(zzuy());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzva();
                return true;
            default:
                throw new zzacd("Protocol message tag had invalid wire type.");
        }
        int zzuw;
        do {
            zzuw = zzuw();
            if (zzuw != 0) {
            }
            zzaj(((i >>> 3) << 3) | 4);
            return true;
        } while (zzak(zzuw));
        zzaj(((i >>> 3) << 3) | 4);
        return true;
    }

    public final void zzal(int i) {
        this.zzbrk = i;
        zzta();
    }

    public final void zzam(int i) {
        zzd(i, this.zzbwt);
    }

    public final byte[] zzc(int i, int i2) {
        if (i2 == 0) {
            return zzach.zzbxs;
        }
        Object obj = new byte[i2];
        System.arraycopy(this.buffer, this.zzbwp + i, obj, 0, i2);
        return obj;
    }

    final void zzd(int i, int i2) {
        if (i > this.zzbws - this.zzbwp) {
            throw new IllegalArgumentException("Position " + i + " is beyond current " + (this.zzbws - this.zzbwp));
        } else if (i < 0) {
            throw new IllegalArgumentException("Bad position " + i);
        } else {
            this.zzbws = this.zzbwp + i;
            this.zzbwt = i2;
        }
    }

    public final int zzuw() throws IOException {
        if (this.zzbws == this.zzbwr) {
            this.zzbwt = 0;
            return 0;
        }
        this.zzbwt = zzuy();
        if (this.zzbwt != 0) {
            return this.zzbwt;
        }
        throw new zzacd("Protocol message contained an invalid tag (zero).");
    }

    public final boolean zzux() throws IOException {
        return zzuy() != 0;
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
        throw zzacd.zzvj();
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
        throw zzacd.zzvj();
    }

    public final int zzva() throws IOException {
        return (((zzvd() & 255) | ((zzvd() & 255) << 8)) | ((zzvd() & 255) << 16)) | ((zzvd() & 255) << 24);
    }

    public final long zzvb() throws IOException {
        byte zzvd = zzvd();
        byte zzvd2 = zzvd();
        return ((((((((((long) zzvd2) & 255) << 8) | (((long) zzvd) & 255)) | ((((long) zzvd()) & 255) << 16)) | ((((long) zzvd()) & 255) << 24)) | ((((long) zzvd()) & 255) << 32)) | ((((long) zzvd()) & 255) << 40)) | ((((long) zzvd()) & 255) << 48)) | ((((long) zzvd()) & 255) << 56);
    }

    public final int zzvc() {
        if (this.zzbrk == ConnectionsManager.DEFAULT_DATACENTER_ID) {
            return -1;
        }
        return this.zzbrk - this.zzbws;
    }
}
