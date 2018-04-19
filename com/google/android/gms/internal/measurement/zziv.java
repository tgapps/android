package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zziv implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ String zzaoa;
    private final /* synthetic */ String zzaob;
    private final /* synthetic */ String zzaoc;
    private final /* synthetic */ zzil zzapy;
    private final /* synthetic */ AtomicReference zzapz;

    zziv(zzil com_google_android_gms_internal_measurement_zzil, AtomicReference atomicReference, String str, String str2, String str3, zzec com_google_android_gms_internal_measurement_zzec) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzapz = atomicReference;
        this.zzaoc = str;
        this.zzaoa = str2;
        this.zzaob = str3;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r6 = this;
        r1 = r6.zzapz;
        monitor-enter(r1);
        r0 = r6.zzapy;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzaps;	 Catch:{ RemoteException -> 0x006b }
        if (r0 != 0) goto L_0x0035;
    L_0x000b:
        r0 = r6.zzapy;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzgg();	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzil();	 Catch:{ RemoteException -> 0x006b }
        r2 = "Failed to get conditional properties";
        r3 = r6.zzaoc;	 Catch:{ RemoteException -> 0x006b }
        r3 = com.google.android.gms.internal.measurement.zzfg.zzbh(r3);	 Catch:{ RemoteException -> 0x006b }
        r4 = r6.zzaoa;	 Catch:{ RemoteException -> 0x006b }
        r5 = r6.zzaob;	 Catch:{ RemoteException -> 0x006b }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x006b }
        r0 = r6.zzapz;	 Catch:{ RemoteException -> 0x006b }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x006b }
        r0.set(r2);	 Catch:{ RemoteException -> 0x006b }
        r0 = r6.zzapz;	 Catch:{ all -> 0x0058 }
        r0.notify();	 Catch:{ all -> 0x0058 }
        monitor-exit(r1);	 Catch:{ all -> 0x0058 }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r6.zzaoc;	 Catch:{ RemoteException -> 0x006b }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x006b }
        if (r2 == 0) goto L_0x005b;
    L_0x003d:
        r2 = r6.zzapz;	 Catch:{ RemoteException -> 0x006b }
        r3 = r6.zzaoa;	 Catch:{ RemoteException -> 0x006b }
        r4 = r6.zzaob;	 Catch:{ RemoteException -> 0x006b }
        r5 = r6.zzanq;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zza(r3, r4, r5);	 Catch:{ RemoteException -> 0x006b }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006b }
    L_0x004c:
        r0 = r6.zzapy;	 Catch:{ RemoteException -> 0x006b }
        r0.zzcu();	 Catch:{ RemoteException -> 0x006b }
        r0 = r6.zzapz;	 Catch:{ all -> 0x0058 }
        r0.notify();	 Catch:{ all -> 0x0058 }
    L_0x0056:
        monitor-exit(r1);	 Catch:{ all -> 0x0058 }
        goto L_0x0034;
    L_0x0058:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0058 }
        throw r0;
    L_0x005b:
        r2 = r6.zzapz;	 Catch:{ RemoteException -> 0x006b }
        r3 = r6.zzaoc;	 Catch:{ RemoteException -> 0x006b }
        r4 = r6.zzaoa;	 Catch:{ RemoteException -> 0x006b }
        r5 = r6.zzaob;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zze(r3, r4, r5);	 Catch:{ RemoteException -> 0x006b }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006b }
        goto L_0x004c;
    L_0x006b:
        r0 = move-exception;
        r2 = r6.zzapy;	 Catch:{ all -> 0x0093 }
        r2 = r2.zzgg();	 Catch:{ all -> 0x0093 }
        r2 = r2.zzil();	 Catch:{ all -> 0x0093 }
        r3 = "Failed to get conditional properties";
        r4 = r6.zzaoc;	 Catch:{ all -> 0x0093 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbh(r4);	 Catch:{ all -> 0x0093 }
        r5 = r6.zzaoa;	 Catch:{ all -> 0x0093 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x0093 }
        r0 = r6.zzapz;	 Catch:{ all -> 0x0093 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0093 }
        r0.set(r2);	 Catch:{ all -> 0x0093 }
        r0 = r6.zzapz;	 Catch:{ all -> 0x0058 }
        r0.notify();	 Catch:{ all -> 0x0058 }
        goto L_0x0056;
    L_0x0093:
        r0 = move-exception;
        r2 = r6.zzapz;	 Catch:{ all -> 0x0058 }
        r2.notify();	 Catch:{ all -> 0x0058 }
        throw r0;	 Catch:{ all -> 0x0058 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zziv.run():void");
    }
}
