package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zziw implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ String zzaoa;
    private final /* synthetic */ String zzaob;
    private final /* synthetic */ String zzaoc;
    private final /* synthetic */ boolean zzaos;
    private final /* synthetic */ zzil zzapy;
    private final /* synthetic */ AtomicReference zzapz;

    zziw(zzil com_google_android_gms_internal_measurement_zzil, AtomicReference atomicReference, String str, String str2, String str3, boolean z, zzec com_google_android_gms_internal_measurement_zzec) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzapz = atomicReference;
        this.zzaoc = str;
        this.zzaoa = str2;
        this.zzaob = str3;
        this.zzaos = z;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r7 = this;
        r1 = r7.zzapz;
        monitor-enter(r1);
        r0 = r7.zzapy;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzaps;	 Catch:{ RemoteException -> 0x006f }
        if (r0 != 0) goto L_0x0035;
    L_0x000b:
        r0 = r7.zzapy;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzgg();	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzil();	 Catch:{ RemoteException -> 0x006f }
        r2 = "Failed to get user properties";
        r3 = r7.zzaoc;	 Catch:{ RemoteException -> 0x006f }
        r3 = com.google.android.gms.internal.measurement.zzfg.zzbh(r3);	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzaoa;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzaob;	 Catch:{ RemoteException -> 0x006f }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzapz;	 Catch:{ RemoteException -> 0x006f }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x006f }
        r0.set(r2);	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzapz;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r7.zzaoc;	 Catch:{ RemoteException -> 0x006f }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x006f }
        if (r2 == 0) goto L_0x005d;
    L_0x003d:
        r2 = r7.zzapz;	 Catch:{ RemoteException -> 0x006f }
        r3 = r7.zzaoa;	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzaob;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzaos;	 Catch:{ RemoteException -> 0x006f }
        r6 = r7.zzanq;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006f }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006f }
    L_0x004e:
        r0 = r7.zzapy;	 Catch:{ RemoteException -> 0x006f }
        r0.zzcu();	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzapz;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
    L_0x0058:
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
        goto L_0x0034;
    L_0x005a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
        throw r0;
    L_0x005d:
        r2 = r7.zzapz;	 Catch:{ RemoteException -> 0x006f }
        r3 = r7.zzaoc;	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzaoa;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzaob;	 Catch:{ RemoteException -> 0x006f }
        r6 = r7.zzaos;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006f }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006f }
        goto L_0x004e;
    L_0x006f:
        r0 = move-exception;
        r2 = r7.zzapy;	 Catch:{ all -> 0x0097 }
        r2 = r2.zzgg();	 Catch:{ all -> 0x0097 }
        r2 = r2.zzil();	 Catch:{ all -> 0x0097 }
        r3 = "Failed to get user properties";
        r4 = r7.zzaoc;	 Catch:{ all -> 0x0097 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x0097 }
        r5 = r7.zzaoa;	 Catch:{ all -> 0x0097 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x0097 }
        r0 = r7.zzapz;	 Catch:{ all -> 0x0097 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0097 }
        r0.set(r2);	 Catch:{ all -> 0x0097 }
        r0 = r7.zzapz;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
        goto L_0x0058;
    L_0x0097:
        r0 = move-exception;
        r2 = r7.zzapz;	 Catch:{ all -> 0x005a }
        r2.notify();	 Catch:{ all -> 0x005a }
        throw r0;	 Catch:{ all -> 0x005a }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zziw.run():void");
    }
}
