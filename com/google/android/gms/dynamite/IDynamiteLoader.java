package com.google.android.gms.dynamite;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.stable.zza;
import com.google.android.gms.internal.stable.zzb;
import com.google.android.gms.internal.stable.zzc;

public interface IDynamiteLoader extends IInterface {

    public static abstract class Stub extends zzb implements IDynamiteLoader {

        public static class Proxy extends zza implements IDynamiteLoader {
            Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.dynamite.IDynamiteLoader");
            }

            public IObjectWrapper createModuleContext(IObjectWrapper iObjectWrapper, String str, int i) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
                obtainAndWriteInterfaceToken.writeString(str);
                obtainAndWriteInterfaceToken.writeInt(i);
                Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken);
                IObjectWrapper asInterface = com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            public int getModuleVersion(IObjectWrapper iObjectWrapper, String str) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
                obtainAndWriteInterfaceToken.writeString(str);
                Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken);
                int readInt = transactAndReadException.readInt();
                transactAndReadException.recycle();
                return readInt;
            }

            public int getModuleVersion2(IObjectWrapper iObjectWrapper, String str, boolean z) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
                obtainAndWriteInterfaceToken.writeString(str);
                zzc.zza(obtainAndWriteInterfaceToken, z);
                Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken);
                int readInt = transactAndReadException.readInt();
                transactAndReadException.recycle();
                return readInt;
            }
        }

        public static IDynamiteLoader asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
            return queryLocalInterface instanceof IDynamiteLoader ? (IDynamiteLoader) queryLocalInterface : new Proxy(iBinder);
        }

        protected boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    i = getModuleVersion(com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readString());
                    break;
                case 2:
                    IInterface createModuleContext = createModuleContext(com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    zzc.zza(parcel2, createModuleContext);
                    break;
                case 3:
                    i = getModuleVersion2(com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()), parcel.readString(), zzc.zza(parcel));
                    break;
                default:
                    return false;
            }
            parcel2.writeNoException();
            parcel2.writeInt(i);
            return true;
        }
    }

    IObjectWrapper createModuleContext(IObjectWrapper iObjectWrapper, String str, int i) throws RemoteException;

    int getModuleVersion(IObjectWrapper iObjectWrapper, String str) throws RemoteException;

    int getModuleVersion2(IObjectWrapper iObjectWrapper, String str, boolean z) throws RemoteException;
}
