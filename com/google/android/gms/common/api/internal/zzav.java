package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClientEventManager;
import com.google.android.gms.common.internal.GmsClientEventManager.GmsClientEventState;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;

public final class zzav extends GoogleApiClient implements zzbq {
    private final Context mContext;
    private final Looper zzcn;
    private final int zzde;
    private final GoogleApiAvailability zzdg;
    private final AbstractClientBuilder<? extends SignInClient, SignInOptions> zzdh;
    private boolean zzdk;
    private final Lock zzga;
    private final ClientSettings zzgf;
    private final Map<Api<?>, Boolean> zzgi;
    final Queue<ApiMethodImpl<?, ?>> zzgo = new LinkedList();
    private final GmsClientEventManager zzie;
    private zzbp zzif = null;
    private volatile boolean zzig;
    private long zzih = 120000;
    private long zzii = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    private final zzba zzij;
    private GooglePlayServicesUpdatedReceiver zzik;
    final Map<AnyClientKey<?>, Client> zzil;
    Set<Scope> zzim = new HashSet();
    private final ListenerHolders zzin = new ListenerHolders();
    private final ArrayList<zzp> zzio;
    private Integer zzip = null;
    Set<zzch> zziq = null;
    final zzck zzir;
    private final GmsClientEventState zzis = new zzaw(this);

    public zzav(Context context, Lock lock, Looper looper, ClientSettings clientSettings, GoogleApiAvailability googleApiAvailability, AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder, Map<Api<?>, Boolean> map, List<ConnectionCallbacks> list, List<OnConnectionFailedListener> list2, Map<AnyClientKey<?>, Client> map2, int i, int i2, ArrayList<zzp> arrayList, boolean z) {
        Looper looper2 = looper;
        this.mContext = context;
        this.zzga = lock;
        this.zzdk = false;
        this.zzie = new GmsClientEventManager(looper2, this.zzis);
        this.zzcn = looper2;
        this.zzij = new zzba(this, looper2);
        this.zzdg = googleApiAvailability;
        this.zzde = i;
        if (this.zzde >= 0) {
            r0.zzip = Integer.valueOf(i2);
        }
        r0.zzgi = map;
        r0.zzil = map2;
        r0.zzio = arrayList;
        r0.zzir = new zzck(r0.zzil);
        for (ConnectionCallbacks registerConnectionCallbacks : list) {
            r0.zzie.registerConnectionCallbacks(registerConnectionCallbacks);
        }
        for (OnConnectionFailedListener registerConnectionFailedListener : list2) {
            r0.zzie.registerConnectionFailedListener(registerConnectionFailedListener);
        }
        r0.zzgf = clientSettings;
        r0.zzdh = abstractClientBuilder;
    }

