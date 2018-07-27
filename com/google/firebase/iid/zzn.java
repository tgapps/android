package com.google.firebase.iid;

import com.google.android.gms.tasks.Task;

final /* synthetic */ class zzn implements zzaq {
    private final FirebaseInstanceId zzaq;
    private final String zzar;
    private final String zzas;
    private final String zzav;

    zzn(FirebaseInstanceId firebaseInstanceId, String str, String str2, String str3) {
        this.zzaq = firebaseInstanceId;
        this.zzar = str;
        this.zzas = str2;
        this.zzav = str3;
    }

    public final Task zzt() {
        return this.zzaq.zza(this.zzar, this.zzas, this.zzav);
    }
}
