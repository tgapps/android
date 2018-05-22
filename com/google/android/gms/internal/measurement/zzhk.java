package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.measurement.AppMeasurement.ConditionalUserProperty;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.EventInterceptor;
import com.google.android.gms.measurement.AppMeasurement.OnEventListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;

public final class zzhk extends zzhh {
    protected zzid zzanp;
    private EventInterceptor zzanq;
    private final Set<OnEventListener> zzanr = new CopyOnWriteArraySet();
    private boolean zzans;
    private final AtomicReference<String> zzant = new AtomicReference();
    protected boolean zzanu = true;

    protected zzhk(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final void zza(ConditionalUserProperty conditionalUserProperty) {
        long currentTimeMillis = zzbt().currentTimeMillis();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        Preconditions.checkNotEmpty(conditionalUserProperty.mOrigin);
        Preconditions.checkNotNull(conditionalUserProperty.mValue);
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        String str = conditionalUserProperty.mName;
        Object obj = conditionalUserProperty.mValue;
        if (zzgb().zzcf(str) != 0) {
            zzge().zzim().zzg("Invalid conditional user property name", zzga().zzbl(str));
        } else if (zzgb().zzi(str, obj) != 0) {
            zzge().zzim().zze("Invalid conditional user property value", zzga().zzbl(str), obj);
        } else {
            Object zzj = zzgb().zzj(str, obj);
            if (zzj == null) {
                zzge().zzim().zze("Unable to normalize conditional user property value", zzga().zzbl(str), obj);
                return;
            }
            conditionalUserProperty.mValue = zzj;
            long j = conditionalUserProperty.mTriggerTimeout;
            if (TextUtils.isEmpty(conditionalUserProperty.mTriggerEventName) || (j <= 15552000000L && j >= 1)) {
                j = conditionalUserProperty.mTimeToLive;
                if (j > 15552000000L || j < 1) {
                    zzge().zzim().zze("Invalid conditional user property time to live", zzga().zzbl(str), Long.valueOf(j));
                    return;
                } else {
                    zzgd().zzc(new zzhr(this, conditionalUserProperty));
                    return;
                }
            }
            zzge().zzim().zze("Invalid conditional user property timeout", zzga().zzbl(str), Long.valueOf(j));
        }
    }

    private final void zza(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        Bundle bundle2;
        if (bundle == null) {
            bundle2 = new Bundle();
        } else {
            bundle2 = new Bundle(bundle);
            for (String str4 : bundle2.keySet()) {
                Object obj = bundle2.get(str4);
                if (obj instanceof Bundle) {
                    bundle2.putBundle(str4, new Bundle((Bundle) obj));
                } else if (obj instanceof Parcelable[]) {
                    Parcelable[] parcelableArr = (Parcelable[]) obj;
                    for (r4 = 0; r4 < parcelableArr.length; r4++) {
                        if (parcelableArr[r4] instanceof Bundle) {
                            parcelableArr[r4] = new Bundle((Bundle) parcelableArr[r4]);
                        }
                    }
                } else if (obj instanceof ArrayList) {
                    ArrayList arrayList = (ArrayList) obj;
                    for (r4 = 0; r4 < arrayList.size(); r4++) {
                        Object obj2 = arrayList.get(r4);
                        if (obj2 instanceof Bundle) {
                            arrayList.set(r4, new Bundle((Bundle) obj2));
                        }
                    }
                }
            }
        }
        zzgd().zzc(new zzic(this, str, str2, j, bundle2, z, z2, z3, str3));
    }

    private final void zza(String str, String str2, long j, Object obj) {
        zzgd().zzc(new zzhm(this, str, str2, obj, j));
    }

    private final void zza(String str, String str2, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        zza(str, str2, zzbt().currentTimeMillis(), bundle, true, z2, z3, null);
    }

    private final void zza(String str, String str2, Object obj, long j) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        if (!this.zzacw.isEnabled()) {
            zzge().zzis().log("User property not set since app measurement is disabled");
        } else if (this.zzacw.zzjv()) {
            zzge().zzis().zze("Setting user property (FE)", zzga().zzbj(str2), obj);
            zzfx().zzb(new zzjx(str2, j, obj, str));
        }
    }

