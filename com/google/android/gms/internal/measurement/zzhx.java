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
            Object obj;
            AtomicReference atomicReference = this.zzaoo;
            zzhj zzgi = this.zzaop.zzgi();
            String zzah = zzgi.zzfv().zzah();
            zzex com_google_android_gms_internal_measurement_zzex = zzew.zzaht;
            if (zzah == null) {
                obj = (String) com_google_android_gms_internal_measurement_zzex.get();
            } else {
                String str = (String) com_google_android_gms_internal_measurement_zzex.get(zzgi.zzgd().zzm(zzah, com_google_android_gms_internal_measurement_zzex.getKey()));
            }
            atomicReference.set(obj);
        } finally {
            this.zzaoo.notify();
        }
    }
}
