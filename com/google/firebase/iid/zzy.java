package com.google.firebase.iid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

final class zzy extends Handler {
    private final /* synthetic */ zzx zzbrt;

    zzy(zzx com_google_firebase_iid_zzx, Looper looper) {
        this.zzbrt = com_google_firebase_iid_zzx;
        super(looper);
    }

    public final void handleMessage(Message message) {
        this.zzbrt.zzb(message);
    }
}
