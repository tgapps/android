package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;

public final class DashWrappingSegmentIndex implements DashSegmentIndex {
    private final ChunkIndex chunkIndex;
    private final long timeOffsetUs;

    public DashWrappingSegmentIndex(ChunkIndex chunkIndex, long timeOffsetUs) {
        this.chunkIndex = chunkIndex;
        this.timeOffsetUs = timeOffsetUs;
    }

    public long getFirstSegmentNum() {
        return 0;
    }

    public int getSegmentCount(long periodDurationUs) {
        return this.chunkIndex.length;
    }

    public long getTimeUs(long segmentNum) {
        return this.chunkIndex.timesUs[(int) segmentNum] - this.timeOffsetUs;
    }

    public long getDurationUs(long segmentNum, long periodDurationUs) {
        return this.chunkIndex.durationsUs[(int) segmentNum];
    }

    public RangedUri getSegmentUrl(long segmentNum) {
        return new RangedUri(null, this.chunkIndex.offsets[(int) segmentNum], (long) this.chunkIndex.sizes[(int) segmentNum]);
    }

    public long getSegmentNum(long timeUs, long periodDurationUs) {
        return (long) this.chunkIndex.getChunkIndex(this.timeOffsetUs + timeUs);
    }

    public boolean isExplicit() {
        return true;
    }
}
