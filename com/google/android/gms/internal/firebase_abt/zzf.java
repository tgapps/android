package com.google.android.gms.internal.firebase_abt;

public final class zzf implements Cloneable {
    private static final zzg zzu = new zzg();
    private int mSize;
    private boolean zzv;
    private int[] zzw;
    private zzg[] zzx;

    zzf() {
        this(10);
    }

    private zzf(int i) {
        this.zzv = false;
        i = idealIntArraySize(i);
        this.zzw = new int[i];
        this.zzx = new zzg[i];
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

    private final int zzh(int i) {
        int i2 = this.mSize - 1;
        int i3 = 0;
        while (i3 <= i2) {
            int i4 = (i3 + i2) >>> 1;
            int i5 = this.zzw[i4];
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
        zzf com_google_android_gms_internal_firebase_abt_zzf = new zzf(i);
        int i2 = 0;
        System.arraycopy(this.zzw, 0, com_google_android_gms_internal_firebase_abt_zzf.zzw, 0, i);
        while (i2 < i) {
            if (this.zzx[i2] != null) {
                com_google_android_gms_internal_firebase_abt_zzf.zzx[i2] = (zzg) this.zzx[i2].clone();
            }
            i2++;
        }
        com_google_android_gms_internal_firebase_abt_zzf.mSize = i;
        return com_google_android_gms_internal_firebase_abt_zzf;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzf)) {
            return false;
        }
        zzf com_google_android_gms_internal_firebase_abt_zzf = (zzf) obj;
        if (this.mSize != com_google_android_gms_internal_firebase_abt_zzf.mSize) {
            return false;
        }
        boolean z;
        int[] iArr = this.zzw;
        int[] iArr2 = com_google_android_gms_internal_firebase_abt_zzf.zzw;
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
            zzg[] com_google_android_gms_internal_firebase_abt_zzgArr = this.zzx;
            zzg[] com_google_android_gms_internal_firebase_abt_zzgArr2 = com_google_android_gms_internal_firebase_abt_zzf.zzx;
            int i3 = this.mSize;
            for (i = 0; i < i3; i++) {
                if (!com_google_android_gms_internal_firebase_abt_zzgArr[i].equals(com_google_android_gms_internal_firebase_abt_zzgArr2[i])) {
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
            i = (((i * 31) + this.zzw[i2]) * 31) + this.zzx[i2].hashCode();
        }
        return i;
    }

    final void zza(int i, zzg com_google_android_gms_internal_firebase_abt_zzg) {
        int zzh = zzh(i);
        if (zzh >= 0) {
            this.zzx[zzh] = com_google_android_gms_internal_firebase_abt_zzg;
            return;
        }
        zzh ^= -1;
        if (zzh >= this.mSize || this.zzx[zzh] != zzu) {
            if (this.mSize >= this.zzw.length) {
                int idealIntArraySize = idealIntArraySize(this.mSize + 1);
                Object obj = new int[idealIntArraySize];
                Object obj2 = new zzg[idealIntArraySize];
                System.arraycopy(this.zzw, 0, obj, 0, this.zzw.length);
                System.arraycopy(this.zzx, 0, obj2, 0, this.zzx.length);
                this.zzw = obj;
                this.zzx = obj2;
            }
            if (this.mSize - zzh != 0) {
                int i2 = zzh + 1;
                System.arraycopy(this.zzw, zzh, this.zzw, i2, this.mSize - zzh);
                System.arraycopy(this.zzx, zzh, this.zzx, i2, this.mSize - zzh);
            }
            this.zzw[zzh] = i;
            this.zzx[zzh] = com_google_android_gms_internal_firebase_abt_zzg;
            this.mSize++;
            return;
        }
        this.zzw[zzh] = i;
        this.zzx[zzh] = com_google_android_gms_internal_firebase_abt_zzg;
    }

    final zzg zzg(int i) {
        i = zzh(i);
        if (i >= 0) {
            if (this.zzx[i] != zzu) {
                return this.zzx[i];
            }
        }
        return null;
    }
}
