package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

final class zzr implements zzbp {
    private final Context mContext;
    private final Looper zzcn;
    private final zzav zzfq;
    private final zzbd zzfr;
    private final zzbd zzfs;
    private final Map<AnyClientKey<?>, zzbd> zzft;
    private final Set<SignInConnectionListener> zzfu = Collections.newSetFromMap(new WeakHashMap());
    private final Client zzfv;
    private Bundle zzfw;
    private ConnectionResult zzfx = null;
    private ConnectionResult zzfy = null;
    private boolean zzfz = false;
    private final Lock zzga;
    @GuardedBy("mLock")
    private int zzgb = 0;

    private zzr(Context context, zzav com_google_android_gms_common_api_internal_zzav, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<AnyClientKey<?>, Client> map, Map<AnyClientKey<?>, Client> map2, ClientSettings clientSettings, AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder, Client client, ArrayList<zzp> arrayList, ArrayList<zzp> arrayList2, Map<Api<?>, Boolean> map3, Map<Api<?>, Boolean> map4) {
        this.mContext = context;
        this.zzfq = com_google_android_gms_common_api_internal_zzav;
        this.zzga = lock;
        this.zzcn = looper;
        this.zzfv = client;
        this.zzfr = new zzbd(context, this.zzfq, lock, looper, googleApiAvailabilityLight, map2, null, map4, null, arrayList2, new zzt());
        this.zzfs = new zzbd(context, this.zzfq, lock, looper, googleApiAvailabilityLight, map, clientSettings, map3, abstractClientBuilder, arrayList, new zzu());
        Map arrayMap = new ArrayMap();
        for (AnyClientKey put : map2.keySet()) {
            arrayMap.put(put, this.zzfr);
        }
        for (AnyClientKey put2 : map.keySet()) {
            arrayMap.put(put2, this.zzfs);
        }
        this.zzft = Collections.unmodifiableMap(arrayMap);
    }

    public static zzr zza(Context context, zzav com_google_android_gms_common_api_internal_zzav, Lock lock, Looper looper, GoogleApiAvailabilityLight googleApiAvailabilityLight, Map<AnyClientKey<?>, Client> map, ClientSettings clientSettings, Map<Api<?>, Boolean> map2, AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder, ArrayList<zzp> arrayList) {
        Client client = null;
        Map arrayMap = new ArrayMap();
        Map arrayMap2 = new ArrayMap();
        for (Entry entry : map.entrySet()) {
            Client client2 = (Client) entry.getValue();
            if (client2.providesSignIn()) {
                client = client2;
            }
            if (client2.requiresSignIn()) {
                arrayMap.put((AnyClientKey) entry.getKey(), client2);
            } else {
                arrayMap2.put((AnyClientKey) entry.getKey(), client2);
            }
        }
        Preconditions.checkState(!arrayMap.isEmpty(), "CompositeGoogleApiClient should not be used without any APIs that require sign-in.");
        Map arrayMap3 = new ArrayMap();
        Map arrayMap4 = new ArrayMap();
        for (Api api : map2.keySet()) {
            AnyClientKey clientKey = api.getClientKey();
            if (arrayMap.containsKey(clientKey)) {
                arrayMap3.put(api, (Boolean) map2.get(api));
            } else if (arrayMap2.containsKey(clientKey)) {
                arrayMap4.put(api, (Boolean) map2.get(api));
            } else {
                throw new IllegalStateException("Each API in the isOptionalMap must have a corresponding client in the clients map.");
            }
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = arrayList;
        int size = arrayList4.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList4.get(i);
            i++;
            zzp com_google_android_gms_common_api_internal_zzp = (zzp) obj;
            if (arrayMap3.containsKey(com_google_android_gms_common_api_internal_zzp.mApi)) {
                arrayList2.add(com_google_android_gms_common_api_internal_zzp);
            } else if (arrayMap4.containsKey(com_google_android_gms_common_api_internal_zzp.mApi)) {
                arrayList3.add(com_google_android_gms_common_api_internal_zzp);
            } else {
                throw new IllegalStateException("Each ClientCallbacks must have a corresponding API in the isOptionalMap");
            }
        }
        return new zzr(context, com_google_android_gms_common_api_internal_zzav, lock, looper, googleApiAvailabilityLight, arrayMap, arrayMap2, clientSettings, abstractClientBuilder, client, arrayList2, arrayList3, arrayMap3, arrayMap4);
    }

    @GuardedBy("mLock")
    private final void zza(int i, boolean z) {
        this.zzfq.zzb(i, z);
        this.zzfy = null;
        this.zzfx = null;
    }

