package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzvm.zze;
import java.io.IOException;
import java.util.Arrays;

public final class zzyc {
    private static final zzyc zzcco = new zzyc(0, new int[0], new Object[0], false);
    private int count;
    private boolean zzbtu;
    private int zzbyn;
    private Object[] zzcba;
    private int[] zzccp;

    public static zzyc zzyf() {
        return zzcco;
    }

    static zzyc zzyg() {
        return new zzyc();
    }

    static zzyc zza(zzyc com_google_android_gms_internal_measurement_zzyc, zzyc com_google_android_gms_internal_measurement_zzyc2) {
        int i = com_google_android_gms_internal_measurement_zzyc.count + com_google_android_gms_internal_measurement_zzyc2.count;
        Object copyOf = Arrays.copyOf(com_google_android_gms_internal_measurement_zzyc.zzccp, i);
        System.arraycopy(com_google_android_gms_internal_measurement_zzyc2.zzccp, 0, copyOf, com_google_android_gms_internal_measurement_zzyc.count, com_google_android_gms_internal_measurement_zzyc2.count);
        Object copyOf2 = Arrays.copyOf(com_google_android_gms_internal_measurement_zzyc.zzcba, i);
        System.arraycopy(com_google_android_gms_internal_measurement_zzyc2.zzcba, 0, copyOf2, com_google_android_gms_internal_measurement_zzyc.count, com_google_android_gms_internal_measurement_zzyc2.count);
        return new zzyc(i, copyOf, copyOf2, true);
    }

    private zzyc() {
        this(0, new int[8], new Object[8], true);
    }

    private zzyc(int i, int[] iArr, Object[] objArr, boolean z) {
        this.zzbyn = -1;
        this.count = i;
        this.zzccp = iArr;
        this.zzcba = objArr;
        this.zzbtu = z;
    }

    public final void zzsm() {
        this.zzbtu = false;
    }

    final void zza(zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
        int i;
        if (com_google_android_gms_internal_measurement_zzyw.zzvj() == zze.zzbzf) {
            for (i = this.count - 1; i >= 0; i--) {
                com_google_android_gms_internal_measurement_zzyw.zza(this.zzccp[i] >>> 3, this.zzcba[i]);
            }
            return;
        }
        for (i = 0; i < this.count; i++) {
            com_google_android_gms_internal_measurement_zzyw.zza(this.zzccp[i] >>> 3, this.zzcba[i]);
        }
    }

    public final void zzb(zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
        if (this.count != 0) {
            int i;
            if (com_google_android_gms_internal_measurement_zzyw.zzvj() == zze.zzbze) {
                for (i = 0; i < this.count; i++) {
                    zzb(this.zzccp[i], this.zzcba[i], com_google_android_gms_internal_measurement_zzyw);
                }
                return;
            }
            for (i = this.count - 1; i >= 0; i--) {
                zzb(this.zzccp[i], this.zzcba[i], com_google_android_gms_internal_measurement_zzyw);
            }
        }
    }

    private static void zzb(int i, Object obj, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
        int i2 = i >>> 3;
        switch (i & 7) {
            case 0:
                com_google_android_gms_internal_measurement_zzyw.zzi(i2, ((Long) obj).longValue());
                return;
            case 1:
                com_google_android_gms_internal_measurement_zzyw.zzc(i2, ((Long) obj).longValue());
                return;
            case 2:
                com_google_android_gms_internal_measurement_zzyw.zza(i2, (zzud) obj);
                return;
            case 3:
                if (com_google_android_gms_internal_measurement_zzyw.zzvj() == zze.zzbze) {
                    com_google_android_gms_internal_measurement_zzyw.zzbk(i2);
                    ((zzyc) obj).zzb(com_google_android_gms_internal_measurement_zzyw);
                    com_google_android_gms_internal_measurement_zzyw.zzbl(i2);
                    return;
                }
                com_google_android_gms_internal_measurement_zzyw.zzbl(i2);
                ((zzyc) obj).zzb(com_google_android_gms_internal_measurement_zzyw);
                com_google_android_gms_internal_measurement_zzyw.zzbk(i2);
                return;
            case 5:
                com_google_android_gms_internal_measurement_zzyw.zzg(i2, ((Integer) obj).intValue());
                return;
            default:
                throw new RuntimeException(zzvt.zzwo());
        }
    }

