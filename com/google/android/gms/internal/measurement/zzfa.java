package com.google.android.gms.internal.measurement;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import java.util.List;

public final class zzfa extends zzn implements zzey {
    zzfa(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.internal.IMeasurementService");
    }

    public final List<zzjx> zza(zzdz com_google_android_gms_internal_measurement_zzdz, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        zzp.zza(obtainAndWriteInterfaceToken, z);
        obtainAndWriteInterfaceToken = transactAndReadException(7, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzjx.CREATOR);
        obtainAndWriteInterfaceToken.recycle();
        return createTypedArrayList;
    }

    public final List<zzed> zza(String str, String str2, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        obtainAndWriteInterfaceToken = transactAndReadException(16, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzed.CREATOR);
        obtainAndWriteInterfaceToken.recycle();
        return createTypedArrayList;
    }

    public final List<zzjx> zza(String str, String str2, String str3, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        zzp.zza(obtainAndWriteInterfaceToken, z);
        obtainAndWriteInterfaceToken = transactAndReadException(15, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzjx.CREATOR);
        obtainAndWriteInterfaceToken.recycle();
        return createTypedArrayList;
    }

    public final List<zzjx> zza(String str, String str2, boolean z, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzp.zza(obtainAndWriteInterfaceToken, z);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        obtainAndWriteInterfaceToken = transactAndReadException(14, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzjx.CREATOR);
        obtainAndWriteInterfaceToken.recycle();
        return createTypedArrayList;
    }

    public final void zza(long j, String str, String str2, String str3) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeLong(j);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        transactAndReadExceptionReturnVoid(10, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzed com_google_android_gms_internal_measurement_zzed, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzed);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(12, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzeu com_google_android_gms_internal_measurement_zzeu, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzeu);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(1, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzeu com_google_android_gms_internal_measurement_zzeu, String str, String str2) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzeu);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        transactAndReadExceptionReturnVoid(5, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzjx com_google_android_gms_internal_measurement_zzjx, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzjx);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
    }

    public final byte[] zza(zzeu com_google_android_gms_internal_measurement_zzeu, String str) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzeu);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken = transactAndReadException(9, obtainAndWriteInterfaceToken);
        byte[] createByteArray = obtainAndWriteInterfaceToken.createByteArray();
        obtainAndWriteInterfaceToken.recycle();
        return createByteArray;
    }

    public final void zzb(zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(6, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzed com_google_android_gms_internal_measurement_zzed) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzed);
        transactAndReadExceptionReturnVoid(13, obtainAndWriteInterfaceToken);
    }

    public final String zzc(zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        obtainAndWriteInterfaceToken = transactAndReadException(11, obtainAndWriteInterfaceToken);
        String readString = obtainAndWriteInterfaceToken.readString();
        obtainAndWriteInterfaceToken.recycle();
        return readString;
    }

    public final void zzd(zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(18, obtainAndWriteInterfaceToken);
    }

    public final List<zzed> zze(String str, String str2, String str3) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        obtainAndWriteInterfaceToken = transactAndReadException(17, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzed.CREATOR);
        obtainAndWriteInterfaceToken.recycle();
        return createTypedArrayList;
    }
}
