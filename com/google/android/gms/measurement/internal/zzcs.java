package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.measurement.AppMeasurement.ConditionalUserProperty;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.EventInterceptor;
import com.google.android.gms.measurement.AppMeasurement.OnEventListener;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

public final class zzcs extends zzf {
    protected zzdm zzaqv;
    private EventInterceptor zzaqw;
    private final Set<OnEventListener> zzaqx = new CopyOnWriteArraySet();
    private boolean zzaqy;
    private final AtomicReference<String> zzaqz = new AtomicReference();
    protected boolean zzara = true;

    protected zzcs(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
    }

    protected final boolean zzgt() {
        return false;
    }

    public final void zzd(boolean z) {
        zzcl();
        zzgb();
        zzgn().zzc(new zzdj(this, z));
    }

    private final void zzky() {
        if (zzgq().zze(zzgf().zzal(), zzaf.zzalj)) {
            this.zzadj.zzj(false);
        }
        if (zzgq().zzbd(zzgf().zzal()) && this.zzadj.isEnabled() && this.zzara) {
            zzgo().zzjk().zzbx("Recording app launch after enabling measurement for the first time (FE)");
            zzkz();
            return;
        }
        zzgo().zzjk().zzbx("Updating Scion state (FE)");
        zzgg().zzlc();
    }

    public final void logEvent(String str, String str2, Bundle bundle) {
        logEvent(str, str2, bundle, true, true, zzbx().currentTimeMillis());
    }

    final void zza(String str, String str2, Bundle bundle) {
        zzgb();
        zzaf();
        zza(str, str2, zzbx().currentTimeMillis(), bundle);
    }

    final void zza(String str, String str2, long j, Bundle bundle) {
        zzgb();
        zzaf();
        boolean z = this.zzaqw == null || zzfk.zzcv(str2);
        zza(str, str2, j, bundle, true, z, false, null);
    }

