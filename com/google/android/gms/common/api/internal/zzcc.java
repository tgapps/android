package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public final class zzcc extends Fragment implements LifecycleFragment {
    private static WeakHashMap<FragmentActivity, WeakReference<zzcc>> zzla = new WeakHashMap();
    private Map<String, LifecycleCallback> zzlb = new ArrayMap();
    private int zzlc = 0;
    private Bundle zzld;

    public static zzcc zza(FragmentActivity fragmentActivity) {
        zzcc com_google_android_gms_common_api_internal_zzcc;
        WeakReference weakReference = (WeakReference) zzla.get(fragmentActivity);
        if (weakReference != null) {
            com_google_android_gms_common_api_internal_zzcc = (zzcc) weakReference.get();
            if (com_google_android_gms_common_api_internal_zzcc != null) {
                return com_google_android_gms_common_api_internal_zzcc;
            }
        }
        try {
            com_google_android_gms_common_api_internal_zzcc = (zzcc) fragmentActivity.getSupportFragmentManager().findFragmentByTag("SupportLifecycleFragmentImpl");
            if (com_google_android_gms_common_api_internal_zzcc == null || com_google_android_gms_common_api_internal_zzcc.isRemoving()) {
                com_google_android_gms_common_api_internal_zzcc = new zzcc();
                fragmentActivity.getSupportFragmentManager().beginTransaction().add(com_google_android_gms_common_api_internal_zzcc, "SupportLifecycleFragmentImpl").commitAllowingStateLoss();
            }
            zzla.put(fragmentActivity, new WeakReference(com_google_android_gms_common_api_internal_zzcc));
            return com_google_android_gms_common_api_internal_zzcc;
        } catch (Throwable e) {
            throw new IllegalStateException("Fragment with tag SupportLifecycleFragmentImpl is not a SupportLifecycleFragmentImpl", e);
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
            new Handler(Looper.getMainLooper()).post(new zzcd(this, lifecycleCallback, str));
        }
    }

    public final <T extends LifecycleCallback> T getCallbackOrNull(String str, Class<T> cls) {
        return (LifecycleCallback) cls.cast(this.zzlb.get(str));
    }

    public final /* synthetic */ Activity getLifecycleActivity() {
        return getActivity();
    }
}
