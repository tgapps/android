package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhl implements Runnable {
    private final /* synthetic */ AtomicReference zzanv;
    private final /* synthetic */ zzhk zzanw;

    zzhl(zzhk com_google_android_gms_internal_measurement_zzhk, AtomicReference atomicReference) {
        this.zzanw = com_google_android_gms_internal_measurement_zzhk;
        this.zzanv = atomicReference;
    }

    public final void run() {
        synchronized (this.zzanv) {
            try {
                this.zzanv.set(Boolean.valueOf(this.zzanw.zzgg().zzhl()));
                this.zzanv.notify();
            } catch (Throwable th) {
                this.zzanv.notify();
            }
        }
    }
}
