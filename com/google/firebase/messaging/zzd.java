package com.google.firebase.messaging;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.measurement.AppMeasurement;

final class zzd {
    private static void zzb(Context context, String str, Intent intent) {
        Bundle bundle = new Bundle();
        String stringExtra = intent.getStringExtra("google.c.a.c_id");
        if (stringExtra != null) {
            bundle.putString("_nmid", stringExtra);
        }
        stringExtra = intent.getStringExtra("google.c.a.c_l");
        if (stringExtra != null) {
            bundle.putString("_nmn", stringExtra);
        }
        Object stringExtra2 = intent.getStringExtra("google.c.a.m_l");
        if (!TextUtils.isEmpty(stringExtra2)) {
            bundle.putString("label", stringExtra2);
        }
        stringExtra = intent.getStringExtra("from");
        if (stringExtra == null || !stringExtra.startsWith("/topics/")) {
            stringExtra = null;
        }
        if (stringExtra != null) {
            bundle.putString("_nt", stringExtra);
        }
        try {
            bundle.putInt("_nmt", Integer.parseInt(intent.getStringExtra("google.c.a.ts")));
        } catch (Throwable e) {
            Log.w("FirebaseMessaging", "Error while parsing timestamp in GCM event", e);
        }
        if (intent.hasExtra("google.c.a.udt")) {
            try {
                bundle.putInt("_ndt", Integer.parseInt(intent.getStringExtra("google.c.a.udt")));
            } catch (Throwable e2) {
                Log.w("FirebaseMessaging", "Error while parsing use_device_time in GCM event", e2);
            }
        }
        if (Log.isLoggable("FirebaseMessaging", 3)) {
            stringExtra = String.valueOf(bundle);
            StringBuilder stringBuilder = new StringBuilder((22 + String.valueOf(str).length()) + String.valueOf(stringExtra).length());
            stringBuilder.append("Sending event=");
            stringBuilder.append(str);
            stringBuilder.append(" params=");
            stringBuilder.append(stringExtra);
            Log.d("FirebaseMessaging", stringBuilder.toString());
        }
        AppMeasurement zzx = zzx(context);
        if (zzx != null) {
            zzx.logEventInternal(AppMeasurement.FCM_ORIGIN, str, bundle);
        } else {
            Log.w("FirebaseMessaging", "Unable to log event: analytics library is missing");
        }
    }

    public static void zzc(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("google.c.a.abt");
        if (stringExtra != null) {
            zzc.zza(context, AppMeasurement.FCM_ORIGIN, Base64.decode(stringExtra, 0), new zzb(), 1);
        }
        zzb(context, "_nr", intent);
    }

    public static void zzd(Context context, Intent intent) {
        if (intent != null) {
            if ("1".equals(intent.getStringExtra("google.c.a.tc"))) {
                AppMeasurement zzx = zzx(context);
                if (Log.isLoggable("FirebaseMessaging", 3)) {
                    Log.d("FirebaseMessaging", "Received event with track-conversion=true. Setting user property and reengagement event");
                }
                if (zzx != null) {
                    String stringExtra = intent.getStringExtra("google.c.a.c_id");
                    zzx.setUserPropertyInternal(AppMeasurement.FCM_ORIGIN, "_ln", stringExtra);
                    Bundle bundle = new Bundle();
                    bundle.putString("source", "Firebase");
                    bundle.putString("medium", "notification");
                    bundle.putString("campaign", stringExtra);
                    zzx.logEventInternal(AppMeasurement.FCM_ORIGIN, "_cmp", bundle);
                } else {
                    Log.w("FirebaseMessaging", "Unable to set user property for conversion tracking:  analytics library is missing");
                }
            } else if (Log.isLoggable("FirebaseMessaging", 3)) {
                Log.d("FirebaseMessaging", "Received event with track-conversion=false. Do not set user property");
            }
        }
        zzb(context, "_no", intent);
    }

    public static void zze(Context context, Intent intent) {
        zzb(context, "_nd", intent);
    }

    public static void zzf(Context context, Intent intent) {
        zzb(context, "_nf", intent);
    }

    private static AppMeasurement zzx(Context context) {
        try {
            return AppMeasurement.getInstance(context);
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }
}
