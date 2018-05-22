package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzit implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ String zzanh;
    private final /* synthetic */ String zzani;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ boolean zzanz;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ AtomicReference zzapf;

    zzit(zzii com_google_android_gms_internal_measurement_zzii, AtomicReference atomicReference, String str, String str2, String str3, boolean z, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzape = com_google_android_gms_internal_measurement_zzii;
        this.zzapf = atomicReference;
        this.zzanj = str;
        this.zzanh = str2;
        this.zzani = str3;
        this.zzanz = z;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r7 = this;
        r1 = r7.zzapf;
        monitor-enter(r1);
        r0 = r7.zzape;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzaoy;	 Catch:{ RemoteException -> 0x006f }
        if (r0 != 0) goto L_0x0035;
    L_0x000b:
        r0 = r7.zzape;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzge();	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zzim();	 Catch:{ RemoteException -> 0x006f }
        r2 = "Failed to get user properties";
        r3 = r7.zzanj;	 Catch:{ RemoteException -> 0x006f }
        r3 = com.google.android.gms.internal.measurement.zzfg.zzbm(r3);	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzanh;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzani;	 Catch:{ RemoteException -> 0x006f }
        r0.zzd(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzapf;	 Catch:{ RemoteException -> 0x006f }
        r2 = java.util.Collections.emptyList();	 Catch:{ RemoteException -> 0x006f }
        r0.set(r2);	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzapf;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
    L_0x0034:
        return;
    L_0x0035:
        r2 = r7.zzanj;	 Catch:{ RemoteException -> 0x006f }
        r2 = android.text.TextUtils.isEmpty(r2);	 Catch:{ RemoteException -> 0x006f }
        if (r2 == 0) goto L_0x005d;
    L_0x003d:
        r2 = r7.zzapf;	 Catch:{ RemoteException -> 0x006f }
        r3 = r7.zzanh;	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzani;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzanz;	 Catch:{ RemoteException -> 0x006f }
        r6 = r7.zzane;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006f }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006f }
    L_0x004e:
        r0 = r7.zzape;	 Catch:{ RemoteException -> 0x006f }
        r0.zzcu();	 Catch:{ RemoteException -> 0x006f }
        r0 = r7.zzapf;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
    L_0x0058:
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
        goto L_0x0034;
    L_0x005a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x005a }
        throw r0;
    L_0x005d:
        r2 = r7.zzapf;	 Catch:{ RemoteException -> 0x006f }
        r3 = r7.zzanj;	 Catch:{ RemoteException -> 0x006f }
        r4 = r7.zzanh;	 Catch:{ RemoteException -> 0x006f }
        r5 = r7.zzani;	 Catch:{ RemoteException -> 0x006f }
        r6 = r7.zzanz;	 Catch:{ RemoteException -> 0x006f }
        r0 = r0.zza(r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x006f }
        r2.set(r0);	 Catch:{ RemoteException -> 0x006f }
        goto L_0x004e;
    L_0x006f:
        r0 = move-exception;
        r2 = r7.zzape;	 Catch:{ all -> 0x0097 }
        r2 = r2.zzge();	 Catch:{ all -> 0x0097 }
        r2 = r2.zzim();	 Catch:{ all -> 0x0097 }
        r3 = "Failed to get user properties";
        r4 = r7.zzanj;	 Catch:{ all -> 0x0097 }
        r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r4);	 Catch:{ all -> 0x0097 }
        r5 = r7.zzanh;	 Catch:{ all -> 0x0097 }
        r2.zzd(r3, r4, r5, r0);	 Catch:{ all -> 0x0097 }
        r0 = r7.zzapf;	 Catch:{ all -> 0x0097 }
        r2 = java.util.Collections.emptyList();	 Catch:{ all -> 0x0097 }
        r0.set(r2);	 Catch:{ all -> 0x0097 }
        r0 = r7.zzapf;	 Catch:{ all -> 0x005a }
        r0.notify();	 Catch:{ all -> 0x005a }
        goto L_0x0058;
    L_0x0097:
        r0 = move-exception;
        r2 = r7.zzapf;	 Catch:{ all -> 0x005a }
        r2.notify();	 Catch:{ all -> 0x005a }
        throw r0;	 Catch:{ all -> 0x005a }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzit.run():void");
    }
}
