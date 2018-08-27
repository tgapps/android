package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzct implements Runnable {
    private final /* synthetic */ AtomicReference zzarb;
    private final /* synthetic */ zzcs zzarc;

    public final void run() {
        synchronized (this.zzarb) {
            try {
                this.zzarb.set(Boolean.valueOf(this.zzarc.zzgq().zzba(this.zzarc.zzgf().zzal())));
                this.zzarb.notify();
            } catch (Throwable th) {
                this.zzarb.notify();
            }
        }
    }
}
