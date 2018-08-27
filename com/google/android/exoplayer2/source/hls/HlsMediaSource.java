package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.SinglePeriodTimeline;
import com.google.android.exoplayer2.source.ads.AdsMediaSource.MediaSourceFactory;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PrimaryPlaylistListener;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy$$CC;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.List;

public final class HlsMediaSource extends BaseMediaSource implements PrimaryPlaylistListener {
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private final boolean allowChunklessPreparation;
    private final LoadErrorHandlingPolicy<Chunk> chunkLoadErrorHandlingPolicy;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private final HlsExtractorFactory extractorFactory;
    private final Uri manifestUri;
    private TransferListener mediaTransferListener;
    private final int minLoadableRetryCount;
    private final HlsPlaylistTracker playlistTracker;
    private final Object tag;

    public static final class Factory implements MediaSourceFactory {
        private boolean allowChunklessPreparation;
        private LoadErrorHandlingPolicy<Chunk> chunkLoadErrorHandlingPolicy;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
        private HlsExtractorFactory extractorFactory;
        private final HlsDataSourceFactory hlsDataSourceFactory;
        private boolean isCreateCalled;
        private int minLoadableRetryCount;
        private Parser<HlsPlaylist> playlistParser;
        private HlsPlaylistTracker playlistTracker;
        private Object tag;

        public Factory(com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory) {
            this(new DefaultHlsDataSourceFactory(dataSourceFactory));
        }

        public Factory(HlsDataSourceFactory hlsDataSourceFactory) {
            this.hlsDataSourceFactory = (HlsDataSourceFactory) Assertions.checkNotNull(hlsDataSourceFactory);
            this.extractorFactory = HlsExtractorFactory.DEFAULT;
            this.chunkLoadErrorHandlingPolicy = LoadErrorHandlingPolicy$$CC.getDefault$$STATIC$$();
            this.minLoadableRetryCount = 3;
            this.compositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
        }

        public Factory setTag(Object tag) {
            Assertions.checkState(!this.isCreateCalled);
            this.tag = tag;
            return this;
        }

        public Factory setExtractorFactory(HlsExtractorFactory extractorFactory) {
            Assertions.checkState(!this.isCreateCalled);
            this.extractorFactory = (HlsExtractorFactory) Assertions.checkNotNull(extractorFactory);
            return this;
        }

        public Factory setChunkLoadErrorHandlingPolicy(LoadErrorHandlingPolicy<Chunk> chunkLoadErrorHandlingPolicy) {
            Assertions.checkState(!this.isCreateCalled);
            this.chunkLoadErrorHandlingPolicy = chunkLoadErrorHandlingPolicy;
            return this;
        }

        public Factory setMinLoadableRetryCount(int minLoadableRetryCount) {
            Assertions.checkState(!this.isCreateCalled);
            this.minLoadableRetryCount = minLoadableRetryCount;
            return this;
        }

        public Factory setPlaylistParser(Parser<HlsPlaylist> playlistParser) {
            boolean z;
            boolean z2 = true;
            if (this.isCreateCalled) {
                z = false;
            } else {
                z = true;
            }
            Assertions.checkState(z);
            if (this.playlistTracker != null) {
                z2 = false;
            }
            Assertions.checkState(z2, "A playlist tracker has already been set.");
            this.playlistParser = (Parser) Assertions.checkNotNull(playlistParser);
            return this;
        }

        public Factory setPlaylistTracker(HlsPlaylistTracker playlistTracker) {
            boolean z;
            boolean z2 = true;
            if (this.isCreateCalled) {
                z = false;
            } else {
                z = true;
            }
            Assertions.checkState(z);
            if (this.playlistParser != null) {
                z2 = false;
            }
            Assertions.checkState(z2, "A playlist parser has already been set.");
            this.playlistTracker = (HlsPlaylistTracker) Assertions.checkNotNull(playlistTracker);
            return this;
        }

        public Factory setCompositeSequenceableLoaderFactory(CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory) {
            Assertions.checkState(!this.isCreateCalled);
            this.compositeSequenceableLoaderFactory = (CompositeSequenceableLoaderFactory) Assertions.checkNotNull(compositeSequenceableLoaderFactory);
            return this;
        }

        public Factory setAllowChunklessPreparation(boolean allowChunklessPreparation) {
            Assertions.checkState(!this.isCreateCalled);
            this.allowChunklessPreparation = allowChunklessPreparation;
            return this;
        }

        public HlsMediaSource createMediaSource(Uri playlistUri) {
            this.isCreateCalled = true;
            if (this.playlistTracker == null) {
                this.playlistTracker = new DefaultHlsPlaylistTracker(this.hlsDataSourceFactory, LoadErrorHandlingPolicy$$CC.getDefault$$STATIC$$(), this.minLoadableRetryCount, this.playlistParser != null ? this.playlistParser : new HlsPlaylistParser());
            }
            return new HlsMediaSource(playlistUri, this.hlsDataSourceFactory, this.extractorFactory, this.compositeSequenceableLoaderFactory, this.chunkLoadErrorHandlingPolicy, this.minLoadableRetryCount, this.playlistTracker, this.allowChunklessPreparation, this.tag);
        }

        @Deprecated
        public HlsMediaSource createMediaSource(Uri playlistUri, Handler eventHandler, MediaSourceEventListener eventListener) {
            HlsMediaSource mediaSource = createMediaSource(playlistUri);
            if (!(eventHandler == null || eventListener == null)) {
                mediaSource.addEventListener(eventHandler, eventListener);
            }
            return mediaSource;
        }

