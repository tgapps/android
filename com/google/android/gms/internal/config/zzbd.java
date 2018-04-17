package com.google.android.gms.internal.config;

public final class zzbd implements Cloneable {
    private static final zzbe zzck = new zzbe();
    private int mSize;
    private boolean zzcl;
    private int[] zzcm;
    private zzbe[] zzcn;

    zzbd() {
        this(10);
    }

    private zzbd(int i) {
        this.zzcl = false;
        i = idealIntArraySize(i);
        this.zzcm = new int[i];
        this.zzcn = new zzbe[i];
        this.mSize = 0;
    }

    private static int idealIntArraySize(int i) {
        i <<= 2;
        for (int i2 = 4; i2 < 32; i2++) {
            int i3 = (1 << i2) - 12;
            if (i <= i3) {
                i = i3;
                break;
            }
        }
        return i / 4;
    }

    private final int zzq(int i) {
        int i2 = this.mSize - 1;
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) >>> 1;
            int i5 = this.zzcm[i4];
            if (i5 < i) {
                i3 = i4 + 1;
            } else if (i5 <= i) {
                return i4;
            } else {
                i2 = i4 - 1;
            }
        }
        return i3 ^ -1;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        int i = this.mSize;
        zzbd com_google_android_gms_internal_config_zzbd = new zzbd(i);
        int i2 = 0;
        System.arraycopy(this.zzcm, 0, com_google_android_gms_internal_config_zzbd.zzcm, 0, i);
        while (i2 < i) {
            if (this.zzcn[i2] != null) {
                com_google_android_gms_internal_config_zzbd.zzcn[i2] = (zzbe) this.zzcn[i2].clone();
            }
            i2++;
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
        boolean z;
        int[] iArr = this.zzcm;
        int[] iArr2 = com_google_android_gms_internal_config_zzbd.zzcm;
        int i = this.mSize;
        for (int i2 = 0; i2 < i; i2++) {
            if (iArr[i2] != iArr2[i2]) {
                z = false;
                break;
            }
        }
        z = true;
        if (z) {
            boolean z2;
            zzbe[] com_google_android_gms_internal_config_zzbeArr = this.zzcn;
            zzbe[] com_google_android_gms_internal_config_zzbeArr2 = com_google_android_gms_internal_config_zzbd.zzcn;
            int i3 = this.mSize;
            for (i = 0; i < i3; i++) {
                if (!com_google_android_gms_internal_config_zzbeArr[i].equals(com_google_android_gms_internal_config_zzbeArr2[i])) {
                    z2 = false;
                    break;
                }
            }
            z2 = true;
            if (z2) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int i = 17;
        for (int i2 = 0; i2 < this.mSize; i2++) {
            i = (((i * 31) + this.zzcm[i2]) * 31) + this.zzcn[i2].hashCode();
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
            this.zzcn[zzq] = com_google_android_gms_internal_config_zzbe;
            return;
        }
        zzq ^= -1;
        if (zzq >= this.mSize || this.zzcn[zzq] != zzck) {
            if (this.mSize >= this.zzcm.length) {
                int idealIntArraySize = idealIntArraySize(this.mSize + 1);
                Object obj = new int[idealIntArraySize];
                Object obj2 = new zzbe[idealIntArraySize];
                System.arraycopy(this.zzcm, 0, obj, 0, this.zzcm.length);
                System.arraycopy(this.zzcn, 0, obj2, 0, this.zzcn.length);
                this.zzcm = obj;
                this.zzcn = obj2;
            }
            if (this.mSize - zzq != 0) {
                int i2 = zzq + 1;
                System.arraycopy(this.zzcm, zzq, this.zzcm, i2, this.mSize - zzq);
                System.arraycopy(this.zzcn, zzq, this.zzcn, i2, this.mSize - zzq);
            }
            this.zzcm[zzq] = i;
            this.zzcn[zzq] = com_google_android_gms_internal_config_zzbe;
            this.mSize++;
            return;
        }
        this.zzcm[zzq] = i;
        this.zzcn[zzq] = com_google_android_gms_internal_config_zzbe;
    }

    final zzbe zzo(int i) {
        i = zzq(i);
        if (i >= 0) {
            if (this.zzcn[i] != zzck) {
                return this.zzcn[i];
            }
        }
        return null;
    }

    final zzbe zzp(int i) {
        return this.zzcn[i];
    }
}
