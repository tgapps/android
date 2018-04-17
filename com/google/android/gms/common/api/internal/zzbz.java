package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;

final class zzbz implements Runnable {
    private final /* synthetic */ zzby zzlx;

    zzbz(zzby com_google_android_gms_common_api_internal_zzby) {
        this.zzlx = com_google_android_gms_common_api_internal_zzby;
    }

    public final void run() {
        this.zzlx.zzlw.zzg(new ConnectionResult(4));
    }
}
