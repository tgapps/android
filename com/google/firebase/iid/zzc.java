package com.google.firebase.iid;

import android.content.Intent;

final class zzc implements Runnable {
    private final /* synthetic */ Intent zzp;
    private final /* synthetic */ Intent zzq;
    private final /* synthetic */ zzb zzr;

    zzc(zzb com_google_firebase_iid_zzb, Intent intent, Intent intent2) {
        this.zzr = com_google_firebase_iid_zzb;
        this.zzp = intent;
        this.zzq = intent2;
    }

    public final void run() {
        this.zzr.zzd(this.zzp);
        this.zzr.zza(this.zzq);
    }
}
