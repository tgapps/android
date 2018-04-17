package com.google.android.gms.internal.wallet;

import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.IsReadyToPayRequest;

public interface zzq extends IInterface {
    void zza(FullWalletRequest fullWalletRequest, Bundle bundle, zzu com_google_android_gms_internal_wallet_zzu) throws RemoteException;

    void zza(IsReadyToPayRequest isReadyToPayRequest, Bundle bundle, zzu com_google_android_gms_internal_wallet_zzu) throws RemoteException;
}
