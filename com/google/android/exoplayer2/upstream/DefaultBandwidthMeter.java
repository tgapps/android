package com.google.android.exoplayer2.upstream;

import android.os.Handler;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.upstream.BandwidthMeter.EventListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.EventDispatcher;
import com.google.android.exoplayer2.util.SlidingPercentile;

public final class DefaultBandwidthMeter implements BandwidthMeter, TransferListener {
    private static final int BYTES_TRANSFERRED_FOR_ESTIMATE = 524288;
    public static final long DEFAULT_INITIAL_BITRATE_ESTIMATE = 1000000;
    public static final int DEFAULT_SLIDING_WINDOW_MAX_WEIGHT = 2000;
    private static final int ELAPSED_MILLIS_FOR_ESTIMATE = 2000;
    private long bitrateEstimate;
    private final Clock clock;
    private final EventDispatcher<EventListener> eventDispatcher;
    private long sampleBytesTransferred;
    private long sampleStartTimeMs;
    private final SlidingPercentile slidingPercentile;
    private int streamCount;
    private long totalBytesTransferred;
    private long totalElapsedTimeMs;

    public static final class Builder {
        private Clock clock = Clock.DEFAULT;
        private Handler eventHandler;
        private EventListener eventListener;
        private long initialBitrateEstimate = 1000000;
        private int slidingWindowMaxWeight = 2000;

        public Builder setEventListener(Handler eventHandler, EventListener eventListener) {
            boolean z = (eventHandler == null || eventListener == null) ? false : true;
            Assertions.checkArgument(z);
            this.eventHandler = eventHandler;
            this.eventListener = eventListener;
            return this;
        }

        public Builder setSlidingWindowMaxWeight(int slidingWindowMaxWeight) {
            this.slidingWindowMaxWeight = slidingWindowMaxWeight;
            return this;
        }

        public Builder setInitialBitrateEstimate(long initialBitrateEstimate) {
            this.initialBitrateEstimate = initialBitrateEstimate;
            return this;
        }

        public Builder setClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public DefaultBandwidthMeter build() {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(this.initialBitrateEstimate, this.slidingWindowMaxWeight, this.clock);
            if (!(this.eventHandler == null || this.eventListener == null)) {
                bandwidthMeter.addEventListener(this.eventHandler, this.eventListener);
            }
            return bandwidthMeter;
        }
    }

    public DefaultBandwidthMeter() {
        this(1000000, 2000, Clock.DEFAULT);
    }

    @Deprecated
    public DefaultBandwidthMeter(Handler eventHandler, EventListener eventListener) {
        this(1000000, 2000, Clock.DEFAULT);
        if (eventHandler != null && eventListener != null) {
            addEventListener(eventHandler, eventListener);
        }
    }

    @Deprecated
    public DefaultBandwidthMeter(Handler eventHandler, EventListener eventListener, int maxWeight) {
        this(1000000, maxWeight, Clock.DEFAULT);
        if (eventHandler != null && eventListener != null) {
            addEventListener(eventHandler, eventListener);
        }
    }

    private DefaultBandwidthMeter(long initialBitrateEstimate, int maxWeight, Clock clock) {
        this.eventDispatcher = new EventDispatcher();
        this.slidingPercentile = new SlidingPercentile(maxWeight);
        this.clock = clock;
        this.bitrateEstimate = initialBitrateEstimate;
    }

    public synchronized long getBitrateEstimate() {
        return this.bitrateEstimate;
    }

    public TransferListener getTransferListener() {
        return this;
    }

    public void addEventListener(Handler eventHandler, EventListener eventListener) {
        this.eventDispatcher.addListener(eventHandler, eventListener);
    }

    public void removeEventListener(EventListener eventListener) {
        this.eventDispatcher.removeListener(eventListener);
    }

    public void onTransferInitializing(DataSource source, DataSpec dataSpec, boolean isNetwork) {
    }

    public synchronized void onTransferStart(DataSource source, DataSpec dataSpec, boolean isNetwork) {
        if (isNetwork) {
            if (this.streamCount == 0) {
                this.sampleStartTimeMs = this.clock.elapsedRealtime();
            }
            this.streamCount++;
        }
    }

    public synchronized void onBytesTransferred(DataSource source, DataSpec dataSpec, boolean isNetwork, int bytes) {
        if (isNetwork) {
            this.sampleBytesTransferred += (long) bytes;
        }
    }

    public synchronized void onTransferEnd(DataSource source, DataSpec dataSpec, boolean isNetwork) {
        if (isNetwork) {
            Assertions.checkState(this.streamCount > 0);
            long nowMs = this.clock.elapsedRealtime();
            int sampleElapsedTimeMs = (int) (nowMs - this.sampleStartTimeMs);
            this.totalElapsedTimeMs += (long) sampleElapsedTimeMs;
            this.totalBytesTransferred += this.sampleBytesTransferred;
            if (sampleElapsedTimeMs > 0) {
                this.slidingPercentile.addSample((int) Math.sqrt((double) this.sampleBytesTransferred), (float) ((this.sampleBytesTransferred * 8000) / ((long) sampleElapsedTimeMs)));
                if (this.totalElapsedTimeMs >= AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS || this.totalBytesTransferred >= 524288) {
                    this.bitrateEstimate = (long) this.slidingPercentile.getPercentile(0.5f);
                }
            }
            notifyBandwidthSample(sampleElapsedTimeMs, this.sampleBytesTransferred, this.bitrateEstimate);
            int i = this.streamCount - 1;
            this.streamCount = i;
            if (i > 0) {
                this.sampleStartTimeMs = nowMs;
            }
            this.sampleBytesTransferred = 0;
        }
    }

    private void notifyBandwidthSample(int elapsedMs, long bytes, long bitrate) {
        this.eventDispatcher.dispatch(new DefaultBandwidthMeter$$Lambda$0(elapsedMs, bytes, bitrate));
    }
}
