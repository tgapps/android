package com.google.firebase.iid;

final /* synthetic */ class zzx implements Runnable {
    private final zzu zzbo;

    zzx(zzu com_google_firebase_iid_zzu) {
        this.zzbo = com_google_firebase_iid_zzu;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r8 = this;
        r7 = 2;
        r1 = r8.zzbo;
    L_0x0003:
        monitor-enter(r1);
        r0 = r1.state;	 Catch:{ all -> 0x0017 }
        if (r0 == r7) goto L_0x000a;
    L_0x0008:
        monitor-exit(r1);	 Catch:{ all -> 0x0017 }
    L_0x0009:
        return;
    L_0x000a:
        r0 = r1.zzbl;	 Catch:{ all -> 0x0017 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0017 }
        if (r0 == 0) goto L_0x001a;
    L_0x0012:
        r1.zzu();	 Catch:{ all -> 0x0017 }
        monitor-exit(r1);	 Catch:{ all -> 0x0017 }
        goto L_0x0009;
    L_0x0017:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0017 }
        throw r0;
    L_0x001a:
        r0 = r1.zzbl;	 Catch:{ all -> 0x0017 }
        r0 = r0.poll();	 Catch:{ all -> 0x0017 }
        r0 = (com.google.firebase.iid.zzab) r0;	 Catch:{ all -> 0x0017 }
        r2 = r1.zzbm;	 Catch:{ all -> 0x0017 }
        r3 = r0.zzbr;	 Catch:{ all -> 0x0017 }
        r2.put(r3, r0);	 Catch:{ all -> 0x0017 }
        r2 = r1.zzbn;	 Catch:{ all -> 0x0017 }
        r2 = r2.zzbg;	 Catch:{ all -> 0x0017 }
        r3 = new com.google.firebase.iid.zzy;	 Catch:{ all -> 0x0017 }
        r3.<init>(r1, r0);	 Catch:{ all -> 0x0017 }
        r4 = 30;
        r6 = java.util.concurrent.TimeUnit.SECONDS;	 Catch:{ all -> 0x0017 }
        r2.schedule(r3, r4, r6);	 Catch:{ all -> 0x0017 }
        monitor-exit(r1);	 Catch:{ all -> 0x0017 }
        r2 = "MessengerIpcClient";
        r3 = 3;
        r2 = android.util.Log.isLoggable(r2, r3);
        if (r2 == 0) goto L_0x006e;
    L_0x0046:
        r2 = "MessengerIpcClient";
        r3 = java.lang.String.valueOf(r0);
        r4 = java.lang.String.valueOf(r3);
        r4 = r4.length();
        r4 = r4 + 8;
        r5 = new java.lang.StringBuilder;
        r5.<init>(r4);
        r4 = "Sending ";
        r4 = r5.append(r4);
        r3 = r4.append(r3);
        r3 = r3.toString();
        android.util.Log.d(r2, r3);
    L_0x006e:
        r2 = r1.zzbn;
        r2 = r2.zzz;
        r3 = r1.zzbj;
        r4 = android.os.Message.obtain();
        r5 = r0.what;
        r4.what = r5;
        r5 = r0.zzbr;
        r4.arg1 = r5;
        r4.replyTo = r3;
        r3 = new android.os.Bundle;
        r3.<init>();
        r5 = "oneWay";
        r6 = r0.zzw();
        r3.putBoolean(r5, r6);
        r5 = "pkg";
        r2 = r2.getPackageName();
        r3.putString(r5, r2);
        r2 = "data";
        r0 = r0.zzbt;
        r3.putBundle(r2, r0);
        r4.setData(r3);
        r0 = r1.zzbk;	 Catch:{ RemoteException -> 0x00af }
        r0.send(r4);	 Catch:{ RemoteException -> 0x00af }
        goto L_0x0003;
    L_0x00af:
        r0 = move-exception;
        r0 = r0.getMessage();
        r1.zza(r7, r0);
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzx.run():void");
    }
}
