package com.google.android.gms.internal.measurement;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public final class zzfb extends zzn implements zzez {
    zzfb(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.internal.IMeasurementService");
    }

    public final List<zzjz> zza(zzdz com_google_android_gms_internal_measurement_zzdz, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        zzp.writeBoolean(obtainAndWriteInterfaceToken, z);
        obtainAndWriteInterfaceToken = transactAndReadException(7, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzjz.CREATOR);
        obtainAndWriteInterfaceToken.recycle();
        return createTypedArrayList;
    }

    public final List<zzee> zza(String str, String str2, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        obtainAndWriteInterfaceToken = transactAndReadException(16, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzee.CREATOR);
        obtainAndWriteInterfaceToken.recycle();
        return createTypedArrayList;
    }

    public final List<zzjz> zza(String str, String str2, String str3, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        zzp.writeBoolean(obtainAndWriteInterfaceToken, z);
        obtainAndWriteInterfaceToken = transactAndReadException(15, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzjz.CREATOR);
        obtainAndWriteInterfaceToken.recycle();
        return createTypedArrayList;
    }

    public final List<zzjz> zza(String str, String str2, boolean z, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzp.writeBoolean(obtainAndWriteInterfaceToken, z);
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        obtainAndWriteInterfaceToken = transactAndReadException(14, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzjz.CREATOR);
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
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzee com_google_android_gms_internal_measurement_zzee, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzee);
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(12, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzew com_google_android_gms_internal_measurement_zzew, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzew);
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(1, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzew com_google_android_gms_internal_measurement_zzew, String str, String str2) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzew);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        transactAndReadExceptionReturnVoid(5, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzjz com_google_android_gms_internal_measurement_zzjz, zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzjz);
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(6, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzee com_google_android_gms_internal_measurement_zzee) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzee);
        transactAndReadExceptionReturnVoid(13, obtainAndWriteInterfaceToken);
    }

    public final String zzc(zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        obtainAndWriteInterfaceToken = transactAndReadException(11, obtainAndWriteInterfaceToken);
        String readString = obtainAndWriteInterfaceToken.readString();
        obtainAndWriteInterfaceToken.recycle();
        return readString;
    }

    public final void zzd(zzdz com_google_android_gms_internal_measurement_zzdz) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, com_google_android_gms_internal_measurement_zzdz);
        transactAndReadExceptionReturnVoid(18, obtainAndWriteInterfaceToken);
    }

    public final List<zzee> zze(String str, String str2, String str3) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        obtainAndWriteInterfaceToken = transactAndReadException(17, obtainAndWriteInterfaceToken);
        List createTypedArrayList = obtainAndWriteInterfaceToken.createTypedArrayList(zzee.CREATOR);
        obtainAndWriteInterfaceToken.recycle();
        return createTypedArrayList;
    }
}
