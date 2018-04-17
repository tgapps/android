package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

public class LifecycleActivity {
    private final Object zzkz;

    public final boolean zzbv() {
        return this.zzkz instanceof FragmentActivity;
    }

    public final boolean zzbw() {
        return this.zzkz instanceof Activity;
    }

    public final Activity zzbx() {
        return (Activity) this.zzkz;
    }

    public final FragmentActivity zzby() {
        return (FragmentActivity) this.zzkz;
    }
}
