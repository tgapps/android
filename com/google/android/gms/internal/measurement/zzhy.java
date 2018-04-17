package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhy implements Runnable {
    private final /* synthetic */ AtomicReference zzaoo;
    private final /* synthetic */ zzhm zzaop;

    zzhy(zzhm com_google_android_gms_internal_measurement_zzhm, AtomicReference atomicReference) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaoo = atomicReference;
    }

    public final void run() {
        try {
            AtomicReference atomicReference = this.zzaoo;
            zzhj zzgi = this.zzaop.zzgi();
            atomicReference.set(Long.valueOf(zzgi.zza(zzgi.zzfv().zzah(), zzew.zzahu)));
        } finally {
            this.zzaoo.notify();
        }
    }
}
