package com.google.android.exoplayer2.source;

@Deprecated
public final class DynamicConcatenatingMediaSource extends ConcatenatingMediaSource {
    @Deprecated
    public DynamicConcatenatingMediaSource() {
        super(new MediaSource[0]);
    }

    @Deprecated
    public DynamicConcatenatingMediaSource(boolean isAtomic) {
        super(isAtomic, new MediaSource[0]);
    }

    @Deprecated
    public DynamicConcatenatingMediaSource(boolean isAtomic, ShuffleOrder shuffleOrder) {
        super(isAtomic, shuffleOrder, new MediaSource[0]);
    }
}
