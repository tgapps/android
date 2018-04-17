package com.google.android.gms.internal.maps;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.maps.model.LatLng;

public interface zzt extends IInterface {
    LatLng getPosition() throws RemoteException;

    void setPosition(LatLng latLng) throws RemoteException;

    int zzi() throws RemoteException;

    boolean zzj(zzt com_google_android_gms_internal_maps_zzt) throws RemoteException;
}
