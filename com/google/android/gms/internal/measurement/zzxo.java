package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

final class zzxo implements Iterator<Entry<K, V>> {
    private int pos;
    private Iterator<Entry<K, V>> zzccg;
    private final /* synthetic */ zzxm zzcch;

    private zzxo(zzxm com_google_android_gms_internal_measurement_zzxm) {
        this.zzcch = com_google_android_gms_internal_measurement_zzxm;
        this.pos = this.zzcch.zzccb.size();
    }

    public final boolean hasNext() {
        return (this.pos > 0 && this.pos <= this.zzcch.zzccb.size()) || zzyb().hasNext();
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }

    private final Iterator<Entry<K, V>> zzyb() {
        if (this.zzccg == null) {
            this.zzccg = this.zzcch.zzcce.entrySet().iterator();
        }
        return this.zzccg;
    }

    public final /* synthetic */ Object next() {
        if (zzyb().hasNext()) {
            return (Entry) zzyb().next();
        }
        List zzb = this.zzcch.zzccb;
        int i = this.pos - 1;
        this.pos = i;
        return (Entry) zzb.get(i);
    }
}
