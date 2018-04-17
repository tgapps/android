package org.telegram.messenger.support.customtabs;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ICustomTabsService extends IInterface {

    public static abstract class Stub extends Binder implements ICustomTabsService {
        private static final String DESCRIPTOR = "android.support.customtabs.ICustomTabsService";
        static final int TRANSACTION_extraCommand = 5;
        static final int TRANSACTION_mayLaunchUrl = 4;
        static final int TRANSACTION_newSession = 3;
        static final int TRANSACTION_postMessage = 8;
        static final int TRANSACTION_requestPostMessageChannel = 7;
        static final int TRANSACTION_updateVisuals = 6;
        static final int TRANSACTION_warmup = 2;

        private static class Proxy implements ICustomTabsService {
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

            public boolean warmup(long flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(flags);
                    boolean z = false;
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _result = z;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean newSession(ICustomTabsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean z = false;
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _result = z;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean mayLaunchUrl(ICustomTabsCallback callback, Uri url, Bundle extras, List<Bundle> otherLikelyBundles) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _result = true;
                    if (url != null) {
                        _data.writeInt(1);
                        url.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(otherLikelyBundles);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle extraCommand(String commandName, Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bundle _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(commandName);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateVisuals(ICustomTabsCallback callback, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _result = true;
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean requestPostMessageChannel(ICustomTabsCallback callback, Uri postMessageOrigin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _result = true;
                    if (postMessageOrigin != null) {
                        _data.writeInt(1);
                        postMessageOrigin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int postMessage(ICustomTabsCallback callback, String message, Bundle extras) throws RemoteException {
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
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public boolean onTransact(int r1, android.os.Parcel r2, android.os.Parcel r3, int r4) throws android.os.RemoteException {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.customtabs.ICustomTabsService.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean
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
            if (r7 == r0) goto L_0x0123;
        L_0x0006:
            r0 = 0;
            switch(r7) {
                case 2: goto L_0x010f;
                case 3: goto L_0x00f7;
                case 4: goto L_0x00b9;
                case 5: goto L_0x008b;
                case 6: goto L_0x0063;
                case 7: goto L_0x003b;
                case 8: goto L_0x000f;
                default: goto L_0x000a;
            };
        L_0x000a:
            r0 = super.onTransact(r7, r8, r9, r10);
            return r0;
        L_0x000f:
            r2 = "android.support.customtabs.ICustomTabsService";
            r8.enforceInterface(r2);
            r2 = r8.readStrongBinder();
            r2 = org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub.asInterface(r2);
            r3 = r8.readString();
            r4 = r8.readInt();
            if (r4 == 0) goto L_0x002f;
        L_0x0026:
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r8);
            r0 = (android.os.Bundle) r0;
            goto L_0x0030;
            r4 = r6.postMessage(r2, r3, r0);
            r9.writeNoException();
            r9.writeInt(r4);
            return r1;
        L_0x003b:
            r2 = "android.support.customtabs.ICustomTabsService";
            r8.enforceInterface(r2);
            r2 = r8.readStrongBinder();
            r2 = org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub.asInterface(r2);
            r3 = r8.readInt();
            if (r3 == 0) goto L_0x0057;
            r0 = android.net.Uri.CREATOR;
            r0 = r0.createFromParcel(r8);
            r0 = (android.net.Uri) r0;
            goto L_0x0058;
            r3 = r6.requestPostMessageChannel(r2, r0);
            r9.writeNoException();
            r9.writeInt(r3);
            return r1;
        L_0x0063:
            r2 = "android.support.customtabs.ICustomTabsService";
            r8.enforceInterface(r2);
            r2 = r8.readStrongBinder();
            r2 = org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub.asInterface(r2);
            r3 = r8.readInt();
            if (r3 == 0) goto L_0x007f;
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r8);
            r0 = (android.os.Bundle) r0;
            goto L_0x0080;
            r3 = r6.updateVisuals(r2, r0);
            r9.writeNoException();
            r9.writeInt(r3);
            return r1;
        L_0x008b:
            r2 = "android.support.customtabs.ICustomTabsService";
            r8.enforceInterface(r2);
            r2 = r8.readString();
            r3 = r8.readInt();
            if (r3 == 0) goto L_0x00a3;
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r8);
            r0 = (android.os.Bundle) r0;
            goto L_0x00a4;
            r3 = r6.extraCommand(r2, r0);
            r9.writeNoException();
            if (r3 == 0) goto L_0x00b4;
            r9.writeInt(r1);
            r3.writeToParcel(r9, r1);
            goto L_0x00b8;
            r4 = 0;
            r9.writeInt(r4);
            return r1;
        L_0x00b9:
            r2 = "android.support.customtabs.ICustomTabsService";
            r8.enforceInterface(r2);
            r2 = r8.readStrongBinder();
            r2 = org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub.asInterface(r2);
            r3 = r8.readInt();
            if (r3 == 0) goto L_0x00d5;
            r3 = android.net.Uri.CREATOR;
            r3 = r3.createFromParcel(r8);
            r3 = (android.net.Uri) r3;
            goto L_0x00d6;
            r3 = r0;
            r4 = r8.readInt();
            if (r4 == 0) goto L_0x00e5;
            r0 = android.os.Bundle.CREATOR;
            r0 = r0.createFromParcel(r8);
            r0 = (android.os.Bundle) r0;
            goto L_0x00e6;
            r4 = android.os.Bundle.CREATOR;
            r4 = r8.createTypedArrayList(r4);
            r5 = r6.mayLaunchUrl(r2, r3, r0, r4);
            r9.writeNoException();
            r9.writeInt(r5);
            return r1;
        L_0x00f7:
            r0 = "android.support.customtabs.ICustomTabsService";
            r8.enforceInterface(r0);
            r0 = r8.readStrongBinder();
            r0 = org.telegram.messenger.support.customtabs.ICustomTabsCallback.Stub.asInterface(r0);
            r2 = r6.newSession(r0);
            r9.writeNoException();
            r9.writeInt(r2);
            return r1;
        L_0x010f:
            r0 = "android.support.customtabs.ICustomTabsService";
            r8.enforceInterface(r0);
            r2 = r8.readLong();
            r0 = r6.warmup(r2);
            r9.writeNoException();
            r9.writeInt(r0);
            return r1;
        L_0x0123:
            r0 = "android.support.customtabs.ICustomTabsService";
            r9.writeString(r0);
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.customtabs.ICustomTabsService.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICustomTabsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            ICustomTabsService proxy = (iin == null || !(iin instanceof ICustomTabsService)) ? new Proxy(obj) : (ICustomTabsService) iin;
            return proxy;
        }

        public IBinder asBinder() {
            return this;
        }
    }

    Bundle extraCommand(String str, Bundle bundle) throws RemoteException;

    boolean mayLaunchUrl(ICustomTabsCallback iCustomTabsCallback, Uri uri, Bundle bundle, List<Bundle> list) throws RemoteException;

    boolean newSession(ICustomTabsCallback iCustomTabsCallback) throws RemoteException;

    int postMessage(ICustomTabsCallback iCustomTabsCallback, String str, Bundle bundle) throws RemoteException;

    boolean requestPostMessageChannel(ICustomTabsCallback iCustomTabsCallback, Uri uri) throws RemoteException;

    boolean updateVisuals(ICustomTabsCallback iCustomTabsCallback, Bundle bundle) throws RemoteException;

    boolean warmup(long j) throws RemoteException;
}
