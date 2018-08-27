package com.google.firebase.iid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

final class zzat extends Handler {
    private final /* synthetic */ zzas zzct;

    zzat(zzas com_google_firebase_iid_zzas, Looper looper) {
        this.zzct = com_google_firebase_iid_zzas;
        super(looper);
    }

    public final void handleMessage(Message message) {
        this.zzct.zzb(message);
    }
}
