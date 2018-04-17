package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.GmsClientEventManager.GmsClientEventState;

final class zzaw implements GmsClientEventState {
    private final /* synthetic */ zzav zzit;

    zzaw(zzav com_google_android_gms_common_api_internal_zzav) {
        this.zzit = com_google_android_gms_common_api_internal_zzav;
    }

    public final Bundle getConnectionHint() {
        return null;
    }

    public final boolean isConnected() {
        return this.zzit.isConnected();
    }
}
