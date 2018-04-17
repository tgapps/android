package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.GooglePlayServicesUpdatedReceiver.Callback;
import java.lang.ref.WeakReference;

final class zzbb extends Callback {
    private WeakReference<zzav> zziy;

    zzbb(zzav com_google_android_gms_common_api_internal_zzav) {
        this.zziy = new WeakReference(com_google_android_gms_common_api_internal_zzav);
    }

    public final void zzv() {
        zzav com_google_android_gms_common_api_internal_zzav = (zzav) this.zziy.get();
        if (com_google_android_gms_common_api_internal_zzav != null) {
            com_google_android_gms_common_api_internal_zzav.resume();
        }
    }
}
