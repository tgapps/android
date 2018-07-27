package com.google.firebase.iid;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzo implements OnCompleteListener {
    private final FirebaseInstanceId zzaq;
    private final String zzar;
    private final String zzas;
    private final TaskCompletionSource zzat;
    private final String zzau;

    zzo(FirebaseInstanceId firebaseInstanceId, String str, String str2, TaskCompletionSource taskCompletionSource, String str3) {
        this.zzaq = firebaseInstanceId;
        this.zzar = str;
        this.zzas = str2;
        this.zzat = taskCompletionSource;
        this.zzau = str3;
    }

    public final void onComplete(Task task) {
        this.zzaq.zza(this.zzar, this.zzas, this.zzat, this.zzau, task);
    }
}
