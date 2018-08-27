package com.google.android.gms.measurement.internal;

import android.app.job.JobParameters;

final /* synthetic */ class zzen implements Runnable {
    private final JobParameters zzace;
    private final zzel zzasr;
    private final zzap zzasu;

    zzen(zzel com_google_android_gms_measurement_internal_zzel, zzap com_google_android_gms_measurement_internal_zzap, JobParameters jobParameters) {
        this.zzasr = com_google_android_gms_measurement_internal_zzel;
        this.zzasu = com_google_android_gms_measurement_internal_zzap;
        this.zzace = jobParameters;
    }

    public final void run() {
        this.zzasr.zza(this.zzasu, this.zzace);
    }
}
