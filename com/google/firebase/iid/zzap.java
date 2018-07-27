package com.google.firebase.iid;

import android.util.Pair;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

final /* synthetic */ class zzap implements Continuation {
    private final zzao zzcg;
    private final Pair zzch;

    zzap(zzao com_google_firebase_iid_zzao, Pair pair) {
        this.zzcg = com_google_firebase_iid_zzao;
        this.zzch = pair;
    }

    public final Object then(Task task) {
        return this.zzcg.zza(this.zzch, task);
    }
}
