package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzee implements Runnable {
    private final /* synthetic */ boolean zzaev;
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ AtomicReference zzash;

    zzee(zzdr com_google_android_gms_measurement_internal_zzdr, AtomicReference atomicReference, zzh com_google_android_gms_measurement_internal_zzh, boolean z) {
        this.zzasg = com_google_android_gms_measurement_internal_zzdr;
        this.zzash = atomicReference;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
        this.zzaev = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r5 = this;
        r1 = r5.zzash;
        monitor-enter(r1);
        r0 = r5.zzasg;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzasa;	 Catch:{ RemoteException -> 0x003e }
        if (r0 != 0) goto L_0x0022;
    L_0x000b:
        r0 = r5.zzasg;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzgo();	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzjd();	 Catch:{ RemoteException -> 0x003e }
        r2 = "Failed to get user properties";
        r0.zzbx(r2);	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzash;	 Catch:{ all -> 0x003b }
        r0.notify();	 Catch:{ all -> 0x003b }
        monitor-exit(r1);	 Catch:{ all -> 0x003b }
    L_0x0021:
        return;
    L_0x0022:
        r2 = r5.zzash;	 Catch:{ RemoteException -> 0x003e }
        r3 = r5.zzaqn;	 Catch:{ RemoteException -> 0x003e }
        r4 = r5.zzaev;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zza(r3, r4);	 Catch:{ RemoteException -> 0x003e }
        r2.set(r0);	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzasg;	 Catch:{ RemoteException -> 0x003e }
        r0.zzcy();	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzash;	 Catch:{ all -> 0x003b }
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
        r2 = r5.zzasg;	 Catch:{ all -> 0x0055 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x0055 }
        r2 = r2.zzjd();	 Catch:{ all -> 0x0055 }
        r3 = "Failed to get user properties";
        r2.zzg(r3, r0);	 Catch:{ all -> 0x0055 }
        r0 = r5.zzash;	 Catch:{ all -> 0x003b }
        r0.notify();	 Catch:{ all -> 0x003b }
        goto L_0x0039;
    L_0x0055:
        r0 = move-exception;
        r2 = r5.zzash;	 Catch:{ all -> 0x003b }
        r2.notify();	 Catch:{ all -> 0x003b }
        throw r0;	 Catch:{ all -> 0x003b }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzee.run():void");
    }
}
