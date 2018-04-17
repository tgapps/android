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

public final class zzhm extends zzhk {
    protected zzif zzaoi;
    private EventInterceptor zzaoj;
    private final Set<OnEventListener> zzaok = new CopyOnWriteArraySet();
    private boolean zzaol;
    private final AtomicReference<String> zzaom = new AtomicReference();
    protected boolean zzaon = true;

    protected zzhm(zzgl com_google_android_gms_internal_measurement_zzgl) {
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
        if (zzgc().zzby(str) != 0) {
            zzgg().zzil().zzg("Invalid conditional user property name", zzgb().zzbg(str));
        } else if (zzgc().zzi(str, obj) != 0) {
            zzgg().zzil().zze("Invalid conditional user property value", zzgb().zzbg(str), obj);
        } else {
            Object zzj = zzgc().zzj(str, obj);
            if (zzj == null) {
                zzgg().zzil().zze("Unable to normalize conditional user property value", zzgb().zzbg(str), obj);
                return;
            }
            conditionalUserProperty.mValue = zzj;
            long j = conditionalUserProperty.mTriggerTimeout;
            if (TextUtils.isEmpty(conditionalUserProperty.mTriggerEventName) || (j <= 15552000000L && j >= 1)) {
                j = conditionalUserProperty.mTimeToLive;
                if (j <= 15552000000L) {
                    if (j >= 1) {
                        zzgf().zzc(new zzht(this, conditionalUserProperty));
                        return;
                    }
                }
                zzgg().zzil().zze("Invalid conditional user property time to live", zzgb().zzbg(str), Long.valueOf(j));
                return;
            }
            zzgg().zzil().zze("Invalid conditional user property timeout", zzgb().zzbg(str), Long.valueOf(j));
        }
    }

    private final void zza(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        Bundle bundle2;
        Bundle bundle3 = bundle;
        if (bundle3 == null) {
            bundle2 = new Bundle();
        } else {
            Bundle bundle4 = new Bundle(bundle3);
            for (String str4 : bundle4.keySet()) {
                Object obj = bundle4.get(str4);
                if (obj instanceof Bundle) {
                    bundle4.putBundle(str4, new Bundle((Bundle) obj));
                } else {
                    int i = 0;
                    if (obj instanceof Parcelable[]) {
                        Parcelable[] parcelableArr = (Parcelable[]) obj;
                        while (i < parcelableArr.length) {
                            if (parcelableArr[i] instanceof Bundle) {
                                parcelableArr[i] = new Bundle((Bundle) parcelableArr[i]);
                            }
                            i++;
                        }
                    } else if (obj instanceof ArrayList) {
                        ArrayList arrayList = (ArrayList) obj;
                        while (i < arrayList.size()) {
                            Object obj2 = arrayList.get(i);
                            if (obj2 instanceof Bundle) {
                                arrayList.set(i, new Bundle((Bundle) obj2));
                            }
                            i++;
                        }
                    }
                }
            }
            bundle2 = bundle4;
        }
        zzgf().zzc(new zzie(this, str, str2, j, bundle2, z, z2, z3, str3));
    }

    private final void zza(String str, String str2, long j, Object obj) {
        zzgf().zzc(new zzho(this, str, str2, obj, j));
    }

    private final void zza(String str, String str2, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        zza(str, str2, zzbt().currentTimeMillis(), bundle, true, z2, z3, null);
    }

    private final void zza(String str, String str2, Object obj, long j) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        if (!this.zzacr.isEnabled()) {
            zzgg().zziq().log("User property not set since app measurement is disabled");
        } else if (this.zzacr.zzjk()) {
            zzgg().zziq().zze("Setting user property (FE)", zzgb().zzbe(str2), obj);
            zzfx().zzb(new zzjs(str2, j, obj, str));
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
        zzgf().zzc(new zzhu(this, conditionalUserProperty));
    }

