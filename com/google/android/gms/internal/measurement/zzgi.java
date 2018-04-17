package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import java.lang.Thread.UncaughtExceptionHandler;

final class zzgi implements UncaughtExceptionHandler {
    private final String zzalw;
    private final /* synthetic */ zzgg zzalx;

    public zzgi(zzgg com_google_android_gms_internal_measurement_zzgg, String str) {
        this.zzalx = com_google_android_gms_internal_measurement_zzgg;
        Preconditions.checkNotNull(str);
        this.zzalw = str;
    }

    public final synchronized void uncaughtException(Thread thread, Throwable th) {
        this.zzalx.zzgg().zzil().zzg(this.zzalw, th);
    }
}
