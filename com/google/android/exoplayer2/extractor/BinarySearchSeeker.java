package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.SeekMap.SeekPoints;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;

public abstract class BinarySearchSeeker {
    private static final long MAX_SKIP_BYTES = 262144;
    private final int minimumSearchRange;
    protected final BinarySearchSeekMap seekMap;
    protected SeekOperationParams seekOperationParams;
    protected final TimestampSeeker timestampSeeker;

    public static final class OutputFrameHolder {
        public ByteBuffer byteBuffer;
        public long timeUs = 0;

        public OutputFrameHolder(ByteBuffer outputByteBuffer) {
            this.byteBuffer = outputByteBuffer;
        }
    }

    protected static class SeekOperationParams {
        private final long approxBytesPerFrame;
        private long ceilingBytePosition;
        private long ceilingTimePosition;
        private long floorBytePosition;
        private long floorTimePosition;
        private long nextSearchBytePosition;
        private final long seekTimeUs;
        private final long targetTimePosition;

        protected static long calculateNextSearchBytePosition(long targetTimePosition, long floorTimePosition, long ceilingTimePosition, long floorBytePosition, long ceilingBytePosition, long approxBytesPerFrame) {
            if (1 + floorBytePosition >= ceilingBytePosition || 1 + floorTimePosition >= ceilingTimePosition) {
                return floorBytePosition;
            }
            long bytesToSkip = (long) (((float) (targetTimePosition - floorTimePosition)) * (((float) (ceilingBytePosition - floorBytePosition)) / ((float) (ceilingTimePosition - floorTimePosition))));
            return Util.constrainValue(((floorBytePosition + bytesToSkip) - approxBytesPerFrame) - (bytesToSkip / 20), floorBytePosition, ceilingBytePosition - 1);
        }

        protected SeekOperationParams(long seekTimeUs, long targetTimePosition, long floorTimePosition, long ceilingTimePosition, long floorBytePosition, long ceilingBytePosition, long approxBytesPerFrame) {
            this.seekTimeUs = seekTimeUs;
            this.targetTimePosition = targetTimePosition;
            this.floorTimePosition = floorTimePosition;
            this.ceilingTimePosition = ceilingTimePosition;
            this.floorBytePosition = floorBytePosition;
            this.ceilingBytePosition = ceilingBytePosition;
            this.approxBytesPerFrame = approxBytesPerFrame;
            this.nextSearchBytePosition = calculateNextSearchBytePosition(targetTimePosition, floorTimePosition, ceilingTimePosition, floorBytePosition, ceilingBytePosition, approxBytesPerFrame);
        }

        private long getFloorBytePosition() {
            return this.floorBytePosition;
        }

        private long getCeilingBytePosition() {
            return this.ceilingBytePosition;
        }

        private long getTargetTimePosition() {
            return this.targetTimePosition;
        }

        private long getSeekTimeUs() {
            return this.seekTimeUs;
        }

        private void updateSeekFloor(long floorTimePosition, long floorBytePosition) {
            this.floorTimePosition = floorTimePosition;
            this.floorBytePosition = floorBytePosition;
            updateNextSearchBytePosition();
        }

        private void updateSeekCeiling(long ceilingTimePosition, long ceilingBytePosition) {
            this.ceilingTimePosition = ceilingTimePosition;
            this.ceilingBytePosition = ceilingBytePosition;
            updateNextSearchBytePosition();
        }

        private long getNextSearchBytePosition() {
            return this.nextSearchBytePosition;
        }

        private void updateNextSearchBytePosition() {
            this.nextSearchBytePosition = calculateNextSearchBytePosition(this.targetTimePosition, this.floorTimePosition, this.ceilingTimePosition, this.floorBytePosition, this.ceilingBytePosition, this.approxBytesPerFrame);
        }
    }

    protected interface SeekTimestampConverter {
        long timeUsToTargetTime(long j);
    }

