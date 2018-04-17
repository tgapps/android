package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.AnyClientKey;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.ClientSettings.OptionalApiSettings;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.internal.SignInResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import javax.annotation.concurrent.GuardedBy;

public final class zzaj implements zzbc {
    private final Context mContext;
    private final AbstractClientBuilder<? extends SignInClient, SignInOptions> zzdh;
    private final Lock zzga;
    private final ClientSettings zzgf;
    private final Map<Api<?>, Boolean> zzgi;
    private final GoogleApiAvailabilityLight zzgk;
    private ConnectionResult zzgt;
    private final zzbd zzhf;
    private int zzhi;
    private int zzhj = 0;
    private int zzhk;
    private final Bundle zzhl = new Bundle();
    private final Set<AnyClientKey> zzhm = new HashSet();
    private SignInClient zzhn;
    private boolean zzho;
    private boolean zzhp;
    private boolean zzhq;
    private IAccountAccessor zzhr;
    private boolean zzhs;
    private boolean zzht;
    private ArrayList<Future<?>> zzhu = new ArrayList();

    public zzaj(zzbd com_google_android_gms_common_api_internal_zzbd, ClientSettings clientSettings, Map<Api<?>, Boolean> map, GoogleApiAvailabilityLight googleApiAvailabilityLight, AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder, Lock lock, Context context) {
        this.zzhf = com_google_android_gms_common_api_internal_zzbd;
        this.zzgf = clientSettings;
        this.zzgi = map;
        this.zzgk = googleApiAvailabilityLight;
        this.zzdh = abstractClientBuilder;
        this.zzga = lock;
        this.mContext = context;
    }

    @GuardedBy("mLock")
    private final void zza(SignInResponse signInResponse) {
        if (zze(0)) {
            ConnectionResult connectionResult = signInResponse.getConnectionResult();
            if (connectionResult.isSuccess()) {
                ResolveAccountResponse resolveAccountResponse = signInResponse.getResolveAccountResponse();
                connectionResult = resolveAccountResponse.getConnectionResult();
                if (connectionResult.isSuccess()) {
                    this.zzhq = true;
                    this.zzhr = resolveAccountResponse.getAccountAccessor();
                    this.zzhs = resolveAccountResponse.getSaveDefaultAccount();
                    this.zzht = resolveAccountResponse.isFromCrossClientAuth();
                    zzas();
                    return;
                }
                String valueOf = String.valueOf(connectionResult);
                StringBuilder stringBuilder = new StringBuilder(48 + String.valueOf(valueOf).length());
                stringBuilder.append("Sign-in succeeded with resolve account failure: ");
                stringBuilder.append(valueOf);
                Log.wtf("GoogleApiClientConnecting", stringBuilder.toString(), new Exception());
                zze(connectionResult);
            } else if (zzd(connectionResult)) {
                zzau();
                zzas();
            } else {
                zze(connectionResult);
            }
        }
    }

    private final void zza(boolean z) {
        if (this.zzhn != null) {
            if (this.zzhn.isConnected() && z) {
                this.zzhn.clearAccountFromSessionStore();
            }
            this.zzhn.disconnect();
            this.zzhr = null;
        }
    }

    @GuardedBy("mLock")
    private final boolean zzar() {
        this.zzhk--;
        if (this.zzhk > 0) {
            return false;
        }
        ConnectionResult connectionResult;
        if (this.zzhk < 0) {
            Log.w("GoogleApiClientConnecting", this.zzhf.zzfq.zzbb());
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", new Exception());
            connectionResult = new ConnectionResult(8, null);
        } else if (this.zzgt == null) {
            return true;
        } else {
            this.zzhf.zzje = this.zzhi;
            connectionResult = this.zzgt;
        }
        zze(connectionResult);
        return false;
    }

    @GuardedBy("mLock")
    private final void zzas() {
        if (this.zzhk == 0) {
            if (!this.zzhp || this.zzhq) {
                ArrayList arrayList = new ArrayList();
                this.zzhj = 1;
                this.zzhk = this.zzhf.zzil.size();
                for (AnyClientKey anyClientKey : this.zzhf.zzil.keySet()) {
                    if (!this.zzhf.zzjb.containsKey(anyClientKey)) {
                        arrayList.add((Client) this.zzhf.zzil.get(anyClientKey));
                    } else if (zzar()) {
                        zzat();
                    }
                }
                if (!arrayList.isEmpty()) {
                    this.zzhu.add(zzbg.zzbe().submit(new zzap(this, arrayList)));
                }
            }
        }
    }

