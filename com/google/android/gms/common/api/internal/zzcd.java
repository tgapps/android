package com.google.android.gms.common.api.internal;

final class zzcd implements Runnable {
    private final /* synthetic */ LifecycleCallback zzle;
    private final /* synthetic */ String zzlf;
    private final /* synthetic */ zzcc zzly;

    zzcd(zzcc com_google_android_gms_common_api_internal_zzcc, LifecycleCallback lifecycleCallback, String str) {
        this.zzly = com_google_android_gms_common_api_internal_zzcc;
        this.zzle = lifecycleCallback;
        this.zzlf = str;
    }

    public final void run() {
        if (this.zzly.zzlc > 0) {
            this.zzle.onCreate(this.zzly.zzld != null ? this.zzly.zzld.getBundle(this.zzlf) : null);
        }
        if (this.zzly.zzlc >= 2) {
            this.zzle.onStart();
        }
        if (this.zzly.zzlc >= 3) {
            this.zzle.onResume();
        }
        if (this.zzly.zzlc >= 4) {
            this.zzle.onStop();
        }
        if (this.zzly.zzlc >= 5) {
            this.zzle.onDestroy();
        }
    }
}
