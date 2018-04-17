package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhn implements Runnable {
    private final /* synthetic */ AtomicReference zzaoo;
    private final /* synthetic */ zzhm zzaop;

    zzhn(zzhm com_google_android_gms_internal_measurement_zzhm, AtomicReference atomicReference) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaoo = atomicReference;
    }

    public final void run() {
        try {
            AtomicReference atomicReference = this.zzaoo;
            zzhj zzgi = this.zzaop.zzgi();
            atomicReference.set(Boolean.valueOf(zzgi.zzd(zzgi.zzfv().zzah(), zzew.zzahs)));
        } finally {
            this.zzaoo.notify();
        }
    }
}
