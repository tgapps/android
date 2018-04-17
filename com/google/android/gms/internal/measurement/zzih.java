package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.measurement.AppMeasurement.zza;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class zzih extends zzhk {
    protected zzik zzapc;
    private volatile zzig zzapd;
    private zzig zzape;
    private long zzapf;
    private final Map<Activity, zzik> zzapg = new ArrayMap();
    private final CopyOnWriteArrayList<zza> zzaph = new CopyOnWriteArrayList();
    private boolean zzapi;

    public zzih(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final void zza(Activity activity, zzik com_google_android_gms_internal_measurement_zzik, boolean z) {
        zzig com_google_android_gms_internal_measurement_zzig = null;
        zzig com_google_android_gms_internal_measurement_zzig2 = this.zzapd != null ? this.zzapd : (this.zzape == null || Math.abs(zzbt().elapsedRealtime() - this.zzapf) >= 1000) ? null : this.zzape;
        if (com_google_android_gms_internal_measurement_zzig2 != null) {
            com_google_android_gms_internal_measurement_zzig = new zzig(com_google_android_gms_internal_measurement_zzig2);
        }
        int i = 1;
        this.zzapi = true;
        try {
            Iterator it = this.zzaph.iterator();
            while (it.hasNext()) {
                try {
                    i &= ((zza) it.next()).zza(com_google_android_gms_internal_measurement_zzig, com_google_android_gms_internal_measurement_zzik);
                } catch (Exception e) {
                    zzgg().zzil().zzg("onScreenChangeCallback threw exception", e);
                }
            }
        } catch (Exception e2) {
            zzgg().zzil().zzg("onScreenChangeCallback loop threw exception", e2);
        } catch (Throwable th) {
            this.zzapi = false;
        }
        this.zzapi = false;
        com_google_android_gms_internal_measurement_zzig = this.zzapd == null ? this.zzape : this.zzapd;
        if (i != 0) {
            if (com_google_android_gms_internal_measurement_zzik.zzapa == null) {
                com_google_android_gms_internal_measurement_zzik.zzapa = zzbu(activity.getClass().getCanonicalName());
            }
            zzig com_google_android_gms_internal_measurement_zzik2 = new zzik(com_google_android_gms_internal_measurement_zzik);
            this.zzape = this.zzapd;
            this.zzapf = zzbt().elapsedRealtime();
            this.zzapd = com_google_android_gms_internal_measurement_zzik2;
            zzgf().zzc(new zzii(this, z, com_google_android_gms_internal_measurement_zzig, com_google_android_gms_internal_measurement_zzik2));
        }
    }

    public static void zza(zzig com_google_android_gms_internal_measurement_zzig, Bundle bundle, boolean z) {
        if (bundle == null || com_google_android_gms_internal_measurement_zzig == null || (bundle.containsKey("_sc") && !z)) {
            if (bundle != null && com_google_android_gms_internal_measurement_zzig == null && z) {
                bundle.remove("_sn");
                bundle.remove("_sc");
                bundle.remove("_si");
            }
            return;
        }
        if (com_google_android_gms_internal_measurement_zzig.zzug != null) {
            bundle.putString("_sn", com_google_android_gms_internal_measurement_zzig.zzug);
        } else {
            bundle.remove("_sn");
        }
        bundle.putString("_sc", com_google_android_gms_internal_measurement_zzig.zzapa);
        bundle.putLong("_si", com_google_android_gms_internal_measurement_zzig.zzapb);
    }

    private final void zza(zzik com_google_android_gms_internal_measurement_zzik) {
        zzfs().zzk(zzbt().elapsedRealtime());
        if (zzge().zzm(com_google_android_gms_internal_measurement_zzik.zzapq)) {
            com_google_android_gms_internal_measurement_zzik.zzapq = false;
        }
    }

    private static String zzbu(String str) {
        String[] split = str.split("\\.");
        str = split.length > 0 ? split[split.length - 1] : TtmlNode.ANONYMOUS_REGION_ID;
        return str.length() > 100 ? str.substring(0, 100) : str;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final void onActivityDestroyed(Activity activity) {
        this.zzapg.remove(activity);
    }

    public final void onActivityPaused(Activity activity) {
        zzik zze = zze(activity);
        this.zzape = this.zzapd;
        this.zzapf = zzbt().elapsedRealtime();
        this.zzapd = null;
        zzgf().zzc(new zzij(this, zze));
    }

    public final void onActivityResumed(Activity activity) {
        zza(activity, zze(activity), false);
        zzhj zzfs = zzfs();
        zzfs.zzgf().zzc(new zzea(zzfs, zzfs.zzbt().elapsedRealtime()));
    }

    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (bundle != null) {
            zzik com_google_android_gms_internal_measurement_zzik = (zzik) this.zzapg.get(activity);
            if (com_google_android_gms_internal_measurement_zzik != null) {
                Bundle bundle2 = new Bundle();
                bundle2.putLong(TtmlNode.ATTR_ID, com_google_android_gms_internal_measurement_zzik.zzapb);
                bundle2.putString("name", com_google_android_gms_internal_measurement_zzik.zzug);
                bundle2.putString("referrer_name", com_google_android_gms_internal_measurement_zzik.zzapa);
                bundle.putBundle("com.google.firebase.analytics.screen_service", bundle2);
            }
        }
    }

    public final void registerOnScreenChangeCallback(zza com_google_android_gms_measurement_AppMeasurement_zza) {
        if (com_google_android_gms_measurement_AppMeasurement_zza == null) {
            zzgg().zzin().log("Attempting to register null OnScreenChangeCallback");
            return;
        }
        this.zzaph.remove(com_google_android_gms_measurement_AppMeasurement_zza);
        this.zzaph.add(com_google_android_gms_measurement_AppMeasurement_zza);
    }

    public final void setCurrentScreen(Activity activity, String str, String str2) {
        zzgf();
        if (!zzgg.isMainThread()) {
            zzgg().zzin().log("setCurrentScreen must be called from the main thread");
        } else if (this.zzapi) {
            zzgg().zzin().log("Cannot call setCurrentScreen from onScreenChangeCallback");
        } else if (this.zzapd == null) {
            zzgg().zzin().log("setCurrentScreen cannot be called while no activity active");
        } else if (this.zzapg.get(activity) == null) {
            zzgg().zzin().log("setCurrentScreen must be called with an activity in the activity lifecycle");
        } else {
            if (str2 == null) {
                str2 = zzbu(activity.getClass().getCanonicalName());
            }
            boolean equals = this.zzapd.zzapa.equals(str2);
            boolean zzs = zzjv.zzs(this.zzapd.zzug, str);
            if (equals && zzs) {
                zzgg().zzio().log("setCurrentScreen cannot be called with the same class and name");
            } else if (str != null && (str.length() <= 0 || str.length() > 100)) {
                zzgg().zzin().zzg("Invalid screen name length in setCurrentScreen. Length", Integer.valueOf(str.length()));
            } else if (str2 == null || (str2.length() > 0 && str2.length() <= 100)) {
                zzgg().zzir().zze("Setting current screen to name, class", str == null ? "null" : str, str2);
                zzik com_google_android_gms_internal_measurement_zzik = new zzik(str, str2, zzgc().zzkt());
                this.zzapg.put(activity, com_google_android_gms_internal_measurement_zzik);
                zza(activity, com_google_android_gms_internal_measurement_zzik, true);
            } else {
                zzgg().zzin().zzg("Invalid class name length in setCurrentScreen. Length", Integer.valueOf(str2.length()));
            }
        }
    }

    public final void unregisterOnScreenChangeCallback(zza com_google_android_gms_measurement_AppMeasurement_zza) {
        this.zzaph.remove(com_google_android_gms_measurement_AppMeasurement_zza);
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    final zzik zze(Activity activity) {
        Preconditions.checkNotNull(activity);
        zzik com_google_android_gms_internal_measurement_zzik = (zzik) this.zzapg.get(activity);
        if (com_google_android_gms_internal_measurement_zzik != null) {
            return com_google_android_gms_internal_measurement_zzik;
        }
        zzik com_google_android_gms_internal_measurement_zzik2 = new zzik(null, zzbu(activity.getClass().getCanonicalName()), zzgc().zzkt());
        this.zzapg.put(activity, com_google_android_gms_internal_measurement_zzik2);
        return com_google_android_gms_internal_measurement_zzik2;
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

    public final zzik zzkk() {
        zzch();
        zzab();
        return this.zzapc;
    }

    public final zzig zzkl() {
        zzig com_google_android_gms_internal_measurement_zzig = this.zzapd;
        return com_google_android_gms_internal_measurement_zzig == null ? null : new zzig(com_google_android_gms_internal_measurement_zzig);
    }
}
