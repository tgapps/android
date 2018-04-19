package com.google.firebase.components;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class zze implements zzf {
    private zze() {
    }

    private static Bundle zzd(Context context) {
        Bundle bundle = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                Log.w("ComponentDiscovery", "Context has no PackageManager.");
            } else {
                ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName(context, ComponentDiscoveryService.class), 128);
                if (serviceInfo == null) {
                    Log.w("ComponentDiscovery", "ComponentDiscoveryService has no service info.");
                } else {
                    bundle = serviceInfo.metaData;
                }
            }
        } catch (NameNotFoundException e) {
            Log.w("ComponentDiscovery", "Application info not found.");
        }
        return bundle;
    }

    public final List<String> zzc(Context context) {
        Bundle zzd = zzd(context);
        if (zzd == null) {
            Log.w("ComponentDiscovery", "Could not retrieve metadata, returning empty list of registrars.");
            return Collections.emptyList();
        }
        List<String> arrayList = new ArrayList();
        for (String str : zzd.keySet()) {
            if ("com.google.firebase.components.ComponentRegistrar".equals(zzd.get(str)) && str.startsWith("com.google.firebase.components:")) {
                arrayList.add(str.substring(31));
            }
        }
        return arrayList;
    }
}
