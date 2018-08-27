package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.MediaSourceEventListener.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData;
import java.io.IOException;

final /* synthetic */ class MediaSourceEventListener$EventDispatcher$$Lambda$5 implements Runnable {
    private final EventDispatcher arg$1;
    private final MediaSourceEventListener arg$2;
    private final LoadEventInfo arg$3;
    private final MediaLoadData arg$4;
    private final IOException arg$5;
    private final boolean arg$6;

    MediaSourceEventListener$EventDispatcher$$Lambda$5(EventDispatcher eventDispatcher, MediaSourceEventListener mediaSourceEventListener, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException iOException, boolean z) {
        this.arg$1 = eventDispatcher;
        this.arg$2 = mediaSourceEventListener;
        this.arg$3 = loadEventInfo;
        this.arg$4 = mediaLoadData;
        this.arg$5 = iOException;
        this.arg$6 = z;
    }

    public void run() {
        this.arg$1.lambda$loadError$5$MediaSourceEventListener$EventDispatcher(this.arg$2, this.arg$3, this.arg$4, this.arg$5, this.arg$6);
    }
}
