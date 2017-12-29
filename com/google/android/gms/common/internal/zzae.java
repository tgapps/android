package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class zzae implements Callback {
    private final Handler mHandler;
    private final Object mLock = new Object();
    private final zzaf zzgab;
    private final ArrayList<ConnectionCallbacks> zzgac = new ArrayList();
    private ArrayList<ConnectionCallbacks> zzgad = new ArrayList();
    private final ArrayList<OnConnectionFailedListener> zzgae = new ArrayList();
    private volatile boolean zzgaf = false;
    private final AtomicInteger zzgag = new AtomicInteger(0);
    private boolean zzgah = false;

    public zzae(Looper looper, zzaf com_google_android_gms_common_internal_zzaf) {
        this.zzgab = com_google_android_gms_common_internal_zzaf;
        this.mHandler = new Handler(looper, this);
    }

    public final boolean handleMessage(Message message) {
        if (message.what == 1) {
            ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) message.obj;
            synchronized (this.mLock) {
                if (this.zzgaf && this.zzgab.isConnected() && this.zzgac.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(this.zzgab.zzafi());
                }
            }
            return true;
        }
        Log.wtf("GmsClientEvents", "Don't know how to handle message: " + message.what, new Exception());
        return false;
    }

    public final void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        zzbq.checkNotNull(connectionCallbacks);
        synchronized (this.mLock) {
            if (this.zzgac.contains(connectionCallbacks)) {
                String valueOf = String.valueOf(connectionCallbacks);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(valueOf).length() + 62).append("registerConnectionCallbacks(): listener ").append(valueOf).append(" is already registered").toString());
            } else {
                this.zzgac.add(connectionCallbacks);
            }
        }
        if (this.zzgab.isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, connectionCallbacks));
        }
    }

    public final void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzbq.checkNotNull(onConnectionFailedListener);
        synchronized (this.mLock) {
            if (this.zzgae.contains(onConnectionFailedListener)) {
                String valueOf = String.valueOf(onConnectionFailedListener);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(valueOf).length() + 67).append("registerConnectionFailedListener(): listener ").append(valueOf).append(" is already registered").toString());
            } else {
                this.zzgae.add(onConnectionFailedListener);
            }
        }
    }

    public final void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzbq.checkNotNull(onConnectionFailedListener);
        synchronized (this.mLock) {
            if (!this.zzgae.remove(onConnectionFailedListener)) {
                String valueOf = String.valueOf(onConnectionFailedListener);
                Log.w("GmsClientEvents", new StringBuilder(String.valueOf(valueOf).length() + 57).append("unregisterConnectionFailedListener(): listener ").append(valueOf).append(" not found").toString());
            }
        }
    }

    public final void zzali() {
        this.zzgaf = false;
        this.zzgag.incrementAndGet();
    }

    public final void zzalj() {
        this.zzgaf = true;
    }

    public final void zzcg(int i) {
        int i2 = 0;
        zzbq.zza(Looper.myLooper() == this.mHandler.getLooper(), "onUnintentionalDisconnection must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.mLock) {
            this.zzgah = true;
            ArrayList arrayList = new ArrayList(this.zzgac);
            int i3 = this.zzgag.get();
            arrayList = arrayList;
            int size = arrayList.size();
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) obj;
                if (this.zzgaf && this.zzgag.get() == i3) {
                    if (this.zzgac.contains(connectionCallbacks)) {
                        connectionCallbacks.onConnectionSuspended(i);
                    }
                }
            }
            this.zzgad.clear();
            this.zzgah = false;
        }
    }

    public final void zzk(Bundle bundle) {
        boolean z = true;
        int i = 0;
        zzbq.zza(Looper.myLooper() == this.mHandler.getLooper(), "onConnectionSuccess must only be called on the Handler thread");
        synchronized (this.mLock) {
            zzbq.checkState(!this.zzgah);
            this.mHandler.removeMessages(1);
            this.zzgah = true;
            if (this.zzgad.size() != 0) {
                z = false;
            }
            zzbq.checkState(z);
            ArrayList arrayList = new ArrayList(this.zzgac);
            int i2 = this.zzgag.get();
            arrayList = arrayList;
            int size = arrayList.size();
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) obj;
                if (this.zzgaf && this.zzgab.isConnected() && this.zzgag.get() == i2) {
                    if (!this.zzgad.contains(connectionCallbacks)) {
                        connectionCallbacks.onConnected(bundle);
                    }
                }
            }
            this.zzgad.clear();
            this.zzgah = false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzk(com.google.android.gms.common.ConnectionResult r8) {
        /*
        r7 = this;
        r1 = 1;
        r2 = 0;
        r0 = android.os.Looper.myLooper();
        r3 = r7.mHandler;
        r3 = r3.getLooper();
        if (r0 != r3) goto L_0x0048;
    L_0x000e:
        r0 = r1;
    L_0x000f:
        r3 = "onConnectionFailure must only be called on the Handler thread";
        com.google.android.gms.common.internal.zzbq.zza(r0, r3);
        r0 = r7.mHandler;
        r0.removeMessages(r1);
        r3 = r7.mLock;
        monitor-enter(r3);
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x0056 }
        r1 = r7.zzgae;	 Catch:{ all -> 0x0056 }
        r0.<init>(r1);	 Catch:{ all -> 0x0056 }
        r1 = r7.zzgag;	 Catch:{ all -> 0x0056 }
        r4 = r1.get();	 Catch:{ all -> 0x0056 }
        r0 = (java.util.ArrayList) r0;	 Catch:{ all -> 0x0056 }
        r5 = r0.size();	 Catch:{ all -> 0x0056 }
    L_0x0030:
        if (r2 >= r5) goto L_0x0059;
    L_0x0032:
        r1 = r0.get(r2);	 Catch:{ all -> 0x0056 }
        r2 = r2 + 1;
        r1 = (com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener) r1;	 Catch:{ all -> 0x0056 }
        r6 = r7.zzgaf;	 Catch:{ all -> 0x0056 }
        if (r6 == 0) goto L_0x0046;
    L_0x003e:
        r6 = r7.zzgag;	 Catch:{ all -> 0x0056 }
        r6 = r6.get();	 Catch:{ all -> 0x0056 }
        if (r6 == r4) goto L_0x004a;
    L_0x0046:
        monitor-exit(r3);	 Catch:{ all -> 0x0056 }
    L_0x0047:
        return;
    L_0x0048:
        r0 = r2;
        goto L_0x000f;
    L_0x004a:
        r6 = r7.zzgae;	 Catch:{ all -> 0x0056 }
        r6 = r6.contains(r1);	 Catch:{ all -> 0x0056 }
        if (r6 == 0) goto L_0x0030;
    L_0x0052:
        r1.onConnectionFailed(r8);	 Catch:{ all -> 0x0056 }
        goto L_0x0030;
    L_0x0056:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0056 }
        throw r0;
    L_0x0059:
        monitor-exit(r3);	 Catch:{ all -> 0x0056 }
        goto L_0x0047;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzae.zzk(com.google.android.gms.common.ConnectionResult):void");
    }
}