    private final void zza(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotNull(bundle);
        zzaf();
        zzcl();
        if (this.zzadj.isEnabled()) {
            if (!this.zzaqy) {
                this.zzaqy = true;
                try {
                    try {
                        Class.forName("com.google.android.gms.tagmanager.TagManagerService").getDeclaredMethod("initialize", new Class[]{Context.class}).invoke(null, new Object[]{getContext()});
                    } catch (Exception e) {
                        zzgo().zzjg().zzg("Failed to invoke Tag Manager's initialize() method", e);
                    }
                } catch (ClassNotFoundException e2) {
                    zzgo().zzjj().zzbx("Tag Manager is not found and thus will not be used");
                }
            }
            if (z3) {
                zzgr();
                if (!"_iap".equals(str2)) {
                    int i;
                    zzfk zzgm = this.zzadj.zzgm();
                    if (!zzgm.zzr("event", str2)) {
                        i = 2;
                    } else if (!zzgm.zza("event", Event.zzadk, str2)) {
                        i = 13;
                    } else if (zzgm.zza("event", 40, str2)) {
                        i = 0;
                    } else {
                        i = 2;
                    }
                    if (i != 0) {
                        zzgo().zzjf().zzg("Invalid public event name. Event will not be logged (FE)", zzgl().zzbs(str2));
                        this.zzadj.zzgm();
                        this.zzadj.zzgm().zza(i, "_ev", zzfk.zza(str2, 40, true), str2 != null ? str2.length() : 0);
                        return;
                    }
                }
            }
            zzgr();
            zzdn zzla = zzgh().zzla();
            if (zzla != null) {
                if (!bundle.containsKey("_sc")) {
                    zzla.zzarn = true;
                }
            }
            boolean z4 = z && z3;
            zzdo.zza(zzla, bundle, z4);
            boolean equals = "am".equals(str);
            z4 = zzfk.zzcv(str2);
            if (z && this.zzaqw != null && !z4 && !equals) {
                zzgo().zzjk().zze("Passing event to registered event handler (FE)", zzgl().zzbs(str2), zzgl().zzd(bundle));
                this.zzaqw.interceptEvent(str, str2, bundle, j);
                return;
            } else if (this.zzadj.zzkr()) {
                int zzcr = zzgm().zzcr(str2);
                if (zzcr != 0) {
                    zzgo().zzjf().zzg("Invalid event name. Event will not be logged (FE)", zzgl().zzbs(str2));
                    zzgm();
                    this.zzadj.zzgm().zza(str3, zzcr, "_ev", zzfk.zza(str2, 40, true), str2 != null ? str2.length() : 0);
                    return;
                }
                zzdn com_google_android_gms_measurement_internal_zzdn;
                zzdn com_google_android_gms_measurement_internal_zzdn2;
                Bundle zza;
                List listOf = CollectionUtils.listOf("_o", "_sn", "_sc", "_si");
                Bundle zza2 = zzgm().zza(str3, str2, bundle, listOf, z3, true);
                if (zza2 != null && zza2.containsKey("_sc") && zza2.containsKey("_si")) {
                    com_google_android_gms_measurement_internal_zzdn = new zzdn(zza2.getString("_sn"), zza2.getString("_sc"), Long.valueOf(zza2.getLong("_si")).longValue());
                } else {
                    com_google_android_gms_measurement_internal_zzdn = null;
                }
                if (com_google_android_gms_measurement_internal_zzdn == null) {
                    com_google_android_gms_measurement_internal_zzdn2 = zzla;
                } else {
                    com_google_android_gms_measurement_internal_zzdn2 = com_google_android_gms_measurement_internal_zzdn;
                }
                List arrayList = new ArrayList();
                arrayList.add(zza2);
                long nextLong = zzgm().zzmd().nextLong();
                int i2 = 0;
                String[] strArr = (String[]) zza2.keySet().toArray(new String[bundle.size()]);
                Arrays.sort(strArr);
                int length = strArr.length;
                int i3 = 0;
                while (i3 < length) {
                    int length2;
                    String str4 = strArr[i3];
                    Object obj = zza2.get(str4);
                    zzgm();
                    Bundle[] zze = zzfk.zze(obj);
                    if (zze != null) {
                        zza2.putInt(str4, zze.length);
                        for (int i4 = 0; i4 < zze.length; i4++) {
                            Bundle bundle2 = zze[i4];
                            zzdo.zza(com_google_android_gms_measurement_internal_zzdn2, bundle2, true);
                            zza = zzgm().zza(str3, "_ep", bundle2, listOf, z3, false);
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
                    String str5;
                    Bundle zze2;
                    zza = (Bundle) arrayList.get(i5);
                    if ((i5 != 0 ? 1 : null) != null) {
                        str5 = "_ep";
                    } else {
                        str5 = str2;
                    }
                    zza.putString("_o", str);
                    if (z2) {
                        zze2 = zzgm().zze(zza);
                    } else {
                        zze2 = zza;
                    }
                    zzgo().zzjk().zze("Logging event (FE)", zzgl().zzbs(str2), zzgl().zzd(zze2));
                    zzgg().zzb(new zzad(str5, new zzaa(zze2), str, j), str3);
                    if (!equals) {
                        for (OnEventListener onEvent : this.zzaqx) {
                            onEvent.onEvent(str, str2, new Bundle(zze2), j);
                        }
                    }
                    i5++;
                }
                zzgr();
                if (zzgh().zzla() != null && "_ae".equals(str2)) {
                    zzgj().zzn(true);
                    return;
                }
                return;
            } else {
                return;
            }
        }
        zzgo().zzjk().zzbx("Event not sent since app measurement is disabled");
    }

    public final void logEvent(String str, String str2, Bundle bundle, boolean z, boolean z2, long j) {
        String str3;
        Bundle bundle2;
        zzgb();
        if (str == null) {
            str3 = "app";
        } else {
            str3 = str;
        }
        if (bundle == null) {
            bundle2 = new Bundle();
        } else {
            bundle2 = bundle;
        }
        boolean z3 = !z2 || this.zzaqw == null || zzfk.zzcv(str2);
        zzb(str3, str2, j, bundle2, z2, z3, !z, null);
    }

    private final void zzb(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        zzgn().zzc(new zzcu(this, str, str2, j, zzfk.zzf(bundle), z, z2, z3, str3));
    }

    public final void zzb(String str, String str2, Object obj, boolean z) {
        zza(str, str2, obj, z, zzbx().currentTimeMillis());
    }

    public final void zza(String str, String str2, Object obj, boolean z, long j) {
        String str3;
        int i = 6;
        int i2 = 0;
        if (str == null) {
            str3 = "app";
        } else {
            str3 = str;
        }
        if (z || "_ap".equals(str2)) {
            i = zzgm().zzcs(str2);
        } else {
            zzfk zzgm = zzgm();
            if (zzgm.zzr("user property", str2)) {
                if (!zzgm.zza("user property", UserProperty.zzado, str2)) {
                    i = 15;
                } else if (zzgm.zza("user property", 24, str2)) {
                    i = 0;
                }
            }
        }
        if (i != 0) {
            zzgm();
            str3 = zzfk.zza(str2, 24, true);
            if (str2 != null) {
                i2 = str2.length();
            }
            this.zzadj.zzgm().zza(i, "_ev", str3, i2);
        } else if (obj != null) {
            i = zzgm().zzi(str2, obj);
            if (i != 0) {
                zzgm();
                str3 = zzfk.zza(str2, 24, true);
                if ((obj instanceof String) || (obj instanceof CharSequence)) {
                    i2 = String.valueOf(obj).length();
                }
                this.zzadj.zzgm().zza(i, "_ev", str3, i2);
                return;
            }
            Object zzj = zzgm().zzj(str2, obj);
            if (zzj != null) {
                zza(str3, str2, j, zzj);
            }
        } else {
            zza(str3, str2, j, null);
        }
    }

    private final void zza(String str, String str2, long j, Object obj) {
        zzgn().zzc(new zzcv(this, str, str2, obj, j));
    }

    final void zza(String str, String str2, Object obj, long j) {
        Object obj2;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzaf();
        zzgb();
        zzcl();
        if (zzgq().zze(zzgf().zzal(), zzaf.zzalj)) {
            if ("_ap".equals(str2) && !"auto".equals(str)) {
                if ((obj instanceof String) && !TextUtils.isEmpty((String) obj)) {
                    long j2;
                    if ("true".equals(((String) obj).toLowerCase(Locale.ENGLISH)) || "1".equals(obj)) {
                        j2 = 1;
                    } else {
                        j2 = 0;
                    }
                    Long valueOf = Long.valueOf(j2);
                    zzgp().zzans.zzcc(((Long) valueOf).longValue() == 1 ? "true" : "false");
                    obj2 = valueOf;
                } else if (obj == null) {
                    zzgp().zzans.zzcc("unset");
                    zzgn().zzc(new zzcw(this));
                    obj2 = obj;
                }
            }
            obj2 = obj;
        } else {
            if ("_ap".equals(str2)) {
                return;
            }
            obj2 = obj;
        }
        if (!this.zzadj.isEnabled()) {
            zzgo().zzjk().zzbx("User property not set since app measurement is disabled");
        } else if (this.zzadj.zzkr()) {
            zzgo().zzjk().zze("Setting user property (FE)", zzgl().zzbs(str2), obj2);
            zzgg().zzb(new zzfh(str2, j, obj2, str));
        }
    }

    public final List<zzfh> zzl(boolean z) {
        zzgb();
        zzcl();
        zzgo().zzjk().zzbx("Fetching user attributes (FE)");
        if (zzgn().zzkb()) {
            zzgo().zzjd().zzbx("Cannot get all user properties from analytics worker thread");
            return Collections.emptyList();
        } else if (zzk.isMainThread()) {
            zzgo().zzjd().zzbx("Cannot get all user properties from main thread");
            return Collections.emptyList();
        } else {
            AtomicReference atomicReference = new AtomicReference();
            synchronized (atomicReference) {
                this.zzadj.zzgn().zzc(new zzcx(this, atomicReference, z));
                try {
                    atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                } catch (InterruptedException e) {
                    zzgo().zzjg().zzg("Interrupted waiting for get user properties", e);
                }
            }
            List<zzfh> list = (List) atomicReference.get();
            if (list != null) {
                return list;
            }
            zzgo().zzjg().zzbx("Timed out waiting for get user properties");
            return Collections.emptyList();
        }
    }

    public final String zzfx() {
        zzgb();
        return (String) this.zzaqz.get();
    }

    final void zzcm(String str) {
        this.zzaqz.set(str);
    }

    public final void zzkz() {
        zzaf();
        zzgb();
        zzcl();
        if (this.zzadj.zzkr()) {
            zzgg().zzkz();
            this.zzara = false;
            String zzjw = zzgp().zzjw();
            if (!TextUtils.isEmpty(zzjw)) {
                zzgk().zzcl();
                if (!zzjw.equals(VERSION.RELEASE)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("_po", zzjw);
                    logEvent("auto", "_ou", bundle);
                }
            }
        }
    }

    public final void setConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        zzgb();
        ConditionalUserProperty conditionalUserProperty2 = new ConditionalUserProperty(conditionalUserProperty);
        if (!TextUtils.isEmpty(conditionalUserProperty2.mAppId)) {
            zzgo().zzjg().zzbx("Package name should be null when calling setConditionalUserProperty");
        }
        conditionalUserProperty2.mAppId = null;
        zza(conditionalUserProperty2);
    }

    public final void setConditionalUserPropertyAs(ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mAppId);
        zzga();
        zza(new ConditionalUserProperty(conditionalUserProperty));
    }

