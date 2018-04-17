package com.google.firebase.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzgl;
import com.google.android.gms.tasks.Task;

@Keep
public final class FirebaseAnalytics {
    private final zzgl zzacr;

    public static class Event {
    }

    public static class Param {
    }

    public static class UserProperty {
    }

    public FirebaseAnalytics(zzgl com_google_android_gms_internal_measurement_zzgl) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgl);
        this.zzacr = com_google_android_gms_internal_measurement_zzgl;
    }

    @Keep
    public static FirebaseAnalytics getInstance(Context context) {
        return zzgl.zzg(context).zzjp();
    }

    public final Task<String> getAppInstanceId() {
        return this.zzacr.zzfu().getAppInstanceId();
    }

    public final void logEvent(String str, Bundle bundle) {
        this.zzacr.zzjo().logEvent(str, bundle);
    }

    public final void resetAnalyticsData() {
        this.zzacr.zzfu().resetAnalyticsData();
    }

    public final void setAnalyticsCollectionEnabled(boolean z) {
        this.zzacr.zzjo().setMeasurementEnabled(z);
    }

    @Keep
    public final void setCurrentScreen(Activity activity, String str, String str2) {
        this.zzacr.zzfy().setCurrentScreen(activity, str, str2);
    }

    public final void setMinimumSessionDuration(long j) {
        this.zzacr.zzjo().setMinimumSessionDuration(j);
    }

    public final void setSessionTimeoutDuration(long j) {
        this.zzacr.zzjo().setSessionTimeoutDuration(j);
    }

    public final void setUserId(String str) {
        this.zzacr.zzjo().setUserPropertyInternal("app", "_id", str);
    }

    public final void setUserProperty(String str, String str2) {
        this.zzacr.zzjo().setUserProperty(str, str2);
    }
}
