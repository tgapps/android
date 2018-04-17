package com.google.android.gms.internal.measurement;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

@TargetApi(14)
final class zzif implements ActivityLifecycleCallbacks {
    private final /* synthetic */ zzhm zzaop;

    private zzif(zzhm com_google_android_gms_internal_measurement_zzhm) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
        try {
            this.zzaop.zzgg().zzir().log("onActivityCreated");
            Intent intent = activity.getIntent();
            if (intent != null) {
                Uri data = intent.getData();
                if (data != null && data.isHierarchical()) {
                    if (bundle == null) {
                        Bundle zza = this.zzaop.zzgc().zza(data);
                        this.zzaop.zzgc();
                        String str = zzjv.zzd(intent) ? "gs" : "auto";
                        if (zza != null) {
                            this.zzaop.zza(str, "_cmp", zza);
                        }
                    }
                    Object queryParameter = data.getQueryParameter("referrer");
                    if (!TextUtils.isEmpty(queryParameter)) {
                        Object obj = (queryParameter.contains("gclid") && (queryParameter.contains("utm_campaign") || queryParameter.contains("utm_source") || queryParameter.contains("utm_medium") || queryParameter.contains("utm_term") || queryParameter.contains("utm_content"))) ? 1 : null;
                        if (obj == null) {
                            this.zzaop.zzgg().zziq().log("Activity created with data 'referrer' param without gclid and at least one utm field");
                            return;
                        }
                        this.zzaop.zzgg().zziq().zzg("Activity created with referrer", queryParameter);
                        if (!TextUtils.isEmpty(queryParameter)) {
                            this.zzaop.zza("auto", "_ldl", queryParameter);
                        }
                    } else {
                        return;
                    }
                }
            }
        } catch (Throwable th) {
            this.zzaop.zzgg().zzil().zzg("Throwable caught in onActivityCreated", th);
        }
        zzih zzfy = this.zzaop.zzfy();
        if (bundle != null) {
            bundle = bundle.getBundle("com.google.firebase.analytics.screen_service");
            if (bundle != null) {
                zzik zze = zzfy.zze(activity);
                zze.zzapb = bundle.getLong(TtmlNode.ATTR_ID);
                zze.zzug = bundle.getString("name");
                zze.zzapa = bundle.getString("referrer_name");
            }
        }
    }

    public final void onActivityDestroyed(Activity activity) {
        this.zzaop.zzfy().onActivityDestroyed(activity);
    }

    public final void onActivityPaused(Activity activity) {
        this.zzaop.zzfy().onActivityPaused(activity);
        zzhj zzge = this.zzaop.zzge();
        zzge.zzgf().zzc(new zzjo(zzge, zzge.zzbt().elapsedRealtime()));
    }

    public final void onActivityResumed(Activity activity) {
        this.zzaop.zzfy().onActivityResumed(activity);
        zzhj zzge = this.zzaop.zzge();
        zzge.zzgf().zzc(new zzjn(zzge, zzge.zzbt().elapsedRealtime()));
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this.zzaop.zzfy().onActivitySaveInstanceState(activity, bundle);
    }

    public final void onActivityStarted(Activity activity) {
    }

    public final void onActivityStopped(Activity activity) {
    }
}
