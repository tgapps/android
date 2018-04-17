package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

final class zzac implements Runnable {
    private final zzw zzbqn;
    private final long zzbsc;
    private final WakeLock zzbsd = ((PowerManager) getContext().getSystemService("power")).newWakeLock(1, "fiid-sync");
    private final FirebaseInstanceId zzbse;

    zzac(FirebaseInstanceId firebaseInstanceId, zzw com_google_firebase_iid_zzw, long j) {
        this.zzbse = firebaseInstanceId;
        this.zzbqn = com_google_firebase_iid_zzw;
        this.zzbsc = j;
        this.zzbsd.setReferenceCounted(false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzfg(java.lang.String r7) {
        /*
        r6 = this;
        r0 = "!";
        r7 = r7.split(r0);
        r0 = 1;
        r1 = 2;
        r2 = r7.length;
        if (r2 != r1) goto L_0x0079;
    L_0x000b:
        r1 = 0;
        r2 = r7[r1];
        r7 = r7[r0];
        r3 = -1;
        r4 = r2.hashCode();	 Catch:{ IOException -> 0x0058 }
        r5 = 83;
        if (r4 == r5) goto L_0x0028;
    L_0x0019:
        r5 = 85;
        if (r4 == r5) goto L_0x001e;
    L_0x001d:
        goto L_0x0031;
    L_0x001e:
        r4 = "U";
        r2 = r2.equals(r4);	 Catch:{ IOException -> 0x0058 }
        if (r2 == 0) goto L_0x0031;
    L_0x0026:
        r3 = r0;
        goto L_0x0031;
    L_0x0028:
        r4 = "S";
        r2 = r2.equals(r4);	 Catch:{ IOException -> 0x0058 }
        if (r2 == 0) goto L_0x0031;
    L_0x0030:
        r3 = r1;
    L_0x0031:
        switch(r3) {
            case 0: goto L_0x0048;
            case 1: goto L_0x0035;
            default: goto L_0x0034;
        };	 Catch:{ IOException -> 0x0058 }
    L_0x0034:
        return r0;
    L_0x0035:
        r2 = r6.zzbse;	 Catch:{ IOException -> 0x0058 }
        r2.zzex(r7);	 Catch:{ IOException -> 0x0058 }
        r7 = com.google.firebase.iid.FirebaseInstanceId.zzsj();	 Catch:{ IOException -> 0x0058 }
        if (r7 == 0) goto L_0x0079;
    L_0x0040:
        r7 = "FirebaseInstanceId";
        r2 = "unsubscribe operation succeeded";
    L_0x0044:
        android.util.Log.d(r7, r2);	 Catch:{ IOException -> 0x0058 }
        return r0;
    L_0x0048:
        r2 = r6.zzbse;	 Catch:{ IOException -> 0x0058 }
        r2.zzew(r7);	 Catch:{ IOException -> 0x0058 }
        r7 = com.google.firebase.iid.FirebaseInstanceId.zzsj();	 Catch:{ IOException -> 0x0058 }
        if (r7 == 0) goto L_0x0079;
    L_0x0053:
        r7 = "FirebaseInstanceId";
        r2 = "subscribe operation succeeded";
        goto L_0x0044;
    L_0x0058:
        r7 = move-exception;
        r0 = "FirebaseInstanceId";
        r2 = "Topic sync failed: ";
        r7 = r7.getMessage();
        r7 = java.lang.String.valueOf(r7);
        r3 = r7.length();
        if (r3 == 0) goto L_0x0070;
    L_0x006b:
        r7 = r2.concat(r7);
        goto L_0x0075;
    L_0x0070:
        r7 = new java.lang.String;
        r7.<init>(r2);
    L_0x0075:
        android.util.Log.e(r0, r7);
        return r1;
    L_0x0079:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzac.zzfg(java.lang.String):boolean");
    }

    private final boolean zzte() {
        String zzsh;
        zzab zzsg = this.zzbse.zzsg();
        if (zzsg != null && !zzsg.zzff(this.zzbqn.zzsv())) {
            return true;
        }
        try {
            zzsh = this.zzbse.zzsh();
            if (zzsh == null) {
                Log.e("FirebaseInstanceId", "Token retrieval failed: null");
                return false;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Token successfully retrieved");
            }
            if (zzsg == null || !(zzsg == null || zzsh.equals(zzsg.zzbsb))) {
                Context context = getContext();
                Parcelable intent = new Intent("com.google.firebase.iid.TOKEN_REFRESH");
                Intent intent2 = new Intent("com.google.firebase.INSTANCE_ID_EVENT");
                intent2.setClass(context, FirebaseInstanceIdReceiver.class);
                intent2.putExtra("wrapped_intent", intent);
                context.sendBroadcast(intent2);
            }
            return true;
        } catch (Exception e) {
            String str = "FirebaseInstanceId";
            zzsh = "Token retrieval failed: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.e(str, valueOf.length() == 0 ? new String(zzsh) : zzsh.concat(valueOf));
            return false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zztf() {
        /*
        r3 = this;
    L_0x0000:
        r0 = r3.zzbse;
        monitor-enter(r0);
        r1 = com.google.firebase.iid.FirebaseInstanceId.zzsi();	 Catch:{ all -> 0x0028 }
        r1 = r1.zztc();	 Catch:{ all -> 0x0028 }
        if (r1 != 0) goto L_0x0017;
    L_0x000d:
        r1 = "FirebaseInstanceId";
        r2 = "topic sync succeeded";
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0028 }
        r1 = 1;
        monitor-exit(r0);	 Catch:{ all -> 0x0028 }
        return r1;
    L_0x0017:
        monitor-exit(r0);	 Catch:{ all -> 0x0028 }
        r0 = r3.zzfg(r1);
        if (r0 != 0) goto L_0x0020;
    L_0x001e:
        r0 = 0;
        return r0;
    L_0x0020:
        r0 = com.google.firebase.iid.FirebaseInstanceId.zzsi();
        r0.zzez(r1);
        goto L_0x0000;
    L_0x0028:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0028 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzac.zztf():boolean");
    }

    final Context getContext() {
        return this.zzbse.zzsf().getApplicationContext();
    }

    public final void run() {
        this.zzbsd.acquire();
        try {
            FirebaseInstanceId firebaseInstanceId;
            boolean z = true;
            this.zzbse.zzu(true);
            if (this.zzbqn.zzsu() == 0) {
                z = false;
            }
            if (z) {
                if (!zztg()) {
                    new zzad(this).zzth();
                } else if (zzte() && zztf()) {
                    firebaseInstanceId = this.zzbse;
                } else {
                    this.zzbse.zzan(this.zzbsc);
                }
                this.zzbsd.release();
            }
            firebaseInstanceId = this.zzbse;
            firebaseInstanceId.zzu(false);
            this.zzbsd.release();
        } catch (Throwable th) {
            this.zzbsd.release();
        }
    }

    final boolean zztg() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
