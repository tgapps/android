package com.google.android.gms.internal.firebase_messaging;

import java.lang.ref.ReferenceQueue;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

final class zzc {
    private final ConcurrentHashMap<zzd, List<Throwable>> zzd = new ConcurrentHashMap(16, 0.75f, 10);
    private final ReferenceQueue<Throwable> zze = new ReferenceQueue();

    zzc() {
    }

    public final List<Throwable> zza(Throwable th, boolean z) {
        Object poll = this.zze.poll();
        while (poll != null) {
            this.zzd.remove(poll);
            poll = this.zze.poll();
        }
        List<Throwable> list = (List) this.zzd.get(new zzd(th, null));
        if (list != null) {
            return list;
        }
        Vector vector = new Vector(2);
        list = (List) this.zzd.putIfAbsent(new zzd(th, this.zze), vector);
        return list == null ? vector : list;
    }
}
