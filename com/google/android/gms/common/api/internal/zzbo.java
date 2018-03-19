package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzbz;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.internal.zzcxd;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class zzbo<O extends ApiOptions> implements ConnectionCallbacks, OnConnectionFailedListener, zzu {
    private final zzh<O> zzfmf;
    private final zze zzfpv;
    private boolean zzfrw;
    final /* synthetic */ zzbm zzfti;
    private final Queue<zza> zzftj = new LinkedList();
    private final zzb zzftk;
    private final zzae zzftl;
    private final Set<zzj> zzftm = new HashSet();
    private final Map<zzck<?>, zzcr> zzftn = new HashMap();
    private final int zzfto;
    private final zzcv zzftp;
    private ConnectionResult zzftq = null;

    public zzbo(zzbm com_google_android_gms_common_api_internal_zzbm, GoogleApi<O> googleApi) {
        this.zzfti = com_google_android_gms_common_api_internal_zzbm;
        this.zzfpv = googleApi.zza(com_google_android_gms_common_api_internal_zzbm.mHandler.getLooper(), this);
        if (this.zzfpv instanceof zzbz) {
            this.zzftk = zzbz.zzals();
        } else {
            this.zzftk = this.zzfpv;
        }
        this.zzfmf = googleApi.zzagn();
        this.zzftl = new zzae();
        this.zzfto = googleApi.getInstanceId();
        if (this.zzfpv.zzaay()) {
            this.zzftp = googleApi.zza(com_google_android_gms_common_api_internal_zzbm.mContext, com_google_android_gms_common_api_internal_zzbm.mHandler);
        } else {
            this.zzftp = null;
        }
    }

    private final void zzaiw() {
        zzaiz();
        zzi(ConnectionResult.zzfkr);
        zzajb();
        for (zzcr com_google_android_gms_common_api_internal_zzcr : this.zzftn.values()) {
            try {
                com_google_android_gms_common_api_internal_zzcr.zzfnq.zzb(this.zzftk, new TaskCompletionSource());
            } catch (DeadObjectException e) {
                onConnectionSuspended(1);
                this.zzfpv.disconnect();
            } catch (RemoteException e2) {
            }
        }
        while (this.zzfpv.isConnected() && !this.zzftj.isEmpty()) {
            zzb((zza) this.zzftj.remove());
        }
        zzajc();
    }

    private final void zzaix() {
        zzaiz();
        this.zzfrw = true;
        this.zzftl.zzahw();
        this.zzfti.mHandler.sendMessageDelayed(Message.obtain(this.zzfti.mHandler, 9, this.zzfmf), this.zzfti.zzfry);
        this.zzfti.mHandler.sendMessageDelayed(Message.obtain(this.zzfti.mHandler, 11, this.zzfmf), this.zzfti.zzfrx);
        this.zzfti.zzftc = -1;
    }

    private final void zzajb() {
        if (this.zzfrw) {
            this.zzfti.mHandler.removeMessages(11, this.zzfmf);
            this.zzfti.mHandler.removeMessages(9, this.zzfmf);
            this.zzfrw = false;
        }
    }

    private final void zzajc() {
        this.zzfti.mHandler.removeMessages(12, this.zzfmf);
        this.zzfti.mHandler.sendMessageDelayed(this.zzfti.mHandler.obtainMessage(12, this.zzfmf), this.zzfti.zzfta);
    }

    private final void zzb(zza com_google_android_gms_common_api_internal_zza) {
        com_google_android_gms_common_api_internal_zza.zza(this.zzftl, zzaay());
        try {
            com_google_android_gms_common_api_internal_zza.zza(this);
        } catch (DeadObjectException e) {
            onConnectionSuspended(1);
            this.zzfpv.disconnect();
        }
    }

    private final void zzi(ConnectionResult connectionResult) {
        for (zzj com_google_android_gms_common_api_internal_zzj : this.zzftm) {
            String str = null;
            if (connectionResult == ConnectionResult.zzfkr) {
                str = this.zzfpv.zzagi();
            }
            com_google_android_gms_common_api_internal_zzj.zza(this.zzfmf, connectionResult, str);
        }
        this.zzftm.clear();
    }

    public final void connect() {
        zzbq.zza(this.zzfti.mHandler);
        if (!this.zzfpv.isConnected() && !this.zzfpv.isConnecting()) {
            if (this.zzfpv.zzagg() && this.zzfti.zzftc != 0) {
                this.zzfti.zzftc = this.zzfti.zzfmy.isGooglePlayServicesAvailable(this.zzfti.mContext);
                if (this.zzfti.zzftc != 0) {
                    onConnectionFailed(new ConnectionResult(this.zzfti.zzftc, null));
                    return;
                }
            }
            zzj com_google_android_gms_common_api_internal_zzbu = new zzbu(this.zzfti, this.zzfpv, this.zzfmf);
            if (this.zzfpv.zzaay()) {
                this.zzftp.zza((zzcy) com_google_android_gms_common_api_internal_zzbu);
            }
            this.zzfpv.zza(com_google_android_gms_common_api_internal_zzbu);
        }
    }

    public final int getInstanceId() {
        return this.zzfto;
    }

    final boolean isConnected() {
        return this.zzfpv.isConnected();
    }

    public final void onConnected(Bundle bundle) {
        if (Looper.myLooper() == this.zzfti.mHandler.getLooper()) {
            zzaiw();
        } else {
            this.zzfti.mHandler.post(new zzbp(this));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onConnectionFailed(com.google.android.gms.common.ConnectionResult r6) {
        /*
        r5 = this;
        r0 = r5.zzfti;
        r0 = r0.mHandler;
        com.google.android.gms.common.internal.zzbq.zza(r0);
        r0 = r5.zzftp;
        if (r0 == 0) goto L_0x0012;
    L_0x000d:
        r0 = r5.zzftp;
        r0.zzajq();
    L_0x0012:
        r5.zzaiz();
        r0 = r5.zzfti;
        r1 = -1;
        r0.zzftc = r1;
        r5.zzi(r6);
        r0 = r6.getErrorCode();
        r1 = 4;
        if (r0 != r1) goto L_0x002d;
    L_0x0025:
        r0 = com.google.android.gms.common.api.internal.zzbm.zzfsz;
        r5.zzw(r0);
    L_0x002c:
        return;
    L_0x002d:
        r0 = r5.zzftj;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x0038;
    L_0x0035:
        r5.zzftq = r6;
        goto L_0x002c;
    L_0x0038:
        r1 = com.google.android.gms.common.api.internal.zzbm.sLock;
        monitor-enter(r1);
        r0 = r5.zzfti;	 Catch:{ all -> 0x0060 }
        r0 = r0.zzftf;	 Catch:{ all -> 0x0060 }
        if (r0 == 0) goto L_0x0063;
    L_0x0045:
        r0 = r5.zzfti;	 Catch:{ all -> 0x0060 }
        r0 = r0.zzftg;	 Catch:{ all -> 0x0060 }
        r2 = r5.zzfmf;	 Catch:{ all -> 0x0060 }
        r0 = r0.contains(r2);	 Catch:{ all -> 0x0060 }
        if (r0 == 0) goto L_0x0063;
    L_0x0053:
        r0 = r5.zzfti;	 Catch:{ all -> 0x0060 }
        r0 = r0.zzftf;	 Catch:{ all -> 0x0060 }
        r2 = r5.zzfto;	 Catch:{ all -> 0x0060 }
        r0.zzb(r6, r2);	 Catch:{ all -> 0x0060 }
        monitor-exit(r1);	 Catch:{ all -> 0x0060 }
        goto L_0x002c;
    L_0x0060:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0060 }
        throw r0;
    L_0x0063:
        monitor-exit(r1);	 Catch:{ all -> 0x0060 }
        r0 = r5.zzfti;
        r1 = r5.zzfto;
        r0 = r0.zzc(r6, r1);
        if (r0 != 0) goto L_0x002c;
    L_0x006e:
        r0 = r6.getErrorCode();
        r1 = 18;
        if (r0 != r1) goto L_0x0079;
    L_0x0076:
        r0 = 1;
        r5.zzfrw = r0;
    L_0x0079:
        r0 = r5.zzfrw;
        if (r0 == 0) goto L_0x009b;
    L_0x007d:
        r0 = r5.zzfti;
        r0 = r0.mHandler;
        r1 = r5.zzfti;
        r1 = r1.mHandler;
        r2 = 9;
        r3 = r5.zzfmf;
        r1 = android.os.Message.obtain(r1, r2, r3);
        r2 = r5.zzfti;
        r2 = r2.zzfry;
        r0.sendMessageDelayed(r1, r2);
        goto L_0x002c;
    L_0x009b:
        r0 = new com.google.android.gms.common.api.Status;
        r1 = 17;
        r2 = r5.zzfmf;
        r2 = r2.zzagy();
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
        r5.zzw(r0);
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzbo.onConnectionFailed(com.google.android.gms.common.ConnectionResult):void");
    }

    public final void onConnectionSuspended(int i) {
        if (Looper.myLooper() == this.zzfti.mHandler.getLooper()) {
            zzaix();
        } else {
            this.zzfti.mHandler.post(new zzbq(this));
        }
    }

    public final void resume() {
        zzbq.zza(this.zzfti.mHandler);
        if (this.zzfrw) {
            connect();
        }
    }

    public final void signOut() {
        zzbq.zza(this.zzfti.mHandler);
        zzw(zzbm.zzfsy);
        this.zzftl.zzahv();
        for (zzck com_google_android_gms_common_api_internal_zzf : (zzck[]) this.zzftn.keySet().toArray(new zzck[this.zzftn.size()])) {
            zza(new zzf(com_google_android_gms_common_api_internal_zzf, new TaskCompletionSource()));
        }
        zzi(new ConnectionResult(4));
        if (this.zzfpv.isConnected()) {
            this.zzfpv.zza(new zzbs(this));
        }
    }

    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
        if (Looper.myLooper() == this.zzfti.mHandler.getLooper()) {
            onConnectionFailed(connectionResult);
        } else {
            this.zzfti.mHandler.post(new zzbr(this, connectionResult));
        }
    }

    public final void zza(zza com_google_android_gms_common_api_internal_zza) {
        zzbq.zza(this.zzfti.mHandler);
        if (this.zzfpv.isConnected()) {
            zzb(com_google_android_gms_common_api_internal_zza);
            zzajc();
            return;
        }
        this.zzftj.add(com_google_android_gms_common_api_internal_zza);
        if (this.zzftq == null || !this.zzftq.hasResolution()) {
            connect();
        } else {
            onConnectionFailed(this.zzftq);
        }
    }

    public final void zza(zzj com_google_android_gms_common_api_internal_zzj) {
        zzbq.zza(this.zzfti.mHandler);
        this.zzftm.add(com_google_android_gms_common_api_internal_zzj);
    }

    public final boolean zzaay() {
        return this.zzfpv.zzaay();
    }

    public final zze zzahp() {
        return this.zzfpv;
    }

    public final void zzaij() {
        zzbq.zza(this.zzfti.mHandler);
        if (this.zzfrw) {
            zzajb();
            zzw(this.zzfti.zzfmy.isGooglePlayServicesAvailable(this.zzfti.mContext) == 18 ? new Status(8, "Connection timed out while waiting for Google Play services update to complete.") : new Status(8, "API failed to connect while resuming due to an unknown error."));
            this.zzfpv.disconnect();
        }
    }

    public final Map<zzck<?>, zzcr> zzaiy() {
        return this.zzftn;
    }

    public final void zzaiz() {
        zzbq.zza(this.zzfti.mHandler);
        this.zzftq = null;
    }

    public final ConnectionResult zzaja() {
        zzbq.zza(this.zzfti.mHandler);
        return this.zzftq;
    }

    public final void zzajd() {
        zzbq.zza(this.zzfti.mHandler);
        if (!this.zzfpv.isConnected() || this.zzftn.size() != 0) {
            return;
        }
        if (this.zzftl.zzahu()) {
            zzajc();
        } else {
            this.zzfpv.disconnect();
        }
    }

    final zzcxd zzaje() {
        return this.zzftp == null ? null : this.zzftp.zzaje();
    }

    public final void zzh(ConnectionResult connectionResult) {
        zzbq.zza(this.zzfti.mHandler);
        this.zzfpv.disconnect();
        onConnectionFailed(connectionResult);
    }

    public final void zzw(Status status) {
        zzbq.zza(this.zzfti.mHandler);
        for (zza zzs : this.zzftj) {
            zzs.zzs(status);
        }
        this.zzftj.clear();
    }
}
