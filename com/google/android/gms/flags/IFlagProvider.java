package com.google.android.gms.flags;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.stable.zza;
import com.google.android.gms.internal.stable.zzb;
import com.google.android.gms.internal.stable.zzc;

public interface IFlagProvider extends IInterface {

    public static abstract class Stub extends zzb implements IFlagProvider {

        public static class Proxy extends zza implements IFlagProvider {
            Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.flags.IFlagProvider");
            }

            public boolean getBooleanFlagValue(String str, boolean z, int i) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                zzc.zza(obtainAndWriteInterfaceToken, z);
                obtainAndWriteInterfaceToken.writeInt(i);
                Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken);
                z = zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return z;
            }

            public int getIntFlagValue(String str, int i, int i2) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                obtainAndWriteInterfaceToken.writeInt(i);
                obtainAndWriteInterfaceToken.writeInt(i2);
                Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken);
                i = transactAndReadException.readInt();
                transactAndReadException.recycle();
                return i;
            }

            public long getLongFlagValue(String str, long j, int i) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                obtainAndWriteInterfaceToken.writeLong(j);
                obtainAndWriteInterfaceToken.writeInt(i);
                Parcel transactAndReadException = transactAndReadException(4, obtainAndWriteInterfaceToken);
                j = transactAndReadException.readLong();
                transactAndReadException.recycle();
                return j;
            }

            public String getStringFlagValue(String str, String str2, int i) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                obtainAndWriteInterfaceToken.writeString(str2);
                obtainAndWriteInterfaceToken.writeInt(i);
                Parcel transactAndReadException = transactAndReadException(5, obtainAndWriteInterfaceToken);
                str2 = transactAndReadException.readString();
                transactAndReadException.recycle();
                return str2;
            }

            public void init(IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
                transactAndReadExceptionReturnVoid(1, obtainAndWriteInterfaceToken);
            }
        }

        public Stub() {
            super("com.google.android.gms.flags.IFlagProvider");
        }

        public static IFlagProvider asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.flags.IFlagProvider");
            return queryLocalInterface instanceof IFlagProvider ? (IFlagProvider) queryLocalInterface : new Proxy(iBinder);
        }

        protected boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    init(com.google.android.gms.dynamic.IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    break;
                case 2:
                    boolean booleanFlagValue = getBooleanFlagValue(parcel.readString(), zzc.zza(parcel), parcel.readInt());
                    parcel2.writeNoException();
                    zzc.zza(parcel2, booleanFlagValue);
                    break;
                case 3:
                    i = getIntFlagValue(parcel.readString(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(i);
                    break;
                case 4:
                    long longFlagValue = getLongFlagValue(parcel.readString(), parcel.readLong(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeLong(longFlagValue);
                    break;
                case 5:
                    String stringFlagValue = getStringFlagValue(parcel.readString(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(stringFlagValue);
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    boolean getBooleanFlagValue(String str, boolean z, int i) throws RemoteException;

    int getIntFlagValue(String str, int i, int i2) throws RemoteException;

    long getLongFlagValue(String str, long j, int i) throws RemoteException;

    String getStringFlagValue(String str, String str2, int i) throws RemoteException;

    void init(IObjectWrapper iObjectWrapper) throws RemoteException;
}
