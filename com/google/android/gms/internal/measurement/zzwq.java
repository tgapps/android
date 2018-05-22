package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;

final class zzwq extends ContentObserver {
    private final /* synthetic */ zzwp zzbnb;

    zzwq(zzwp com_google_android_gms_internal_measurement_zzwp, Handler handler) {
        this.zzbnb = com_google_android_gms_internal_measurement_zzwp;
        super(null);
    }

    public final void onChange(boolean z) {
        this.zzbnb.zzru();
        this.zzbnb.zzrw();
    }
}
