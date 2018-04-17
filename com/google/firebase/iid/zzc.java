package com.google.firebase.iid;

import android.content.Intent;

final class zzc implements Runnable {
    private final /* synthetic */ Intent zzbpr;
    private final /* synthetic */ Intent zzbps;
    private final /* synthetic */ zzb zzbpt;

    zzc(zzb com_google_firebase_iid_zzb, Intent intent, Intent intent2) {
        this.zzbpt = com_google_firebase_iid_zzb;
        this.zzbpr = intent;
        this.zzbps = intent2;
    }

    public final void run() {
        this.zzbpt.zzh(this.zzbpr);
        this.zzbpt.zze(this.zzbps);
    }
}
