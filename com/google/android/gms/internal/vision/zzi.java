package com.google.android.gms.internal.vision;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.vision.barcode.Barcode;

public final class zzi extends zza implements zzh {
    zzi(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.barcode.internal.client.INativeBarcodeDetector");
    }

    public final Barcode[] zza(IObjectWrapper iObjectWrapper, zzm com_google_android_gms_internal_vision_zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_vision_zzm);
        Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
        Barcode[] barcodeArr = (Barcode[]) transactAndReadException.createTypedArray(Barcode.CREATOR);
        transactAndReadException.recycle();
        return barcodeArr;
    }

    public final Barcode[] zzb(IObjectWrapper iObjectWrapper, zzm com_google_android_gms_internal_vision_zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_vision_zzm);
        Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken);
        Barcode[] barcodeArr = (Barcode[]) transactAndReadException.createTypedArray(Barcode.CREATOR);
        transactAndReadException.recycle();
        return barcodeArr;
    }

    public final void zzn() throws RemoteException {
        transactAndReadExceptionReturnVoid(3, obtainAndWriteInterfaceToken());
    }
}
