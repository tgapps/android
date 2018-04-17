package com.google.android.gms.common.api.internal;

final class zzbm implements Runnable {
    private final /* synthetic */ zzbl zzkm;

    zzbm(zzbl com_google_android_gms_common_api_internal_zzbl) {
        this.zzkm = com_google_android_gms_common_api_internal_zzbl;
    }

    public final void run() {
        this.zzkm.zzkk.zzka.disconnect();
    }
}
