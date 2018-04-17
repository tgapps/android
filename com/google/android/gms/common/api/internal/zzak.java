package com.google.android.gms.common.api.internal;

final class zzak implements Runnable {
    private final /* synthetic */ zzaj zzhv;

    zzak(zzaj com_google_android_gms_common_api_internal_zzaj) {
        this.zzhv = com_google_android_gms_common_api_internal_zzaj;
    }

    public final void run() {
        this.zzhv.zzgk.cancelAvailabilityErrorNotifications(this.zzhv.mContext);
    }
}
