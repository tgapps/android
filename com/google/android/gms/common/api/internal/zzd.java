package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.api.internal.GoogleApiManager.zza;

public final class zzd<A extends ApiMethodImpl<? extends Result, AnyClient>> extends zzb {
    private final A zzdv;

    public zzd(int i, A a) {
        super(i);
        this.zzdv = a;
    }

    public final void zza(Status status) {
        this.zzdv.setFailedResult(status);
    }

    public final void zza(zza<?> com_google_android_gms_common_api_internal_GoogleApiManager_zza_) throws DeadObjectException {
        try {
            this.zzdv.run(com_google_android_gms_common_api_internal_GoogleApiManager_zza_.zzae());
        } catch (RuntimeException e) {
            zza(e);
        }
    }

    public final void zza(zzaa com_google_android_gms_common_api_internal_zzaa, boolean z) {
        com_google_android_gms_common_api_internal_zzaa.zza(this.zzdv, z);
    }

    public final void zza(RuntimeException runtimeException) {
        String simpleName = runtimeException.getClass().getSimpleName();
        String localizedMessage = runtimeException.getLocalizedMessage();
        StringBuilder stringBuilder = new StringBuilder((2 + String.valueOf(simpleName).length()) + String.valueOf(localizedMessage).length());
        stringBuilder.append(simpleName);
        stringBuilder.append(": ");
        stringBuilder.append(localizedMessage);
        this.zzdv.setFailedResult(new Status(10, stringBuilder.toString()));
    }
}
