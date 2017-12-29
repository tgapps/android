package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzckt implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ boolean zzjhf;
    private /* synthetic */ zzckg zzjij;
    private /* synthetic */ AtomicReference zzjik;

    zzckt(zzckg com_google_android_gms_internal_zzckg, AtomicReference atomicReference, zzcgi com_google_android_gms_internal_zzcgi, boolean z) {
        this.zzjij = com_google_android_gms_internal_zzckg;
        this.zzjik = atomicReference;
        this.zzjgn = com_google_android_gms_internal_zzcgi;
        this.zzjhf = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r5 = this;
        r1 = r5.zzjik;
        monitor-enter(r1);
        r0 = r5.zzjij;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzjid;	 Catch:{ RemoteException -> 0x003e }
        if (r0 != 0) goto L_0x0022;
    L_0x000b:
        r0 = r5.zzjij;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzawy();	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzazd();	 Catch:{ RemoteException -> 0x003e }
        r2 = "Failed to get user properties";
        r0.log(r2);	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzjik;	 Catch:{ all -> 0x003b }
        r0.notify();	 Catch:{ all -> 0x003b }
        monitor-exit(r1);	 Catch:{ all -> 0x003b }
    L_0x0021:
        return;
    L_0x0022:
        r2 = r5.zzjik;	 Catch:{ RemoteException -> 0x003e }
        r3 = r5.zzjgn;	 Catch:{ RemoteException -> 0x003e }
        r4 = r5.zzjhf;	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zza(r3, r4);	 Catch:{ RemoteException -> 0x003e }
        r2.set(r0);	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzjij;	 Catch:{ RemoteException -> 0x003e }
        r0.zzxr();	 Catch:{ RemoteException -> 0x003e }
        r0 = r5.zzjik;	 Catch:{ all -> 0x003b }
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
        r2 = r5.zzjij;	 Catch:{ all -> 0x0055 }
        r2 = r2.zzawy();	 Catch:{ all -> 0x0055 }
        r2 = r2.zzazd();	 Catch:{ all -> 0x0055 }
        r3 = "Failed to get user properties";
        r2.zzj(r3, r0);	 Catch:{ all -> 0x0055 }
        r0 = r5.zzjik;	 Catch:{ all -> 0x003b }
        r0.notify();	 Catch:{ all -> 0x003b }
        goto L_0x0039;
    L_0x0055:
        r0 = move-exception;
        r2 = r5.zzjik;	 Catch:{ all -> 0x003b }
        r2.notify();	 Catch:{ all -> 0x003b }
        throw r0;	 Catch:{ all -> 0x003b }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzckt.run():void");
    }
}
