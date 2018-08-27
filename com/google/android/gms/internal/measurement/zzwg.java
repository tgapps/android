package com.google.android.gms.internal.measurement;

import java.util.Collection;
import java.util.List;

final class zzwg extends zzwd {
    private zzwg() {
        super();
    }

    final <L> List<L> zza(Object obj, long j) {
        zzvs zzd = zzd(obj, j);
        if (zzd.zztw()) {
            return zzd;
        }
        int size = zzd.size();
        Object zzak = zzd.zzak(size == 0 ? 10 : size << 1);
        zzyh.zza(obj, j, zzak);
        return zzak;
    }

    final void zzb(Object obj, long j) {
        zzd(obj, j).zzsm();
    }

    final <E> void zza(Object obj, Object obj2, long j) {
        Object zzd = zzd(obj, j);
        Collection zzd2 = zzd(obj2, j);
        int size = zzd.size();
        int size2 = zzd2.size();
        if (size > 0 && size2 > 0) {
            if (!zzd.zztw()) {
                zzd = zzd.zzak(size2 + size);
            }
            zzd.addAll(zzd2);
        }
        if (size <= 0) {
            Collection collection = zzd2;
        }
        zzyh.zza(obj, j, zzd);
    }

    private static <E> zzvs<E> zzd(Object obj, long j) {
        return (zzvs) zzyh.zzp(obj, j);
    }
}
