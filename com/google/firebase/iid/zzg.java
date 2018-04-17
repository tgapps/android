package com.google.firebase.iid;

import android.util.Log;

final class zzg implements Runnable {
    private final /* synthetic */ zzd zzbpz;
    private final /* synthetic */ zzf zzbqa;

    zzg(zzf com_google_firebase_iid_zzf, zzd com_google_firebase_iid_zzd) {
        this.zzbqa = com_google_firebase_iid_zzf;
        this.zzbpz = com_google_firebase_iid_zzd;
    }

    public final void run() {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "bg processing of the intent starting now");
        }
        this.zzbqa.zzbpy.zzh(this.zzbpz.intent);
        this.zzbpz.finish();
    }
}
