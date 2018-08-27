package com.google.android.exoplayer2.source.hls;

import android.util.SparseArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;

public final class TimestampAdjusterProvider {
    private final SparseArray<TimestampAdjuster> timestampAdjusters = new SparseArray();

    public TimestampAdjuster getAdjuster(int discontinuitySequence) {
        TimestampAdjuster adjuster = (TimestampAdjuster) this.timestampAdjusters.get(discontinuitySequence);
        if (adjuster != null) {
            return adjuster;
        }
        adjuster = new TimestampAdjuster(Long.MAX_VALUE);
        this.timestampAdjusters.put(discontinuitySequence, adjuster);
        return adjuster;
    }

    public void reset() {
        this.timestampAdjusters.clear();
    }
}
