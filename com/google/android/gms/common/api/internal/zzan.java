package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import javax.annotation.concurrent.GuardedBy;

final class zzan extends zzbe {
    private final /* synthetic */ ConnectionResult zzhy;
    private final /* synthetic */ zzam zzhz;

    zzan(zzam com_google_android_gms_common_api_internal_zzam, zzbc com_google_android_gms_common_api_internal_zzbc, ConnectionResult connectionResult) {
        this.zzhz = com_google_android_gms_common_api_internal_zzam;
        this.zzhy = connectionResult;
        super(com_google_android_gms_common_api_internal_zzbc);
    }

    @GuardedBy("mLock")
    public final void zzaq() {
        this.zzhz.zzhv.zze(this.zzhy);
    }
}
