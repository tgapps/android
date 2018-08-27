package com.google.android.gms.measurement.internal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

@TargetApi(14)
final class zzdm implements ActivityLifecycleCallbacks {
    private final /* synthetic */ zzcs zzarc;

    private zzdm(zzcs com_google_android_gms_measurement_internal_zzcs) {
        this.zzarc = com_google_android_gms_measurement_internal_zzcs;
    }

    public final void onActivityStarted(Activity activity) {
    }

    public final void onActivityStopped(Activity activity) {
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
        Object obj = 1;
        try {
            this.zzarc.zzgo().zzjl().zzbx("onActivityCreated");
            Intent intent = activity.getIntent();
            if (intent != null) {
                Uri data = intent.getData();
                if (data != null && data.isHierarchical()) {
                    if (bundle == null) {
                        String str;
                        Bundle zza = this.zzarc.zzgm().zza(data);
                        this.zzarc.zzgm();
                        if (zzfk.zzd(intent)) {
                            str = "gs";
                        } else {
                            str = "auto";
                        }
                        if (zza != null) {
                            this.zzarc.logEvent(str, "_cmp", zza);
                        }
                    }
                    Object queryParameter = data.getQueryParameter("referrer");
                    if (!TextUtils.isEmpty(queryParameter)) {
                        if (!(queryParameter.contains("gclid") && (queryParameter.contains("utm_campaign") || queryParameter.contains("utm_source") || queryParameter.contains("utm_medium") || queryParameter.contains("utm_term") || queryParameter.contains("utm_content")))) {
                            obj = null;
                        }
                        if (obj == null) {
                            this.zzarc.zzgo().zzjk().zzbx("Activity created with data 'referrer' param without gclid and at least one utm field");
                            return;
                        }
                        this.zzarc.zzgo().zzjk().zzg("Activity created with referrer", queryParameter);
                        if (!TextUtils.isEmpty(queryParameter)) {
                            this.zzarc.zzb("auto", "_ldl", queryParameter, true);
                        }
                    } else {
                        return;
                    }
                }
            }
        } catch (Exception e) {
            this.zzarc.zzgo().zzjd().zzg("Throwable caught in onActivityCreated", e);
        }
        this.zzarc.zzgh().onActivityCreated(activity, bundle);
    }

    public final void onActivityDestroyed(Activity activity) {
        this.zzarc.zzgh().onActivityDestroyed(activity);
    }

    public final void onActivityPaused(Activity activity) {
        this.zzarc.zzgh().onActivityPaused(activity);
        zzco zzgj = this.zzarc.zzgj();
        zzgj.zzgn().zzc(new zzeu(zzgj, zzgj.zzbx().elapsedRealtime()));
    }

    public final void onActivityResumed(Activity activity) {
        this.zzarc.zzgh().onActivityResumed(activity);
        zzco zzgj = this.zzarc.zzgj();
        zzgj.zzgn().zzc(new zzet(zzgj, zzgj.zzbx().elapsedRealtime()));
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this.zzarc.zzgh().onActivitySaveInstanceState(activity, bundle);
    }
}
