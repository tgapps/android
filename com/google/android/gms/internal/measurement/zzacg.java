package com.google.android.gms.internal.measurement;

import java.util.Arrays;

final class zzacg {
    final int tag;
    final byte[] zzbrc;

    zzacg(int i, byte[] bArr) {
        this.tag = i;
        this.zzbrc = bArr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzacg)) {
            return false;
        }
        zzacg com_google_android_gms_internal_measurement_zzacg = (zzacg) obj;
        return this.tag == com_google_android_gms_internal_measurement_zzacg.tag && Arrays.equals(this.zzbrc, com_google_android_gms_internal_measurement_zzacg.zzbrc);
    }

    public final int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzbrc);
    }
}
