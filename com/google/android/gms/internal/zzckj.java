package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzckj implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzckg zzjij;
    private /* synthetic */ AtomicReference zzjik;

    zzckj(zzckg com_google_android_gms_internal_zzckg, AtomicReference atomicReference, zzcgi com_google_android_gms_internal_zzcgi) {
        this.zzjij = com_google_android_gms_internal_zzckg;
        this.zzjik = atomicReference;
        this.zzjgn = com_google_android_gms_internal_zzcgi;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r4 = this;
        r1 = r4.zzjik;
        monitor-enter(r1);
        r0 = r4.zzjij;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzjid;	 Catch:{ RemoteException -> 0x005a }
        if (r0 != 0) goto L_0x0022;
    L_0x000b:
        r0 = r4.zzjij;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzawy();	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzazd();	 Catch:{ RemoteException -> 0x005a }
        r2 = "Failed to get app instance id";
        r0.log(r2);	 Catch:{ RemoteException -> 0x005a }
        r0 = r4.zzjik;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
        monitor-exit(r1);	 Catch:{ all -> 0x0057 }
    L_0x0021:
        return;
    L_0x0022:
        r2 = r4.zzjik;	 Catch:{ RemoteException -> 0x005a }
        r3 = r4.zzjgn;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzc(r3);	 Catch:{ RemoteException -> 0x005a }
        r2.set(r0);	 Catch:{ RemoteException -> 0x005a }
        r0 = r4.zzjik;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.get();	 Catch:{ RemoteException -> 0x005a }
        r0 = (java.lang.String) r0;	 Catch:{ RemoteException -> 0x005a }
        if (r0 == 0) goto L_0x004b;
    L_0x0037:
        r2 = r4.zzjij;	 Catch:{ RemoteException -> 0x005a }
        r2 = r2.zzawm();	 Catch:{ RemoteException -> 0x005a }
        r2.zzjp(r0);	 Catch:{ RemoteException -> 0x005a }
        r2 = r4.zzjij;	 Catch:{ RemoteException -> 0x005a }
        r2 = r2.zzawz();	 Catch:{ RemoteException -> 0x005a }
        r2 = r2.zzjcx;	 Catch:{ RemoteException -> 0x005a }
        r2.zzjq(r0);	 Catch:{ RemoteException -> 0x005a }
    L_0x004b:
        r0 = r4.zzjij;	 Catch:{ RemoteException -> 0x005a }
        r0.zzxr();	 Catch:{ RemoteException -> 0x005a }
        r0 = r4.zzjik;	 Catch:{ all -> 0x0057 }
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
        r2 = r4.zzjij;	 Catch:{ all -> 0x0071 }
        r2 = r2.zzawy();	 Catch:{ all -> 0x0071 }
        r2 = r2.zzazd();	 Catch:{ all -> 0x0071 }
        r3 = "Failed to get app instance id";
        r2.zzj(r3, r0);	 Catch:{ all -> 0x0071 }
        r0 = r4.zzjik;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
        goto L_0x0055;
    L_0x0071:
        r0 = move-exception;
        r2 = r4.zzjik;	 Catch:{ all -> 0x0057 }
        r2.notify();	 Catch:{ all -> 0x0057 }
        throw r0;	 Catch:{ all -> 0x0057 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzckj.run():void");
    }
}
