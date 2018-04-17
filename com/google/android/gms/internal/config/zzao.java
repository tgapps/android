package com.google.android.gms.internal.config;

import java.util.List;
import java.util.Map;

public final class zzao {
    private Map<String, Map<String, byte[]>> zzaw;
    private long zzax;
    private List<byte[]> zzs;

    public zzao(Map<String, Map<String, byte[]>> map, long j, List<byte[]> list) {
        this.zzaw = map;
        this.zzax = j;
        this.zzs = list;
    }

    public final long getTimestamp() {
        return this.zzax;
    }

    public final void setTimestamp(long j) {
        this.zzax = j;
    }

    public final boolean zzb(String str) {
        return (str == null || !zzq() || this.zzaw.get(str) == null || ((Map) this.zzaw.get(str)).isEmpty()) ? false : true;
    }

    public final boolean zzb(String str, String str2) {
        return zzq() && zzb(str2) && zzc(str, str2) != null;
    }

    public final byte[] zzc(String str, String str2) {
        if (str != null) {
            if (zzb(str2)) {
                return (byte[]) ((Map) this.zzaw.get(str2)).get(str);
            }
        }
        return null;
    }

    public final List<byte[]> zzg() {
        return this.zzs;
    }

    public final Map<String, Map<String, byte[]>> zzp() {
        return this.zzaw;
    }

    public final boolean zzq() {
        return (this.zzaw == null || this.zzaw.isEmpty()) ? false : true;
    }
}
