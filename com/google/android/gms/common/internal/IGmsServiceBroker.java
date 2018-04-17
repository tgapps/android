package com.google.android.gms.common.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGmsServiceBroker extends IInterface {

    public static abstract class Stub extends Binder implements IGmsServiceBroker {

        private static class zza implements IGmsServiceBroker {
            private final IBinder zza;

            zza(IBinder iBinder) {
                this.zza = iBinder;
            }

            public final IBinder asBinder() {
                return this.zza;
            }

            public final void getService(IGmsCallbacks iGmsCallbacks, GetServiceRequest getServiceRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
                    obtain.writeStrongBinder(iGmsCallbacks != null ? iGmsCallbacks.asBinder() : null);
                    if (getServiceRequest != null) {
                        obtain.writeInt(1);
                        getServiceRequest.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zza.transact(46, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IGmsServiceBroker asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IGmsServiceBroker)) ? new zza(iBinder) : (IGmsServiceBroker) queryLocalInterface;
        }

        protected void getLegacyService(int i, IGmsCallbacks iGmsCallbacks, int i2, String str, String str2, String[] strArr, Bundle bundle, IBinder iBinder, String str3, String str4) throws RemoteException {
            throw new UnsupportedOperationException();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r12, android.os.Parcel r13, android.os.Parcel r14, int r15) throws android.os.RemoteException {
            /*
            r11 = this;
            r0 = 16777215; // 0xffffff float:2.3509886E-38 double:8.2890456E-317;
            if (r12 <= r0) goto L_0x000a;
        L_0x0005:
            r12 = super.onTransact(r12, r13, r14, r15);
            return r12;
        L_0x000a:
            r15 = "com.google.android.gms.common.internal.IGmsServiceBroker";
            r13.enforceInterface(r15);
            r15 = r13.readStrongBinder();
            r2 = com.google.android.gms.common.internal.IGmsCallbacks.Stub.asInterface(r15);
            r15 = 46;
            r0 = 0;
            if (r12 != r15) goto L_0x0030;
        L_0x001c:
            r12 = r13.readInt();
            if (r12 == 0) goto L_0x002b;
        L_0x0022:
            r12 = com.google.android.gms.common.internal.GetServiceRequest.CREATOR;
            r12 = r12.createFromParcel(r13);
            r0 = r12;
            r0 = (com.google.android.gms.common.internal.GetServiceRequest) r0;
        L_0x002b:
            r11.getService(r2, r0);
            goto L_0x0142;
        L_0x0030:
            r15 = 47;
            if (r12 != r15) goto L_0x0048;
        L_0x0034:
            r12 = r13.readInt();
            if (r12 == 0) goto L_0x0043;
        L_0x003a:
            r12 = com.google.android.gms.common.internal.ValidateAccountRequest.CREATOR;
            r12 = r12.createFromParcel(r13);
            r0 = r12;
            r0 = (com.google.android.gms.common.internal.ValidateAccountRequest) r0;
        L_0x0043:
            r11.validateAccount(r2, r0);
            goto L_0x0142;
        L_0x0048:
            r3 = r13.readInt();
            r15 = 4;
            if (r12 == r15) goto L_0x0055;
        L_0x004f:
            r15 = r13.readString();
            r4 = r15;
            goto L_0x0056;
        L_0x0055:
            r4 = r0;
        L_0x0056:
            r15 = 23;
            if (r12 == r15) goto L_0x0124;
        L_0x005a:
            r15 = 25;
            if (r12 == r15) goto L_0x0124;
        L_0x005e:
            r15 = 27;
            if (r12 == r15) goto L_0x0124;
        L_0x0062:
            r15 = 30;
            if (r12 == r15) goto L_0x0103;
        L_0x0066:
            r15 = 34;
            if (r12 == r15) goto L_0x00fc;
        L_0x006a:
            r15 = 41;
            if (r12 == r15) goto L_0x0124;
        L_0x006e:
            r15 = 43;
            if (r12 == r15) goto L_0x0124;
        L_0x0072:
            switch(r12) {
                case 1: goto L_0x00d8;
                case 2: goto L_0x0124;
                default: goto L_0x0075;
            };
        L_0x0075:
            switch(r12) {
                case 5: goto L_0x0124;
                case 6: goto L_0x0124;
                case 7: goto L_0x0124;
                case 8: goto L_0x0124;
                case 9: goto L_0x00aa;
                case 10: goto L_0x009d;
                case 11: goto L_0x0124;
                case 12: goto L_0x0124;
                case 13: goto L_0x0124;
                case 14: goto L_0x0124;
                case 15: goto L_0x0124;
                case 16: goto L_0x0124;
                case 17: goto L_0x0124;
                case 18: goto L_0x0124;
                case 19: goto L_0x007d;
                case 20: goto L_0x0103;
                default: goto L_0x0078;
            };
        L_0x0078:
            switch(r12) {
                case 37: goto L_0x0124;
                case 38: goto L_0x0124;
                default: goto L_0x007b;
            };
        L_0x007b:
            goto L_0x0137;
        L_0x007d:
            r15 = r13.readStrongBinder();
            r1 = r13.readInt();
            if (r1 == 0) goto L_0x0096;
        L_0x0087:
            r1 = android.os.Bundle.CREATOR;
            r13 = r1.createFromParcel(r13);
            r13 = (android.os.Bundle) r13;
            r7 = r13;
            r8 = r15;
            r5 = r0;
            r6 = r5;
            r9 = r6;
            goto L_0x013c;
        L_0x0096:
            r8 = r15;
            r5 = r0;
            r6 = r5;
            r7 = r6;
            r9 = r7;
            goto L_0x013c;
        L_0x009d:
            r15 = r13.readString();
            r13 = r13.createStringArray();
            r6 = r13;
            r5 = r15;
            r7 = r0;
            goto L_0x013a;
        L_0x00aa:
            r15 = r13.readString();
            r1 = r13.createStringArray();
            r5 = r13.readString();
            r6 = r13.readStrongBinder();
            r7 = r13.readString();
            r8 = r13.readInt();
            if (r8 == 0) goto L_0x00d2;
        L_0x00c4:
            r0 = android.os.Bundle.CREATOR;
            r13 = r0.createFromParcel(r13);
            r13 = (android.os.Bundle) r13;
            r9 = r5;
            r8 = r6;
            r10 = r7;
            r7 = r13;
            r5 = r15;
            goto L_0x00fa;
        L_0x00d2:
            r9 = r5;
            r8 = r6;
            r10 = r7;
            r5 = r15;
            r7 = r0;
            goto L_0x00fa;
        L_0x00d8:
            r15 = r13.readString();
            r1 = r13.createStringArray();
            r5 = r13.readString();
            r6 = r13.readInt();
            if (r6 == 0) goto L_0x00f6;
        L_0x00ea:
            r6 = android.os.Bundle.CREATOR;
            r13 = r6.createFromParcel(r13);
            r13 = (android.os.Bundle) r13;
            r7 = r13;
            r9 = r15;
            r8 = r0;
            goto L_0x00f9;
        L_0x00f6:
            r9 = r15;
            r7 = r0;
            r8 = r7;
        L_0x00f9:
            r10 = r8;
        L_0x00fa:
            r6 = r1;
            goto L_0x013d;
        L_0x00fc:
            r13 = r13.readString();
            r5 = r13;
            r6 = r0;
            goto L_0x0139;
        L_0x0103:
            r15 = r13.createStringArray();
            r1 = r13.readString();
            r5 = r13.readInt();
            if (r5 == 0) goto L_0x011d;
        L_0x0111:
            r5 = android.os.Bundle.CREATOR;
            r13 = r5.createFromParcel(r13);
            r13 = (android.os.Bundle) r13;
            r7 = r13;
            r6 = r15;
            r8 = r0;
            goto L_0x0120;
        L_0x011d:
            r6 = r15;
            r7 = r0;
            r8 = r7;
        L_0x0120:
            r9 = r8;
            r10 = r9;
            r5 = r1;
            goto L_0x013d;
        L_0x0124:
            r15 = r13.readInt();
            if (r15 == 0) goto L_0x0137;
        L_0x012a:
            r15 = android.os.Bundle.CREATOR;
            r13 = r15.createFromParcel(r13);
            r13 = (android.os.Bundle) r13;
            r7 = r13;
            r5 = r0;
            r6 = r5;
            r8 = r6;
            goto L_0x013b;
        L_0x0137:
            r5 = r0;
            r6 = r5;
        L_0x0139:
            r7 = r6;
        L_0x013a:
            r8 = r7;
        L_0x013b:
            r9 = r8;
        L_0x013c:
            r10 = r9;
        L_0x013d:
            r0 = r11;
            r1 = r12;
            r0.getLegacyService(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        L_0x0142:
            r14.writeNoException();
            r12 = 1;
            return r12;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.IGmsServiceBroker.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        protected void validateAccount(IGmsCallbacks iGmsCallbacks, ValidateAccountRequest validateAccountRequest) throws RemoteException {
            throw new UnsupportedOperationException();
        }
    }

    void getService(IGmsCallbacks iGmsCallbacks, GetServiceRequest getServiceRequest) throws RemoteException;
}
