package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.common.util.PlatformVersion;
import javax.annotation.concurrent.GuardedBy;

public final class FirebaseInstanceIdReceiver extends WakefulBroadcastReceiver {
    private static boolean zzaw = false;
    @GuardedBy("FirebaseInstanceIdReceiver.class")
    private static zzh zzax;
    @GuardedBy("FirebaseInstanceIdReceiver.class")
    private static zzh zzay;

    private static synchronized zzh zza(Context context, String str) {
        zzh com_google_firebase_iid_zzh;
        synchronized (FirebaseInstanceIdReceiver.class) {
            if ("com.google.firebase.MESSAGING_EVENT".equals(str)) {
                if (zzay == null) {
                    zzay = new zzh(context, str);
                }
                com_google_firebase_iid_zzh = zzay;
            } else {
                if (zzax == null) {
                    zzax = new zzh(context, str);
                }
                com_google_firebase_iid_zzh = zzax;
            }
        }
        return com_google_firebase_iid_zzh;
    }

    private final void zza(Context context, Intent intent, String str) {
        int i;
        intent.setComponent(null);
        intent.setPackage(context.getPackageName());
        if (VERSION.SDK_INT <= 18) {
            intent.removeCategory(context.getPackageName());
        }
        String stringExtra = intent.getStringExtra("gcm.rawData64");
        if (stringExtra != null) {
            intent.putExtra("rawData", Base64.decode(stringExtra, 0));
            intent.removeExtra("gcm.rawData64");
        }
        if ("google.com/iid".equals(intent.getStringExtra("from")) || "com.google.firebase.INSTANCE_ID_EVENT".equals(str)) {
            stringExtra = "com.google.firebase.INSTANCE_ID_EVENT";
        } else if ("com.google.android.c2dm.intent.RECEIVE".equals(str) || "com.google.firebase.MESSAGING_EVENT".equals(str)) {
            stringExtra = "com.google.firebase.MESSAGING_EVENT";
        } else {
            Log.d("FirebaseInstanceId", "Unexpected intent");
            stringExtra = null;
        }
        if (stringExtra != null) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String str2 = "FirebaseInstanceId";
                String str3 = "Starting service: ";
                String valueOf = String.valueOf(stringExtra);
                Log.d(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            }
            i = (!PlatformVersion.isAtLeastO() || context.getApplicationInfo().targetSdkVersion < 26) ? 0 : 1;
            if (i != 0) {
                if (isOrderedBroadcast()) {
                    setResultCode(-1);
                }
                zza(context, stringExtra).zza(intent, goAsync());
                i = -1;
            } else {
                i = zzat.zzah().zzb(context, stringExtra, intent);
            }
        } else {
            i = -1;
        }
        if (isOrderedBroadcast()) {
            setResultCode(i);
        }
    }

    public final void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Parcelable parcelableExtra = intent.getParcelableExtra("wrapped_intent");
            Intent intent2 = parcelableExtra instanceof Intent ? (Intent) parcelableExtra : null;
            if (intent2 != null) {
                zza(context, intent2, intent.getAction());
            } else {
                zza(context, intent, intent.getAction());
            }
        }
    }
}
