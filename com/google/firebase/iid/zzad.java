package com.google.firebase.iid;

import android.os.Handler.Callback;
import android.os.Message;

final /* synthetic */ class zzad implements Callback {
    private final zzac zzbz;

    zzad(zzac com_google_firebase_iid_zzac) {
        this.zzbz = com_google_firebase_iid_zzac;
    }

    public final boolean handleMessage(Message message) {
        return this.zzbz.zza(message);
    }
}
