package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzcip implements Runnable {
    private /* synthetic */ boolean zzbtw;
    private /* synthetic */ zzcid zzbua;
    private /* synthetic */ AtomicReference zzbub;

    zzcip(zzcid com_google_android_gms_internal_zzcid, AtomicReference atomicReference, boolean z) {
        this.zzbua = com_google_android_gms_internal_zzcid;
        this.zzbub = atomicReference;
        this.zzbtw = z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r5 = this;
        r1 = r5.zzbub;
        monitor-enter(r1);
        r0 = r5.zzbua;	 Catch:{ RemoteException -> 0x0047 }
        r0 = r0.zzbtU;	 Catch:{ RemoteException -> 0x0047 }
        if (r0 != 0) goto L_0x0022;
    L_0x000b:
        r0 = r5.zzbua;	 Catch:{ RemoteException -> 0x0047 }
        r0 = r0.zzwF();	 Catch:{ RemoteException -> 0x0047 }
        r0 = r0.zzyx();	 Catch:{ RemoteException -> 0x0047 }
        r2 = "Failed to get user properties";
        r0.log(r2);	 Catch:{ RemoteException -> 0x0047 }
        r0 = r5.zzbub;	 Catch:{ all -> 0x0044 }
        r0.notify();	 Catch:{ all -> 0x0044 }
        monitor-exit(r1);	 Catch:{ all -> 0x0044 }
    L_0x0021:
        return;
    L_0x0022:
        r2 = r5.zzbub;	 Catch:{ RemoteException -> 0x0047 }
        r3 = r5.zzbua;	 Catch:{ RemoteException -> 0x0047 }
        r3 = r3.zzwu();	 Catch:{ RemoteException -> 0x0047 }
        r4 = 0;
        r3 = r3.zzdV(r4);	 Catch:{ RemoteException -> 0x0047 }
        r4 = r5.zzbtw;	 Catch:{ RemoteException -> 0x0047 }
        r0 = r0.zza(r3, r4);	 Catch:{ RemoteException -> 0x0047 }
        r2.set(r0);	 Catch:{ RemoteException -> 0x0047 }
        r0 = r5.zzbua;	 Catch:{ RemoteException -> 0x0047 }
        r0.zzkP();	 Catch:{ RemoteException -> 0x0047 }
        r0 = r5.zzbub;	 Catch:{ all -> 0x0044 }
        r0.notify();	 Catch:{ all -> 0x0044 }
    L_0x0042:
        monitor-exit(r1);	 Catch:{ all -> 0x0044 }
        goto L_0x0021;
    L_0x0044:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0044 }
        throw r0;
    L_0x0047:
        r0 = move-exception;
        r2 = r5.zzbua;	 Catch:{ all -> 0x005e }
        r2 = r2.zzwF();	 Catch:{ all -> 0x005e }
        r2 = r2.zzyx();	 Catch:{ all -> 0x005e }
        r3 = "Failed to get user properties";
        r2.zzj(r3, r0);	 Catch:{ all -> 0x005e }
        r0 = r5.zzbub;	 Catch:{ all -> 0x0044 }
        r0.notify();	 Catch:{ all -> 0x0044 }
        goto L_0x0042;
    L_0x005e:
        r0 = move-exception;
        r2 = r5.zzbub;	 Catch:{ all -> 0x0044 }
        r2.notify();	 Catch:{ all -> 0x0044 }
        throw r0;	 Catch:{ all -> 0x0044 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcip.run():void");
    }
}
