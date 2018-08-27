package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData;

final /* synthetic */ class MediaSourceEventListener$EventDispatcher$$Lambda$8 implements Runnable {
    private final EventDispatcher arg$1;
    private final MediaSourceEventListener arg$2;
    private final MediaLoadData arg$3;

    MediaSourceEventListener$EventDispatcher$$Lambda$8(EventDispatcher eventDispatcher, MediaSourceEventListener mediaSourceEventListener, MediaLoadData mediaLoadData) {
        this.arg$1 = eventDispatcher;
        this.arg$2 = mediaSourceEventListener;
        this.arg$3 = mediaLoadData;
    }

    public void run() {
        this.arg$1.lambda$downstreamFormatChanged$8$MediaSourceEventListener$EventDispatcher(this.arg$2, this.arg$3);
    }
}
