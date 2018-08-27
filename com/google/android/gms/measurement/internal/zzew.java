package com.google.android.gms.measurement.internal;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.PersistableBundle;
import com.google.android.gms.common.util.Clock;

public final class zzew extends zzez {
    private final zzv zzata;
    private final AlarmManager zzyt = ((AlarmManager) getContext().getSystemService("alarm"));
    private Integer zzyu;

    protected zzew(zzfa com_google_android_gms_measurement_internal_zzfa) {
        super(com_google_android_gms_measurement_internal_zzfa);
        this.zzata = new zzex(this, com_google_android_gms_measurement_internal_zzfa.zzmb(), com_google_android_gms_measurement_internal_zzfa);
    }

    protected final boolean zzgt() {
        this.zzyt.cancel(zzeo());
        if (VERSION.SDK_INT >= 24) {
            zzlm();
        }
        return false;
    }

    @TargetApi(24)
    private final void zzlm() {
        JobScheduler jobScheduler = (JobScheduler) getContext().getSystemService("jobscheduler");
        zzgo().zzjl().zzg("Cancelling job. JobID", Integer.valueOf(getJobId()));
        jobScheduler.cancel(getJobId());
    }

    public final void zzh(long j) {
        zzcl();
        zzgr();
        if (!zzbj.zza(getContext())) {
            zzgo().zzjk().zzbx("Receiver not registered/enabled");
        }
        zzgr();
        if (!zzfk.zza(getContext(), false)) {
            zzgo().zzjk().zzbx("Service not registered/enabled");
        }
        cancel();
        long elapsedRealtime = zzbx().elapsedRealtime() + j;
        if (j < Math.max(0, ((Long) zzaf.zzaka.get()).longValue()) && !this.zzata.zzej()) {
            zzgo().zzjl().zzbx("Scheduling upload with DelayedRunnable");
            this.zzata.zzh(j);
        }
        zzgr();
        if (VERSION.SDK_INT >= 24) {
            zzgo().zzjl().zzbx("Scheduling upload with JobScheduler");
            JobScheduler jobScheduler = (JobScheduler) getContext().getSystemService("jobscheduler");
            Builder builder = new Builder(getJobId(), new ComponentName(getContext(), "com.google.android.gms.measurement.AppMeasurementJobService"));
            builder.setMinimumLatency(j);
            builder.setOverrideDeadline(j << 1);
            PersistableBundle persistableBundle = new PersistableBundle();
            persistableBundle.putString("action", "com.google.android.gms.measurement.UPLOAD");
            builder.setExtras(persistableBundle);
            JobInfo build = builder.build();
            zzgo().zzjl().zzg("Scheduling job. JobID", Integer.valueOf(getJobId()));
            jobScheduler.schedule(build);
            return;
        }
        zzgo().zzjl().zzbx("Scheduling upload with AlarmManager");
        this.zzyt.setInexactRepeating(2, elapsedRealtime, Math.max(((Long) zzaf.zzajv.get()).longValue(), j), zzeo());
    }

    private final int getJobId() {
        if (this.zzyu == null) {
            String str = "measurement";
            String valueOf = String.valueOf(getContext().getPackageName());
            this.zzyu = Integer.valueOf((valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).hashCode());
        }
        return this.zzyu.intValue();
    }

    public final void cancel() {
        zzcl();
        this.zzyt.cancel(zzeo());
        this.zzata.cancel();
        if (VERSION.SDK_INT >= 24) {
            zzlm();
        }
    }

    private final PendingIntent zzeo() {
        Intent className = new Intent().setClassName(getContext(), "com.google.android.gms.measurement.AppMeasurementReceiver");
        className.setAction("com.google.android.gms.measurement.UPLOAD");
        return PendingIntent.getBroadcast(getContext(), 0, className, 0);
    }

    public final /* bridge */ /* synthetic */ zzfg zzjo() {
        return super.zzjo();
    }

    public final /* bridge */ /* synthetic */ zzj zzjp() {
        return super.zzjp();
    }

    public final /* bridge */ /* synthetic */ zzq zzjq() {
        return super.zzjq();
    }

    public final /* bridge */ /* synthetic */ void zzga() {
        super.zzga();
    }

    public final /* bridge */ /* synthetic */ void zzgb() {
        super.zzgb();
    }

    public final /* bridge */ /* synthetic */ void zzgc() {
        super.zzgc();
    }

    public final /* bridge */ /* synthetic */ void zzaf() {
        super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zzx zzgk() {
        return super.zzgk();
    }

    public final /* bridge */ /* synthetic */ Clock zzbx() {
        return super.zzbx();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzan zzgl() {
        return super.zzgl();
    }

    public final /* bridge */ /* synthetic */ zzfk zzgm() {
        return super.zzgm();
    }

    public final /* bridge */ /* synthetic */ zzbo zzgn() {
        return super.zzgn();
    }

    public final /* bridge */ /* synthetic */ zzap zzgo() {
        return super.zzgo();
    }

    public final /* bridge */ /* synthetic */ zzba zzgp() {
        return super.zzgp();
    }

    public final /* bridge */ /* synthetic */ zzn zzgq() {
        return super.zzgq();
    }

    public final /* bridge */ /* synthetic */ zzk zzgr() {
        return super.zzgr();
    }
}
