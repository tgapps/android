package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

final class zzbr<V> extends FutureTask<V> implements Comparable<zzbr> {
    private final String zzapf;
    private final /* synthetic */ zzbo zzapg;
    private final long zzaph = zzbo.zzape.getAndIncrement();
    final boolean zzapi;

    zzbr(zzbo com_google_android_gms_measurement_internal_zzbo, Callable<V> callable, boolean z, String str) {
        this.zzapg = com_google_android_gms_measurement_internal_zzbo;
        super(callable);
        Preconditions.checkNotNull(str);
        this.zzapf = str;
        this.zzapi = z;
        if (this.zzaph == Long.MAX_VALUE) {
            com_google_android_gms_measurement_internal_zzbo.zzgo().zzjd().zzbx("Tasks index overflow");
        }
    }

    zzbr(zzbo com_google_android_gms_measurement_internal_zzbo, Runnable runnable, boolean z, String str) {
        this.zzapg = com_google_android_gms_measurement_internal_zzbo;
        super(runnable, null);
        Preconditions.checkNotNull(str);
        this.zzapf = str;
        this.zzapi = false;
        if (this.zzaph == Long.MAX_VALUE) {
            com_google_android_gms_measurement_internal_zzbo.zzgo().zzjd().zzbx("Tasks index overflow");
        }
    }

    protected final void setException(Throwable th) {
        this.zzapg.zzgo().zzjd().zzg(this.zzapf, th);
        if (th instanceof zzbp) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
        super.setException(th);
    }

    public final /* synthetic */ int compareTo(Object obj) {
        zzbr com_google_android_gms_measurement_internal_zzbr = (zzbr) obj;
        if (this.zzapi != com_google_android_gms_measurement_internal_zzbr.zzapi) {
            if (this.zzapi) {
                return -1;
            }
            return 1;
        } else if (this.zzaph < com_google_android_gms_measurement_internal_zzbr.zzaph) {
            return -1;
        } else {
            if (this.zzaph > com_google_android_gms_measurement_internal_zzbr.zzaph) {
                return 1;
            }
            this.zzapg.zzgo().zzje().zzg("Two tasks share the same index. index", Long.valueOf(this.zzaph));
            return 0;
        }
    }
}
