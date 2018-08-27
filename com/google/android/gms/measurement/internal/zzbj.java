package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;

public final class zzbj {
    private final zzbm zzaoi;

    public zzbj(zzbm com_google_android_gms_measurement_internal_zzbm) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzbm);
        this.zzaoi = com_google_android_gms_measurement_internal_zzbm;
    }

    public static boolean zza(Context context) {
        Preconditions.checkNotNull(context);
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            ActivityInfo receiverInfo = packageManager.getReceiverInfo(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementReceiver"), 0);
            if (receiverInfo == null || !receiverInfo.enabled) {
                return false;
            }
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public final void onReceive(Context context, Intent intent) {
        zzbt zza = zzbt.zza(context, null);
        zzap zzgo = zza.zzgo();
        if (intent == null) {
            zzgo.zzjg().zzbx("Receiver called with null intent");
            return;
        }
        zza.zzgr();
        String action = intent.getAction();
        zzgo.zzjl().zzg("Local receiver got", action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            Intent className = new Intent().setClassName(context, "com.google.android.gms.measurement.AppMeasurementService");
            className.setAction("com.google.android.gms.measurement.UPLOAD");
            zzgo.zzjl().zzbx("Starting wakeful intent.");
            this.zzaoi.doStartService(context, className);
        } else if ("com.android.vending.INSTALL_REFERRER".equals(action)) {
            try {
                zza.zzgn().zzc(new zzbk(this, zza, zzgo));
            } catch (Exception e) {
                zzgo.zzjg().zzg("Install Referrer Reporter encountered a problem", e);
            }
            PendingResult doGoAsync = this.zzaoi.doGoAsync();
            action = intent.getStringExtra("referrer");
            if (action == null) {
                zzgo.zzjl().zzbx("Install referrer extras are null");
                if (doGoAsync != null) {
                    doGoAsync.finish();
                    return;
                }
                return;
            }
            zzgo.zzjj().zzg("Install referrer extras are", action);
            if (!action.contains("?")) {
                String str = "?";
                action = String.valueOf(action);
                action = action.length() != 0 ? str.concat(action) : new String(str);
            }
            Bundle zza2 = zza.zzgm().zza(Uri.parse(action));
            if (zza2 == null) {
                zzgo.zzjl().zzbx("No campaign defined in install referrer broadcast");
                if (doGoAsync != null) {
                    doGoAsync.finish();
                    return;
                }
                return;
            }
            long longExtra = 1000 * intent.getLongExtra("referrer_timestamp_seconds", 0);
            if (longExtra == 0) {
                zzgo.zzjg().zzbx("Install referrer is missing timestamp");
            }
            zza.zzgn().zzc(new zzbl(this, zza, longExtra, zza2, context, zzgo, doGoAsync));
        }
    }
}
