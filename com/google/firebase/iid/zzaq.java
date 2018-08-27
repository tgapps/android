package com.google.firebase.iid;

import android.util.Pair;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

final /* synthetic */ class zzaq implements Continuation {
    private final zzap zzcm;
    private final Pair zzcn;

    zzaq(zzap com_google_firebase_iid_zzap, Pair pair) {
        this.zzcm = com_google_firebase_iid_zzap;
        this.zzcn = pair;
    }

    public final Object then(Task task) {
        return this.zzcm.zza(this.zzcn, task);
    }
}