    private final Map<String, Object> zzb(String str, String str2, String str3, boolean z) {
        zzfi zzil;
        if (zzgf().zzjg()) {
            zzil = zzgg().zzil();
            str2 = "Cannot get user properties from analytics worker thread";
        } else {
            zzgf();
            if (zzgg.isMainThread()) {
                zzil = zzgg().zzil();
                str2 = "Cannot get user properties from main thread";
            } else {
                AtomicReference atomicReference = new AtomicReference();
                synchronized (atomicReference) {
                    this.zzacr.zzgf().zzc(new zzhw(this, atomicReference, str, str2, str3, z));
                    try {
                        atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                    } catch (InterruptedException e) {
                        zzgg().zzin().zzg("Interrupted waiting for get user properties", e);
                    }
                }
                List<zzjs> list = (List) atomicReference.get();
                if (list == null) {
                    zzil = zzgg().zzin();
                    str2 = "Timed out waiting for get user properties";
                } else {
                    Map<String, Object> arrayMap = new ArrayMap(list.size());
                    for (zzjs com_google_android_gms_internal_measurement_zzjs : list) {
                        arrayMap.put(com_google_android_gms_internal_measurement_zzjs.name, com_google_android_gms_internal_measurement_zzjs.getValue());
                    }
                    return arrayMap;
                }
            }
        }
        zzil.log(str2);
        return Collections.emptyMap();
    }

