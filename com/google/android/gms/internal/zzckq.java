package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzckq implements Runnable {
    private /* synthetic */ String zzimf;
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ String zzjgq;
    private /* synthetic */ String zzjgr;
    private /* synthetic */ zzckg zzjij;
    private /* synthetic */ AtomicReference zzjik;

    zzckq(zzckg com_google_android_gms_internal_zzckg, AtomicReference atomicReference, String str, String str2, String str3, zzcgi com_google_android_gms_internal_zzcgi) {
        this.zzjij = com_google_android_gms_internal_zzckg;
        this.zzjik = atomicReference;
        this.zzimf = str;
        this.zzjgq = str2;
        this.zzjgr = str3;
        this.zzjgn = com_google_android_gms_internal_zzcgi;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r6 = this;
        r1 = r6.zzjik;
        monitor-enter(r1);
        r0 = r6.zzjij;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzjid;	 Catch:{ RemoteException -> 0x006b }
        if (r0 != 0) goto L_0x0035;
    L_0x000b:
        r0 = r6.zzjij;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzawy();	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzazd();	 Catch:{ RemoteException -> 0x006b }
        r2 = "Failed to get conditional properties";
        r3 = r6.zzimf;	 Catch:{ RemoteException -> 0x006b }
        r3 = com.google.android.gms.internal.zzchm.zzjk(r3);	 Catch:{ RemoteException -> 0x006b }
        r4 = r6.zzjgq;	 Catch:{ RemoteException -> 0x006b }
        r5 = r6.zzjgr;	 Catch:{ RemoteException -> 0x006b }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x006b }
        r0 = r6.zzjik;	 Catch:{ RemoteException -> 0x006b }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x006b }
        r0.set(r2);	 Catch:{ RemoteException -> 0x006b }
        r0 = r6.zzjik;	 Catch:{ all -> 0x0058 }
        r0.notify();	 Catch:{ all -> 0x0058 }
        monitor-exit(r1);	 Catch:{ all -> 0x0058 }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r6.zzimf;	 Catch:{ RemoteException -> 0x006b }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x006b }
        if (r2 == 0) goto L_0x005b;
    L_0x003d:
        r2 = r6.zzjik;	 Catch:{ RemoteException -> 0x006b }
        r3 = r6.zzjgq;	 Catch:{ RemoteException -> 0x006b }
        r4 = r6.zzjgr;	 Catch:{ RemoteException -> 0x006b }
        r5 = r6.zzjgn;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zza(r3, r4, r5);	 Catch:{ RemoteException -> 0x006b }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006b }
    L_0x004c:
        r0 = r6.zzjij;	 Catch:{ RemoteException -> 0x006b }
        r0.zzxr();	 Catch:{ RemoteException -> 0x006b }
        r0 = r6.zzjik;	 Catch:{ all -> 0x0058 }
        r0.notify();	 Catch:{ all -> 0x0058 }
    L_0x0056:
        monitor-exit(r1);	 Catch:{ all -> 0x0058 }
        goto L_0x0034;
    L_0x0058:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0058 }
        throw r0;
    L_0x005b:
        r2 = r6.zzjik;	 Catch:{ RemoteException -> 0x006b }
        r3 = r6.zzimf;	 Catch:{ RemoteException -> 0x006b }
        r4 = r6.zzjgq;	 Catch:{ RemoteException -> 0x006b }
        r5 = r6.zzjgr;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzj(r3, r4, r5);	 Catch:{ RemoteException -> 0x006b }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006b }
        goto L_0x004c;
    L_0x006b:
        r0 = move-exception;
        r2 = r6.zzjij;	 Catch:{ all -> 0x0093 }
        r2 = r2.zzawy();	 Catch:{ all -> 0x0093 }
        r2 = r2.zzazd();	 Catch:{ all -> 0x0093 }
        r3 = "Failed to get conditional properties";
        r4 = r6.zzimf;	 Catch:{ all -> 0x0093 }
        r4 = com.google.android.gms.internal.zzchm.zzjk(r4);	 Catch:{ all -> 0x0093 }
        r5 = r6.zzjgq;	 Catch:{ all -> 0x0093 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x0093 }
        r0 = r6.zzjik;	 Catch:{ all -> 0x0093 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0093 }
        r0.set(r2);	 Catch:{ all -> 0x0093 }
        r0 = r6.zzjik;	 Catch:{ all -> 0x0058 }
        r0.notify();	 Catch:{ all -> 0x0058 }
        goto L_0x0056;
    L_0x0093:
        r0 = move-exception;
        r2 = r6.zzjik;	 Catch:{ all -> 0x0058 }
        r2.notify();	 Catch:{ all -> 0x0058 }
        throw r0;	 Catch:{ all -> 0x0058 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzckq.run():void");
    }
}
