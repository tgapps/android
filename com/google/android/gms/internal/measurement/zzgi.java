package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import java.lang.Thread.UncaughtExceptionHandler;

final class zzgi implements UncaughtExceptionHandler {
    private final String zzaly;
    private final /* synthetic */ zzgg zzalz;

    public zzgi(zzgg com_google_android_gms_internal_measurement_zzgg, String str) {
        this.zzalz = com_google_android_gms_internal_measurement_zzgg;
        Preconditions.checkNotNull(str);
        this.zzaly = str;
    }

    public final synchronized void uncaughtException(Thread thread, Throwable th) {
        this.zzalz.zzge().zzim().zzg(this.zzaly, th);
    }
}
