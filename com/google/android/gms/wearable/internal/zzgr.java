package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.internal.BaseImplementation.ResultHolder;
import com.google.android.gms.wearable.CapabilityApi.GetCapabilityResult;

final class zzgr extends zzgm<GetCapabilityResult> {
    public zzgr(ResultHolder<GetCapabilityResult> resultHolder) {
        super(resultHolder);
    }

    public final void zza(zzdk com_google_android_gms_wearable_internal_zzdk) {
        zza(new zzy(zzgd.zzb(com_google_android_gms_wearable_internal_zzdk.statusCode), com_google_android_gms_wearable_internal_zzdk.zzdq == null ? null : new zzw(com_google_android_gms_wearable_internal_zzdk.zzdq)));
    }
}
