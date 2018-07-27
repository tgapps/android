package com.google.firebase.iid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

final class zzas extends Handler {
    private final /* synthetic */ zzar zzcn;

    zzas(zzar com_google_firebase_iid_zzar, Looper looper) {
        this.zzcn = com_google_firebase_iid_zzar;
        super(looper);
    }

    public final void handleMessage(Message message) {
        this.zzcn.zzb(message);
    }
}
