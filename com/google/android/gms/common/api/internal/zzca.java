package com.google.android.gms.common.api.internal;

import com.google.android.gms.signin.internal.SignInResponse;

final class zzca implements Runnable {
    private final /* synthetic */ SignInResponse zzid;
    private final /* synthetic */ zzby zzlx;

    zzca(zzby com_google_android_gms_common_api_internal_zzby, SignInResponse signInResponse) {
        this.zzlx = com_google_android_gms_common_api_internal_zzby;
        this.zzid = signInResponse;
    }

    public final void run() {
        this.zzlx.zzb(this.zzid);
    }
}
