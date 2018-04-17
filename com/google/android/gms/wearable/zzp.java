package com.google.android.gms.wearable;

import java.util.List;

final class zzp implements Runnable {
    private final /* synthetic */ zzd zzao;
    private final /* synthetic */ List zzar;

    zzp(zzd com_google_android_gms_wearable_WearableListenerService_zzd, List list) {
        this.zzao = com_google_android_gms_wearable_WearableListenerService_zzd;
        this.zzar = list;
    }

    public final void run() {
        this.zzao.zzak.onConnectedNodes(this.zzar);
    }
}
