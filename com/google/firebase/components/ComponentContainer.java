package com.google.firebase.components;

import com.google.firebase.inject.Provider;

public interface ComponentContainer {
    <T> Provider<T> getProvider(Class<T> cls);
}
