package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

abstract class zztz<E> extends AbstractList<E> implements zzvs<E> {
    private boolean zzbtu = true;

    zztz() {
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        if (!(obj instanceof RandomAccess)) {
            return super.equals(obj);
        }
        List list = (List) obj;
        int size = size();
        if (size != list.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(list.get(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < size(); i2++) {
            i = (i * 31) + get(i2).hashCode();
        }
        return i;
    }

    public boolean add(E e) {
        zztx();
        return super.add(e);
    }

    public void add(int i, E e) {
        zztx();
        super.add(i, e);
    }

    public boolean addAll(Collection<? extends E> collection) {
        zztx();
        return super.addAll(collection);
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        zztx();
        return super.addAll(i, collection);
    }

    public void clear() {
        zztx();
        super.clear();
    }

    public boolean zztw() {
        return this.zzbtu;
    }

    public final void zzsm() {
        this.zzbtu = false;
    }

    public E remove(int i) {
        zztx();
        return super.remove(i);
    }

    public boolean remove(Object obj) {
        zztx();
        return super.remove(obj);
    }

    public boolean removeAll(Collection<?> collection) {
        zztx();
        return super.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        zztx();
        return super.retainAll(collection);
    }

    public E set(int i, E e) {
        zztx();
        return super.set(i, e);
    }

    protected final void zztx() {
        if (!this.zzbtu) {
            throw new UnsupportedOperationException();
        }
    }
}
