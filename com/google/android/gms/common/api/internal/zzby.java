package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.internal.BaseSignInCallbacks;
import com.google.android.gms.signin.internal.SignInResponse;
import java.util.Set;

public final class zzby extends BaseSignInCallbacks implements ConnectionCallbacks, OnConnectionFailedListener {
    private static AbstractClientBuilder<? extends SignInClient, SignInOptions> zzlv = SignIn.CLIENT_BUILDER;
    private final Context mContext;
    private final Handler mHandler;
    private Set<Scope> mScopes;
    private final AbstractClientBuilder<? extends SignInClient, SignInOptions> zzby;
    private ClientSettings zzgf;
    private SignInClient zzhn;
    private zzcb zzlw;

    public zzby(Context context, Handler handler, ClientSettings clientSettings) {
        this(context, handler, clientSettings, zzlv);
    }

    public zzby(Context context, Handler handler, ClientSettings clientSettings, AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder) {
        this.mContext = context;
        this.mHandler = handler;
        this.zzgf = (ClientSettings) Preconditions.checkNotNull(clientSettings, "ClientSettings must not be null");
        this.mScopes = clientSettings.getRequiredScopes();
        this.zzby = abstractClientBuilder;
    }

    private final void zzb(SignInResponse signInResponse) {
        ConnectionResult connectionResult = signInResponse.getConnectionResult();
        if (connectionResult.isSuccess()) {
            ResolveAccountResponse resolveAccountResponse = signInResponse.getResolveAccountResponse();
            connectionResult = resolveAccountResponse.getConnectionResult();
            if (connectionResult.isSuccess()) {
                this.zzlw.zza(resolveAccountResponse.getAccountAccessor(), this.mScopes);
                this.zzhn.disconnect();
            }
            String valueOf = String.valueOf(connectionResult);
            StringBuilder stringBuilder = new StringBuilder(48 + String.valueOf(valueOf).length());
            stringBuilder.append("Sign-in succeeded with resolve account failure: ");
            stringBuilder.append(valueOf);
            Log.wtf("SignInCoordinator", stringBuilder.toString(), new Exception());
        }
        this.zzlw.zzg(connectionResult);
        this.zzhn.disconnect();
    }

    public final void onConnected(Bundle bundle) {
        this.zzhn.signIn(this);
    }

    public final void onConnectionFailed(ConnectionResult connectionResult) {
        this.zzlw.zzg(connectionResult);
    }

    public final void onConnectionSuspended(int i) {
        this.zzhn.disconnect();
    }

    public final void onSignInComplete(SignInResponse signInResponse) {
        this.mHandler.post(new zzca(this, signInResponse));
    }

    public final void zza(zzcb com_google_android_gms_common_api_internal_zzcb) {
        if (this.zzhn != null) {
            this.zzhn.disconnect();
        }
        this.zzgf.setClientSessionId(Integer.valueOf(System.identityHashCode(this)));
        this.zzhn = (SignInClient) this.zzby.buildClient(this.mContext, this.mHandler.getLooper(), this.zzgf, this.zzgf.getSignInOptions(), this, this);
        this.zzlw = com_google_android_gms_common_api_internal_zzcb;
        if (this.mScopes != null) {
            if (!this.mScopes.isEmpty()) {
                this.zzhn.connect();
                return;
            }
        }
        this.mHandler.post(new zzbz(this));
    }

    public final SignInClient zzbt() {
        return this.zzhn;
    }

    public final void zzbz() {
        if (this.zzhn != null) {
            this.zzhn.disconnect();
        }
    }
}
