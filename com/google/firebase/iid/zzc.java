package com.google.firebase.iid;

import android.content.Intent;

final class zzc implements Runnable {
    private final /* synthetic */ Intent zzl;
    private final /* synthetic */ Intent zzm;
    private final /* synthetic */ zzb zzn;

    zzc(zzb com_google_firebase_iid_zzb, Intent intent, Intent intent2) {
        this.zzn = com_google_firebase_iid_zzb;
        this.zzl = intent;
        this.zzm = intent2;
    }

    public final void run() {
        this.zzn.zzd(this.zzl);
        this.zzn.zza(this.zzm);
    }
}
