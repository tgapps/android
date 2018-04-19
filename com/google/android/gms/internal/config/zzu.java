package com.google.android.gms.internal.config;

import com.google.android.gms.common.api.Status;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class zzu implements zzk {
    private final Status mStatus;
    private final Map<String, TreeMap<String, byte[]>> zzq;
    private final long zzr;
    private final List<byte[]> zzs;

    public zzu(Status status, Map<String, TreeMap<String, byte[]>> map) {
        this(status, (Map) map, -1);
    }

    private zzu(Status status, Map<String, TreeMap<String, byte[]>> map, long j) {
        this(status, map, -1, null);
    }

    public zzu(Status status, Map<String, TreeMap<String, byte[]>> map, long j, List<byte[]> list) {
        this.mStatus = status;
        this.zzq = map;
        this.zzr = j;
        this.zzs = list;
    }

    public zzu(Status status, Map<String, TreeMap<String, byte[]>> map, List<byte[]> list) {
        this(status, map, -1, list);
    }

    public final Status getStatus() {
        return this.mStatus;
    }

    public final long getThrottleEndTimeMillis() {
        return this.zzr;
    }

    public final byte[] zza(String str, byte[] bArr, String str2) {
        Object obj = (this.zzq == null || this.zzq.get(str2) == null) ? null : ((TreeMap) this.zzq.get(str2)).get(str) != null ? 1 : null;
        return obj != null ? (byte[]) ((TreeMap) this.zzq.get(str2)).get(str) : null;
    }

    public final List<byte[]> zzg() {
        return this.zzs;
    }

    public final Map<String, Set<String>> zzh() {
        Map<String, Set<String>> hashMap = new HashMap();
        if (this.zzq != null) {
            for (String str : this.zzq.keySet()) {
                Map map = (Map) this.zzq.get(str);
                if (map != null) {
                    hashMap.put(str, map.keySet());
                }
            }
        }
        return hashMap;
    }
}
