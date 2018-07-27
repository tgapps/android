package com.google.android.gms.internal.measurement;

import java.util.Iterator;

final class zzeu implements Iterator<String> {
    private Iterator<String> zzaga = this.zzagb.zzafz.keySet().iterator();
    private final /* synthetic */ zzet zzagb;

    zzeu(zzet com_google_android_gms_internal_measurement_zzet) {
        this.zzagb = com_google_android_gms_internal_measurement_zzet;
    }

    public final boolean hasNext() {
        return this.zzaga.hasNext();
    }

    public final /* synthetic */ Object next() {
        return (String) this.zzaga.next();
    }

    public final void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
