package com.google.firebase.iid;

import android.content.Intent;
import android.util.Log;

public class FirebaseInstanceIdService extends zzb {
    public void onTokenRefresh() {
    }

    protected final Intent zzf(Intent intent) {
        return (Intent) zzz.zzta().zzbrx.poll();
    }

    public final void zzh(Intent intent) {
        if ("com.google.firebase.iid.TOKEN_REFRESH".equals(intent.getAction())) {
            onTokenRefresh();
            return;
        }
        String stringExtra = intent.getStringExtra("CMD");
        if (stringExtra != null) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(intent.getExtras());
                StringBuilder stringBuilder = new StringBuilder((21 + String.valueOf(stringExtra).length()) + String.valueOf(valueOf).length());
                stringBuilder.append("Received command: ");
                stringBuilder.append(stringExtra);
                stringBuilder.append(" - ");
                stringBuilder.append(valueOf);
                Log.d("FirebaseInstanceId", stringBuilder.toString());
            }
            if (!"RST".equals(stringExtra)) {
                if (!"RST_FULL".equals(stringExtra)) {
                    if ("SYNC".equals(stringExtra)) {
                        FirebaseInstanceId.getInstance().zzsl();
                        return;
                    }
                }
            }
            FirebaseInstanceId.getInstance().zzsk();
        }
    }
}
