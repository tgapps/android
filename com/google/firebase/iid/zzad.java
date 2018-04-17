package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import javax.annotation.Nullable;

final class zzad extends BroadcastReceiver {
    @Nullable
    private zzac zzbsf;

    public zzad(zzac com_google_firebase_iid_zzac) {
        this.zzbsf = com_google_firebase_iid_zzac;
    }

    public final void onReceive(Context context, Intent intent) {
        if (this.zzbsf != null && this.zzbsf.zztg()) {
            if (FirebaseInstanceId.zzsj()) {
                Log.d("FirebaseInstanceId", "Connectivity changed. Starting background sync.");
            }
            FirebaseInstanceId.zza(this.zzbsf, 0);
            this.zzbsf.getContext().unregisterReceiver(this);
            this.zzbsf = null;
        }
    }

    public final void zzth() {
        if (FirebaseInstanceId.zzsj()) {
            Log.d("FirebaseInstanceId", "Connectivity change received registered");
        }
        this.zzbsf.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
}
