package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhp implements Runnable {
    private final /* synthetic */ AtomicReference zzaoo;
    private final /* synthetic */ zzhm zzaop;
    private final /* synthetic */ boolean zzaos;

    zzhp(zzhm com_google_android_gms_internal_measurement_zzhm, AtomicReference atomicReference, boolean z) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaoo = atomicReference;
        this.zzaos = z;
    }

    public final void run() {
        this.zzaop.zzfx().zza(this.zzaoo, this.zzaos);
    }
}
