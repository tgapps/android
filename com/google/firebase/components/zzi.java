package com.google.firebase.components;

import com.google.firebase.inject.Provider;

/* compiled from: com.google.firebase:firebase-common@@16.0.1 */
final /* synthetic */ class zzi implements Provider {
    private final ComponentFactory zza;
    private final ComponentContainer zzb;

    zzi(ComponentFactory componentFactory, ComponentContainer componentContainer) {
        this.zza = componentFactory;
        this.zzb = componentContainer;
    }

    public final Object get() {
        return this.zza.create(this.zzb);
    }
}
