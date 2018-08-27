package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;
import org.telegram.tgnet.ConnectionsManager;

final class zzuw extends zztz<Double> implements zzvs<Double>, zzxe, RandomAccess {
    private static final zzuw zzbvg;
    private int size;
    private double[] zzbvh;

    zzuw() {
        this(new double[10], 0);
    }

    private zzuw(double[] dArr, int i) {
        this.zzbvh = dArr;
        this.size = i;
    }

    protected final void removeRange(int i, int i2) {
        zztx();
        if (i2 < i) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        System.arraycopy(this.zzbvh, i2, this.zzbvh, i, this.size - i2);
        this.size -= i2 - i;
        this.modCount++;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzuw)) {
            return super.equals(obj);
        }
        zzuw com_google_android_gms_internal_measurement_zzuw = (zzuw) obj;
        if (this.size != com_google_android_gms_internal_measurement_zzuw.size) {
            return false;
        }
        double[] dArr = com_google_android_gms_internal_measurement_zzuw.zzbvh;
        for (int i = 0; i < this.size; i++) {
            if (this.zzbvh[i] != dArr[i]) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.size; i2++) {
            i = (i * 31) + zzvo.zzbf(Double.doubleToLongBits(this.zzbvh[i2]));
        }
        return i;
    }

    public final int size() {
        return this.size;
    }

    public final void zzd(double d) {
        zzc(this.size, d);
    }

    private final void zzc(int i, double d) {
        zztx();
        if (i < 0 || i > this.size) {
            throw new IndexOutOfBoundsException(zzaj(i));
        }
        if (this.size < this.zzbvh.length) {
            System.arraycopy(this.zzbvh, i, this.zzbvh, i + 1, this.size - i);
        } else {
            Object obj = new double[(((this.size * 3) / 2) + 1)];
            System.arraycopy(this.zzbvh, 0, obj, 0, i);
            System.arraycopy(this.zzbvh, i, obj, i + 1, this.size - i);
            this.zzbvh = obj;
        }
        this.zzbvh[i] = d;
        this.size++;
        this.modCount++;
    }

    public final boolean addAll(Collection<? extends Double> collection) {
        zztx();
        zzvo.checkNotNull(collection);
        if (!(collection instanceof zzuw)) {
            return super.addAll(collection);
        }
        zzuw com_google_android_gms_internal_measurement_zzuw = (zzuw) collection;
        if (com_google_android_gms_internal_measurement_zzuw.size == 0) {
            return false;
        }
        if (ConnectionsManager.DEFAULT_DATACENTER_ID - this.size < com_google_android_gms_internal_measurement_zzuw.size) {
            throw new OutOfMemoryError();
        }
        int i = this.size + com_google_android_gms_internal_measurement_zzuw.size;
        if (i > this.zzbvh.length) {
            this.zzbvh = Arrays.copyOf(this.zzbvh, i);
        }
        System.arraycopy(com_google_android_gms_internal_measurement_zzuw.zzbvh, 0, this.zzbvh, this.size, com_google_android_gms_internal_measurement_zzuw.size);
        this.size = i;
        this.modCount++;
        return true;
    }

    public final boolean remove(Object obj) {
        zztx();
        for (int i = 0; i < this.size; i++) {
            if (obj.equals(Double.valueOf(this.zzbvh[i]))) {
                System.arraycopy(this.zzbvh, i + 1, this.zzbvh, i, this.size - i);
                this.size--;
                this.modCount++;
                return true;
            }
        }
        return false;
    }

    private final void zzai(int i) {
        if (i < 0 || i >= this.size) {
            throw new IndexOutOfBoundsException(zzaj(i));
        }
    }

    private final String zzaj(int i) {
        return "Index:" + i + ", Size:" + this.size;
    }

    public final /* synthetic */ Object set(int i, Object obj) {
        double doubleValue = ((Double) obj).doubleValue();
        zztx();
        zzai(i);
        double d = this.zzbvh[i];
        this.zzbvh[i] = doubleValue;
        return Double.valueOf(d);
    }

    public final /* synthetic */ Object remove(int i) {
        zztx();
        zzai(i);
        double d = this.zzbvh[i];
        if (i < this.size - 1) {
            System.arraycopy(this.zzbvh, i + 1, this.zzbvh, i, this.size - i);
        }
        this.size--;
        this.modCount++;
        return Double.valueOf(d);
    }

    public final /* synthetic */ void add(int i, Object obj) {
        zzc(i, ((Double) obj).doubleValue());
    }

    public final /* synthetic */ zzvs zzak(int i) {
        if (i >= this.size) {
            return new zzuw(Arrays.copyOf(this.zzbvh, i), this.size);
        }
        throw new IllegalArgumentException();
    }

    public final /* synthetic */ Object get(int i) {
        zzai(i);
        return Double.valueOf(this.zzbvh[i]);
    }

    static {
        zztz com_google_android_gms_internal_measurement_zzuw = new zzuw();
        zzbvg = com_google_android_gms_internal_measurement_zzuw;
        com_google_android_gms_internal_measurement_zzuw.zzsm();
    }
}
