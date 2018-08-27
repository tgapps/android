package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class zzwn<K, V> extends LinkedHashMap<K, V> {
    private static final zzwn zzcau;
    private boolean zzbtu = true;

    private zzwn() {
    }

    private zzwn(Map<K, V> map) {
        super(map);
    }

    public static <K, V> zzwn<K, V> zzxa() {
        return zzcau;
    }

    public final void zza(zzwn<K, V> com_google_android_gms_internal_measurement_zzwn_K__V) {
        zzxc();
        if (!com_google_android_gms_internal_measurement_zzwn_K__V.isEmpty()) {
            putAll(com_google_android_gms_internal_measurement_zzwn_K__V);
        }
    }

    public final Set<Entry<K, V>> entrySet() {
        return isEmpty() ? Collections.emptySet() : super.entrySet();
    }

    public final void clear() {
        zzxc();
        super.clear();
    }

    public final V put(K k, V v) {
        zzxc();
        zzvo.checkNotNull(k);
        zzvo.checkNotNull(v);
        return super.put(k, v);
    }

    public final void putAll(Map<? extends K, ? extends V> map) {
        zzxc();
        for (Object next : map.keySet()) {
            zzvo.checkNotNull(next);
            zzvo.checkNotNull(map.get(next));
        }
        super.putAll(map);
    }

    public final V remove(Object obj) {
        zzxc();
        return super.remove(obj);
    }

    public final boolean equals(Object obj) {
        if (obj instanceof Map) {
            Object obj2;
            obj = (Map) obj;
            if (this != obj) {
                if (size() == obj.size()) {
                    for (Entry entry : entrySet()) {
                        if (!obj.containsKey(entry.getKey())) {
                            obj2 = null;
                            break;
                        }
                        boolean equals;
                        Object value = entry.getValue();
                        Object obj3 = obj.get(entry.getKey());
                        if ((value instanceof byte[]) && (obj3 instanceof byte[])) {
                            equals = Arrays.equals((byte[]) value, (byte[]) obj3);
                            continue;
                        } else {
                            equals = value.equals(obj3);
                            continue;
                        }
                        if (!equals) {
                            obj2 = null;
                            break;
                        }
                    }
                }
                obj2 = null;
                if (obj2 != null) {
                    return true;
                }
            }
            int i = 1;
            if (obj2 != null) {
                return true;
            }
        }
        return false;
    }

    private static int zzx(Object obj) {
        if (obj instanceof byte[]) {
            return zzvo.hashCode((byte[]) obj);
        }
        if (!(obj instanceof zzvp)) {
            return obj.hashCode();
        }
        throw new UnsupportedOperationException();
    }

    public final int hashCode() {
        int i = 0;
        for (Entry entry : entrySet()) {
            i = (zzx(entry.getValue()) ^ zzx(entry.getKey())) + i;
        }
        return i;
    }

    public final zzwn<K, V> zzxb() {
        return isEmpty() ? new zzwn() : new zzwn(this);
    }

    public final void zzsm() {
        this.zzbtu = false;
    }

    public final boolean isMutable() {
        return this.zzbtu;
    }

    private final void zzxc() {
        if (!this.zzbtu) {
            throw new UnsupportedOperationException();
        }
    }

    static {
        zzwn com_google_android_gms_internal_measurement_zzwn = new zzwn();
        zzcau = com_google_android_gms_internal_measurement_zzwn;
        com_google_android_gms_internal_measurement_zzwn.zzbtu = false;
    }
}
