package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.api.internal.ListenerHolder.Notifier;
import com.google.android.gms.wearable.CapabilityApi.CapabilityListener;

final class zzho implements Notifier<CapabilityListener> {
    private final /* synthetic */ zzah zzfr;

    zzho(zzah com_google_android_gms_wearable_internal_zzah) {
        this.zzfr = com_google_android_gms_wearable_internal_zzah;
    }

    public final /* synthetic */ void notifyListener(Object obj) {
        ((CapabilityListener) obj).onCapabilityChanged(this.zzfr);
    }

    public final void onNotifyListenerFailed() {
    }
}
