package com.google.android.gms.wallet;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.Client;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.internal.wallet.zzaf;
import com.google.android.gms.wallet.Wallet.WalletOptions;

final class zzaq extends AbstractClientBuilder<zzaf, WalletOptions> {
    zzaq() {
    }

    public final /* synthetic */ Client buildClient(Context context, Looper looper, ClientSettings clientSettings, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        obj = (WalletOptions) obj;
        if (obj == null) {
            obj = new WalletOptions();
        }
        return new zzaf(context, looper, clientSettings, connectionCallbacks, onConnectionFailedListener, obj.environment, obj.theme, obj.zzet);
    }
}
