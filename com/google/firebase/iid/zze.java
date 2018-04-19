package com.google.firebase.iid;

import android.content.Intent;
import android.util.Log;

final class zze implements Runnable {
    private final /* synthetic */ Intent zzbpr;
    private final /* synthetic */ zzd zzbpx;

    zze(zzd com_google_firebase_iid_zzd, Intent intent) {
        this.zzbpx = com_google_firebase_iid_zzd;
        this.zzbpr = intent;
    }

    public final void run() {
        String action = this.zzbpr.getAction();
        Log.w("EnhancedIntentService", new StringBuilder(String.valueOf(action).length() + 61).append("Service took too long to process intent: ").append(action).append(" App may get closed.").toString());
        this.zzbpx.finish();
    }
}
