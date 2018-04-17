package com.google.android.gms.internal.measurement;

import android.content.ComponentName;

final class zzjd implements Runnable {
    private final /* synthetic */ zziz zzaqi;

    zzjd(zziz com_google_android_gms_internal_measurement_zziz) {
        this.zzaqi = com_google_android_gms_internal_measurement_zziz;
    }

    public final void run() {
        this.zzaqi.zzapy.onServiceDisconnected(new ComponentName(this.zzaqi.zzapy.getContext(), "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
