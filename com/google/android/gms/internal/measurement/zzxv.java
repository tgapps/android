package com.google.android.gms.internal.measurement;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;

class zzxv extends AbstractSet<Entry<K, V>> {
    private final /* synthetic */ zzxm zzcch;

    private zzxv(zzxm com_google_android_gms_internal_measurement_zzxm) {
        this.zzcch = com_google_android_gms_internal_measurement_zzxm;
    }

    public Iterator<Entry<K, V>> iterator() {
        return new zzxu(this.zzcch);
    }

    public int size() {
        return this.zzcch.size();
    }

    public boolean contains(Object obj) {
        Entry entry = (Entry) obj;
        Object obj2 = this.zzcch.get(entry.getKey());
        Object value = entry.getValue();
        return obj2 == value || (obj2 != null && obj2.equals(value));
    }

    public boolean remove(Object obj) {
        Entry entry = (Entry) obj;
        if (!contains(entry)) {
            return false;
        }
        this.zzcch.remove(entry.getKey());
        return true;
    }

    public void clear() {
        this.zzcch.clear();
    }

    public /* synthetic */ boolean add(Object obj) {
        Entry entry = (Entry) obj;
        if (contains(entry)) {
            return false;
        }
        this.zzcch.zza((Comparable) entry.getKey(), entry.getValue());
        return true;
    }
}
