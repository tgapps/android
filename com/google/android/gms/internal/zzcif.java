package com.google.android.gms.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzcif implements Runnable {
    private /* synthetic */ zzcid zzbua;
    private /* synthetic */ AtomicReference zzbub;

    zzcif(zzcid com_google_android_gms_internal_zzcid, AtomicReference atomicReference) {
        this.zzbua = com_google_android_gms_internal_zzcid;
        this.zzbub = atomicReference;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r5 = this;
        r1 = r5.zzbub;
        monitor-enter(r1);
        r0 = r5.zzbua;	 Catch:{ RemoteException -> 0x0063 }
        r0 = r0.zzbtU;	 Catch:{ RemoteException -> 0x0063 }
        if (r0 != 0) goto L_0x0022;
    L_0x000b:
        r0 = r5.zzbua;	 Catch:{ RemoteException -> 0x0063 }
        r0 = r0.zzwF();	 Catch:{ RemoteException -> 0x0063 }
        r0 = r0.zzyx();	 Catch:{ RemoteException -> 0x0063 }
        r2 = "Failed to get app instance id";
        r0.log(r2);	 Catch:{ RemoteException -> 0x0063 }
        r0 = r5.zzbub;	 Catch:{ all -> 0x0060 }
        r0.notify();	 Catch:{ all -> 0x0060 }
        monitor-exit(r1);	 Catch:{ all -> 0x0060 }
    L_0x0021:
        return;
    L_0x0022:
        r2 = r5.zzbub;	 Catch:{ RemoteException -> 0x0063 }
        r3 = r5.zzbua;	 Catch:{ RemoteException -> 0x0063 }
        r3 = r3.zzwu();	 Catch:{ RemoteException -> 0x0063 }
        r4 = 0;
        r3 = r3.zzdV(r4);	 Catch:{ RemoteException -> 0x0063 }
        r0 = r0.zzc(r3);	 Catch:{ RemoteException -> 0x0063 }
        r2.set(r0);	 Catch:{ RemoteException -> 0x0063 }
        r0 = r5.zzbub;	 Catch:{ RemoteException -> 0x0063 }
        r0 = r0.get();	 Catch:{ RemoteException -> 0x0063 }
        r0 = (java.lang.String) r0;	 Catch:{ RemoteException -> 0x0063 }
        if (r0 == 0) goto L_0x0054;
    L_0x0040:
        r2 = r5.zzbua;	 Catch:{ RemoteException -> 0x0063 }
        r2 = r2.zzwt();	 Catch:{ RemoteException -> 0x0063 }
        r2.zzee(r0);	 Catch:{ RemoteException -> 0x0063 }
        r2 = r5.zzbua;	 Catch:{ RemoteException -> 0x0063 }
        r2 = r2.zzwG();	 Catch:{ RemoteException -> 0x0063 }
        r2 = r2.zzbrq;	 Catch:{ RemoteException -> 0x0063 }
        r2.zzef(r0);	 Catch:{ RemoteException -> 0x0063 }
    L_0x0054:
        r0 = r5.zzbua;	 Catch:{ RemoteException -> 0x0063 }
        r0.zzkP();	 Catch:{ RemoteException -> 0x0063 }
        r0 = r5.zzbub;	 Catch:{ all -> 0x0060 }
        r0.notify();	 Catch:{ all -> 0x0060 }
    L_0x005e:
        monitor-exit(r1);	 Catch:{ all -> 0x0060 }
        goto L_0x0021;
    L_0x0060:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0060 }
        throw r0;
    L_0x0063:
        r0 = move-exception;
        r2 = r5.zzbua;	 Catch:{ all -> 0x007a }
        r2 = r2.zzwF();	 Catch:{ all -> 0x007a }
        r2 = r2.zzyx();	 Catch:{ all -> 0x007a }
        r3 = "Failed to get app instance id";
        r2.zzj(r3, r0);	 Catch:{ all -> 0x007a }
        r0 = r5.zzbub;	 Catch:{ all -> 0x0060 }
        r0.notify();	 Catch:{ all -> 0x0060 }
        goto L_0x005e;
    L_0x007a:
        r0 = move-exception;
        r2 = r5.zzbub;	 Catch:{ all -> 0x0060 }
        r2.notify();	 Catch:{ all -> 0x0060 }
        throw r0;	 Catch:{ all -> 0x0060 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcif.run():void");
    }
}
