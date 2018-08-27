package com.google.android.gms.internal.wallet;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClient;
import com.google.android.gms.wallet.FullWalletRequest;

public final class zzaf extends GmsClient<zzs> {
    private final int environment;
    private final int theme;
    private final String zzcj;
    private final boolean zzet;
    private final Context zzgj;

    public zzaf(Context context, Looper looper, ClientSettings clientSettings, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, int i, int i2, boolean z) {
        super(context, looper, 4, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zzgj = context;
        this.environment = i;
        this.zzcj = clientSettings.getAccountName();
        this.theme = i2;
        this.zzet = z;
    }

    public final boolean requiresAccount() {
        return true;
    }

    protected final String getStartServiceAction() {
        return "com.google.android.gms.wallet.service.BIND";
    }

    protected final String getServiceDescriptor() {
        return "com.google.android.gms.wallet.internal.IOwService";
    }

    public final void zza(FullWalletRequest fullWalletRequest, int i) {
        Object com_google_android_gms_internal_wallet_zzag = new zzag((Activity) this.zzgj, i);
        try {
            ((zzs) getService()).zza(fullWalletRequest, zzd(), com_google_android_gms_internal_wallet_zzag);
        } catch (Throwable e) {
            Log.e("WalletClientImpl", "RemoteException getting full wallet", e);
            com_google_android_gms_internal_wallet_zzag.zza(8, null, Bundle.EMPTY);
        }
    }

    private final Bundle zzd() {
        int i = this.environment;
        String packageName = this.zzgj.getPackageName();
        Object obj = this.zzcj;
        int i2 = this.theme;
        boolean z = this.zzet;
        Bundle bundle = new Bundle();
        bundle.putInt("com.google.android.gms.wallet.EXTRA_ENVIRONMENT", i);
        bundle.putBoolean("com.google.android.gms.wallet.EXTRA_USING_ANDROID_PAY_BRAND", z);
        bundle.putString("androidPackageName", packageName);
        if (!TextUtils.isEmpty(obj)) {
            bundle.putParcelable("com.google.android.gms.wallet.EXTRA_BUYER_ACCOUNT", new Account(obj, "com.google"));
        }
        bundle.putInt("com.google.android.gms.wallet.EXTRA_THEME", i2);
        return bundle;
    }

    public final int getMinApkVersion() {
        return 12600000;
    }

    protected final /* synthetic */ IInterface createServiceInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wallet.internal.IOwService");
        if (queryLocalInterface instanceof zzs) {
            return (zzs) queryLocalInterface;
        }
        return new zzt(iBinder);
    }
}
