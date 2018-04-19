package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.ProcessUtils;
import com.google.android.gms.common.wrappers.Wrappers;
import java.lang.reflect.InvocationTargetException;

public final class zzeh extends zzhj {
    private Boolean zzxu;

    zzeh(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    public static long zzhj() {
        return ((Long) zzew.zzaho.get()).longValue();
    }

    public static long zzhk() {
        return ((Long) zzew.zzago.get()).longValue();
    }

    public static boolean zzhm() {
        return ((Boolean) zzew.zzagj.get()).booleanValue();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final long zza(String str, zzex<Long> com_google_android_gms_internal_measurement_zzex_java_lang_Long) {
        if (str == null) {
            return ((Long) com_google_android_gms_internal_measurement_zzex_java_lang_Long.get()).longValue();
        }
        Object zzm = zzgd().zzm(str, com_google_android_gms_internal_measurement_zzex_java_lang_Long.getKey());
        if (TextUtils.isEmpty(zzm)) {
            return ((Long) com_google_android_gms_internal_measurement_zzex_java_lang_Long.get()).longValue();
        }
        try {
            return ((Long) com_google_android_gms_internal_measurement_zzex_java_lang_Long.get(Long.valueOf(Long.parseLong(zzm)))).longValue();
        } catch (NumberFormatException e) {
            return ((Long) com_google_android_gms_internal_measurement_zzex_java_lang_Long.get()).longValue();
        }
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final int zzar(String str) {
        return zzb(str, zzew.zzagz);
    }

    final Boolean zzas(String str) {
        Boolean bool = null;
        Preconditions.checkNotEmpty(str);
        try {
            if (getContext().getPackageManager() == null) {
                zzgg().zzil().log("Failed to load metadata: PackageManager is null");
            } else {
                ApplicationInfo applicationInfo = Wrappers.packageManager(getContext()).getApplicationInfo(getContext().getPackageName(), 128);
                if (applicationInfo == null) {
                    zzgg().zzil().log("Failed to load metadata: ApplicationInfo is null");
                } else if (applicationInfo.metaData == null) {
                    zzgg().zzil().log("Failed to load metadata: Metadata bundle is null");
                } else if (applicationInfo.metaData.containsKey(str)) {
                    bool = Boolean.valueOf(applicationInfo.metaData.getBoolean(str));
                }
            }
        } catch (NameNotFoundException e) {
            zzgg().zzil().zzg("Failed to load metadata: Package name not found", e);
        }
        return bool;
    }

    public final boolean zzat(String str) {
        return "1".equals(zzgd().zzm(str, "gaia_collection_enabled"));
    }

    final boolean zzau(String str) {
        return zzd(str, zzew.zzahx);
    }

    final boolean zzav(String str) {
        return zzd(str, zzew.zzaic);
    }

    public final int zzb(String str, zzex<Integer> com_google_android_gms_internal_measurement_zzex_java_lang_Integer) {
        if (str == null) {
            return ((Integer) com_google_android_gms_internal_measurement_zzex_java_lang_Integer.get()).intValue();
        }
        Object zzm = zzgd().zzm(str, com_google_android_gms_internal_measurement_zzex_java_lang_Integer.getKey());
        if (TextUtils.isEmpty(zzm)) {
            return ((Integer) com_google_android_gms_internal_measurement_zzex_java_lang_Integer.get()).intValue();
        }
        try {
            return ((Integer) com_google_android_gms_internal_measurement_zzex_java_lang_Integer.get(Integer.valueOf(Integer.parseInt(zzm)))).intValue();
        } catch (NumberFormatException e) {
            return ((Integer) com_google_android_gms_internal_measurement_zzex_java_lang_Integer.get()).intValue();
        }
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final double zzc(String str, zzex<Double> com_google_android_gms_internal_measurement_zzex_java_lang_Double) {
        if (str == null) {
            return ((Double) com_google_android_gms_internal_measurement_zzex_java_lang_Double.get()).doubleValue();
        }
        Object zzm = zzgd().zzm(str, com_google_android_gms_internal_measurement_zzex_java_lang_Double.getKey());
        if (TextUtils.isEmpty(zzm)) {
            return ((Double) com_google_android_gms_internal_measurement_zzex_java_lang_Double.get()).doubleValue();
        }
        try {
            return ((Double) com_google_android_gms_internal_measurement_zzex_java_lang_Double.get(Double.valueOf(Double.parseDouble(zzm)))).doubleValue();
        } catch (NumberFormatException e) {
            return ((Double) com_google_android_gms_internal_measurement_zzex_java_lang_Double.get()).doubleValue();
        }
    }

    public final boolean zzd(String str, zzex<Boolean> com_google_android_gms_internal_measurement_zzex_java_lang_Boolean) {
        if (str == null) {
            return ((Boolean) com_google_android_gms_internal_measurement_zzex_java_lang_Boolean.get()).booleanValue();
        }
        Object zzm = zzgd().zzm(str, com_google_android_gms_internal_measurement_zzex_java_lang_Boolean.getKey());
        return TextUtils.isEmpty(zzm) ? ((Boolean) com_google_android_gms_internal_measurement_zzex_java_lang_Boolean.get()).booleanValue() : ((Boolean) com_google_android_gms_internal_measurement_zzex_java_lang_Boolean.get(Boolean.valueOf(Boolean.parseBoolean(zzm)))).booleanValue();
    }

    public final boolean zzds() {
        if (this.zzxu == null) {
            synchronized (this) {
                if (this.zzxu == null) {
                    ApplicationInfo applicationInfo = getContext().getApplicationInfo();
                    String myProcessName = ProcessUtils.getMyProcessName();
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        boolean z = str != null && str.equals(myProcessName);
                        this.zzxu = Boolean.valueOf(z);
                    }
                    if (this.zzxu == null) {
                        this.zzxu = Boolean.TRUE;
                        zzgg().zzil().log("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzxu.booleanValue();
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

    public final boolean zzhi() {
        Boolean zzas = zzas("firebase_analytics_collection_deactivated");
        return zzas != null && zzas.booleanValue();
    }

    public final String zzhl() {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke(null, new Object[]{"debug.firebase.analytics.app", TtmlNode.ANONYMOUS_REGION_ID});
        } catch (ClassNotFoundException e) {
            zzgg().zzil().zzg("Could not find SystemProperties class", e);
        } catch (NoSuchMethodException e2) {
            zzgg().zzil().zzg("Could not find SystemProperties.get() method", e2);
        } catch (IllegalAccessException e3) {
            zzgg().zzil().zzg("Could not access SystemProperties.get()", e3);
        } catch (InvocationTargetException e4) {
            zzgg().zzil().zzg("SystemProperties.get() threw an exception", e4);
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }
}
