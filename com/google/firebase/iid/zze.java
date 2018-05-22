package com.google.firebase.iid;

import android.content.Intent;
import android.util.Log;

final class zze implements Runnable {
    private final /* synthetic */ Intent zzp;
    private final /* synthetic */ zzd zzv;

    zze(zzd com_google_firebase_iid_zzd, Intent intent) {
        this.zzv = com_google_firebase_iid_zzd;
        this.zzp = intent;
    }

    public final void run() {
        String action = this.zzp.getAction();
        Log.w("EnhancedIntentService", new StringBuilder(String.valueOf(action).length() + 61).append("Service took too long to process intent: ").append(action).append(" App may get closed.").toString());
        this.zzv.finish();
    }
}
