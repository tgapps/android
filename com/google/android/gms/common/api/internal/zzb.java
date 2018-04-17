package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager.zza;
import com.google.android.gms.common.util.PlatformVersion;

public abstract class zzb {
    private final int type;

    public zzb(int i) {
        this.type = i;
    }

    private static Status zza(RemoteException remoteException) {
        StringBuilder stringBuilder = new StringBuilder();
        if (PlatformVersion.isAtLeastIceCreamSandwichMR1() && (remoteException instanceof TransactionTooLargeException)) {
            stringBuilder.append("TransactionTooLargeException: ");
        }
        stringBuilder.append(remoteException.getLocalizedMessage());
        return new Status(8, stringBuilder.toString());
    }

    public abstract void zza(Status status);

    public abstract void zza(zza<?> com_google_android_gms_common_api_internal_GoogleApiManager_zza_) throws DeadObjectException;

    public abstract void zza(zzaa com_google_android_gms_common_api_internal_zzaa, boolean z);

    public abstract void zza(RuntimeException runtimeException);
}