    private final void resume() {
        this.zzga.lock();
        try {
            if (this.zzig) {
                zzax();
            }
            this.zzga.unlock();
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    public static int zza(Iterable<Client> iterable, boolean z) {
        int i = 0;
        int i2 = 0;
        for (Client client : iterable) {
            if (client.requiresSignIn()) {
                i = 1;
            }
            if (client.providesSignIn()) {
                i2 = 1;
            }
        }
        return i != 0 ? (i2 == 0 || !z) ? 1 : 2 : 3;
    }

    @GuardedBy("mLock")
    private final void zzax() {
        this.zzie.enableCallbacks();
        this.zzif.connect();
    }

    private final void zzay() {
        this.zzga.lock();
        try {
            if (zzaz()) {
                zzax();
            }
            this.zzga.unlock();
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    private final void zzg(int i) {
        if (this.zzip == null) {
            this.zzip = Integer.valueOf(i);
        } else if (this.zzip.intValue() != i) {
            String zzh = zzh(i);
            String zzh2 = zzh(this.zzip.intValue());
            StringBuilder stringBuilder = new StringBuilder((51 + String.valueOf(zzh).length()) + String.valueOf(zzh2).length());
            stringBuilder.append("Cannot use sign-in mode: ");
            stringBuilder.append(zzh);
            stringBuilder.append(". Mode was already set to ");
            stringBuilder.append(zzh2);
            throw new IllegalStateException(stringBuilder.toString());
        }
        if (this.zzif == null) {
            Object obj = null;
            Object obj2 = null;
            for (Client client : this.zzil.values()) {
                if (client.requiresSignIn()) {
                    obj = 1;
                }
                if (client.providesSignIn()) {
                    obj2 = 1;
                }
            }
            switch (this.zzip.intValue()) {
                case 1:
                    if (obj == null) {
                        throw new IllegalStateException("SIGN_IN_MODE_REQUIRED cannot be used on a GoogleApiClient that does not contain any authenticated APIs. Use connect() instead.");
                    } else if (obj2 != null) {
                        throw new IllegalStateException("Cannot use SIGN_IN_MODE_REQUIRED with GOOGLE_SIGN_IN_API. Use connect(SIGN_IN_MODE_OPTIONAL) instead.");
                    }
                    break;
                case 2:
                    if (obj != null) {
                        if (this.zzdk) {
                            this.zzif = new zzw(this.mContext, this.zzga, this.zzcn, this.zzdg, this.zzil, this.zzgf, this.zzgi, this.zzdh, this.zzio, this, true);
                            return;
                        } else {
                            this.zzif = zzr.zza(this.mContext, this, this.zzga, this.zzcn, this.zzdg, this.zzil, this.zzgf, this.zzgi, this.zzdh, this.zzio);
                            return;
                        }
                    }
                    break;
                case 3:
                    break;
                default:
                    break;
            }
            if (this.zzdk && obj2 == null) {
                this.zzif = new zzw(this.mContext, this.zzga, this.zzcn, this.zzdg, this.zzil, this.zzgf, this.zzgi, this.zzdh, this.zzio, this, false);
            } else {
                this.zzif = new zzbd(this.mContext, this, this.zzga, this.zzcn, this.zzdg, this.zzil, this.zzgf, this.zzgi, this.zzdh, this.zzio, this);
            }
        }
    }

    private static String zzh(int i) {
        switch (i) {
            case 1:
                return "SIGN_IN_MODE_REQUIRED";
            case 2:
                return "SIGN_IN_MODE_OPTIONAL";
            case 3:
                return "SIGN_IN_MODE_NONE";
            default:
                return "UNKNOWN";
        }
    }

    public final ConnectionResult blockingConnect() {
        boolean z = true;
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper(), "blockingConnect must not be called on the UI thread");
        this.zzga.lock();
        try {
            if (this.zzde >= 0) {
                if (this.zzip == null) {
                    z = false;
                }
                Preconditions.checkState(z, "Sign-in mode should have been set explicitly by auto-manage.");
            } else if (this.zzip == null) {
                this.zzip = Integer.valueOf(zza(this.zzil.values(), false));
            } else if (this.zzip.intValue() == 2) {
                throw new IllegalStateException("Cannot call blockingConnect() when sign-in mode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            zzg(this.zzip.intValue());
            this.zzie.enableCallbacks();
            ConnectionResult blockingConnect = this.zzif.blockingConnect();
            return blockingConnect;
        } finally {
            this.zzga.unlock();
        }
    }

    public final void connect() {
        this.zzga.lock();
        try {
            boolean z = false;
            if (this.zzde >= 0) {
                if (this.zzip != null) {
                    z = true;
                }
                Preconditions.checkState(z, "Sign-in mode should have been set explicitly by auto-manage.");
            } else if (this.zzip == null) {
                this.zzip = Integer.valueOf(zza(this.zzil.values(), false));
            } else if (this.zzip.intValue() == 2) {
                throw new IllegalStateException("Cannot call connect() when SignInMode is set to SIGN_IN_MODE_OPTIONAL. Call connect(SIGN_IN_MODE_OPTIONAL) instead.");
            }
            connect(this.zzip.intValue());
        } finally {
            this.zzga.unlock();
        }
    }

    public final void connect(int i) {
        this.zzga.lock();
        boolean z = true;
        if (!(i == 3 || i == 1)) {
            if (i != 2) {
                z = false;
            }
        }
        try {
            StringBuilder stringBuilder = new StringBuilder(33);
            stringBuilder.append("Illegal sign-in mode: ");
            stringBuilder.append(i);
            Preconditions.checkArgument(z, stringBuilder.toString());
            zzg(i);
            zzax();
        } finally {
            this.zzga.unlock();
        }
    }

    public final void disconnect() {
        this.zzga.lock();
        try {
            this.zzir.release();
            if (this.zzif != null) {
                this.zzif.disconnect();
            }
            this.zzin.release();
            for (ApiMethodImpl apiMethodImpl : this.zzgo) {
                apiMethodImpl.zza(null);
                apiMethodImpl.cancel();
            }
            this.zzgo.clear();
            if (this.zzif != null) {
                zzaz();
                this.zzie.disableCallbacks();
            }
            this.zzga.unlock();
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append(str).append("mContext=").println(this.mContext);
        printWriter.append(str).append("mResuming=").print(this.zzig);
        printWriter.append(" mWorkQueue.size()=").print(this.zzgo.size());
        printWriter.append(" mUnconsumedApiCalls.size()=").println(this.zzir.zzmo.size());
        if (this.zzif != null) {
            this.zzif.dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(T t) {
        Preconditions.checkArgument(t.getClientKey() != null, "This task can not be enqueued (it's probably a Batch or malformed)");
        boolean containsKey = this.zzil.containsKey(t.getClientKey());
        String name = t.getApi() != null ? t.getApi().getName() : "the API";
        StringBuilder stringBuilder = new StringBuilder(65 + String.valueOf(name).length());
        stringBuilder.append("GoogleApiClient is not configured to use ");
        stringBuilder.append(name);
        stringBuilder.append(" required for this call.");
        Preconditions.checkArgument(containsKey, stringBuilder.toString());
        this.zzga.lock();
        try {
            if (this.zzif == null) {
                this.zzgo.add(t);
            } else {
                t = this.zzif.enqueue(t);
            }
            this.zzga.unlock();
            return t;
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(T t) {
        Preconditions.checkArgument(t.getClientKey() != null, "This task can not be executed (it's probably a Batch or malformed)");
        boolean containsKey = this.zzil.containsKey(t.getClientKey());
        String name = t.getApi() != null ? t.getApi().getName() : "the API";
        StringBuilder stringBuilder = new StringBuilder(65 + String.valueOf(name).length());
        stringBuilder.append("GoogleApiClient is not configured to use ");
        stringBuilder.append(name);
        stringBuilder.append(" required for this call.");
        Preconditions.checkArgument(containsKey, stringBuilder.toString());
        this.zzga.lock();
        try {
            if (this.zzif == null) {
                throw new IllegalStateException("GoogleApiClient is not connected yet.");
            }
            if (this.zzig) {
                this.zzgo.add(t);
                while (!this.zzgo.isEmpty()) {
                    ApiMethodImpl apiMethodImpl = (ApiMethodImpl) this.zzgo.remove();
                    this.zzir.zzb(apiMethodImpl);
                    apiMethodImpl.setFailedResult(Status.RESULT_INTERNAL_ERROR);
                }
            } else {
                t = this.zzif.execute(t);
            }
            this.zzga.unlock();
            return t;
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    public final Looper getLooper() {
        return this.zzcn;
    }

    public final boolean isConnected() {
        return this.zzif != null && this.zzif.isConnected();
    }

    public final void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.zzie.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public final void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.zzie.unregisterConnectionFailedListener(onConnectionFailedListener);
    }

    public final void zza(zzch com_google_android_gms_common_api_internal_zzch) {
        this.zzga.lock();
        try {
            if (this.zziq == null) {
                this.zziq = new HashSet();
            }
            this.zziq.add(com_google_android_gms_common_api_internal_zzch);
        } finally {
            this.zzga.unlock();
        }
    }

    @GuardedBy("mLock")
    final boolean zzaz() {
        if (!this.zzig) {
            return false;
        }
        this.zzig = false;
        this.zzij.removeMessages(2);
        this.zzij.removeMessages(1);
        if (this.zzik != null) {
            this.zzik.unregister();
            this.zzik = null;
        }
        return true;
    }

    @GuardedBy("mLock")
    public final void zzb(int i, boolean z) {
        if (!(i != 1 || z || this.zzig)) {
            this.zzig = true;
            if (this.zzik == null) {
                this.zzik = this.zzdg.registerCallbackOnUpdate(this.mContext.getApplicationContext(), new zzbb(this));
            }
            this.zzij.sendMessageDelayed(this.zzij.obtainMessage(1), this.zzih);
            this.zzij.sendMessageDelayed(this.zzij.obtainMessage(2), this.zzii);
        }
        this.zzir.zzce();
        this.zzie.onUnintentionalDisconnection(i);
        this.zzie.disableCallbacks();
        if (i == 2) {
            zzax();
        }
    }

    @GuardedBy("mLock")
    public final void zzb(Bundle bundle) {
        while (!this.zzgo.isEmpty()) {
            execute((ApiMethodImpl) this.zzgo.remove());
        }
        this.zzie.onConnectionSuccess(bundle);
    }

    public final void zzb(zzch com_google_android_gms_common_api_internal_zzch) {
        this.zzga.lock();
        try {
            String str;
            String str2;
            Throwable exception;
            if (this.zziq == null) {
                str = "GoogleApiClientImpl";
                str2 = "Attempted to remove pending transform when no transforms are registered.";
                exception = new Exception();
            } else if (this.zziq.remove(com_google_android_gms_common_api_internal_zzch)) {
                if (!zzba()) {
                    this.zzif.zzz();
                }
                this.zzga.unlock();
            } else {
                str = "GoogleApiClientImpl";
                str2 = "Failed to remove pending transform - this may lead to memory leaks!";
                exception = new Exception();
            }
            Log.wtf(str, str2, exception);
            this.zzga.unlock();
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    final boolean zzba() {
        this.zzga.lock();
        try {
            if (this.zziq == null) {
                return false;
            }
            boolean isEmpty = this.zziq.isEmpty() ^ 1;
            this.zzga.unlock();
            return isEmpty;
        } finally {
            this.zzga.unlock();
        }
    }

    final String zzbb() {
        Writer stringWriter = new StringWriter();
        dump(TtmlNode.ANONYMOUS_REGION_ID, null, new PrintWriter(stringWriter), null);
        return stringWriter.toString();
    }

    @GuardedBy("mLock")
    public final void zzc(ConnectionResult connectionResult) {
        if (!this.zzdg.isPlayServicesPossiblyUpdating(this.mContext, connectionResult.getErrorCode())) {
            zzaz();
        }
        if (!this.zzig) {
            this.zzie.onConnectionFailure(connectionResult);
            this.zzie.disableCallbacks();
        }
    }
}
