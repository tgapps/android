package com.google.android.gms.wearable;

import com.google.android.gms.wearable.internal.zzi;

final class zzs implements Runnable {
    private final /* synthetic */ zzd zzao;
    private final /* synthetic */ zzi zzau;

    zzs(zzd com_google_android_gms_wearable_WearableListenerService_zzd, zzi com_google_android_gms_wearable_internal_zzi) {
        this.zzao = com_google_android_gms_wearable_WearableListenerService_zzd;
        this.zzau = com_google_android_gms_wearable_internal_zzi;
    }

    public final void run() {
        this.zzao.zzak.onEntityUpdate(this.zzau);
    }
}
