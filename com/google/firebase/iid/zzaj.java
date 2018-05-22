package com.google.firebase.iid;

import android.util.Pair;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzaj implements zzak {
    private final TaskCompletionSource zzbb;
    private final zzah zzcc;
    private final zzak zzcd;
    private final Pair zzce;

    zzaj(zzah com_google_firebase_iid_zzah, zzak com_google_firebase_iid_zzak, TaskCompletionSource taskCompletionSource, Pair pair) {
        this.zzcc = com_google_firebase_iid_zzah;
        this.zzcd = com_google_firebase_iid_zzak;
        this.zzbb = taskCompletionSource;
        this.zzce = pair;
    }

    public final String zzp() {
        return this.zzcc.zza(this.zzcd, this.zzbb, this.zzce);
    }
}
