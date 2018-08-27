package com.google.firebase.events;

/* compiled from: com.google.firebase:firebase-common@@16.0.1 */
public class Event<T> {
    private final Class<T> zza;
    private final T zzb;

    public Class<T> getType() {
        return this.zza;
    }

    public T getPayload() {
        return this.zzb;
    }

    public String toString() {
        return String.format("Event{type: %s, payload: %s}", new Object[]{this.zza, this.zzb});
    }
}
