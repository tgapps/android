package com.google.android.gms.common.api;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions;
import com.google.android.gms.common.api.Api.ApiOptions.NotRequiredOptions;
import com.google.android.gms.common.api.Api.BaseClientBuilder;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.api.internal.LifecycleActivity;
import com.google.android.gms.common.api.internal.zzav;
import com.google.android.gms.common.api.internal.zzch;
import com.google.android.gms.common.api.internal.zzi;
import com.google.android.gms.common.api.internal.zzp;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.ClientSettings.OptionalApiSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.concurrent.GuardedBy;

public abstract class GoogleApiClient {
    @GuardedBy("sAllClients")
    private static final Set<GoogleApiClient> zzcu = Collections.newSetFromMap(new WeakHashMap());

    public static final class Builder {
        private final Context mContext;
        private Looper zzcn;
        private final Set<Scope> zzcv = new HashSet();
        private final Set<Scope> zzcw = new HashSet();
        private int zzcx;
        private View zzcy;
        private String zzcz;
        private String zzda;
        private final Map<Api<?>, OptionalApiSettings> zzdb = new ArrayMap();
        private final Map<Api<?>, ApiOptions> zzdc = new ArrayMap();
        private LifecycleActivity zzdd;
        private int zzde = -1;
        private OnConnectionFailedListener zzdf;
        private GoogleApiAvailability zzdg = GoogleApiAvailability.getInstance();
        private AbstractClientBuilder<? extends SignInClient, SignInOptions> zzdh = SignIn.CLIENT_BUILDER;
        private final ArrayList<ConnectionCallbacks> zzdi = new ArrayList();
        private final ArrayList<OnConnectionFailedListener> zzdj = new ArrayList();
        private boolean zzdk = false;
        private Account zzs;

        public Builder(Context context) {
            this.mContext = context;
            this.zzcn = context.getMainLooper();
            this.zzcz = context.getPackageName();
            this.zzda = context.getClass().getName();
        }

        public final Builder addApi(Api<? extends NotRequiredOptions> api) {
            Preconditions.checkNotNull(api, "Api must not be null");
            this.zzdc.put(api, null);
            Collection impliedScopes = api.zzj().getImpliedScopes(null);
            this.zzcw.addAll(impliedScopes);
            this.zzcv.addAll(impliedScopes);
            return this;
        }

        public final <O extends HasOptions> Builder addApi(Api<O> api, O o) {
            Preconditions.checkNotNull(api, "Api must not be null");
            Preconditions.checkNotNull(o, "Null options are not permitted for this Api");
            this.zzdc.put(api, o);
            Collection impliedScopes = api.zzj().getImpliedScopes(o);
            this.zzcw.addAll(impliedScopes);
            this.zzcv.addAll(impliedScopes);
            return this;
        }

        public final Builder addConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
            Preconditions.checkNotNull(connectionCallbacks, "Listener must not be null");
            this.zzdi.add(connectionCallbacks);
            return this;
        }

