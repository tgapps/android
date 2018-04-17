package com.google.firebase.components;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class zzc {
    private final Context mContext;
    private final zzf zzag;

    public zzc(Context context) {
        this(context, new zze());
    }

    private zzc(Context context, zzf com_google_firebase_components_zzf) {
        this.mContext = context;
        this.zzag = com_google_firebase_components_zzf;
    }

    private static List<ComponentRegistrar> zza(List<String> list) {
        Throwable e;
        String str;
        String str2;
        Object[] objArr;
        List<ComponentRegistrar> arrayList = new ArrayList();
        for (String cls : list) {
            try {
                Class cls2 = Class.forName(cls);
                if (ComponentRegistrar.class.isAssignableFrom(cls2)) {
                    arrayList.add((ComponentRegistrar) cls2.newInstance());
                } else {
                    Log.w("ComponentDiscovery", String.format("Class %s is not an instance of %s", new Object[]{cls, "com.google.firebase.components.ComponentRegistrar"}));
                }
            } catch (ClassNotFoundException e2) {
                e = e2;
                str = "ComponentDiscovery";
                str2 = "Class %s is not an found.";
                objArr = new Object[]{cls};
                Log.w(str, String.format(str2, objArr), e);
            } catch (IllegalAccessException e3) {
                e = e3;
                str = "ComponentDiscovery";
                str2 = "Could not instantiate %s.";
                objArr = new Object[]{cls};
                Log.w(str, String.format(str2, objArr), e);
            } catch (InstantiationException e4) {
                e = e4;
                str = "ComponentDiscovery";
                str2 = "Could not instantiate %s.";
                objArr = new Object[]{cls};
                Log.w(str, String.format(str2, objArr), e);
            }
        }
        return arrayList;
    }

    public final List<ComponentRegistrar> zzj() {
        return zza(this.zzag.zzc(this.mContext));
    }
}
