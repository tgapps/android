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

    public final List<zzjs> zza(zzec com_google_android_gms_internal_measurement_zzec, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        zzp.zza(obtainAndWriteInterfaceToken, z);
        Parcel transactAndReadException = transactAndReadException(7, obtainAndWriteInterfaceToken);
        List createTypedArrayList = transactAndReadException.createTypedArrayList(zzjs.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }

    public final List<zzef> zza(String str, String str2, zzec com_google_android_gms_internal_measurement_zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        Parcel transactAndReadException = transactAndReadException(16, obtainAndWriteInterfaceToken);
        List createTypedArrayList = transactAndReadException.createTypedArrayList(zzef.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }

    public final List<zzjs> zza(String str, String str2, String str3, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        zzp.zza(obtainAndWriteInterfaceToken, z);
        Parcel transactAndReadException = transactAndReadException(15, obtainAndWriteInterfaceToken);
        List createTypedArrayList = transactAndReadException.createTypedArrayList(zzjs.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }

    public final List<zzjs> zza(String str, String str2, boolean z, zzec com_google_android_gms_internal_measurement_zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzp.zza(obtainAndWriteInterfaceToken, z);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        Parcel transactAndReadException = transactAndReadException(14, obtainAndWriteInterfaceToken);
        List createTypedArrayList = transactAndReadException.createTypedArrayList(zzjs.CREATOR);
        transactAndReadException.recycle();
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

    public final void zza(zzec com_google_android_gms_internal_measurement_zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzef com_google_android_gms_internal_measurement_zzef, zzec com_google_android_gms_internal_measurement_zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzef);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        transactAndReadExceptionReturnVoid(12, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzeu com_google_android_gms_internal_measurement_zzeu, zzec com_google_android_gms_internal_measurement_zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzeu);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        transactAndReadExceptionReturnVoid(1, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzeu com_google_android_gms_internal_measurement_zzeu, String str, String str2) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzeu);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        transactAndReadExceptionReturnVoid(5, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzjs com_google_android_gms_internal_measurement_zzjs, zzec com_google_android_gms_internal_measurement_zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzjs);
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzec com_google_android_gms_internal_measurement_zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        transactAndReadExceptionReturnVoid(6, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzef com_google_android_gms_internal_measurement_zzef) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzef);
        transactAndReadExceptionReturnVoid(13, obtainAndWriteInterfaceToken);
    }

    public final String zzc(zzec com_google_android_gms_internal_measurement_zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        Parcel transactAndReadException = transactAndReadException(11, obtainAndWriteInterfaceToken);
        String readString = transactAndReadException.readString();
        transactAndReadException.recycle();
        return readString;
    }

    public final void zzd(zzec com_google_android_gms_internal_measurement_zzec) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzp.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_measurement_zzec);
        transactAndReadExceptionReturnVoid(18, obtainAndWriteInterfaceToken);
    }

    public final List<zzef> zze(String str, String str2, String str3) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        Parcel transactAndReadException = transactAndReadException(17, obtainAndWriteInterfaceToken);
        List createTypedArrayList = transactAndReadException.createTypedArrayList(zzef.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }
}
