package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

final class zzxl {
    private static final Class<?> zzcbw = zzxu();
    private static final zzyb<?, ?> zzcbx = zzx(false);
    private static final zzyb<?, ?> zzcby = zzx(true);
    private static final zzyb<?, ?> zzcbz = new zzyd();

    public static void zzj(Class<?> cls) {
        if (!zzvm.class.isAssignableFrom(cls) && zzcbw != null && !zzcbw.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
        }
    }

    public static void zza(int i, List<Double> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzg(i, list, z);
        }
    }

    public static void zzb(int i, List<Float> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzf(i, list, z);
        }
    }

    public static void zzc(int i, List<Long> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzc(i, list, z);
        }
    }

    public static void zzd(int i, List<Long> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzd(i, list, z);
        }
    }

    public static void zze(int i, List<Long> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzn(i, list, z);
        }
    }

    public static void zzf(int i, List<Long> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zze(i, list, z);
        }
    }

    public static void zzg(int i, List<Long> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzl(i, list, z);
        }
    }

    public static void zzh(int i, List<Integer> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zza(i, (List) list, z);
        }
    }

    public static void zzi(int i, List<Integer> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzj(i, list, z);
        }
    }

    public static void zzj(int i, List<Integer> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzm(i, list, z);
        }
    }

    public static void zzk(int i, List<Integer> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzb(i, (List) list, z);
        }
    }

    public static void zzl(int i, List<Integer> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzk(i, list, z);
        }
    }

    public static void zzm(int i, List<Integer> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzh(i, list, z);
        }
    }

    public static void zzn(int i, List<Boolean> list, zzyw com_google_android_gms_internal_measurement_zzyw, boolean z) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzi(i, list, z);
        }
    }

    public static void zza(int i, List<String> list, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zza(i, (List) list);
        }
    }

    public static void zzb(int i, List<zzud> list, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzb(i, (List) list);
        }
    }

    public static void zza(int i, List<?> list, zzyw com_google_android_gms_internal_measurement_zzyw, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zza(i, (List) list, com_google_android_gms_internal_measurement_zzxj);
        }
    }

    public static void zzb(int i, List<?> list, zzyw com_google_android_gms_internal_measurement_zzyw, zzxj com_google_android_gms_internal_measurement_zzxj) throws IOException {
        if (list != null && !list.isEmpty()) {
            com_google_android_gms_internal_measurement_zzyw.zzb(i, (List) list, com_google_android_gms_internal_measurement_zzxj);
        }
    }

    static int zzx(List<Long> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i;
        if (list instanceof zzwh) {
            zzwh com_google_android_gms_internal_measurement_zzwh = (zzwh) list;
            int i2 = 0;
            for (i = 0; i < size; i++) {
                i2 += zzut.zzay(com_google_android_gms_internal_measurement_zzwh.getLong(i));
            }
            return i2;
        }
        i = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i += zzut.zzay(((Long) list.get(i3)).longValue());
        }
        return i;
    }

    static int zzo(int i, List<Long> list, boolean z) {
        if (list.size() == 0) {
            return 0;
        }
        return zzx((List) list) + (list.size() * zzut.zzbb(i));
    }

    static int zzy(List<Long> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i;
        if (list instanceof zzwh) {
            zzwh com_google_android_gms_internal_measurement_zzwh = (zzwh) list;
            int i2 = 0;
            for (i = 0; i < size; i++) {
                i2 += zzut.zzaz(com_google_android_gms_internal_measurement_zzwh.getLong(i));
            }
            return i2;
        }
        i = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i += zzut.zzaz(((Long) list.get(i3)).longValue());
        }
        return i;
    }

    static int zzp(int i, List<Long> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return (size * zzut.zzbb(i)) + zzy(list);
    }

    static int zzz(List<Long> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i;
        if (list instanceof zzwh) {
            zzwh com_google_android_gms_internal_measurement_zzwh = (zzwh) list;
            int i2 = 0;
            for (i = 0; i < size; i++) {
                i2 += zzut.zzba(com_google_android_gms_internal_measurement_zzwh.getLong(i));
            }
            return i2;
        }
        i = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i += zzut.zzba(((Long) list.get(i3)).longValue());
        }
        return i;
    }

    static int zzq(int i, List<Long> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return (size * zzut.zzbb(i)) + zzz(list);
    }

    static int zzaa(List<Integer> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            int i2 = 0;
            for (i = 0; i < size; i++) {
                i2 += zzut.zzbh(com_google_android_gms_internal_measurement_zzvn.getInt(i));
            }
            return i2;
        }
        i = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i += zzut.zzbh(((Integer) list.get(i3)).intValue());
        }
        return i;
    }

    static int zzr(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return (size * zzut.zzbb(i)) + zzaa(list);
    }

    static int zzab(List<Integer> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            int i2 = 0;
            for (i = 0; i < size; i++) {
                i2 += zzut.zzbc(com_google_android_gms_internal_measurement_zzvn.getInt(i));
            }
            return i2;
        }
        i = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i += zzut.zzbc(((Integer) list.get(i3)).intValue());
        }
        return i;
    }

    static int zzs(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return (size * zzut.zzbb(i)) + zzab(list);
    }

    static int zzac(List<Integer> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            int i2 = 0;
            for (i = 0; i < size; i++) {
                i2 += zzut.zzbd(com_google_android_gms_internal_measurement_zzvn.getInt(i));
            }
            return i2;
        }
        i = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i += zzut.zzbd(((Integer) list.get(i3)).intValue());
        }
        return i;
    }

    static int zzt(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return (size * zzut.zzbb(i)) + zzac(list);
    }

    static int zzad(List<Integer> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i;
        if (list instanceof zzvn) {
            zzvn com_google_android_gms_internal_measurement_zzvn = (zzvn) list;
            int i2 = 0;
            for (i = 0; i < size; i++) {
                i2 += zzut.zzbe(com_google_android_gms_internal_measurement_zzvn.getInt(i));
            }
            return i2;
        }
        i = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i += zzut.zzbe(((Integer) list.get(i3)).intValue());
        }
        return i;
    }

    static int zzu(int i, List<Integer> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return (size * zzut.zzbb(i)) + zzad(list);
    }

    static int zzae(List<?> list) {
        return list.size() << 2;
    }

    static int zzv(int i, List<?> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return zzut.zzk(i, 0) * size;
    }

    static int zzaf(List<?> list) {
        return list.size() << 3;
    }

    static int zzw(int i, List<?> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzut.zzg(i, 0);
    }

    static int zzag(List<?> list) {
        return list.size();
    }

    static int zzx(int i, List<?> list, boolean z) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzut.zzc(i, true);
    }

    static int zzc(int i, List<?> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zzbb = zzut.zzbb(i) * size;
        int i2;
        Object raw;
        int zzb;
        if (list instanceof zzwc) {
            zzwc com_google_android_gms_internal_measurement_zzwc = (zzwc) list;
            i2 = 0;
            while (i2 < size) {
                raw = com_google_android_gms_internal_measurement_zzwc.getRaw(i2);
                if (raw instanceof zzud) {
                    zzb = zzut.zzb((zzud) raw) + zzbb;
                } else {
                    zzb = zzut.zzfx((String) raw) + zzbb;
                }
                i2++;
                zzbb = zzb;
            }
            return zzbb;
        }
        i2 = 0;
        while (i2 < size) {
            raw = list.get(i2);
            if (raw instanceof zzud) {
                zzb = zzut.zzb((zzud) raw) + zzbb;
            } else {
                zzb = zzut.zzfx((String) raw) + zzbb;
            }
            i2++;
            zzbb = zzb;
        }
        return zzbb;
    }

    static int zzc(int i, Object obj, zzxj com_google_android_gms_internal_measurement_zzxj) {
        if (obj instanceof zzwa) {
            return zzut.zza(i, (zzwa) obj);
        }
        return zzut.zzb(i, (zzwt) obj, com_google_android_gms_internal_measurement_zzxj);
    }

    static int zzc(int i, List<?> list, zzxj com_google_android_gms_internal_measurement_zzxj) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zzbb = zzut.zzbb(i) * size;
        int i2 = 0;
        while (i2 < size) {
            int zza;
            Object obj = list.get(i2);
            if (obj instanceof zzwa) {
                zza = zzut.zza((zzwa) obj) + zzbb;
            } else {
                zza = zzut.zzb((zzwt) obj, com_google_android_gms_internal_measurement_zzxj) + zzbb;
            }
            i2++;
            zzbb = zza;
        }
        return zzbb;
    }

    static int zzd(int i, List<zzud> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int zzbb = zzut.zzbb(i) * size;
        for (size = 0; size < list.size(); size++) {
            zzbb += zzut.zzb((zzud) list.get(size));
        }
        return zzbb;
    }

    static int zzd(int i, List<zzwt> list, zzxj com_google_android_gms_internal_measurement_zzxj) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i2 += zzut.zzc(i, (zzwt) list.get(i3), com_google_android_gms_internal_measurement_zzxj);
        }
        return i2;
    }

    public static zzyb<?, ?> zzxr() {
        return zzcbx;
    }

    public static zzyb<?, ?> zzxs() {
        return zzcby;
    }

    public static zzyb<?, ?> zzxt() {
        return zzcbz;
    }

    private static zzyb<?, ?> zzx(boolean z) {
        try {
            Class zzxv = zzxv();
            if (zzxv == null) {
                return null;
            }
            return (zzyb) zzxv.getConstructor(new Class[]{Boolean.TYPE}).newInstance(new Object[]{Boolean.valueOf(z)});
        } catch (Throwable th) {
            return null;
        }
    }

    private static Class<?> zzxu() {
        try {
            return Class.forName("com.google.protobuf.GeneratedMessage");
        } catch (Throwable th) {
            return null;
        }
    }

    private static Class<?> zzxv() {
        try {
            return Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable th) {
            return null;
        }
    }

    static boolean zze(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    static <T> void zza(zzwo com_google_android_gms_internal_measurement_zzwo, T t, T t2, long j) {
        zzyh.zza((Object) t, j, com_google_android_gms_internal_measurement_zzwo.zzc(zzyh.zzp(t, j), zzyh.zzp(t2, j)));
    }

    static <T, FT extends zzvf<FT>> void zza(zzva<FT> com_google_android_gms_internal_measurement_zzva_FT, T t, T t2) {
        zzvd zzs = com_google_android_gms_internal_measurement_zzva_FT.zzs(t2);
        if (!zzs.isEmpty()) {
            com_google_android_gms_internal_measurement_zzva_FT.zzt(t).zza(zzs);
        }
    }

    static <T, UT, UB> void zza(zzyb<UT, UB> com_google_android_gms_internal_measurement_zzyb_UT__UB, T t, T t2) {
        com_google_android_gms_internal_measurement_zzyb_UT__UB.zzf(t, com_google_android_gms_internal_measurement_zzyb_UT__UB.zzh(com_google_android_gms_internal_measurement_zzyb_UT__UB.zzah(t), com_google_android_gms_internal_measurement_zzyb_UT__UB.zzah(t2)));
    }

    static <UT, UB> UB zza(int i, List<Integer> list, zzvr com_google_android_gms_internal_measurement_zzvr, UB ub, zzyb<UT, UB> com_google_android_gms_internal_measurement_zzyb_UT__UB) {
        if (com_google_android_gms_internal_measurement_zzvr == null) {
            return ub;
        }
        UB ub2;
        int intValue;
        if (list instanceof RandomAccess) {
            int size = list.size();
            int i2 = 0;
            int i3 = 0;
            ub2 = ub;
            while (i2 < size) {
                intValue = ((Integer) list.get(i2)).intValue();
                if (com_google_android_gms_internal_measurement_zzvr.zzb(intValue)) {
                    if (i2 != i3) {
                        list.set(i3, Integer.valueOf(intValue));
                    }
                    intValue = i3 + 1;
                } else {
                    ub2 = zza(i, intValue, (Object) ub2, (zzyb) com_google_android_gms_internal_measurement_zzyb_UT__UB);
                    intValue = i3;
                }
                i2++;
                i3 = intValue;
            }
            if (i3 != size) {
                list.subList(i3, size).clear();
            }
        } else {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                intValue = ((Integer) it.next()).intValue();
                if (!com_google_android_gms_internal_measurement_zzvr.zzb(intValue)) {
                    ub = zza(i, intValue, (Object) ub, (zzyb) com_google_android_gms_internal_measurement_zzyb_UT__UB);
                    it.remove();
                }
            }
            ub2 = ub;
        }
        return ub2;
    }

    static <UT, UB> UB zza(int i, int i2, UB ub, zzyb<UT, UB> com_google_android_gms_internal_measurement_zzyb_UT__UB) {
        Object zzye;
        if (ub == null) {
            zzye = com_google_android_gms_internal_measurement_zzyb_UT__UB.zzye();
        }
        com_google_android_gms_internal_measurement_zzyb_UT__UB.zza(zzye, i, (long) i2);
        return zzye;
    }
}
