package com.google.firebase.components;

import com.google.firebase.inject.Provider;

public interface ComponentContainer {
    <T> T get(Class<T> cls);

    <T> Provider<T> getProvider(Class<T> cls);
}
