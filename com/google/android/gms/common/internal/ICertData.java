package com.google.android.gms.common.internal;

import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.stable.zzb;
import com.google.android.gms.internal.stable.zzc;

public interface ICertData extends IInterface {

    public static abstract class Stub extends zzb implements ICertData {
        public Stub() {
            super("com.google.android.gms.common.internal.ICertData");
        }

        protected boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    IInterface bytesWrapped = getBytesWrapped();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, bytesWrapped);
                    break;
                case 2:
                    i = getHashCode();
                    parcel2.writeNoException();
                    parcel2.writeInt(i);
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    IObjectWrapper getBytesWrapped() throws RemoteException;

    int getHashCode() throws RemoteException;
}