        public int[] getSupportedTypes() {
            return new int[]{2};
        }
    }

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.hls");
    }

    @Deprecated
    public HlsMediaSource(Uri manifestUri, com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory, Handler eventHandler, MediaSourceEventListener eventListener) {
        this(manifestUri, dataSourceFactory, 3, eventHandler, eventListener);
    }

    @Deprecated
    public HlsMediaSource(Uri manifestUri, com.google.android.exoplayer2.upstream.DataSource.Factory dataSourceFactory, int minLoadableRetryCount, Handler eventHandler, MediaSourceEventListener eventListener) {
        this(manifestUri, new DefaultHlsDataSourceFactory(dataSourceFactory), HlsExtractorFactory.DEFAULT, minLoadableRetryCount, eventHandler, eventListener, new HlsPlaylistParser());
    }

    @Deprecated
    public HlsMediaSource(Uri manifestUri, HlsDataSourceFactory dataSourceFactory, HlsExtractorFactory extractorFactory, int minLoadableRetryCount, Handler eventHandler, MediaSourceEventListener eventListener, Parser<HlsPlaylist> playlistParser) {
        this(manifestUri, dataSourceFactory, extractorFactory, new DefaultCompositeSequenceableLoaderFactory(), LoadErrorHandlingPolicy$$CC.getDefault$$STATIC$$(), minLoadableRetryCount, new DefaultHlsPlaylistTracker(dataSourceFactory, LoadErrorHandlingPolicy$$CC.getDefault$$STATIC$$(), minLoadableRetryCount, playlistParser), false, null);
        if (eventHandler != null && eventListener != null) {
            addEventListener(eventHandler, eventListener);
        }
    }

    private HlsMediaSource(Uri manifestUri, HlsDataSourceFactory dataSourceFactory, HlsExtractorFactory extractorFactory, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, LoadErrorHandlingPolicy<Chunk> chunkLoadErrorHandlingPolicy, int minLoadableRetryCount, HlsPlaylistTracker playlistTracker, boolean allowChunklessPreparation, Object tag) {
        this.manifestUri = manifestUri;
        this.dataSourceFactory = dataSourceFactory;
        this.extractorFactory = extractorFactory;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.chunkLoadErrorHandlingPolicy = chunkLoadErrorHandlingPolicy;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.playlistTracker = playlistTracker;
        this.allowChunklessPreparation = allowChunklessPreparation;
        this.tag = tag;
    }

    public void prepareSourceInternal(ExoPlayer player, boolean isTopLevelSource, TransferListener mediaTransferListener) {
        this.mediaTransferListener = mediaTransferListener;
        this.playlistTracker.start(this.manifestUri, createEventDispatcher(null), this);
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.playlistTracker.maybeThrowPrimaryPlaylistRefreshError();
    }

    public MediaPeriod createPeriod(MediaPeriodId id, Allocator allocator) {
        Assertions.checkArgument(id.periodIndex == 0);
        return new HlsMediaPeriod(this.extractorFactory, this.playlistTracker, this.dataSourceFactory, this.mediaTransferListener, this.chunkLoadErrorHandlingPolicy, this.minLoadableRetryCount, createEventDispatcher(id), allocator, this.compositeSequenceableLoaderFactory, this.allowChunklessPreparation);
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        ((HlsMediaPeriod) mediaPeriod).release();
    }

    public void releaseSourceInternal() {
        if (this.playlistTracker != null) {
            this.playlistTracker.stop();
        }
    }

    public void onPrimaryPlaylistRefreshed(HlsMediaPlaylist playlist) {
        SinglePeriodTimeline timeline;
        long windowStartTimeMs = playlist.hasProgramDateTime ? C.usToMs(playlist.startTimeUs) : C.TIME_UNSET;
        long presentationStartTimeMs = (playlist.playlistType == 2 || playlist.playlistType == 1) ? windowStartTimeMs : C.TIME_UNSET;
        long windowDefaultStartPositionUs = playlist.startOffsetUs;
        if (this.playlistTracker.isLive()) {
            boolean z;
            long offsetFromInitialStartTimeUs = playlist.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
            long periodDurationUs = playlist.hasEndTag ? offsetFromInitialStartTimeUs + playlist.durationUs : C.TIME_UNSET;
            List<Segment> segments = playlist.segments;
            if (windowDefaultStartPositionUs == C.TIME_UNSET) {
                if (segments.isEmpty()) {
                    windowDefaultStartPositionUs = 0;
                } else {
                    windowDefaultStartPositionUs = ((Segment) segments.get(Math.max(0, segments.size() - 3))).relativeStartTimeUs;
                }
            }
            long j = playlist.durationUs;
            if (playlist.hasEndTag) {
                z = false;
            } else {
                z = true;
            }
            timeline = new SinglePeriodTimeline(presentationStartTimeMs, windowStartTimeMs, periodDurationUs, j, offsetFromInitialStartTimeUs, windowDefaultStartPositionUs, true, z, this.tag);
        } else {
            if (windowDefaultStartPositionUs == C.TIME_UNSET) {
                windowDefaultStartPositionUs = 0;
            }
            SinglePeriodTimeline singlePeriodTimeline = new SinglePeriodTimeline(presentationStartTimeMs, windowStartTimeMs, playlist.durationUs, playlist.durationUs, 0, windowDefaultStartPositionUs, true, false, this.tag);
        }
        refreshSourceInfo(timeline, new HlsManifest(this.playlistTracker.getMasterPlaylist(), playlist));
    }
}
