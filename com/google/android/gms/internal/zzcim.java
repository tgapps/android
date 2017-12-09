package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzcim implements Runnable {
    private /* synthetic */ String zzbjh;
    private /* synthetic */ String zzbth;
    private /* synthetic */ String zzbti;
    private /* synthetic */ zzcid zzbua;
    private /* synthetic */ AtomicReference zzbub;

    zzcim(zzcid com_google_android_gms_internal_zzcid, AtomicReference atomicReference, String str, String str2, String str3) {
        this.zzbua = com_google_android_gms_internal_zzcid;
        this.zzbub = atomicReference;
        this.zzbjh = str;
        this.zzbth = str2;
        this.zzbti = str3;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r7 = this;
        r1 = r7.zzbub;
        monitor-enter(r1);
        r0 = r7.zzbua;	 Catch:{ RemoteException -> 0x007d }
        r0 = r0.zzbtU;	 Catch:{ RemoteException -> 0x007d }
        if (r0 != 0) goto L_0x0035;
    L_0x000b:
        r0 = r7.zzbua;	 Catch:{ RemoteException -> 0x007d }
        r0 = r0.zzwF();	 Catch:{ RemoteException -> 0x007d }
        r0 = r0.zzyx();	 Catch:{ RemoteException -> 0x007d }
        r2 = "Failed to get conditional properties";
        r3 = r7.zzbjh;	 Catch:{ RemoteException -> 0x007d }
        r3 = com.google.android.gms.internal.zzcfl.zzdZ(r3);	 Catch:{ RemoteException -> 0x007d }
        r4 = r7.zzbth;	 Catch:{ RemoteException -> 0x007d }
        r5 = r7.zzbti;	 Catch:{ RemoteException -> 0x007d }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x007d }
        r0 = r7.zzbub;	 Catch:{ RemoteException -> 0x007d }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x007d }
        r0.set(r2);	 Catch:{ RemoteException -> 0x007d }
        r0 = r7.zzbub;	 Catch:{ all -> 0x006a }
        r0.notify();	 Catch:{ all -> 0x006a }
        monitor-exit(r1);	 Catch:{ all -> 0x006a }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r7.zzbjh;	 Catch:{ RemoteException -> 0x007d }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x007d }
        if (r2 == 0) goto L_0x006d;
    L_0x003d:
        r2 = r7.zzbub;	 Catch:{ RemoteException -> 0x007d }
        r3 = r7.zzbth;	 Catch:{ RemoteException -> 0x007d }
        r4 = r7.zzbti;	 Catch:{ RemoteException -> 0x007d }
        r5 = r7.zzbua;	 Catch:{ RemoteException -> 0x007d }
        r5 = r5.zzwu();	 Catch:{ RemoteException -> 0x007d }
        r6 = r7.zzbua;	 Catch:{ RemoteException -> 0x007d }
        r6 = r6.zzwF();	 Catch:{ RemoteException -> 0x007d }
        r6 = r6.zzyE();	 Catch:{ RemoteException -> 0x007d }
        r5 = r5.zzdV(r6);	 Catch:{ RemoteException -> 0x007d }
        r0 = r0.zza(r3, r4, r5);	 Catch:{ RemoteException -> 0x007d }
        r2.set(r0);	 Catch:{ RemoteException -> 0x007d }
    L_0x005e:
        r0 = r7.zzbua;	 Catch:{ RemoteException -> 0x007d }
        r0.zzkP();	 Catch:{ RemoteException -> 0x007d }
        r0 = r7.zzbub;	 Catch:{ all -> 0x006a }
        r0.notify();	 Catch:{ all -> 0x006a }
    L_0x0068:
        monitor-exit(r1);	 Catch:{ all -> 0x006a }
        goto L_0x0034;
    L_0x006a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x006a }
        throw r0;
    L_0x006d:
        r2 = r7.zzbub;	 Catch:{ RemoteException -> 0x007d }
        r3 = r7.zzbjh;	 Catch:{ RemoteException -> 0x007d }
        r4 = r7.zzbth;	 Catch:{ RemoteException -> 0x007d }
        r5 = r7.zzbti;	 Catch:{ RemoteException -> 0x007d }
        r0 = r0.zzk(r3, r4, r5);	 Catch:{ RemoteException -> 0x007d }
        r2.set(r0);	 Catch:{ RemoteException -> 0x007d }
        goto L_0x005e;
    L_0x007d:
        r0 = move-exception;
        r2 = r7.zzbua;	 Catch:{ all -> 0x00a5 }
        r2 = r2.zzwF();	 Catch:{ all -> 0x00a5 }
        r2 = r2.zzyx();	 Catch:{ all -> 0x00a5 }
        r3 = "Failed to get conditional properties";
        r4 = r7.zzbjh;	 Catch:{ all -> 0x00a5 }
        r4 = com.google.android.gms.internal.zzcfl.zzdZ(r4);	 Catch:{ all -> 0x00a5 }
        r5 = r7.zzbth;	 Catch:{ all -> 0x00a5 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x00a5 }
        r0 = r7.zzbub;	 Catch:{ all -> 0x00a5 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x00a5 }
        r0.set(r2);	 Catch:{ all -> 0x00a5 }
        r0 = r7.zzbub;	 Catch:{ all -> 0x006a }
        r0.notify();	 Catch:{ all -> 0x006a }
        goto L_0x0068;
    L_0x00a5:
        r0 = move-exception;
        r2 = r7.zzbub;	 Catch:{ all -> 0x006a }
        r2.notify();	 Catch:{ all -> 0x006a }
        throw r0;	 Catch:{ all -> 0x006a }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcim.run():void");
    }
}
