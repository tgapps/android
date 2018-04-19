package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.wrappers.InstantApps;
import com.google.firebase.iid.FirebaseInstanceId;

public final class zzfb extends zzhk {
    private String zzadh;
    private String zzado;
    private long zzads;
    private int zzaei;
    private int zzaie;
    private long zzaif;
    private String zztb;
    private String zztc;
    private String zztd;

    zzfb(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final String zzgl() {
        String str = null;
        zzab();
        if (!zzgi().zzd(this.zztd, zzew.zzaib) || this.zzacr.isEnabled()) {
            try {
                str = FirebaseInstanceId.getInstance().getId();
            } catch (IllegalStateException e) {
                zzgg().zzin().log("Failed to retrieve Firebase Instance Id");
            }
        }
        return str;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    final String getGmpAppId() {
        zzch();
        return this.zzadh;
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    final String zzah() {
        zzch();
        return this.zztd;
    }

    final zzec zzbd(String str) {
        zzab();
        String zzah = zzah();
        String gmpAppId = getGmpAppId();
        zzch();
        String str2 = this.zztc;
        long zzii = (long) zzii();
        zzch();
        String str3 = this.zzado;
        zzch();
        zzab();
        if (this.zzaif == 0) {
            this.zzaif = this.zzacr.zzgc().zzd(getContext(), getContext().getPackageName());
        }
        long j = this.zzaif;
        boolean isEnabled = this.zzacr.isEnabled();
        boolean z = !zzgh().zzakm;
        String zzgl = zzgl();
        zzch();
        long zzju = this.zzacr.zzju();
        int zzij = zzij();
        Boolean zzas = zzgi().zzas("google_analytics_adid_collection_enabled");
        boolean z2 = zzas == null || zzas.booleanValue();
        boolean booleanValue = Boolean.valueOf(z2).booleanValue();
        zzas = zzgi().zzas("google_analytics_ssaid_collection_enabled");
        z2 = zzas == null || zzas.booleanValue();
        return new zzec(zzah, gmpAppId, str2, zzii, str3, 12451, j, str, isEnabled, z, zzgl, 0, zzju, zzij, booleanValue, Boolean.valueOf(z2).booleanValue(), zzgh().zzja());
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
        return true;
    }

    protected final void zzig() {
        int i = 1;
        String str = "unknown";
        String str2 = "Unknown";
        int i2 = Integer.MIN_VALUE;
        String str3 = "Unknown";
        String packageName = getContext().getPackageName();
        PackageManager packageManager = getContext().getPackageManager();
        if (packageManager == null) {
            zzgg().zzil().zzg("PackageManager is null, app identity information might be inaccurate. appId", zzfg.zzbh(packageName));
        } else {
            try {
                str = packageManager.getInstallerPackageName(packageName);
            } catch (IllegalArgumentException e) {
                zzgg().zzil().zzg("Error retrieving app installer package name. appId", zzfg.zzbh(packageName));
            }
            if (str == null) {
                str = "manual_install";
            } else if ("com.android.vending".equals(str)) {
                str = TtmlNode.ANONYMOUS_REGION_ID;
            }
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(getContext().getPackageName(), 0);
                if (packageInfo != null) {
                    CharSequence applicationLabel = packageManager.getApplicationLabel(packageInfo.applicationInfo);
                    if (!TextUtils.isEmpty(applicationLabel)) {
                        str3 = applicationLabel.toString();
                    }
                    str2 = packageInfo.versionName;
                    i2 = packageInfo.versionCode;
                }
            } catch (NameNotFoundException e2) {
                zzgg().zzil().zze("Error retrieving package info. appId, appName", zzfg.zzbh(packageName), str3);
            }
        }
        this.zztd = packageName;
        this.zzado = str;
        this.zztc = str2;
        this.zzaie = i2;
        this.zztb = str3;
        this.zzaif = 0;
        Status initialize = GoogleServices.initialize(getContext());
        int i3 = (initialize == null || !initialize.isSuccess()) ? 0 : 1;
        if (i3 == 0) {
            if (initialize == null) {
                zzgg().zzil().log("GoogleService failed to initialize (no status)");
            } else {
                zzgg().zzil().zze("GoogleService failed to initialize, status", Integer.valueOf(initialize.getStatusCode()), initialize.getStatusMessage());
            }
        }
        if (i3 != 0) {
            Boolean zzas = zzgi().zzas("firebase_analytics_collection_enabled");
            if (zzgi().zzhi()) {
                zzgg().zzip().log("Collection disabled with firebase_analytics_collection_deactivated=1");
                i3 = 0;
            } else if (zzas != null && !zzas.booleanValue()) {
                zzgg().zzip().log("Collection disabled with firebase_analytics_collection_enabled=0");
                i3 = 0;
            } else if (zzas == null && GoogleServices.isMeasurementExplicitlyDisabled()) {
                zzgg().zzip().log("Collection disabled with google_app_measurement_enable=0");
                i3 = 0;
            } else {
                zzgg().zzir().log("Collection enabled");
                i3 = 1;
            }
        } else {
            i3 = 0;
        }
        this.zzadh = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzads = 0;
        try {
            String googleAppId = GoogleServices.getGoogleAppId();
            if (TextUtils.isEmpty(googleAppId)) {
                googleAppId = TtmlNode.ANONYMOUS_REGION_ID;
            }
            this.zzadh = googleAppId;
            if (i3 != 0) {
                zzgg().zzir().zze("App package, google app id", this.zztd, this.zzadh);
            }
        } catch (IllegalStateException e3) {
            zzgg().zzil().zze("getGoogleAppId or isMeasurementEnabled failed with exception. appId", zzfg.zzbh(packageName), e3);
        }
        if (VERSION.SDK_INT >= 16) {
            if (!InstantApps.isInstantApp(getContext())) {
                i = 0;
            }
            this.zzaei = i;
            return;
        }
        this.zzaei = 0;
    }

    final int zzii() {
        zzch();
        return this.zzaie;
    }

    final int zzij() {
        zzch();
        return this.zzaei;
    }
}
