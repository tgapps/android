package com.google.android.gms.internal.measurement;

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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r5 = this;
        r1 = r5.zzapz;
        monitor-enter(r1);
        r0 = r5.zzapy;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzaps;	 Catch:{ RemoteException -> 0x003e }
        if (r0 != 0) goto L_0x0022;
    L_0x000b:
        r0 = r5.zzapy;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzgg();	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzil();	 Catch:{ RemoteException -> 0x003e }
        r2 = "Failed to get user properties";
        r0.log(r2);	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzapz;	 Catch:{ all -> 0x003b }
        r0.notify();	 Catch:{ all -> 0x003b }
        monitor-exit(r1);	 Catch:{ all -> 0x003b }
    L_0x0021:
        return;
    L_0x0022:
        r2 = r5.zzapz;	 Catch:{ RemoteException -> 0x003e }
        r3 = r5.zzanq;	 Catch:{ RemoteException -> 0x003e }
        r4 = r5.zzaos;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zza(r3, r4);	 Catch:{ RemoteException -> 0x003e }
        r2.set(r0);	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzapy;	 Catch:{ RemoteException -> 0x003e }
        r0.zzcu();	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzapz;	 Catch:{ all -> 0x003b }
        r0.notify();	 Catch:{ all -> 0x003b }
    L_0x0039:
        monitor-exit(r1);	 Catch:{ all -> 0x003b }
        goto L_0x0021;
    L_0x003b:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x003b }
        throw r0;
    L_0x003e:
        r0 = move-exception;
        r2 = r5.zzapy;	 Catch:{ all -> 0x0055 }
        r2 = r2.zzgg();	 Catch:{ all -> 0x0055 }
        r2 = r2.zzil();	 Catch:{ all -> 0x0055 }
        r3 = "Failed to get user properties";
        r2.zzg(r3, r0);	 Catch:{ all -> 0x0055 }
        r0 = r5.zzapz;	 Catch:{ all -> 0x003b }
        r0.notify();	 Catch:{ all -> 0x003b }
        goto L_0x0039;
    L_0x0055:
        r0 = move-exception;
        r2 = r5.zzapz;	 Catch:{ all -> 0x003b }
        r2.notify();	 Catch:{ all -> 0x003b }
        throw r0;	 Catch:{ all -> 0x003b }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zziy.run():void");
    }
}
