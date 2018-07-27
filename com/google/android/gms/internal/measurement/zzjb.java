package com.google.android.gms.internal.measurement;

import android.content.ComponentName;
import android.content.Context;

final class zzjb implements Runnable {
    private final /* synthetic */ zzix zzapw;

    zzjb(zzix com_google_android_gms_internal_measurement_zzix) {
        this.zzapw = com_google_android_gms_internal_measurement_zzix;
    }

    public final void run() {
        zzij com_google_android_gms_internal_measurement_zzij = this.zzapw.zzapn;
        Context context = this.zzapw.zzapn.getContext();
        this.zzapw.zzapn.zzgi();
        com_google_android_gms_internal_measurement_zzij.onServiceDisconnected(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