    public static final class TimestampSearchResult {
        public static final TimestampSearchResult NO_TIMESTAMP_IN_RANGE_RESULT = new TimestampSearchResult(-3, C.TIME_UNSET, -1);
        public static final int RESULT_NO_TIMESTAMP = -3;
        public static final int RESULT_POSITION_OVERESTIMATED = -1;
        public static final int RESULT_POSITION_UNDERESTIMATED = -2;
        public static final int RESULT_TARGET_TIMESTAMP_FOUND = 0;
        private final long bytePositionToUpdate;
        private final int result;
        private final long timestampToUpdate;

        @Retention(RetentionPolicy.SOURCE)
        @interface SearchResult {
        }

        private TimestampSearchResult(int result, long timestampToUpdate, long bytePositionToUpdate) {
            this.result = result;
            this.timestampToUpdate = timestampToUpdate;
            this.bytePositionToUpdate = bytePositionToUpdate;
        }

        public static TimestampSearchResult overestimatedResult(long newCeilingTimestamp, long newCeilingBytePosition) {
            return new TimestampSearchResult(-1, newCeilingTimestamp, newCeilingBytePosition);
        }

        public static TimestampSearchResult underestimatedResult(long newFloorTimestamp, long newCeilingBytePosition) {
            return new TimestampSearchResult(-2, newFloorTimestamp, newCeilingBytePosition);
        }

        public static TimestampSearchResult targetFoundResult(long resultBytePosition) {
            return new TimestampSearchResult(0, C.TIME_UNSET, resultBytePosition);
        }
    }

    protected interface TimestampSeeker {
        int getTimestampSearchBytesRange();

        TimestampSearchResult searchForTimestamp(ExtractorInput extractorInput, long j, OutputFrameHolder outputFrameHolder) throws IOException, InterruptedException;
    }

    public static class BinarySearchSeekMap implements SeekMap {
        private final long approxBytesPerFrame;
        private final long ceilingBytePosition;
        private final long ceilingTimePosition;
        private final long durationUs;
        private final long floorBytePosition;
        private final long floorTimePosition;
        private final SeekTimestampConverter seekTimestampConverter;

        public BinarySearchSeekMap(SeekTimestampConverter seekTimestampConverter, long durationUs, long floorTimePosition, long ceilingTimePosition, long floorBytePosition, long ceilingBytePosition, long approxBytesPerFrame) {
            this.seekTimestampConverter = seekTimestampConverter;
            this.durationUs = durationUs;
            this.floorTimePosition = floorTimePosition;
            this.ceilingTimePosition = ceilingTimePosition;
            this.floorBytePosition = floorBytePosition;
            this.ceilingBytePosition = ceilingBytePosition;
            this.approxBytesPerFrame = approxBytesPerFrame;
        }

        public boolean isSeekable() {
            return true;
        }

        public SeekPoints getSeekPoints(long timeUs) {
            return new SeekPoints(new SeekPoint(timeUs, SeekOperationParams.calculateNextSearchBytePosition(this.seekTimestampConverter.timeUsToTargetTime(timeUs), this.floorTimePosition, this.ceilingTimePosition, this.floorBytePosition, this.ceilingBytePosition, this.approxBytesPerFrame)));
        }

        public long getDurationUs() {
            return this.durationUs;
        }

        public long timeUsToTargetTime(long timeUs) {
            return this.seekTimestampConverter.timeUsToTargetTime(timeUs);
        }
    }

    private static final class DefaultSeekTimestampConverter implements SeekTimestampConverter {
        private DefaultSeekTimestampConverter() {
        }

        public long timeUsToTargetTime(long timeUs) {
            return timeUs;
        }
    }

    protected BinarySearchSeeker(SeekTimestampConverter seekTimestampConverter, TimestampSeeker timestampSeeker, long durationUs, long floorTimePosition, long ceilingTimePosition, long floorBytePosition, long ceilingBytePosition, long approxBytesPerFrame, int minimumSearchRange) {
        this.timestampSeeker = timestampSeeker;
        this.minimumSearchRange = minimumSearchRange;
        this.seekMap = new BinarySearchSeekMap(seekTimestampConverter, durationUs, floorTimePosition, ceilingTimePosition, floorBytePosition, ceilingBytePosition, approxBytesPerFrame);
    }

