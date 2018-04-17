package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

final class zzio implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ zzil zzapy;
    private final /* synthetic */ AtomicReference zzapz;

    zzio(zzil com_google_android_gms_internal_measurement_zzil, AtomicReference atomicReference, zzec com_google_android_gms_internal_measurement_zzec) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzapz = atomicReference;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
    }

    public final void run() {
        synchronized (this.zzapz) {
            Object obj;
            try {
                zzey zzd = this.zzapy.zzaps;
                if (zzd == null) {
                    this.zzapy.zzgg().zzil().log("Failed to get app instance id");
                    this.zzapz.notify();
                    return;
                }
                this.zzapz.set(zzd.zzc(this.zzanq));
                String str = (String) this.zzapz.get();
                if (str != null) {
                    this.zzapy.zzfu().zzbm(str);
                    this.zzapy.zzgh().zzaka.zzbn(str);
                }
                this.zzapy.zzcu();
                obj = this.zzapz;
                obj.notify();
            } catch (RemoteException e) {
                try {
                    this.zzapy.zzgg().zzil().zzg("Failed to get app instance id", e);
                    obj = this.zzapz;
                } catch (Throwable th) {
                    this.zzapz.notify();
                }
            }
        }
    }
}
