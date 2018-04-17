package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhv implements Runnable {
    private final /* synthetic */ String zzaoa;
    private final /* synthetic */ String zzaob;
    private final /* synthetic */ String zzaoc;
    private final /* synthetic */ AtomicReference zzaoo;
    private final /* synthetic */ zzhm zzaop;

    zzhv(zzhm com_google_android_gms_internal_measurement_zzhm, AtomicReference atomicReference, String str, String str2, String str3) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaoo = atomicReference;
        this.zzaoc = str;
        this.zzaoa = str2;
        this.zzaob = str3;
    }

    public final void run() {
        this.zzaop.zzacr.zzfx().zza(this.zzaoo, this.zzaoc, this.zzaoa, this.zzaob);
    }
}
