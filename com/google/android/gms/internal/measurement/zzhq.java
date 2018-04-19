package com.google.android.gms.internal.measurement;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

final class zzhq implements Callable<String> {
    private final /* synthetic */ zzhm zzaop;

    zzhq(zzhm com_google_android_gms_internal_measurement_zzhm) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
    }

    public final /* synthetic */ Object call() throws Exception {
        Object zziw = this.zzaop.zzgh().zziw();
        if (zziw == null) {
            zzhj zzfu = this.zzaop.zzfu();
            if (zzfu.zzgf().zzjg()) {
                zzfu.zzgg().zzil().log("Cannot retrieve app instance id from analytics worker thread");
                zziw = null;
            } else {
                zzfu.zzgf();
                if (zzgg.isMainThread()) {
                    zzfu.zzgg().zzil().log("Cannot retrieve app instance id from main thread");
                    zziw = null;
                } else {
                    long elapsedRealtime = zzfu.zzbt().elapsedRealtime();
                    zziw = zzfu.zzae(120000);
                    elapsedRealtime = zzfu.zzbt().elapsedRealtime() - elapsedRealtime;
                    if (zziw == null && elapsedRealtime < 120000) {
                        zziw = zzfu.zzae(120000 - elapsedRealtime);
                    }
                }
            }
            if (zziw == null) {
                throw new TimeoutException();
            }
            this.zzaop.zzgh().zzbm(zziw);
        }
        return zziw;
    }
}
