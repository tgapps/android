package com.google.android.gms.internal.maps;

import android.graphics.Bitmap;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

public interface zze extends IInterface {
    IObjectWrapper zza(int i) throws RemoteException;

    IObjectWrapper zza(Bitmap bitmap) throws RemoteException;
}
