package com.google.firebase.iid;

import android.util.Log;

final class zzg implements Runnable {
    private final /* synthetic */ zzd zzx;
    private final /* synthetic */ zzf zzy;

    zzg(zzf com_google_firebase_iid_zzf, zzd com_google_firebase_iid_zzd) {
        this.zzy = com_google_firebase_iid_zzf;
        this.zzx = com_google_firebase_iid_zzd;
    }

    public final void run() {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "bg processing of the intent starting now");
        }
        this.zzy.zzw.zzd(this.zzx.intent);
        this.zzx.finish();
    }
}
