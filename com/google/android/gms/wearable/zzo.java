package com.google.android.gms.wearable;

import com.google.android.gms.wearable.internal.zzfo;

final class zzo implements Runnable {
    private final /* synthetic */ zzd zzao;
    private final /* synthetic */ zzfo zzaq;

    zzo(zzd com_google_android_gms_wearable_WearableListenerService_zzd, zzfo com_google_android_gms_wearable_internal_zzfo) {
        this.zzao = com_google_android_gms_wearable_WearableListenerService_zzd;
        this.zzaq = com_google_android_gms_wearable_internal_zzfo;
    }

    public final void run() {
        this.zzao.zzak.onPeerDisconnected(this.zzaq);
    }
}
