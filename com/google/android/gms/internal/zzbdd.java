package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbo;
import com.google.android.gms.common.internal.zzbx;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class zzbdd<O extends ApiOptions> implements ConnectionCallbacks, OnConnectionFailedListener, zzbbj {
    private final zzbat<O> zzaAK;
    private final zze zzaCy;
    private boolean zzaDA;
    private /* synthetic */ zzbdb zzaEm;
    private final Queue<zzbam> zzaEn = new LinkedList();
    private final zzb zzaEo;
    private final zzbbt zzaEp;
    private final Set<zzbav> zzaEq = new HashSet();
    private final Map<zzbdy<?>, zzbef> zzaEr = new HashMap();
    private final int zzaEs;
    private final zzbej zzaEt;
    private ConnectionResult zzaEu = null;

    @WorkerThread
    public zzbdd(zzbdb com_google_android_gms_internal_zzbdb, GoogleApi<O> googleApi) {
        this.zzaEm = com_google_android_gms_internal_zzbdb;
        this.zzaCy = googleApi.zza(com_google_android_gms_internal_zzbdb.mHandler.getLooper(), this);
        if (this.zzaCy instanceof zzbx) {
            zzbx com_google_android_gms_common_internal_zzbx = (zzbx) this.zzaCy;
            this.zzaEo = null;
        } else {
            this.zzaEo = this.zzaCy;
        }
        this.zzaAK = googleApi.zzph();
        this.zzaEp = new zzbbt();
        this.zzaEs = googleApi.getInstanceId();
        if (this.zzaCy.zzmv()) {
            this.zzaEt = googleApi.zza(com_google_android_gms_internal_zzbdb.mContext, com_google_android_gms_internal_zzbdb.mHandler);
        } else {
            this.zzaEt = null;
        }
    }

    @WorkerThread
    private final void zzb(zzbam com_google_android_gms_internal_zzbam) {
        com_google_android_gms_internal_zzbam.zza(this.zzaEp, zzmv());
        try {
            com_google_android_gms_internal_zzbam.zza(this);
        } catch (DeadObjectException e) {
            onConnectionSuspended(1);
            this.zzaCy.disconnect();
        }
    }

    @WorkerThread
    private final void zzi(ConnectionResult connectionResult) {
        for (zzbav zza : this.zzaEq) {
            zza.zza(this.zzaAK, connectionResult);
        }
        this.zzaEq.clear();
    }

    @WorkerThread
    private final void zzqq() {
        zzqt();
        zzi(ConnectionResult.zzazX);
        zzqv();
        for (zzbef com_google_android_gms_internal_zzbef : this.zzaEr.values()) {
            try {
                com_google_android_gms_internal_zzbef.zzaBu.zzb(this.zzaEo, new TaskCompletionSource());
            } catch (DeadObjectException e) {
                onConnectionSuspended(1);
                this.zzaCy.disconnect();
            } catch (RemoteException e2) {
            }
        }
        while (this.zzaCy.isConnected() && !this.zzaEn.isEmpty()) {
            zzb((zzbam) this.zzaEn.remove());
        }
        zzqw();
    }

    @WorkerThread
    private final void zzqr() {
        zzqt();
        this.zzaDA = true;
        this.zzaEp.zzpQ();
        this.zzaEm.mHandler.sendMessageDelayed(Message.obtain(this.zzaEm.mHandler, 9, this.zzaAK), this.zzaEm.zzaDC);
        this.zzaEm.mHandler.sendMessageDelayed(Message.obtain(this.zzaEm.mHandler, 11, this.zzaAK), this.zzaEm.zzaDB);
        this.zzaEm.zzaEg = -1;
    }

    @WorkerThread
    private final void zzqv() {
        if (this.zzaDA) {
            this.zzaEm.mHandler.removeMessages(11, this.zzaAK);
            this.zzaEm.mHandler.removeMessages(9, this.zzaAK);
            this.zzaDA = false;
        }
    }

    private final void zzqw() {
        this.zzaEm.mHandler.removeMessages(12, this.zzaAK);
        this.zzaEm.mHandler.sendMessageDelayed(this.zzaEm.mHandler.obtainMessage(12, this.zzaAK), this.zzaEm.zzaEe);
    }

    @WorkerThread
    public final void connect() {
        zzbo.zza(this.zzaEm.mHandler);
        if (!this.zzaCy.isConnected() && !this.zzaCy.isConnecting()) {
            if (this.zzaCy.zzpe() && this.zzaEm.zzaEg != 0) {
                this.zzaEm.zzaEg = this.zzaEm.zzaBd.isGooglePlayServicesAvailable(this.zzaEm.mContext);
                if (this.zzaEm.zzaEg != 0) {
                    onConnectionFailed(new ConnectionResult(this.zzaEm.zzaEg, null));
                    return;
                }
            }
            Object com_google_android_gms_internal_zzbdh = new zzbdh(this.zzaEm, this.zzaCy, this.zzaAK);
            if (this.zzaCy.zzmv()) {
                this.zzaEt.zza(com_google_android_gms_internal_zzbdh);
            }
            this.zzaCy.zza(com_google_android_gms_internal_zzbdh);
        }
    }

    public final int getInstanceId() {
        return this.zzaEs;
    }

    final boolean isConnected() {
        return this.zzaCy.isConnected();
    }

    public final void onConnected(@Nullable Bundle bundle) {
        if (Looper.myLooper() == this.zzaEm.mHandler.getLooper()) {
            zzqq();
        } else {
            this.zzaEm.mHandler.post(new zzbde(this));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.WorkerThread
    public final void onConnectionFailed(@android.support.annotation.NonNull com.google.android.gms.common.ConnectionResult r6) {
        /*
        r5 = this;
        r0 = r5.zzaEm;
        r0 = r0.mHandler;
        com.google.android.gms.common.internal.zzbo.zza(r0);
        r0 = r5.zzaEt;
        if (r0 == 0) goto L_0x0012;
    L_0x000d:
        r0 = r5.zzaEt;
        r0.zzqI();
    L_0x0012:
        r5.zzqt();
        r0 = r5.zzaEm;
        r1 = -1;
        r0.zzaEg = r1;
        r5.zzi(r6);
        r0 = r6.getErrorCode();
        r1 = 4;
        if (r0 != r1) goto L_0x002d;
    L_0x0025:
        r0 = com.google.android.gms.internal.zzbdb.zzaEd;
        r5.zzt(r0);
    L_0x002c:
        return;
    L_0x002d:
        r0 = r5.zzaEn;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x0038;
    L_0x0035:
        r5.zzaEu = r6;
        goto L_0x002c;
    L_0x0038:
        r1 = com.google.android.gms.internal.zzbdb.zzuF;
        monitor-enter(r1);
        r0 = r5.zzaEm;	 Catch:{ all -> 0x0060 }
        r0 = r0.zzaEj;	 Catch:{ all -> 0x0060 }
        if (r0 == 0) goto L_0x0063;
    L_0x0045:
        r0 = r5.zzaEm;	 Catch:{ all -> 0x0060 }
        r0 = r0.zzaEk;	 Catch:{ all -> 0x0060 }
        r2 = r5.zzaAK;	 Catch:{ all -> 0x0060 }
        r0 = r0.contains(r2);	 Catch:{ all -> 0x0060 }
        if (r0 == 0) goto L_0x0063;
    L_0x0053:
        r0 = r5.zzaEm;	 Catch:{ all -> 0x0060 }
        r0 = r0.zzaEj;	 Catch:{ all -> 0x0060 }
        r2 = r5.zzaEs;	 Catch:{ all -> 0x0060 }
        r0.zzb(r6, r2);	 Catch:{ all -> 0x0060 }
        monitor-exit(r1);	 Catch:{ all -> 0x0060 }
        goto L_0x002c;
    L_0x0060:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0060 }
        throw r0;
    L_0x0063:
        monitor-exit(r1);	 Catch:{ all -> 0x0060 }
        r0 = r5.zzaEm;
        r1 = r5.zzaEs;
        r0 = r0.zzc(r6, r1);
        if (r0 != 0) goto L_0x002c;
    L_0x006e:
        r0 = r6.getErrorCode();
        r1 = 18;
        if (r0 != r1) goto L_0x0079;
    L_0x0076:
        r0 = 1;
        r5.zzaDA = r0;
    L_0x0079:
        r0 = r5.zzaDA;
        if (r0 == 0) goto L_0x009b;
    L_0x007d:
        r0 = r5.zzaEm;
        r0 = r0.mHandler;
        r1 = r5.zzaEm;
        r1 = r1.mHandler;
        r2 = 9;
        r3 = r5.zzaAK;
        r1 = android.os.Message.obtain(r1, r2, r3);
        r2 = r5.zzaEm;
        r2 = r2.zzaDC;
        r0.sendMessageDelayed(r1, r2);
        goto L_0x002c;
    L_0x009b:
        r0 = new com.google.android.gms.common.api.Status;
        r1 = 17;
        r2 = r5.zzaAK;
        r2 = r2.zzpr();
        r2 = java.lang.String.valueOf(r2);
        r3 = java.lang.String.valueOf(r2);
        r3 = r3.length();
        r3 = r3 + 38;
        r4 = new java.lang.StringBuilder;
        r4.<init>(r3);
        r3 = "API: ";
        r3 = r4.append(r3);
        r2 = r3.append(r2);
        r3 = " is not available on this device.";
        r2 = r2.append(r3);
        r2 = r2.toString();
        r0.<init>(r1, r2);
        r5.zzt(r0);
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbdd.onConnectionFailed(com.google.android.gms.common.ConnectionResult):void");
    }

    public final void onConnectionSuspended(int i) {
        if (Looper.myLooper() == this.zzaEm.mHandler.getLooper()) {
            zzqr();
        } else {
            this.zzaEm.mHandler.post(new zzbdf(this));
        }
    }

    @WorkerThread
    public final void resume() {
        zzbo.zza(this.zzaEm.mHandler);
        if (this.zzaDA) {
            connect();
        }
    }

    @WorkerThread
    public final void signOut() {
        zzbo.zza(this.zzaEm.mHandler);
        zzt(zzbdb.zzaEc);
        this.zzaEp.zzpP();
        for (zzbdy com_google_android_gms_internal_zzbar : this.zzaEr.keySet()) {
            zza(new zzbar(com_google_android_gms_internal_zzbar, new TaskCompletionSource()));
        }
        zzi(new ConnectionResult(4));
        this.zzaCy.disconnect();
    }

    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
        if (Looper.myLooper() == this.zzaEm.mHandler.getLooper()) {
            onConnectionFailed(connectionResult);
        } else {
            this.zzaEm.mHandler.post(new zzbdg(this, connectionResult));
        }
    }

    @WorkerThread
    public final void zza(zzbam com_google_android_gms_internal_zzbam) {
        zzbo.zza(this.zzaEm.mHandler);
        if (this.zzaCy.isConnected()) {
            zzb(com_google_android_gms_internal_zzbam);
            zzqw();
            return;
        }
        this.zzaEn.add(com_google_android_gms_internal_zzbam);
        if (this.zzaEu == null || !this.zzaEu.hasResolution()) {
            connect();
        } else {
            onConnectionFailed(this.zzaEu);
        }
    }

    @WorkerThread
    public final void zza(zzbav com_google_android_gms_internal_zzbav) {
        zzbo.zza(this.zzaEm.mHandler);
        this.zzaEq.add(com_google_android_gms_internal_zzbav);
    }

    @WorkerThread
    public final void zzh(@NonNull ConnectionResult connectionResult) {
        zzbo.zza(this.zzaEm.mHandler);
        this.zzaCy.disconnect();
        onConnectionFailed(connectionResult);
    }

    public final boolean zzmv() {
        return this.zzaCy.zzmv();
    }

    public final zze zzpJ() {
        return this.zzaCy;
    }

    @WorkerThread
    public final void zzqd() {
        zzbo.zza(this.zzaEm.mHandler);
        if (this.zzaDA) {
            zzqv();
            zzt(this.zzaEm.zzaBd.isGooglePlayServicesAvailable(this.zzaEm.mContext) == 18 ? new Status(8, "Connection timed out while waiting for Google Play services update to complete.") : new Status(8, "API failed to connect while resuming due to an unknown error."));
            this.zzaCy.disconnect();
        }
    }

    public final Map<zzbdy<?>, zzbef> zzqs() {
        return this.zzaEr;
    }

    @WorkerThread
    public final void zzqt() {
        zzbo.zza(this.zzaEm.mHandler);
        this.zzaEu = null;
    }

    @WorkerThread
    public final ConnectionResult zzqu() {
        zzbo.zza(this.zzaEm.mHandler);
        return this.zzaEu;
    }

    @WorkerThread
    public final void zzqx() {
        zzbo.zza(this.zzaEm.mHandler);
        if (!this.zzaCy.isConnected() || this.zzaEr.size() != 0) {
            return;
        }
        if (this.zzaEp.zzpO()) {
            zzqw();
        } else {
            this.zzaCy.disconnect();
        }
    }

    final zzctk zzqy() {
        return this.zzaEt == null ? null : this.zzaEt.zzqy();
    }

    @WorkerThread
    public final void zzt(Status status) {
        zzbo.zza(this.zzaEm.mHandler);
        for (zzbam zzp : this.zzaEn) {
            zzp.zzp(status);
        }
        this.zzaEn.clear();
    }
}
