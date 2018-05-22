package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import javax.annotation.Nullable;

final class zzar extends BroadcastReceiver {
    @Nullable
    private zzaq zzcy;

    public zzar(zzaq com_google_firebase_iid_zzaq) {
        this.zzcy = com_google_firebase_iid_zzaq;
    }

    public final void onReceive(Context context, Intent intent) {
        if (this.zzcy != null && this.zzcy.zzaj()) {
            if (FirebaseInstanceId.zzj()) {
                Log.d("FirebaseInstanceId", "Connectivity changed. Starting background sync.");
            }
            FirebaseInstanceId.zza(this.zzcy, 0);
            this.zzcy.getContext().unregisterReceiver(this);
            this.zzcy = null;
        }
    }

    public final void zzak() {
        if (FirebaseInstanceId.zzj()) {
            Log.d("FirebaseInstanceId", "Connectivity change received registered");
        }
        this.zzcy.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
}
