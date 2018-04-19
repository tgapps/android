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
        int idealIntArraySize = idealIntArraySize(i);
        this.zzbzn = new int[idealIntArraySize];
        this.zzbzo = new zzabg[idealIntArraySize];
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

    private final int zzax(int i) {
        int i2 = 0;
        int i3 = this.mSize - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) >>> 1;
            int i5 = this.zzbzn[i4];
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
        zzabf com_google_android_gms_internal_measurement_zzabf = new zzabf(i);
        System.arraycopy(this.zzbzn, 0, com_google_android_gms_internal_measurement_zzabf.zzbzn, 0, i);
        for (int i2 = 0; i2 < i; i2++) {
            if (this.zzbzo[i2] != null) {
                com_google_android_gms_internal_measurement_zzabf.zzbzo[i2] = (zzabg) this.zzbzo[i2].clone();
            }
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
        int i;
        boolean z;
        int[] iArr = this.zzbzn;
        int[] iArr2 = com_google_android_gms_internal_measurement_zzabf.zzbzn;
        int i2 = this.mSize;
        for (i = 0; i < i2; i++) {
            if (iArr[i] != iArr2[i]) {
                z = false;
                break;
            }
        }
        z = true;
        if (z) {
            zzabg[] com_google_android_gms_internal_measurement_zzabgArr = this.zzbzo;
            zzabg[] com_google_android_gms_internal_measurement_zzabgArr2 = com_google_android_gms_internal_measurement_zzabf.zzbzo;
            i2 = this.mSize;
            for (i = 0; i < i2; i++) {
                if (!com_google_android_gms_internal_measurement_zzabgArr[i].equals(com_google_android_gms_internal_measurement_zzabgArr2[i])) {
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
                System.arraycopy(this.zzbzn, zzax, this.zzbzn, zzax + 1, this.mSize - zzax);
                System.arraycopy(this.zzbzo, zzax, this.zzbzo, zzax + 1, this.mSize - zzax);
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
        int zzax = zzax(i);
        return (zzax < 0 || this.zzbzo[zzax] == zzbzl) ? null : this.zzbzo[zzax];
    }

    final zzabg zzaw(int i) {
        return this.zzbzo[i];
    }
}
