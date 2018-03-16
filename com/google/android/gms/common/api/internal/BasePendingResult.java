package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzaq;
import com.google.android.gms.common.internal.zzbq;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@KeepName
public abstract class BasePendingResult<R extends Result> extends PendingResult<R> {
    static final ThreadLocal<Boolean> zzfot = new zzs();
    @KeepName
    private zzb mResultGuardian;
    private Status mStatus;
    private boolean zzan;
    private final CountDownLatch zzapd;
    private R zzfne;
    private final Object zzfou;
    private zza<R> zzfov;
    private WeakReference<GoogleApiClient> zzfow;
    private final ArrayList<com.google.android.gms.common.api.PendingResult.zza> zzfox;
    private ResultCallback<? super R> zzfoy;
    private final AtomicReference<zzdm> zzfoz;
    private volatile boolean zzfpa;
    private boolean zzfpb;
    private zzaq zzfpc;
    private volatile zzdg<R> zzfpd;
    private boolean zzfpe;

    public static class zza<R extends Result> extends Handler {
        public zza() {
            this(Looper.getMainLooper());
        }

        public zza(Looper looper) {
            super(looper);
        }

        public final void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Pair pair = (Pair) message.obj;
                    ResultCallback resultCallback = (ResultCallback) pair.first;
                    Result result = (Result) pair.second;
                    try {
                        resultCallback.onResult(result);
                        return;
                    } catch (RuntimeException e) {
                        BasePendingResult.zzd(result);
                        throw e;
                    }
                case 2:
                    ((BasePendingResult) message.obj).zzv(Status.zzfnl);
                    return;
                default:
                    Log.wtf("BasePendingResult", "Don't know how to handle message: " + message.what, new Exception());
                    return;
            }
        }

        public final void zza(ResultCallback<? super R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }
    }

    final class zzb {
        private /* synthetic */ BasePendingResult zzfpf;

        private zzb(BasePendingResult basePendingResult) {
            this.zzfpf = basePendingResult;
        }

        protected final void finalize() throws Throwable {
            BasePendingResult.zzd(this.zzfpf.zzfne);
            super.finalize();
        }
    }

    @Deprecated
    BasePendingResult() {
        this.zzfou = new Object();
        this.zzapd = new CountDownLatch(1);
        this.zzfox = new ArrayList();
        this.zzfoz = new AtomicReference();
        this.zzfpe = false;
        this.zzfov = new zza(Looper.getMainLooper());
        this.zzfow = new WeakReference(null);
    }

    protected BasePendingResult(GoogleApiClient googleApiClient) {
        this.zzfou = new Object();
        this.zzapd = new CountDownLatch(1);
        this.zzfox = new ArrayList();
        this.zzfoz = new AtomicReference();
        this.zzfpe = false;
        this.zzfov = new zza(googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
        this.zzfow = new WeakReference(googleApiClient);
    }

    private final R get() {
        R r;
        boolean z = true;
        synchronized (this.zzfou) {
            if (this.zzfpa) {
                z = false;
            }
            zzbq.zza(z, "Result has already been consumed.");
            zzbq.zza(isReady(), "Result is not ready.");
            r = this.zzfne;
            this.zzfne = null;
            this.zzfoy = null;
            this.zzfpa = true;
        }
        zzdm com_google_android_gms_common_api_internal_zzdm = (zzdm) this.zzfoz.getAndSet(null);
        if (com_google_android_gms_common_api_internal_zzdm != null) {
            com_google_android_gms_common_api_internal_zzdm.zzc(this);
        }
        return r;
    }

    private final void zzc(R r) {
        this.zzfne = r;
        this.zzfpc = null;
        this.zzapd.countDown();
        this.mStatus = this.zzfne.getStatus();
        if (this.zzan) {
            this.zzfoy = null;
        } else if (this.zzfoy != null) {
            this.zzfov.removeMessages(2);
            this.zzfov.zza(this.zzfoy, get());
        } else if (this.zzfne instanceof Releasable) {
            this.mResultGuardian = new zzb();
        }
        ArrayList arrayList = this.zzfox;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((com.google.android.gms.common.api.PendingResult.zza) obj).zzr(this.mStatus);
        }
        this.zzfox.clear();
    }

    public static void zzd(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (Throwable e) {
                String valueOf = String.valueOf(result);
                Log.w("BasePendingResult", new StringBuilder(String.valueOf(valueOf).length() + 18).append("Unable to release ").append(valueOf).toString(), e);
            }
        }
    }

    public final R await() {
        boolean z = true;
        zzbq.zzgn("await must not be called on the UI thread");
        zzbq.zza(!this.zzfpa, "Result has already been consumed");
        if (this.zzfpd != null) {
            z = false;
        }
        zzbq.zza(z, "Cannot await if then() has been called.");
        try {
            this.zzapd.await();
        } catch (InterruptedException e) {
            zzv(Status.zzfnj);
        }
        zzbq.zza(isReady(), "Result is not ready.");
        return get();
    }

    public final R await(long j, TimeUnit timeUnit) {
        boolean z = true;
        if (j > 0) {
            zzbq.zzgn("await must not be called on the UI thread when time is greater than zero.");
        }
        zzbq.zza(!this.zzfpa, "Result has already been consumed.");
        if (this.zzfpd != null) {
            z = false;
        }
        zzbq.zza(z, "Cannot await if then() has been called.");
        try {
            if (!this.zzapd.await(j, timeUnit)) {
                zzv(Status.zzfnl);
            }
        } catch (InterruptedException e) {
            zzv(Status.zzfnj);
        }
        zzbq.zza(isReady(), "Result is not ready.");
        return get();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel() {
        /*
        r2 = this;
        r1 = r2.zzfou;
        monitor-enter(r1);
        r0 = r2.zzan;	 Catch:{ all -> 0x0029 }
        if (r0 != 0) goto L_0x000b;
    L_0x0007:
        r0 = r2.zzfpa;	 Catch:{ all -> 0x0029 }
        if (r0 == 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r1);	 Catch:{ all -> 0x0029 }
    L_0x000c:
        return;
    L_0x000d:
        r0 = r2.zzfpc;	 Catch:{ all -> 0x0029 }
        if (r0 == 0) goto L_0x0016;
    L_0x0011:
        r0 = r2.zzfpc;	 Catch:{ RemoteException -> 0x002c }
        r0.cancel();	 Catch:{ RemoteException -> 0x002c }
    L_0x0016:
        r0 = r2.zzfne;	 Catch:{ all -> 0x0029 }
        zzd(r0);	 Catch:{ all -> 0x0029 }
        r0 = 1;
        r2.zzan = r0;	 Catch:{ all -> 0x0029 }
        r0 = com.google.android.gms.common.api.Status.zzfnm;	 Catch:{ all -> 0x0029 }
        r0 = r2.zzb(r0);	 Catch:{ all -> 0x0029 }
        r2.zzc(r0);	 Catch:{ all -> 0x0029 }
        monitor-exit(r1);	 Catch:{ all -> 0x0029 }
        goto L_0x000c;
    L_0x0029:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0029 }
        throw r0;
    L_0x002c:
        r0 = move-exception;
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.cancel():void");
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this.zzfou) {
            z = this.zzan;
        }
        return z;
    }

    public final boolean isReady() {
        return this.zzapd.getCount() == 0;
    }

    public final void setResult(R r) {
        boolean z = true;
        synchronized (this.zzfou) {
            if (this.zzfpb || this.zzan) {
                zzd(r);
                return;
            }
            if (isReady()) {
            }
            zzbq.zza(!isReady(), "Results have already been set");
            if (this.zzfpa) {
                z = false;
            }
            zzbq.zza(z, "Result has already been consumed");
            zzc(r);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r6) {
        /*
        r5 = this;
        r0 = 1;
        r1 = 0;
        r3 = r5.zzfou;
        monitor-enter(r3);
        if (r6 != 0) goto L_0x000c;
    L_0x0007:
        r0 = 0;
        r5.zzfoy = r0;	 Catch:{ all -> 0x0029 }
        monitor-exit(r3);	 Catch:{ all -> 0x0029 }
    L_0x000b:
        return;
    L_0x000c:
        r2 = r5.zzfpa;	 Catch:{ all -> 0x0029 }
        if (r2 != 0) goto L_0x002c;
    L_0x0010:
        r2 = r0;
    L_0x0011:
        r4 = "Result has already been consumed.";
        com.google.android.gms.common.internal.zzbq.zza(r2, r4);	 Catch:{ all -> 0x0029 }
        r2 = r5.zzfpd;	 Catch:{ all -> 0x0029 }
        if (r2 != 0) goto L_0x002e;
    L_0x001b:
        r1 = "Cannot set callbacks if then() has been called.";
        com.google.android.gms.common.internal.zzbq.zza(r0, r1);	 Catch:{ all -> 0x0029 }
        r0 = r5.isCanceled();	 Catch:{ all -> 0x0029 }
        if (r0 == 0) goto L_0x0030;
    L_0x0027:
        monitor-exit(r3);	 Catch:{ all -> 0x0029 }
        goto L_0x000b;
    L_0x0029:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0029 }
        throw r0;
    L_0x002c:
        r2 = r1;
        goto L_0x0011;
    L_0x002e:
        r0 = r1;
        goto L_0x001b;
    L_0x0030:
        r0 = r5.isReady();	 Catch:{ all -> 0x0029 }
        if (r0 == 0) goto L_0x0041;
    L_0x0036:
        r0 = r5.zzfov;	 Catch:{ all -> 0x0029 }
        r1 = r5.get();	 Catch:{ all -> 0x0029 }
        r0.zza(r6, r1);	 Catch:{ all -> 0x0029 }
    L_0x003f:
        monitor-exit(r3);	 Catch:{ all -> 0x0029 }
        goto L_0x000b;
    L_0x0041:
        r5.zzfoy = r6;	 Catch:{ all -> 0x0029 }
        goto L_0x003f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.setResultCallback(com.google.android.gms.common.api.ResultCallback):void");
    }

    public final void zza(com.google.android.gms.common.api.PendingResult.zza com_google_android_gms_common_api_PendingResult_zza) {
        zzbq.checkArgument(com_google_android_gms_common_api_PendingResult_zza != null, "Callback cannot be null.");
        synchronized (this.zzfou) {
            if (isReady()) {
                com_google_android_gms_common_api_PendingResult_zza.zzr(this.mStatus);
            } else {
                this.zzfox.add(com_google_android_gms_common_api_PendingResult_zza);
            }
        }
    }

    public final void zza(zzdm com_google_android_gms_common_api_internal_zzdm) {
        this.zzfoz.set(com_google_android_gms_common_api_internal_zzdm);
    }

    public final Integer zzagv() {
        return null;
    }

    public final boolean zzahh() {
        boolean isCanceled;
        synchronized (this.zzfou) {
            if (((GoogleApiClient) this.zzfow.get()) == null || !this.zzfpe) {
                cancel();
            }
            isCanceled = isCanceled();
        }
        return isCanceled;
    }

    public final void zzahi() {
        boolean z = this.zzfpe || ((Boolean) zzfot.get()).booleanValue();
        this.zzfpe = z;
    }

    protected abstract R zzb(Status status);

    public final void zzv(Status status) {
        synchronized (this.zzfou) {
            if (!isReady()) {
                setResult(zzb(status));
                this.zzfpb = true;
            }
        }
    }
}
