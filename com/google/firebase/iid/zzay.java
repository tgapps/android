package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import javax.annotation.Nullable;

final class zzay extends BroadcastReceiver {
    @Nullable
    private zzax zzdh;

    public zzay(zzax com_google_firebase_iid_zzax) {
        this.zzdh = com_google_firebase_iid_zzax;
    }

    public final void zzao() {
        if (FirebaseInstanceId.zzk()) {
            Log.d("FirebaseInstanceId", "Connectivity change received registered");
        }
        this.zzdh.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public final void onReceive(Context context, Intent intent) {
        if (this.zzdh != null && this.zzdh.zzan()) {
            if (FirebaseInstanceId.zzk()) {
                Log.d("FirebaseInstanceId", "Connectivity changed. Starting background sync.");
            }
            FirebaseInstanceId.zza(this.zzdh, 0);
            this.zzdh.getContext().unregisterReceiver(this);
            this.zzdh = null;
        }
    }
}
