package com.google.firebase.components;

import com.google.firebase.inject.Provider;

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
