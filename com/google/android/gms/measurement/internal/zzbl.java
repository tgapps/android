package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver.PendingResult;
import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.measurement.AppMeasurement;

final class zzbl implements Runnable {
    private final /* synthetic */ Context val$context;
    private final /* synthetic */ zzbt zzaoj;
    private final /* synthetic */ zzap zzaok;
    private final /* synthetic */ long zzaol;
    private final /* synthetic */ Bundle zzaom;
    private final /* synthetic */ PendingResult zzrf;

    zzbl(zzbj com_google_android_gms_measurement_internal_zzbj, zzbt com_google_android_gms_measurement_internal_zzbt, long j, Bundle bundle, Context context, zzap com_google_android_gms_measurement_internal_zzap, PendingResult pendingResult) {
        this.zzaoj = com_google_android_gms_measurement_internal_zzbt;
        this.zzaol = j;
        this.zzaom = bundle;
        this.val$context = context;
        this.zzaok = com_google_android_gms_measurement_internal_zzap;
        this.zzrf = pendingResult;
    }

    public final void run() {
        long j = this.zzaoj.zzgp().zzanj.get();
        long j2 = this.zzaol;
        if (j > 0 && (j2 >= j || j2 <= 0)) {
            j2 = j - 1;
        }
        if (j2 > 0) {
            this.zzaom.putLong("click_timestamp", j2);
        }
        this.zzaom.putString("_cis", "referrer broadcast");
        AppMeasurement.getInstance(this.val$context).logEventInternal("auto", "_cmp", this.zzaom);
        this.zzaok.zzjl().zzbx("Install campaign recorded");
        if (this.zzrf != null) {
            this.zzrf.finish();
        }
    }
}
