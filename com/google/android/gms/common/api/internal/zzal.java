package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;

final class zzal implements ConnectionProgressReportCallbacks {
    private final Api<?> mApi;
    private final boolean zzfo;
    private final WeakReference<zzaj> zzhw;

    public zzal(zzaj com_google_android_gms_common_api_internal_zzaj, Api<?> api, boolean z) {
        this.zzhw = new WeakReference(com_google_android_gms_common_api_internal_zzaj);
        this.mApi = api;
        this.zzfo = z;
    }

    public final void onReportServiceBinding(ConnectionResult connectionResult) {
        boolean z = false;
        zzaj com_google_android_gms_common_api_internal_zzaj = (zzaj) this.zzhw.get();
        if (com_google_android_gms_common_api_internal_zzaj != null) {
            if (Looper.myLooper() == com_google_android_gms_common_api_internal_zzaj.zzhf.zzfq.getLooper()) {
                z = true;
            }
            Preconditions.checkState(z, "onReportServiceBinding must be called on the GoogleApiClient handler thread");
            com_google_android_gms_common_api_internal_zzaj.zzga.lock();
            try {
                if (com_google_android_gms_common_api_internal_zzaj.zze(0)) {
                    if (!connectionResult.isSuccess()) {
                        com_google_android_gms_common_api_internal_zzaj.zzb(connectionResult, this.mApi, this.zzfo);
                    }
                    if (com_google_android_gms_common_api_internal_zzaj.zzar()) {
                        com_google_android_gms_common_api_internal_zzaj.zzas();
                    }
                    com_google_android_gms_common_api_internal_zzaj.zzga.unlock();
                }
            } finally {
                com_google_android_gms_common_api_internal_zzaj.zzga.unlock();
            }
        }
    }
}
