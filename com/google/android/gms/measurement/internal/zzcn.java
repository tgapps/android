package com.google.android.gms.measurement.internal;

final class zzcn implements Runnable {
    private final /* synthetic */ String zzaeq;
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ String zzaqt;
    private final /* synthetic */ long zzaqu;

    zzcn(zzbv com_google_android_gms_measurement_internal_zzbv, String str, String str2, String str3, long j) {
        this.zzaqo = com_google_android_gms_measurement_internal_zzbv;
        this.zzaqt = str;
        this.zzaqq = str2;
        this.zzaeq = str3;
        this.zzaqu = j;
    }

    public final void run() {
        if (this.zzaqt == null) {
            this.zzaqo.zzamz.zzmb().zzgh().zza(this.zzaqq, null);
            return;
        }
        this.zzaqo.zzamz.zzmb().zzgh().zza(this.zzaqq, new zzdn(this.zzaeq, this.zzaqt, this.zzaqu));
    }
}
