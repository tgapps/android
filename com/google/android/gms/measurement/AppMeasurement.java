package com.google.android.gms.measurement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.measurement.internal.zzbt;
import com.google.android.gms.measurement.internal.zzfh;
import com.google.android.gms.measurement.internal.zzfk;
import java.util.List;
import java.util.Map;

@Deprecated
public class AppMeasurement {
    private final zzbt zzadj;

    public static class ConditionalUserProperty {
        @Keep
        public boolean mActive;
        @Keep
        public String mAppId;
        @Keep
        public long mCreationTimestamp;
        @Keep
        public String mExpiredEventName;
        @Keep
        public Bundle mExpiredEventParams;
        @Keep
        public String mName;
        @Keep
        public String mOrigin;
        @Keep
        public long mTimeToLive;
        @Keep
        public String mTimedOutEventName;
        @Keep
        public Bundle mTimedOutEventParams;
        @Keep
        public String mTriggerEventName;
        @Keep
        public long mTriggerTimeout;
        @Keep
        public String mTriggeredEventName;
        @Keep
        public Bundle mTriggeredEventParams;
        @Keep
        public long mTriggeredTimestamp;
        @Keep
        public Object mValue;

        public ConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
            Preconditions.checkNotNull(conditionalUserProperty);
            this.mAppId = conditionalUserProperty.mAppId;
            this.mOrigin = conditionalUserProperty.mOrigin;
            this.mCreationTimestamp = conditionalUserProperty.mCreationTimestamp;
            this.mName = conditionalUserProperty.mName;
            if (conditionalUserProperty.mValue != null) {
                this.mValue = zzfk.zzf(conditionalUserProperty.mValue);
                if (this.mValue == null) {
                    this.mValue = conditionalUserProperty.mValue;
                }
            }
            this.mActive = conditionalUserProperty.mActive;
            this.mTriggerEventName = conditionalUserProperty.mTriggerEventName;
            this.mTriggerTimeout = conditionalUserProperty.mTriggerTimeout;
            this.mTimedOutEventName = conditionalUserProperty.mTimedOutEventName;
            if (conditionalUserProperty.mTimedOutEventParams != null) {
                this.mTimedOutEventParams = new Bundle(conditionalUserProperty.mTimedOutEventParams);
            }
            this.mTriggeredEventName = conditionalUserProperty.mTriggeredEventName;
            if (conditionalUserProperty.mTriggeredEventParams != null) {
                this.mTriggeredEventParams = new Bundle(conditionalUserProperty.mTriggeredEventParams);
            }
            this.mTriggeredTimestamp = conditionalUserProperty.mTriggeredTimestamp;
            this.mTimeToLive = conditionalUserProperty.mTimeToLive;
            this.mExpiredEventName = conditionalUserProperty.mExpiredEventName;
            if (conditionalUserProperty.mExpiredEventParams != null) {
                this.mExpiredEventParams = new Bundle(conditionalUserProperty.mExpiredEventParams);
            }
        }
    }

    public static final class Event {
        public static final String[] zzadk = new String[]{"app_clear_data", "app_exception", "app_remove", "app_upgrade", "app_install", "app_update", "firebase_campaign", "error", "first_open", "first_visit", "in_app_purchase", "notification_dismiss", "notification_foreground", "notification_open", "notification_receive", "os_update", "session_start", "user_engagement", "ad_exposure", "adunit_exposure", "ad_query", "ad_activeview", "ad_impression", "ad_click", "ad_reward", "screen_view", "ga_extra_parameter"};
        public static final String[] zzadl = new String[]{"_cd", "_ae", "_ui", "_ug", "_in", "_au", "_cmp", "_err", "_f", "_v", "_iap", "_nd", "_nf", "_no", "_nr", "_ou", "_s", "_e", "_xa", "_xu", "_aq", "_aa", "_ai", "_ac", "_ar", "_vs", "_ep"};

        public static String zzal(String str) {
            return zzfk.zza(str, zzadk, zzadl);
        }
    }

    public interface EventInterceptor {
        void interceptEvent(String str, String str2, Bundle bundle, long j);
    }

    public interface OnEventListener {
        void onEvent(String str, String str2, Bundle bundle, long j);
    }

    public static final class Param {
        public static final String[] zzadm = new String[]{"firebase_conversion", "engagement_time_msec", "exposure_time", "ad_event_id", "ad_unit_id", "firebase_error", "firebase_error_value", "firebase_error_length", "firebase_event_origin", "firebase_screen", "firebase_screen_class", "firebase_screen_id", "firebase_previous_screen", "firebase_previous_class", "firebase_previous_id", "message_device_time", "message_id", "message_name", "message_time", "previous_app_version", "previous_os_version", "topic", "update_with_analytics", "previous_first_open_count", "system_app", "system_app_update", "previous_install_count", "ga_event_id", "ga_extra_params_ct", "ga_group_name", "ga_list_length", "ga_index", "ga_event_name", "campaign_info_source", "deferred_analytics_collection", "session_number", "session_id"};
        public static final String[] zzadn = new String[]{"_c", "_et", "_xt", "_aeid", "_ai", "_err", "_ev", "_el", "_o", "_sn", "_sc", "_si", "_pn", "_pc", "_pi", "_ndt", "_nmid", "_nmn", "_nmt", "_pv", "_po", "_nt", "_uwa", "_pfo", "_sys", "_sysu", "_pin", "_eid", "_epc", "_gn", "_ll", "_i", "_en", "_cis", "_dac", "_sno", "_sid"};

        public static String zzal(String str) {
            return zzfk.zza(str, zzadm, zzadn);
        }
    }

    public static final class UserProperty {
        public static final String[] zzado = new String[]{"firebase_last_notification", "first_open_time", "first_visit_time", "last_deep_link_referrer", "user_id", "first_open_after_install", "lifetime_user_engagement", "google_allow_ad_personalization_signals", "session_number", "session_id"};
        public static final String[] zzadp = new String[]{"_ln", "_fot", "_fvt", "_ldl", "_id", "_fi", "_lte", "_ap", "_sno", "_sid"};

        public static String zzal(String str) {
            return zzfk.zza(str, zzado, zzadp);
        }
    }

    @Deprecated
    @Keep
    public static AppMeasurement getInstance(Context context) {
        return zzbt.zza(context, null).zzki();
    }

    public final void zzd(boolean z) {
        this.zzadj.zzge().zzd(z);
    }

    public AppMeasurement(zzbt com_google_android_gms_measurement_internal_zzbt) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzbt);
        this.zzadj = com_google_android_gms_measurement_internal_zzbt;
    }

    @Keep
    public void logEventInternal(String str, String str2, Bundle bundle) {
        this.zzadj.zzge().logEvent(str, str2, bundle);
    }

    public void setUserPropertyInternal(String str, String str2, Object obj) {
        Preconditions.checkNotEmpty(str);
        this.zzadj.zzge().zzb(str, str2, obj, true);
    }

    public Map<String, Object> getUserProperties(boolean z) {
        List<zzfh> zzl = this.zzadj.zzge().zzl(z);
        Map<String, Object> arrayMap = new ArrayMap(zzl.size());
        for (zzfh com_google_android_gms_measurement_internal_zzfh : zzl) {
            arrayMap.put(com_google_android_gms_measurement_internal_zzfh.name, com_google_android_gms_measurement_internal_zzfh.getValue());
        }
        return arrayMap;
    }

    @Keep
    public String getCurrentScreenName() {
        return this.zzadj.zzge().getCurrentScreenName();
    }

    @Keep
    public String getCurrentScreenClass() {
        return this.zzadj.zzge().getCurrentScreenClass();
    }

    @Keep
    public String getAppInstanceId() {
        return this.zzadj.zzge().zzfx();
    }

    @Keep
    public String getGmpAppId() {
        return this.zzadj.zzge().getGmpAppId();
    }

    @Keep
    public long generateEventId() {
        return this.zzadj.zzgm().zzmc();
    }

    @Keep
    public void beginAdUnitExposure(String str) {
        this.zzadj.zzgd().beginAdUnitExposure(str, this.zzadj.zzbx().elapsedRealtime());
    }

    @Keep
    public void endAdUnitExposure(String str) {
        this.zzadj.zzgd().endAdUnitExposure(str, this.zzadj.zzbx().elapsedRealtime());
    }

    @Keep
    public void setConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
        this.zzadj.zzge().setConditionalUserProperty(conditionalUserProperty);
    }

    @Keep
    protected void setConditionalUserPropertyAs(ConditionalUserProperty conditionalUserProperty) {
        this.zzadj.zzge().setConditionalUserPropertyAs(conditionalUserProperty);
    }

    @Keep
    public void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        this.zzadj.zzge().clearConditionalUserProperty(str, str2, bundle);
    }

    @Keep
    protected void clearConditionalUserPropertyAs(String str, String str2, String str3, Bundle bundle) {
        this.zzadj.zzge().clearConditionalUserPropertyAs(str, str2, str3, bundle);
    }

    @Keep
    protected Map<String, Object> getUserProperties(String str, String str2, boolean z) {
        return this.zzadj.zzge().getUserProperties(str, str2, z);
    }

    @Keep
    protected Map<String, Object> getUserPropertiesAs(String str, String str2, String str3, boolean z) {
        return this.zzadj.zzge().getUserPropertiesAs(str, str2, str3, z);
    }

    @Keep
    public List<ConditionalUserProperty> getConditionalUserProperties(String str, String str2) {
        return this.zzadj.zzge().getConditionalUserProperties(str, str2);
    }

    @Keep
    protected List<ConditionalUserProperty> getConditionalUserPropertiesAs(String str, String str2, String str3) {
        return this.zzadj.zzge().getConditionalUserPropertiesAs(str, str2, str3);
    }

    @Keep
    public int getMaxUserProperties(String str) {
        this.zzadj.zzge();
        Preconditions.checkNotEmpty(str);
        return 25;
    }
}
