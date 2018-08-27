package com.google.android.gms.measurement.internal;

import android.content.Intent;

final /* synthetic */ class zzem implements Runnable {
    private final int zzacb;
    private final zzel zzasr;
    private final zzap zzass;
    private final Intent zzast;

    zzem(zzel com_google_android_gms_measurement_internal_zzel, int i, zzap com_google_android_gms_measurement_internal_zzap, Intent intent) {
        this.zzasr = com_google_android_gms_measurement_internal_zzel;
        this.zzacb = i;
        this.zzass = com_google_android_gms_measurement_internal_zzap;
        this.zzast = intent;
    }

    public final void run() {
        this.zzasr.zza(this.zzacb, this.zzass, this.zzast);
    }
}
