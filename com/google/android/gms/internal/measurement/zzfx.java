package com.google.android.gms.internal.measurement;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.wrappers.PackageManagerWrapper;
import com.google.android.gms.common.wrappers.Wrappers;
import java.util.List;

public final class zzfx {
    private final zzgl zzacw;
    volatile zzr zzakw;
    private ServiceConnection zzakx;

    zzfx(zzgl com_google_android_gms_internal_measurement_zzgl) {
        this.zzacw = com_google_android_gms_internal_measurement_zzgl;
    }

    private final boolean zzji() {
        try {
            PackageManagerWrapper packageManager = Wrappers.packageManager(this.zzacw.getContext());
            if (packageManager != null) {
                return packageManager.getPackageInfo("com.android.vending", 128).versionCode >= 80837300;
            } else {
                this.zzacw.zzge().zzir().log("Failed to retrieve Package Manager to check Play Store compatibility");
                return false;
            }
        } catch (Exception e) {
            this.zzacw.zzge().zzir().zzg("Failed to retrieve Play Store version", e);
            return false;
        }
    }

    final void zzc(Bundle bundle) {
        this.zzacw.zzab();
        if (bundle != null) {
            long j = bundle.getLong("install_begin_timestamp_seconds", 0) * 1000;
            if (j == 0) {
                this.zzacw.zzge().zzim().log("Service response is missing Install Referrer install timestamp");
                return;
            }
            String string = bundle.getString("install_referrer");
            if (string == null || string.isEmpty()) {
                this.zzacw.zzge().zzim().log("No referrer defined in install referrer response");
                return;
            }
            this.zzacw.zzge().zzit().zzg("InstallReferrer API result", string);
            zzka zzgb = this.zzacw.zzgb();
            String str = "?";
            string = String.valueOf(string);
            Bundle zza = zzgb.zza(Uri.parse(string.length() != 0 ? str.concat(string) : new String(str)));
            if (zza == null) {
                this.zzacw.zzge().zzim().log("No campaign params defined in install referrer result");
                return;
            }
            string = zza.getString("medium");
            Object obj = (string == null || "(not set)".equalsIgnoreCase(string) || "organic".equalsIgnoreCase(string)) ? null : 1;
            if (obj != null) {
                long j2 = bundle.getLong("referrer_click_timestamp_seconds", 0) * 1000;
                if (j2 == 0) {
                    this.zzacw.zzge().zzim().log("Install Referrer is missing click timestamp for ad campaign");
                    return;
                }
                zza.putLong("click_timestamp", j2);
            }
            if (j == this.zzacw.zzgf().zzaka.get()) {
                this.zzacw.zzge().zzit().log("Campaign has already been logged");
                return;
            }
            zza.putString("_cis", "referrer API");
            this.zzacw.zzgf().zzaka.set(j);
            this.zzacw.zzfu().logEvent("auto", "_cmp", zza);
            if (this.zzakx != null) {
                ConnectionTracker.getInstance().unbindService(this.zzacw.getContext(), this.zzakx);
            }
        }
    }

    protected final void zzjh() {
        this.zzacw.zzab();
        if (zzji()) {
            this.zzakx = new zzfz();
            this.zzacw.zzge().zzir().log("Install Referrer Reporter is initializing");
            this.zzacw.zzab();
            Intent intent = new Intent("com.google.android.finsky.BIND_GET_INSTALL_REFERRER_SERVICE");
            intent.setComponent(new ComponentName("com.android.vending", "com.google.android.finsky.externalreferrer.GetInstallReferrerService"));
            PackageManager packageManager = this.zzacw.getContext().getPackageManager();
            if (packageManager == null) {
                this.zzacw.zzge().zzip().log("Failed to obtain Package Manager to verify binding conditions");
                return;
            }
            List queryIntentServices = packageManager.queryIntentServices(intent, 0);
            if (queryIntentServices == null || queryIntentServices.isEmpty()) {
                this.zzacw.zzge().zzir().log("Play Service for fetching Install Referrer is unavailable on device");
                return;
            }
            ResolveInfo resolveInfo = (ResolveInfo) queryIntentServices.get(0);
            if (resolveInfo.serviceInfo != null) {
                String str = resolveInfo.serviceInfo.packageName;
                if (resolveInfo.serviceInfo.name == null || this.zzakx == null || !"com.android.vending".equals(str) || !zzji()) {
                    this.zzacw.zzge().zzir().log("Play Store missing or incompatible. Version 8.3.73 or later required");
                    return;
                }
                try {
                    this.zzacw.zzge().zzir().zzg("Install Referrer Service is", ConnectionTracker.getInstance().bindService(this.zzacw.getContext(), new Intent(intent), this.zzakx, 1) ? "available" : "not available");
                    return;
                } catch (Exception e) {
                    this.zzacw.zzge().zzim().zzg("Exception occurred while binding to Install Referrer Service", e.getMessage());
                    return;
                }
            }
            return;
        }
        this.zzacw.zzge().zzir().log("Install Referrer Reporter is not available");
        this.zzakx = null;
    }

    final Bundle zzjj() {
        this.zzacw.zzab();
        if (this.zzakw == null) {
            this.zzacw.zzge().zzip().log("Attempting to use Install Referrer Service while it is not initialized");
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("package_name", this.zzacw.getContext().getPackageName());
        try {
            bundle = this.zzakw.zza(bundle);
            if (bundle != null) {
                return bundle;
            }
            this.zzacw.zzge().zzim().log("Install Referrer Service returned a null response");
            return null;
        } catch (Exception e) {
            this.zzacw.zzge().zzim().zzg("Exception occurred while retrieving the Install Referrer", e.getMessage());
            return null;
        }
    }
}
