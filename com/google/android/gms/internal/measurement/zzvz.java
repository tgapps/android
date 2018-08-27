package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map.Entry;

final class zzvz<K> implements Iterator<Entry<K, Object>> {
    private Iterator<Entry<K, Object>> zzcac;

    public zzvz(Iterator<Entry<K, Object>> it) {
        this.zzcac = it;
    }

    public final boolean hasNext() {
        return this.zzcac.hasNext();
    }

    public final void remove() {
        this.zzcac.remove();
    }

    public final /* synthetic */ Object next() {
        Entry entry = (Entry) this.zzcac.next();
        if (entry.getValue() instanceof zzvw) {
            return new zzvy(entry);
        }
        return entry;
    }
}
