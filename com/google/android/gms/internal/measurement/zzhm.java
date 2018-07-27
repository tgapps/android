package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhm implements Runnable {
    private final /* synthetic */ AtomicReference zzaof;
    private final /* synthetic */ zzhl zzaog;

    zzhm(zzhl com_google_android_gms_internal_measurement_zzhl, AtomicReference atomicReference) {
        this.zzaog = com_google_android_gms_internal_measurement_zzhl;
        this.zzaof = atomicReference;
    }

    public final void run() {
        synchronized (this.zzaof) {
            try {
                this.zzaof.set(Boolean.valueOf(this.zzaog.zzgh().zzhp()));
                this.zzaof.notify();
            } catch (Throwable th) {
                this.zzaof.notify();
            }
        }
    }
}
