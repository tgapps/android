package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import java.util.ArrayList;
import java.util.List;

final class zzjv implements zzek {
    private final /* synthetic */ zzjr zzaqu;
    zzkq zzaqv;
    List<Long> zzaqw;
    List<zzkn> zzaqx;
    private long zzaqy;

    private zzjv(zzjr com_google_android_gms_internal_measurement_zzjr) {
        this.zzaqu = com_google_android_gms_internal_measurement_zzjr;
    }

    private static long zza(zzkn com_google_android_gms_internal_measurement_zzkn) {
        return ((com_google_android_gms_internal_measurement_zzkn.zzatb.longValue() / 1000) / 60) / 60;
    }

    public final boolean zza(long j, zzkn com_google_android_gms_internal_measurement_zzkn) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkn);
        if (this.zzaqx == null) {
            this.zzaqx = new ArrayList();
        }
        if (this.zzaqw == null) {
            this.zzaqw = new ArrayList();
        }
        if (this.zzaqx.size() > 0 && zza((zzkn) this.zzaqx.get(0)) != zza(com_google_android_gms_internal_measurement_zzkn)) {
            return false;
        }
        long zzvm = this.zzaqy + ((long) com_google_android_gms_internal_measurement_zzkn.zzvm());
        if (zzvm >= ((long) Math.max(0, ((Integer) zzew.zzagq.get()).intValue()))) {
            return false;
        }
        this.zzaqy = zzvm;
        this.zzaqx.add(com_google_android_gms_internal_measurement_zzkn);
        this.zzaqw.add(Long.valueOf(j));
        return this.zzaqx.size() < Math.max(1, ((Integer) zzew.zzagr.get()).intValue());
    }

    public final void zzb(zzkq com_google_android_gms_internal_measurement_zzkq) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzkq);
        this.zzaqv = com_google_android_gms_internal_measurement_zzkq;
    }
}