    private final void zzb(ConditionalUserProperty conditionalUserProperty) {
        ConditionalUserProperty conditionalUserProperty2 = conditionalUserProperty;
        zzab();
        zzch();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty2.mName);
        Preconditions.checkNotEmpty(conditionalUserProperty2.mOrigin);
        Preconditions.checkNotNull(conditionalUserProperty2.mValue);
        if (this.zzacr.isEnabled()) {
            zzjs com_google_android_gms_internal_measurement_zzjs = new zzjs(conditionalUserProperty2.mName, conditionalUserProperty2.mTriggeredTimestamp, conditionalUserProperty2.mValue, conditionalUserProperty2.mOrigin);
            try {
                zzeu zza = zzgc().zza(conditionalUserProperty2.mTriggeredEventName, conditionalUserProperty2.mTriggeredEventParams, conditionalUserProperty2.mOrigin, 0, true, false);
                zzeu zza2 = zzgc().zza(conditionalUserProperty2.mTimedOutEventName, conditionalUserProperty2.mTimedOutEventParams, conditionalUserProperty2.mOrigin, 0, true, false);
                zzeu zza3 = zzgc().zza(conditionalUserProperty2.mExpiredEventName, conditionalUserProperty2.mExpiredEventParams, conditionalUserProperty2.mOrigin, 0, true, false);
                String str = conditionalUserProperty2.mAppId;
                String str2 = conditionalUserProperty2.mOrigin;
                long j = conditionalUserProperty2.mCreationTimestamp;
                String str3 = conditionalUserProperty2.mTriggerEventName;
                long j2 = conditionalUserProperty2.mTriggerTimeout;
                long j3 = conditionalUserProperty2.mTimeToLive;
                zzef com_google_android_gms_internal_measurement_zzef = r4;
                zzef com_google_android_gms_internal_measurement_zzef2 = new zzef(str, str2, com_google_android_gms_internal_measurement_zzjs, j, false, str3, zza2, j2, zza, j3, zza3);
                zzfx().zzf(com_google_android_gms_internal_measurement_zzef);
                return;
            } catch (IllegalArgumentException e) {
                return;
            }
        }
        zzgg().zziq().log("Conditional property not sent since Firebase Analytics is disabled");
    }

    private final void zzb(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        String str4 = str;
        String str5 = str2;
        Bundle bundle2 = bundle;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotNull(bundle);
        zzab();
        zzch();
        if (this.zzacr.isEnabled()) {
            String zza;
            zzig com_google_android_gms_internal_measurement_zzig = null;
            int i = 0;
            if (!r1.zzaol) {
                r1.zzaol = true;
                try {
                    try {
                        Class.forName("com.google.android.gms.tagmanager.TagManagerService").getDeclaredMethod("initialize", new Class[]{Context.class}).invoke(null, new Object[]{getContext()});
                    } catch (Exception e) {
                        zzgg().zzin().zzg("Failed to invoke Tag Manager's initialize() method", e);
                    }
                } catch (ClassNotFoundException e2) {
                    zzgg().zzip().log("Tag Manager is not found and thus will not be used");
                }
            }
            if (z3 && !"_iap".equals(str5)) {
                int i2;
                zzjv zzgc = r1.zzacr.zzgc();
                if (zzgc.zzq("event", str5)) {
                    if (!zzgc.zza("event", Event.zzacs, str5)) {
                        i2 = 13;
                    } else if (zzgc.zza("event", 40, str5)) {
                        i2 = 0;
                    }
                    if (i2 != 0) {
                        r1.zzacr.zzgc();
                        zza = zzjv.zza(str5, 40, true);
                        if (str5 != null) {
                            i = str2.length();
                        }
                        r1.zzacr.zzgc().zza(i2, "_ev", zza, i);
                        return;
                    }
                }
                i2 = 2;
                if (i2 != 0) {
                    r1.zzacr.zzgc();
                    zza = zzjv.zza(str5, 40, true);
                    if (str5 != null) {
                        i = str2.length();
                    }
                    r1.zzacr.zzgc().zza(i2, "_ev", zza, i);
                    return;
                }
            }
            zzig zzkk = zzfy().zzkk();
            if (!(zzkk == null || bundle2.containsKey("_sc"))) {
                zzkk.zzapq = true;
            }
            boolean z4 = z && z3;
            zzih.zza(zzkk, bundle2, z4);
            boolean equals = "am".equals(str4);
            z4 = zzjv.zzcb(str2);
            if (z && r1.zzaoj != null && !z4 && !equals) {
                zzgg().zziq().zze("Passing event to registered event handler (FE)", zzgb().zzbe(str5), zzgb().zzb(bundle2));
                r1.zzaoj.interceptEvent(str4, str5, bundle2, j);
                return;
            } else if (r1.zzacr.zzjk()) {
                int zzbw = zzgc().zzbw(str5);
                if (zzbw != 0) {
                    zzgc();
                    zza = zzjv.zza(str5, 40, true);
                    if (str5 != null) {
                        i = str2.length();
                    }
                    r1.zzacr.zzgc().zza(str3, zzbw, "_ev", zza, i);
                    return;
                }
                Bundle zza2;
                long j2;
                List listOf = CollectionUtils.listOf("_o", "_sn", "_sc", "_si");
                Bundle zza3 = zzgc().zza(str5, bundle2, listOf, z3, true);
                if (zza3 != null && zza3.containsKey("_sc")) {
                    if (zza3.containsKey("_si")) {
                        com_google_android_gms_internal_measurement_zzig = new zzik(zza3.getString("_sn"), zza3.getString("_sc"), Long.valueOf(zza3.getLong("_si")).longValue());
                    }
                }
                if (com_google_android_gms_internal_measurement_zzig == null) {
                    com_google_android_gms_internal_measurement_zzig = zzkk;
                }
                List arrayList = new ArrayList();
                arrayList.add(zza3);
                long nextLong = zzgc().zzku().nextLong();
                String[] strArr = (String[]) zza3.keySet().toArray(new String[bundle.size()]);
                Arrays.sort(strArr);
                int length = strArr.length;
                int i3 = 0;
                int i4 = i3;
                while (i4 < length) {
                    int i5;
                    int i6;
                    String[] strArr2;
                    String str6 = strArr[i4];
                    Object obj = zza3.get(str6);
                    zzgc();
                    Bundle[] zze = zzjv.zze(obj);
                    if (zze != null) {
                        int i7;
                        i5 = i3;
                        zza3.putInt(str6, zze.length);
                        int i8 = i4;
                        i3 = 0;
                        while (i3 < zze.length) {
                            Bundle bundle3 = zze[i3];
                            i6 = i3;
                            zzih.zza(com_google_android_gms_internal_measurement_zzig, bundle3, true);
                            i7 = i5;
                            int i9 = i6;
                            i5 = i8;
                            i6 = length;
                            long j3 = nextLong;
                            strArr2 = strArr;
                            bundle2 = zza3;
                            zza2 = zzgc().zza("_ep", bundle3, listOf, z3, false);
                            zza2.putString("_en", str5);
                            j2 = j3;
                            zza2.putLong("_eid", j2);
                            zza2.putString("_gn", str6);
                            zza2.putInt("_ll", zze.length);
                            int i10 = i9;
                            zza2.putInt("_i", i10);
                            arrayList.add(zza2);
                            i3 = i10 + 1;
                            nextLong = j2;
                            zza3 = bundle2;
                            i8 = i5;
                            length = i6;
                            i5 = i7;
                            strArr = strArr2;
                        }
                        i6 = length;
                        j2 = nextLong;
                        strArr2 = strArr;
                        i7 = i5;
                        i5 = i8;
                        bundle2 = zza3;
                        i3 = zze.length + i7;
                    } else {
                        i = i3;
                        i5 = i4;
                        i6 = length;
                        j2 = nextLong;
                        strArr2 = strArr;
                        bundle2 = zza3;
                    }
                    i4 = i5 + 1;
                    nextLong = j2;
                    zza3 = bundle2;
                    length = i6;
                    strArr = strArr2;
                }
                i = i3;
                j2 = nextLong;
                bundle2 = zza3;
                if (i != 0) {
                    bundle2.putLong("_eid", j2);
                    bundle2.putInt("_epc", i);
                }
                int i11 = 0;
                while (i11 < arrayList.size()) {
                    zza2 = (Bundle) arrayList.get(i11);
                    String str7 = (i11 != 0 ? 1 : null) != null ? "_ep" : str5;
                    zza2.putString("_o", str4);
                    if (z2) {
                        zza2 = zzgc().zzd(zza2);
                    }
                    Bundle bundle4 = zza2;
                    zzgg().zziq().zze("Logging event (FE)", zzgb().zzbe(str5), zzgb().zzb(bundle4));
                    zzfx().zzc(new zzeu(str7, new zzer(bundle4), str4, j), str3);
                    if (!equals) {
                        for (OnEventListener onEvent : r1.zzaok) {
                            onEvent.onEvent(str4, str5, new Bundle(bundle4), j);
                        }
                    }
                    i11++;
                }
                if (zzfy().zzkk() != null && "_ae".equals(str5)) {
                    zzge().zzm(true);
                }
                return;
            } else {
                return;
            }
        }
        zzgg().zziq().log("Event not sent since app measurement is disabled");
    }

    private final void zzc(ConditionalUserProperty conditionalUserProperty) {
        ConditionalUserProperty conditionalUserProperty2 = conditionalUserProperty;
        zzab();
        zzch();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty2.mName);
        if (this.zzacr.isEnabled()) {
            zzjs com_google_android_gms_internal_measurement_zzjs = new zzjs(conditionalUserProperty2.mName, 0, null, null);
            try {
                zzeu zza = zzgc().zza(conditionalUserProperty2.mExpiredEventName, conditionalUserProperty2.mExpiredEventParams, conditionalUserProperty2.mOrigin, conditionalUserProperty2.mCreationTimestamp, true, false);
                String str = conditionalUserProperty2.mAppId;
                String str2 = conditionalUserProperty2.mOrigin;
                long j = conditionalUserProperty2.mCreationTimestamp;
                boolean z = conditionalUserProperty2.mActive;
                String str3 = conditionalUserProperty2.mTriggerEventName;
                long j2 = conditionalUserProperty2.mTriggerTimeout;
                long j3 = conditionalUserProperty2.mTimeToLive;
                zzef com_google_android_gms_internal_measurement_zzef = r4;
                zzef com_google_android_gms_internal_measurement_zzef2 = new zzef(str, str2, com_google_android_gms_internal_measurement_zzjs, j, z, str3, null, j2, null, j3, zza);
                zzfx().zzf(com_google_android_gms_internal_measurement_zzef);
                return;
            } catch (IllegalArgumentException e) {
                return;
            }
        }
        zzgg().zziq().log("Conditional property not cleared since Firebase Analytics is disabled");
    }

    private final List<ConditionalUserProperty> zzf(String str, String str2, String str3) {
        zzfi zzil;
        if (zzgf().zzjg()) {
            zzil = zzgg().zzil();
            str2 = "Cannot get conditional user properties from analytics worker thread";
        } else {
            zzgf();
            if (zzgg.isMainThread()) {
                zzil = zzgg().zzil();
                str2 = "Cannot get conditional user properties from main thread";
            } else {
                AtomicReference atomicReference = new AtomicReference();
                synchronized (atomicReference) {
                    this.zzacr.zzgf().zzc(new zzhv(this, atomicReference, str, str2, str3));
                    try {
                        atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                    } catch (InterruptedException e) {
                        zzgg().zzin().zze("Interrupted waiting for get conditional user properties", str, e);
                    }
                }
                List<zzef> list = (List) atomicReference.get();
                if (list == null) {
                    zzgg().zzin().zzg("Timed out waiting for get conditional user properties", str);
                    return Collections.emptyList();
                }
                List<ConditionalUserProperty> arrayList = new ArrayList(list.size());
                for (zzef com_google_android_gms_internal_measurement_zzef : list) {
                    ConditionalUserProperty conditionalUserProperty = new ConditionalUserProperty();
                    conditionalUserProperty.mAppId = str;
                    conditionalUserProperty.mOrigin = str2;
                    conditionalUserProperty.mCreationTimestamp = com_google_android_gms_internal_measurement_zzef.zzaem;
                    conditionalUserProperty.mName = com_google_android_gms_internal_measurement_zzef.zzael.name;
                    conditionalUserProperty.mValue = com_google_android_gms_internal_measurement_zzef.zzael.getValue();
                    conditionalUserProperty.mActive = com_google_android_gms_internal_measurement_zzef.zzaen;
                    conditionalUserProperty.mTriggerEventName = com_google_android_gms_internal_measurement_zzef.zzaeo;
                    if (com_google_android_gms_internal_measurement_zzef.zzaep != null) {
                        conditionalUserProperty.mTimedOutEventName = com_google_android_gms_internal_measurement_zzef.zzaep.name;
                        if (com_google_android_gms_internal_measurement_zzef.zzaep.zzafo != null) {
                            conditionalUserProperty.mTimedOutEventParams = com_google_android_gms_internal_measurement_zzef.zzaep.zzafo.zzif();
                        }
                    }
                    conditionalUserProperty.mTriggerTimeout = com_google_android_gms_internal_measurement_zzef.zzaeq;
                    if (com_google_android_gms_internal_measurement_zzef.zzaer != null) {
                        conditionalUserProperty.mTriggeredEventName = com_google_android_gms_internal_measurement_zzef.zzaer.name;
                        if (com_google_android_gms_internal_measurement_zzef.zzaer.zzafo != null) {
                            conditionalUserProperty.mTriggeredEventParams = com_google_android_gms_internal_measurement_zzef.zzaer.zzafo.zzif();
                        }
                    }
                    conditionalUserProperty.mTriggeredTimestamp = com_google_android_gms_internal_measurement_zzef.zzael.zzaqu;
                    conditionalUserProperty.mTimeToLive = com_google_android_gms_internal_measurement_zzef.zzaes;
                    if (com_google_android_gms_internal_measurement_zzef.zzaet != null) {
                        conditionalUserProperty.mExpiredEventName = com_google_android_gms_internal_measurement_zzef.zzaet.name;
                        if (com_google_android_gms_internal_measurement_zzef.zzaet.zzafo != null) {
                            conditionalUserProperty.mExpiredEventParams = com_google_android_gms_internal_measurement_zzef.zzaet.zzafo.zzif();
                        }
                    }
                    arrayList.add(conditionalUserProperty);
                }
                return arrayList;
            }
        }
        zzil.log(str2);
        return Collections.emptyList();
    }

    private final void zzj(boolean z) {
        zzab();
        zzch();
        zzgg().zziq().zzg("Setting app measurement enabled (FE)", Boolean.valueOf(z));
        zzgh().setMeasurementEnabled(z);
        if (!zzgi().zzav(zzfv().zzah())) {
            zzfx().zzkm();
        } else if (this.zzacr.isEnabled() && this.zzaon) {
            zzgg().zziq().log("Recording app launch after enabling measurement for the first time (FE)");
            zzkj();
        } else {
            zzfx().zzkm();
        }
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        zza(null, str, str2, bundle);
    }

    public final void clearConditionalUserPropertyAs(String str, String str2, String str3, Bundle bundle) {
        Preconditions.checkNotEmpty(str);
        zzfq();
        zza(str, str2, str3, bundle);
    }

    public final Task<String> getAppInstanceId() {
        try {
            String zziw = zzgh().zziw();
            return zziw != null ? Tasks.forResult(zziw) : Tasks.call(zzgf().zzjh(), new zzhq(this));
        } catch (Exception e) {
            zzgg().zzin().log("Failed to schedule task for getAppInstanceId");
            return Tasks.forException(e);
        }
    }

    public final List<ConditionalUserProperty> getConditionalUserProperties(String str, String str2) {
        return zzf(null, str, str2);
    }

    public final List<ConditionalUserProperty> getConditionalUserPropertiesAs(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzfq();
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
        zzfq();
        return zzb(str, str2, str3, z);
    }

    public final void registerOnMeasurementEventListener(OnEventListener onEventListener) {
        zzch();
        Preconditions.checkNotNull(onEventListener);
        if (!this.zzaok.add(onEventListener)) {
            zzgg().zzin().log("OnEventListener already registered");
        }
    }

    public final void resetAnalyticsData() {
        zzgf().zzc(new zzhs(this));
    }

    public final void setConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        ConditionalUserProperty conditionalUserProperty2 = new ConditionalUserProperty(conditionalUserProperty);
        if (!TextUtils.isEmpty(conditionalUserProperty2.mAppId)) {
            zzgg().zzin().log("Package name should be null when calling setConditionalUserProperty");
        }
        conditionalUserProperty2.mAppId = null;
        zza(conditionalUserProperty2);
    }

    public final void setConditionalUserPropertyAs(ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mAppId);
        zzfq();
        zza(new ConditionalUserProperty(conditionalUserProperty));
    }

    public final void setEventInterceptor(EventInterceptor eventInterceptor) {
        zzab();
        zzch();
        if (!(eventInterceptor == null || eventInterceptor == this.zzaoj)) {
            Preconditions.checkState(this.zzaoj == null, "EventInterceptor already set.");
        }
        this.zzaoj = eventInterceptor;
    }

    public final void setMeasurementEnabled(boolean z) {
        zzch();
        zzgf().zzc(new zzib(this, z));
    }

    public final void setMinimumSessionDuration(long j) {
        zzgf().zzc(new zzic(this, j));
    }

    public final void setSessionTimeoutDuration(long j) {
        zzgf().zzc(new zzid(this, j));
    }

    public final void unregisterOnMeasurementEventListener(OnEventListener onEventListener) {
        zzch();
        Preconditions.checkNotNull(onEventListener);
        if (!this.zzaok.remove(onEventListener)) {
            zzgg().zzin().log("OnEventListener had not been registered");
        }
    }

    public final void zza(String str, String str2, Bundle bundle) {
        boolean z;
        if (this.zzaoj != null) {
            if (!zzjv.zzcb(str2)) {
                z = false;
                zza(str, str2, bundle, true, z, false, null);
            }
        }
        z = true;
        zza(str, str2, bundle, true, z, false, null);
    }

    public final void zza(String str, String str2, Bundle bundle, long j) {
        zza(str, str2, j, bundle, false, true, true, null);
    }

    public final void zza(String str, String str2, Bundle bundle, boolean z) {
        if (this.zzaoj != null) {
            if (!zzjv.zzcb(str2)) {
                z = false;
                zza(str, str2, bundle, true, z, true, null);
            }
        }
        z = true;
        zza(str, str2, bundle, true, z, true, null);
    }

    public final void zza(String str, String str2, Object obj) {
        Preconditions.checkNotEmpty(str);
        long currentTimeMillis = zzbt().currentTimeMillis();
        int zzby = zzgc().zzby(str2);
        int i = 0;
        if (zzby != 0) {
            zzgc();
            str = zzjv.zza(str2, 24, true);
            if (str2 != null) {
                i = str2.length();
            }
            this.zzacr.zzgc().zza(zzby, "_ev", str, i);
        } else if (obj != null) {
            zzby = zzgc().zzi(str2, obj);
            if (zzby != 0) {
                zzgc();
                str = zzjv.zza(str2, 24, true);
                if ((obj instanceof String) || (obj instanceof CharSequence)) {
                    i = String.valueOf(obj).length();
                }
                this.zzacr.zzgc().zza(zzby, "_ev", str, i);
                return;
            }
            Object zzj = zzgc().zzj(str2, obj);
            if (zzj != null) {
                zza(str, str2, currentTimeMillis, zzj);
            }
        } else {
            zza(str, str2, currentTimeMillis, null);
        }
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    final String zzae(long j) {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            zzgf().zzc(new zzhr(this, atomicReference));
            try {
                atomicReference.wait(j);
            } catch (InterruptedException e) {
                zzgg().zzin().log("Interrupted waiting for app instance id");
                return null;
            }
        }
        return (String) atomicReference.get();
    }

    final void zzbm(String str) {
        this.zzaom.set(str);
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

    protected final boolean zzhh() {
        return false;
    }

    public final String zziw() {
        return (String) this.zzaom.get();
    }

    public final List<zzjs> zzk(boolean z) {
        zzfi zzil;
        String str;
        zzch();
        zzgg().zziq().log("Fetching user attributes (FE)");
        if (zzgf().zzjg()) {
            zzil = zzgg().zzil();
            str = "Cannot get all user properties from analytics worker thread";
        } else {
            zzgf();
            if (zzgg.isMainThread()) {
                zzil = zzgg().zzil();
                str = "Cannot get all user properties from main thread";
            } else {
                AtomicReference atomicReference = new AtomicReference();
                synchronized (atomicReference) {
                    this.zzacr.zzgf().zzc(new zzhp(this, atomicReference, z));
                    try {
                        atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                    } catch (InterruptedException e) {
                        zzgg().zzin().zzg("Interrupted waiting for get user properties", e);
                    }
                }
                List<zzjs> list = (List) atomicReference.get();
                if (list != null) {
                    return list;
                }
                zzil = zzgg().zzin();
                str = "Timed out waiting for get user properties";
            }
        }
        zzil.log(str);
        return Collections.emptyList();
    }

    public final Boolean zzke() {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            zzgf().zzc(new zzhn(this, atomicReference));
            try {
                atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            } catch (InterruptedException e) {
                zzgg().zzin().log("Interrupted waiting for boolean test flag value");
                return null;
            }
        }
        return (Boolean) atomicReference.get();
    }

    public final String zzkf() {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            zzgf().zzc(new zzhx(this, atomicReference));
            try {
                atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            } catch (InterruptedException e) {
                zzgg().zzin().log("Interrupted waiting for String test flag value");
                return null;
            }
        }
        return (String) atomicReference.get();
    }

    public final Long zzkg() {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            zzgf().zzc(new zzhy(this, atomicReference));
            try {
                atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            } catch (InterruptedException e) {
                zzgg().zzin().log("Interrupted waiting for long test flag value");
                return null;
            }
        }
        return (Long) atomicReference.get();
    }

    public final Integer zzkh() {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            zzgf().zzc(new zzhz(this, atomicReference));
            try {
                atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            } catch (InterruptedException e) {
                zzgg().zzin().log("Interrupted waiting for int test flag value");
                return null;
            }
        }
        return (Integer) atomicReference.get();
    }

    public final Double zzki() {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            zzgf().zzc(new zzia(this, atomicReference));
            try {
                atomicReference.wait(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            } catch (InterruptedException e) {
                zzgg().zzin().log("Interrupted waiting for double test flag value");
                return null;
            }
        }
        return (Double) atomicReference.get();
    }

    public final void zzkj() {
        zzab();
        zzch();
        if (this.zzacr.zzjk()) {
            zzfx().zzkj();
            this.zzaon = false;
            String zziz = zzgh().zziz();
            if (!TextUtils.isEmpty(zziz)) {
                zzfw().zzch();
                if (!zziz.equals(VERSION.RELEASE)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("_po", zziz);
                    zza("auto", "_ou", bundle);
                }
            }
        }
    }
}
