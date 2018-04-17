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
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ResultHolder;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClient;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.IsReadyToPayRequest;

public final class zzad extends GmsClient<zzq> {
    private final int environment;
    private final int theme;
    private final String zzci;
    private final boolean zzer;
    private final Context zzgh;

    public zzad(Context context, Looper looper, ClientSettings clientSettings, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, int i, int i2, boolean z) {
        super(context, looper, 4, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zzgh = context;
        this.environment = i;
        this.zzci = clientSettings.getAccountName();
        this.theme = i2;
        this.zzer = z;
    }

    private final Bundle zzd() {
        int i = this.environment;
        String packageName = this.zzgh.getPackageName();
        Object obj = this.zzci;
        int i2 = this.theme;
        boolean z = this.zzer;
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

    protected final /* synthetic */ IInterface createServiceInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wallet.internal.IOwService");
        return queryLocalInterface instanceof zzq ? (zzq) queryLocalInterface : new zzr(iBinder);
    }

    public final int getMinApkVersion() {
        return 12451000;
    }

    protected final String getServiceDescriptor() {
        return "com.google.android.gms.wallet.internal.IOwService";
    }

    protected final String getStartServiceAction() {
        return "com.google.android.gms.wallet.service.BIND";
    }

    public final boolean requiresAccount() {
        return true;
    }

    public final void zza(FullWalletRequest fullWalletRequest, int i) {
        zzu com_google_android_gms_internal_wallet_zzae = new zzae((Activity) this.zzgh, i);
        try {
            ((zzq) getService()).zza(fullWalletRequest, zzd(), com_google_android_gms_internal_wallet_zzae);
        } catch (Throwable e) {
            Log.e("WalletClientImpl", "RemoteException getting full wallet", e);
            com_google_android_gms_internal_wallet_zzae.zza(8, null, Bundle.EMPTY);
        }
    }

    public final void zza(IsReadyToPayRequest isReadyToPayRequest, ResultHolder<BooleanResult> resultHolder) {
        zzu com_google_android_gms_internal_wallet_zzai = new zzai(resultHolder);
        try {
            ((zzq) getService()).zza(isReadyToPayRequest, zzd(), com_google_android_gms_internal_wallet_zzai);
        } catch (Throwable e) {
            Log.e("WalletClientImpl", "RemoteException during isReadyToPay", e);
            com_google_android_gms_internal_wallet_zzai.zza(Status.RESULT_INTERNAL_ERROR, false, Bundle.EMPTY);
        }
    }
}
