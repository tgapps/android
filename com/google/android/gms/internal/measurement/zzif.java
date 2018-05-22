package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.util.Map;

public final class zzif extends zzhh {
    protected zzie zzaol;
    private volatile zzie zzaom;
    private zzie zzaon;
    private long zzaoo;
    private final Map<Activity, zzie> zzaop = new ArrayMap();
    private zzie zzaoq;
    private String zzaor;

    public zzif(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final void zza(Activity activity, zzie com_google_android_gms_internal_measurement_zzie, boolean z) {
        zzie com_google_android_gms_internal_measurement_zzie2 = this.zzaom == null ? this.zzaon : this.zzaom;
        if (com_google_android_gms_internal_measurement_zzie.zzaoi == null) {
            com_google_android_gms_internal_measurement_zzie = new zzie(com_google_android_gms_internal_measurement_zzie.zzul, zzca(activity.getClass().getCanonicalName()), com_google_android_gms_internal_measurement_zzie.zzaoj);
        }
        this.zzaon = this.zzaom;
        this.zzaoo = zzbt().elapsedRealtime();
        this.zzaom = com_google_android_gms_internal_measurement_zzie;
        zzgd().zzc(new zzig(this, z, com_google_android_gms_internal_measurement_zzie2, com_google_android_gms_internal_measurement_zzie));
    }

    private final void zza(zzie com_google_android_gms_internal_measurement_zzie) {
        zzft().zzk(zzbt().elapsedRealtime());
        if (zzgc().zzl(com_google_android_gms_internal_measurement_zzie.zzaok)) {
            com_google_android_gms_internal_measurement_zzie.zzaok = false;
        }
    }

    public static void zza(zzie com_google_android_gms_internal_measurement_zzie, Bundle bundle, boolean z) {
        if (bundle != null && com_google_android_gms_internal_measurement_zzie != null && (!bundle.containsKey("_sc") || z)) {
            if (com_google_android_gms_internal_measurement_zzie.zzul != null) {
                bundle.putString("_sn", com_google_android_gms_internal_measurement_zzie.zzul);
            } else {
                bundle.remove("_sn");
            }
            bundle.putString("_sc", com_google_android_gms_internal_measurement_zzie.zzaoi);
            bundle.putLong("_si", com_google_android_gms_internal_measurement_zzie.zzaoj);
        } else if (bundle != null && com_google_android_gms_internal_measurement_zzie == null && z) {
            bundle.remove("_sn");
            bundle.remove("_sc");
            bundle.remove("_si");
        }
    }

    private static String zzca(String str) {
        String[] split = str.split("\\.");
        String str2 = split.length > 0 ? split[split.length - 1] : TtmlNode.ANONYMOUS_REGION_ID;
        return str2.length() > 100 ? str2.substring(0, 100) : str2;
    }

    private final zzie zze(Activity activity) {
        Preconditions.checkNotNull(activity);
        zzie com_google_android_gms_internal_measurement_zzie = (zzie) this.zzaop.get(activity);
        if (com_google_android_gms_internal_measurement_zzie != null) {
            return com_google_android_gms_internal_measurement_zzie;
        }
        com_google_android_gms_internal_measurement_zzie = new zzie(null, zzca(activity.getClass().getCanonicalName()), zzgb().zzlb());
        this.zzaop.put(activity, com_google_android_gms_internal_measurement_zzie);
        return com_google_android_gms_internal_measurement_zzie;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
        if (bundle != null) {
            Bundle bundle2 = bundle.getBundle("com.google.firebase.analytics.screen_service");
            if (bundle2 != null) {
                this.zzaop.put(activity, new zzie(bundle2.getString("name"), bundle2.getString("referrer_name"), bundle2.getLong(TtmlNode.ATTR_ID)));
            }
        }
    }

    public final void onActivityDestroyed(Activity activity) {
        this.zzaop.remove(activity);
    }

    public final void onActivityPaused(Activity activity) {
        zzie zze = zze(activity);
        this.zzaon = this.zzaom;
        this.zzaoo = zzbt().elapsedRealtime();
        this.zzaom = null;
        zzgd().zzc(new zzih(this, zze));
    }

    public final void onActivityResumed(Activity activity) {
        zza(activity, zze(activity), false);
        zzhg zzft = zzft();
        zzft.zzgd().zzc(new zzdx(zzft, zzft.zzbt().elapsedRealtime()));
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (bundle != null) {
            zzie com_google_android_gms_internal_measurement_zzie = (zzie) this.zzaop.get(activity);
            if (com_google_android_gms_internal_measurement_zzie != null) {
                Bundle bundle2 = new Bundle();
                bundle2.putLong(TtmlNode.ATTR_ID, com_google_android_gms_internal_measurement_zzie.zzaoj);
                bundle2.putString("name", com_google_android_gms_internal_measurement_zzie.zzul);
                bundle2.putString("referrer_name", com_google_android_gms_internal_measurement_zzie.zzaoi);
                bundle.putBundle("com.google.firebase.analytics.screen_service", bundle2);
            }
        }
    }

    public final void setCurrentScreen(Activity activity, String str, String str2) {
        zzgd();
        if (!zzgg.isMainThread()) {
            zzge().zzip().log("setCurrentScreen must be called from the main thread");
        } else if (this.zzaom == null) {
            zzge().zzip().log("setCurrentScreen cannot be called while no activity active");
        } else if (this.zzaop.get(activity) == null) {
            zzge().zzip().log("setCurrentScreen must be called with an activity in the activity lifecycle");
        } else {
            if (str2 == null) {
                str2 = zzca(activity.getClass().getCanonicalName());
            }
            boolean equals = this.zzaom.zzaoi.equals(str2);
            boolean zzs = zzka.zzs(this.zzaom.zzul, str);
            if (equals && zzs) {
                zzge().zziq().log("setCurrentScreen cannot be called with the same class and name");
            } else if (str != null && (str.length() <= 0 || str.length() > 100)) {
                zzge().zzip().zzg("Invalid screen name length in setCurrentScreen. Length", Integer.valueOf(str.length()));
            } else if (str2 == null || (str2.length() > 0 && str2.length() <= 100)) {
                Object obj;
                zzfi zzit = zzge().zzit();
                String str3 = "Setting current screen to name, class";
                if (str == null) {
                    obj = "null";
                } else {
                    String str4 = str;
                }
                zzit.zze(str3, obj, str2);
                zzie com_google_android_gms_internal_measurement_zzie = new zzie(str, str2, zzgb().zzlb());
                this.zzaop.put(activity, com_google_android_gms_internal_measurement_zzie);
                zza(activity, com_google_android_gms_internal_measurement_zzie, true);
            } else {
                zzge().zzip().zzg("Invalid class name length in setCurrentScreen. Length", Integer.valueOf(str2.length()));
            }
        }
    }

    public final void zza(String str, zzie com_google_android_gms_internal_measurement_zzie) {
        zzab();
        synchronized (this) {
            if (this.zzaor == null || this.zzaor.equals(str) || com_google_android_gms_internal_measurement_zzie != null) {
                this.zzaor = str;
                this.zzaoq = com_google_android_gms_internal_measurement_zzie;
            }
        }
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
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

    public final zzie zzkc() {
        zzch();
        zzab();
        return this.zzaol;
    }

    public final zzie zzkd() {
        return this.zzaom;
    }
}
