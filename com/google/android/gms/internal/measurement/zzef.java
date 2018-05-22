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

public final class zzef extends zzhg {
    private zzeh zzaet = zzeg.zzaeu;
    private Boolean zzxz;

    zzef(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    public static long zzhh() {
        return ((Long) zzew.zzahl.get()).longValue();
    }

    public static long zzhi() {
        return ((Long) zzew.zzagl.get()).longValue();
    }

    public static boolean zzhk() {
        return ((Boolean) zzew.zzagh.get()).booleanValue();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final long zza(String str, zzex<Long> com_google_android_gms_internal_measurement_zzex_java_lang_Long) {
        if (str == null) {
            return ((Long) com_google_android_gms_internal_measurement_zzex_java_lang_Long.get()).longValue();
        }
        Object zze = this.zzaet.zze(str, com_google_android_gms_internal_measurement_zzex_java_lang_Long.getKey());
        if (TextUtils.isEmpty(zze)) {
            return ((Long) com_google_android_gms_internal_measurement_zzex_java_lang_Long.get()).longValue();
        }
        try {
            return ((Long) com_google_android_gms_internal_measurement_zzex_java_lang_Long.get(Long.valueOf(Long.parseLong(zze)))).longValue();
        } catch (NumberFormatException e) {
            return ((Long) com_google_android_gms_internal_measurement_zzex_java_lang_Long.get()).longValue();
        }
    }

    final void zza(zzeh com_google_android_gms_internal_measurement_zzeh) {
        this.zzaet = com_google_android_gms_internal_measurement_zzeh;
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final int zzar(String str) {
        return zzb(str, zzew.zzagw);
    }

    final Boolean zzas(String str) {
        Boolean bool = null;
        Preconditions.checkNotEmpty(str);
        try {
            if (getContext().getPackageManager() == null) {
                zzge().zzim().log("Failed to load metadata: PackageManager is null");
            } else {
                ApplicationInfo applicationInfo = Wrappers.packageManager(getContext()).getApplicationInfo(getContext().getPackageName(), 128);
                if (applicationInfo == null) {
                    zzge().zzim().log("Failed to load metadata: ApplicationInfo is null");
                } else if (applicationInfo.metaData == null) {
                    zzge().zzim().log("Failed to load metadata: Metadata bundle is null");
                } else if (applicationInfo.metaData.containsKey(str)) {
                    bool = Boolean.valueOf(applicationInfo.metaData.getBoolean(str));
                }
            }
        } catch (NameNotFoundException e) {
            zzge().zzim().zzg("Failed to load metadata: Package name not found", e);
        }
        return bool;
    }

    public final boolean zzat(String str) {
        return "1".equals(this.zzaet.zze(str, "gaia_collection_enabled"));
    }

    public final boolean zzau(String str) {
        return "1".equals(this.zzaet.zze(str, "measurement.event_sampling_enabled"));
    }

    final boolean zzav(String str) {
        return zzd(str, zzew.zzahu);
    }

    final boolean zzaw(String str) {
        return zzd(str, zzew.zzahw);
    }

    final boolean zzax(String str) {
        return zzd(str, zzew.zzahx);
    }

    final boolean zzay(String str) {
        return zzd(str, zzew.zzahy);
    }

    final boolean zzaz(String str) {
        return zzd(str, zzew.zzahz);
    }

    public final int zzb(String str, zzex<Integer> com_google_android_gms_internal_measurement_zzex_java_lang_Integer) {
        if (str == null) {
            return ((Integer) com_google_android_gms_internal_measurement_zzex_java_lang_Integer.get()).intValue();
        }
        Object zze = this.zzaet.zze(str, com_google_android_gms_internal_measurement_zzex_java_lang_Integer.getKey());
        if (TextUtils.isEmpty(zze)) {
            return ((Integer) com_google_android_gms_internal_measurement_zzex_java_lang_Integer.get()).intValue();
        }
        try {
            return ((Integer) com_google_android_gms_internal_measurement_zzex_java_lang_Integer.get(Integer.valueOf(Integer.parseInt(zze)))).intValue();
        } catch (NumberFormatException e) {
            return ((Integer) com_google_android_gms_internal_measurement_zzex_java_lang_Integer.get()).intValue();
        }
    }

    final boolean zzba(String str) {
        return zzd(str, zzew.zzaic);
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final double zzc(String str, zzex<Double> com_google_android_gms_internal_measurement_zzex_java_lang_Double) {
        if (str == null) {
            return ((Double) com_google_android_gms_internal_measurement_zzex_java_lang_Double.get()).doubleValue();
        }
        Object zze = this.zzaet.zze(str, com_google_android_gms_internal_measurement_zzex_java_lang_Double.getKey());
        if (TextUtils.isEmpty(zze)) {
            return ((Double) com_google_android_gms_internal_measurement_zzex_java_lang_Double.get()).doubleValue();
        }
        try {
            return ((Double) com_google_android_gms_internal_measurement_zzex_java_lang_Double.get(Double.valueOf(Double.parseDouble(zze)))).doubleValue();
        } catch (NumberFormatException e) {
            return ((Double) com_google_android_gms_internal_measurement_zzex_java_lang_Double.get()).doubleValue();
        }
    }

    public final boolean zzd(String str, zzex<Boolean> com_google_android_gms_internal_measurement_zzex_java_lang_Boolean) {
        if (str == null) {
            return ((Boolean) com_google_android_gms_internal_measurement_zzex_java_lang_Boolean.get()).booleanValue();
        }
        Object zze = this.zzaet.zze(str, com_google_android_gms_internal_measurement_zzex_java_lang_Boolean.getKey());
        return TextUtils.isEmpty(zze) ? ((Boolean) com_google_android_gms_internal_measurement_zzex_java_lang_Boolean.get()).booleanValue() : ((Boolean) com_google_android_gms_internal_measurement_zzex_java_lang_Boolean.get(Boolean.valueOf(Boolean.parseBoolean(zze)))).booleanValue();
    }

    public final boolean zzds() {
        if (this.zzxz == null) {
            synchronized (this) {
                if (this.zzxz == null) {
                    ApplicationInfo applicationInfo = getContext().getApplicationInfo();
                    String myProcessName = ProcessUtils.getMyProcessName();
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        boolean z = str != null && str.equals(myProcessName);
                        this.zzxz = Boolean.valueOf(z);
                    }
                    if (this.zzxz == null) {
                        this.zzxz = Boolean.TRUE;
                        zzge().zzim().log("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzxz.booleanValue();
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

    public final boolean zzhg() {
        Boolean zzas = zzas("firebase_analytics_collection_deactivated");
        return zzas != null && zzas.booleanValue();
    }

    public final String zzhj() {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke(null, new Object[]{"debug.firebase.analytics.app", TtmlNode.ANONYMOUS_REGION_ID});
        } catch (ClassNotFoundException e) {
            zzge().zzim().zzg("Could not find SystemProperties class", e);
        } catch (NoSuchMethodException e2) {
            zzge().zzim().zzg("Could not find SystemProperties.get() method", e2);
        } catch (IllegalAccessException e3) {
            zzge().zzim().zzg("Could not access SystemProperties.get()", e3);
        } catch (InvocationTargetException e4) {
            zzge().zzim().zzg("SystemProperties.get() threw an exception", e4);
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }

    final boolean zzhl() {
        return zzd(zzfv().zzah(), zzew.zzahp);
    }

    final String zzhm() {
        String zzah = zzfv().zzah();
        zzex com_google_android_gms_internal_measurement_zzex = zzew.zzahq;
        return zzah == null ? (String) com_google_android_gms_internal_measurement_zzex.get() : (String) com_google_android_gms_internal_measurement_zzex.get(this.zzaet.zze(zzah, com_google_android_gms_internal_measurement_zzex.getKey()));
    }
}
