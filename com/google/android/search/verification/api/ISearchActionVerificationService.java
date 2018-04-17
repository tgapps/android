package com.google.android.search.verification.api;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISearchActionVerificationService extends IInterface {

    public static abstract class Stub extends Binder implements ISearchActionVerificationService {

        private static class Proxy implements ISearchActionVerificationService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public boolean isSearchAction(Intent intent, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.google.android.search.verification.api.ISearchActionVerificationService");
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
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

            public int getVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.google.android.search.verification.api.ISearchActionVerificationService");
                    this.mRemote.transact(2, _data, _reply, 0);
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
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.google.android.search.verification.api.ISearchActionVerificationService.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean
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
            if (r5 == r0) goto L_0x004f;
        L_0x0006:
            switch(r5) {
                case 1: goto L_0x001e;
                case 2: goto L_0x000e;
                default: goto L_0x0009;
            };
        L_0x0009:
            r0 = super.onTransact(r5, r6, r7, r8);
            return r0;
        L_0x000e:
            r0 = "com.google.android.search.verification.api.ISearchActionVerificationService";
            r6.enforceInterface(r0);
            r0 = r4.getVersion();
            r7.writeNoException();
            r7.writeInt(r0);
            return r1;
        L_0x001e:
            r0 = "com.google.android.search.verification.api.ISearchActionVerificationService";
            r6.enforceInterface(r0);
            r0 = r6.readInt();
            r2 = 0;
            if (r0 == 0) goto L_0x0033;
        L_0x002a:
            r0 = android.content.Intent.CREATOR;
            r0 = r0.createFromParcel(r6);
            r0 = (android.content.Intent) r0;
            goto L_0x0034;
        L_0x0033:
            r0 = r2;
        L_0x0034:
            r3 = r6.readInt();
            if (r3 == 0) goto L_0x0043;
        L_0x003a:
            r2 = android.os.Bundle.CREATOR;
            r2 = r2.createFromParcel(r6);
            r2 = (android.os.Bundle) r2;
            goto L_0x0044;
            r3 = r4.isSearchAction(r0, r2);
            r7.writeNoException();
            r7.writeInt(r3);
            return r1;
        L_0x004f:
            r0 = "com.google.android.search.verification.api.ISearchActionVerificationService";
            r7.writeString(r0);
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.search.verification.api.ISearchActionVerificationService.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        public static ISearchActionVerificationService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("com.google.android.search.verification.api.ISearchActionVerificationService");
            if (iin == null || !(iin instanceof ISearchActionVerificationService)) {
                return new Proxy(obj);
            }
            return (ISearchActionVerificationService) iin;
        }
    }

    int getVersion() throws RemoteException;

    boolean isSearchAction(Intent intent, Bundle bundle) throws RemoteException;
}
