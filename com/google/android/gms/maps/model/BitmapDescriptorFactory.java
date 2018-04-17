package com.google.android.gms.maps.model;

import android.graphics.Bitmap;
import android.os.RemoteException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.maps.zze;

public final class BitmapDescriptorFactory {
    private static zze zzcl;

    public static BitmapDescriptor fromBitmap(Bitmap bitmap) {
        try {
            return new BitmapDescriptor(zzf().zza(bitmap));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static BitmapDescriptor fromResource(int i) {
        try {
            return new BitmapDescriptor(zzf().zza(i));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static void zza(zze com_google_android_gms_internal_maps_zze) {
        if (zzcl == null) {
            zzcl = (zze) Preconditions.checkNotNull(com_google_android_gms_internal_maps_zze);
        }
    }

    private static zze zzf() {
        return (zze) Preconditions.checkNotNull(zzcl, "IBitmapDescriptorFactory is not initialized");
    }
}
