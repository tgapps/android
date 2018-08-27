package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;
import org.telegram.tgnet.ConnectionsManager;

final class zzvn extends zztz<Integer> implements zzvs<Integer>, zzxe, RandomAccess {
    private static final zzvn zzbzh;
    private int size;
    private int[] zzbzi;

    zzvn() {
        this(new int[10], 0);
    }

    private zzvn(int[] iArr, int i) {
        this.zzbzi = iArr;
        this.size = i;
    }

    protected final void removeRange(int i, int i2) {
        zztx();
        if (i2 < i) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        System.arraycopy(this.zzbzi, i2, this.zzbzi, i, this.size - i2);
        this.size -= i2 - i;
        this.modCount++;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzvn)) {
            return super.equals(obj);
        }
        zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) obj;
        if (this.size != com_google_android_gms_internal_measurement_zzvn.size) {
            return false;
        }
        int[] iArr = com_google_android_gms_internal_measurement_zzvn.zzbzi;
        for (int i = 0; i < this.size; i++) {
            if (this.zzbzi[i] != iArr[i]) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i = 1;
        for (int i2 = 0; i2 < this.size; i2++) {
            i = (i * 31) + this.zzbzi[i2];
        }
        return i;
    }

    public final int getInt(int i) {
        zzai(i);
        return this.zzbzi[i];
    }

    public final int size() {
        return this.size;
    }

    public final void zzbm(int i) {
        zzp(this.size, i);
    }

    private final void zzp(int i, int i2) {
        zztx();
        if (i < 0 || i > this.size) {
            throw new IndexOutOfBoundsException(zzaj(i));
        }
        if (this.size < this.zzbzi.length) {
            System.arraycopy(this.zzbzi, i, this.zzbzi, i + 1, this.size - i);
        } else {
            Object obj = new int[(((this.size * 3) / 2) + 1)];
            System.arraycopy(this.zzbzi, 0, obj, 0, i);
            System.arraycopy(this.zzbzi, i, obj, i + 1, this.size - i);
            this.zzbzi = obj;
        }
        this.zzbzi[i] = i2;
        this.size++;
        this.modCount++;
    }

    public final boolean addAll(Collection<? extends Integer> collection) {
        zztx();
        zzvo.checkNotNull(collection);
        if (!(collection instanceof zzvn)) {
            return super.addAll(collection);
        }
        zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) collection;
        if (com_google_android_gms_internal_measurement_zzvn.size == 0) {
            return false;
        }
        if (ConnectionsManager.DEFAULT_DATACENTER_ID - this.size < com_google_android_gms_internal_measurement_zzvn.size) {
            throw new OutOfMemoryError();
        }
        int i = this.size + com_google_android_gms_internal_measurement_zzvn.size;
        if (i > this.zzbzi.length) {
            this.zzbzi = Arrays.copyOf(this.zzbzi, i);
        }
        System.arraycopy(com_google_android_gms_internal_measurement_zzvn.zzbzi, 0, this.zzbzi, this.size, com_google_android_gms_internal_measurement_zzvn.size);
        this.size = i;
        this.modCount++;
        return true;
    }

    public final boolean remove(Object obj) {
        zztx();
        for (int i = 0; i < this.size; i++) {
            if (obj.equals(Integer.valueOf(this.zzbzi[i]))) {
                System.arraycopy(this.zzbzi, i + 1, this.zzbzi, i, this.size - i);
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
        int intValue = ((Integer) obj).intValue();
        zztx();
        zzai(i);
        int i2 = this.zzbzi[i];
        this.zzbzi[i] = intValue;
        return Integer.valueOf(i2);
    }

    public final /* synthetic */ Object remove(int i) {
        zztx();
        zzai(i);
        int i2 = this.zzbzi[i];
        if (i < this.size - 1) {
            System.arraycopy(this.zzbzi, i + 1, this.zzbzi, i, this.size - i);
        }
        this.size--;
        this.modCount++;
        return Integer.valueOf(i2);
    }

    public final /* synthetic */ void add(int i, Object obj) {
        zzp(i, ((Integer) obj).intValue());
    }

    public final /* synthetic */ zzvs zzak(int i) {
        if (i >= this.size) {
            return new zzvn(Arrays.copyOf(this.zzbzi, i), this.size);
        }
        throw new IllegalArgumentException();
    }

    public final /* synthetic */ Object get(int i) {
        return Integer.valueOf(getInt(i));
    }

    static {
        zztz com_google_android_gms_internal_measurement_zzvn = new zzvn();
        zzbzh = com_google_android_gms_internal_measurement_zzvn;
        com_google_android_gms_internal_measurement_zzvn.zzsm();
    }
}