        public final Builder addOnConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
            Preconditions.checkNotNull(onConnectionFailedListener, "Listener must not be null");
            this.zzdj.add(onConnectionFailedListener);
            return this;
        }

        public final GoogleApiClient build() {
            boolean z;
            boolean z2 = true;
            Preconditions.checkArgument(this.zzdc.isEmpty() ^ true, "must call addApi() to add at least one API");
            ClientSettings buildClientSettings = buildClientSettings();
            Api api = null;
            Map optionalApiSettings = buildClientSettings.getOptionalApiSettings();
            Map arrayMap = new ArrayMap();
            Map arrayMap2 = new ArrayMap();
            ArrayList arrayList = new ArrayList();
            Iterator it = this.zzdc.keySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Api api2 = (Api) it.next();
                Object obj = r1.zzdc.get(api2);
                boolean z3 = optionalApiSettings.get(api2) != null ? z2 : false;
                arrayMap.put(api2, Boolean.valueOf(z3));
                zzp com_google_android_gms_common_api_internal_zzp = new zzp(api2, z3);
                arrayList.add(com_google_android_gms_common_api_internal_zzp);
                BaseClientBuilder zzk = api2.zzk();
                BaseClientBuilder baseClientBuilder = zzk;
                zzp com_google_android_gms_common_api_internal_zzp2 = com_google_android_gms_common_api_internal_zzp;
                Map map = optionalApiSettings;
                Api api3 = api2;
                Iterator it2 = it;
                Client buildClient = zzk.buildClient(r1.mContext, r1.zzcn, buildClientSettings, obj, com_google_android_gms_common_api_internal_zzp2, com_google_android_gms_common_api_internal_zzp2);
                arrayMap2.put(api3.getClientKey(), buildClient);
                if (baseClientBuilder.getPriority() == 1) {
                    i = obj != null ? 1 : 0;
                }
                if (buildClient.providesSignIn()) {
                    if (api != null) {
                        String name = api3.getName();
                        String name2 = api.getName();
                        StringBuilder stringBuilder = new StringBuilder((21 + String.valueOf(name).length()) + String.valueOf(name2).length());
                        stringBuilder.append(name);
                        stringBuilder.append(" cannot be used with ");
                        stringBuilder.append(name2);
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                    api = api3;
                }
                optionalApiSettings = map;
                it = it2;
                z2 = true;
            }
            if (api == null) {
                z = true;
            } else if (i != 0) {
                name = api.getName();
                StringBuilder stringBuilder2 = new StringBuilder(82 + String.valueOf(name).length());
                stringBuilder2.append("With using ");
                stringBuilder2.append(name);
                stringBuilder2.append(", GamesOptions can only be specified within GoogleSignInOptions.Builder");
                throw new IllegalStateException(stringBuilder2.toString());
            } else {
                z = true;
                Preconditions.checkState(r1.zzs == null, "Must not set an account in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead", api.getName());
                Preconditions.checkState(r1.zzcv.equals(r1.zzcw), "Must not set scopes in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead.", api.getName());
            }
            ClientSettings clientSettings = buildClientSettings;
            GoogleApiClient com_google_android_gms_common_api_internal_zzav = new zzav(r1.mContext, new ReentrantLock(), r1.zzcn, clientSettings, r1.zzdg, r1.zzdh, arrayMap, r1.zzdi, r1.zzdj, arrayMap2, r1.zzde, zzav.zza(arrayMap2.values(), z), arrayList, false);
            synchronized (GoogleApiClient.zzcu) {
                try {
                    GoogleApiClient.zzcu.add(com_google_android_gms_common_api_internal_zzav);
                } catch (Throwable th) {
                    while (true) {
                        Throwable th2 = th;
                    }
                }
            }
            if (r1.zzde >= 0) {
                zzi.zza(r1.zzdd).zza(r1.zzde, com_google_android_gms_common_api_internal_zzav, r1.zzdf);
            }
            return com_google_android_gms_common_api_internal_zzav;
        }

        public final ClientSettings buildClientSettings() {
            SignInOptions signInOptions = SignInOptions.DEFAULT;
            if (this.zzdc.containsKey(SignIn.API)) {
                signInOptions = (SignInOptions) this.zzdc.get(SignIn.API);
            }
            return new ClientSettings(this.zzs, this.zzcv, this.zzdb, this.zzcx, this.zzcy, this.zzcz, this.zzda, signInOptions);
        }
    }

    public interface ConnectionCallbacks {
        void onConnected(Bundle bundle);

        void onConnectionSuspended(int i);
    }

    public interface OnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    public abstract ConnectionResult blockingConnect();

    public abstract void connect();

    public void connect(int i) {
        throw new UnsupportedOperationException();
    }

    public abstract void disconnect();

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(T t) {
        throw new UnsupportedOperationException();
    }

    public <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(T t) {
        throw new UnsupportedOperationException();
    }

    public Looper getLooper() {
        throw new UnsupportedOperationException();
    }

    public abstract boolean isConnected();

    public abstract void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);

    public abstract void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);

    public void zza(zzch com_google_android_gms_common_api_internal_zzch) {
        throw new UnsupportedOperationException();
    }

    public void zzb(zzch com_google_android_gms_common_api_internal_zzch) {
        throw new UnsupportedOperationException();
    }
}
