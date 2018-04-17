package com.google.android.gms.internal.config;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;

abstract class zzr<R extends Result> extends ApiMethodImpl<R, zzw> {
    public zzr(GoogleApiClient googleApiClient) {
        super(zze.API, googleApiClient);
    }

    protected /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zzw com_google_android_gms_internal_config_zzw = (zzw) anyClient;
        zza(com_google_android_gms_internal_config_zzw.getContext(), (zzah) com_google_android_gms_internal_config_zzw.getService());
    }

    protected abstract void zza(Context context, zzah com_google_android_gms_internal_config_zzah) throws RemoteException;
}
