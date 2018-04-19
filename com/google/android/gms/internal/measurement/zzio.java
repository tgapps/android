package com.google.android.gms.internal.measurement;

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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r4 = this;
        r1 = r4.zzapz;
        monitor-enter(r1);
        r0 = r4.zzapy;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzaps;	 Catch:{ RemoteException -> 0x005a }
        if (r0 != 0) goto L_0x0022;
    L_0x000b:
        r0 = r4.zzapy;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzgg();	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzil();	 Catch:{ RemoteException -> 0x005a }
        r2 = "Failed to get app instance id";
        r0.log(r2);	 Catch:{ RemoteException -> 0x005a }
        r0 = r4.zzapz;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
        monitor-exit(r1);	 Catch:{ all -> 0x0057 }
    L_0x0021:
        return;
    L_0x0022:
        r2 = r4.zzapz;	 Catch:{ RemoteException -> 0x005a }
        r3 = r4.zzanq;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzc(r3);	 Catch:{ RemoteException -> 0x005a }
        r2.set(r0);	 Catch:{ RemoteException -> 0x005a }
        r0 = r4.zzapz;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.get();	 Catch:{ RemoteException -> 0x005a }
        r0 = (java.lang.String) r0;	 Catch:{ RemoteException -> 0x005a }
        if (r0 == 0) goto L_0x004b;
    L_0x0037:
        r2 = r4.zzapy;	 Catch:{ RemoteException -> 0x005a }
        r2 = r2.zzfu();	 Catch:{ RemoteException -> 0x005a }
        r2.zzbm(r0);	 Catch:{ RemoteException -> 0x005a }
        r2 = r4.zzapy;	 Catch:{ RemoteException -> 0x005a }
        r2 = r2.zzgh();	 Catch:{ RemoteException -> 0x005a }
        r2 = r2.zzaka;	 Catch:{ RemoteException -> 0x005a }
        r2.zzbn(r0);	 Catch:{ RemoteException -> 0x005a }
    L_0x004b:
        r0 = r4.zzapy;	 Catch:{ RemoteException -> 0x005a }
        r0.zzcu();	 Catch:{ RemoteException -> 0x005a }
        r0 = r4.zzapz;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
    L_0x0055:
        monitor-exit(r1);	 Catch:{ all -> 0x0057 }
        goto L_0x0021;
    L_0x0057:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0057 }
        throw r0;
    L_0x005a:
        r0 = move-exception;
        r2 = r4.zzapy;	 Catch:{ all -> 0x0071 }
        r2 = r2.zzgg();	 Catch:{ all -> 0x0071 }
        r2 = r2.zzil();	 Catch:{ all -> 0x0071 }
        r3 = "Failed to get app instance id";
        r2.zzg(r3, r0);	 Catch:{ all -> 0x0071 }
        r0 = r4.zzapz;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
        goto L_0x0055;
    L_0x0071:
        r0 = move-exception;
        r2 = r4.zzapz;	 Catch:{ all -> 0x0057 }
        r2.notify();	 Catch:{ all -> 0x0057 }
        throw r0;	 Catch:{ all -> 0x0057 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzio.run():void");
    }
}
