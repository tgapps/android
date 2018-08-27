package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map.Entry;

final class zzxp extends zzxv {
    private final /* synthetic */ zzxm zzcch;

    private zzxp(zzxm com_google_android_gms_internal_measurement_zzxm) {
        this.zzcch = com_google_android_gms_internal_measurement_zzxm;
        super(com_google_android_gms_internal_measurement_zzxm);
    }

    public final Iterator<Entry<K, V>> iterator() {
        return new zzxo(this.zzcch);
    }
}
