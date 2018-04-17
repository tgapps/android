package com.google.android.gms.common.api.internal;

import com.google.android.gms.signin.internal.BaseSignInCallbacks;
import com.google.android.gms.signin.internal.SignInResponse;
import java.lang.ref.WeakReference;

final class zzaq extends BaseSignInCallbacks {
    private final WeakReference<zzaj> zzhw;

    zzaq(zzaj com_google_android_gms_common_api_internal_zzaj) {
        this.zzhw = new WeakReference(com_google_android_gms_common_api_internal_zzaj);
    }

    public final void onSignInComplete(SignInResponse signInResponse) {
        zzaj com_google_android_gms_common_api_internal_zzaj = (zzaj) this.zzhw.get();
        if (com_google_android_gms_common_api_internal_zzaj != null) {
            com_google_android_gms_common_api_internal_zzaj.zzhf.zza(new zzar(this, com_google_android_gms_common_api_internal_zzaj, com_google_android_gms_common_api_internal_zzaj, signInResponse));
        }
    }
}
