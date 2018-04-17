package com.google.android.gms.wearable.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.internal.wearable.zza;
import com.google.android.gms.internal.wearable.zzc;

public final class zzeq extends zza implements zzep {
    zzeq(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.wearable.internal.IWearableService");
    }

    public final void zza(zzek com_google_android_gms_wearable_internal_zzek, zzd com_google_android_gms_wearable_internal_zzd) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_wearable_internal_zzek);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_wearable_internal_zzd);
        transactAndReadExceptionReturnVoid(16, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzek com_google_android_gms_wearable_internal_zzek, zzei com_google_android_gms_wearable_internal_zzei, String str) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_wearable_internal_zzek);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_wearable_internal_zzei);
        obtainAndWriteInterfaceToken.writeString(str);
        transactAndReadExceptionReturnVoid(34, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzek com_google_android_gms_wearable_internal_zzek, String str, int i) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_wearable_internal_zzek);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeInt(i);
        transactAndReadExceptionReturnVoid(42, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzek com_google_android_gms_wearable_internal_zzek, String str, String str2, byte[] bArr) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_wearable_internal_zzek);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeByteArray(bArr);
        transactAndReadExceptionReturnVoid(12, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzek com_google_android_gms_wearable_internal_zzek, zzei com_google_android_gms_wearable_internal_zzei, String str) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_wearable_internal_zzek);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_wearable_internal_zzei);
        obtainAndWriteInterfaceToken.writeString(str);
        transactAndReadExceptionReturnVoid(35, obtainAndWriteInterfaceToken);
    }

    public final void zzc(zzek com_google_android_gms_wearable_internal_zzek, String str) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) com_google_android_gms_wearable_internal_zzek);
        obtainAndWriteInterfaceToken.writeString(str);
        transactAndReadExceptionReturnVoid(32, obtainAndWriteInterfaceToken);
    }
}
