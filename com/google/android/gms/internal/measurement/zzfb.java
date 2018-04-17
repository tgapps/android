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
        zzab();
        if (zzgi().zzd(this.zztd, zzew.zzaib) && !this.zzacr.isEnabled()) {
            return null;
        }
        try {
            return FirebaseInstanceId.getInstance().getId();
        } catch (IllegalStateException e) {
            zzgg().zzin().log("Failed to retrieve Firebase Instance Id");
            return null;
        }
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
        boolean z;
        boolean booleanValue;
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
            r0.zzaif = r0.zzacr.zzgc().zzd(getContext(), getContext().getPackageName());
        }
        long j = r0.zzaif;
        boolean isEnabled = r0.zzacr.isEnabled();
        boolean z2 = true;
        boolean z3 = zzgh().zzakm ^ 1;
        String zzgl = zzgl();
        zzch();
        long zzju = r0.zzacr.zzju();
        int zzij = zzij();
        Boolean zzas = zzgi().zzas("google_analytics_adid_collection_enabled");
        if (zzas != null) {
            if (!zzas.booleanValue()) {
                z = false;
                booleanValue = Boolean.valueOf(z).booleanValue();
                zzas = zzgi().zzas("google_analytics_ssaid_collection_enabled");
                if (zzas != null) {
                    if (zzas.booleanValue()) {
                        z2 = false;
                    }
                }
                return new zzec(zzah, gmpAppId, str2, zzii, str3, 12451, j, str, isEnabled, z3, zzgl, 0, zzju, zzij, booleanValue, Boolean.valueOf(z2).booleanValue(), zzgh().zzja());
            }
        }
        z = true;
        booleanValue = Boolean.valueOf(z).booleanValue();
        zzas = zzgi().zzas("google_analytics_ssaid_collection_enabled");
        if (zzas != null) {
            if (zzas.booleanValue()) {
                z2 = false;
            }
        }
        return new zzec(zzah, gmpAppId, str2, zzii, str3, 12451, j, str, isEnabled, z3, zzgl, 0, zzju, zzij, booleanValue, Boolean.valueOf(z2).booleanValue(), zzgh().zzja());
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
        String str;
        Status initialize;
        int i;
        Boolean zzas;
        zzfi zzip;
        String str2 = "unknown";
        String str3 = "Unknown";
        String str4 = "Unknown";
        String packageName = getContext().getPackageName();
        PackageManager packageManager = getContext().getPackageManager();
        int i2 = Integer.MIN_VALUE;
        if (packageManager == null) {
            zzgg().zzil().zzg("PackageManager is null, app identity information might be inaccurate. appId", zzfg.zzbh(packageName));
        } else {
            try {
                str2 = packageManager.getInstallerPackageName(packageName);
            } catch (IllegalArgumentException e) {
                zzgg().zzil().zzg("Error retrieving app installer package name. appId", zzfg.zzbh(packageName));
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
                    str = packageInfo.versionName;
                    try {
                        i2 = packageInfo.versionCode;
                        str3 = str;
                    } catch (NameNotFoundException e2) {
                        str3 = str;
                        zzgg().zzil().zze("Error retrieving package info. appId, appName", zzfg.zzbh(packageName), str4);
                        this.zztd = packageName;
                        this.zzado = str2;
                        this.zztc = str3;
                        this.zzaie = i2;
                        this.zztb = str4;
                        this.zzaif = 0;
                        initialize = GoogleServices.initialize(getContext());
                        i = 1;
                        if (initialize == null) {
                        }
                        if (i2 == 0) {
                            if (initialize == null) {
                                zzgg().zzil().log("GoogleService failed to initialize (no status)");
                            } else {
                                zzgg().zzil().zze("GoogleService failed to initialize, status", Integer.valueOf(initialize.getStatusCode()), initialize.getStatusMessage());
                            }
                        }
                        if (i2 != 0) {
                            zzas = zzgi().zzas("firebase_analytics_collection_enabled");
                            if (zzgi().zzhi()) {
                                zzip = zzgg().zzip();
                                str = "Collection disabled with firebase_analytics_collection_deactivated=1";
                            } else {
                                if (zzas == null) {
                                }
                                if (zzas == null) {
                                }
                                zzgg().zzir().log("Collection enabled");
                                this.zzadh = TtmlNode.ANONYMOUS_REGION_ID;
                                this.zzads = 0;
                                str2 = GoogleServices.getGoogleAppId();
                                if (TextUtils.isEmpty(str2)) {
                                    str2 = TtmlNode.ANONYMOUS_REGION_ID;
                                }
                                this.zzadh = str2;
                                if (i != 0) {
                                    zzgg().zzir().zze("App package, google app id", this.zztd, this.zzadh);
                                }
                                if (VERSION.SDK_INT < 16) {
                                    this.zzaei = InstantApps.isInstantApp(getContext());
                                } else {
                                    this.zzaei = 0;
                                }
                            }
                            zzip.log(str);
                        }
                        i = 0;
                        this.zzadh = TtmlNode.ANONYMOUS_REGION_ID;
                        this.zzads = 0;
                        str2 = GoogleServices.getGoogleAppId();
                        if (TextUtils.isEmpty(str2)) {
                            str2 = TtmlNode.ANONYMOUS_REGION_ID;
                        }
                        this.zzadh = str2;
                        if (i != 0) {
                            zzgg().zzir().zze("App package, google app id", this.zztd, this.zzadh);
                        }
                        if (VERSION.SDK_INT < 16) {
                            this.zzaei = 0;
                        } else {
                            this.zzaei = InstantApps.isInstantApp(getContext());
                        }
                    }
                }
            } catch (NameNotFoundException e3) {
                zzgg().zzil().zze("Error retrieving package info. appId, appName", zzfg.zzbh(packageName), str4);
                this.zztd = packageName;
                this.zzado = str2;
                this.zztc = str3;
                this.zzaie = i2;
                this.zztb = str4;
                this.zzaif = 0;
                initialize = GoogleServices.initialize(getContext());
                i = 1;
                if (initialize == null) {
                }
                if (i2 == 0) {
                    if (initialize == null) {
                        zzgg().zzil().zze("GoogleService failed to initialize, status", Integer.valueOf(initialize.getStatusCode()), initialize.getStatusMessage());
                    } else {
                        zzgg().zzil().log("GoogleService failed to initialize (no status)");
                    }
                }
                if (i2 != 0) {
                    zzas = zzgi().zzas("firebase_analytics_collection_enabled");
                    if (zzgi().zzhi()) {
                        if (zzas == null) {
                        }
                        if (zzas == null) {
                        }
                        zzgg().zzir().log("Collection enabled");
                        this.zzadh = TtmlNode.ANONYMOUS_REGION_ID;
                        this.zzads = 0;
                        str2 = GoogleServices.getGoogleAppId();
                        if (TextUtils.isEmpty(str2)) {
                            str2 = TtmlNode.ANONYMOUS_REGION_ID;
                        }
                        this.zzadh = str2;
                        if (i != 0) {
                            zzgg().zzir().zze("App package, google app id", this.zztd, this.zzadh);
                        }
                        if (VERSION.SDK_INT < 16) {
                            this.zzaei = InstantApps.isInstantApp(getContext());
                        } else {
                            this.zzaei = 0;
                        }
                    }
                    zzip = zzgg().zzip();
                    str = "Collection disabled with firebase_analytics_collection_deactivated=1";
                    zzip.log(str);
                }
                i = 0;
                this.zzadh = TtmlNode.ANONYMOUS_REGION_ID;
                this.zzads = 0;
                str2 = GoogleServices.getGoogleAppId();
                if (TextUtils.isEmpty(str2)) {
                    str2 = TtmlNode.ANONYMOUS_REGION_ID;
                }
                this.zzadh = str2;
                if (i != 0) {
                    zzgg().zzir().zze("App package, google app id", this.zztd, this.zzadh);
                }
                if (VERSION.SDK_INT < 16) {
                    this.zzaei = 0;
                } else {
                    this.zzaei = InstantApps.isInstantApp(getContext());
                }
            }
        }
        this.zztd = packageName;
        this.zzado = str2;
        this.zztc = str3;
        this.zzaie = i2;
        this.zztb = str4;
        this.zzaif = 0;
        initialize = GoogleServices.initialize(getContext());
        i = 1;
        i2 = (initialize == null && initialize.isSuccess()) ? 1 : 0;
        if (i2 == 0) {
            if (initialize == null) {
                zzgg().zzil().log("GoogleService failed to initialize (no status)");
            } else {
                zzgg().zzil().zze("GoogleService failed to initialize, status", Integer.valueOf(initialize.getStatusCode()), initialize.getStatusMessage());
            }
        }
        if (i2 != 0) {
            zzas = zzgi().zzas("firebase_analytics_collection_enabled");
            if (zzgi().zzhi()) {
                zzip = zzgg().zzip();
                str = "Collection disabled with firebase_analytics_collection_deactivated=1";
            } else if (zzas == null && !zzas.booleanValue()) {
                zzip = zzgg().zzip();
                str = "Collection disabled with firebase_analytics_collection_enabled=0";
            } else if (zzas == null || !GoogleServices.isMeasurementExplicitlyDisabled()) {
                zzgg().zzir().log("Collection enabled");
                this.zzadh = TtmlNode.ANONYMOUS_REGION_ID;
                this.zzads = 0;
                str2 = GoogleServices.getGoogleAppId();
                if (TextUtils.isEmpty(str2)) {
                    str2 = TtmlNode.ANONYMOUS_REGION_ID;
                }
                this.zzadh = str2;
                if (i != 0) {
                    zzgg().zzir().zze("App package, google app id", this.zztd, this.zzadh);
                }
                if (VERSION.SDK_INT < 16) {
                    this.zzaei = InstantApps.isInstantApp(getContext());
                } else {
                    this.zzaei = 0;
                }
            } else {
                zzip = zzgg().zzip();
                str = "Collection disabled with google_app_measurement_enable=0";
            }
            zzip.log(str);
        }
        i = 0;
        this.zzadh = TtmlNode.ANONYMOUS_REGION_ID;
        this.zzads = 0;
        try {
            str2 = GoogleServices.getGoogleAppId();
            if (TextUtils.isEmpty(str2)) {
                str2 = TtmlNode.ANONYMOUS_REGION_ID;
            }
            this.zzadh = str2;
            if (i != 0) {
                zzgg().zzir().zze("App package, google app id", this.zztd, this.zzadh);
            }
        } catch (IllegalStateException e4) {
            zzgg().zzil().zze("getGoogleAppId or isMeasurementEnabled failed with exception. appId", zzfg.zzbh(packageName), e4);
        }
        if (VERSION.SDK_INT < 16) {
            this.zzaei = 0;
        } else {
            this.zzaei = InstantApps.isInstantApp(getContext());
        }
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
