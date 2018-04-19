package com.google.android.gms.internal.config;

import java.util.Arrays;

final class zzbj {
    final int tag;
    final byte[] zzcs;

    zzbj(int i, byte[] bArr) {
        this.tag = i;
        this.zzcs = bArr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzbj)) {
            return false;
        }
        zzbj com_google_android_gms_internal_config_zzbj = (zzbj) obj;
        return this.tag == com_google_android_gms_internal_config_zzbj.tag && Arrays.equals(this.zzcs, com_google_android_gms_internal_config_zzbj.zzcs);
    }

    public final int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzcs);
    }
}
