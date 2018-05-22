package com.google.firebase.iid;

import android.os.Handler.Callback;
import android.os.Message;

final /* synthetic */ class zzv implements Callback {
    private final zzu zzbo;

    zzv(zzu com_google_firebase_iid_zzu) {
        this.zzbo = com_google_firebase_iid_zzu;
    }

    public final boolean handleMessage(Message message) {
        return this.zzbo.zza(message);
    }
}
