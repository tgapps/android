package org.telegram.messenger.exoplayer2.source.hls;

import android.net.Uri;
import android.os.Handler;
import java.io.IOException;
import java.util.List;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ExoPlayer;
import org.telegram.messenger.exoplayer2.ExoPlayerLibraryInfo;
import org.telegram.messenger.exoplayer2.source.CompositeSequenceableLoaderFactory;
import org.telegram.messenger.exoplayer2.source.DefaultCompositeSequenceableLoaderFactory;
import org.telegram.messenger.exoplayer2.source.MediaPeriod;
import org.telegram.messenger.exoplayer2.source.MediaSource;
import org.telegram.messenger.exoplayer2.source.MediaSource.Listener;
import org.telegram.messenger.exoplayer2.source.MediaSource.MediaPeriodId;
import org.telegram.messenger.exoplayer2.source.MediaSourceEventListener;
import org.telegram.messenger.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
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

public final class HlsMediaSource implements MediaSource, PrimaryPlaylistListener {
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private final boolean allowChunklessPreparation;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private final EventDispatcher eventDispatcher;
    private final HlsExtractorFactory extractorFactory;
    private final Uri manifestUri;
    private final int minLoadableRetryCount;
    private final Parser<HlsPlaylist> playlistParser;
    private HlsPlaylistTracker playlistTracker;
    private Listener sourceListener;

    public static final class Factory implements MediaSourceFactory {
        private boolean allowChunklessPreparation;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
        private HlsExtractorFactory extractorFactory;
        private final HlsDataSourceFactory hlsDataSourceFactory;
        private boolean isCreateCalled;
        private int minLoadableRetryCount;
        private Parser<HlsPlaylist> playlistParser;

        public Factory(org.telegram.messenger.exoplayer2.upstream.DataSource.Factory dataSourceFactory) {
            this(new DefaultHlsDataSourceFactory(dataSourceFactory));
        }

        public Factory(HlsDataSourceFactory hlsDataSourceFactory) {
            this.hlsDataSourceFactory = (HlsDataSourceFactory) Assertions.checkNotNull(hlsDataSourceFactory);
            this.extractorFactory = HlsExtractorFactory.DEFAULT;
            this.minLoadableRetryCount = 3;
            this.compositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
        }

        public Factory setExtractorFactory(HlsExtractorFactory extractorFactory) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.extractorFactory = (HlsExtractorFactory) Assertions.checkNotNull(extractorFactory);
            return this;
        }

