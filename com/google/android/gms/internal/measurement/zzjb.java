package com.google.android.gms.internal.measurement;

import android.content.ComponentName;

final class zzjb implements Runnable {
    private final /* synthetic */ ComponentName val$name;
    private final /* synthetic */ zziz zzaqi;

    zzjb(zziz com_google_android_gms_internal_measurement_zziz, ComponentName componentName) {
        this.zzaqi = com_google_android_gms_internal_measurement_zziz;
        this.val$name = componentName;
    }

    public final void run() {
        this.zzaqi.zzapy.onServiceDisconnected(this.val$name);
    }
}
