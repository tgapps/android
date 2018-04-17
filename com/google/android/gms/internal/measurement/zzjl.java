package com.google.android.gms.internal.measurement;

import android.os.Bundle;

final class zzjl extends zzem {
    private final /* synthetic */ zzjk zzaqr;

    zzjl(zzjk com_google_android_gms_internal_measurement_zzjk, zzgl com_google_android_gms_internal_measurement_zzgl) {
        this.zzaqr = com_google_android_gms_internal_measurement_zzjk;
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    public final void run() {
        zzhj com_google_android_gms_internal_measurement_zzhj = this.zzaqr;
        com_google_android_gms_internal_measurement_zzhj.zzab();
        com_google_android_gms_internal_measurement_zzhj.zzgg().zzir().zzg("Session started, time", Long.valueOf(com_google_android_gms_internal_measurement_zzhj.zzbt().elapsedRealtime()));
        com_google_android_gms_internal_measurement_zzhj.zzgh().zzakj.set(false);
        com_google_android_gms_internal_measurement_zzhj.zzfu().zza("auto", "_s", new Bundle());
        com_google_android_gms_internal_measurement_zzhj.zzgh().zzakk.set(com_google_android_gms_internal_measurement_zzhj.zzbt().currentTimeMillis());
    }
}
