package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.wrappers.InstantApps;

public final class zzaj extends zzf {
    private String zzafx;
    private String zzage;
    private long zzagh;
    private String zzagk;
    private int zzagy;
    private int zzalo;
    private long zzalp;
    private String zztr;
    private String zzts;
    private String zztt;

    zzaj(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
    }

    protected final boolean zzgt() {
        return true;
    }

    protected final void zzgu() {
        int i;
        CharSequence googleAppId;
        CharSequence charSequence;
        String str;
        int i2 = 1;
        String str2 = "unknown";
        String str3 = "Unknown";
        int i3 = Integer.MIN_VALUE;
        String str4 = "Unknown";
        String packageName = getContext().getPackageName();
        PackageManager packageManager = getContext().getPackageManager();
        if (packageManager == null) {
            zzgo().zzjd().zzg("PackageManager is null, app identity information might be inaccurate. appId", zzap.zzbv(packageName));
        } else {
            try {
                str2 = packageManager.getInstallerPackageName(packageName);
            } catch (IllegalArgumentException e) {
                zzgo().zzjd().zzg("Error retrieving app installer package name. appId", zzap.zzbv(packageName));
            }
            if (str2 == null) {
                str2 = "manual_install";
            } else if ("com.android.vending".equals(str2)) {
                str2 = TtmlNode.ANONYMOUS_REGION_ID;
            }
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
                if (packageInfo != null) {
                    CharSequence applicationLabel = packageManager.getApplicationLabel(packageInfo.applicationInfo);
                    if (!TextUtils.isEmpty(applicationLabel)) {
                        str4 = applicationLabel.toString();
                    }
                    str3 = packageInfo.versionName;
                    i3 = packageInfo.versionCode;
                }
            } catch (NameNotFoundException e2) {
                zzgo().zzjd().zze("Error retrieving package info. appId, appName", zzap.zzbv(packageName), str4);
            }
        }
        this.zztt = packageName;
        this.zzage = str2;
        this.zzts = str3;
        this.zzalo = i3;
        this.zztr = str4;
        this.zzalp = 0;
        zzgr();
        Status initialize = GoogleServices.initialize(getContext());
        if (initialize == null || !initialize.isSuccess()) {
            i = 0;
        } else {
            i = 1;
        }
        if (TextUtils.isEmpty(this.zzadj.zzkk()) || !"am".equals(this.zzadj.zzkl())) {
            i3 = 0;
        } else {
            i3 = 1;
        }
        i |= i3;
        if (i == 0) {
            if (initialize == null) {
                zzgo().zzjd().zzbx("GoogleService failed to initialize (no status)");
            } else {
                zzgo().zzjd().zze("GoogleService failed to initialize, status", Integer.valueOf(initialize.getStatusCode()), initialize.getStatusMessage());
            }
        }
        if (i != 0) {
            Boolean zzhv = zzgq().zzhv();
            if (zzgq().zzhu()) {
                if (this.zzadj.zzkj()) {
                    zzgo().zzjj().zzbx("Collection disabled with firebase_analytics_collection_deactivated=1");
                    i = 0;
                    this.zzafx = TtmlNode.ANONYMOUS_REGION_ID;
                    this.zzagk = TtmlNode.ANONYMOUS_REGION_ID;
                    this.zzagh = 0;
                    zzgr();
                    if (!TextUtils.isEmpty(this.zzadj.zzkk()) && "am".equals(this.zzadj.zzkl())) {
                        this.zzagk = this.zzadj.zzkk();
                    }
                    googleAppId = GoogleServices.getGoogleAppId();
                    if (TextUtils.isEmpty(googleAppId)) {
                        charSequence = googleAppId;
                    } else {
                        str = TtmlNode.ANONYMOUS_REGION_ID;
                    }
                    this.zzafx = str;
                    if (!TextUtils.isEmpty(googleAppId)) {
                        this.zzagk = new StringResourceValueReader(getContext()).getString("gma_app_id");
                    }
                    if (i != 0) {
                        zzgo().zzjl().zze("App package, google app id", this.zztt, this.zzafx);
                    }
                    if (VERSION.SDK_INT < 16) {
                        if (!InstantApps.isInstantApp(getContext())) {
                            i2 = 0;
                        }
                        this.zzagy = i2;
                    }
                    this.zzagy = 0;
                    return;
                }
            } else if (zzhv == null || zzhv.booleanValue()) {
                if (zzhv == null && GoogleServices.isMeasurementExplicitlyDisabled()) {
                    zzgo().zzjj().zzbx("Collection disabled with google_app_measurement_enable=0");
                    i = 0;
                } else {
                    zzgo().zzjl().zzbx("Collection enabled");
                    i = 1;
                }
                this.zzafx = TtmlNode.ANONYMOUS_REGION_ID;
                this.zzagk = TtmlNode.ANONYMOUS_REGION_ID;
                this.zzagh = 0;
                zzgr();
                this.zzagk = this.zzadj.zzkk();
                googleAppId = GoogleServices.getGoogleAppId();
                if (TextUtils.isEmpty(googleAppId)) {
                    charSequence = googleAppId;
                } else {
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                }
                this.zzafx = str;
                if (TextUtils.isEmpty(googleAppId)) {
                    this.zzagk = new StringResourceValueReader(getContext()).getString("gma_app_id");
                }
                if (i != 0) {
                    zzgo().zzjl().zze("App package, google app id", this.zztt, this.zzafx);
                }
                if (VERSION.SDK_INT < 16) {
                    this.zzagy = 0;
                    return;
                }
                if (InstantApps.isInstantApp(getContext())) {
                    i2 = 0;
                }
                this.zzagy = i2;
            } else if (this.zzadj.zzkj()) {
                zzgo().zzjj().zzbx("Collection disabled with firebase_analytics_collection_enabled=0");
                i = 0;
                this.zzafx = TtmlNode.ANONYMOUS_REGION_ID;
                this.zzagk = TtmlNode.ANONYMOUS_REGION_ID;
                this.zzagh = 0;
                zzgr();
                this.zzagk = this.zzadj.zzkk();
                googleAppId = GoogleServices.getGoogleAppId();
                if (TextUtils.isEmpty(googleAppId)) {
                    str = TtmlNode.ANONYMOUS_REGION_ID;
                } else {
                    charSequence = googleAppId;
                }
                this.zzafx = str;
                if (TextUtils.isEmpty(googleAppId)) {
                    this.zzagk = new StringResourceValueReader(getContext()).getString("gma_app_id");
                }
                if (i != 0) {
                    zzgo().zzjl().zze("App package, google app id", this.zztt, this.zzafx);
                }
                if (VERSION.SDK_INT < 16) {
                    if (InstantApps.isInstantApp(getContext())) {
                        i2 = 0;
                    }
                    this.zzagy = i2;
                }
                this.zzagy = 0;
                return;
            }
        }
        i = 0;
        this.zzafx = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzagk = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzagh = 0;
        zzgr();
        this.zzagk = this.zzadj.zzkk();
        try {
            googleAppId = GoogleServices.getGoogleAppId();
            if (TextUtils.isEmpty(googleAppId)) {
                charSequence = googleAppId;
            } else {
                str = TtmlNode.ANONYMOUS_REGION_ID;
            }
            this.zzafx = str;
            if (TextUtils.isEmpty(googleAppId)) {
                this.zzagk = new StringResourceValueReader(getContext()).getString("gma_app_id");
            }
            if (i != 0) {
                zzgo().zzjl().zze("App package, google app id", this.zztt, this.zzafx);
            }
        } catch (IllegalStateException e3) {
            zzgo().zzjd().zze("getGoogleAppId or isMeasurementEnabled failed with exception. appId", zzap.zzbv(packageName), e3);
        }
        if (VERSION.SDK_INT < 16) {
            this.zzagy = 0;
            return;
        }
        if (InstantApps.isInstantApp(getContext())) {
            i2 = 0;
        }
        this.zzagy = i2;
    }

    final zzh zzbr(String str) {
        String zziz;
        zzaf();
        zzgb();
        String zzal = zzal();
        String gmpAppId = getGmpAppId();
        zzcl();
        String str2 = this.zzts;
        long zzja = (long) zzja();
        zzcl();
        String str3 = this.zzage;
        long zzhc = zzgq().zzhc();
        zzcl();
        zzaf();
        if (this.zzalp == 0) {
            this.zzalp = this.zzadj.zzgm().zzd(getContext(), getContext().getPackageName());
        }
        long j = this.zzalp;
        boolean isEnabled = this.zzadj.isEnabled();
        boolean z = !zzgp().zzanv;
        zzaf();
        zzgb();
        if (!zzgq().zzbc(this.zztt) || this.zzadj.isEnabled()) {
            zziz = zziz();
        } else {
            zziz = null;
        }
        zzcl();
        long j2 = this.zzagh;
        long zzkp = this.zzadj.zzkp();
        int zzjb = zzjb();
        zzco zzgq = zzgq();
        zzgq.zzgb();
        Boolean zzau = zzgq.zzau("google_analytics_adid_collection_enabled");
        boolean z2 = zzau == null || zzau.booleanValue();
        boolean booleanValue = Boolean.valueOf(z2).booleanValue();
        zzgq = zzgq();
        zzgq.zzgb();
        zzau = zzgq.zzau("google_analytics_ssaid_collection_enabled");
        z2 = zzau == null || zzau.booleanValue();
        return new zzh(zzal, gmpAppId, str2, zzja, str3, zzhc, j, str, isEnabled, z, zziz, j2, zzkp, zzjb, booleanValue, Boolean.valueOf(z2).booleanValue(), zzgp().zzjx(), zzgw());
    }

    private final String zziz() {
        try {
            Class loadClass = getContext().getClassLoader().loadClass("com.google.firebase.analytics.FirebaseAnalytics");
            if (loadClass == null) {
                return null;
            }
            try {
                Object invoke = loadClass.getDeclaredMethod("getInstance", new Class[]{Context.class}).invoke(null, new Object[]{getContext()});
                if (invoke == null) {
                    return null;
                }
                try {
                    return (String) loadClass.getDeclaredMethod("getFirebaseInstanceId", new Class[0]).invoke(invoke, new Object[0]);
                } catch (Exception e) {
                    zzgo().zzji().zzbx("Failed to retrieve Firebase Instance Id");
                    return null;
                }
            } catch (Exception e2) {
                zzgo().zzjh().zzbx("Failed to obtain Firebase Analytics instance");
                return null;
            }
        } catch (ClassNotFoundException e3) {
            return null;
        }
    }

    final String zzal() {
        zzcl();
        return this.zztt;
    }

    final String getGmpAppId() {
        zzcl();
        return this.zzafx;
    }

    final String zzgw() {
        zzcl();
        return this.zzagk;
    }

    final int zzja() {
        zzcl();
        return this.zzalo;
    }

    final int zzjb() {
        zzcl();
        return this.zzagy;
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
