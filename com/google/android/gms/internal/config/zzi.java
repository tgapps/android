package com.google.android.gms.internal.config;

import java.util.Collections;
import java.util.Map;

public final class zzi {
    private final long zzf;
    private final Map<String, String> zzg;
    private final int zzh;
    private final int zzi;
    private final int zzj;
    private final String zzk;

    private zzi(zzj com_google_android_gms_internal_config_zzj) {
        this.zzf = com_google_android_gms_internal_config_zzj.zzf;
        this.zzg = com_google_android_gms_internal_config_zzj.zzg;
        this.zzh = com_google_android_gms_internal_config_zzj.zzh;
        this.zzi = com_google_android_gms_internal_config_zzj.zzi;
        this.zzj = com_google_android_gms_internal_config_zzj.zzj;
        this.zzk = com_google_android_gms_internal_config_zzj.zzk;
    }

    public final String getGmpAppId() {
        return this.zzk;
    }

    public final long zza() {
        return this.zzf;
    }

    public final Map<String, String> zzb() {
        return this.zzg == null ? Collections.emptyMap() : this.zzg;
    }

    public final int zzc() {
        return this.zzh;
    }

    public final int zzd() {
        return this.zzj;
    }

    public final int zze() {
        return this.zzi;
    }
}
