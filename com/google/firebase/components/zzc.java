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

public final class zzc {
    private final Context zza;
    private final zzb zzb;

    interface zzb {
        List<String> zza(Context context);
    }

    static class zza implements zzb {
        private zza() {
        }

        public final List<String> zza(Context context) {
            Bundle zzb = zzb(context);
            if (zzb == null) {
                Log.w("ComponentDiscovery", "Could not retrieve metadata, returning empty list of registrars.");
                return Collections.emptyList();
            }
            List<String> arrayList = new ArrayList();
            for (String str : zzb.keySet()) {
                if ("com.google.firebase.components.ComponentRegistrar".equals(zzb.get(str)) && str.startsWith("com.google.firebase.components:")) {
                    arrayList.add(str.substring(31));
                }
            }
            return arrayList;
        }

        private static Bundle zzb(Context context) {
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
    }

    public zzc(Context context) {
        this(context, new zza());
    }

    private zzc(Context context, zzb com_google_firebase_components_zzc_zzb) {
        this.zza = context;
        this.zzb = com_google_firebase_components_zzc_zzb;
    }

    public final List<ComponentRegistrar> zza() {
        return zza(this.zzb.zza(this.zza));
    }

    private static List<ComponentRegistrar> zza(List<String> list) {
        List<ComponentRegistrar> arrayList = new ArrayList();
        for (String cls : list) {
            try {
                Class cls2 = Class.forName(cls);
                if (ComponentRegistrar.class.isAssignableFrom(cls2)) {
                    arrayList.add((ComponentRegistrar) cls2.newInstance());
                } else {
                    Log.w("ComponentDiscovery", String.format("Class %s is not an instance of %s", new Object[]{cls, "com.google.firebase.components.ComponentRegistrar"}));
                }
            } catch (Throwable e) {
                Log.w("ComponentDiscovery", String.format("Class %s is not an found.", new Object[]{cls}), e);
            } catch (Throwable e2) {
                Log.w("ComponentDiscovery", String.format("Could not instantiate %s.", new Object[]{cls}), e2);
            } catch (Throwable e22) {
                Log.w("ComponentDiscovery", String.format("Could not instantiate %s.", new Object[]{cls}), e22);
            }
        }
        return arrayList;
    }
}
