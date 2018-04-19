package com.google.android.gms.internal.config;

import java.io.IOException;
import org.telegram.tgnet.ConnectionsManager;

public final class zzay {
    private final byte[] buffer;
    private final int zzbx;
    private final int zzby;
    private int zzbz;
    private int zzca;
    private int zzcb;
    private int zzcc;
    private int zzcd = ConnectionsManager.DEFAULT_DATACENTER_ID;
    private int zzce;
    private int zzcf = 64;
    private int zzcg = ConnectionsManager.FileTypeFile;

    private zzay(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzbx = 0;
        int i3 = i2 + 0;
        this.zzbz = i3;
        this.zzby = i3;
        this.zzcb = 0;
    }

    public static zzay zza(byte[] bArr, int i, int i2) {
        return new zzay(bArr, 0, i2);
    }

    private final void zzab() {
        this.zzbz += this.zzca;
        int i = this.zzbz;
        if (i > this.zzcd) {
            this.zzca = i - this.zzcd;
            this.zzbz -= this.zzca;
            return;
        }
        this.zzca = 0;
    }

    private final byte zzac() throws IOException {
        if (this.zzcb == this.zzbz) {
            throw zzbg.zzag();
        }
        byte[] bArr = this.buffer;
        int i = this.zzcb;
        this.zzcb = i + 1;
        return bArr[i];
    }

    private final void zzg(int i) throws zzbg {
        if (this.zzcc != i) {
            throw new zzbg("Protocol message end-group tag did not match expected tag.");
        }
    }

    private final void zzi(int i) throws IOException {
        if (i < 0) {
            throw zzbg.zzah();
        } else if (this.zzcb + i > this.zzcd) {
            zzi(this.zzcd - this.zzcb);
            throw zzbg.zzag();
        } else if (i <= this.zzbz - this.zzcb) {
            this.zzcb += i;
        } else {
            throw zzbg.zzag();
        }
    }

    public final int getPosition() {
        return this.zzcb - this.zzbx;
    }

    public final byte[] readBytes() throws IOException {
        int zzz = zzz();
        if (zzz < 0) {
            throw zzbg.zzah();
        } else if (zzz == 0) {
            return zzbk.zzde;
        } else {
            if (zzz > this.zzbz - this.zzcb) {
                throw zzbg.zzag();
            }
            Object obj = new byte[zzz];
            System.arraycopy(this.buffer, this.zzcb, obj, 0, zzz);
            this.zzcb = zzz + this.zzcb;
            return obj;
        }
    }

    public final String readString() throws IOException {
        int zzz = zzz();
        if (zzz < 0) {
            throw zzbg.zzah();
        } else if (zzz > this.zzbz - this.zzcb) {
            throw zzbg.zzag();
        } else {
            String str = new String(this.buffer, this.zzcb, zzz, zzbf.UTF_8);
            this.zzcb = zzz + this.zzcb;
            return str;
        }
    }

    public final void zza(zzbh com_google_android_gms_internal_config_zzbh) throws IOException {
        int zzz = zzz();
        if (this.zzce >= this.zzcf) {
            throw new zzbg("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
        } else if (zzz < 0) {
            throw zzbg.zzah();
        } else {
            zzz += this.zzcb;
            int i = this.zzcd;
            if (zzz > i) {
                throw zzbg.zzag();
            }
            this.zzcd = zzz;
            zzab();
            this.zzce++;
            com_google_android_gms_internal_config_zzbh.zza(this);
            zzg(0);
            this.zzce--;
            this.zzcd = i;
            zzab();
        }
    }

    public final byte[] zza(int i, int i2) {
        if (i2 == 0) {
            return zzbk.zzde;
        }
        Object obj = new byte[i2];
        System.arraycopy(this.buffer, this.zzbx + i, obj, 0, i2);
        return obj;
    }

    public final long zzaa() throws IOException {
        byte zzac = zzac();
        byte zzac2 = zzac();
        return ((((((((((long) zzac2) & 255) << 8) | (((long) zzac) & 255)) | ((((long) zzac()) & 255) << 16)) | ((((long) zzac()) & 255) << 24)) | ((((long) zzac()) & 255) << 32)) | ((((long) zzac()) & 255) << 40)) | ((((long) zzac()) & 255) << 48)) | ((((long) zzac()) & 255) << 56);
    }

    final void zzb(int i, int i2) {
        if (i > this.zzcb - this.zzbx) {
            throw new IllegalArgumentException("Position " + i + " is beyond current " + (this.zzcb - this.zzbx));
        } else if (i < 0) {
            throw new IllegalArgumentException("Bad position " + i);
        } else {
            this.zzcb = this.zzbx + i;
            this.zzcc = i2;
        }
    }

    public final boolean zzh(int i) throws IOException {
        switch (i & 7) {
            case 0:
                zzz();
                return true;
            case 1:
                zzaa();
                return true;
            case 2:
                zzi(zzz());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzac();
                zzac();
                zzac();
                zzac();
                return true;
            default:
                throw new zzbg("Protocol message tag had invalid wire type.");
        }
        int zzy;
        do {
            zzy = zzy();
            if (zzy != 0) {
            }
            zzg(((i >>> 3) << 3) | 4);
            return true;
        } while (zzh(zzy));
        zzg(((i >>> 3) << 3) | 4);
        return true;
    }

    public final int zzy() throws IOException {
        if (this.zzcb == this.zzbz) {
            this.zzcc = 0;
            return 0;
        }
        this.zzcc = zzz();
        if (this.zzcc != 0) {
            return this.zzcc;
        }
        throw new zzbg("Protocol message contained an invalid tag (zero).");
    }

    public final int zzz() throws IOException {
        byte zzac = zzac();
        if (zzac >= (byte) 0) {
            return zzac;
        }
        int i = zzac & 127;
        byte zzac2 = zzac();
        if (zzac2 >= (byte) 0) {
            return i | (zzac2 << 7);
        }
        i |= (zzac2 & 127) << 7;
        zzac2 = zzac();
        if (zzac2 >= (byte) 0) {
            return i | (zzac2 << 14);
        }
        i |= (zzac2 & 127) << 14;
        zzac2 = zzac();
        if (zzac2 >= (byte) 0) {
            return i | (zzac2 << 21);
        }
        i |= (zzac2 & 127) << 21;
        zzac2 = zzac();
        i |= zzac2 << 28;
        if (zzac2 >= (byte) 0) {
            return i;
        }
        for (int i2 = 0; i2 < 5; i2++) {
            if (zzac() >= (byte) 0) {
                return i;
            }
        }
        throw new zzbg("CodedInputStream encountered a malformed varint.");
    }
}
