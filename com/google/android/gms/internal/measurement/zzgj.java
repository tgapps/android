package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

final class zzgj<V> extends FutureTask<V> implements Comparable<zzgj> {
    private final String zzaly;
    private final /* synthetic */ zzgg zzalz;
    private final long zzama = zzgg.zzalx.getAndIncrement();
    final boolean zzamb;

    zzgj(zzgg com_google_android_gms_internal_measurement_zzgg, Runnable runnable, boolean z, String str) {
        this.zzalz = com_google_android_gms_internal_measurement_zzgg;
        super(runnable, null);
        Preconditions.checkNotNull(str);
        this.zzaly = str;
        this.zzamb = false;
        if (this.zzama == Long.MAX_VALUE) {
            com_google_android_gms_internal_measurement_zzgg.zzge().zzim().log("Tasks index overflow");
        }
    }

    zzgj(zzgg com_google_android_gms_internal_measurement_zzgg, Callable<V> callable, boolean z, String str) {
        this.zzalz = com_google_android_gms_internal_measurement_zzgg;
        super(callable);
        Preconditions.checkNotNull(str);
        this.zzaly = str;
        this.zzamb = z;
        if (this.zzama == Long.MAX_VALUE) {
            com_google_android_gms_internal_measurement_zzgg.zzge().zzim().log("Tasks index overflow");
        }
    }

    public final /* synthetic */ int compareTo(Object obj) {
        zzgj com_google_android_gms_internal_measurement_zzgj = (zzgj) obj;
        if (this.zzamb != com_google_android_gms_internal_measurement_zzgj.zzamb) {
            return this.zzamb ? -1 : 1;
        } else {
            if (this.zzama < com_google_android_gms_internal_measurement_zzgj.zzama) {
                return -1;
            }
            if (this.zzama > com_google_android_gms_internal_measurement_zzgj.zzama) {
                return 1;
            }
            this.zzalz.zzge().zzin().zzg("Two tasks share the same index. index", Long.valueOf(this.zzama));
            return 0;
        }
    }

    protected final void setException(Throwable th) {
        this.zzalz.zzge().zzim().zzg(this.zzaly, th);
        if (th instanceof zzgh) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
        super.setException(th);
    }
}
