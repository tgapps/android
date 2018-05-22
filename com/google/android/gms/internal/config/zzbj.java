package com.google.android.gms.internal.config;

import java.util.Arrays;

final class zzbj {
    final int tag;
    final byte[] zzcr;

    zzbj(int i, byte[] bArr) {
        this.tag = i;
        this.zzcr = bArr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzbj)) {
            return false;
        }
        zzbj com_google_android_gms_internal_config_zzbj = (zzbj) obj;
        return this.tag == com_google_android_gms_internal_config_zzbj.tag && Arrays.equals(this.zzcr, com_google_android_gms_internal_config_zzbj.zzcr);
    }

    public final int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzcr);
    }
}
