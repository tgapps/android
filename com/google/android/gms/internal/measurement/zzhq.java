package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

final class zzhq implements Runnable {
    private final /* synthetic */ AtomicReference zzaof;
    private final /* synthetic */ zzhl zzaog;

    zzhq(zzhl com_google_android_gms_internal_measurement_zzhl, AtomicReference atomicReference) {
        this.zzaog = com_google_android_gms_internal_measurement_zzhl;
        this.zzaof = atomicReference;
    }

    public final void run() {
        this.zzaog.zzfy().zza(this.zzaof);
    }
}
