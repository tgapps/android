package com.google.android.gms.internal.measurement;

public final class zzzc implements Cloneable {
    private static final zzzd zzcff = new zzzd();
    private int mSize;
    private boolean zzcfg;
    private int[] zzcfh;
    private zzzd[] zzcfi;

    zzzc() {
        this(10);
    }

    private zzzc(int i) {
        this.zzcfg = false;
        int idealIntArraySize = idealIntArraySize(i);
        this.zzcfh = new int[idealIntArraySize];
        this.zzcfi = new zzzd[idealIntArraySize];
        this.mSize = 0;
    }

    final zzzd zzcb(int i) {
        int zzcd = zzcd(i);
        if (zzcd < 0 || this.zzcfi[zzcd] == zzcff) {
            return null;
        }
        return this.zzcfi[zzcd];
    }

    final void zza(int i, zzzd com_google_android_gms_internal_measurement_zzzd) {
        int zzcd = zzcd(i);
        if (zzcd >= 0) {
            this.zzcfi[zzcd] = com_google_android_gms_internal_measurement_zzzd;
            return;
        }
        zzcd ^= -1;
        if (zzcd >= this.mSize || this.zzcfi[zzcd] != zzcff) {
            if (this.mSize >= this.zzcfh.length) {
                int idealIntArraySize = idealIntArraySize(this.mSize + 1);
                Object obj = new int[idealIntArraySize];
                Object obj2 = new zzzd[idealIntArraySize];
                System.arraycopy(this.zzcfh, 0, obj, 0, this.zzcfh.length);
                System.arraycopy(this.zzcfi, 0, obj2, 0, this.zzcfi.length);
                this.zzcfh = obj;
                this.zzcfi = obj2;
            }
            if (this.mSize - zzcd != 0) {
                System.arraycopy(this.zzcfh, zzcd, this.zzcfh, zzcd + 1, this.mSize - zzcd);
                System.arraycopy(this.zzcfi, zzcd, this.zzcfi, zzcd + 1, this.mSize - zzcd);
            }
            this.zzcfh[zzcd] = i;
            this.zzcfi[zzcd] = com_google_android_gms_internal_measurement_zzzd;
            this.mSize++;
            return;
        }
        this.zzcfh[zzcd] = i;
        this.zzcfi[zzcd] = com_google_android_gms_internal_measurement_zzzd;
    }

    final int size() {
        return this.mSize;
    }

    public final boolean isEmpty() {
        return this.mSize == 0;
    }

    final zzzd zzcc(int i) {
        return this.zzcfi[i];
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzzc)) {
            return false;
        }
        zzzc com_google_android_gms_internal_measurement_zzzc = (zzzc) obj;
        if (this.mSize != com_google_android_gms_internal_measurement_zzzc.mSize) {
            return false;
        }
        int i;
        boolean z;
        int[] iArr = this.zzcfh;
        int[] iArr2 = com_google_android_gms_internal_measurement_zzzc.zzcfh;
        int i2 = this.mSize;
        for (i = 0; i < i2; i++) {
            if (iArr[i] != iArr2[i]) {
                z = false;
                break;
            }
        }
        z = true;
        if (z) {
            zzzd[] com_google_android_gms_internal_measurement_zzzdArr = this.zzcfi;
            zzzd[] com_google_android_gms_internal_measurement_zzzdArr2 = com_google_android_gms_internal_measurement_zzzc.zzcfi;
            i2 = this.mSize;
            for (i = 0; i < i2; i++) {
                if (!com_google_android_gms_internal_measurement_zzzdArr[i].equals(com_google_android_gms_internal_measurement_zzzdArr2[i])) {
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
            i = (((i * 31) + this.zzcfh[i2]) * 31) + this.zzcfi[i2].hashCode();
        }
        return i;
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

    private final int zzcd(int i) {
        int i2 = 0;
        int i3 = this.mSize - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) >>> 1;
            int i5 = this.zzcfh[i4];
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
        zzzc com_google_android_gms_internal_measurement_zzzc = new zzzc(i);
        System.arraycopy(this.zzcfh, 0, com_google_android_gms_internal_measurement_zzzc.zzcfh, 0, i);
        for (int i2 = 0; i2 < i; i2++) {
            if (this.zzcfi[i2] != null) {
                com_google_android_gms_internal_measurement_zzzc.zzcfi[i2] = (zzzd) this.zzcfi[i2].clone();
            }
        }
        com_google_android_gms_internal_measurement_zzzc.mSize = i;
        return com_google_android_gms_internal_measurement_zzzc;
    }
}
