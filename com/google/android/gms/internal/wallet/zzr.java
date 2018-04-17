package com.google.android.gms.internal.wallet;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.IsReadyToPayRequest;

public final class zzr extends zza implements zzq {
    zzr(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.wallet.internal.IOwService");
    }

    public final void zza(FullWalletRequest fullWalletRequest, Bundle bundle, zzu com_google_android_gms_internal_wallet_zzu) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) fullWalletRequest);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_internal_wallet_zzu);
        transactOneway(2, obtainAndWriteInterfaceToken);
    }

    public final void zza(IsReadyToPayRequest isReadyToPayRequest, Bundle bundle, zzu com_google_android_gms_internal_wallet_zzu) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) isReadyToPayRequest);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_internal_wallet_zzu);
        transactOneway(14, obtainAndWriteInterfaceToken);
    }
}
