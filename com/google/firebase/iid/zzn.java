package com.google.firebase.iid;

import com.google.android.gms.tasks.Task;

final /* synthetic */ class zzn implements zzar {
    private final FirebaseInstanceId zzaq;
    private final String zzar;
    private final String zzas;
    private final String zzau;
    private final String zzav;

    zzn(FirebaseInstanceId firebaseInstanceId, String str, String str2, String str3, String str4) {
        this.zzaq = firebaseInstanceId;
        this.zzar = str;
        this.zzas = str2;
        this.zzav = str3;
        this.zzau = str4;
    }

    public final Task zzr() {
        return this.zzaq.zza(this.zzar, this.zzas, this.zzav, this.zzau);
    }
}