    private final void zza(String str, String str2, String str3, Bundle bundle) {
        long currentTimeMillis = zzbt().currentTimeMillis();
        Preconditions.checkNotEmpty(str2);
        ConditionalUserProperty conditionalUserProperty = new ConditionalUserProperty();
        conditionalUserProperty.mAppId = str;
        conditionalUserProperty.mName = str2;
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        if (str3 != null) {
            conditionalUserProperty.mExpiredEventName = str3;
            conditionalUserProperty.mExpiredEventParams = bundle;
        }
        zzgd().zzc(new zzhs(this, conditionalUserProperty));
    }

    private final Map<String, Object> zzb(String str, String str2, String str3, boolean z) {
        if (zzgd().zzjk()) {
            zzge().zzim().log("Cannot get user properties from analytics worker thread");
            return Collections.emptyMap();
        }
        zzgd();
        if (zzgg.isMainThread()) {
            zzge().zzim().log("Cannot get user properties from main thread");
            return Collections.emptyMap();
        }
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            this.zzacw.zzgd().zzc(new zzhu(this, atomicReference, str, str2, str3, z));
            try {
                atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            } catch (InterruptedException e) {
                zzge().zzip().zzg("Interrupted waiting for get user properties", e);
            }
        }
        List<zzjx> list = (List) atomicReference.get();
        if (list == null) {
            zzge().zzip().log("Timed out waiting for get user properties");
            return Collections.emptyMap();
        }
        Map<String, Object> arrayMap = new ArrayMap(list.size());
        for (zzjx com_google_android_gms_internal_measurement_zzjx : list) {
            arrayMap.put(com_google_android_gms_internal_measurement_zzjx.name, com_google_android_gms_internal_measurement_zzjx.getValue());
        }
        return arrayMap;
    }

    private final void zzb(ConditionalUserProperty conditionalUserProperty) {
        zzab();
        zzch();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        Preconditions.checkNotEmpty(conditionalUserProperty.mOrigin);
        Preconditions.checkNotNull(conditionalUserProperty.mValue);
        if (this.zzacw.isEnabled()) {
            zzjx com_google_android_gms_internal_measurement_zzjx = new zzjx(conditionalUserProperty.mName, conditionalUserProperty.mTriggeredTimestamp, conditionalUserProperty.mValue, conditionalUserProperty.mOrigin);
            try {
                zzeu zza = zzgb().zza(conditionalUserProperty.mTriggeredEventName, conditionalUserProperty.mTriggeredEventParams, conditionalUserProperty.mOrigin, 0, true, false);
                zzfx().zzd(new zzed(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, com_google_android_gms_internal_measurement_zzjx, conditionalUserProperty.mCreationTimestamp, false, conditionalUserProperty.mTriggerEventName, zzgb().zza(conditionalUserProperty.mTimedOutEventName, conditionalUserProperty.mTimedOutEventParams, conditionalUserProperty.mOrigin, 0, true, false), conditionalUserProperty.mTriggerTimeout, zza, conditionalUserProperty.mTimeToLive, zzgb().zza(conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, 0, true, false)));
                return;
            } catch (IllegalArgumentException e) {
                return;
            }
        }
        zzge().zzis().log("Conditional property not sent since Firebase Analytics is disabled");
    }

    private final void zzb(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotNull(bundle);
        zzab();
        zzch();
        if (this.zzacw.isEnabled()) {
            if (!this.zzans) {
                this.zzans = true;
                try {
                    try {
                        Class.forName("com.google.android.gms.tagmanager.TagManagerService").getDeclaredMethod("initialize", new Class[]{Context.class}).invoke(null, new Object[]{getContext()});
                    } catch (Exception e) {
                        zzge().zzip().zzg("Failed to invoke Tag Manager's initialize() method", e);
                    }
                } catch (ClassNotFoundException e2) {
                    zzge().zzir().log("Tag Manager is not found and thus will not be used");
                }
            }
            if (z3 && !"_iap".equals(str2)) {
                zzka zzgb = this.zzacw.zzgb();
                int i = !zzgb.zzq("event", str2) ? 2 : !zzgb.zza("event", Event.zzacx, str2) ? 13 : !zzgb.zza("event", 40, str2) ? 2 : 0;
                if (i != 0) {
                    zzge().zzio().zzg("Invalid public event name. Event will not be logged (FE)", zzga().zzbj(str2));
                    this.zzacw.zzgb();
                    this.zzacw.zzgb().zza(i, "_ev", zzka.zza(str2, 40, true), str2 != null ? str2.length() : 0);
                    return;
                }
            }
            zzie zzkc = zzfy().zzkc();
            if (zzkc != null) {
                if (!bundle.containsKey("_sc")) {
                    zzkc.zzaok = true;
                }
            }
            boolean z4 = z && z3;
            zzif.zza(zzkc, bundle, z4);
            boolean equals = "am".equals(str);
            z4 = zzka.zzci(str2);
            if (z && this.zzanq != null && !z4 && !equals) {
                zzge().zzis().zze("Passing event to registered event handler (FE)", zzga().zzbj(str2), zzga().zzb(bundle));
                this.zzanq.interceptEvent(str, str2, bundle, j);
                return;
            } else if (this.zzacw.zzjv()) {
                int zzcd = zzgb().zzcd(str2);
                if (zzcd != 0) {
                    zzge().zzio().zzg("Invalid event name. Event will not be logged (FE)", zzga().zzbj(str2));
                    zzgb();
                    this.zzacw.zzgb().zza(str3, zzcd, "_ev", zzka.zza(str2, 40, true), str2 != null ? str2.length() : 0);
                    return;
                }
                Bundle zza;
                List listOf = CollectionUtils.listOf("_o", "_sn", "_sc", "_si");
                Bundle zza2 = zzgb().zza(str2, bundle, listOf, z3, true);
                zzie com_google_android_gms_internal_measurement_zzie = (zza2 != null && zza2.containsKey("_sc") && zza2.containsKey("_si")) ? new zzie(zza2.getString("_sn"), zza2.getString("_sc"), Long.valueOf(zza2.getLong("_si")).longValue()) : null;
                zzie com_google_android_gms_internal_measurement_zzie2 = com_google_android_gms_internal_measurement_zzie == null ? zzkc : com_google_android_gms_internal_measurement_zzie;
                List arrayList = new ArrayList();
                arrayList.add(zza2);
                long nextLong = zzgb().zzlc().nextLong();
                int i2 = 0;
                String[] strArr = (String[]) zza2.keySet().toArray(new String[bundle.size()]);
                Arrays.sort(strArr);
                int length = strArr.length;
                int i3 = 0;
                while (i3 < length) {
                    int length2;
                    String str4 = strArr[i3];
                    Object obj = zza2.get(str4);
                    zzgb();
                    Bundle[] zze = zzka.zze(obj);
                    if (zze != null) {
                        zza2.putInt(str4, zze.length);
                        for (int i4 = 0; i4 < zze.length; i4++) {
                            Bundle bundle2 = zze[i4];
                            zzif.zza(com_google_android_gms_internal_measurement_zzie2, bundle2, true);
                            zza = zzgb().zza("_ep", bundle2, listOf, z3, false);
                            zza.putString("_en", str2);
                            zza.putLong("_eid", nextLong);
                            zza.putString("_gn", str4);
                            zza.putInt("_ll", zze.length);
                            zza.putInt("_i", i4);
                            arrayList.add(zza);
                        }
                        length2 = zze.length + i2;
                    } else {
                        length2 = i2;
                    }
                    i3++;
                    i2 = length2;
                }
                if (i2 != 0) {
                    zza2.putLong("_eid", nextLong);
                    zza2.putInt("_epc", i2);
                }
                int i5 = 0;
                while (i5 < arrayList.size()) {
                    zza = (Bundle) arrayList.get(i5);
                    String str5 = (i5 != 0 ? 1 : null) != null ? "_ep" : str2;
                    zza.putString("_o", str);
                    Bundle zzd = z2 ? zzgb().zzd(zza) : zza;
                    zzge().zzis().zze("Logging event (FE)", zzga().zzbj(str2), zzga().zzb(zzd));
                    zzfx().zzb(new zzeu(str5, new zzer(zzd), str, j), str3);
                    if (!equals) {
                        for (OnEventListener onEvent : this.zzanr) {
                            onEvent.onEvent(str, str2, new Bundle(zzd), j);
                        }
                    }
                    i5++;
                }
                if (zzfy().zzkc() != null && "_ae".equals(str2)) {
                    zzgc().zzl(true);
                    return;
                }
                return;
            } else {
                return;
            }
        }
        zzge().zzis().log("Event not sent since app measurement is disabled");
    }

    private final void zzc(ConditionalUserProperty conditionalUserProperty) {
        zzab();
        zzch();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        if (this.zzacw.isEnabled()) {
            zzjx com_google_android_gms_internal_measurement_zzjx = new zzjx(conditionalUserProperty.mName, 0, null, null);
            try {
                zzfx().zzd(new zzed(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, com_google_android_gms_internal_measurement_zzjx, conditionalUserProperty.mCreationTimestamp, conditionalUserProperty.mActive, conditionalUserProperty.mTriggerEventName, null, conditionalUserProperty.mTriggerTimeout, null, conditionalUserProperty.mTimeToLive, zzgb().zza(conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, conditionalUserProperty.mCreationTimestamp, true, false)));
                return;
            } catch (IllegalArgumentException e) {
                return;
            }
        }
        zzge().zzis().log("Conditional property not cleared since Firebase Analytics is disabled");
    }

    private final List<ConditionalUserProperty> zzf(String str, String str2, String str3) {
        if (zzgd().zzjk()) {
            zzge().zzim().log("Cannot get conditional user properties from analytics worker thread");
            return Collections.emptyList();
        }
        zzgd();
        if (zzgg.isMainThread()) {
            zzge().zzim().log("Cannot get conditional user properties from main thread");
            return Collections.emptyList();
        }
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            this.zzacw.zzgd().zzc(new zzht(this, atomicReference, str, str2, str3));
            try {
                atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            } catch (InterruptedException e) {
                zzge().zzip().zze("Interrupted waiting for get conditional user properties", str, e);
            }
        }
        List<zzed> list = (List) atomicReference.get();
        if (list == null) {
            zzge().zzip().zzg("Timed out waiting for get conditional user properties", str);
            return Collections.emptyList();
        }
        List<ConditionalUserProperty> arrayList = new ArrayList(list.size());
        for (zzed com_google_android_gms_internal_measurement_zzed : list) {
            ConditionalUserProperty conditionalUserProperty = new ConditionalUserProperty();
            conditionalUserProperty.mAppId = str;
            conditionalUserProperty.mOrigin = str2;
            conditionalUserProperty.mCreationTimestamp = com_google_android_gms_internal_measurement_zzed.creationTimestamp;
            conditionalUserProperty.mName = com_google_android_gms_internal_measurement_zzed.zzaep.name;
            conditionalUserProperty.mValue = com_google_android_gms_internal_measurement_zzed.zzaep.getValue();
            conditionalUserProperty.mActive = com_google_android_gms_internal_measurement_zzed.active;
            conditionalUserProperty.mTriggerEventName = com_google_android_gms_internal_measurement_zzed.triggerEventName;
            if (com_google_android_gms_internal_measurement_zzed.zzaeq != null) {
                conditionalUserProperty.mTimedOutEventName = com_google_android_gms_internal_measurement_zzed.zzaeq.name;
                if (com_google_android_gms_internal_measurement_zzed.zzaeq.zzafq != null) {
                    conditionalUserProperty.mTimedOutEventParams = com_google_android_gms_internal_measurement_zzed.zzaeq.zzafq.zzif();
                }
            }
            conditionalUserProperty.mTriggerTimeout = com_google_android_gms_internal_measurement_zzed.triggerTimeout;
            if (com_google_android_gms_internal_measurement_zzed.zzaer != null) {
                conditionalUserProperty.mTriggeredEventName = com_google_android_gms_internal_measurement_zzed.zzaer.name;
                if (com_google_android_gms_internal_measurement_zzed.zzaer.zzafq != null) {
                    conditionalUserProperty.mTriggeredEventParams = com_google_android_gms_internal_measurement_zzed.zzaer.zzafq.zzif();
                }
            }
            conditionalUserProperty.mTriggeredTimestamp = com_google_android_gms_internal_measurement_zzed.zzaep.zzaqz;
            conditionalUserProperty.mTimeToLive = com_google_android_gms_internal_measurement_zzed.timeToLive;
            if (com_google_android_gms_internal_measurement_zzed.zzaes != null) {
                conditionalUserProperty.mExpiredEventName = com_google_android_gms_internal_measurement_zzed.zzaes.name;
                if (com_google_android_gms_internal_measurement_zzed.zzaes.zzafq != null) {
                    conditionalUserProperty.mExpiredEventParams = com_google_android_gms_internal_measurement_zzed.zzaes.zzafq.zzif();
                }
            }
            arrayList.add(conditionalUserProperty);
        }
        return arrayList;
    }

    private final void zzi(boolean z) {
        zzab();
        zzch();
        zzge().zzis().zzg("Setting app measurement enabled (FE)", Boolean.valueOf(z));
        zzgf().setMeasurementEnabled(z);
        if (!zzgg().zzaz(zzfv().zzah())) {
            zzfx().zzke();
        } else if (this.zzacw.isEnabled() && this.zzanu) {
            zzge().zzis().log("Recording app launch after enabling measurement for the first time (FE)");
            zzkb();
        } else {
            zzfx().zzke();
        }
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        zza(null, str, str2, bundle);
    }

    public final void clearConditionalUserPropertyAs(String str, String str2, String str3, Bundle bundle) {
        Preconditions.checkNotEmpty(str);
        zzfr();
        zza(str, str2, str3, bundle);
    }

    public final Task<String> getAppInstanceId() {
        try {
            String zzja = zzgf().zzja();
            return zzja != null ? Tasks.forResult(zzja) : Tasks.call(zzgd().zzjl(), new zzho(this));
        } catch (Exception e) {
            zzge().zzip().log("Failed to schedule task for getAppInstanceId");
            return Tasks.forException(e);
        }
    }

    public final List<ConditionalUserProperty> getConditionalUserProperties(String str, String str2) {
        return zzf(null, str, str2);
    }

    public final List<ConditionalUserProperty> getConditionalUserPropertiesAs(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzfr();
        return zzf(str, str2, str3);
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final Map<String, Object> getUserProperties(String str, String str2, boolean z) {
        return zzb(null, str, str2, z);
    }

    public final Map<String, Object> getUserPropertiesAs(String str, String str2, String str3, boolean z) {
        Preconditions.checkNotEmpty(str);
        zzfr();
        return zzb(str, str2, str3, z);
    }

    public final void logEvent(String str, String str2, Bundle bundle) {
        boolean z = this.zzanq == null || zzka.zzci(str2);
        zza(str, str2, bundle, true, z, false, null);
    }

    public final void registerOnMeasurementEventListener(OnEventListener onEventListener) {
        zzch();
        Preconditions.checkNotNull(onEventListener);
        if (!this.zzanr.add(onEventListener)) {
            zzge().zzip().log("OnEventListener already registered");
        }
    }

    public final void resetAnalyticsData() {
        zzgd().zzc(new zzhq(this, zzbt().currentTimeMillis()));
    }

    public final void setConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        ConditionalUserProperty conditionalUserProperty2 = new ConditionalUserProperty(conditionalUserProperty);
        if (!TextUtils.isEmpty(conditionalUserProperty2.mAppId)) {
            zzge().zzip().log("Package name should be null when calling setConditionalUserProperty");
        }
        conditionalUserProperty2.mAppId = null;
        zza(conditionalUserProperty2);
    }

    public final void setConditionalUserPropertyAs(ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mAppId);
        zzfr();
        zza(new ConditionalUserProperty(conditionalUserProperty));
    }

    public final void setEventInterceptor(EventInterceptor eventInterceptor) {
        zzab();
        zzch();
        if (!(eventInterceptor == null || eventInterceptor == this.zzanq)) {
            Preconditions.checkState(this.zzanq == null, "EventInterceptor already set.");
        }
        this.zzanq = eventInterceptor;
    }

    public final void setMeasurementEnabled(boolean z) {
        zzch();
        zzgd().zzc(new zzhz(this, z));
    }

    public final void setMinimumSessionDuration(long j) {
        zzgd().zzc(new zzia(this, j));
    }

    public final void setSessionTimeoutDuration(long j) {
        zzgd().zzc(new zzib(this, j));
    }

    public final void setUserProperty(String str, String str2, Object obj) {
        int i = 0;
        Preconditions.checkNotEmpty(str);
        long currentTimeMillis = zzbt().currentTimeMillis();
        int zzcf = zzgb().zzcf(str2);
        String zza;
        if (zzcf != 0) {
            zzgb();
            zza = zzka.zza(str2, 24, true);
            if (str2 != null) {
                i = str2.length();
            }
            this.zzacw.zzgb().zza(zzcf, "_ev", zza, i);
        } else if (obj != null) {
            zzcf = zzgb().zzi(str2, obj);
            if (zzcf != 0) {
                zzgb();
                zza = zzka.zza(str2, 24, true);
                if ((obj instanceof String) || (obj instanceof CharSequence)) {
                    i = String.valueOf(obj).length();
                }
                this.zzacw.zzgb().zza(zzcf, "_ev", zza, i);
                return;
            }
            Object zzj = zzgb().zzj(str2, obj);
            if (zzj != null) {
                zza(str, str2, currentTimeMillis, zzj);
            }
        } else {
            zza(str, str2, currentTimeMillis, null);
        }
    }

    public final void unregisterOnMeasurementEventListener(OnEventListener onEventListener) {
        zzch();
        Preconditions.checkNotNull(onEventListener);
        if (!this.zzanr.remove(onEventListener)) {
            zzge().zzip().log("OnEventListener had not been registered");
        }
    }

    final void zza(String str, String str2, Bundle bundle) {
        zzab();
        boolean z = this.zzanq == null || zzka.zzci(str2);
        zzb(str, str2, zzbt().currentTimeMillis(), bundle, true, z, false, null);
    }

    public final void zza(String str, String str2, Bundle bundle, long j) {
        zza(str, str2, j, bundle, false, true, true, null);
    }

    public final void zza(String str, String str2, Bundle bundle, boolean z) {
        boolean z2 = this.zzanq == null || zzka.zzci(str2);
        zza(str, str2, bundle, true, z2, true, null);
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    final String zzae(long j) {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            zzgd().zzc(new zzhp(this, atomicReference));
            try {
                atomicReference.wait(j);
            } catch (InterruptedException e) {
                zzge().zzip().log("Interrupted waiting for app instance id");
                return null;
            }
        }
        return (String) atomicReference.get();
    }

    final void zzbr(String str) {
        this.zzant.set(str);
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ void zzfs() {
        super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    protected final boolean zzhf() {
        return false;
    }

    public final String zzhm() {
        AtomicReference atomicReference = new AtomicReference();
        return (String) zzgd().zza(atomicReference, 15000, "String test flag value", new zzhv(this, atomicReference));
    }

    public final List<zzjx> zzj(boolean z) {
        zzch();
        zzge().zzis().log("Fetching user attributes (FE)");
        if (zzgd().zzjk()) {
            zzge().zzim().log("Cannot get all user properties from analytics worker thread");
            return Collections.emptyList();
        }
        zzgd();
        if (zzgg.isMainThread()) {
            zzge().zzim().log("Cannot get all user properties from main thread");
            return Collections.emptyList();
        }
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            this.zzacw.zzgd().zzc(new zzhn(this, atomicReference, z));
            try {
                atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            } catch (InterruptedException e) {
                zzge().zzip().zzg("Interrupted waiting for get user properties", e);
            }
        }
        List<zzjx> list = (List) atomicReference.get();
        if (list != null) {
            return list;
        }
        zzge().zzip().log("Timed out waiting for get user properties");
        return Collections.emptyList();
    }

    public final String zzja() {
        return (String) this.zzant.get();
    }

    public final Boolean zzjx() {
        AtomicReference atomicReference = new AtomicReference();
        return (Boolean) zzgd().zza(atomicReference, 15000, "boolean test flag value", new zzhl(this, atomicReference));
    }

    public final Long zzjy() {
        AtomicReference atomicReference = new AtomicReference();
        return (Long) zzgd().zza(atomicReference, 15000, "long test flag value", new zzhw(this, atomicReference));
    }

    public final Integer zzjz() {
        AtomicReference atomicReference = new AtomicReference();
        return (Integer) zzgd().zza(atomicReference, 15000, "int test flag value", new zzhx(this, atomicReference));
    }

    public final Double zzka() {
        AtomicReference atomicReference = new AtomicReference();
        return (Double) zzgd().zza(atomicReference, 15000, "double test flag value", new zzhy(this, atomicReference));
    }

    public final void zzkb() {
        zzab();
        zzch();
        if (this.zzacw.zzjv()) {
            zzfx().zzkb();
            this.zzanu = false;
            String zzjd = zzgf().zzjd();
            if (!TextUtils.isEmpty(zzjd)) {
                zzfw().zzch();
                if (!zzjd.equals(VERSION.RELEASE)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("_po", zzjd);
                    logEvent("auto", "_ou", bundle);
                }
            }
        }
    }
}
