package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.List;

final class zzxg<E> extends zztz<E> {
    private static final zzxg<Object> zzcbv;
    private final List<E> zzcai;

    public static <E> zzxg<E> zzxo() {
        return zzcbv;
    }

    zzxg() {
        this(new ArrayList(10));
    }

    private zzxg(List<E> list) {
        this.zzcai = list;
    }

    public final void add(int i, E e) {
        zztx();
        this.zzcai.add(i, e);
        this.modCount++;
    }

    public final E get(int i) {
        return this.zzcai.get(i);
    }

    public final E remove(int i) {
        zztx();
        E remove = this.zzcai.remove(i);
        this.modCount++;
        return remove;
    }

    public final E set(int i, E e) {
        zztx();
        E e2 = this.zzcai.set(i, e);
        this.modCount++;
        return e2;
    }

    public final int size() {
        return this.zzcai.size();
    }

    public final /* synthetic */ zzvs zzak(int i) {
        if (i < size()) {
            throw new IllegalArgumentException();
        }
        List arrayList = new ArrayList(i);
        arrayList.addAll(this.zzcai);
        return new zzxg(arrayList);
    }

    static {
        zztz com_google_android_gms_internal_measurement_zzxg = new zzxg();
        zzcbv = com_google_android_gms_internal_measurement_zzxg;
        com_google_android_gms_internal_measurement_zzxg.zzsm();
    }
}
