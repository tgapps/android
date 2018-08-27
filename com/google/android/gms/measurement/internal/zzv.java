package com.google.android.gms.measurement.internal;

import android.os.Handler;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzdx;

abstract class zzv {
    private static volatile Handler handler;
    private final zzcq zzahw;
    private final Runnable zzyo;
    private volatile long zzyp;

    zzv(zzcq com_google_android_gms_measurement_internal_zzcq) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzcq);
        this.zzahw = com_google_android_gms_measurement_internal_zzcq;
        this.zzyo = new zzw(this, com_google_android_gms_measurement_internal_zzcq);
    }

    public abstract void run();

    public final void zzh(long j) {
        cancel();
        if (j >= 0) {
            this.zzyp = this.zzahw.zzbx().currentTimeMillis();
            if (!getHandler().postDelayed(this.zzyo, j)) {
                this.zzahw.zzgo().zzjd().zzg("Failed to schedule delayed post. time", Long.valueOf(j));
            }
        }
    }

    public final boolean zzej() {
        return this.zzyp != 0;
    }

    final void cancel() {
        this.zzyp = 0;
        getHandler().removeCallbacks(this.zzyo);
    }

    private final Handler getHandler() {
        if (handler != null) {
            return handler;
        }
        Handler handler;
        synchronized (zzv.class) {
            if (handler == null) {
                handler = new zzdx(this.zzahw.getContext().getMainLooper());
            }
            handler = handler;
        }
        return handler;
    }
}
