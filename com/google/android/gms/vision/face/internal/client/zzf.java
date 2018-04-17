package com.google.android.gms.vision.face.internal.client;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.vision.zza;
import com.google.android.gms.internal.vision.zzb;
import com.google.android.gms.internal.vision.zzk;

public final class zzf extends zza implements zze {
    zzf(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.vision.face.internal.client.INativeFaceDetector");
    }

    public final FaceParcel[] zzc(IObjectWrapper iObjectWrapper, zzk com_google_android_gms_internal_vision_zzk) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzb.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzb.zza(obtainAndWriteInterfaceToken, (Parcelable) com_google_android_gms_internal_vision_zzk);
        Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
        FaceParcel[] faceParcelArr = (FaceParcel[]) transactAndReadException.createTypedArray(FaceParcel.CREATOR);
        transactAndReadException.recycle();
        return faceParcelArr;
    }

    public final void zzf() throws RemoteException {
        transactAndReadExceptionReturnVoid(3, obtainAndWriteInterfaceToken());
    }
}
