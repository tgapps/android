package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher.Event;

final /* synthetic */ class DefaultDrmSession$$Lambda$3 implements Event {
    private final Exception arg$1;

    DefaultDrmSession$$Lambda$3(Exception exception) {
        this.arg$1 = exception;
    }

    public void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmSessionManagerError(this.arg$1);
    }
}
