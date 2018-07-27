package com.google.firebase.components;

import com.google.firebase.inject.Provider;

public abstract /* synthetic */ class ComponentContainer$$CC {
    public static Object get(ComponentContainer this_, Class anInterface) {
        Provider provider = this_.getProvider(anInterface);
        if (provider == null) {
            return null;
        }
        return provider.get();
    }
}
