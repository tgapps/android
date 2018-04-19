package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import java.io.IOException;

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

    private final boolean zzfg(String str) {
        String[] split = str.split("!");
        if (split.length != 2) {
            return true;
        }
        String str2;
        String valueOf;
        String str3 = split[0];
        String str4 = split[1];
        int i = -1;
        try {
            switch (str3.hashCode()) {
                case 83:
                    if (str3.equals("S")) {
                        i = 0;
                        break;
                    }
                    break;
                case 85:
                    if (str3.equals("U")) {
                        boolean z = true;
                        break;
                    }
                    break;
            }
            switch (i) {
                case 0:
                    this.zzbse.zzew(str4);
                    if (!FirebaseInstanceId.zzsj()) {
                        return true;
                    }
                    Log.d("FirebaseInstanceId", "subscribe operation succeeded");
                    return true;
                case 1:
                    this.zzbse.zzex(str4);
                    if (!FirebaseInstanceId.zzsj()) {
                        return true;
                    }
                    Log.d("FirebaseInstanceId", "unsubscribe operation succeeded");
                    return true;
                default:
                    return true;
            }
        } catch (IOException e) {
            str2 = "FirebaseInstanceId";
            str3 = "Topic sync failed: ";
            valueOf = String.valueOf(e.getMessage());
            Log.e(str2, valueOf.length() == 0 ? new String(str3) : str3.concat(valueOf));
            return false;
        }
        str2 = "FirebaseInstanceId";
        str3 = "Topic sync failed: ";
        valueOf = String.valueOf(e.getMessage());
        if (valueOf.length() == 0) {
        }
        Log.e(str2, valueOf.length() == 0 ? new String(str3) : str3.concat(valueOf));
        return false;
    }

    private final boolean zzte() {
        String zzsh;
        Exception e;
        String str;
        String valueOf;
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
            if (zzsg != null && (zzsg == null || zzsh.equals(zzsg.zzbsb))) {
                return true;
            }
            Context context = getContext();
            Parcelable intent = new Intent("com.google.firebase.iid.TOKEN_REFRESH");
            Intent intent2 = new Intent("com.google.firebase.INSTANCE_ID_EVENT");
            intent2.setClass(context, FirebaseInstanceIdReceiver.class);
            intent2.putExtra("wrapped_intent", intent);
            context.sendBroadcast(intent2);
            return true;
        } catch (IOException e2) {
            e = e2;
            str = "FirebaseInstanceId";
            zzsh = "Token retrieval failed: ";
            valueOf = String.valueOf(e.getMessage());
            Log.e(str, valueOf.length() == 0 ? zzsh.concat(valueOf) : new String(zzsh));
            return false;
        } catch (SecurityException e3) {
            e = e3;
            str = "FirebaseInstanceId";
            zzsh = "Token retrieval failed: ";
            valueOf = String.valueOf(e.getMessage());
            if (valueOf.length() == 0) {
            }
            Log.e(str, valueOf.length() == 0 ? zzsh.concat(valueOf) : new String(zzsh));
            return false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zztf() {
        /*
        r3 = this;
    L_0x0000:
        r1 = r3.zzbse;
        monitor-enter(r1);
        r0 = com.google.firebase.iid.FirebaseInstanceId.zzsi();	 Catch:{ all -> 0x0022 }
        r0 = r0.zztc();	 Catch:{ all -> 0x0022 }
        if (r0 != 0) goto L_0x0019;
    L_0x000d:
        r0 = "FirebaseInstanceId";
        r2 = "topic sync succeeded";
        android.util.Log.d(r0, r2);	 Catch:{ all -> 0x0022 }
        r0 = 1;
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
    L_0x0018:
        return r0;
    L_0x0019:
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
        r1 = r3.zzfg(r0);
        if (r1 != 0) goto L_0x0025;
    L_0x0020:
        r0 = 0;
        goto L_0x0018;
    L_0x0022:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
        throw r0;
    L_0x0025:
        r1 = com.google.firebase.iid.FirebaseInstanceId.zzsi();
        r1.zzez(r0);
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzac.zztf():boolean");
    }

    final Context getContext() {
        return this.zzbse.zzsf().getApplicationContext();
    }

    public final void run() {
        Object obj = 1;
        this.zzbsd.acquire();
        try {
            this.zzbse.zzu(true);
            if (this.zzbqn.zzsu() == 0) {
                obj = null;
            }
            if (obj == null) {
                this.zzbse.zzu(false);
            } else if (zztg()) {
                if (zzte() && zztf()) {
                    this.zzbse.zzu(false);
                } else {
                    this.zzbse.zzan(this.zzbsc);
                }
                this.zzbsd.release();
            } else {
                new zzad(this).zzth();
                this.zzbsd.release();
            }
        } finally {
            this.zzbsd.release();
        }
    }

    final boolean zztg() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
