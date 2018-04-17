package com.google.android.gms.internal.measurement;

import com.google.android.gms.measurement.AppMeasurement.ConditionalUserProperty;

final class zzht implements Runnable {
    private final /* synthetic */ zzhm zzaop;
    private final /* synthetic */ ConditionalUserProperty zzaot;

    zzht(zzhm com_google_android_gms_internal_measurement_zzhm, ConditionalUserProperty conditionalUserProperty) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
        this.zzaot = conditionalUserProperty;
    }

    public final void run() {
        this.zzaop.zzb(this.zzaot);
    }
}