    private final void zza(ConditionalUserProperty conditionalUserProperty) {
        long currentTimeMillis = zzbx().currentTimeMillis();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        Preconditions.checkNotEmpty(conditionalUserProperty.mOrigin);
        Preconditions.checkNotNull(conditionalUserProperty.mValue);
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        String str = conditionalUserProperty.mName;
        Object obj = conditionalUserProperty.mValue;
        if (zzgm().zzcs(str) != 0) {
            zzgo().zzjd().zzg("Invalid conditional user property name", zzgl().zzbu(str));
        } else if (zzgm().zzi(str, obj) != 0) {
            zzgo().zzjd().zze("Invalid conditional user property value", zzgl().zzbu(str), obj);
        } else {
            Object zzj = zzgm().zzj(str, obj);
            if (zzj == null) {
                zzgo().zzjd().zze("Unable to normalize conditional user property value", zzgl().zzbu(str), obj);
                return;
            }
            conditionalUserProperty.mValue = zzj;
            long j = conditionalUserProperty.mTriggerTimeout;
            if (TextUtils.isEmpty(conditionalUserProperty.mTriggerEventName) || (j <= 15552000000L && j >= 1)) {
                j = conditionalUserProperty.mTimeToLive;
                if (j > 15552000000L || j < 1) {
                    zzgo().zzjd().zze("Invalid conditional user property time to live", zzgl().zzbu(str), Long.valueOf(j));
                    return;
                } else {
                    zzgn().zzc(new zzda(this, conditionalUserProperty));
                    return;
                }
            }
            zzgo().zzjd().zze("Invalid conditional user property timeout", zzgl().zzbu(str), Long.valueOf(j));
        }
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        zzgb();
        zza(null, str, str2, bundle);
    }

