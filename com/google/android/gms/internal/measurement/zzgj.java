package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.FutureTask;

final class zzgj<V> extends FutureTask<V> implements Comparable<zzgj> {
    private final String zzalw;
    private final /* synthetic */ zzgg zzalx;
    private final long zzaly = zzgg.zzalv.getAndIncrement();
    final boolean zzalz;

    zzgj(zzgg com_google_android_gms_internal_measurement_zzgg, Runnable runnable, boolean z, String str) {
        this.zzalx = com_google_android_gms_internal_measurement_zzgg;
        super(runnable, null);
        Preconditions.checkNotNull(str);
        this.zzalw = str;
        this.zzalz = false;
        if (this.zzaly == Long.MAX_VALUE) {
            com_google_android_gms_internal_measurement_zzgg.zzgg().zzil().log("Tasks index overflow");
        }
    }

    public final /* synthetic */ int compareTo(Object obj) {
        zzgj com_google_android_gms_internal_measurement_zzgj = (zzgj) obj;
        if (this.zzalz != com_google_android_gms_internal_measurement_zzgj.zzalz) {
            return this.zzalz ? -1 : 1;
        } else {
            if (this.zzaly < com_google_android_gms_internal_measurement_zzgj.zzaly) {
                return -1;
            }
            if (this.zzaly > com_google_android_gms_internal_measurement_zzgj.zzaly) {
                return 1;
            }
            this.zzalx.zzgg().zzim().zzg("Two tasks share the same index. index", Long.valueOf(this.zzaly));
            return 0;
        }
    }

    protected final void setException(Throwable th) {
        this.zzalx.zzgg().zzil().zzg(this.zzalw, th);
        if (th instanceof zzgh) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
        super.setException(th);
    }
}
