package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzdu implements Runnable {
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ AtomicReference zzash;

    zzdu(zzdr com_google_android_gms_measurement_internal_zzdr, AtomicReference atomicReference, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzasg = com_google_android_gms_measurement_internal_zzdr;
        this.zzash = atomicReference;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r4 = this;
        r1 = r4.zzash;
        monitor-enter(r1);
        r0 = r4.zzasg;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzasa;	 Catch:{ RemoteException -> 0x005a }
        if (r0 != 0) goto L_0x0022;
    L_0x000b:
        r0 = r4.zzasg;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzgo();	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzjd();	 Catch:{ RemoteException -> 0x005a }
        r2 = "Failed to get app instance id";
        r0.zzbx(r2);	 Catch:{ RemoteException -> 0x005a }
        r0 = r4.zzash;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
        monitor-exit(r1);	 Catch:{ all -> 0x0057 }
    L_0x0021:
        return;
    L_0x0022:
        r2 = r4.zzash;	 Catch:{ RemoteException -> 0x005a }
        r3 = r4.zzaqn;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.zzc(r3);	 Catch:{ RemoteException -> 0x005a }
        r2.set(r0);	 Catch:{ RemoteException -> 0x005a }
        r0 = r4.zzash;	 Catch:{ RemoteException -> 0x005a }
        r0 = r0.get();	 Catch:{ RemoteException -> 0x005a }
        r0 = (java.lang.String) r0;	 Catch:{ RemoteException -> 0x005a }
        if (r0 == 0) goto L_0x004b;
    L_0x0037:
        r2 = r4.zzasg;	 Catch:{ RemoteException -> 0x005a }
        r2 = r2.zzge();	 Catch:{ RemoteException -> 0x005a }
        r2.zzcm(r0);	 Catch:{ RemoteException -> 0x005a }
        r2 = r4.zzasg;	 Catch:{ RemoteException -> 0x005a }
        r2 = r2.zzgp();	 Catch:{ RemoteException -> 0x005a }
        r2 = r2.zzanl;	 Catch:{ RemoteException -> 0x005a }
        r2.zzcc(r0);	 Catch:{ RemoteException -> 0x005a }
    L_0x004b:
        r0 = r4.zzasg;	 Catch:{ RemoteException -> 0x005a }
        r0.zzcy();	 Catch:{ RemoteException -> 0x005a }
        r0 = r4.zzash;	 Catch:{ all -> 0x0057 }
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
        r2 = r4.zzasg;	 Catch:{ all -> 0x0071 }
        r2 = r2.zzgo();	 Catch:{ all -> 0x0071 }
        r2 = r2.zzjd();	 Catch:{ all -> 0x0071 }
        r3 = "Failed to get app instance id";
        r2.zzg(r3, r0);	 Catch:{ all -> 0x0071 }
        r0 = r4.zzash;	 Catch:{ all -> 0x0057 }
        r0.notify();	 Catch:{ all -> 0x0057 }
        goto L_0x0055;
    L_0x0071:
        r0 = move-exception;
        r2 = r4.zzash;	 Catch:{ all -> 0x0057 }
        r2.notify();	 Catch:{ all -> 0x0057 }
        throw r0;	 Catch:{ all -> 0x0057 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzdu.run():void");
    }
}
