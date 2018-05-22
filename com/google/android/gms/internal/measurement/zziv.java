package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zziv implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ boolean zzanz;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ AtomicReference zzapf;

    zziv(zzii com_google_android_gms_internal_measurement_zzii, AtomicReference atomicReference, zzdz com_google_android_gms_internal_measurement_zzdz, boolean z) {
        this.zzape = com_google_android_gms_internal_measurement_zzii;
        this.zzapf = atomicReference;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
        this.zzanz = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r5 = this;
        r1 = r5.zzapf;
        monitor-enter(r1);
        r0 = r5.zzape;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzaoy;	 Catch:{ RemoteException -> 0x003e }
        if (r0 != 0) goto L_0x0022;
    L_0x000b:
        r0 = r5.zzape;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzge();	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzim();	 Catch:{ RemoteException -> 0x003e }
        r2 = "Failed to get user properties";
        r0.log(r2);	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzapf;	 Catch:{ all -> 0x003b }
        r0.notify();	 Catch:{ all -> 0x003b }
        monitor-exit(r1);	 Catch:{ all -> 0x003b }
    L_0x0021:
        return;
    L_0x0022:
        r2 = r5.zzapf;	 Catch:{ RemoteException -> 0x003e }
        r3 = r5.zzane;	 Catch:{ RemoteException -> 0x003e }
        r4 = r5.zzanz;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zza(r3, r4);	 Catch:{ RemoteException -> 0x003e }
        r2.set(r0);	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzape;	 Catch:{ RemoteException -> 0x003e }
        r0.zzcu();	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzapf;	 Catch:{ all -> 0x003b }
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
        r2 = r5.zzape;	 Catch:{ all -> 0x0055 }
        r2 = r2.zzge();	 Catch:{ all -> 0x0055 }
        r2 = r2.zzim();	 Catch:{ all -> 0x0055 }
        r3 = "Failed to get user properties";
        r2.zzg(r3, r0);	 Catch:{ all -> 0x0055 }
        r0 = r5.zzapf;	 Catch:{ all -> 0x003b }
        r0.notify();	 Catch:{ all -> 0x003b }
        goto L_0x0039;
    L_0x0055:
        r0 = move-exception;
        r2 = r5.zzapf;	 Catch:{ all -> 0x003b }
        r2.notify();	 Catch:{ all -> 0x003b }
        throw r0;	 Catch:{ all -> 0x003b }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zziv.run():void");
    }
}
