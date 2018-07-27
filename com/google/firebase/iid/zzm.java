package com.google.firebase.iid;

import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzm implements Runnable {
    private final FirebaseInstanceId zzaq;
    private final String zzar;
    private final String zzas;
    private final TaskCompletionSource zzat;
    private final String zzau;

    zzm(FirebaseInstanceId firebaseInstanceId, String str, String str2, TaskCompletionSource taskCompletionSource, String str3) {
        this.zzaq = firebaseInstanceId;
        this.zzar = str;
        this.zzas = str2;
        this.zzat = taskCompletionSource;
        this.zzau = str3;
    }

    public final void run() {
        this.zzaq.zza(this.zzar, this.zzas, this.zzat, this.zzau);
    }
}
