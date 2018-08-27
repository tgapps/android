package com.google.android.gms.measurement.internal;

import android.content.ComponentName;

final class zzeh implements Runnable {
    private final /* synthetic */ ComponentName val$name;
    private final /* synthetic */ zzef zzasp;

    zzeh(zzef com_google_android_gms_measurement_internal_zzef, ComponentName componentName) {
        this.zzasp = com_google_android_gms_measurement_internal_zzef;
        this.val$name = componentName;
    }

    public final void run() {
        this.zzasp.zzasg.onServiceDisconnected(this.val$name);
    }
}
