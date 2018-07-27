package com.google.android.gms.internal.measurement;

import android.os.Bundle;

final class zzjj extends zzeo {
    private final /* synthetic */ zzji zzaqg;

    zzjj(zzji com_google_android_gms_internal_measurement_zzji, zzhj com_google_android_gms_internal_measurement_zzhj) {
        this.zzaqg = com_google_android_gms_internal_measurement_zzji;
        super(com_google_android_gms_internal_measurement_zzhj);
    }

    public final void run() {
        zzhh com_google_android_gms_internal_measurement_zzhh = this.zzaqg;
        com_google_android_gms_internal_measurement_zzhh.zzab();
        com_google_android_gms_internal_measurement_zzhh.zzgf().zziz().zzg("Session started, time", Long.valueOf(com_google_android_gms_internal_measurement_zzhh.zzbt().elapsedRealtime()));
        com_google_android_gms_internal_measurement_zzhh.zzgg().zzakt.set(false);
        com_google_android_gms_internal_measurement_zzhh.zzfv().zza("auto", "_s", new Bundle());
        com_google_android_gms_internal_measurement_zzhh.zzgg().zzaku.set(com_google_android_gms_internal_measurement_zzhh.zzbt().currentTimeMillis());
    }
}
