package com.google.firebase.components;

import com.google.firebase.events.Publisher;
import com.google.firebase.inject.Provider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class zzj implements ComponentContainer {
    private final Set<Class<?>> zza;
    private final Set<Class<?>> zzb;
    private final Set<Class<?>> zzc;
    private final ComponentContainer zzd;

    static class zza implements Publisher {
        private final Set<Class<?>> zza;
        private final Publisher zzb;

        public zza(Set<Class<?>> set, Publisher publisher) {
            this.zza = set;
            this.zzb = publisher;
        }
    }

    zzj(Component<?> component, ComponentContainer componentContainer) {
        Set hashSet = new HashSet();
        Set hashSet2 = new HashSet();
        for (Dependency dependency : component.zzb()) {
            if (dependency.zzc()) {
                hashSet.add(dependency.zza());
            } else {
                hashSet2.add(dependency.zza());
            }
        }
        if (!component.zzd().isEmpty()) {
            hashSet.add(Publisher.class);
        }
        this.zza = Collections.unmodifiableSet(hashSet);
        this.zzb = Collections.unmodifiableSet(hashSet2);
        this.zzc = component.zzd();
        this.zzd = componentContainer;
    }

    public final <T> T get(Class<T> anInterface) {
        if (this.zza.contains(anInterface)) {
            T t = this.zzd.get(anInterface);
            if (anInterface.equals(Publisher.class)) {
                return new zza(this.zzc, (Publisher) t);
            }
            return t;
        }
        throw new IllegalArgumentException(String.format("Requesting %s is not allowed.", new Object[]{anInterface}));
    }

    public final <T> Provider<T> getProvider(Class<T> anInterface) {
        if (this.zzb.contains(anInterface)) {
            return this.zzd.getProvider(anInterface);
        }
        throw new IllegalArgumentException(String.format("Requesting Provider<%s> is not allowed.", new Object[]{anInterface}));
    }
}
