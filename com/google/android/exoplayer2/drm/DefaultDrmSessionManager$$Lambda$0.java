package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager.MissingSchemeDataException;
import com.google.android.exoplayer2.util.EventDispatcher.Event;

final /* synthetic */ class DefaultDrmSessionManager$$Lambda$0 implements Event {
    private final MissingSchemeDataException arg$1;

    DefaultDrmSessionManager$$Lambda$0(MissingSchemeDataException missingSchemeDataException) {
        this.arg$1 = missingSchemeDataException;
    }

    public void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmSessionManagerError(this.arg$1);
    }
}
