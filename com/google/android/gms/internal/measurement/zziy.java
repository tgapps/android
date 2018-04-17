package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

final class zziy implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ boolean zzaos;
    private final /* synthetic */ zzil zzapy;
    private final /* synthetic */ AtomicReference zzapz;

    zziy(zzil com_google_android_gms_internal_measurement_zzil, AtomicReference atomicReference, zzec com_google_android_gms_internal_measurement_zzec, boolean z) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzapz = atomicReference;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
        this.zzaos = z;
    }

    public final void run() {
        synchronized (this.zzapz) {
            Object obj;
            try {
                zzey zzd = this.zzapy.zzaps;
                if (zzd == null) {
                    this.zzapy.zzgg().zzil().log("Failed to get user properties");
                    this.zzapz.notify();
                    return;
                }
                this.zzapz.set(zzd.zza(this.zzanq, this.zzaos));
                this.zzapy.zzcu();
                obj = this.zzapz;
                obj.notify();
            } catch (RemoteException e) {
                try {
                    this.zzapy.zzgg().zzil().zzg("Failed to get user properties", e);
                    obj = this.zzapz;
                } catch (Throwable th) {
                    this.zzapz.notify();
                }
            }
        }
    }
}
