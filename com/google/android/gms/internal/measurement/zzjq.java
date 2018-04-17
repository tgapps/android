package com.google.android.gms.internal.measurement;

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

public final class zzjq extends zzhk {
    private final zzem zzaqs;
    private final AlarmManager zzyd = ((AlarmManager) getContext().getSystemService("alarm"));
    private Integer zzye;

    protected zzjq(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
        this.zzaqs = new zzjr(this, com_google_android_gms_internal_measurement_zzgl, com_google_android_gms_internal_measurement_zzgl);
    }

    private final int getJobId() {
        if (this.zzye == null) {
            String str = "measurement";
            String valueOf = String.valueOf(getContext().getPackageName());
            this.zzye = Integer.valueOf((valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).hashCode());
        }
        return this.zzye.intValue();
    }

    private final PendingIntent zzek() {
        Intent className = new Intent().setClassName(getContext(), "com.google.android.gms.measurement.AppMeasurementReceiver");
        className.setAction("com.google.android.gms.measurement.UPLOAD");
        return PendingIntent.getBroadcast(getContext(), 0, className, 0);
    }

    @TargetApi(24)
    private final void zzks() {
        JobScheduler jobScheduler = (JobScheduler) getContext().getSystemService("jobscheduler");
        zzgg().zzir().zzg("Cancelling job. JobID", Integer.valueOf(getJobId()));
        jobScheduler.cancel(getJobId());
    }

    public final void cancel() {
        zzch();
        this.zzyd.cancel(zzek());
        this.zzaqs.cancel();
        if (VERSION.SDK_INT >= 24) {
            zzks();
        }
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final /* bridge */ /* synthetic */ void zzfq() {
        super.zzfq();
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ zzdx zzfs() {
        return super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzee zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhm zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzil zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzih zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzei zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzfe zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjv zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgf zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzjk zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzfg zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzeh zzgi() {
        return super.zzgi();
    }

    public final void zzh(long j) {
        zzch();
        if (!zzgb.zza(getContext())) {
            zzgg().zziq().log("Receiver not registered/enabled");
        }
        if (!zzjf.zza(getContext(), false)) {
            zzgg().zziq().log("Service not registered/enabled");
        }
        cancel();
        long elapsedRealtime = zzbt().elapsedRealtime() + j;
        if (j < Math.max(0, ((Long) zzew.zzahi.get()).longValue()) && !this.zzaqs.zzef()) {
            zzgg().zzir().log("Scheduling upload with DelayedRunnable");
            this.zzaqs.zzh(j);
        }
        if (VERSION.SDK_INT >= 24) {
            zzgg().zzir().log("Scheduling upload with JobScheduler");
            JobScheduler jobScheduler = (JobScheduler) getContext().getSystemService("jobscheduler");
            Builder builder = new Builder(getJobId(), new ComponentName(getContext(), "com.google.android.gms.measurement.AppMeasurementJobService"));
            builder.setMinimumLatency(j);
            builder.setOverrideDeadline(j << 1);
            PersistableBundle persistableBundle = new PersistableBundle();
            persistableBundle.putString("action", "com.google.android.gms.measurement.UPLOAD");
            builder.setExtras(persistableBundle);
            JobInfo build = builder.build();
            zzgg().zzir().zzg("Scheduling job. JobID", Integer.valueOf(getJobId()));
            jobScheduler.schedule(build);
            return;
        }
        zzgg().zzir().log("Scheduling upload with AlarmManager");
        this.zzyd.setInexactRepeating(2, elapsedRealtime, Math.max(((Long) zzew.zzahd.get()).longValue(), j), zzek());
    }

    protected final boolean zzhh() {
        this.zzyd.cancel(zzek());
        if (VERSION.SDK_INT >= 24) {
            zzks();
        }
        return false;
    }
}
