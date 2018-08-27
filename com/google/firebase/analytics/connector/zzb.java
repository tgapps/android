package com.google.firebase.analytics.connector;

import com.google.firebase.events.Event;
import com.google.firebase.events.EventHandler;

final /* synthetic */ class zzb implements EventHandler {
    static final EventHandler zzbsj = new zzb();

    private zzb() {
    }

    public final void handle(Event event) {
        AnalyticsConnectorImpl.zza(event);
    }
}
