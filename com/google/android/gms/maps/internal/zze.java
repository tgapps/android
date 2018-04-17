package com.google.android.gms.maps.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.GoogleMapOptions;

public interface zze extends IInterface {
    IMapViewDelegate zza(IObjectWrapper iObjectWrapper, GoogleMapOptions googleMapOptions) throws RemoteException;

    void zza(IObjectWrapper iObjectWrapper, int i) throws RemoteException;

    ICameraUpdateFactoryDelegate zzd() throws RemoteException;

    com.google.android.gms.internal.maps.zze zze() throws RemoteException;
}
