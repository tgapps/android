package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.util.Map;

public final class zzdo extends zzf {
    protected zzdn zzaro;
    private volatile zzdn zzarp;
    private zzdn zzarq;
    private final Map<Activity, zzdn> zzarr = new ArrayMap();
    private zzdn zzars;
    private String zzart;

    public zzdo(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
    }

    protected final boolean zzgt() {
        return false;
    }

    public final zzdn zzla() {
        zzcl();
        zzaf();
        return this.zzaro;
    }

    public final void setCurrentScreen(Activity activity, String str, String str2) {
        if (this.zzarp == null) {
            zzgo().zzjg().zzbx("setCurrentScreen cannot be called while no activity active");
        } else if (this.zzarr.get(activity) == null) {
            zzgo().zzjg().zzbx("setCurrentScreen must be called with an activity in the activity lifecycle");
        } else {
            if (str2 == null) {
                str2 = zzcn(activity.getClass().getCanonicalName());
            }
            boolean equals = this.zzarp.zzarl.equals(str2);
            boolean zzu = zzfk.zzu(this.zzarp.zzuw, str);
            if (equals && zzu) {
                zzgo().zzji().zzbx("setCurrentScreen cannot be called with the same class and name");
            } else if (str != null && (str.length() <= 0 || str.length() > 100)) {
                zzgo().zzjg().zzg("Invalid screen name length in setCurrentScreen. Length", Integer.valueOf(str.length()));
            } else if (str2 == null || (str2.length() > 0 && str2.length() <= 100)) {
                Object obj;
                zzar zzjl = zzgo().zzjl();
                String str3 = "Setting current screen to name, class";
                if (str == null) {
                    obj = "null";
                } else {
                    String str4 = str;
                }
                zzjl.zze(str3, obj, str2);
                zzdn com_google_android_gms_measurement_internal_zzdn = new zzdn(str, str2, zzgm().zzmc());
                this.zzarr.put(activity, com_google_android_gms_measurement_internal_zzdn);
                zza(activity, com_google_android_gms_measurement_internal_zzdn, true);
            } else {
                zzgo().zzjg().zzg("Invalid class name length in setCurrentScreen. Length", Integer.valueOf(str2.length()));
            }
        }
    }

    public final zzdn zzlb() {
        zzgb();
        return this.zzarp;
    }

    private final void zza(Activity activity, zzdn com_google_android_gms_measurement_internal_zzdn, boolean z) {
        zzdn com_google_android_gms_measurement_internal_zzdn2 = this.zzarp == null ? this.zzarq : this.zzarp;
        if (com_google_android_gms_measurement_internal_zzdn.zzarl == null) {
            com_google_android_gms_measurement_internal_zzdn = new zzdn(com_google_android_gms_measurement_internal_zzdn.zzuw, zzcn(activity.getClass().getCanonicalName()), com_google_android_gms_measurement_internal_zzdn.zzarm);
        }
        this.zzarq = this.zzarp;
        this.zzarp = com_google_android_gms_measurement_internal_zzdn;
        zzgn().zzc(new zzdp(this, z, com_google_android_gms_measurement_internal_zzdn2, com_google_android_gms_measurement_internal_zzdn));
    }

    private final void zza(zzdn com_google_android_gms_measurement_internal_zzdn) {
        zzgd().zzq(zzbx().elapsedRealtime());
        if (zzgj().zzn(com_google_android_gms_measurement_internal_zzdn.zzarn)) {
            com_google_android_gms_measurement_internal_zzdn.zzarn = false;
        }
    }

    public static void zza(zzdn com_google_android_gms_measurement_internal_zzdn, Bundle bundle, boolean z) {
        if (bundle != null && com_google_android_gms_measurement_internal_zzdn != null && (!bundle.containsKey("_sc") || z)) {
            if (com_google_android_gms_measurement_internal_zzdn.zzuw != null) {
                bundle.putString("_sn", com_google_android_gms_measurement_internal_zzdn.zzuw);
            } else {
                bundle.remove("_sn");
            }
            bundle.putString("_sc", com_google_android_gms_measurement_internal_zzdn.zzarl);
            bundle.putLong("_si", com_google_android_gms_measurement_internal_zzdn.zzarm);
        } else if (bundle != null && com_google_android_gms_measurement_internal_zzdn == null && z) {
            bundle.remove("_sn");
            bundle.remove("_sc");
            bundle.remove("_si");
        }
    }

    public final void zza(String str, zzdn com_google_android_gms_measurement_internal_zzdn) {
        zzaf();
        synchronized (this) {
            if (this.zzart == null || this.zzart.equals(str) || com_google_android_gms_measurement_internal_zzdn != null) {
                this.zzart = str;
                this.zzars = com_google_android_gms_measurement_internal_zzdn;
            }
        }
    }

    private static String zzcn(String str) {
        String str2;
        String[] split = str.split("\\.");
        if (split.length > 0) {
            str2 = split[split.length - 1];
        } else {
            str2 = TtmlNode.ANONYMOUS_REGION_ID;
        }
        if (str2.length() > 100) {
            return str2.substring(0, 100);
        }
        return str2;
    }

    private final zzdn zze(Activity activity) {
        Preconditions.checkNotNull(activity);
        zzdn com_google_android_gms_measurement_internal_zzdn = (zzdn) this.zzarr.get(activity);
        if (com_google_android_gms_measurement_internal_zzdn != null) {
            return com_google_android_gms_measurement_internal_zzdn;
        }
        com_google_android_gms_measurement_internal_zzdn = new zzdn(null, zzcn(activity.getClass().getCanonicalName()), zzgm().zzmc());
        this.zzarr.put(activity, com_google_android_gms_measurement_internal_zzdn);
        return com_google_android_gms_measurement_internal_zzdn;
    }

    public final void onActivityCreated(Activity activity, Bundle bundle) {
        if (bundle != null) {
            Bundle bundle2 = bundle.getBundle("com.google.app_measurement.screen_service");
            if (bundle2 != null) {
                this.zzarr.put(activity, new zzdn(bundle2.getString("name"), bundle2.getString("referrer_name"), bundle2.getLong(TtmlNode.ATTR_ID)));
            }
        }
    }

    public final void onActivityResumed(Activity activity) {
        zza(activity, zze(activity), false);
        zzco zzgd = zzgd();
        zzgd.zzgn().zzc(new zzd(zzgd, zzgd.zzbx().elapsedRealtime()));
    }

    public final void onActivityPaused(Activity activity) {
        zzdn zze = zze(activity);
        this.zzarq = this.zzarp;
        this.zzarp = null;
        zzgn().zzc(new zzdq(this, zze));
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (bundle != null) {
            zzdn com_google_android_gms_measurement_internal_zzdn = (zzdn) this.zzarr.get(activity);
            if (com_google_android_gms_measurement_internal_zzdn != null) {
                Bundle bundle2 = new Bundle();
                bundle2.putLong(TtmlNode.ATTR_ID, com_google_android_gms_measurement_internal_zzdn.zzarm);
                bundle2.putString("name", com_google_android_gms_measurement_internal_zzdn.zzuw);
                bundle2.putString("referrer_name", com_google_android_gms_measurement_internal_zzdn.zzarl);
                bundle.putBundle("com.google.app_measurement.screen_service", bundle2);
            }
        }
    }

    public final void onActivityDestroyed(Activity activity) {
        this.zzarr.remove(activity);
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
