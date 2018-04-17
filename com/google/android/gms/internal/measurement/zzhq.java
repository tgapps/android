package com.google.android.gms.internal.measurement;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

final class zzhq implements Callable<String> {
    private final /* synthetic */ zzhm zzaop;

    zzhq(zzhm com_google_android_gms_internal_measurement_zzhm) {
        this.zzaop = com_google_android_gms_internal_measurement_zzhm;
    }

    public final /* synthetic */ Object call() throws Exception {
        String zziw = this.zzaop.zzgh().zziw();
        if (zziw != null) {
            return zziw;
        }
        zzfi zzil;
        String str;
        zzhj zzfu = this.zzaop.zzfu();
        String str2 = null;
        if (zzfu.zzgf().zzjg()) {
            zzil = zzfu.zzgg().zzil();
            str = "Cannot retrieve app instance id from analytics worker thread";
        } else {
            zzfu.zzgf();
            if (zzgg.isMainThread()) {
                zzil = zzfu.zzgg().zzil();
                str = "Cannot retrieve app instance id from main thread";
            } else {
                long elapsedRealtime = zzfu.zzbt().elapsedRealtime();
                String zzae = zzfu.zzae(120000);
                long elapsedRealtime2 = zzfu.zzbt().elapsedRealtime() - elapsedRealtime;
                str2 = (zzae != null || elapsedRealtime2 >= 120000) ? zzae : zzfu.zzae(120000 - elapsedRealtime2);
                if (str2 != null) {
                    throw new TimeoutException();
                }
                this.zzaop.zzgh().zzbm(str2);
                return str2;
            }
        }
        zzil.log(str);
        if (str2 != null) {
            this.zzaop.zzgh().zzbm(str2);
            return str2;
        }
        throw new TimeoutException();
    }
}
