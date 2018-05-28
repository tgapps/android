package com.google.android.gms.internal.vision;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.DynamiteModule.LoadingException;
import javax.annotation.concurrent.GuardedBy;

public abstract class zzl<T> {
    private static String PREFIX = "com.google.android.gms.vision.dynamite";
    private final Object lock = new Object();
    private final String tag;
    private final String zzci;
    private final String zzcj;
    private boolean zzck = false;
    @GuardedBy("lock")
    private T zzcl;
    private final Context zze;

    public zzl(Context context, String str, String str2) {
        this.zze = context;
        this.tag = str;
        String str3 = PREFIX;
        this.zzci = new StringBuilder((String.valueOf(str3).length() + 1) + String.valueOf(str2).length()).append(str3).append(".").append(str2).toString();
        this.zzcj = PREFIX;
    }

    public final boolean isOperational() {
        return zzp() != null;
    }

    protected abstract T zza(DynamiteModule dynamiteModule, Context context) throws RemoteException, LoadingException;

    protected abstract void zzm() throws RemoteException;

    public final void zzo() {
        synchronized (this.lock) {
            if (this.zzcl == null) {
                return;
            }
            try {
                zzm();
            } catch (Throwable e) {
                Log.e(this.tag, "Could not finalize native handle", e);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected final T zzp() {
        /*
        r5 = this;
        r2 = r5.lock;
        monitor-enter(r2);
        r0 = r5.zzcl;	 Catch:{ all -> 0x0037 }
        if (r0 == 0) goto L_0x000b;
    L_0x0007:
        r0 = r5.zzcl;	 Catch:{ all -> 0x0037 }
        monitor-exit(r2);	 Catch:{ all -> 0x0037 }
    L_0x000a:
        return r0;
    L_0x000b:
        r1 = 0;
        r0 = r5.zze;	 Catch:{ LoadingException -> 0x003a, RemoteException -> 0x0059 }
        r3 = com.google.android.gms.dynamite.DynamiteModule.PREFER_HIGHEST_OR_REMOTE_VERSION;	 Catch:{ LoadingException -> 0x003a, RemoteException -> 0x0059 }
        r4 = r5.zzci;	 Catch:{ LoadingException -> 0x003a, RemoteException -> 0x0059 }
        r0 = com.google.android.gms.dynamite.DynamiteModule.load(r0, r3, r4);	 Catch:{ LoadingException -> 0x003a, RemoteException -> 0x0059 }
    L_0x0016:
        if (r0 == 0) goto L_0x0020;
    L_0x0018:
        r1 = r5.zze;	 Catch:{ LoadingException -> 0x0074, RemoteException -> 0x0059 }
        r0 = r5.zza(r0, r1);	 Catch:{ LoadingException -> 0x0074, RemoteException -> 0x0059 }
        r5.zzcl = r0;	 Catch:{ LoadingException -> 0x0074, RemoteException -> 0x0059 }
    L_0x0020:
        r0 = r5.zzck;	 Catch:{ all -> 0x0037 }
        if (r0 != 0) goto L_0x0063;
    L_0x0024:
        r0 = r5.zzcl;	 Catch:{ all -> 0x0037 }
        if (r0 != 0) goto L_0x0063;
    L_0x0028:
        r0 = r5.tag;	 Catch:{ all -> 0x0037 }
        r1 = "Native handle not yet available. Reverting to no-op handle.";
        android.util.Log.w(r0, r1);	 Catch:{ all -> 0x0037 }
        r0 = 1;
        r5.zzck = r0;	 Catch:{ all -> 0x0037 }
    L_0x0033:
        r0 = r5.zzcl;	 Catch:{ all -> 0x0037 }
        monitor-exit(r2);	 Catch:{ all -> 0x0037 }
        goto L_0x000a;
    L_0x0037:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0037 }
        throw r0;
    L_0x003a:
        r0 = move-exception;
        r0 = r5.tag;	 Catch:{ LoadingException -> 0x0074, RemoteException -> 0x0059 }
        r3 = "Cannot load feature, fall back to load whole module.";
        android.util.Log.d(r0, r3);	 Catch:{ LoadingException -> 0x0074, RemoteException -> 0x0059 }
        r0 = r5.zze;	 Catch:{ LoadingException -> 0x004e, RemoteException -> 0x0059 }
        r3 = com.google.android.gms.dynamite.DynamiteModule.PREFER_HIGHEST_OR_REMOTE_VERSION;	 Catch:{ LoadingException -> 0x004e, RemoteException -> 0x0059 }
        r4 = r5.zzcj;	 Catch:{ LoadingException -> 0x004e, RemoteException -> 0x0059 }
        r0 = com.google.android.gms.dynamite.DynamiteModule.load(r0, r3, r4);	 Catch:{ LoadingException -> 0x004e, RemoteException -> 0x0059 }
        goto L_0x0016;
    L_0x004e:
        r0 = move-exception;
        r3 = r5.tag;	 Catch:{ LoadingException -> 0x0074, RemoteException -> 0x0059 }
        r4 = "Error Loading module";
        android.util.Log.e(r3, r4, r0);	 Catch:{ LoadingException -> 0x0074, RemoteException -> 0x0059 }
        r0 = r1;
        goto L_0x0016;
    L_0x0059:
        r0 = move-exception;
    L_0x005a:
        r1 = r5.tag;	 Catch:{ all -> 0x0037 }
        r3 = "Error creating remote native handle";
        android.util.Log.e(r1, r3, r0);	 Catch:{ all -> 0x0037 }
        goto L_0x0020;
    L_0x0063:
        r0 = r5.zzck;	 Catch:{ all -> 0x0037 }
        if (r0 == 0) goto L_0x0033;
    L_0x0067:
        r0 = r5.zzcl;	 Catch:{ all -> 0x0037 }
        if (r0 == 0) goto L_0x0033;
    L_0x006b:
        r0 = r5.tag;	 Catch:{ all -> 0x0037 }
        r1 = "Native handle is now available.";
        android.util.Log.w(r0, r1);	 Catch:{ all -> 0x0037 }
        goto L_0x0033;
    L_0x0074:
        r0 = move-exception;
        goto L_0x005a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.vision.zzl.zzp():T");
    }
}
