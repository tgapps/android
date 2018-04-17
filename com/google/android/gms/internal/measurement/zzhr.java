package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhr implements Runnable {
    private final /* synthetic */ AtomicReference zzaoo;
    private final /* synthetic */ zzhm zzaop;

    zzhr(zzhm com_google_android_gms_internal_measurement_zzhm, AtomicReference atomicReference) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaoo = atomicReference;
    }

    public final void run() {
        this.zzaop.zzfx().zza(this.zzaoo);
    }
}
