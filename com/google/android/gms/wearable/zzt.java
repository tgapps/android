package com.google.android.gms.wearable;

import com.google.android.gms.wearable.internal.zzaw;

final class zzt implements Runnable {
    private final /* synthetic */ zzd zzao;
    private final /* synthetic */ zzaw zzav;

    zzt(zzd com_google_android_gms_wearable_WearableListenerService_zzd, zzaw com_google_android_gms_wearable_internal_zzaw) {
        this.zzao = com_google_android_gms_wearable_WearableListenerService_zzd;
        this.zzav = com_google_android_gms_wearable_internal_zzaw;
    }

    public final void run() {
        this.zzav.zza(this.zzao.zzak);
        this.zzav.zza(this.zzao.zzak.zzaj);
    }
}
