package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhx implements Runnable {
    private final /* synthetic */ AtomicReference zzaof;
    private final /* synthetic */ zzhl zzaog;

    zzhx(zzhl com_google_android_gms_internal_measurement_zzhl, AtomicReference atomicReference) {
        this.zzaog = com_google_android_gms_internal_measurement_zzhl;
        this.zzaof = atomicReference;
    }

    public final void run() {
        synchronized (this.zzaof) {
            try {
                AtomicReference atomicReference = this.zzaof;
                zzhh zzgh = this.zzaog.zzgh();
                atomicReference.set(Long.valueOf(zzgh.zza(zzgh.zzfw().zzah(), zzey.zzahy)));
                this.zzaof.notify();
            } catch (Throwable th) {
                this.zzaof.notify();
            }
        }
    }
}
