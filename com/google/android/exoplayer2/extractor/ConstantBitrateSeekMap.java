package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.SeekMap.SeekPoints;
import com.google.android.exoplayer2.util.Util;

public class ConstantBitrateSeekMap implements SeekMap {
    private final int bitrate;
    private final long dataSize;
    private final long durationUs;
    private final long firstFrameBytePosition;
    private final int frameSize;
    private final long inputLength;

    public ConstantBitrateSeekMap(long inputLength, long firstFrameBytePosition, int bitrate, int frameSize) {
        this.inputLength = inputLength;
        this.firstFrameBytePosition = firstFrameBytePosition;
        if (frameSize == -1) {
            frameSize = 1;
        }
        this.frameSize = frameSize;
        this.bitrate = bitrate;
        if (inputLength == -1) {
            this.dataSize = -1;
            this.durationUs = C.TIME_UNSET;
            return;
        }
        this.dataSize = inputLength - firstFrameBytePosition;
        this.durationUs = getTimeUsAtPosition(inputLength, firstFrameBytePosition, bitrate);
    }

    public boolean isSeekable() {
        return this.dataSize != -1;
    }

    public SeekPoints getSeekPoints(long timeUs) {
        if (this.dataSize == -1) {
            return new SeekPoints(new SeekPoint(0, this.firstFrameBytePosition));
        }
        long seekFramePosition = getFramePositionForTimeUs(timeUs);
        long seekTimeUs = getTimeUsAtPosition(seekFramePosition);
        SeekPoint seekPoint = new SeekPoint(seekTimeUs, seekFramePosition);
        if (seekTimeUs >= timeUs || ((long) this.frameSize) + seekFramePosition >= this.inputLength) {
            return new SeekPoints(seekPoint);
        }
        long secondSeekPosition = seekFramePosition + ((long) this.frameSize);
        return new SeekPoints(seekPoint, new SeekPoint(getTimeUsAtPosition(secondSeekPosition), secondSeekPosition));
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    public long getTimeUsAtPosition(long position) {
        return getTimeUsAtPosition(position, this.firstFrameBytePosition, this.bitrate);
    }

    private static long getTimeUsAtPosition(long position, long firstFrameBytePosition, int bitrate) {
        return ((Math.max(0, position - firstFrameBytePosition) * 8) * 1000000) / ((long) bitrate);
    }

    private long getFramePositionForTimeUs(long timeUs) {
        return this.firstFrameBytePosition + Util.constrainValue((((((long) this.bitrate) * timeUs) / 8000000) / ((long) this.frameSize)) * ((long) this.frameSize), 0, this.dataSize - ((long) this.frameSize));
    }
}
