package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzcin implements Runnable {
    private /* synthetic */ String zzbjh;
    private /* synthetic */ String zzbth;
    private /* synthetic */ String zzbti;
    private /* synthetic */ boolean zzbtw;
    private /* synthetic */ zzcid zzbua;
    private /* synthetic */ AtomicReference zzbub;

    zzcin(zzcid com_google_android_gms_internal_zzcid, AtomicReference atomicReference, String str, String str2, String str3, boolean z) {
        this.zzbua = com_google_android_gms_internal_zzcid;
        this.zzbub = atomicReference;
        this.zzbjh = str;
        this.zzbth = str2;
        this.zzbti = str3;
        this.zzbtw = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r8 = this;
        r1 = r8.zzbub;
        monitor-enter(r1);
        r0 = r8.zzbua;	 Catch:{ RemoteException -> 0x0081 }
        r0 = r0.zzbtU;	 Catch:{ RemoteException -> 0x0081 }
        if (r0 != 0) goto L_0x0035;
    L_0x000b:
        r0 = r8.zzbua;	 Catch:{ RemoteException -> 0x0081 }
        r0 = r0.zzwF();	 Catch:{ RemoteException -> 0x0081 }
        r0 = r0.zzyx();	 Catch:{ RemoteException -> 0x0081 }
        r2 = "Failed to get user properties";
        r3 = r8.zzbjh;	 Catch:{ RemoteException -> 0x0081 }
        r3 = com.google.android.gms.internal.zzcfl.zzdZ(r3);	 Catch:{ RemoteException -> 0x0081 }
        r4 = r8.zzbth;	 Catch:{ RemoteException -> 0x0081 }
        r5 = r8.zzbti;	 Catch:{ RemoteException -> 0x0081 }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x0081 }
        r0 = r8.zzbub;	 Catch:{ RemoteException -> 0x0081 }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x0081 }
        r0.set(r2);	 Catch:{ RemoteException -> 0x0081 }
        r0 = r8.zzbub;	 Catch:{ all -> 0x006c }
        r0.notify();	 Catch:{ all -> 0x006c }
        monitor-exit(r1);	 Catch:{ all -> 0x006c }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r8.zzbjh;	 Catch:{ RemoteException -> 0x0081 }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x0081 }
        if (r2 == 0) goto L_0x006f;
    L_0x003d:
        r2 = r8.zzbub;	 Catch:{ RemoteException -> 0x0081 }
        r3 = r8.zzbth;	 Catch:{ RemoteException -> 0x0081 }
        r4 = r8.zzbti;	 Catch:{ RemoteException -> 0x0081 }
        r5 = r8.zzbtw;	 Catch:{ RemoteException -> 0x0081 }
        r6 = r8.zzbua;	 Catch:{ RemoteException -> 0x0081 }
        r6 = r6.zzwu();	 Catch:{ RemoteException -> 0x0081 }
        r7 = r8.zzbua;	 Catch:{ RemoteException -> 0x0081 }
        r7 = r7.zzwF();	 Catch:{ RemoteException -> 0x0081 }
        r7 = r7.zzyE();	 Catch:{ RemoteException -> 0x0081 }
        r6 = r6.zzdV(r7);	 Catch:{ RemoteException -> 0x0081 }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x0081 }
        r2.set(r0);	 Catch:{ RemoteException -> 0x0081 }
    L_0x0060:
        r0 = r8.zzbua;	 Catch:{ RemoteException -> 0x0081 }
        r0.zzkP();	 Catch:{ RemoteException -> 0x0081 }
        r0 = r8.zzbub;	 Catch:{ all -> 0x006c }
        r0.notify();	 Catch:{ all -> 0x006c }
    L_0x006a:
        monitor-exit(r1);	 Catch:{ all -> 0x006c }
        goto L_0x0034;
    L_0x006c:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x006c }
        throw r0;
    L_0x006f:
        r2 = r8.zzbub;	 Catch:{ RemoteException -> 0x0081 }
        r3 = r8.zzbjh;	 Catch:{ RemoteException -> 0x0081 }
        r4 = r8.zzbth;	 Catch:{ RemoteException -> 0x0081 }
        r5 = r8.zzbti;	 Catch:{ RemoteException -> 0x0081 }
        r6 = r8.zzbtw;	 Catch:{ RemoteException -> 0x0081 }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x0081 }
        r2.set(r0);	 Catch:{ RemoteException -> 0x0081 }
        goto L_0x0060;
    L_0x0081:
        r0 = move-exception;
        r2 = r8.zzbua;	 Catch:{ all -> 0x00a9 }
        r2 = r2.zzwF();	 Catch:{ all -> 0x00a9 }
        r2 = r2.zzyx();	 Catch:{ all -> 0x00a9 }
        r3 = "Failed to get user properties";
        r4 = r8.zzbjh;	 Catch:{ all -> 0x00a9 }
        r4 = com.google.android.gms.internal.zzcfl.zzdZ(r4);	 Catch:{ all -> 0x00a9 }
        r5 = r8.zzbth;	 Catch:{ all -> 0x00a9 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x00a9 }
        r0 = r8.zzbub;	 Catch:{ all -> 0x00a9 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x00a9 }
        r0.set(r2);	 Catch:{ all -> 0x00a9 }
        r0 = r8.zzbub;	 Catch:{ all -> 0x006c }
        r0.notify();	 Catch:{ all -> 0x006c }
        goto L_0x006a;
    L_0x00a9:
        r0 = move-exception;
        r2 = r8.zzbub;	 Catch:{ all -> 0x006c }
        r2.notify();	 Catch:{ all -> 0x006c }
        throw r0;	 Catch:{ all -> 0x006c }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcin.run():void");
    }
}
