package com.google.android.gms.internal.measurement;

import android.os.Bundle;

final class zzji extends zzem {
    private final /* synthetic */ zzjh zzapx;

    zzji(zzjh com_google_android_gms_internal_measurement_zzjh, zzhi com_google_android_gms_internal_measurement_zzhi) {
        this.zzapx = com_google_android_gms_internal_measurement_zzjh;
        super(com_google_android_gms_internal_measurement_zzhi);
    }

    public final void run() {
        zzhg com_google_android_gms_internal_measurement_zzhg = this.zzapx;
        com_google_android_gms_internal_measurement_zzhg.zzab();
        com_google_android_gms_internal_measurement_zzhg.zzge().zzit().zzg("Session started, time", Long.valueOf(com_google_android_gms_internal_measurement_zzhg.zzbt().elapsedRealtime()));
        com_google_android_gms_internal_measurement_zzhg.zzgf().zzakk.set(false);
        com_google_android_gms_internal_measurement_zzhg.zzfu().zza("auto", "_s", new Bundle());
        com_google_android_gms_internal_measurement_zzhg.zzgf().zzakl.set(com_google_android_gms_internal_measurement_zzhg.zzbt().currentTimeMillis());
    }
}
