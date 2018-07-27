package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzq implements Runnable {
    private final zzp zzbb;
    private final Bundle zzbc;
    private final TaskCompletionSource zzbd;

    zzq(zzp com_google_firebase_iid_zzp, Bundle bundle, TaskCompletionSource taskCompletionSource) {
        this.zzbb = com_google_firebase_iid_zzp;
        this.zzbc = bundle;
        this.zzbd = taskCompletionSource;
    }

    public final void run() {
        this.zzbb.zza(this.zzbc, this.zzbd);
    }
}
