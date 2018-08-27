package com.google.firebase.components;

/* compiled from: com.google.firebase:firebase-common@@16.0.1 */
final /* synthetic */ class zzb implements ComponentFactory {
    private final Object zza;

    zzb(Object obj) {
        this.zza = obj;
    }

    public final Object create(ComponentContainer componentContainer) {
        return Component.zza(this.zza);
    }
}
