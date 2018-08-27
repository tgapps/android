package com.google.android.exoplayer2.source;

import android.os.Handler;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.IOException;

public interface MediaSource {

    public static final class MediaPeriodId {
        public final int adGroupIndex;
        public final int adIndexInAdGroup;
        public final long endPositionUs;
        public final int periodIndex;
        public final long windowSequenceNumber;

        public MediaPeriodId(int periodIndex) {
            this(periodIndex, -1);
        }

        public MediaPeriodId(int periodIndex, long windowSequenceNumber) {
            this(periodIndex, -1, -1, windowSequenceNumber, Long.MIN_VALUE);
        }

        public MediaPeriodId(int periodIndex, long windowSequenceNumber, long endPositionUs) {
            this(periodIndex, -1, -1, windowSequenceNumber, endPositionUs);
        }

        public MediaPeriodId(int periodIndex, int adGroupIndex, int adIndexInAdGroup, long windowSequenceNumber) {
            this(periodIndex, adGroupIndex, adIndexInAdGroup, windowSequenceNumber, Long.MIN_VALUE);
        }

        private MediaPeriodId(int periodIndex, int adGroupIndex, int adIndexInAdGroup, long windowSequenceNumber, long endPositionUs) {
            this.periodIndex = periodIndex;
            this.adGroupIndex = adGroupIndex;
            this.adIndexInAdGroup = adIndexInAdGroup;
            this.windowSequenceNumber = windowSequenceNumber;
            this.endPositionUs = endPositionUs;
        }

        public MediaPeriodId copyWithPeriodIndex(int newPeriodIndex) {
            if (this.periodIndex == newPeriodIndex) {
                return this;
            }
            return new MediaPeriodId(newPeriodIndex, this.adGroupIndex, this.adIndexInAdGroup, this.windowSequenceNumber, this.endPositionUs);
        }

        public boolean isAd() {
            return this.adGroupIndex != -1;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            MediaPeriodId periodId = (MediaPeriodId) obj;
            if (this.periodIndex == periodId.periodIndex && this.adGroupIndex == periodId.adGroupIndex && this.adIndexInAdGroup == periodId.adIndexInAdGroup && this.windowSequenceNumber == periodId.windowSequenceNumber && this.endPositionUs == periodId.endPositionUs) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return ((((((((this.periodIndex + 527) * 31) + this.adGroupIndex) * 31) + this.adIndexInAdGroup) * 31) + ((int) this.windowSequenceNumber)) * 31) + ((int) this.endPositionUs);
        }
    }

    public interface SourceInfoRefreshListener {
        void onSourceInfoRefreshed(MediaSource mediaSource, Timeline timeline, Object obj);
    }

    void addEventListener(Handler handler, MediaSourceEventListener mediaSourceEventListener);

    MediaPeriod createPeriod(MediaPeriodId mediaPeriodId, Allocator allocator);

    void maybeThrowSourceInfoRefreshError() throws IOException;

    @Deprecated
    void prepareSource(ExoPlayer exoPlayer, boolean z, SourceInfoRefreshListener sourceInfoRefreshListener);

    void prepareSource(ExoPlayer exoPlayer, boolean z, SourceInfoRefreshListener sourceInfoRefreshListener, TransferListener transferListener);

    void releasePeriod(MediaPeriod mediaPeriod);

    void releaseSource(SourceInfoRefreshListener sourceInfoRefreshListener);

    void removeEventListener(MediaSourceEventListener mediaSourceEventListener);
}
