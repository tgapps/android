package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzec implements Runnable {
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ String zzaeo;
    private final /* synthetic */ boolean zzaev;
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ AtomicReference zzash;

    zzec(zzdr com_google_android_gms_measurement_internal_zzdr, AtomicReference atomicReference, String str, String str2, String str3, boolean z, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzasg = com_google_android_gms_measurement_internal_zzdr;
        this.zzash = atomicReference;
        this.zzaqq = str;
        this.zzaeh = str2;
        this.zzaeo = str3;
        this.zzaev = z;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r7 = this;
        r1 = r7.zzash;
        monitor-enter(r1);
        r0 = r7.zzasg;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzasa;	 Catch:{ RemoteException -> 0x006f }
        if (r0 != 0) goto L_0x0035;
    L_0x000b:
        r0 = r7.zzasg;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzgo();	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzjd();	 Catch:{ RemoteException -> 0x006f }
        r2 = "Failed to get user properties";
        r3 = r7.zzaqq;	 Catch:{ RemoteException -> 0x006f }
        r3 = com.google.android.gms.measurement.internal.zzap.zzbv(r3);	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzaeh;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzaeo;	 Catch:{ RemoteException -> 0x006f }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzash;	 Catch:{ RemoteException -> 0x006f }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x006f }
        r0.set(r2);	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzash;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r7.zzaqq;	 Catch:{ RemoteException -> 0x006f }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x006f }
        if (r2 == 0) goto L_0x005d;
    L_0x003d:
        r2 = r7.zzash;	 Catch:{ RemoteException -> 0x006f }
        r3 = r7.zzaeh;	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzaeo;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzaev;	 Catch:{ RemoteException -> 0x006f }
        r6 = r7.zzaqn;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006f }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006f }
    L_0x004e:
        r0 = r7.zzasg;	 Catch:{ RemoteException -> 0x006f }
        r0.zzcy();	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzash;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
    L_0x0058:
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
        goto L_0x0034;
    L_0x005a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
        throw r0;
    L_0x005d:
        r2 = r7.zzash;	 Catch:{ RemoteException -> 0x006f }
        r3 = r7.zzaqq;	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzaeh;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzaeo;	 Catch:{ RemoteException -> 0x006f }
        r6 = r7.zzaev;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006f }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006f }
        goto L_0x004e;
    L_0x006f:
        r0 = move-exception;
        r2 = r7.zzasg;	 Catch:{ all -> 0x0097 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x0097 }
        r2 = r2.zzjd();	 Catch:{ all -> 0x0097 }
        r3 = "Failed to get user properties";
        r4 = r7.zzaqq;	 Catch:{ all -> 0x0097 }
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x0097 }
        r5 = r7.zzaeh;	 Catch:{ all -> 0x0097 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x0097 }
        r0 = r7.zzash;	 Catch:{ all -> 0x0097 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0097 }
        r0.set(r2);	 Catch:{ all -> 0x0097 }
        r0 = r7.zzash;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
        goto L_0x0058;
    L_0x0097:
        r0 = move-exception;
        r2 = r7.zzash;	 Catch:{ all -> 0x005a }
        r2.notify();	 Catch:{ all -> 0x005a }
        throw r0;	 Catch:{ all -> 0x005a }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzec.run():void");
    }
}
