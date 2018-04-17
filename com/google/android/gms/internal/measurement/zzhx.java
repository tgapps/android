package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhx implements Runnable {
    private final /* synthetic */ AtomicReference zzaoo;
    private final /* synthetic */ zzhm zzaop;

    zzhx(zzhm com_google_android_gms_internal_measurement_zzhm, AtomicReference atomicReference) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaoo = atomicReference;
    }

    public final void run() {
        try {
            AtomicReference atomicReference = this.zzaoo;
            zzhj zzgi = this.zzaop.zzgi();
            String zzah = zzgi.zzfv().zzah();
            zzex com_google_android_gms_internal_measurement_zzex = zzew.zzaht;
            atomicReference.set((String) (zzah == null ? com_google_android_gms_internal_measurement_zzex.get() : com_google_android_gms_internal_measurement_zzex.get(zzgi.zzgd().zzm(zzah, com_google_android_gms_internal_measurement_zzex.getKey()))));
        } finally {
            this.zzaoo.notify();
        }
    }
}
