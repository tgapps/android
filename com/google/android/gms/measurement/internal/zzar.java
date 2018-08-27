package com.google.android.gms.measurement.internal;

public final class zzar {
    private final int priority;
    private final /* synthetic */ zzap zzamm;
    private final boolean zzamn;
    private final boolean zzamo;

    zzar(zzap com_google_android_gms_measurement_internal_zzap, int i, boolean z, boolean z2) {
        this.zzamm = com_google_android_gms_measurement_internal_zzap;
        this.priority = i;
        this.zzamn = z;
        this.zzamo = z2;
    }

    public final void zzbx(String str) {
        this.zzamm.zza(this.priority, this.zzamn, this.zzamo, str, null, null, null);
    }

    public final void zzg(String str, Object obj) {
        this.zzamm.zza(this.priority, this.zzamn, this.zzamo, str, obj, null, null);
    }

    public final void zze(String str, Object obj, Object obj2) {
        this.zzamm.zza(this.priority, this.zzamn, this.zzamo, str, obj, obj2, null);
    }

    public final void zzd(String str, Object obj, Object obj2, Object obj3) {
        this.zzamm.zza(this.priority, this.zzamn, this.zzamo, str, obj, obj2, obj3);
    }
}
