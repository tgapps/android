package com.google.android.gms.wearable.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.wearable.zza;
import com.google.android.gms.internal.wearable.zzc;
import java.util.List;

public final class zzeo extends zza implements zzem {
    zzeo(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.wearable.internal.IWearableListener");
    }

    public final void onConnectedNodes(List<zzfo> list) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeTypedList(list);
        transactOneway(5, obtainAndWriteInterfaceToken);
    }

    public final void zza(DataHolder dataHolder) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) dataHolder);
        transactOneway(1, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzah com_google_android_gms_wearable_internal_zzah) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_wearable_internal_zzah);
        transactOneway(8, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzaw com_google_android_gms_wearable_internal_zzaw) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_wearable_internal_zzaw);
        transactOneway(7, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzfe com_google_android_gms_wearable_internal_zzfe) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_wearable_internal_zzfe);
        transactOneway(2, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzfo com_google_android_gms_wearable_internal_zzfo) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_wearable_internal_zzfo);
        transactOneway(3, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzi com_google_android_gms_wearable_internal_zzi) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_wearable_internal_zzi);
        transactOneway(9, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzl com_google_android_gms_wearable_internal_zzl) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_wearable_internal_zzl);
        transactOneway(6, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzfo com_google_android_gms_wearable_internal_zzfo) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_wearable_internal_zzfo);
        transactOneway(4, obtainAndWriteInterfaceToken);
    }
}
