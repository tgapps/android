package com.google.android.gms.internal.config;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Status;

public abstract class zzag extends zzb implements zzaf {
    public zzag() {
        super("com.google.android.gms.config.internal.IConfigCallbacks");
    }

    protected final boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                zza((Status) zzc.zza(parcel, Status.CREATOR), parcel.createByteArray());
                break;
            case 2:
                zza((Status) zzc.zza(parcel, Status.CREATOR), zzc.zza(parcel));
                break;
            case 3:
                zza((Status) zzc.zza(parcel, Status.CREATOR));
                break;
            case 4:
                zza((Status) zzc.zza(parcel, Status.CREATOR), (zzad) zzc.zza(parcel, zzad.CREATOR));
                break;
            default:
                return false;
        }
        return true;
    }
}
