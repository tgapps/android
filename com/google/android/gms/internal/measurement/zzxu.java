package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map.Entry;

final class zzxu implements Iterator<Entry<K, V>> {
    private int pos;
    private Iterator<Entry<K, V>> zzccg;
    private final /* synthetic */ zzxm zzcch;
    private boolean zzccl;

    private zzxu(zzxm com_google_android_gms_internal_measurement_zzxm) {
        this.zzcch = com_google_android_gms_internal_measurement_zzxm;
        this.pos = -1;
    }

    public final boolean hasNext() {
        return this.pos + 1 < this.zzcch.zzccb.size() || (!this.zzcch.zzccc.isEmpty() && zzyb().hasNext());
    }

    public final void remove() {
        if (this.zzccl) {
            this.zzccl = false;
            this.zzcch.zzxz();
            if (this.pos < this.zzcch.zzccb.size()) {
                zzxm com_google_android_gms_internal_measurement_zzxm = this.zzcch;
                int i = this.pos;
                this.pos = i - 1;
                com_google_android_gms_internal_measurement_zzxm.zzbv(i);
                return;
            }
            zzyb().remove();
            return;
        }
        throw new IllegalStateException("remove() was called before next()");
    }

    private final Iterator<Entry<K, V>> zzyb() {
        if (this.zzccg == null) {
            this.zzccg = this.zzcch.zzccc.entrySet().iterator();
        }
        return this.zzccg;
    }

    public final /* synthetic */ Object next() {
        this.zzccl = true;
        int i = this.pos + 1;
        this.pos = i;
        if (i < this.zzcch.zzccb.size()) {
            return (Entry) this.zzcch.zzccb.get(this.pos);
        }
        return (Entry) zzyb().next();
    }
}
