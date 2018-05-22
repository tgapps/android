package com.google.android.gms.internal.measurement;

import android.content.Intent;

final /* synthetic */ class zzjd implements Runnable {
    private final int zzabp;
    private final zzjc zzapp;
    private final zzfg zzapq;
    private final Intent zzapr;

    zzjd(zzjc com_google_android_gms_internal_measurement_zzjc, int i, zzfg com_google_android_gms_internal_measurement_zzfg, Intent intent) {
        this.zzapp = com_google_android_gms_internal_measurement_zzjc;
        this.zzabp = i;
        this.zzapq = com_google_android_gms_internal_measurement_zzfg;
        this.zzapr = intent;
    }

    public final void run() {
        this.zzapp.zza(this.zzabp, this.zzapq, this.zzapr);
    }
}
