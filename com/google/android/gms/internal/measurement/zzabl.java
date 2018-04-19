package com.google.android.gms.internal.measurement;

import java.util.Arrays;

final class zzabl {
    final int tag;
    final byte[] zzbto;

    zzabl(int i, byte[] bArr) {
        this.tag = i;
        this.zzbto = bArr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzabl)) {
            return false;
        }
        zzabl com_google_android_gms_internal_measurement_zzabl = (zzabl) obj;
        return this.tag == com_google_android_gms_internal_measurement_zzabl.tag && Arrays.equals(this.zzbto, com_google_android_gms_internal_measurement_zzabl.zzbto);
    }

    public final int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzbto);
    }
}
