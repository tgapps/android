package com.google.firebase.analytics.connector;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.connector.internal.zzb;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnalyticsConnectorImpl implements AnalyticsConnector {
    private static volatile AnalyticsConnector zzbof;
    private final AppMeasurement zzboe;
    final Map<String, Object> zzbog = new ConcurrentHashMap();

    private AnalyticsConnectorImpl(AppMeasurement appMeasurement) {
        Preconditions.checkNotNull(appMeasurement);
        this.zzboe = appMeasurement;
    }

    public static AnalyticsConnector getInstance(Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzbof == null) {
            synchronized (AnalyticsConnector.class) {
                if (zzbof == null) {
                    zzbof = new AnalyticsConnectorImpl(AppMeasurement.getInstance(context));
                }
            }
        }
        return zzbof;
    }

    public void logEvent(String str, String str2, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (!zzb.zzfd(str)) {
            String str3 = "FA-C";
            String str4 = "Origin not allowed : ";
            String valueOf = String.valueOf(str);
            Log.d(str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
        } else if (!zzb.zza(str2, bundle)) {
            Log.d("FA-C", "Event or Params not allowed");
        } else if (zzb.zzb(str, str2, bundle)) {
            this.zzboe.logEventInternal(str, str2, bundle);
        } else {
            Log.d("FA-C", "Campaign events not allowed");
        }
    }

    public void setUserProperty(String str, String str2, Object obj) {
        String str3;
        String str4;
        String valueOf;
        if (!zzb.zzfd(str)) {
            str3 = "FA-C";
            str4 = "Origin not allowed : ";
            valueOf = String.valueOf(str);
            Log.d(str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
        } else if (!zzb.zzfe(str2)) {
            str3 = "FA-C";
            str4 = "User Property not allowed : ";
            valueOf = String.valueOf(str2);
            Log.d(str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
        } else if ((!str2.equals("_ce1") && !str2.equals("_ce2")) || str.equals(AppMeasurement.FCM_ORIGIN) || str.equals("frc")) {
            this.zzboe.setUserPropertyInternal(str, str2, obj);
        } else {
            str3 = "FA-C";
            str4 = "User Property not allowed for this origin: ";
            valueOf = String.valueOf(str2);
            Log.d(str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
        }
    }
}
