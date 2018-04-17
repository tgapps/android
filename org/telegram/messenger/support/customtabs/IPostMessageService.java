package org.telegram.messenger.support.customtabs;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPostMessageService extends IInterface {

    public static abstract class Stub extends Binder implements IPostMessageService {
        private static final String DESCRIPTOR = "android.support.customtabs.IPostMessageService";
        static final int TRANSACTION_onMessageChannelReady = 2;
        static final int TRANSACTION_onPostMessage = 3;

        private static class Proxy implements IPostMessageService {
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

            public void onMessageChannelReady(ICustomTabsCallback callback, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
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

            public void onPostMessage(ICustomTabsCallback callback, String message, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(message);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
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
        }

        public boolean onTransact(int r1, android.os.Parcel r2, android.os.Parcel r3, int r4) throws android.os.RemoteException {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.customtabs.IPostMessageService.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = 1598968902; // 0x5f4e5446 float:1.4867585E19 double:7.89995603E-315;
            r1 = 1;
            if (r6 == r0) goto L_0x005b;
        L_0x0006:
            r0 = 0;
            switch(r6) {
                case 2: goto L_0x0037;
                case 3: goto L_0x000f;
                default: goto L_0x000a;
            };
        L_0x000a:
            r0 = super.onTransact(r6, r7, r8, r9);
            return r0;
        L_0x000f:
            r2 = "android.support.customtabs.IPostMessageService";
            r7.enforceInterface(r2);
            r2 = r7.readStrongBinder();
            r2 = org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub.asInterface(r2);
            r3 = r7.readString();
            r4 = r7.readInt();
            if (r4 == 0) goto L_0x002f;
        L_0x0026:
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r7);
            r0 = (android.os.Bundle) r0;
            goto L_0x0030;
            r5.onPostMessage(r2, r3, r0);
            r8.writeNoException();
            return r1;
        L_0x0037:
            r2 = "android.support.customtabs.IPostMessageService";
            r7.enforceInterface(r2);
            r2 = r7.readStrongBinder();
            r2 = org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub.asInterface(r2);
            r3 = r7.readInt();
            if (r3 == 0) goto L_0x0053;
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r7);
            r0 = (android.os.Bundle) r0;
            goto L_0x0054;
            r5.onMessageChannelReady(r2, r0);
            r8.writeNoException();
            return r1;
        L_0x005b:
            r0 = "android.support.customtabs.IPostMessageService";
            r8.writeString(r0);
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.customtabs.IPostMessageService.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPostMessageService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            IPostMessageService proxy = (iin == null || !(iin instanceof IPostMessageService)) ? new Proxy(obj) : (IPostMessageService) iin;
            return proxy;
        }

        public IBinder asBinder() {
            return this;
        }
    }

    void onMessageChannelReady(ICustomTabsCallback iCustomTabsCallback, Bundle bundle) throws RemoteException;

    void onPostMessage(ICustomTabsCallback iCustomTabsCallback, String str, Bundle bundle) throws RemoteException;
}
