package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

final class zzaz extends zzn<Status> {
    private final /* synthetic */ zzay zzcm;

    zzaz(zzay com_google_android_gms_wearable_internal_zzay, GoogleApiClient googleApiClient) {
        this.zzcm = com_google_android_gms_wearable_internal_zzay;
        super(googleApiClient);
    }

    protected final /* synthetic */ Result createFailedResult(Status status) {
        return status;
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zzhg com_google_android_gms_wearable_internal_zzhg = (zzhg) anyClient;
        ((zzep) com_google_android_gms_wearable_internal_zzhg.getService()).zzc(new zzgn(this), this.zzcm.zzce);
    }
}
