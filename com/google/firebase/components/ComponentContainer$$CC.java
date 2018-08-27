package com.google.firebase.components;

import com.google.firebase.inject.Provider;

/* compiled from: com.google.firebase:firebase-common@@16.0.1 */
public abstract /* synthetic */ class ComponentContainer$$CC {
    public static Object get(ComponentContainer this_, Class anInterface) {
        Provider provider = this_.getProvider(anInterface);
        if (provider == null) {
            return null;
        }
        return provider.get();
    }
}
