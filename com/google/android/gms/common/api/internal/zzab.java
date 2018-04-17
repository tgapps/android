package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.PendingResult.StatusListener;
import com.google.android.gms.common.api.Status;

final class zzab implements StatusListener {
    private final /* synthetic */ BasePendingResult zzgy;
    private final /* synthetic */ zzaa zzgz;

    zzab(zzaa com_google_android_gms_common_api_internal_zzaa, BasePendingResult basePendingResult) {
        this.zzgz = com_google_android_gms_common_api_internal_zzaa;
        this.zzgy = basePendingResult;
    }

    public final void onComplete(Status status) {
        this.zzgz.zzgw.remove(this.zzgy);
    }
}
