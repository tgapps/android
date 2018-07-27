package com.google.firebase.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzgm;
import com.google.android.gms.tasks.Task;

@Keep
public final class FirebaseAnalytics {
    private final zzgm zzacw;

    public static class Event {
    }

    public static class Param {
    }

    public static class UserProperty {
    }

    public FirebaseAnalytics(zzgm com_google_android_gms_internal_measurement_zzgm) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgm);
        this.zzacw = com_google_android_gms_internal_measurement_zzgm;
    }

    @Keep
    public static FirebaseAnalytics getInstance(Context context) {
        return zzgm.zza(context, null, null).zzjz();
    }

    public final Task<String> getAppInstanceId() {
        return this.zzacw.zzfv().getAppInstanceId();
    }

    public final void logEvent(String str, Bundle bundle) {
        this.zzacw.zzjy().logEvent(str, bundle);
    }

    public final void resetAnalyticsData() {
        this.zzacw.zzfv().resetAnalyticsData();
    }

    public final void setAnalyticsCollectionEnabled(boolean z) {
        this.zzacw.zzjy().setMeasurementEnabled(z);
    }

    @Keep
    public final void setCurrentScreen(Activity activity, String str, String str2) {
        this.zzacw.zzfz().setCurrentScreen(activity, str, str2);
    }

    public final void setMinimumSessionDuration(long j) {
        this.zzacw.zzjy().setMinimumSessionDuration(j);
    }

    public final void setSessionTimeoutDuration(long j) {
        this.zzacw.zzjy().setSessionTimeoutDuration(j);
    }

    public final void setUserId(String str) {
        this.zzacw.zzjy().setUserPropertyInternal("app", "_id", str);
    }

    public final void setUserProperty(String str, String str2) {
        this.zzacw.zzjy().setUserProperty(str, str2);
    }
}
