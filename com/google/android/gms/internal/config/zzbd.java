package com.google.android.gms.internal.config;

public final class zzbd implements Cloneable {
    private static final zzbe zzcj = new zzbe();
    private int mSize;
    private boolean zzck;
    private int[] zzcl;
    private zzbe[] zzcm;

    zzbd() {
        this(10);
    }

    private zzbd(int i) {
        this.zzck = false;
        int idealIntArraySize = idealIntArraySize(i);
        this.zzcl = new int[idealIntArraySize];
        this.zzcm = new zzbe[idealIntArraySize];
        this.mSize = 0;
    }

    private static int idealIntArraySize(int i) {
        int i2 = i << 2;
        for (int i3 = 4; i3 < 32; i3++) {
            if (i2 <= (1 << i3) - 12) {
                i2 = (1 << i3) - 12;
                break;
            }
        }
        return i2 / 4;
    }

    private final int zzq(int i) {
        int i2 = 0;
        int i3 = this.mSize - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) >>> 1;
            int i5 = this.zzcl[i4];
            if (i5 < i) {
                i2 = i4 + 1;
            } else if (i5 <= i) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return i2 ^ -1;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        int i = this.mSize;
        zzbd com_google_android_gms_internal_config_zzbd = new zzbd(i);
        System.arraycopy(this.zzcl, 0, com_google_android_gms_internal_config_zzbd.zzcl, 0, i);
        for (int i2 = 0; i2 < i; i2++) {
            if (this.zzcm[i2] != null) {
                com_google_android_gms_internal_config_zzbd.zzcm[i2] = (zzbe) this.zzcm[i2].clone();
            }
        }
        com_google_android_gms_internal_config_zzbd.mSize = i;
        return com_google_android_gms_internal_config_zzbd;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzbd)) {
            return false;
        }
        zzbd com_google_android_gms_internal_config_zzbd = (zzbd) obj;
        if (this.mSize != com_google_android_gms_internal_config_zzbd.mSize) {
            return false;
        }
        int i;
        boolean z;
        int[] iArr = this.zzcl;
        int[] iArr2 = com_google_android_gms_internal_config_zzbd.zzcl;
        int i2 = this.mSize;
        for (i = 0; i < i2; i++) {
            if (iArr[i] != iArr2[i]) {
                z = false;
                break;
            }
        }
        z = true;
        if (z) {
            zzbe[] com_google_android_gms_internal_config_zzbeArr = this.zzcm;
            zzbe[] com_google_android_gms_internal_config_zzbeArr2 = com_google_android_gms_internal_config_zzbd.zzcm;
            i2 = this.mSize;
            for (i = 0; i < i2; i++) {
                if (!com_google_android_gms_internal_config_zzbeArr[i].equals(com_google_android_gms_internal_config_zzbeArr2[i])) {
                    z = false;
                    break;
                }
            }
            z = true;
            if (z) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int i = 17;
        for (int i2 = 0; i2 < this.mSize; i2++) {
            i = (((i * 31) + this.zzcl[i2]) * 31) + this.zzcm[i2].hashCode();
        }
        return i;
    }

    public final boolean isEmpty() {
        return this.mSize == 0;
    }

    final int size() {
        return this.mSize;
    }

    final void zza(int i, zzbe com_google_android_gms_internal_config_zzbe) {
        int zzq = zzq(i);
        if (zzq >= 0) {
            this.zzcm[zzq] = com_google_android_gms_internal_config_zzbe;
            return;
        }
        zzq ^= -1;
        if (zzq >= this.mSize || this.zzcm[zzq] != zzcj) {
            if (this.mSize >= this.zzcl.length) {
                int idealIntArraySize = idealIntArraySize(this.mSize + 1);
                Object obj = new int[idealIntArraySize];
                Object obj2 = new zzbe[idealIntArraySize];
                System.arraycopy(this.zzcl, 0, obj, 0, this.zzcl.length);
                System.arraycopy(this.zzcm, 0, obj2, 0, this.zzcm.length);
                this.zzcl = obj;
                this.zzcm = obj2;
            }
            if (this.mSize - zzq != 0) {
                System.arraycopy(this.zzcl, zzq, this.zzcl, zzq + 1, this.mSize - zzq);
                System.arraycopy(this.zzcm, zzq, this.zzcm, zzq + 1, this.mSize - zzq);
            }
            this.zzcl[zzq] = i;
            this.zzcm[zzq] = com_google_android_gms_internal_config_zzbe;
            this.mSize++;
            return;
        }
        this.zzcl[zzq] = i;
        this.zzcm[zzq] = com_google_android_gms_internal_config_zzbe;
    }

    final zzbe zzo(int i) {
        int zzq = zzq(i);
        return (zzq < 0 || this.zzcm[zzq] == zzcj) ? null : this.zzcm[zzq];
    }

    final zzbe zzp(int i) {
        return this.zzcm[i];
    }
}
