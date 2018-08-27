package com.google.android.gms.measurement.internal;

import com.google.android.gms.measurement.AppMeasurement.ConditionalUserProperty;

final class zzdb implements Runnable {
    private final /* synthetic */ zzcs zzarc;
    private final /* synthetic */ ConditionalUserProperty zzarj;

    zzdb(zzcs com_google_android_gms_measurement_internal_zzcs, ConditionalUserProperty conditionalUserProperty) {
        this.zzarc = com_google_android_gms_measurement_internal_zzcs;
        this.zzarj = conditionalUserProperty;
    }

    public final void run() {
        this.zzarc.zzc(this.zzarj);
    }
}
