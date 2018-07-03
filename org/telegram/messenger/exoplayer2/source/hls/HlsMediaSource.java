package org.telegram.messenger.exoplayer2.source.hls;

import android.net.Uri;
import android.os.Handler;
import java.io.IOException;
import java.util.List;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ExoPlayer;
import org.telegram.messenger.exoplayer2.ExoPlayerLibraryInfo;
import org.telegram.messenger.exoplayer2.source.BaseMediaSource;
import org.telegram.messenger.exoplayer2.source.CompositeSequenceableLoaderFactory;
import org.telegram.messenger.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import org.telegram.messenger.exoplayer2.source.MediaPeriod;
import org.telegram.messenger.exoplayer2.source.MediaSource.MediaPeriodId;
import org.telegram.messenger.exoplayer2.source.MediaSourceEventListener;
import org.telegram.messenger.exoplayer2.source.SinglePeriodTimeline;
import org.telegram.messenger.exoplayer2.source.ads.AdsMediaSource.MediaSourceFactory;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylist;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PrimaryPlaylistListener;
import org.telegram.messenger.exoplayer2.upstream.Allocator;
import org.telegram.messenger.exoplayer2.upstream.ParsingLoadable.Parser;
import org.telegram.messenger.exoplayer2.util.Assertions;

public final class HlsMediaSource extends BaseMediaSource implements PrimaryPlaylistListener {
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private final boolean allowChunklessPreparation;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private final HlsExtractorFactory extractorFactory;
    private final Uri manifestUri;
    private final int minLoadableRetryCount;
    private final Parser<HlsPlaylist> playlistParser;
    private HlsPlaylistTracker playlistTracker;
    private final Object tag;

    public static final class Factory implements MediaSourceFactory {
        private boolean allowChunklessPreparation;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
        private HlsExtractorFactory extractorFactory;
        private final HlsDataSourceFactory hlsDataSourceFactory;
        private boolean isCreateCalled;
        private int minLoadableRetryCount;
        private Parser<HlsPlaylist> playlistParser;
        private Object tag;

        public Factory(org.telegram.messenger.exoplayer2.upstream.DataSource.Factory dataSourceFactory) {
            this(new DefaultHlsDataSourceFactory(dataSourceFactory));
        }

        public Factory(HlsDataSourceFactory hlsDataSourceFactory) {
            this.hlsDataSourceFactory = (HlsDataSourceFactory) Assertions.checkNotNull(hlsDataSourceFactory);
            this.extractorFactory = HlsExtractorFactory.DEFAULT;
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

        public Factory setMinLoadableRetryCount(int minLoadableRetryCount) {
            Assertions.checkState(!this.isCreateCalled);
            this.minLoadableRetryCount = minLoadableRetryCount;
            return this;
        }

        public Factory setPlaylistParser(Parser<HlsPlaylist> playlistParser) {
            Assertions.checkState(!this.isCreateCalled);
            this.playlistParser = (Parser) Assertions.checkNotNull(playlistParser);
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
            if (this.playlistParser == null) {
                this.playlistParser = new HlsPlaylistParser();
            }
            return new HlsMediaSource(playlistUri, this.hlsDataSourceFactory, this.extractorFactory, this.compositeSequenceableLoaderFactory, this.minLoadableRetryCount, this.playlistParser, this.allowChunklessPreparation, this.tag);
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
    public HlsMediaSource(Uri manifestUri, org.telegram.messenger.exoplayer2.upstream.DataSource.Factory dataSourceFactory, Handler eventHandler, MediaSourceEventListener eventListener) {
        this(manifestUri, dataSourceFactory, 3, eventHandler, eventListener);
    }

    @Deprecated
    public HlsMediaSource(Uri manifestUri, org.telegram.messenger.exoplayer2.upstream.DataSource.Factory dataSourceFactory, int minLoadableRetryCount, Handler eventHandler, MediaSourceEventListener eventListener) {
        this(manifestUri, new DefaultHlsDataSourceFactory(dataSourceFactory), HlsExtractorFactory.DEFAULT, minLoadableRetryCount, eventHandler, eventListener, new HlsPlaylistParser());
    }

    @Deprecated
    public HlsMediaSource(Uri manifestUri, HlsDataSourceFactory dataSourceFactory, HlsExtractorFactory extractorFactory, int minLoadableRetryCount, Handler eventHandler, MediaSourceEventListener eventListener, Parser<HlsPlaylist> playlistParser) {
        this(manifestUri, dataSourceFactory, extractorFactory, new DefaultCompositeSequenceableLoaderFactory(), minLoadableRetryCount, playlistParser, false, null);
        if (eventHandler != null && eventListener != null) {
            addEventListener(eventHandler, eventListener);
        }
    }

    private HlsMediaSource(Uri manifestUri, HlsDataSourceFactory dataSourceFactory, HlsExtractorFactory extractorFactory, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, int minLoadableRetryCount, Parser<HlsPlaylist> playlistParser, boolean allowChunklessPreparation, Object tag) {
        this.manifestUri = manifestUri;
        this.dataSourceFactory = dataSourceFactory;
        this.extractorFactory = extractorFactory;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.playlistParser = playlistParser;
        this.allowChunklessPreparation = allowChunklessPreparation;
        this.tag = tag;
    }

    public void prepareSourceInternal(ExoPlayer player, boolean isTopLevelSource) {
        this.playlistTracker = new HlsPlaylistTracker(this.manifestUri, this.dataSourceFactory, createEventDispatcher(null), this.minLoadableRetryCount, this, this.playlistParser);
        this.playlistTracker.start();
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.playlistTracker.maybeThrowPrimaryPlaylistRefreshError();
    }

    public MediaPeriod createPeriod(MediaPeriodId id, Allocator allocator) {
        Assertions.checkArgument(id.periodIndex == 0);
        return new HlsMediaPeriod(this.extractorFactory, this.playlistTracker, this.dataSourceFactory, this.minLoadableRetryCount, createEventDispatcher(id), allocator, this.compositeSequenceableLoaderFactory, this.allowChunklessPreparation);
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        ((HlsMediaPeriod) mediaPeriod).release();
    }

    public void releaseSourceInternal() {
        if (this.playlistTracker != null) {
            this.playlistTracker.release();
            this.playlistTracker = null;
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
