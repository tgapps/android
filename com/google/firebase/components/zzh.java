package com.google.firebase.components;

import com.google.firebase.inject.Provider;

/* compiled from: com.google.firebase:firebase-common@@16.0.1 */
final class zzh<T> implements Provider<T> {
    private static final Object zza = new Object();
    private volatile Object zzb = zza;
    private volatile Provider<T> zzc;

    zzh(ComponentFactory<T> componentFactory, ComponentContainer componentContainer) {
        this.zzc = new zzi(componentFactory, componentContainer);
    }

    public final T get() {
        T t = this.zzb;
        if (t == zza) {
            synchronized (this) {
                t = this.zzb;
                if (t == zza) {
                    t = this.zzc.get();
                    this.zzb = t;
                    this.zzc = null;
                }
            }
        }
        return t;
    }
}
