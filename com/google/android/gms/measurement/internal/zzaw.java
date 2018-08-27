package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import java.util.List;
import java.util.Map;

final class zzaw implements Runnable {
    private final String packageName;
    private final int status;
    private final zzav zzamr;
    private final Throwable zzams;
    private final byte[] zzamt;
    private final Map<String, List<String>> zzamu;

    private zzaw(String str, zzav com_google_android_gms_measurement_internal_zzav, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzav);
        this.zzamr = com_google_android_gms_measurement_internal_zzav;
        this.status = i;
        this.zzams = th;
        this.zzamt = bArr;
        this.packageName = str;
        this.zzamu = map;
    }

    public final void run() {
        this.zzamr.zza(this.packageName, this.status, this.zzams, this.zzamt, this.zzamu);
    }
}
