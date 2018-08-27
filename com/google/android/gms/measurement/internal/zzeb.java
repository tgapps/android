package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzeb implements Runnable {
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ String zzaeo;
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ AtomicReference zzash;

    zzeb(zzdr com_google_android_gms_measurement_internal_zzdr, AtomicReference atomicReference, String str, String str2, String str3, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzasg = com_google_android_gms_measurement_internal_zzdr;
        this.zzash = atomicReference;
        this.zzaqq = str;
        this.zzaeh = str2;
        this.zzaeo = str3;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r6 = this;
        r1 = r6.zzash;
        monitor-enter(r1);
        r0 = r6.zzasg;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzasa;	 Catch:{ RemoteException -> 0x006b }
        if (r0 != 0) goto L_0x0035;
    L_0x000b:
        r0 = r6.zzasg;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzgo();	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zzjd();	 Catch:{ RemoteException -> 0x006b }
        r2 = "Failed to get conditional properties";
        r3 = r6.zzaqq;	 Catch:{ RemoteException -> 0x006b }
        r3 = com.google.android.gms.measurement.internal.zzap.zzbv(r3);	 Catch:{ RemoteException -> 0x006b }
        r4 = r6.zzaeh;	 Catch:{ RemoteException -> 0x006b }
        r5 = r6.zzaeo;	 Catch:{ RemoteException -> 0x006b }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x006b }
        r0 = r6.zzash;	 Catch:{ RemoteException -> 0x006b }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x006b }
        r0.set(r2);	 Catch:{ RemoteException -> 0x006b }
        r0 = r6.zzash;	 Catch:{ all -> 0x0058 }
        r0.notify();	 Catch:{ all -> 0x0058 }
        monitor-exit(r1);	 Catch:{ all -> 0x0058 }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r6.zzaqq;	 Catch:{ RemoteException -> 0x006b }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x006b }
        if (r2 == 0) goto L_0x005b;
    L_0x003d:
        r2 = r6.zzash;	 Catch:{ RemoteException -> 0x006b }
        r3 = r6.zzaeh;	 Catch:{ RemoteException -> 0x006b }
        r4 = r6.zzaeo;	 Catch:{ RemoteException -> 0x006b }
        r5 = r6.zzaqn;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zza(r3, r4, r5);	 Catch:{ RemoteException -> 0x006b }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006b }
    L_0x004c:
        r0 = r6.zzasg;	 Catch:{ RemoteException -> 0x006b }
        r0.zzcy();	 Catch:{ RemoteException -> 0x006b }
        r0 = r6.zzash;	 Catch:{ all -> 0x0058 }
        r0.notify();	 Catch:{ all -> 0x0058 }
    L_0x0056:
        monitor-exit(r1);	 Catch:{ all -> 0x0058 }
        goto L_0x0034;
    L_0x0058:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0058 }
        throw r0;
    L_0x005b:
        r2 = r6.zzash;	 Catch:{ RemoteException -> 0x006b }
        r3 = r6.zzaqq;	 Catch:{ RemoteException -> 0x006b }
        r4 = r6.zzaeh;	 Catch:{ RemoteException -> 0x006b }
        r5 = r6.zzaeo;	 Catch:{ RemoteException -> 0x006b }
        r0 = r0.zze(r3, r4, r5);	 Catch:{ RemoteException -> 0x006b }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006b }
        goto L_0x004c;
    L_0x006b:
        r0 = move-exception;
        r2 = r6.zzasg;	 Catch:{ all -> 0x0093 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x0093 }
        r2 = r2.zzjd();	 Catch:{ all -> 0x0093 }
        r3 = "Failed to get conditional properties";
        r4 = r6.zzaqq;	 Catch:{ all -> 0x0093 }
        r4 = com.google.android.gms.measurement.internal.zzap.zzbv(r4);	 Catch:{ all -> 0x0093 }
        r5 = r6.zzaeh;	 Catch:{ all -> 0x0093 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x0093 }
        r0 = r6.zzash;	 Catch:{ all -> 0x0093 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0093 }
        r0.set(r2);	 Catch:{ all -> 0x0093 }
        r0 = r6.zzash;	 Catch:{ all -> 0x0058 }
        r0.notify();	 Catch:{ all -> 0x0058 }
        goto L_0x0056;
    L_0x0093:
        r0 = move-exception;
        r2 = r6.zzash;	 Catch:{ all -> 0x0058 }
        r2.notify();	 Catch:{ all -> 0x0058 }
        throw r0;	 Catch:{ all -> 0x0058 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzeb.run():void");
    }
}