    @GuardedBy("mLock")
    private final void zzat() {
        this.zzhf.zzbd();
        zzbg.zzbe().execute(new zzak(this));
        if (this.zzhn != null) {
            if (this.zzhs) {
                this.zzhn.saveDefaultAccount(this.zzhr, this.zzht);
            }
            zza(false);
        }
        for (AnyClientKey anyClientKey : this.zzhf.zzjb.keySet()) {
            ((Client) this.zzhf.zzil.get(anyClientKey)).disconnect();
        }
        this.zzhf.zzjf.zzb(this.zzhl.isEmpty() ? null : this.zzhl);
    }

    @GuardedBy("mLock")
    private final void zzau() {
        this.zzhp = false;
        this.zzhf.zzfq.zzim = Collections.emptySet();
        for (AnyClientKey anyClientKey : this.zzhm) {
            if (!this.zzhf.zzjb.containsKey(anyClientKey)) {
                this.zzhf.zzjb.put(anyClientKey, new ConnectionResult(17, null));
            }
        }
    }

    private final void zzav() {
        ArrayList arrayList = this.zzhu;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((Future) obj).cancel(true);
        }
        this.zzhu.clear();
    }

    private final Set<Scope> zzaw() {
        if (this.zzgf == null) {
            return Collections.emptySet();
        }
        Set<Scope> hashSet = new HashSet(this.zzgf.getRequiredScopes());
        Map optionalApiSettings = this.zzgf.getOptionalApiSettings();
        for (Api api : optionalApiSettings.keySet()) {
            if (!this.zzhf.zzjb.containsKey(api.getClientKey())) {
                hashSet.addAll(((OptionalApiSettings) optionalApiSettings.get(api)).mScopes);
            }
        }
        return hashSet;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @javax.annotation.concurrent.GuardedBy("mLock")
    private final void zzb(com.google.android.gms.common.ConnectionResult r5, com.google.android.gms.common.api.Api<?> r6, boolean r7) {
        /*
        r4 = this;
        r0 = r6.zzj();
        r0 = r0.getPriority();
        r1 = 0;
        r2 = 1;
        if (r7 == 0) goto L_0x0024;
    L_0x000c:
        r7 = r5.hasResolution();
        if (r7 == 0) goto L_0x0014;
    L_0x0012:
        r7 = r2;
        goto L_0x0022;
    L_0x0014:
        r7 = r4.zzgk;
        r3 = r5.getErrorCode();
        r7 = r7.getErrorResolutionIntent(r3);
        if (r7 == 0) goto L_0x0021;
    L_0x0020:
        goto L_0x0012;
    L_0x0021:
        r7 = r1;
    L_0x0022:
        if (r7 == 0) goto L_0x002d;
    L_0x0024:
        r7 = r4.zzgt;
        if (r7 == 0) goto L_0x002c;
    L_0x0028:
        r7 = r4.zzhi;
        if (r0 >= r7) goto L_0x002d;
    L_0x002c:
        r1 = r2;
    L_0x002d:
        if (r1 == 0) goto L_0x0033;
    L_0x002f:
        r4.zzgt = r5;
        r4.zzhi = r0;
    L_0x0033:
        r7 = r4.zzhf;
        r7 = r7.zzjb;
        r6 = r6.getClientKey();
        r7.put(r6, r5);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzaj.zzb(com.google.android.gms.common.ConnectionResult, com.google.android.gms.common.api.Api, boolean):void");
    }

    @GuardedBy("mLock")
    private final boolean zzd(ConnectionResult connectionResult) {
        return this.zzho && !connectionResult.hasResolution();
    }

    @GuardedBy("mLock")
    private final void zze(ConnectionResult connectionResult) {
        zzav();
        zza(connectionResult.hasResolution() ^ 1);
        this.zzhf.zzf(connectionResult);
        this.zzhf.zzjf.zzc(connectionResult);
    }

    @GuardedBy("mLock")
    private final boolean zze(int i) {
        if (this.zzhj == i) {
            return true;
        }
        Log.w("GoogleApiClientConnecting", this.zzhf.zzfq.zzbb());
        String valueOf = String.valueOf(this);
        StringBuilder stringBuilder = new StringBuilder(23 + String.valueOf(valueOf).length());
        stringBuilder.append("Unexpected callback in ");
        stringBuilder.append(valueOf);
        Log.w("GoogleApiClientConnecting", stringBuilder.toString());
        int i2 = this.zzhk;
        stringBuilder = new StringBuilder(33);
        stringBuilder.append("mRemainingConnections=");
        stringBuilder.append(i2);
        Log.w("GoogleApiClientConnecting", stringBuilder.toString());
        valueOf = zzf(this.zzhj);
        String zzf = zzf(i);
        stringBuilder = new StringBuilder((70 + String.valueOf(valueOf).length()) + String.valueOf(zzf).length());
        stringBuilder.append("GoogleApiClient connecting is in step ");
        stringBuilder.append(valueOf);
        stringBuilder.append(" but received callback for step ");
        stringBuilder.append(zzf);
        Log.wtf("GoogleApiClientConnecting", stringBuilder.toString(), new Exception());
        zze(new ConnectionResult(8, null));
        return false;
    }

    private static String zzf(int i) {
        switch (i) {
            case 0:
                return "STEP_SERVICE_BINDINGS_AND_SIGN_IN";
            case 1:
                return "STEP_GETTING_REMOTE_SERVICE";
            default:
                return "UNKNOWN";
        }
    }

    public final void begin() {
        this.zzhf.zzjb.clear();
        this.zzhp = false;
        this.zzgt = null;
        this.zzhj = 0;
        this.zzho = true;
        this.zzhq = false;
        this.zzhs = false;
        Map hashMap = new HashMap();
        int i = 0;
        for (Api api : this.zzgi.keySet()) {
            Client client = (Client) this.zzhf.zzil.get(api.getClientKey());
            i |= api.zzj().getPriority() == 1 ? 1 : 0;
            boolean booleanValue = ((Boolean) this.zzgi.get(api)).booleanValue();
            if (client.requiresSignIn()) {
                this.zzhp = true;
                if (booleanValue) {
                    this.zzhm.add(api.getClientKey());
                } else {
                    this.zzho = false;
                }
            }
            hashMap.put(client, new zzal(this, api, booleanValue));
        }
        if (i != 0) {
            this.zzhp = false;
        }
        if (this.zzhp) {
            this.zzgf.setClientSessionId(Integer.valueOf(System.identityHashCode(this.zzhf.zzfq)));
            OnConnectionFailedListener com_google_android_gms_common_api_internal_zzas = new zzas();
            this.zzhn = (SignInClient) this.zzdh.buildClient(this.mContext, this.zzhf.zzfq.getLooper(), this.zzgf, this.zzgf.getSignInOptions(), com_google_android_gms_common_api_internal_zzas, com_google_android_gms_common_api_internal_zzas);
        }
        this.zzhk = this.zzhf.zzil.size();
        this.zzhu.add(zzbg.zzbe().submit(new zzam(this, hashMap)));
    }

    public final void connect() {
    }

    public final boolean disconnect() {
        zzav();
        zza(true);
        this.zzhf.zzf(null);
        return true;
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(T t) {
        this.zzhf.zzfq.zzgo.add(t);
        return t;
    }

    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }

    @GuardedBy("mLock")
    public final void onConnected(Bundle bundle) {
        if (zze(1)) {
            if (bundle != null) {
                this.zzhl.putAll(bundle);
            }
            if (zzar()) {
                zzat();
            }
        }
    }

    @GuardedBy("mLock")
    public final void onConnectionSuspended(int i) {
        zze(new ConnectionResult(8, null));
    }

    @GuardedBy("mLock")
    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
        if (zze(1)) {
            zzb(connectionResult, api, z);
            if (zzar()) {
                zzat();
            }
        }
    }
}
