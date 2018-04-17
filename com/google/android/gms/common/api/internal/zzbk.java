package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.GoogleApiManager.zza;

final class zzbk implements Runnable {
    private final /* synthetic */ zza zzkk;
    private final /* synthetic */ ConnectionResult zzkl;

    zzbk(zza com_google_android_gms_common_api_internal_GoogleApiManager_zza, ConnectionResult connectionResult) {
        this.zzkk = com_google_android_gms_common_api_internal_GoogleApiManager_zza;
        this.zzkl = connectionResult;
    }

    public final void run() {
        this.zzkk.onConnectionFailed(this.zzkl);
    }
}
