package com.google.firebase.events;

import java.util.concurrent.Executor;

/* compiled from: com.google.firebase:firebase-common@@16.0.1 */
public interface Subscriber {
    <T> void subscribe(Class<T> cls, EventHandler<? super T> eventHandler);

    <T> void subscribe(Class<T> cls, Executor executor, EventHandler<? super T> eventHandler);
}
