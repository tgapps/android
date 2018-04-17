package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public final class zzbr extends Fragment implements LifecycleFragment {
    private static WeakHashMap<Activity, WeakReference<zzbr>> zzla = new WeakHashMap();
    private Map<String, LifecycleCallback> zzlb = new ArrayMap();
    private int zzlc = 0;
    private Bundle zzld;

    public static zzbr zzc(Activity activity) {
        zzbr com_google_android_gms_common_api_internal_zzbr;
        WeakReference weakReference = (WeakReference) zzla.get(activity);
        if (weakReference != null) {
            com_google_android_gms_common_api_internal_zzbr = (zzbr) weakReference.get();
            if (com_google_android_gms_common_api_internal_zzbr != null) {
                return com_google_android_gms_common_api_internal_zzbr;
            }
        }
        try {
            com_google_android_gms_common_api_internal_zzbr = (zzbr) activity.getFragmentManager().findFragmentByTag("LifecycleFragmentImpl");
            if (com_google_android_gms_common_api_internal_zzbr == null || com_google_android_gms_common_api_internal_zzbr.isRemoving()) {
                com_google_android_gms_common_api_internal_zzbr = new zzbr();
                activity.getFragmentManager().beginTransaction().add(com_google_android_gms_common_api_internal_zzbr, "LifecycleFragmentImpl").commitAllowingStateLoss();
            }
            zzla.put(activity, new WeakReference(com_google_android_gms_common_api_internal_zzbr));
            return com_google_android_gms_common_api_internal_zzbr;
        } catch (Throwable e) {
            throw new IllegalStateException("Fragment with tag LifecycleFragmentImpl is not a LifecycleFragmentImpl", e);
        }
    }

    public final void addCallback(String str, LifecycleCallback lifecycleCallback) {
        if (this.zzlb.containsKey(str)) {
            StringBuilder stringBuilder = new StringBuilder(59 + String.valueOf(str).length());
            stringBuilder.append("LifecycleCallback with tag ");
            stringBuilder.append(str);
            stringBuilder.append(" already added to this fragment.");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.zzlb.put(str, lifecycleCallback);
        if (this.zzlc > 0) {
            new Handler(Looper.getMainLooper()).post(new zzbs(this, lifecycleCallback, str));
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        for (LifecycleCallback dump : this.zzlb.values()) {
            dump.dump(str, fileDescriptor, printWriter, strArr);
        }
    }

    public final <T extends LifecycleCallback> T getCallbackOrNull(String str, Class<T> cls) {
        return (LifecycleCallback) cls.cast(this.zzlb.get(str));
    }

    public final Activity getLifecycleActivity() {
        return getActivity();
    }

    public final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (LifecycleCallback onActivityResult : this.zzlb.values()) {
            onActivityResult.onActivityResult(i, i2, intent);
        }
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzlc = 1;
        this.zzld = bundle;
        for (Entry entry : this.zzlb.entrySet()) {
            ((LifecycleCallback) entry.getValue()).onCreate(bundle != null ? bundle.getBundle((String) entry.getKey()) : null);
        }
    }

    public final void onDestroy() {
        super.onDestroy();
        this.zzlc = 5;
        for (LifecycleCallback onDestroy : this.zzlb.values()) {
            onDestroy.onDestroy();
        }
    }

    public final void onResume() {
        super.onResume();
        this.zzlc = 3;
        for (LifecycleCallback onResume : this.zzlb.values()) {
            onResume.onResume();
        }
    }

    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            for (Entry entry : this.zzlb.entrySet()) {
                Bundle bundle2 = new Bundle();
                ((LifecycleCallback) entry.getValue()).onSaveInstanceState(bundle2);
                bundle.putBundle((String) entry.getKey(), bundle2);
            }
        }
    }

    public final void onStart() {
        super.onStart();
        this.zzlc = 2;
        for (LifecycleCallback onStart : this.zzlb.values()) {
            onStart.onStart();
        }
    }

    public final void onStop() {
        super.onStop();
        this.zzlc = 4;
        for (LifecycleCallback onStop : this.zzlb.values()) {
            onStop.onStop();
        }
    }
}
