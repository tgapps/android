package com.google.android.gms.internal.wallet;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.Wallet.zzb;

final class zzab extends zzb {
    private final /* synthetic */ int val$requestCode;
    private final /* synthetic */ FullWalletRequest zzgg;

    zzab(zzy com_google_android_gms_internal_wallet_zzy, GoogleApiClient googleApiClient, FullWalletRequest fullWalletRequest, int i) {
        this.zzgg = fullWalletRequest;
        this.val$requestCode = i;
        super(googleApiClient);
    }

    protected final void zza(zzaf com_google_android_gms_internal_wallet_zzaf) {
        com_google_android_gms_internal_wallet_zzaf.zza(this.zzgg, this.val$requestCode);
        setResult(Status.RESULT_SUCCESS);
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zza((zzaf) anyClient);
    }
}
