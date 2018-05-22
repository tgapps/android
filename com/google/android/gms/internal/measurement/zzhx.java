package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhx implements Runnable {
    private final /* synthetic */ AtomicReference zzanv;
    private final /* synthetic */ zzhk zzanw;

    zzhx(zzhk com_google_android_gms_internal_measurement_zzhk, AtomicReference atomicReference) {
        this.zzanw = com_google_android_gms_internal_measurement_zzhk;
        this.zzanv = atomicReference;
    }

    public final void run() {
        synchronized (this.zzanv) {
            try {
                AtomicReference atomicReference = this.zzanv;
                zzhg zzgg = this.zzanw.zzgg();
                atomicReference.set(Integer.valueOf(zzgg.zzb(zzgg.zzfv().zzah(), zzew.zzahs)));
                this.zzanv.notify();
            } catch (Throwable th) {
                this.zzanv.notify();
            }
        }
    }
}
