package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager.zza;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zzc<T> extends zzb {
    protected final TaskCompletionSource<T> zzdu;

    public zzc(int i, TaskCompletionSource<T> taskCompletionSource) {
        super(i);
        this.zzdu = taskCompletionSource;
    }

    public void zza(Status status) {
        this.zzdu.trySetException(new ApiException(status));
    }

    public final void zza(zza<?> com_google_android_gms_common_api_internal_GoogleApiManager_zza_) throws DeadObjectException {
        try {
            zzb(com_google_android_gms_common_api_internal_GoogleApiManager_zza_);
        } catch (RemoteException e) {
            zza(zzb.zza(e));
            throw e;
        } catch (RemoteException e2) {
            zza(zzb.zza(e2));
        } catch (RuntimeException e3) {
            zza(e3);
        }
    }

    public void zza(zzaa com_google_android_gms_common_api_internal_zzaa, boolean z) {
    }

    public void zza(RuntimeException runtimeException) {
        this.zzdu.trySetException(runtimeException);
    }

    protected abstract void zzb(zza<?> com_google_android_gms_common_api_internal_GoogleApiManager_zza_) throws RemoteException;
}
