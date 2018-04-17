package com.google.android.gms.internal.config;

import java.util.HashMap;
import java.util.Map;

public final class zzj {
    private long zzf = 43200;
    private Map<String, String> zzg;
    private int zzh;
    private int zzi = -1;
    private int zzj = -1;
    private String zzk;

    public final zzj zza(int i) {
        this.zzh = 10300;
        return this;
    }

    public final zzj zza(long j) {
        this.zzf = j;
        return this;
    }

    public final zzj zza(String str) {
        this.zzk = str;
        return this;
    }

    public final zzj zza(String str, String str2) {
        if (this.zzg == null) {
            this.zzg = new HashMap();
        }
        this.zzg.put(str, str2);
        return this;
    }

    public final zzj zzb(int i) {
        this.zzi = i;
        return this;
    }

    public final zzj zzc(int i) {
        this.zzj = i;
        return this;
    }

    public final zzi zzf() {
        return new zzi();
    }
}