    public final int zzyh() {
        int i = this.zzbyn;
        if (i == -1) {
            i = 0;
            for (int i2 = 0; i2 < this.count; i2++) {
                i += zzut.zzd(this.zzccp[i2] >>> 3, (zzud) this.zzcba[i2]);
            }
            this.zzbyn = i;
        }
        return i;
    }

    public final int zzvu() {
        int i = this.zzbyn;
        if (i == -1) {
            i = 0;
            for (int i2 = 0; i2 < this.count; i2++) {
                int i3 = this.zzccp[i2];
                int i4 = i3 >>> 3;
                switch (i3 & 7) {
                    case 0:
                        i += zzut.zze(i4, ((Long) this.zzcba[i2]).longValue());
                        break;
                    case 1:
                        i += zzut.zzg(i4, ((Long) this.zzcba[i2]).longValue());
                        break;
                    case 2:
                        i += zzut.zzc(i4, (zzud) this.zzcba[i2]);
                        break;
                    case 3:
                        i += ((zzyc) this.zzcba[i2]).zzvu() + (zzut.zzbb(i4) << 1);
                        break;
                    case 5:
                        i += zzut.zzk(i4, ((Integer) this.zzcba[i2]).intValue());
                        break;
                    default:
                        throw new IllegalStateException(zzvt.zzwo());
                }
            }
            this.zzbyn = i;
        }
        return i;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof zzyc)) {
            return false;
        }
        zzyc com_google_android_gms_internal_measurement_zzyc = (zzyc) obj;
        if (this.count == com_google_android_gms_internal_measurement_zzyc.count) {
            int i;
            boolean z;
            int[] iArr = this.zzccp;
            int[] iArr2 = com_google_android_gms_internal_measurement_zzyc.zzccp;
            int i2 = this.count;
            for (i = 0; i < i2; i++) {
                if (iArr[i] != iArr2[i]) {
                    z = false;
                    break;
                }
            }
            z = true;
            if (z) {
                Object[] objArr = this.zzcba;
                Object[] objArr2 = com_google_android_gms_internal_measurement_zzyc.zzcba;
                i2 = this.count;
                for (i = 0; i < i2; i++) {
                    if (!objArr[i].equals(objArr2[i])) {
                        z = false;
                        break;
                    }
                }
                z = true;
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }

    public final int hashCode() {
        int i;
        int i2 = 17;
        int i3 = 0;
        int i4 = (this.count + 527) * 31;
        int[] iArr = this.zzccp;
        int i5 = 17;
        for (i = 0; i < this.count; i++) {
            i5 = (i5 * 31) + iArr[i];
        }
        i = (i4 + i5) * 31;
        Object[] objArr = this.zzcba;
        while (i3 < this.count) {
            i2 = (i2 * 31) + objArr[i3].hashCode();
            i3++;
        }
        return i + i2;
    }

    final void zzb(StringBuilder stringBuilder, int i) {
        for (int i2 = 0; i2 < this.count; i2++) {
            zzww.zzb(stringBuilder, i, String.valueOf(this.zzccp[i2] >>> 3), this.zzcba[i2]);
        }
    }

    final void zzb(int i, Object obj) {
        if (this.zzbtu) {
            if (this.count == this.zzccp.length) {
                int i2 = (this.count < 4 ? 8 : this.count >> 1) + this.count;
                this.zzccp = Arrays.copyOf(this.zzccp, i2);
                this.zzcba = Arrays.copyOf(this.zzcba, i2);
            }
            this.zzccp[this.count] = i;
            this.zzcba[this.count] = obj;
            this.count++;
            return;
        }
        throw new UnsupportedOperationException();
    }
}
