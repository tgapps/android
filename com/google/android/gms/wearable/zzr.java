package com.google.android.gms.wearable;

import com.google.android.gms.wearable.internal.zzl;

final class zzr implements Runnable {
    private final /* synthetic */ zzd zzao;
    private final /* synthetic */ zzl zzat;

    zzr(zzd com_google_android_gms_wearable_WearableListenerService_zzd, zzl com_google_android_gms_wearable_internal_zzl) {
        this.zzao = com_google_android_gms_wearable_WearableListenerService_zzd;
        this.zzat = com_google_android_gms_wearable_internal_zzl;
    }

    public final void run() {
        this.zzao.zzak.onNotificationReceived(this.zzat);
    }
}
