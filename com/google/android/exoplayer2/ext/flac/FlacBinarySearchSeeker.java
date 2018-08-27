package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.ext.flac.FlacDecoderJni.FlacFrameDecodeException;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker.OutputFrameHolder;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker.TimestampSearchResult;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import java.io.IOException;
import java.nio.ByteBuffer;

final class FlacBinarySearchSeeker extends BinarySearchSeeker {
    private final FlacDecoderJni decoderJni;

    private static final class FlacSeekTimestampConverter implements SeekTimestampConverter {
        private final FlacStreamInfo streamInfo;

        public FlacSeekTimestampConverter(FlacStreamInfo streamInfo) {
            this.streamInfo = streamInfo;
        }

        public long timeUsToTargetTime(long timeUs) {
            return ((FlacStreamInfo) Assertions.checkNotNull(this.streamInfo)).getSampleIndex(timeUs);
        }
    }

    private static final class FlacTimestampSeeker implements TimestampSeeker {
        private final FlacDecoderJni decoderJni;

        private FlacTimestampSeeker(FlacDecoderJni decoderJni) {
            this.decoderJni = decoderJni;
        }

        public TimestampSearchResult searchForTimestamp(ExtractorInput input, long targetSampleIndex, OutputFrameHolder outputFrameHolder) throws IOException, InterruptedException {
            ByteBuffer outputBuffer = outputFrameHolder.byteBuffer;
            long searchPosition = input.getPosition();
            int searchRangeBytes = getTimestampSearchBytesRange();
            this.decoderJni.reset(searchPosition);
            try {
                this.decoderJni.decodeSampleWithBacktrackPosition(outputBuffer, searchPosition);
                if (outputBuffer.limit() == 0) {
                    return TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
                }
                long lastFrameSampleIndex = this.decoderJni.getLastFrameFirstSampleIndex();
                long nextFrameSampleIndex = this.decoderJni.getNextFrameFirstSampleIndex();
                long nextFrameSamplePosition = this.decoderJni.getDecodePosition();
                boolean targetSampleInLastFrame = lastFrameSampleIndex <= targetSampleIndex && nextFrameSampleIndex > targetSampleIndex;
                if (targetSampleInLastFrame) {
                    outputFrameHolder.timeUs = this.decoderJni.getLastFrameTimestamp();
                    return TimestampSearchResult.targetFoundResult(input.getPosition());
                } else if (nextFrameSampleIndex <= targetSampleIndex) {
                    return TimestampSearchResult.underestimatedResult(nextFrameSampleIndex, nextFrameSamplePosition);
                } else {
                    return TimestampSearchResult.overestimatedResult(lastFrameSampleIndex, searchPosition);
                }
            } catch (FlacFrameDecodeException e) {
                return TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
            }
        }

        public int getTimestampSearchBytesRange() {
            return -1;
        }
    }

    public FlacBinarySearchSeeker(FlacStreamInfo streamInfo, long firstFramePosition, long inputLength, FlacDecoderJni decoderJni) {
        super(new FlacSeekTimestampConverter(streamInfo), new FlacTimestampSeeker(decoderJni), streamInfo.durationUs(), 0, streamInfo.totalSamples, firstFramePosition, inputLength, streamInfo.getApproxBytesPerFrame(), Math.max(1, streamInfo.minFrameSize));
        this.decoderJni = (FlacDecoderJni) Assertions.checkNotNull(decoderJni);
    }

    protected void onSeekOperationFinished(boolean foundTargetFrame, long resultPosition) {
        if (!foundTargetFrame) {
            this.decoderJni.reset(resultPosition);
        }
    }
}