    public final void clearConditionalUserPropertyAs(String str, String str2, String str3, Bundle bundle) {
        Preconditions.checkNotEmpty(str);
        zzga();
        zza(str, str2, str3, bundle);
    }

    private final void zza(String str, String str2, String str3, Bundle bundle) {
        long currentTimeMillis = zzbx().currentTimeMillis();
        Preconditions.checkNotEmpty(str2);
        ConditionalUserProperty conditionalUserProperty = new ConditionalUserProperty();
        conditionalUserProperty.mAppId = str;
        conditionalUserProperty.mName = str2;
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        if (str3 != null) {
            conditionalUserProperty.mExpiredEventName = str3;
            conditionalUserProperty.mExpiredEventParams = bundle;
        }
        zzgn().zzc(new zzdb(this, conditionalUserProperty));
    }

    private final void zzb(ConditionalUserProperty conditionalUserProperty) {
        zzaf();
        zzcl();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        Preconditions.checkNotEmpty(conditionalUserProperty.mOrigin);
        Preconditions.checkNotNull(conditionalUserProperty.mValue);
        if (this.zzadj.isEnabled()) {
            zzfh com_google_android_gms_measurement_internal_zzfh = new zzfh(conditionalUserProperty.mName, conditionalUserProperty.mTriggeredTimestamp, conditionalUserProperty.mValue, conditionalUserProperty.mOrigin);
            try {
                zzad zza = zzgm().zza(conditionalUserProperty.mAppId, conditionalUserProperty.mTriggeredEventName, conditionalUserProperty.mTriggeredEventParams, conditionalUserProperty.mOrigin, 0, true, false);
                zzgg().zzd(new zzl(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, com_google_android_gms_measurement_internal_zzfh, conditionalUserProperty.mCreationTimestamp, false, conditionalUserProperty.mTriggerEventName, zzgm().zza(conditionalUserProperty.mAppId, conditionalUserProperty.mTimedOutEventName, conditionalUserProperty.mTimedOutEventParams, conditionalUserProperty.mOrigin, 0, true, false), conditionalUserProperty.mTriggerTimeout, zza, conditionalUserProperty.mTimeToLive, zzgm().zza(conditionalUserProperty.mAppId, conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, 0, true, false)));
                return;
            } catch (IllegalArgumentException e) {
                return;
            }
        }
        zzgo().zzjk().zzbx("Conditional property not sent since collection is disabled");
    }

