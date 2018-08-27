package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

final class zzwf extends zzwd {
    private static final Class<?> zzcal = Collections.unmodifiableList(Collections.emptyList()).getClass();

    private zzwf() {
        super();
    }

    final <L> List<L> zza(Object obj, long j) {
        return zza(obj, j, 10);
    }

    final void zzb(Object obj, long j) {
        Object zzww;
        List list = (List) zzyh.zzp(obj, j);
        if (list instanceof zzwc) {
            zzww = ((zzwc) list).zzww();
        } else if (!zzcal.isAssignableFrom(list.getClass())) {
            if (!(list instanceof zzxe) || !(list instanceof zzvs)) {
                zzww = Collections.unmodifiableList(list);
            } else if (((zzvs) list).zztw()) {
                ((zzvs) list).zzsm();
                return;
            } else {
                return;
            }
        } else {
            return;
        }
        zzyh.zza(obj, j, zzww);
    }

    private static <L> List<L> zza(Object obj, long j, int i) {
        List<L> zzc = zzc(obj, j);
        Object com_google_android_gms_internal_measurement_zzwb;
        if (zzc.isEmpty()) {
            if (zzc instanceof zzwc) {
                com_google_android_gms_internal_measurement_zzwb = new zzwb(i);
            } else if ((zzc instanceof zzxe) && (zzc instanceof zzvs)) {
                com_google_android_gms_internal_measurement_zzwb = ((zzvs) zzc).zzak(i);
            } else {
                com_google_android_gms_internal_measurement_zzwb = new ArrayList(i);
            }
            zzyh.zza(obj, j, com_google_android_gms_internal_measurement_zzwb);
            return com_google_android_gms_internal_measurement_zzwb;
        } else if (zzcal.isAssignableFrom(zzc.getClass())) {
            r1 = new ArrayList(zzc.size() + i);
            r1.addAll(zzc);
            zzyh.zza(obj, j, (Object) r1);
            return r1;
        } else if (zzc instanceof zzye) {
            r1 = new zzwb(zzc.size() + i);
            r1.addAll((zzye) zzc);
            zzyh.zza(obj, j, (Object) r1);
            return r1;
        } else if (!(zzc instanceof zzxe) || !(zzc instanceof zzvs) || ((zzvs) zzc).zztw()) {
            return zzc;
        } else {
            com_google_android_gms_internal_measurement_zzwb = ((zzvs) zzc).zzak(zzc.size() + i);
            zzyh.zza(obj, j, com_google_android_gms_internal_measurement_zzwb);
            return com_google_android_gms_internal_measurement_zzwb;
        }
    }

    final <E> void zza(Object obj, Object obj2, long j) {
        Collection zzc = zzc(obj2, j);
        Object zza = zza(obj, j, zzc.size());
        int size = zza.size();
        int size2 = zzc.size();
        if (size > 0 && size2 > 0) {
            zza.addAll(zzc);
        }
        if (size <= 0) {
            Collection collection = zzc;
        }
        zzyh.zza(obj, j, zza);
    }

    private static <E> List<E> zzc(Object obj, long j) {
        return (List) zzyh.zzp(obj, j);
    }
}
