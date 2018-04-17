package com.google.android.gms.wearable;

import com.google.android.gms.common.data.DataHolder;

final class zzl implements Runnable {
    private final /* synthetic */ DataHolder zzan;
    private final /* synthetic */ zzd zzao;

    zzl(zzd com_google_android_gms_wearable_WearableListenerService_zzd, DataHolder dataHolder) {
        this.zzao = com_google_android_gms_wearable_WearableListenerService_zzd;
        this.zzan = dataHolder;
    }

    public final void run() {
        DataEventBuffer dataEventBuffer = new DataEventBuffer(this.zzan);
        try {
            this.zzao.zzak.onDataChanged(dataEventBuffer);
        } finally {
            dataEventBuffer.release();
        }
    }
}