    private final void zza(Bundle bundle) {
        if (this.zzfw == null) {
            this.zzfw = bundle;
        } else if (bundle != null) {
            this.zzfw.putAll(bundle);
        }
    }

    @GuardedBy("mLock")
    private final void zza(ConnectionResult connectionResult) {
        switch (this.zzgb) {
            case 1:
                break;
            case 2:
                this.zzfq.zzc(connectionResult);
                break;
            default:
                Log.wtf("CompositeGAC", "Attempted to call failure callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new Exception());
                break;
        }
        zzab();
        this.zzgb = 0;
    }

    private final boolean zza(ApiMethodImpl<? extends Result, ? extends AnyClient> apiMethodImpl) {
        AnyClientKey clientKey = apiMethodImpl.getClientKey();
        Preconditions.checkArgument(this.zzft.containsKey(clientKey), "GoogleApiClient is not configured to use the API required for this call.");
        return ((zzbd) this.zzft.get(clientKey)).equals(this.zzfs);
    }

    @GuardedBy("mLock")
    private final void zzaa() {
        if (zzb(this.zzfx)) {
            if (zzb(this.zzfy) || zzac()) {
                switch (this.zzgb) {
                    case 1:
                        break;
                    case 2:
                        this.zzfq.zzb(this.zzfw);
                        break;
                    default:
                        Log.wtf("CompositeGAC", "Attempted to call success callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new AssertionError());
                        break;
                }
                zzab();
                this.zzgb = 0;
            } else if (this.zzfy == null) {
            } else {
                if (this.zzgb == 1) {
                    zzab();
                    return;
                }
                zza(this.zzfy);
                this.zzfr.disconnect();
            }
        } else if (this.zzfx != null && zzb(this.zzfy)) {
            this.zzfs.disconnect();
            zza(this.zzfx);
        } else if (this.zzfx != null && this.zzfy != null) {
            ConnectionResult connectionResult = this.zzfx;
            if (this.zzfs.zzje < this.zzfr.zzje) {
                connectionResult = this.zzfy;
            }
            zza(connectionResult);
        }
    }

    @GuardedBy("mLock")
    private final void zzab() {
        for (SignInConnectionListener onComplete : this.zzfu) {
            onComplete.onComplete();
        }
        this.zzfu.clear();
    }

    @GuardedBy("mLock")
    private final boolean zzac() {
        return this.zzfy != null && this.zzfy.getErrorCode() == 4;
    }

    private final PendingIntent zzad() {
        return this.zzfv == null ? null : PendingIntent.getActivity(this.mContext, System.identityHashCode(this.zzfq), this.zzfv.getSignInIntent(), 134217728);
    }

    private static boolean zzb(ConnectionResult connectionResult) {
        return connectionResult != null && connectionResult.isSuccess();
    }

    @GuardedBy("mLock")
    public final ConnectionResult blockingConnect() {
        throw new UnsupportedOperationException();
    }

    @GuardedBy("mLock")
    public final void connect() {
        this.zzgb = 2;
        this.zzfz = false;
        this.zzfy = null;
        this.zzfx = null;
        this.zzfr.connect();
        this.zzfs.connect();
    }

    @GuardedBy("mLock")
    public final void disconnect() {
        this.zzfy = null;
        this.zzfx = null;
        this.zzgb = 0;
        this.zzfr.disconnect();
        this.zzfs.disconnect();
        zzab();
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append(str).append("authClient").println(":");
        this.zzfs.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
        printWriter.append(str).append("anonClient").println(":");
        this.zzfr.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
    }

    @GuardedBy("mLock")
    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(T t) {
        if (!zza((ApiMethodImpl) t)) {
            return this.zzfr.enqueue(t);
        }
        if (!zzac()) {
            return this.zzfs.enqueue(t);
        }
        t.setFailedResult(new Status(4, null, zzad()));
        return t;
    }

    @GuardedBy("mLock")
    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(T t) {
        if (!zza((ApiMethodImpl) t)) {
            return this.zzfr.execute(t);
        }
        if (!zzac()) {
            return this.zzfs.execute(t);
        }
        t.setFailedResult(new Status(4, null, zzad()));
        return t;
    }

    public final boolean isConnected() {
        boolean z = true;
        this.zzga.lock();
        try {
            if (!(this.zzfr.isConnected() && (this.zzfs.isConnected() || zzac() || this.zzgb == 1))) {
                z = false;
            }
            this.zzga.unlock();
            return z;
        } catch (Throwable th) {
            this.zzga.unlock();
        }
    }

    @GuardedBy("mLock")
    public final void zzz() {
        this.zzfr.zzz();
        this.zzfs.zzz();
    }
}
