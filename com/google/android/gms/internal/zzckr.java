package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzckr implements Runnable {
    private /* synthetic */ String zzimf;
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ String zzjgq;
    private /* synthetic */ String zzjgr;
    private /* synthetic */ boolean zzjhf;
    private /* synthetic */ zzckg zzjij;
    private /* synthetic */ AtomicReference zzjik;

    zzckr(zzckg com_google_android_gms_internal_zzckg, AtomicReference atomicReference, String str, String str2, String str3, boolean z, zzcgi com_google_android_gms_internal_zzcgi) {
        this.zzjij = com_google_android_gms_internal_zzckg;
        this.zzjik = atomicReference;
        this.zzimf = str;
        this.zzjgq = str2;
        this.zzjgr = str3;
        this.zzjhf = z;
        this.zzjgn = com_google_android_gms_internal_zzcgi;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r7 = this;
        r1 = r7.zzjik;
        monitor-enter(r1);
        r0 = r7.zzjij;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzjid;	 Catch:{ RemoteException -> 0x006f }
        if (r0 != 0) goto L_0x0035;
    L_0x000b:
        r0 = r7.zzjij;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzawy();	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzazd();	 Catch:{ RemoteException -> 0x006f }
        r2 = "Failed to get user properties";
        r3 = r7.zzimf;	 Catch:{ RemoteException -> 0x006f }
        r3 = com.google.android.gms.internal.zzchm.zzjk(r3);	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzjgq;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzjgr;	 Catch:{ RemoteException -> 0x006f }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzjik;	 Catch:{ RemoteException -> 0x006f }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x006f }
        r0.set(r2);	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzjik;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r7.zzimf;	 Catch:{ RemoteException -> 0x006f }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x006f }
        if (r2 == 0) goto L_0x005d;
    L_0x003d:
        r2 = r7.zzjik;	 Catch:{ RemoteException -> 0x006f }
        r3 = r7.zzjgq;	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzjgr;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzjhf;	 Catch:{ RemoteException -> 0x006f }
        r6 = r7.zzjgn;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006f }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006f }
    L_0x004e:
        r0 = r7.zzjij;	 Catch:{ RemoteException -> 0x006f }
        r0.zzxr();	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzjik;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
    L_0x0058:
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
        goto L_0x0034;
    L_0x005a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
        throw r0;
    L_0x005d:
        r2 = r7.zzjik;	 Catch:{ RemoteException -> 0x006f }
        r3 = r7.zzimf;	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzjgq;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzjgr;	 Catch:{ RemoteException -> 0x006f }
        r6 = r7.zzjhf;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006f }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006f }
        goto L_0x004e;
    L_0x006f:
        r0 = move-exception;
        r2 = r7.zzjij;	 Catch:{ all -> 0x0097 }
        r2 = r2.zzawy();	 Catch:{ all -> 0x0097 }
        r2 = r2.zzazd();	 Catch:{ all -> 0x0097 }
        r3 = "Failed to get user properties";
        r4 = r7.zzimf;	 Catch:{ all -> 0x0097 }
        r4 = com.google.android.gms.internal.zzchm.zzjk(r4);	 Catch:{ all -> 0x0097 }
        r5 = r7.zzjgq;	 Catch:{ all -> 0x0097 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x0097 }
        r0 = r7.zzjik;	 Catch:{ all -> 0x0097 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0097 }
        r0.set(r2);	 Catch:{ all -> 0x0097 }
        r0 = r7.zzjik;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
        goto L_0x0058;
    L_0x0097:
        r0 = move-exception;
        r2 = r7.zzjik;	 Catch:{ all -> 0x005a }
        r2.notify();	 Catch:{ all -> 0x005a }
        throw r0;	 Catch:{ all -> 0x005a }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzckr.run():void");
    }
}
