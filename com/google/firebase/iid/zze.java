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
        StringBuilder stringBuilder = new StringBuilder(61 + String.valueOf(action).length());
        stringBuilder.append("Service took too long to process intent: ");
        stringBuilder.append(action);
        stringBuilder.append(" App may get closed.");
        Log.w("EnhancedIntentService", stringBuilder.toString());
        this.zzbpx.finish();
    }
}
