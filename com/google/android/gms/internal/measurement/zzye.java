package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public final class zzye extends AbstractList<String> implements zzwc, RandomAccess {
    private final zzwc zzccq;

    public zzye(zzwc com_google_android_gms_internal_measurement_zzwc) {
        this.zzccq = com_google_android_gms_internal_measurement_zzwc;
    }

    public final Object getRaw(int i) {
        return this.zzccq.getRaw(i);
    }

    public final int size() {
        return this.zzccq.size();
    }

    public final void zzc(zzud com_google_android_gms_internal_measurement_zzud) {
        throw new UnsupportedOperationException();
    }

    public final ListIterator<String> listIterator(int i) {
        return new zzyf(this, i);
    }

    public final Iterator<String> iterator() {
        return new zzyg(this);
    }

    public final List<?> zzwv() {
        return this.zzccq.zzwv();
    }

    public final zzwc zzww() {
        return this;
    }

    public final /* synthetic */ Object get(int i) {
        return (String) this.zzccq.get(i);
    }
}