        public Factory setMinLoadableRetryCount(int minLoadableRetryCount) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.minLoadableRetryCount = minLoadableRetryCount;
            return this;
        }

        public Factory setPlaylistParser(Parser<HlsPlaylist> playlistParser) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.playlistParser = (Parser) Assertions.checkNotNull(playlistParser);
            return this;
        }

        public Factory setCompositeSequenceableLoaderFactory(CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.compositeSequenceableLoaderFactory = (CompositeSequenceableLoaderFactory) Assertions.checkNotNull(compositeSequenceableLoaderFactory);
            return this;
        }

        public Factory setAllowChunklessPreparation(boolean allowChunklessPreparation) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.allowChunklessPreparation = allowChunklessPreparation;
            return this;
        }

        public HlsMediaSource createMediaSource(Uri playlistUri) {
            return createMediaSource(playlistUri, null, null);
        }

        public HlsMediaSource createMediaSource(Uri playlistUri, Handler eventHandler, MediaSourceEventListener eventListener) {
            this.isCreateCalled = true;
            if (this.playlistParser == null) {
                this.playlistParser = new HlsPlaylistParser();
            }
            return new HlsMediaSource(playlistUri, this.hlsDataSourceFactory, this.extractorFactory, this.compositeSequenceableLoaderFactory, this.minLoadableRetryCount, eventHandler, eventListener, this.playlistParser, this.allowChunklessPreparation);
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
        this(manifestUri, dataSourceFactory, extractorFactory, new DefaultCompositeSequenceableLoaderFactory(), minLoadableRetryCount, eventHandler, eventListener, playlistParser, false);
    }

    private HlsMediaSource(Uri manifestUri, HlsDataSourceFactory dataSourceFactory, HlsExtractorFactory extractorFactory, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, int minLoadableRetryCount, Handler eventHandler, MediaSourceEventListener eventListener, Parser<HlsPlaylist> playlistParser, boolean allowChunklessPreparation) {
        this.manifestUri = manifestUri;
        this.dataSourceFactory = dataSourceFactory;
        this.extractorFactory = extractorFactory;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.playlistParser = playlistParser;
        this.allowChunklessPreparation = allowChunklessPreparation;
        this.eventDispatcher = new EventDispatcher(eventHandler, eventListener);
    }

    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, Listener listener) {
        Assertions.checkState(this.sourceListener == null, MediaSource.MEDIA_SOURCE_REUSED_ERROR_MESSAGE);
        this.playlistTracker = new HlsPlaylistTracker(this.manifestUri, this.dataSourceFactory, this.eventDispatcher, this.minLoadableRetryCount, this, this.playlistParser);
        this.sourceListener = listener;
        this.playlistTracker.start();
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.playlistTracker.maybeThrowPrimaryPlaylistRefreshError();
    }

    public MediaPeriod createPeriod(MediaPeriodId id, Allocator allocator) {
        Assertions.checkArgument(id.periodIndex == 0);
        return new HlsMediaPeriod(this.extractorFactory, this.playlistTracker, this.dataSourceFactory, this.minLoadableRetryCount, this.eventDispatcher, allocator, this.compositeSequenceableLoaderFactory, this.allowChunklessPreparation);
    }

    public void releasePeriod(MediaPeriod mediaPeriod) {
        ((HlsMediaPeriod) mediaPeriod).release();
    }

    public void releaseSource() {
        if (this.playlistTracker != null) {
            this.playlistTracker.release();
            this.playlistTracker = null;
        }
    }

    public void onPrimaryPlaylistRefreshed(HlsMediaPlaylist playlist) {
        SinglePeriodTimeline timeline;
        HlsMediaSource hlsMediaSource = this;
        HlsMediaPlaylist hlsMediaPlaylist = playlist;
        long presentationStartTimeMs = hlsMediaPlaylist.hasProgramDateTime ? 0 : C.TIME_UNSET;
        long windowStartTimeMs = hlsMediaPlaylist.hasProgramDateTime ? C.usToMs(hlsMediaPlaylist.startTimeUs) : C.TIME_UNSET;
        long windowDefaultStartPositionUs = hlsMediaPlaylist.startOffsetUs;
        if (hlsMediaSource.playlistTracker.isLive()) {
            long windowDefaultStartPositionUs2;
            long periodDurationUs = hlsMediaPlaylist.hasEndTag ? hlsMediaPlaylist.startTimeUs + hlsMediaPlaylist.durationUs : C.TIME_UNSET;
            List<Segment> segments = hlsMediaPlaylist.segments;
            if (windowDefaultStartPositionUs == C.TIME_UNSET) {
                windowDefaultStartPositionUs2 = segments.isEmpty() ? 0 : ((Segment) segments.get(Math.max(0, segments.size() - 3))).relativeStartTimeUs;
            } else {
                windowDefaultStartPositionUs2 = windowDefaultStartPositionUs;
            }
            timeline = new SinglePeriodTimeline(presentationStartTimeMs, windowStartTimeMs, periodDurationUs, hlsMediaPlaylist.durationUs, hlsMediaPlaylist.startTimeUs, windowDefaultStartPositionUs2, true, hlsMediaPlaylist.hasEndTag ^ 1);
            windowDefaultStartPositionUs = windowDefaultStartPositionUs2;
        } else {
            if (windowDefaultStartPositionUs == C.TIME_UNSET) {
                windowDefaultStartPositionUs = 0;
            }
            SinglePeriodTimeline singlePeriodTimeline = new SinglePeriodTimeline(presentationStartTimeMs, windowStartTimeMs, hlsMediaPlaylist.startTimeUs + hlsMediaPlaylist.durationUs, hlsMediaPlaylist.durationUs, hlsMediaPlaylist.startTimeUs, windowDefaultStartPositionUs, true, false);
        }
        hlsMediaSource.sourceListener.onSourceInfoRefreshed(hlsMediaSource, timeline, new HlsManifest(hlsMediaSource.playlistTracker.getMasterPlaylist(), hlsMediaPlaylist));
    }
}
