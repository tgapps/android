package com.google.android.gms.internal.measurement;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import com.google.android.gms.common.internal.Preconditions;

public final class zzjc<T extends Context & zzjg> {
    private final T zzabm;

    public zzjc(T t) {
        Preconditions.checkNotNull(t);
        this.zzabm = t;
    }

    public static boolean zza(Context context, boolean z) {
        Preconditions.checkNotNull(context);
        return VERSION.SDK_INT >= 24 ? zzka.zzc(context, "com.google.android.gms.measurement.AppMeasurementJobService") : zzka.zzc(context, "com.google.android.gms.measurement.AppMeasurementService");
    }

    private final void zzb(Runnable runnable) {
        zzjr zzg = zzgl.zzg(this.zzabm);
        zzg.zzgd().zzc(new zzjf(this, zzg, runnable));
    }

    private final zzfg zzge() {
        return zzgl.zzg(this.zzabm).zzge();
    }

    public final IBinder onBind(Intent intent) {
        if (intent == null) {
            zzge().zzim().log("onBind called with null intent");
            return null;
        }
        String action = intent.getAction();
        if ("com.google.android.gms.measurement.START".equals(action)) {
            return new zzgn(zzgl.zzg(this.zzabm));
        }
        zzge().zzip().zzg("onBind received unknown action", action);
        return null;
    }

    public final void onCreate() {
        zzgl.zzg(this.zzabm).zzge().zzit().log("Local AppMeasurementService is starting up");
    }

    public final void onDestroy() {
        zzgl.zzg(this.zzabm).zzge().zzit().log("Local AppMeasurementService is shutting down");
    }

    public final void onRebind(Intent intent) {
        if (intent == null) {
            zzge().zzim().log("onRebind called with null intent");
            return;
        }
        zzge().zzit().zzg("onRebind called. action", intent.getAction());
    }

    public final int onStartCommand(Intent intent, int i, int i2) {
        zzfg zzge = zzgl.zzg(this.zzabm).zzge();
        if (intent == null) {
            zzge.zzip().log("AppMeasurementService started with null intent");
        } else {
            String action = intent.getAction();
            zzge.zzit().zze("Local AppMeasurementService called. startId, action", Integer.valueOf(i2), action);
            if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
                zzb(new zzjd(this, i2, zzge, intent));
            }
        }
        return 2;
    }

    @TargetApi(24)
    public final boolean onStartJob(JobParameters jobParameters) {
        zzfg zzge = zzgl.zzg(this.zzabm).zzge();
        String string = jobParameters.getExtras().getString("action");
        zzge.zzit().zzg("Local AppMeasurementJobService called. action", string);
        if ("com.google.android.gms.measurement.UPLOAD".equals(string)) {
            zzb(new zzje(this, zzge, jobParameters));
        }
        return true;
    }

    public final boolean onUnbind(Intent intent) {
        if (intent == null) {
            zzge().zzim().log("onUnbind called with null intent");
        } else {
            zzge().zzit().zzg("onUnbind called for intent. action", intent.getAction());
        }
        return true;
    }

    final /* synthetic */ void zza(int i, zzfg com_google_android_gms_internal_measurement_zzfg, Intent intent) {
        if (((zzjg) this.zzabm).callServiceStopSelfResult(i)) {
            com_google_android_gms_internal_measurement_zzfg.zzit().zzg("Local AppMeasurementService processed last upload request. StartId", Integer.valueOf(i));
            zzge().zzit().log("Completed wakeful intent.");
            ((zzjg) this.zzabm).zzb(intent);
        }
    }

    final /* synthetic */ void zza(zzfg com_google_android_gms_internal_measurement_zzfg, JobParameters jobParameters) {
        com_google_android_gms_internal_measurement_zzfg.zzit().log("AppMeasurementJobService processed last upload request.");
        ((zzjg) this.zzabm).zza(jobParameters, false);
    }
}