    private final void zzc(ConditionalUserProperty conditionalUserProperty) {
        zzaf();
        zzcl();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        if (this.zzadj.isEnabled()) {
            zzfh com_google_android_gms_measurement_internal_zzfh = new zzfh(conditionalUserProperty.mName, 0, null, null);
            try {
                zzgg().zzd(new zzl(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, com_google_android_gms_measurement_internal_zzfh, conditionalUserProperty.mCreationTimestamp, conditionalUserProperty.mActive, conditionalUserProperty.mTriggerEventName, null, conditionalUserProperty.mTriggerTimeout, null, conditionalUserProperty.mTimeToLive, zzgm().zza(conditionalUserProperty.mAppId, conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, conditionalUserProperty.mCreationTimestamp, true, false)));
                return;
            } catch (IllegalArgumentException e) {
                return;
            }
        }
        zzgo().zzjk().zzbx("Conditional property not cleared since collection is disabled");
    }

    public final List<ConditionalUserProperty> getConditionalUserProperties(String str, String str2) {
        zzgb();
        return zzf(null, str, str2);
    }

    public final List<ConditionalUserProperty> getConditionalUserPropertiesAs(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzga();
        return zzf(str, str2, str3);
    }

    private final List<ConditionalUserProperty> zzf(String str, String str2, String str3) {
        if (zzgn().zzkb()) {
            zzgo().zzjd().zzbx("Cannot get conditional user properties from analytics worker thread");
            return Collections.emptyList();
        } else if (zzk.isMainThread()) {
            zzgo().zzjd().zzbx("Cannot get conditional user properties from main thread");
            return Collections.emptyList();
        } else {
            AtomicReference atomicReference = new AtomicReference();
            synchronized (atomicReference) {
                this.zzadj.zzgn().zzc(new zzdc(this, atomicReference, str, str2, str3));
                try {
                    atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                } catch (InterruptedException e) {
                    zzgo().zzjg().zze("Interrupted waiting for get conditional user properties", str, e);
                }
            }
            List<zzl> list = (List) atomicReference.get();
            if (list == null) {
                zzgo().zzjg().zzg("Timed out waiting for get conditional user properties", str);
                return Collections.emptyList();
            }
            List<ConditionalUserProperty> arrayList = new ArrayList(list.size());
            for (zzl com_google_android_gms_measurement_internal_zzl : list) {
                ConditionalUserProperty conditionalUserProperty = new ConditionalUserProperty();
                conditionalUserProperty.mAppId = com_google_android_gms_measurement_internal_zzl.packageName;
                conditionalUserProperty.mOrigin = com_google_android_gms_measurement_internal_zzl.origin;
                conditionalUserProperty.mCreationTimestamp = com_google_android_gms_measurement_internal_zzl.creationTimestamp;
                conditionalUserProperty.mName = com_google_android_gms_measurement_internal_zzl.zzahb.name;
                conditionalUserProperty.mValue = com_google_android_gms_measurement_internal_zzl.zzahb.getValue();
                conditionalUserProperty.mActive = com_google_android_gms_measurement_internal_zzl.active;
                conditionalUserProperty.mTriggerEventName = com_google_android_gms_measurement_internal_zzl.triggerEventName;
                if (com_google_android_gms_measurement_internal_zzl.zzahc != null) {
                    conditionalUserProperty.mTimedOutEventName = com_google_android_gms_measurement_internal_zzl.zzahc.name;
                    if (com_google_android_gms_measurement_internal_zzl.zzahc.zzaid != null) {
                        conditionalUserProperty.mTimedOutEventParams = com_google_android_gms_measurement_internal_zzl.zzahc.zzaid.zziv();
                    }
                }
                conditionalUserProperty.mTriggerTimeout = com_google_android_gms_measurement_internal_zzl.triggerTimeout;
                if (com_google_android_gms_measurement_internal_zzl.zzahd != null) {
                    conditionalUserProperty.mTriggeredEventName = com_google_android_gms_measurement_internal_zzl.zzahd.name;
                    if (com_google_android_gms_measurement_internal_zzl.zzahd.zzaid != null) {
                        conditionalUserProperty.mTriggeredEventParams = com_google_android_gms_measurement_internal_zzl.zzahd.zzaid.zziv();
                    }
                }
                conditionalUserProperty.mTriggeredTimestamp = com_google_android_gms_measurement_internal_zzl.zzahb.zzaue;
                conditionalUserProperty.mTimeToLive = com_google_android_gms_measurement_internal_zzl.timeToLive;
                if (com_google_android_gms_measurement_internal_zzl.zzahe != null) {
                    conditionalUserProperty.mExpiredEventName = com_google_android_gms_measurement_internal_zzl.zzahe.name;
                    if (com_google_android_gms_measurement_internal_zzl.zzahe.zzaid != null) {
                        conditionalUserProperty.mExpiredEventParams = com_google_android_gms_measurement_internal_zzl.zzahe.zzaid.zziv();
                    }
                }
                arrayList.add(conditionalUserProperty);
            }
            return arrayList;
        }
    }

