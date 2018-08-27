package com.google.android.gms.measurement;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import com.google.android.gms.measurement.internal.zzel;
import com.google.android.gms.measurement.internal.zzep;

@TargetApi(24)
public final class AppMeasurementJobService extends JobService implements zzep {
    private zzel<AppMeasurementJobService> zzadr;

    private final zzel<AppMeasurementJobService> zzfu() {
        if (this.zzadr == null) {
            this.zzadr = new zzel(this);
        }
        return this.zzadr;
    }

    public final void onCreate() {
        super.onCreate();
        zzfu().onCreate();
    }

    public final void onDestroy() {
        zzfu().onDestroy();
        super.onDestroy();
    }

    public final boolean onStartJob(JobParameters jobParameters) {
        return zzfu().onStartJob(jobParameters);
    }

    public final boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public final boolean onUnbind(Intent intent) {
        return zzfu().onUnbind(intent);
    }

    public final void onRebind(Intent intent) {
        zzfu().onRebind(intent);
    }

    public final boolean callServiceStopSelfResult(int i) {
        throw new UnsupportedOperationException();
    }

    @TargetApi(24)
    public final void zza(JobParameters jobParameters, boolean z) {
        jobFinished(jobParameters, false);
    }

    public final void zzb(Intent intent) {
    }
}
