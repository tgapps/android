package com.google.android.gms.wearable;

import com.google.android.gms.wearable.internal.zzah;

final class zzq implements Runnable {
    private final /* synthetic */ zzd zzao;
    private final /* synthetic */ zzah zzas;

    zzq(zzd com_google_android_gms_wearable_WearableListenerService_zzd, zzah com_google_android_gms_wearable_internal_zzah) {
        this.zzao = com_google_android_gms_wearable_WearableListenerService_zzd;
        this.zzas = com_google_android_gms_wearable_internal_zzah;
    }

    public final void run() {
        this.zzao.zzak.onCapabilityChanged(this.zzas);
    }
}
