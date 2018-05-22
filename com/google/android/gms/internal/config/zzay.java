package com.google.android.gms.internal.config;

import java.io.IOException;
import org.telegram.tgnet.ConnectionsManager;

public final class zzay {
    private final byte[] buffer;
    private final int zzbw;
    private final int zzbx;
    private int zzby;
    private int zzbz;
    private int zzca;
    private int zzcb;
    private int zzcc = ConnectionsManager.DEFAULT_DATACENTER_ID;
    private int zzcd;
    private int zzce = 64;
    private int zzcf = ConnectionsManager.FileTypeFile;

    private zzay(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzbw = 0;
        int i3 = i2 + 0;
        this.zzby = i3;
        this.zzbx = i3;
        this.zzca = 0;
    }

    public static zzay zza(byte[] bArr, int i, int i2) {
        return new zzay(bArr, 0, i2);
    }

    private final void zzaa() {
        this.zzby += this.zzbz;
        int i = this.zzby;
        if (i > this.zzcc) {
            this.zzbz = i - this.zzcc;
            this.zzby -= this.zzbz;
            return;
        }
        this.zzbz = 0;
    }

    private final byte zzab() throws IOException {
        if (this.zzca == this.zzby) {
            throw zzbg.zzaf();
        }
        byte[] bArr = this.buffer;
        int i = this.zzca;
        this.zzca = i + 1;
        return bArr[i];
    }

    private final void zzg(int i) throws zzbg {
        if (this.zzcb != i) {
            throw new zzbg("Protocol message end-group tag did not match expected tag.");
        }
    }

    private final void zzi(int i) throws IOException {
        if (i < 0) {
            throw zzbg.zzag();
        } else if (this.zzca + i > this.zzcc) {
            zzi(this.zzcc - this.zzca);
            throw zzbg.zzaf();
        } else if (i <= this.zzby - this.zzca) {
            this.zzca += i;
        } else {
            throw zzbg.zzaf();
        }
    }

    public final int getPosition() {
        return this.zzca - this.zzbw;
    }

    public final byte[] readBytes() throws IOException {
        int zzy = zzy();
        if (zzy < 0) {
            throw zzbg.zzag();
        } else if (zzy == 0) {
            return zzbk.zzdd;
        } else {
            if (zzy > this.zzby - this.zzca) {
                throw zzbg.zzaf();
            }
            Object obj = new byte[zzy];
            System.arraycopy(this.buffer, this.zzca, obj, 0, zzy);
            this.zzca = zzy + this.zzca;
            return obj;
        }
    }

    public final String readString() throws IOException {
        int zzy = zzy();
        if (zzy < 0) {
            throw zzbg.zzag();
        } else if (zzy > this.zzby - this.zzca) {
            throw zzbg.zzaf();
        } else {
            String str = new String(this.buffer, this.zzca, zzy, zzbf.UTF_8);
            this.zzca = zzy + this.zzca;
            return str;
        }
    }

    public final void zza(zzbh com_google_android_gms_internal_config_zzbh) throws IOException {
        int zzy = zzy();
        if (this.zzcd >= this.zzce) {
            throw new zzbg("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
        } else if (zzy < 0) {
            throw zzbg.zzag();
        } else {
            zzy += this.zzca;
            int i = this.zzcc;
            if (zzy > i) {
                throw zzbg.zzaf();
            }
            this.zzcc = zzy;
            zzaa();
            this.zzcd++;
            com_google_android_gms_internal_config_zzbh.zza(this);
            zzg(0);
            this.zzcd--;
            this.zzcc = i;
            zzaa();
        }
    }

    public final byte[] zza(int i, int i2) {
        if (i2 == 0) {
            return zzbk.zzdd;
        }
        Object obj = new byte[i2];
        System.arraycopy(this.buffer, this.zzbw + i, obj, 0, i2);
        return obj;
    }

    final void zzb(int i, int i2) {
        if (i > this.zzca - this.zzbw) {
            throw new IllegalArgumentException("Position " + i + " is beyond current " + (this.zzca - this.zzbw));
        } else if (i < 0) {
            throw new IllegalArgumentException("Bad position " + i);
        } else {
            this.zzca = this.zzbw + i;
            this.zzcb = i2;
        }
    }

    public final boolean zzh(int i) throws IOException {
        switch (i & 7) {
            case 0:
                zzy();
                return true;
            case 1:
                zzz();
                return true;
            case 2:
                zzi(zzy());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzab();
                zzab();
                zzab();
                zzab();
                return true;
            default:
                throw new zzbg("Protocol message tag had invalid wire type.");
        }
        int zzx;
        do {
            zzx = zzx();
            if (zzx != 0) {
            }
            zzg(((i >>> 3) << 3) | 4);
            return true;
        } while (zzh(zzx));
        zzg(((i >>> 3) << 3) | 4);
        return true;
    }

    public final int zzx() throws IOException {
        if (this.zzca == this.zzby) {
            this.zzcb = 0;
            return 0;
        }
        this.zzcb = zzy();
        if (this.zzcb != 0) {
            return this.zzcb;
        }
        throw new zzbg("Protocol message contained an invalid tag (zero).");
    }

    public final int zzy() throws IOException {
        byte zzab = zzab();
        if (zzab >= (byte) 0) {
            return zzab;
        }
        int i = zzab & 127;
        byte zzab2 = zzab();
        if (zzab2 >= (byte) 0) {
            return i | (zzab2 << 7);
        }
        i |= (zzab2 & 127) << 7;
        zzab2 = zzab();
        if (zzab2 >= (byte) 0) {
            return i | (zzab2 << 14);
        }
        i |= (zzab2 & 127) << 14;
        zzab2 = zzab();
        if (zzab2 >= (byte) 0) {
            return i | (zzab2 << 21);
        }
        i |= (zzab2 & 127) << 21;
        zzab2 = zzab();
        i |= zzab2 << 28;
        if (zzab2 >= (byte) 0) {
            return i;
        }
        for (int i2 = 0; i2 < 5; i2++) {
            if (zzab() >= (byte) 0) {
                return i;
            }
        }
        throw new zzbg("CodedInputStream encountered a malformed varint.");
    }

    public final long zzz() throws IOException {
        byte zzab = zzab();
        byte zzab2 = zzab();
        return ((((((((((long) zzab2) & 255) << 8) | (((long) zzab) & 255)) | ((((long) zzab()) & 255) << 16)) | ((((long) zzab()) & 255) << 24)) | ((((long) zzab()) & 255) << 32)) | ((((long) zzab()) & 255) << 40)) | ((((long) zzab()) & 255) << 48)) | ((((long) zzab()) & 255) << 56);
    }
}
