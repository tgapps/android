package org.telegram.messenger.exoplayer2.source.ads;

import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.Timeline;
import org.telegram.messenger.exoplayer2.Timeline.Period;
import org.telegram.messenger.exoplayer2.Timeline.Window;
import org.telegram.messenger.exoplayer2.source.ForwardingTimeline;
import org.telegram.messenger.exoplayer2.util.Assertions;

final class SinglePeriodAdTimeline extends ForwardingTimeline {
    private final AdPlaybackState adPlaybackState;

    public SinglePeriodAdTimeline(Timeline contentTimeline, AdPlaybackState adPlaybackState) {
        super(contentTimeline);
        boolean z = false;
        Assertions.checkState(contentTimeline.getPeriodCount() == 1);
        if (contentTimeline.getWindowCount() == 1) {
            z = true;
        }
        Assertions.checkState(z);
        this.adPlaybackState = adPlaybackState;
    }

    public Period getPeriod(int periodIndex, Period period, boolean setIds) {
        this.timeline.getPeriod(periodIndex, period, setIds);
        period.set(period.id, period.uid, period.windowIndex, period.durationUs, period.getPositionInWindowUs(), this.adPlaybackState);
        return period;
    }

    public Window getWindow(int windowIndex, Window window, boolean setIds, long defaultPositionProjectionUs) {
        window = super.getWindow(windowIndex, window, setIds, defaultPositionProjectionUs);
        if (window.durationUs == C.TIME_UNSET) {
            window.durationUs = this.adPlaybackState.contentDurationUs;
        }
        return window;
    }
}
