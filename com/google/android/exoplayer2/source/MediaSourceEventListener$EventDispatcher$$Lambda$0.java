package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;

final /* synthetic */ class MediaSourceEventListener$EventDispatcher$$Lambda$0 implements Runnable {
    private final EventDispatcher arg$1;
    private final MediaSourceEventListener arg$2;
    private final MediaPeriodId arg$3;

    MediaSourceEventListener$EventDispatcher$$Lambda$0(EventDispatcher eventDispatcher, MediaSourceEventListener mediaSourceEventListener, MediaPeriodId mediaPeriodId) {
        this.arg$1 = eventDispatcher;
        this.arg$2 = mediaSourceEventListener;
        this.arg$3 = mediaPeriodId;
    }

    public void run() {
        this.arg$1.lambda$mediaPeriodCreated$0$MediaSourceEventListener$EventDispatcher(this.arg$2, this.arg$3);
    }
}
