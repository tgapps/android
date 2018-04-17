package com.google.android.gms.internal.wallet;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.Wallet.zzb;

final class zzz extends zzb {
    private final /* synthetic */ int val$requestCode;
    private final /* synthetic */ FullWalletRequest zzge;

    zzz(zzw com_google_android_gms_internal_wallet_zzw, GoogleApiClient googleApiClient, FullWalletRequest fullWalletRequest, int i) {
        this.zzge = fullWalletRequest;
        this.val$requestCode = i;
        super(googleApiClient);
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzad) anyClient);
    }

    protected final void zza(zzad com_google_android_gms_internal_wallet_zzad) {
        com_google_android_gms_internal_wallet_zzad.zza(this.zzge, this.val$requestCode);
        setResult(Status.RESULT_SUCCESS);
    }
}
