package com.google.android.gms.internal.config;

import java.util.HashMap;
import java.util.Map;

public final class zzar {
    private boolean zzap;
    private int zzaz;
    private long zzbd;
    private Map<String, zzal> zzbe;
    private long zzbf;

    public zzar() {
        this(-1);
    }

    private zzar(int i, long j, Map<String, zzal> map, boolean z) {
        this(0, -1, null, false, -1);
    }

    private zzar(int i, long j, Map<String, zzal> map, boolean z, long j2) {
        this.zzaz = 0;
        this.zzbd = j;
        this.zzbe = new HashMap();
        this.zzap = false;
        this.zzbf = -1;
    }

    private zzar(long j) {
        this(0, -1, null, false);
    }

    public final int getLastFetchStatus() {
        return this.zzaz;
    }

    public final boolean isDeveloperModeEnabled() {
        return this.zzap;
    }

    public final void zza(Map<String, zzal> map) {
        this.zzbe = map;
    }

    public final void zza(boolean z) {
        this.zzap = z;
    }

    public final void zzc(long j) {
        this.zzbd = j;
    }

    public final void zzd(long j) {
        this.zzbf = j;
    }

    public final void zzf(int i) {
        this.zzaz = i;
    }

    public final Map<String, zzal> zzr() {
        return this.zzbe;
    }

    public final long zzt() {
        return this.zzbf;
    }
}
