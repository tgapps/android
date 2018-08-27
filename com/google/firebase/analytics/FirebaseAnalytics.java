package com.google.firebase.analytics;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Keep;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.measurement.internal.zzbt;
import com.google.android.gms.measurement.internal.zzk;
import com.google.firebase.iid.FirebaseInstanceId;

public final class FirebaseAnalytics {
    private static volatile FirebaseAnalytics zzbsa;
    private final zzbt zzadj;
    private final Object zzbsd = new Object();

    @Keep
    public static FirebaseAnalytics getInstance(Context context) {
        if (zzbsa == null) {
            synchronized (FirebaseAnalytics.class) {
                if (zzbsa == null) {
                    zzbsa = new FirebaseAnalytics(zzbt.zza(context, null));
                }
            }
        }
        return zzbsa;
    }

    @Keep
    public final void setCurrentScreen(Activity activity, String str, String str2) {
        if (zzk.isMainThread()) {
            this.zzadj.zzgh().setCurrentScreen(activity, str, str2);
        } else {
            this.zzadj.zzgo().zzjg().zzbx("setCurrentScreen must be called from the main thread");
        }
    }

    private FirebaseAnalytics(zzbt com_google_android_gms_measurement_internal_zzbt) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzbt);
        this.zzadj = com_google_android_gms_measurement_internal_zzbt;
    }

    @Keep
    public final String getFirebaseInstanceId() {
        return FirebaseInstanceId.getInstance().getId();
    }
}
