package com.google.android.gms.internal.config;

import com.google.android.gms.common.api.Status;

final class zzt extends zzq {
    private final /* synthetic */ zzs zzp;

    zzt(zzs com_google_android_gms_internal_config_zzs) {
        this.zzp = com_google_android_gms_internal_config_zzs;
    }

    public final void zza(Status status, zzad com_google_android_gms_internal_config_zzad) {
        if (com_google_android_gms_internal_config_zzad.getStatusCode() == 6502 || com_google_android_gms_internal_config_zzad.getStatusCode() == 6507) {
            this.zzp.setResult(new zzu(zzo.zzd(com_google_android_gms_internal_config_zzad.getStatusCode()), zzo.zza(com_google_android_gms_internal_config_zzad), com_google_android_gms_internal_config_zzad.getThrottleEndTimeMillis(), zzo.zzb(com_google_android_gms_internal_config_zzad)));
        } else {
            this.zzp.setResult(new zzu(zzo.zzd(com_google_android_gms_internal_config_zzad.getStatusCode()), zzo.zza(com_google_android_gms_internal_config_zzad), zzo.zzb(com_google_android_gms_internal_config_zzad)));
        }
    }
}
