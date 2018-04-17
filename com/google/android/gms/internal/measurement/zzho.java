package com.google.android.gms.internal.measurement;

final class zzho implements Runnable {
    private final /* synthetic */ String val$name;
    private final /* synthetic */ String zzaoa;
    private final /* synthetic */ zzhm zzaop;
    private final /* synthetic */ Object zzaoq;
    private final /* synthetic */ long zzaor;

    zzho(zzhm com_google_android_gms_internal_measurement_zzhm, String str, String str2, Object obj, long j) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaoa = str;
        this.val$name = str2;
        this.zzaoq = obj;
        this.zzaor = j;
    }

    public final void run() {
        this.zzaop.zza(this.zzaoa, this.val$name, this.zzaoq, this.zzaor);
    }
}
