package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.util.EventDispatcher.Event;

final /* synthetic */ class EventDispatcher$$Lambda$0 implements Runnable {
    private final Event arg$1;
    private final Object arg$2;

    EventDispatcher$$Lambda$0(Event event, Object obj) {
        this.arg$1 = event;
        this.arg$2 = obj;
    }

    public void run() {
        this.arg$1.sendTo(this.arg$2);
    }
}
