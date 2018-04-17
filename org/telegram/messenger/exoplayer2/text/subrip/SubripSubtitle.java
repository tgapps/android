package org.telegram.messenger.exoplayer2.text.subrip;

import java.util.Collections;
import java.util.List;
import org.telegram.messenger.exoplayer2.text.Cue;
import org.telegram.messenger.exoplayer2.text.Subtitle;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.Util;

final class SubripSubtitle implements Subtitle {
    private final long[] cueTimesUs;
    private final Cue[] cues;

    public SubripSubtitle(Cue[] cues, long[] cueTimesUs) {
        this.cues = cues;
        this.cueTimesUs = cueTimesUs;
    }

    public int getNextEventTimeIndex(long timeUs) {
        int index = Util.binarySearchCeil(this.cueTimesUs, timeUs, false, false);
        return index < this.cueTimesUs.length ? index : -1;
    }

    public int getEventTimeCount() {
        return this.cueTimesUs.length;
    }

    public long getEventTime(int index) {
        boolean z = false;
        Assertions.checkArgument(index >= 0);
        if (index < this.cueTimesUs.length) {
            z = true;
        }
        Assertions.checkArgument(z);
        return this.cueTimesUs[index];
    }

    public List<Cue> getCues(long timeUs) {
        int index = Util.binarySearchFloor(this.cueTimesUs, timeUs, true, false);
        if (index != -1) {
            if (this.cues[index] != null) {
                return Collections.singletonList(this.cues[index]);
            }
        }
        return Collections.emptyList();
    }
}
