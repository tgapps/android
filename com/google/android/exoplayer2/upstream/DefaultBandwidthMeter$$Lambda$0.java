package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.BandwidthMeter.EventListener;
import com.google.android.exoplayer2.util.EventDispatcher.Event;

final /* synthetic */ class DefaultBandwidthMeter$$Lambda$0 implements Event {
    private final int arg$1;
    private final long arg$2;
    private final long arg$3;

    DefaultBandwidthMeter$$Lambda$0(int i, long j, long j2) {
        this.arg$1 = i;
        this.arg$2 = j;
        this.arg$3 = j2;
    }

    public void sendTo(Object obj) {
        ((EventListener) obj).onBandwidthSample(this.arg$1, this.arg$2, this.arg$3);
    }
}
