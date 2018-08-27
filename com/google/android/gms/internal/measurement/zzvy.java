package com.google.android.gms.internal.measurement;

import java.util.Map.Entry;

final class zzvy<K> implements Entry<K, Object> {
    private Entry<K, zzvw> zzcab;

    private zzvy(Entry<K, zzvw> entry) {
        this.zzcab = entry;
    }

    public final K getKey() {
        return this.zzcab.getKey();
    }

    public final Object getValue() {
        if (((zzvw) this.zzcab.getValue()) == null) {
            return null;
        }
        return zzvw.zzwt();
    }

    public final zzvw zzwu() {
        return (zzvw) this.zzcab.getValue();
    }

    public final Object setValue(Object obj) {
        if (obj instanceof zzwt) {
            return ((zzvw) this.zzcab.getValue()).zzi((zzwt) obj);
        }
        throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
    }
}
