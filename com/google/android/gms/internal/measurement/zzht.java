package com.google.android.gms.internal.measurement;

import com.google.android.gms.measurement.AppMeasurement.ConditionalUserProperty;

final class zzht implements Runnable {
    private final /* synthetic */ zzhl zzaog;
    private final /* synthetic */ ConditionalUserProperty zzaol;

    zzht(zzhl com_google_android_gms_internal_measurement_zzhl, ConditionalUserProperty conditionalUserProperty) {
        this.zzaog = com_google_android_gms_internal_measurement_zzhl;
        this.zzaol = conditionalUserProperty;
    }

    public final void run() {
        this.zzaog.zzc(this.zzaol);
    }
}
