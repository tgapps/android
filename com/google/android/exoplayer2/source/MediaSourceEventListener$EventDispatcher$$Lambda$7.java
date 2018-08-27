package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData;

final /* synthetic */ class MediaSourceEventListener$EventDispatcher$$Lambda$7 implements Runnable {
    private final EventDispatcher arg$1;
    private final MediaSourceEventListener arg$2;
    private final MediaPeriodId arg$3;
    private final MediaLoadData arg$4;

    MediaSourceEventListener$EventDispatcher$$Lambda$7(EventDispatcher eventDispatcher, MediaSourceEventListener mediaSourceEventListener, MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
        this.arg$1 = eventDispatcher;
        this.arg$2 = mediaSourceEventListener;
        this.arg$3 = mediaPeriodId;
        this.arg$4 = mediaLoadData;
    }

    public void run() {
        this.arg$1.lambda$upstreamDiscarded$7$MediaSourceEventListener$EventDispatcher(this.arg$2, this.arg$3, this.arg$4);
    }
}
