package com.google.android.gms.internal.measurement;

public final class zzabf implements Cloneable {
    private static final zzabg zzbzl = new zzabg();
    private int mSize;
    private boolean zzbzm;
    private int[] zzbzn;
    private zzabg[] zzbzo;

    zzabf() {
        this(10);
    }

    private zzabf(int i) {
        this.zzbzm = false;
        i = idealIntArraySize(i);
        this.zzbzn = new int[i];
        this.zzbzo = new zzabg[i];
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

    private final int zzax(int i) {
        int i2 = this.mSize - 1;
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) >>> 1;
            int i5 = this.zzbzn[i4];
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
        zzabf com_google_android_gms_internal_measurement_zzabf = new zzabf(i);
        int i2 = 0;
        System.arraycopy(this.zzbzn, 0, com_google_android_gms_internal_measurement_zzabf.zzbzn, 0, i);
        while (i2 < i) {
            if (this.zzbzo[i2] != null) {
                com_google_android_gms_internal_measurement_zzabf.zzbzo[i2] = (zzabg) this.zzbzo[i2].clone();
            }
            i2++;
        }
        com_google_android_gms_internal_measurement_zzabf.mSize = i;
        return com_google_android_gms_internal_measurement_zzabf;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzabf)) {
            return false;
        }
        zzabf com_google_android_gms_internal_measurement_zzabf = (zzabf) obj;
        if (this.mSize != com_google_android_gms_internal_measurement_zzabf.mSize) {
            return false;
        }
        boolean z;
        int[] iArr = this.zzbzn;
        int[] iArr2 = com_google_android_gms_internal_measurement_zzabf.zzbzn;
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
            zzabg[] com_google_android_gms_internal_measurement_zzabgArr = this.zzbzo;
            zzabg[] com_google_android_gms_internal_measurement_zzabgArr2 = com_google_android_gms_internal_measurement_zzabf.zzbzo;
            int i3 = this.mSize;
            for (i = 0; i < i3; i++) {
                if (!com_google_android_gms_internal_measurement_zzabgArr[i].equals(com_google_android_gms_internal_measurement_zzabgArr2[i])) {
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
            i = (((i * 31) + this.zzbzn[i2]) * 31) + this.zzbzo[i2].hashCode();
        }
        return i;
    }

    public final boolean isEmpty() {
        return this.mSize == 0;
    }

    final int size() {
        return this.mSize;
    }

    final void zza(int i, zzabg com_google_android_gms_internal_measurement_zzabg) {
        int zzax = zzax(i);
        if (zzax >= 0) {
            this.zzbzo[zzax] = com_google_android_gms_internal_measurement_zzabg;
            return;
        }
        zzax ^= -1;
        if (zzax >= this.mSize || this.zzbzo[zzax] != zzbzl) {
            if (this.mSize >= this.zzbzn.length) {
                int idealIntArraySize = idealIntArraySize(this.mSize + 1);
                Object obj = new int[idealIntArraySize];
                Object obj2 = new zzabg[idealIntArraySize];
                System.arraycopy(this.zzbzn, 0, obj, 0, this.zzbzn.length);
                System.arraycopy(this.zzbzo, 0, obj2, 0, this.zzbzo.length);
                this.zzbzn = obj;
                this.zzbzo = obj2;
            }
            if (this.mSize - zzax != 0) {
                int i2 = zzax + 1;
                System.arraycopy(this.zzbzn, zzax, this.zzbzn, i2, this.mSize - zzax);
                System.arraycopy(this.zzbzo, zzax, this.zzbzo, i2, this.mSize - zzax);
            }
            this.zzbzn[zzax] = i;
            this.zzbzo[zzax] = com_google_android_gms_internal_measurement_zzabg;
            this.mSize++;
            return;
        }
        this.zzbzn[zzax] = i;
        this.zzbzo[zzax] = com_google_android_gms_internal_measurement_zzabg;
    }

    final zzabg zzav(int i) {
        i = zzax(i);
        if (i >= 0) {
            if (this.zzbzo[i] != zzbzl) {
                return this.zzbzo[i];
            }
        }
        return null;
    }

    final zzabg zzaw(int i) {
        return this.zzbzo[i];
    }
}
