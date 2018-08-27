package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzvm.zze;

final class zzxh implements zzwr {
    private final int flags;
    private final String info;
    private final Object[] zzcba;
    private final zzwt zzcbd;

    zzxh(zzwt com_google_android_gms_internal_measurement_zzwt, String str, Object[] objArr) {
        this.zzcbd = com_google_android_gms_internal_measurement_zzwt;
        this.info = str;
        this.zzcba = objArr;
        int i = 1;
        char charAt = str.charAt(0);
        if (charAt < '?') {
            this.flags = charAt;
            return;
        }
        int i2 = charAt & 8191;
        int i3 = 13;
        while (true) {
            int i4 = i + 1;
            char charAt2 = str.charAt(i);
            if (charAt2 >= '?') {
                i2 |= (charAt2 & 8191) << i3;
                i3 += 13;
                i = i4;
            } else {
                this.flags = (charAt2 << i3) | i2;
                return;
            }
        }
    }

    final String zzxp() {
        return this.info;
    }

    final Object[] zzxq() {
        return this.zzcba;
    }

    public final zzwt zzxi() {
        return this.zzcbd;
    }

    public final int zzxg() {
        return (this.flags & 1) == 1 ? zze.zzbzb : zze.zzbzc;
    }

    public final boolean zzxh() {
        return (this.flags & 2) == 2;
    }
}
