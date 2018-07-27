package com.google.firebase.events;

public class Event<T> {
    private final Class<T> zza;
    private final T zzb;

    public Class<T> getType() {
        return this.zza;
    }

    public String toString() {
        return String.format("Event{type: %s, payload: %s}", new Object[]{this.zza, this.zzb});
    }
}