    public final SeekMap getSeekMap() {
        return this.seekMap;
    }

    public final void setSeekTargetUs(long timeUs) {
        if (this.seekOperationParams == null || this.seekOperationParams.getSeekTimeUs() != timeUs) {
            this.seekOperationParams = createSeekParamsForTargetTimeUs(timeUs);
        }
    }

    public final boolean isSeeking() {
        return this.seekOperationParams != null;
    }

    public int handlePendingSeek(ExtractorInput input, PositionHolder seekPositionHolder, OutputFrameHolder outputFrameHolder) throws InterruptedException, IOException {
        TimestampSeeker timestampSeeker = (TimestampSeeker) Assertions.checkNotNull(this.timestampSeeker);
        while (true) {
            SeekOperationParams seekOperationParams = (SeekOperationParams) Assertions.checkNotNull(this.seekOperationParams);
            long floorPosition = seekOperationParams.getFloorBytePosition();
            long ceilingPosition = seekOperationParams.getCeilingBytePosition();
            long searchPosition = seekOperationParams.getNextSearchBytePosition();
            if (ceilingPosition - floorPosition <= ((long) this.minimumSearchRange)) {
                markSeekOperationFinished(false, floorPosition);
                return seekToPosition(input, floorPosition, seekPositionHolder);
            } else if (!skipInputUntilPosition(input, searchPosition)) {
                return seekToPosition(input, searchPosition, seekPositionHolder);
            } else {
                input.resetPeekPosition();
                TimestampSearchResult timestampSearchResult = timestampSeeker.searchForTimestamp(input, seekOperationParams.getTargetTimePosition(), outputFrameHolder);
                switch (timestampSearchResult.result) {
                    case -3:
                        markSeekOperationFinished(false, searchPosition);
                        return seekToPosition(input, searchPosition, seekPositionHolder);
                    case -2:
                        seekOperationParams.updateSeekFloor(timestampSearchResult.timestampToUpdate, timestampSearchResult.bytePositionToUpdate);
                        break;
                    case -1:
                        seekOperationParams.updateSeekCeiling(timestampSearchResult.timestampToUpdate, timestampSearchResult.bytePositionToUpdate);
                        break;
                    case 0:
                        markSeekOperationFinished(true, timestampSearchResult.bytePositionToUpdate);
                        skipInputUntilPosition(input, timestampSearchResult.bytePositionToUpdate);
                        return seekToPosition(input, timestampSearchResult.bytePositionToUpdate, seekPositionHolder);
                    default:
                        throw new IllegalStateException("Invalid case");
                }
            }
        }
    }

    protected SeekOperationParams createSeekParamsForTargetTimeUs(long timeUs) {
        return new SeekOperationParams(timeUs, this.seekMap.timeUsToTargetTime(timeUs), this.seekMap.floorTimePosition, this.seekMap.ceilingTimePosition, this.seekMap.floorBytePosition, this.seekMap.ceilingBytePosition, this.seekMap.approxBytesPerFrame);
    }

    protected final void markSeekOperationFinished(boolean foundTargetFrame, long resultPosition) {
        this.seekOperationParams = null;
        onSeekOperationFinished(foundTargetFrame, resultPosition);
    }

    protected void onSeekOperationFinished(boolean foundTargetFrame, long resultPosition) {
    }

    protected final boolean skipInputUntilPosition(ExtractorInput input, long position) throws IOException, InterruptedException {
        long bytesToSkip = position - input.getPosition();
        if (bytesToSkip < 0 || bytesToSkip > MAX_SKIP_BYTES) {
            return false;
        }
        input.skipFully((int) bytesToSkip);
        return true;
    }

    protected final int seekToPosition(ExtractorInput input, long position, PositionHolder seekPositionHolder) {
        if (position == input.getPosition()) {
            return 0;
        }
        seekPositionHolder.position = position;
        return 1;
    }
}
