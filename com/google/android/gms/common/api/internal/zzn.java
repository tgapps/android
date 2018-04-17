package com.google.android.gms.common.api.internal;

import android.app.Dialog;
import com.google.android.gms.common.api.internal.GooglePlayServicesUpdatedReceiver.Callback;

final class zzn extends Callback {
    private final /* synthetic */ Dialog zzex;
    private final /* synthetic */ zzm zzey;

    zzn(zzm com_google_android_gms_common_api_internal_zzm, Dialog dialog) {
        this.zzey = com_google_android_gms_common_api_internal_zzm;
        this.zzex = dialog;
    }

    public final void zzv() {
        this.zzey.zzew.zzt();
        if (this.zzex.isShowing()) {
            this.zzex.dismiss();
        }
    }
}
