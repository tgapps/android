package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhz implements Runnable {
    private final /* synthetic */ AtomicReference zzaoo;
    private final /* synthetic */ zzhm zzaop;

    zzhz(zzhm com_google_android_gms_internal_measurement_zzhm, AtomicReference atomicReference) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaoo = atomicReference;
    }

    public final void run() {
        try {
            AtomicReference atomicReference = this.zzaoo;
            zzhj zzgi = this.zzaop.zzgi();
            atomicReference.set(Integer.valueOf(zzgi.zzb(zzgi.zzfv().zzah(), zzew.zzahv)));
        } finally {
            this.zzaoo.notify();
        }
    }
}
