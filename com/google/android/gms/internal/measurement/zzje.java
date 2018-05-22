package com.google.android.gms.internal.measurement;

import android.app.job.JobParameters;

final /* synthetic */ class zzje implements Runnable {
    private final JobParameters zzabs;
    private final zzjc zzapp;
    private final zzfg zzaps;

    zzje(zzjc com_google_android_gms_internal_measurement_zzjc, zzfg com_google_android_gms_internal_measurement_zzfg, JobParameters jobParameters) {
        this.zzapp = com_google_android_gms_internal_measurement_zzjc;
        this.zzaps = com_google_android_gms_internal_measurement_zzfg;
        this.zzabs = jobParameters;
    }

    public final void run() {
        this.zzapp.zza(this.zzaps, this.zzabs);
    }
}
