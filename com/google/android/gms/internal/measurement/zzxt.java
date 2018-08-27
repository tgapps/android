package com.google.android.gms.internal.measurement;

import java.util.Map.Entry;

final class zzxt implements Comparable<zzxt>, Entry<K, V> {
    private V value;
    private final /* synthetic */ zzxm zzcch;
    private final K zzcck;

    zzxt(zzxm com_google_android_gms_internal_measurement_zzxm, Entry<K, V> entry) {
        this(com_google_android_gms_internal_measurement_zzxm, (Comparable) entry.getKey(), entry.getValue());
    }

    zzxt(zzxm com_google_android_gms_internal_measurement_zzxm, K k, V v) {
        this.zzcch = com_google_android_gms_internal_measurement_zzxm;
        this.zzcck = k;
        this.value = v;
    }

    public final V getValue() {
        return this.value;
    }

    public final V setValue(V v) {
        this.zzcch.zzxz();
        V v2 = this.value;
        this.value = v;
        return v2;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Entry)) {
            return false;
        }
        Entry entry = (Entry) obj;
        if (equals(this.zzcck, entry.getKey()) && equals(this.value, entry.getValue())) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = this.zzcck == null ? 0 : this.zzcck.hashCode();
        if (this.value != null) {
            i = this.value.hashCode();
        }
        return hashCode ^ i;
    }

    public final String toString() {
        String valueOf = String.valueOf(this.zzcck);
        String valueOf2 = String.valueOf(this.value);
        return new StringBuilder((String.valueOf(valueOf).length() + 1) + String.valueOf(valueOf2).length()).append(valueOf).append("=").append(valueOf2).toString();
    }

    private static boolean equals(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        } else {
            return obj.equals(obj2);
        }
    }

    public final /* synthetic */ Object getKey() {
        return this.zzcck;
    }

    public final /* synthetic */ int compareTo(Object obj) {
        return ((Comparable) getKey()).compareTo((Comparable) ((zzxt) obj).getKey());
    }
}
