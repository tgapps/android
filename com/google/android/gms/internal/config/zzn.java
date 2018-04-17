package com.google.android.gms.internal.config;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.measurement.AppMeasurement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class zzn {
    private static AppMeasurement zza(Context context) {
        try {
            return AppMeasurement.getInstance(context);
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }

    public static List<zzl> zzb(Context context) {
        AppMeasurement zza = zza(context);
        if (zza == null) {
            if (Log.isLoggable("FRCAnalytics", 3)) {
                Log.d("FRCAnalytics", "Unable to get user properties: analytics library is missing.");
            }
            return null;
        }
        Map userProperties;
        try {
            userProperties = zza.getUserProperties(false);
        } catch (Throwable e) {
            if (Log.isLoggable("FRCAnalytics", 3)) {
                Log.d("FRCAnalytics", "Unable to get user properties.", e);
            }
            userProperties = null;
        }
        if (userProperties == null) {
            return null;
        }
        List<zzl> arrayList = new ArrayList();
        for (Entry entry : userProperties.entrySet()) {
            if (entry.getValue() != null) {
                arrayList.add(new zzl((String) entry.getKey(), entry.getValue().toString()));
            }
        }
        return arrayList;
    }
}
