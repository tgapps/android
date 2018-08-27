package com.google.android.gms.wallet;

import com.google.android.gms.common.api.GoogleApiClient;

@Deprecated
public interface Payments {
    void loadFullWallet(GoogleApiClient googleApiClient, FullWalletRequest fullWalletRequest, int i);
}
