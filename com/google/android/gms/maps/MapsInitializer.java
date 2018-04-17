package com.google.android.gms.maps;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.maps.internal.zzbz;
import com.google.android.gms.maps.internal.zze;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import javax.annotation.concurrent.GuardedBy;

public final class MapsInitializer {
    @GuardedBy("MapsInitializer.class")
    private static boolean zzbl = false;

    public static synchronized int initialize(Context context) {
        synchronized (MapsInitializer.class) {
            Preconditions.checkNotNull(context, "Context is null");
            if (zzbl) {
                return 0;
            }
            try {
                zze zza = zzbz.zza(context);
                try {
                    CameraUpdateFactory.zza(zza.zzd());
                    BitmapDescriptorFactory.zza(zza.zze());
                    zzbl = true;
                    return 0;
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            } catch (GooglePlayServicesNotAvailableException e2) {
                return e2.errorCode;
            }
        }
    }
}
