package com.google.firebase.components;

import com.google.firebase.inject.Provider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class zzl implements ComponentContainer {
    private final Set<Class<?>> zzav;
    private final Set<Class<?>> zzaw;
    private final ComponentContainer zzax;

    public zzl(Iterable<Dependency> iterable, ComponentContainer componentContainer) {
        Set hashSet = new HashSet();
        Set hashSet2 = new HashSet();
        for (Dependency dependency : iterable) {
            if (dependency.zzp()) {
                hashSet.add(dependency.zzn());
            } else {
                hashSet2.add(dependency.zzn());
            }
        }
        this.zzav = Collections.unmodifiableSet(hashSet);
        this.zzaw = Collections.unmodifiableSet(hashSet2);
        this.zzax = componentContainer;
    }

    public final <T> Provider<T> getProvider(Class<T> cls) {
        if (this.zzaw.contains(cls)) {
            return this.zzax.getProvider(cls);
        }
        throw new IllegalArgumentException(String.format("Requesting Provider<%s> is not allowed.", new Object[]{cls}));
    }
}
