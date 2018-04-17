package com.google.android.gms.wearable.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.Channel.GetOutputStreamResult;

final class zzbc extends zzn<GetOutputStreamResult> {
    private final /* synthetic */ zzay zzcm;

    zzbc(zzay com_google_android_gms_wearable_internal_zzay, GoogleApiClient googleApiClient) {
        this.zzcm = com_google_android_gms_wearable_internal_zzay;
        super(googleApiClient);
    }

    public final /* synthetic */ Result createFailedResult(Status status) {
        return new zzbh(status, null);
    }

    protected final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        zzhg com_google_android_gms_wearable_internal_zzhg = (zzhg) anyClient;
        String zza = this.zzcm.zzce;
        zzei com_google_android_gms_wearable_internal_zzbr = new zzbr();
        ((zzep) com_google_android_gms_wearable_internal_zzhg.getService()).zzb(new zzgt(this, com_google_android_gms_wearable_internal_zzbr), com_google_android_gms_wearable_internal_zzbr, zza);
    }
}
