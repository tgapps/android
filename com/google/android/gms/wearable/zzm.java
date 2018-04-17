package com.google.android.gms.wearable;

import com.google.android.gms.wearable.internal.zzfe;

final class zzm implements Runnable {
    private final /* synthetic */ zzd zzao;
    private final /* synthetic */ zzfe zzap;

    zzm(zzd com_google_android_gms_wearable_WearableListenerService_zzd, zzfe com_google_android_gms_wearable_internal_zzfe) {
        this.zzao = com_google_android_gms_wearable_WearableListenerService_zzd;
        this.zzap = com_google_android_gms_wearable_internal_zzfe;
    }

    public final void run() {
        this.zzao.zzak.onMessageReceived(this.zzap);
    }
}
