package com.google.android.gms.internal.config;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public final class zzai extends zza implements zzah {
    zzai(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.config.internal.IConfigService");
    }

    public final void zza(zzaf com_google_android_gms_internal_config_zzaf, zzab com_google_android_gms_internal_config_zzab) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_internal_config_zzaf);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_config_zzab);
        transactAndReadExceptionReturnVoid(8, obtainAndWriteInterfaceToken);
    }
}
