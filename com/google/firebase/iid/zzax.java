package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import javax.annotation.Nullable;

final class zzax extends BroadcastReceiver {
    @Nullable
    private zzaw zzdb;

    public zzax(zzaw com_google_firebase_iid_zzaw) {
        this.zzdb = com_google_firebase_iid_zzaw;
    }

    public final void onReceive(Context context, Intent intent) {
        if (this.zzdb != null && this.zzdb.zzan()) {
            if (FirebaseInstanceId.zzk()) {
                Log.d("FirebaseInstanceId", "Connectivity changed. Starting background sync.");
            }
            FirebaseInstanceId.zza(this.zzdb, 0);
            this.zzdb.getContext().unregisterReceiver(this);
            this.zzdb = null;
        }
    }

    public final void zzao() {
        if (FirebaseInstanceId.zzk()) {
            Log.d("FirebaseInstanceId", "Connectivity change received registered");
        }
        this.zzdb.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
}
