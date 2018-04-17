package android.support.v4.app;

import android.app.Notification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INotificationSideChannel extends IInterface {

    public static abstract class Stub extends Binder implements INotificationSideChannel {

        private static class Proxy implements INotificationSideChannel {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void notify(String packageName, int id, String tag, Notification notification) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                    _data.writeString(packageName);
                    _data.writeInt(id);
                    _data.writeString(tag);
                    if (notification != null) {
                        _data.writeInt(1);
                        notification.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void cancel(String packageName, int id, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                    _data.writeString(packageName);
                    _data.writeInt(id);
                    _data.writeString(tag);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void cancelAll(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.support.v4.app.INotificationSideChannel");
                    _data.writeString(packageName);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public static INotificationSideChannel asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("android.support.v4.app.INotificationSideChannel");
            if (iin == null || !(iin instanceof INotificationSideChannel)) {
                return new Proxy(obj);
            }
            return (INotificationSideChannel) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        Notification _arg3;
                        data.enforceInterface("android.support.v4.app.INotificationSideChannel");
                        String _arg0 = data.readString();
                        int _arg1 = data.readInt();
                        String _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Notification) Notification.CREATOR.createFromParcel(data);
                        } else {
                            _arg3 = null;
                        }
                        notify(_arg0, _arg1, _arg2, _arg3);
                        return true;
                    case 2:
                        data.enforceInterface("android.support.v4.app.INotificationSideChannel");
                        cancel(data.readString(), data.readInt(), data.readString());
                        return true;
                    case 3:
                        data.enforceInterface("android.support.v4.app.INotificationSideChannel");
                        cancelAll(data.readString());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString("android.support.v4.app.INotificationSideChannel");
            return true;
        }
    }

    void cancel(String str, int i, String str2) throws RemoteException;

    void cancelAll(String str) throws RemoteException;

    void notify(String str, int i, String str2, Notification notification) throws RemoteException;
}
