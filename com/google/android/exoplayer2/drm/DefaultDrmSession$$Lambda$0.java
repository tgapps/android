package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher.Event;

final /* synthetic */ class DefaultDrmSession$$Lambda$0 implements Event {
    static final Event $instance = new DefaultDrmSession$$Lambda$0();

    private DefaultDrmSession$$Lambda$0() {
    }

    public void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmKeysRestored();
    }
}
