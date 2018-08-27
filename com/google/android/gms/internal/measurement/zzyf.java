package com.google.android.gms.internal.measurement;

import java.util.ListIterator;

final class zzyf implements ListIterator<String> {
    private ListIterator<String> zzccr = this.zzcct.zzccq.listIterator(this.zzccs);
    private final /* synthetic */ int zzccs;
    private final /* synthetic */ zzye zzcct;

    zzyf(zzye com_google_android_gms_internal_measurement_zzye, int i) {
        this.zzcct = com_google_android_gms_internal_measurement_zzye;
        this.zzccs = i;
    }

    public final boolean hasNext() {
        return this.zzccr.hasNext();
    }

    public final boolean hasPrevious() {
        return this.zzccr.hasPrevious();
    }

    public final int nextIndex() {
        return this.zzccr.nextIndex();
    }

    public final int previousIndex() {
        return this.zzccr.previousIndex();
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }

    public final /* synthetic */ void add(Object obj) {
        throw new UnsupportedOperationException();
    }

    public final /* synthetic */ void set(Object obj) {
        throw new UnsupportedOperationException();
    }

    public final /* synthetic */ Object previous() {
        return (String) this.zzccr.previous();
    }

    public final /* synthetic */ Object next() {
        return (String) this.zzccr.next();
    }
}
