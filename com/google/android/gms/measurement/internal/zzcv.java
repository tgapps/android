package com.google.android.gms.measurement.internal;

final class zzcv implements Runnable {
    private final /* synthetic */ String val$name;
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ zzcs zzarc;
    private final /* synthetic */ long zzard;
    private final /* synthetic */ Object zzarh;

    zzcv(zzcs com_google_android_gms_measurement_internal_zzcs, String str, String str2, Object obj, long j) {
        this.zzarc = com_google_android_gms_measurement_internal_zzcs;
        this.zzaeh = str;
        this.val$name = str2;
        this.zzarh = obj;
        this.zzard = j;
    }

    public final void run() {
        this.zzarc.zza(this.zzaeh, this.val$name, this.zzarh, this.zzard);
    }
}
