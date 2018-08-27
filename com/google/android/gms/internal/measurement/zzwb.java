package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public final class zzwb extends zztz<String> implements zzwc, RandomAccess {
    private static final zzwb zzcag;
    private static final zzwc zzcah = zzcag;
    private final List<Object> zzcai;

    public zzwb() {
        this(10);
    }

    public zzwb(int i) {
        this(new ArrayList(i));
    }

    private zzwb(ArrayList<Object> arrayList) {
        this.zzcai = arrayList;
    }

    public final int size() {
        return this.zzcai.size();
    }

    public final boolean addAll(Collection<? extends String> collection) {
        return addAll(size(), collection);
    }

    public final boolean addAll(int i, Collection<? extends String> collection) {
        zztx();
        if (collection instanceof zzwc) {
            collection = ((zzwc) collection).zzwv();
        }
        boolean addAll = this.zzcai.addAll(i, collection);
        this.modCount++;
        return addAll;
    }

    public final void clear() {
        zztx();
        this.zzcai.clear();
        this.modCount++;
    }

    public final void zzc(zzud com_google_android_gms_internal_measurement_zzud) {
        zztx();
        this.zzcai.add(com_google_android_gms_internal_measurement_zzud);
        this.modCount++;
    }

    public final Object getRaw(int i) {
        return this.zzcai.get(i);
    }

    private static String zzw(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof zzud) {
            return ((zzud) obj).zzua();
        }
        return zzvo.zzm((byte[]) obj);
    }

    public final List<?> zzwv() {
        return Collections.unmodifiableList(this.zzcai);
    }

    public final zzwc zzww() {
        if (zztw()) {
            return new zzye(this);
        }
        return this;
    }

    public final /* synthetic */ Object set(int i, Object obj) {
        String str = (String) obj;
        zztx();
        return zzw(this.zzcai.set(i, str));
    }

    public final /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    public final /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    public final /* synthetic */ Object remove(int i) {
        zztx();
        Object remove = this.zzcai.remove(i);
        this.modCount++;
        return zzw(remove);
    }

    public final /* bridge */ /* synthetic */ boolean zztw() {
        return super.zztw();
    }

    public final /* synthetic */ void add(int i, Object obj) {
        String str = (String) obj;
        zztx();
        this.zzcai.add(i, str);
        this.modCount++;
    }

    public final /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public final /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public final /* synthetic */ zzvs zzak(int i) {
        if (i < size()) {
            throw new IllegalArgumentException();
        }
        ArrayList arrayList = new ArrayList(i);
        arrayList.addAll(this.zzcai);
        return new zzwb(arrayList);
    }

    public final /* synthetic */ Object get(int i) {
        Object obj = this.zzcai.get(i);
        if (obj instanceof String) {
            return (String) obj;
        }
        String zzua;
        if (obj instanceof zzud) {
            zzud com_google_android_gms_internal_measurement_zzud = (zzud) obj;
            zzua = com_google_android_gms_internal_measurement_zzud.zzua();
            if (com_google_android_gms_internal_measurement_zzud.zzub()) {
                this.zzcai.set(i, zzua);
            }
            return zzua;
        }
        byte[] bArr = (byte[]) obj;
        zzua = zzvo.zzm(bArr);
        if (zzvo.zzl(bArr)) {
            this.zzcai.set(i, zzua);
        }
        return zzua;
    }

    static {
        zztz com_google_android_gms_internal_measurement_zzwb = new zzwb();
        zzcag = com_google_android_gms_internal_measurement_zzwb;
        com_google_android_gms_internal_measurement_zzwb.zzsm();
    }
}
