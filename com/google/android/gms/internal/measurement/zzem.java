package com.google.android.gms.internal.measurement;

import android.os.Handler;
import com.google.android.gms.common.internal.Preconditions;

abstract class zzem {
    private static volatile Handler handler;
    private boolean enabled = true;
    private final zzgl zzacr;
    private final Runnable zzxy;
    private volatile long zzxz;

    zzem(zzgl com_google_android_gms_internal_measurement_zzgl) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzgl);
        this.zzacr = com_google_android_gms_internal_measurement_zzgl;
        this.zzxy = new zzen(this, com_google_android_gms_internal_measurement_zzgl);
    }

    private final Handler getHandler() {
        if (handler != null) {
            return handler;
        }
        Handler handler;
        synchronized (zzem.class) {
            if (handler == null) {
                handler = new Handler(this.zzacr.getContext().getMainLooper());
            }
            handler = handler;
        }
        return handler;
    }

    public final void cancel() {
        this.zzxz = 0;
        getHandler().removeCallbacks(this.zzxy);
    }

    public abstract void run();

    public final boolean zzef() {
        return this.zzxz != 0;
    }

    public final void zzh(long j) {
        cancel();
        if (j >= 0) {
            this.zzxz = this.zzacr.zzbt().currentTimeMillis();
            if (!getHandler().postDelayed(this.zzxy, j)) {
                this.zzacr.zzgg().zzil().zzg("Failed to schedule delayed post. time", Long.valueOf(j));
            }
        }
    }
}
