package com.google.firebase.iid;

import android.os.Handler.Callback;
import android.os.Message;

final /* synthetic */ class zzac implements Callback {
    private final zzab zzbt;

    zzac(zzab com_google_firebase_iid_zzab) {
        this.zzbt = com_google_firebase_iid_zzab;
    }

    public final boolean handleMessage(Message message) {
        return this.zzbt.zza(message);
    }
}
