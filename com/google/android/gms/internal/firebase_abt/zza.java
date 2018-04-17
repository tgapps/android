package com.google.android.gms.internal.firebase_abt;

import java.io.IOException;
import org.telegram.tgnet.ConnectionsManager;

public final class zza {
    private final byte[] buffer;
    private final int zzh;
    private final int zzi;
    private int zzj;
    private int zzk;
    private int zzl;
    private int zzm;
    private int zzn = ConnectionsManager.DEFAULT_DATACENTER_ID;
    private int zzo;
    private int zzp = 64;
    private int zzq = ConnectionsManager.FileTypeFile;

    private zza(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzh = i;
        i2 += i;
        this.zzj = i2;
        this.zzi = i2;
        this.zzl = i;
    }

    public static zza zza(byte[] bArr, int i, int i2) {
        return new zza(bArr, 0, i2);
    }

    private final void zzc(int i) throws IOException {
        if (i < 0) {
            throw zzi.zzm();
        } else if (this.zzl + i > this.zzn) {
            zzc(this.zzn - this.zzl);
            throw zzi.zzl();
        } else if (i <= this.zzj - this.zzl) {
            this.zzl += i;
        } else {
            throw zzi.zzl();
        }
    }

    private final void zzh() {
        this.zzj += this.zzk;
        int i = this.zzj;
        if (i > this.zzn) {
            this.zzk = i - this.zzn;
            this.zzj -= this.zzk;
            return;
        }
        this.zzk = 0;
    }

    private final byte zzi() throws IOException {
        if (this.zzl == this.zzj) {
            throw zzi.zzl();
        }
        byte[] bArr = this.buffer;
        int i = this.zzl;
        this.zzl = i + 1;
        return bArr[i];
    }

    public final int getPosition() {
        return this.zzl - this.zzh;
    }

    public final String readString() throws IOException {
        int zzg = zzg();
        if (zzg < 0) {
            throw zzi.zzm();
        } else if (zzg > this.zzj - this.zzl) {
            throw zzi.zzl();
        } else {
            String str = new String(this.buffer, this.zzl, zzg, zzh.UTF_8);
            this.zzl += zzg;
            return str;
        }
    }

    public final void zza(int i) throws zzi {
        if (this.zzm != i) {
            throw new zzi("Protocol message end-group tag did not match expected tag.");
        }
    }

    public final void zza(zzj com_google_android_gms_internal_firebase_abt_zzj) throws IOException {
        int zzg = zzg();
        if (this.zzo >= this.zzp) {
            throw new zzi("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
        } else if (zzg < 0) {
            throw zzi.zzm();
        } else {
            zzg += this.zzl;
            int i = this.zzn;
            if (zzg > i) {
                throw zzi.zzl();
            }
            this.zzn = zzg;
            zzh();
            this.zzo++;
            com_google_android_gms_internal_firebase_abt_zzj.zza(this);
            zza(0);
            this.zzo--;
            this.zzn = i;
            zzh();
        }
    }

    public final byte[] zza(int i, int i2) {
        if (i2 == 0) {
            return zzm.zzao;
        }
        Object obj = new byte[i2];
        System.arraycopy(this.buffer, this.zzh + i, obj, 0, i2);
        return obj;
    }

    final void zzb(int i, int i2) {
        if (i > this.zzl - this.zzh) {
            int i3 = this.zzl - this.zzh;
            StringBuilder stringBuilder = new StringBuilder(50);
            stringBuilder.append("Position ");
            stringBuilder.append(i);
            stringBuilder.append(" is beyond current ");
            stringBuilder.append(i3);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (i < 0) {
            StringBuilder stringBuilder2 = new StringBuilder(24);
            stringBuilder2.append("Bad position ");
            stringBuilder2.append(i);
            throw new IllegalArgumentException(stringBuilder2.toString());
        } else {
            this.zzl = this.zzh + i;
            this.zzm = 106;
        }
    }

    public final boolean zzb(int i) throws IOException {
        switch (i & 7) {
            case 0:
                zzg();
                return true;
            case 1:
                zzi();
                zzi();
                zzi();
                zzi();
                zzi();
                zzi();
                zzi();
                zzi();
                return true;
            case 2:
                zzc(zzg());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzi();
                zzi();
                zzi();
                zzi();
                return true;
            default:
                throw new zzi("Protocol message tag had invalid wire type.");
        }
        int zzd;
        do {
            zzd = zzd();
            if (zzd != 0) {
            }
            zza(((i >>> 3) << 3) | 4);
            return true;
        } while (zzb(zzd));
        zza(((i >>> 3) << 3) | 4);
        return true;
    }

    public final int zzd() throws IOException {
        if (this.zzl == this.zzj) {
            this.zzm = 0;
            return 0;
        }
        this.zzm = zzg();
        if (this.zzm != 0) {
            return this.zzm;
        }
        throw new zzi("Protocol message contained an invalid tag (zero).");
    }

    public final long zze() throws IOException {
        int i = 0;
        long j = 0;
        while (i < 64) {
            byte zzi = zzi();
            long j2 = j | (((long) (zzi & 127)) << i);
            if ((zzi & 128) == 0) {
                return j2;
            }
            i += 7;
            j = j2;
        }
        throw zzi.zzn();
    }

    public final int zzf() throws IOException {
        return zzg();
    }

    public final int zzg() throws IOException {
        byte zzi = zzi();
        if (zzi >= (byte) 0) {
            return zzi;
        }
        int i;
        int i2 = zzi & 127;
        byte zzi2 = zzi();
        if (zzi2 >= (byte) 0) {
            i = zzi2 << 7;
        } else {
            i2 |= (zzi2 & 127) << 7;
            zzi2 = zzi();
            if (zzi2 >= (byte) 0) {
                i = zzi2 << 14;
            } else {
                i2 |= (zzi2 & 127) << 14;
                zzi2 = zzi();
                if (zzi2 >= (byte) 0) {
                    i = zzi2 << 21;
                } else {
                    i2 |= (zzi2 & 127) << 21;
                    zzi2 = zzi();
                    i2 |= zzi2 << 28;
                    if (zzi2 >= (byte) 0) {
                        return i2;
                    }
                    for (i = 0; i < 5; i++) {
                        if (zzi() >= (byte) 0) {
                            return i2;
                        }
                    }
                    throw zzi.zzn();
                }
            }
        }
        return i2 | i;
    }
}
