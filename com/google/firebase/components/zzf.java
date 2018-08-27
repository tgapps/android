package com.google.firebase.components;

import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.events.Event;
import com.google.firebase.events.EventHandler;
import com.google.firebase.events.Publisher;
import com.google.firebase.events.Subscriber;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/* compiled from: com.google.firebase:firebase-common@@16.0.1 */
class zzf implements Publisher, Subscriber {
    private final Map<Class<?>, ConcurrentHashMap<EventHandler<Object>, Executor>> zza = new HashMap();
    private Queue<Event<?>> zzb = new ArrayDeque();
    private final Executor zzc;

    zzf(Executor executor) {
        this.zzc = executor;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void publish(com.google.firebase.events.Event<?> r5) {
        /*
        r4 = this;
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r5);
        monitor-enter(r4);
        r0 = r4.zzb;	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x000f;
    L_0x0008:
        r0 = r4.zzb;	 Catch:{ all -> 0x0033 }
        r0.add(r5);	 Catch:{ all -> 0x0033 }
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
    L_0x000e:
        return;
    L_0x000f:
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
        r0 = r4.zza(r5);
        r2 = r0.iterator();
    L_0x0018:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x000e;
    L_0x001e:
        r0 = r2.next();
        r0 = (java.util.Map.Entry) r0;
        r1 = r0.getValue();
        r1 = (java.util.concurrent.Executor) r1;
        r3 = new com.google.firebase.components.zzg;
        r3.<init>(r0, r5);
        r1.execute(r3);
        goto L_0x0018;
    L_0x0033:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.components.zzf.publish(com.google.firebase.events.Event):void");
    }

    private synchronized Set<Entry<EventHandler<Object>, Executor>> zza(Event<?> event) {
        Map map;
        map = (Map) this.zza.get(event.getType());
        return map == null ? Collections.emptySet() : map.entrySet();
    }

    public synchronized <T> void subscribe(Class<T> type, Executor executor, EventHandler<? super T> handler) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(handler);
        Preconditions.checkNotNull(executor);
        if (!this.zza.containsKey(type)) {
            this.zza.put(type, new ConcurrentHashMap());
        }
        ((ConcurrentHashMap) this.zza.get(type)).put(handler, executor);
    }

    public <T> void subscribe(Class<T> type, EventHandler<? super T> handler) {
        subscribe(type, this.zzc, handler);
    }

    final void zza() {
        Queue queue = null;
        synchronized (this) {
            if (this.zzb != null) {
                queue = this.zzb;
                this.zzb = null;
            }
        }
        if (r0 != null) {
            for (Event publish : r0) {
                publish(publish);
            }
        }
    }
}
