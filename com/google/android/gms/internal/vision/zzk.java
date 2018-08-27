package com.google.android.gms.internal.vision;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

public final class zzk extends zza implements zzj {
    zzk(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.barcode.internal.client.INativeBarcodeDetectorCreator");
    }

    public final zzh zza(IObjectWrapper iObjectWrapper, zze com_google_android_gms_internal_vision_zze) throws RemoteException {
        zzh com_google_android_gms_internal_vision_zzh;
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_vision_zze);
        Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
        IBinder readStrongBinder = transactAndReadException.readStrongBinder();
        if (readStrongBinder == null) {
            com_google_android_gms_internal_vision_zzh = null;
        } else {
            IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.vision.barcode.internal.client.INativeBarcodeDetector");
            com_google_android_gms_internal_vision_zzh = queryLocalInterface instanceof zzh ? (zzh) queryLocalInterface : new zzi(readStrongBinder);
        }
        transactAndReadException.recycle();
        return com_google_android_gms_internal_vision_zzh;
    }
}
