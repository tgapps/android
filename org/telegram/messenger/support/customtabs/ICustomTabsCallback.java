package org.telegram.messenger.support.customtabs;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICustomTabsCallback extends IInterface {

    public static abstract class Stub extends Binder implements ICustomTabsCallback {
        private static final String DESCRIPTOR = "android.support.customtabs.ICustomTabsCallback";
        static final int TRANSACTION_extraCallback = 3;
        static final int TRANSACTION_onMessageChannelReady = 4;
        static final int TRANSACTION_onNavigationEvent = 2;
        static final int TRANSACTION_onPostMessage = 5;

        private static class Proxy implements ICustomTabsCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void onNavigationEvent(int navigationEvent, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(navigationEvent);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void extraCallback(String callbackName, Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callbackName);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onMessageChannelReady(Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onPostMessage(String message, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(message);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public boolean onTransact(int r1, android.os.Parcel r2, android.os.Parcel r3, int r4) throws android.os.RemoteException {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = 1598968902; // 0x5f4e5446 float:1.4867585E19 double:7.89995603E-315;
            r1 = 1;
            if (r5 == r0) goto L_0x008b;
        L_0x0006:
            r0 = 0;
            switch(r5) {
                case 2: goto L_0x006b;
                case 3: goto L_0x004b;
                case 4: goto L_0x002f;
                case 5: goto L_0x000f;
                default: goto L_0x000a;
            };
        L_0x000a:
            r0 = super.onTransact(r5, r6, r7, r8);
            return r0;
        L_0x000f:
            r2 = "android.support.customtabs.ICustomTabsCallback";
            r6.enforceInterface(r2);
            r2 = r6.readString();
            r3 = r6.readInt();
            if (r3 == 0) goto L_0x0027;
        L_0x001e:
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r6);
            r0 = (android.os.Bundle) r0;
            goto L_0x0028;
            r4.onPostMessage(r2, r0);
            r7.writeNoException();
            return r1;
        L_0x002f:
            r2 = "android.support.customtabs.ICustomTabsCallback";
            r6.enforceInterface(r2);
            r2 = r6.readInt();
            if (r2 == 0) goto L_0x0043;
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r6);
            r0 = (android.os.Bundle) r0;
            goto L_0x0044;
            r4.onMessageChannelReady(r0);
            r7.writeNoException();
            return r1;
        L_0x004b:
            r2 = "android.support.customtabs.ICustomTabsCallback";
            r6.enforceInterface(r2);
            r2 = r6.readString();
            r3 = r6.readInt();
            if (r3 == 0) goto L_0x0063;
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r6);
            r0 = (android.os.Bundle) r0;
            goto L_0x0064;
            r4.extraCallback(r2, r0);
            r7.writeNoException();
            return r1;
        L_0x006b:
            r2 = "android.support.customtabs.ICustomTabsCallback";
            r6.enforceInterface(r2);
            r2 = r6.readInt();
            r3 = r6.readInt();
            if (r3 == 0) goto L_0x0083;
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r6);
            r0 = (android.os.Bundle) r0;
            goto L_0x0084;
            r4.onNavigationEvent(r2, r0);
            r7.writeNoException();
            return r1;
        L_0x008b:
            r0 = "android.support.customtabs.ICustomTabsCallback";
            r7.writeString(r0);
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICustomTabsCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            ICustomTabsCallback proxy = (iin == null || !(iin instanceof ICustomTabsCallback)) ? new Proxy(obj) : (ICustomTabsCallback) iin;
            return proxy;
        }

        public IBinder asBinder() {
            return this;
        }
    }

    void extraCallback(String str, Bundle bundle) throws RemoteException;

    void onMessageChannelReady(Bundle bundle) throws RemoteException;

    void onNavigationEvent(int i, Bundle bundle) throws RemoteException;

    void onPostMessage(String str, Bundle bundle) throws RemoteException;
}