    public final Map<String, Object> getUserProperties(String str, String str2, boolean z) {
        zzgb();
        return zzb(null, str, str2, z);
    }

    public final Map<String, Object> getUserPropertiesAs(String str, String str2, String str3, boolean z) {
        Preconditions.checkNotEmpty(str);
        zzga();
        return zzb(str, str2, str3, z);
    }

    private final Map<String, Object> zzb(String str, String str2, String str3, boolean z) {
        if (zzgn().zzkb()) {
            zzgo().zzjd().zzbx("Cannot get user properties from analytics worker thread");
            return Collections.emptyMap();
        } else if (zzk.isMainThread()) {
            zzgo().zzjd().zzbx("Cannot get user properties from main thread");
            return Collections.emptyMap();
        } else {
            AtomicReference atomicReference = new AtomicReference();
            synchronized (atomicReference) {
                this.zzadj.zzgn().zzc(new zzde(this, atomicReference, str, str2, str3, z));
                try {
                    atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                } catch (InterruptedException e) {
                    zzgo().zzjg().zzg("Interrupted waiting for get user properties", e);
                }
            }
            List<zzfh> list = (List) atomicReference.get();
            if (list == null) {
                zzgo().zzjg().zzbx("Timed out waiting for get user properties");
                return Collections.emptyMap();
            }
            Map<String, Object> arrayMap = new ArrayMap(list.size());
            for (zzfh com_google_android_gms_measurement_internal_zzfh : list) {
                arrayMap.put(com_google_android_gms_measurement_internal_zzfh.name, com_google_android_gms_measurement_internal_zzfh.getValue());
            }
            return arrayMap;
        }
    }

    public final String getCurrentScreenName() {
        zzdn zzlb = this.zzadj.zzgh().zzlb();
        if (zzlb != null) {
            return zzlb.zzuw;
        }
        return null;
    }

    public final String getCurrentScreenClass() {
        zzdn zzlb = this.zzadj.zzgh().zzlb();
        if (zzlb != null) {
            return zzlb.zzarl;
        }
        return null;
    }

    public final String getGmpAppId() {
        if (this.zzadj.zzkk() != null) {
            return this.zzadj.zzkk();
        }
        try {
            return GoogleServices.getGoogleAppId();
        } catch (IllegalStateException e) {
            this.zzadj.zzgo().zzjd().zzg("getGoogleAppId failed with exception", e);
            return null;
        }
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

    public final /* bridge */ /* synthetic */ zza zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzcs zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzaj zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzdr zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzdo zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzal zzgi() {
        return super.zzgi();
    }

    public final /* bridge */ /* synthetic */ zzeq zzgj() {
        return super.zzgj();
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
