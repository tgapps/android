package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhw implements Runnable {
    private final /* synthetic */ AtomicReference zzanv;
    private final /* synthetic */ zzhk zzanw;

    zzhw(zzhk com_google_android_gms_internal_measurement_zzhk, AtomicReference atomicReference) {
        this.zzanw = com_google_android_gms_internal_measurement_zzhk;
        this.zzanv = atomicReference;
    }

    public final void run() {
        synchronized (this.zzanv) {
            try {
                AtomicReference atomicReference = this.zzanv;
                zzhg zzgg = this.zzanw.zzgg();
                atomicReference.set(Long.valueOf(zzgg.zza(zzgg.zzfv().zzah(), zzew.zzahr)));
                this.zzanv.notify();
            } catch (Throwable th) {
                this.zzanv.notify();
            }
        }
    }
}
