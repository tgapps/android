package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;

final class zzej implements Runnable {
    private final /* synthetic */ zzef zzasp;

    zzej(zzef com_google_android_gms_measurement_internal_zzef) {
        this.zzasp = com_google_android_gms_measurement_internal_zzef;
    }

    public final void run() {
        zzdr com_google_android_gms_measurement_internal_zzdr = this.zzasp.zzasg;
        Context context = this.zzasp.zzasg.getContext();
        this.zzasp.zzasg.zzgr();
        com_google_android_gms_measurement_internal_zzdr.onServiceDisconnected(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
