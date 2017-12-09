package com.google.android.gms.wearable.internal;

import android.net.Uri;
import com.google.android.gms.internal.zzbaz;

final class zzfy implements Runnable {
    private /* synthetic */ String zzakq;
    private /* synthetic */ boolean zzbSl;
    private /* synthetic */ zzbaz zzbTq;
    private /* synthetic */ zzfw zzbTr;
    private /* synthetic */ Uri zzbzR;

    zzfy(zzfw com_google_android_gms_wearable_internal_zzfw, Uri uri, zzbaz com_google_android_gms_internal_zzbaz, boolean z, String str) {
        this.zzbTr = com_google_android_gms_wearable_internal_zzfw;
        this.zzbzR = uri;
        this.zzbTq = com_google_android_gms_internal_zzbaz;
        this.zzbSl = z;
        this.zzakq = str;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r4 = this;
        r0 = "WearableClient";
        r1 = 2;
        r0 = android.util.Log.isLoggable(r0, r1);
        if (r0 == 0) goto L_0x0013;
    L_0x000a:
        r0 = "WearableClient";
        r1 = "Executing receiveFileFromChannelTask";
        android.util.Log.v(r0, r1);
    L_0x0013:
        r0 = "file";
        r1 = r4.zzbzR;
        r1 = r1.getScheme();
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x003b;
    L_0x0022:
        r0 = "WearableClient";
        r1 = "Channel.receiveFile used with non-file URI";
        android.util.Log.w(r0, r1);
        r0 = r4.zzbTq;
        r1 = new com.google.android.gms.common.api.Status;
        r2 = 10;
        r3 = "Channel.receiveFile used with non-file URI";
        r1.<init>(r2, r3);
        r0.zzr(r1);
    L_0x003a:
        return;
    L_0x003b:
        r1 = new java.io.File;
        r0 = r4.zzbzR;
        r0 = r0.getPath();
        r1.<init>(r0);
        r2 = 671088640; // 0x28000000 float:7.1054274E-15 double:3.315618423E-315;
        r0 = r4.zzbSl;
        if (r0 == 0) goto L_0x0076;
    L_0x004c:
        r0 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
    L_0x004e:
        r0 = r0 | r2;
        r1 = android.os.ParcelFileDescriptor.open(r1, r0);	 Catch:{ FileNotFoundException -> 0x0078 }
        r0 = r4.zzbTr;	 Catch:{ RemoteException -> 0x00ae }
        r0 = r0.zzrf();	 Catch:{ RemoteException -> 0x00ae }
        r0 = (com.google.android.gms.wearable.internal.zzdn) r0;	 Catch:{ RemoteException -> 0x00ae }
        r2 = new com.google.android.gms.wearable.internal.zzfv;	 Catch:{ RemoteException -> 0x00ae }
        r3 = r4.zzbTq;	 Catch:{ RemoteException -> 0x00ae }
        r2.<init>(r3);	 Catch:{ RemoteException -> 0x00ae }
        r3 = r4.zzakq;	 Catch:{ RemoteException -> 0x00ae }
        r0.zza(r2, r3, r1);	 Catch:{ RemoteException -> 0x00ae }
        r1.close();	 Catch:{ IOException -> 0x006b }
        goto L_0x003a;
    L_0x006b:
        r0 = move-exception;
        r1 = "WearableClient";
        r2 = "Failed to close targetFd";
        android.util.Log.w(r1, r2, r0);
        goto L_0x003a;
    L_0x0076:
        r0 = 0;
        goto L_0x004e;
    L_0x0078:
        r0 = move-exception;
        r0 = "WearableClient";
        r1 = java.lang.String.valueOf(r1);
        r2 = java.lang.String.valueOf(r1);
        r2 = r2.length();
        r2 = r2 + 49;
        r3 = new java.lang.StringBuilder;
        r3.<init>(r2);
        r2 = "File couldn't be opened for Channel.receiveFile: ";
        r2 = r3.append(r2);
        r1 = r2.append(r1);
        r1 = r1.toString();
        android.util.Log.w(r0, r1);
        r0 = r4.zzbTq;
        r1 = new com.google.android.gms.common.api.Status;
        r2 = 13;
        r1.<init>(r2);
        r0.zzr(r1);
        goto L_0x003a;
    L_0x00ae:
        r0 = move-exception;
        r2 = "WearableClient";
        r3 = "Channel.receiveFile failed.";
        android.util.Log.w(r2, r3, r0);	 Catch:{ all -> 0x00d5 }
        r0 = r4.zzbTq;	 Catch:{ all -> 0x00d5 }
        r2 = new com.google.android.gms.common.api.Status;	 Catch:{ all -> 0x00d5 }
        r3 = 8;
        r2.<init>(r3);	 Catch:{ all -> 0x00d5 }
        r0.zzr(r2);	 Catch:{ all -> 0x00d5 }
        r1.close();	 Catch:{ IOException -> 0x00c9 }
        goto L_0x003a;
    L_0x00c9:
        r0 = move-exception;
        r1 = "WearableClient";
        r2 = "Failed to close targetFd";
        android.util.Log.w(r1, r2, r0);
        goto L_0x003a;
    L_0x00d5:
        r0 = move-exception;
        r1.close();	 Catch:{ IOException -> 0x00da }
    L_0x00d9:
        throw r0;
    L_0x00da:
        r1 = move-exception;
        r2 = "WearableClient";
        r3 = "Failed to close targetFd";
        android.util.Log.w(r2, r3, r1);
        goto L_0x00d9;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.wearable.internal.zzfy.run():void");
    }
}
