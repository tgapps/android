package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

final class zzvd<FieldDescriptorType extends zzvf<FieldDescriptorType>> {
    private static final zzvd zzbvs = new zzvd(true);
    private boolean zzbpo;
    private final zzxm<FieldDescriptorType, Object> zzbvq = zzxm.zzbt(16);
    private boolean zzbvr = false;

    private zzvd() {
    }

    private zzvd(boolean z) {
        zzsm();
    }

    public static <T extends zzvf<T>> zzvd<T> zzvt() {
        return zzbvs;
    }

    final boolean isEmpty() {
        return this.zzbvq.isEmpty();
    }

    public final void zzsm() {
        if (!this.zzbpo) {
            this.zzbvq.zzsm();
            this.zzbpo = true;
        }
    }

    public final boolean isImmutable() {
        return this.zzbpo;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzvd)) {
            return false;
        }
        return this.zzbvq.equals(((zzvd) obj).zzbvq);
    }

    public final int hashCode() {
        return this.zzbvq.hashCode();
    }

    public final Iterator<Entry<FieldDescriptorType, Object>> iterator() {
        if (this.zzbvr) {
            return new zzvz(this.zzbvq.entrySet().iterator());
        }
        return this.zzbvq.entrySet().iterator();
    }

    final Iterator<Entry<FieldDescriptorType, Object>> descendingIterator() {
        if (this.zzbvr) {
            return new zzvz(this.zzbvq.zzxy().iterator());
        }
        return this.zzbvq.zzxy().iterator();
    }

    private final Object zza(FieldDescriptorType fieldDescriptorType) {
        Object obj = this.zzbvq.get(fieldDescriptorType);
        if (obj instanceof zzvw) {
            return zzvw.zzwt();
        }
        return obj;
    }

    private final void zza(FieldDescriptorType fieldDescriptorType, Object obj) {
        Object obj2;
        if (!fieldDescriptorType.zzvy()) {
            zza(fieldDescriptorType.zzvw(), obj);
            obj2 = obj;
        } else if (obj instanceof List) {
            obj2 = new ArrayList();
            obj2.addAll((List) obj);
            ArrayList arrayList = (ArrayList) obj2;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj3 = arrayList.get(i);
                i++;
                zza(fieldDescriptorType.zzvw(), obj3);
            }
        } else {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
        if (obj2 instanceof zzvw) {
            this.zzbvr = true;
        }
        this.zzbvq.zza((Comparable) fieldDescriptorType, obj2);
    }

    private static void zza(zzyq com_google_android_gms_internal_measurement_zzyq, Object obj) {
        boolean z = false;
        zzvo.checkNotNull(obj);
        switch (zzve.zzbvt[com_google_android_gms_internal_measurement_zzyq.zzyp().ordinal()]) {
            case 1:
                z = obj instanceof Integer;
                break;
            case 2:
                z = obj instanceof Long;
                break;
            case 3:
                z = obj instanceof Float;
                break;
            case 4:
                z = obj instanceof Double;
                break;
            case 5:
                z = obj instanceof Boolean;
                break;
            case 6:
                z = obj instanceof String;
                break;
            case 7:
                if ((obj instanceof zzud) || (obj instanceof byte[])) {
                    z = true;
                    break;
                }
            case 8:
                if ((obj instanceof Integer) || (obj instanceof zzvp)) {
                    z = true;
                    break;
                }
            case 9:
                if ((obj instanceof zzwt) || (obj instanceof zzvw)) {
                    z = true;
                    break;
                }
        }
        if (!z) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
    }

    public final boolean isInitialized() {
        for (int i = 0; i < this.zzbvq.zzxw(); i++) {
            if (!zzc(this.zzbvq.zzbu(i))) {
                return false;
            }
        }
        for (Entry zzc : this.zzbvq.zzxx()) {
            if (!zzc(zzc)) {
                return false;
            }
        }
        return true;
    }

    private static boolean zzc(Entry<FieldDescriptorType, Object> entry) {
        zzvf com_google_android_gms_internal_measurement_zzvf = (zzvf) entry.getKey();
        if (com_google_android_gms_internal_measurement_zzvf.zzvx() == zzyv.MESSAGE) {
            if (com_google_android_gms_internal_measurement_zzvf.zzvy()) {
                for (zzwt isInitialized : (List) entry.getValue()) {
                    if (!isInitialized.isInitialized()) {
                        return false;
                    }
                }
            }
            Object value = entry.getValue();
            if (value instanceof zzwt) {
                if (!((zzwt) value).isInitialized()) {
                    return false;
                }
            } else if (value instanceof zzvw) {
                return true;
            } else {
                throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            }
        }
        return true;
    }

    public final void zza(zzvd<FieldDescriptorType> com_google_android_gms_internal_measurement_zzvd_FieldDescriptorType) {
        for (int i = 0; i < com_google_android_gms_internal_measurement_zzvd_FieldDescriptorType.zzbvq.zzxw(); i++) {
            zzd(com_google_android_gms_internal_measurement_zzvd_FieldDescriptorType.zzbvq.zzbu(i));
        }
        for (Entry zzd : com_google_android_gms_internal_measurement_zzvd_FieldDescriptorType.zzbvq.zzxx()) {
            zzd(zzd);
        }
    }

    private static Object zzv(Object obj) {
        if (obj instanceof zzwz) {
            return ((zzwz) obj).zzxj();
        }
        if (!(obj instanceof byte[])) {
            return obj;
        }
        byte[] bArr = (byte[]) obj;
        Object obj2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, obj2, 0, bArr.length);
        return obj2;
    }

    private final void zzd(Entry<FieldDescriptorType, Object> entry) {
        Comparable comparable = (zzvf) entry.getKey();
        Object value = entry.getValue();
        if (value instanceof zzvw) {
            value = zzvw.zzwt();
        }
        Object zza;
        if (comparable.zzvy()) {
            zza = zza((zzvf) comparable);
            if (zza == null) {
                zza = new ArrayList();
            }
            for (Object zzv : (List) value) {
                ((List) zza).add(zzv(zzv));
            }
            this.zzbvq.zza(comparable, zza);
        } else if (comparable.zzvx() == zzyv.MESSAGE) {
            zza = zza((zzvf) comparable);
            if (zza == null) {
                this.zzbvq.zza(comparable, zzv(value));
                return;
            }
            if (zza instanceof zzwz) {
                value = comparable.zza((zzwz) zza, (zzwz) value);
            } else {
                value = comparable.zza(((zzwt) zza).zzwd(), (zzwt) value).zzwj();
            }
            this.zzbvq.zza(comparable, value);
        } else {
            this.zzbvq.zza(comparable, zzv(value));
        }
    }

    static void zza(zzut com_google_android_gms_internal_measurement_zzut, zzyq com_google_android_gms_internal_measurement_zzyq, int i, Object obj) throws IOException {
        if (com_google_android_gms_internal_measurement_zzyq == zzyq.GROUP) {
            zzvo.zzf((zzwt) obj);
            zzwt com_google_android_gms_internal_measurement_zzwt = (zzwt) obj;
            com_google_android_gms_internal_measurement_zzut.zzc(i, 3);
            com_google_android_gms_internal_measurement_zzwt.zzb(com_google_android_gms_internal_measurement_zzut);
            com_google_android_gms_internal_measurement_zzut.zzc(i, 4);
            return;
        }
        com_google_android_gms_internal_measurement_zzut.zzc(i, com_google_android_gms_internal_measurement_zzyq.zzyq());
        switch (zzve.zzbuu[com_google_android_gms_internal_measurement_zzyq.ordinal()]) {
            case 1:
                com_google_android_gms_internal_measurement_zzut.zzb(((Double) obj).doubleValue());
                return;
            case 2:
                com_google_android_gms_internal_measurement_zzut.zza(((Float) obj).floatValue());
                return;
            case 3:
                com_google_android_gms_internal_measurement_zzut.zzav(((Long) obj).longValue());
                return;
            case 4:
                com_google_android_gms_internal_measurement_zzut.zzav(((Long) obj).longValue());
                return;
            case 5:
                com_google_android_gms_internal_measurement_zzut.zzax(((Integer) obj).intValue());
                return;
            case 6:
                com_google_android_gms_internal_measurement_zzut.zzax(((Long) obj).longValue());
                return;
            case 7:
                com_google_android_gms_internal_measurement_zzut.zzba(((Integer) obj).intValue());
                return;
            case 8:
                com_google_android_gms_internal_measurement_zzut.zzu(((Boolean) obj).booleanValue());
                return;
            case 9:
                ((zzwt) obj).zzb(com_google_android_gms_internal_measurement_zzut);
                return;
            case 10:
                com_google_android_gms_internal_measurement_zzut.zzb((zzwt) obj);
                return;
            case 11:
                if (obj instanceof zzud) {
                    com_google_android_gms_internal_measurement_zzut.zza((zzud) obj);
                    return;
                } else {
                    com_google_android_gms_internal_measurement_zzut.zzfw((String) obj);
                    return;
                }
            case 12:
                if (obj instanceof zzud) {
                    com_google_android_gms_internal_measurement_zzut.zza((zzud) obj);
                    return;
                }
                byte[] bArr = (byte[]) obj;
                com_google_android_gms_internal_measurement_zzut.zze(bArr, 0, bArr.length);
                return;
            case 13:
                com_google_android_gms_internal_measurement_zzut.zzay(((Integer) obj).intValue());
                return;
            case 14:
                com_google_android_gms_internal_measurement_zzut.zzba(((Integer) obj).intValue());
                return;
            case 15:
                com_google_android_gms_internal_measurement_zzut.zzax(((Long) obj).longValue());
                return;
            case 16:
                com_google_android_gms_internal_measurement_zzut.zzaz(((Integer) obj).intValue());
                return;
            case 17:
                com_google_android_gms_internal_measurement_zzut.zzaw(((Long) obj).longValue());
                return;
            case 18:
                if (obj instanceof zzvp) {
                    com_google_android_gms_internal_measurement_zzut.zzax(((zzvp) obj).zzc());
                    return;
                } else {
                    com_google_android_gms_internal_measurement_zzut.zzax(((Integer) obj).intValue());
                    return;
                }
            default:
                return;
        }
    }

    public final int zzvu() {
        int i = 0;
        for (int i2 = 0; i2 < this.zzbvq.zzxw(); i2++) {
            Entry zzbu = this.zzbvq.zzbu(i2);
            i += zzb((zzvf) zzbu.getKey(), zzbu.getValue());
        }
        for (Entry entry : this.zzbvq.zzxx()) {
            i += zzb((zzvf) entry.getKey(), entry.getValue());
        }
        return i;
    }

    public final int zzvv() {
        int i = 0;
        int i2 = 0;
        while (i < this.zzbvq.zzxw()) {
            i++;
            i2 = zze(this.zzbvq.zzbu(i)) + i2;
        }
        for (Entry zze : this.zzbvq.zzxx()) {
            i2 += zze(zze);
        }
        return i2;
    }

    private static int zze(Entry<FieldDescriptorType, Object> entry) {
        zzvf com_google_android_gms_internal_measurement_zzvf = (zzvf) entry.getKey();
        Object value = entry.getValue();
        if (com_google_android_gms_internal_measurement_zzvf.zzvx() != zzyv.MESSAGE || com_google_android_gms_internal_measurement_zzvf.zzvy() || com_google_android_gms_internal_measurement_zzvf.zzvz()) {
            return zzb(com_google_android_gms_internal_measurement_zzvf, value);
        }
        if (value instanceof zzvw) {
            return zzut.zzb(((zzvf) entry.getKey()).zzc(), (zzvw) value);
        }
        return zzut.zzd(((zzvf) entry.getKey()).zzc(), (zzwt) value);
    }

    static int zza(zzyq com_google_android_gms_internal_measurement_zzyq, int i, Object obj) {
        int i2;
        int zzbb = zzut.zzbb(i);
        if (com_google_android_gms_internal_measurement_zzyq == zzyq.GROUP) {
            zzvo.zzf((zzwt) obj);
            i2 = zzbb << 1;
        } else {
            i2 = zzbb;
        }
        return i2 + zzb(com_google_android_gms_internal_measurement_zzyq, obj);
    }

    private static int zzb(zzyq com_google_android_gms_internal_measurement_zzyq, Object obj) {
        switch (zzve.zzbuu[com_google_android_gms_internal_measurement_zzyq.ordinal()]) {
            case 1:
                return zzut.zzc(((Double) obj).doubleValue());
            case 2:
                return zzut.zzb(((Float) obj).floatValue());
            case 3:
                return zzut.zzay(((Long) obj).longValue());
            case 4:
                return zzut.zzaz(((Long) obj).longValue());
            case 5:
                return zzut.zzbc(((Integer) obj).intValue());
            case 6:
                return zzut.zzbb(((Long) obj).longValue());
            case 7:
                return zzut.zzbf(((Integer) obj).intValue());
            case 8:
                return zzut.zzv(((Boolean) obj).booleanValue());
            case 9:
                return zzut.zzd((zzwt) obj);
            case 10:
                if (obj instanceof zzvw) {
                    return zzut.zza((zzvw) obj);
                }
                return zzut.zzc((zzwt) obj);
            case 11:
                if (obj instanceof zzud) {
                    return zzut.zzb((zzud) obj);
                }
                return zzut.zzfx((String) obj);
            case 12:
                if (obj instanceof zzud) {
                    return zzut.zzb((zzud) obj);
                }
                return zzut.zzk((byte[]) obj);
            case 13:
                return zzut.zzbd(((Integer) obj).intValue());
            case 14:
                return zzut.zzbg(((Integer) obj).intValue());
            case 15:
                return zzut.zzbc(((Long) obj).longValue());
            case 16:
                return zzut.zzbe(((Integer) obj).intValue());
            case 17:
                return zzut.zzba(((Long) obj).longValue());
            case 18:
                if (obj instanceof zzvp) {
                    return zzut.zzbh(((zzvp) obj).zzc());
                }
                return zzut.zzbh(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    private static int zzb(zzvf<?> com_google_android_gms_internal_measurement_zzvf_, Object obj) {
        int i = 0;
        zzyq zzvw = com_google_android_gms_internal_measurement_zzvf_.zzvw();
        int zzc = com_google_android_gms_internal_measurement_zzvf_.zzc();
        if (!com_google_android_gms_internal_measurement_zzvf_.zzvy()) {
            return zza(zzvw, zzc, obj);
        }
        if (com_google_android_gms_internal_measurement_zzvf_.zzvz()) {
            for (Object zzb : (List) obj) {
                i += zzb(zzvw, zzb);
            }
            return zzut.zzbj(i) + (zzut.zzbb(zzc) + i);
        }
        for (Object zzb2 : (List) obj) {
            i += zza(zzvw, zzc, zzb2);
        }
        return i;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzvd com_google_android_gms_internal_measurement_zzvd = new zzvd();
        for (int i = 0; i < this.zzbvq.zzxw(); i++) {
            Entry zzbu = this.zzbvq.zzbu(i);
            com_google_android_gms_internal_measurement_zzvd.zza((zzvf) zzbu.getKey(), zzbu.getValue());
        }
        for (Entry entry : this.zzbvq.zzxx()) {
            com_google_android_gms_internal_measurement_zzvd.zza((zzvf) entry.getKey(), entry.getValue());
        }
        com_google_android_gms_internal_measurement_zzvd.zzbvr = this.zzbvr;
        return com_google_android_gms_internal_measurement_zzvd;
    }
}
