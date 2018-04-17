package org.telegram.messenger.exoplayer2.source.smoothstreaming;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import java.io.IOException;
import java.util.ArrayList;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;
import org.telegram.messenger.exoplayer2.ExoPlayer;
import org.telegram.messenger.exoplayer2.ExoPlayerLibraryInfo;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.Timeline;
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
import org.telegram.messenger.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import org.telegram.messenger.exoplayer2.source.smoothstreaming.manifest.SsManifest.StreamElement;
import org.telegram.messenger.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import org.telegram.messenger.exoplayer2.upstream.Allocator;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.Loader;
import org.telegram.messenger.exoplayer2.upstream.Loader.Callback;
import org.telegram.messenger.exoplayer2.upstream.LoaderErrorThrower;
import org.telegram.messenger.exoplayer2.upstream.LoaderErrorThrower.Dummy;
import org.telegram.messenger.exoplayer2.upstream.ParsingLoadable;
import org.telegram.messenger.exoplayer2.upstream.ParsingLoadable.Parser;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.Util;

public final class SsMediaSource implements MediaSource, Callback<ParsingLoadable<SsManifest>> {
    public static final long DEFAULT_LIVE_PRESENTATION_DELAY_MS = 30000;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private static final int MINIMUM_MANIFEST_REFRESH_PERIOD_MS = 5000;
    private static final long MIN_LIVE_DEFAULT_START_POSITION_US = 5000000;
    private final org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final EventDispatcher eventDispatcher;
    private final long livePresentationDelayMs;
    private SsManifest manifest;
    private DataSource manifestDataSource;
    private final org.telegram.messenger.exoplayer2.upstream.DataSource.Factory manifestDataSourceFactory;
    private long manifestLoadStartTimestamp;
    private Loader manifestLoader;
    private LoaderErrorThrower manifestLoaderErrorThrower;
    private final Parser<? extends SsManifest> manifestParser;
    private Handler manifestRefreshHandler;
    private final Uri manifestUri;
    private final ArrayList<SsMediaPeriod> mediaPeriods;
    private final int minLoadableRetryCount;
    private Listener sourceListener;

    public static final class Factory implements MediaSourceFactory {
        private final org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory;
        private CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory = new DefaultCompositeSequenceableLoaderFactory();
        private boolean isCreateCalled;
        private long livePresentationDelayMs = 30000;
        private final org.telegram.messenger.exoplayer2.upstream.DataSource.Factory manifestDataSourceFactory;
        private Parser<? extends SsManifest> manifestParser;
        private int minLoadableRetryCount = 3;

        public Factory(org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory, org.telegram.messenger.exoplayer2.upstream.DataSource.Factory manifestDataSourceFactory) {
            this.chunkSourceFactory = (org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory) Assertions.checkNotNull(chunkSourceFactory);
            this.manifestDataSourceFactory = manifestDataSourceFactory;
        }

        public Factory setMinLoadableRetryCount(int minLoadableRetryCount) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.minLoadableRetryCount = minLoadableRetryCount;
            return this;
        }

        public Factory setLivePresentationDelayMs(long livePresentationDelayMs) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.livePresentationDelayMs = livePresentationDelayMs;
            return this;
        }

        public Factory setManifestParser(Parser<? extends SsManifest> manifestParser) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.manifestParser = (Parser) Assertions.checkNotNull(manifestParser);
            return this;
        }

        public Factory setCompositeSequenceableLoaderFactory(CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory) {
            Assertions.checkState(this.isCreateCalled ^ 1);
            this.compositeSequenceableLoaderFactory = (CompositeSequenceableLoaderFactory) Assertions.checkNotNull(compositeSequenceableLoaderFactory);
            return this;
        }

        public SsMediaSource createMediaSource(SsManifest manifest, Handler eventHandler, MediaSourceEventListener eventListener) {
            SsManifest ssManifest = manifest;
            Assertions.checkArgument(ssManifest.isLive ^ true);
            this.isCreateCalled = true;
            return new SsMediaSource(ssManifest, null, null, null, this.chunkSourceFactory, this.compositeSequenceableLoaderFactory, this.minLoadableRetryCount, this.livePresentationDelayMs, eventHandler, eventListener);
        }

        public SsMediaSource createMediaSource(Uri manifestUri) {
            return createMediaSource(manifestUri, null, null);
        }

        public SsMediaSource createMediaSource(Uri manifestUri, Handler eventHandler, MediaSourceEventListener eventListener) {
            this.isCreateCalled = true;
            if (this.manifestParser == null) {
                r0.manifestParser = new SsManifestParser();
            }
            return new SsMediaSource(null, (Uri) Assertions.checkNotNull(manifestUri), r0.manifestDataSourceFactory, r0.manifestParser, r0.chunkSourceFactory, r0.compositeSequenceableLoaderFactory, r0.minLoadableRetryCount, r0.livePresentationDelayMs, eventHandler, eventListener);
        }

        public int[] getSupportedTypes() {
            return new int[]{1};
        }
    }

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.smoothstreaming");
    }

    @Deprecated
    public SsMediaSource(SsManifest manifest, org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory, Handler eventHandler, MediaSourceEventListener eventListener) {
        this(manifest, chunkSourceFactory, 3, eventHandler, eventListener);
    }

    @Deprecated
    public SsMediaSource(SsManifest manifest, org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, Handler eventHandler, MediaSourceEventListener eventListener) {
        this(manifest, null, null, null, chunkSourceFactory, new DefaultCompositeSequenceableLoaderFactory(), minLoadableRetryCount, 30000, eventHandler, eventListener);
    }

    @Deprecated
    public SsMediaSource(Uri manifestUri, org.telegram.messenger.exoplayer2.upstream.DataSource.Factory manifestDataSourceFactory, org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory, Handler eventHandler, MediaSourceEventListener eventListener) {
        this(manifestUri, manifestDataSourceFactory, chunkSourceFactory, 3, 30000, eventHandler, eventListener);
    }

    @Deprecated
    public SsMediaSource(Uri manifestUri, org.telegram.messenger.exoplayer2.upstream.DataSource.Factory manifestDataSourceFactory, org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, long livePresentationDelayMs, Handler eventHandler, MediaSourceEventListener eventListener) {
        this(manifestUri, manifestDataSourceFactory, new SsManifestParser(), chunkSourceFactory, minLoadableRetryCount, livePresentationDelayMs, eventHandler, eventListener);
    }

    @Deprecated
    public SsMediaSource(Uri manifestUri, org.telegram.messenger.exoplayer2.upstream.DataSource.Factory manifestDataSourceFactory, Parser<? extends SsManifest> manifestParser, org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, long livePresentationDelayMs, Handler eventHandler, MediaSourceEventListener eventListener) {
        this(null, manifestUri, manifestDataSourceFactory, manifestParser, chunkSourceFactory, new DefaultCompositeSequenceableLoaderFactory(), minLoadableRetryCount, livePresentationDelayMs, eventHandler, eventListener);
    }

    private SsMediaSource(SsManifest manifest, Uri manifestUri, org.telegram.messenger.exoplayer2.upstream.DataSource.Factory manifestDataSourceFactory, Parser<? extends SsManifest> manifestParser, org.telegram.messenger.exoplayer2.source.smoothstreaming.SsChunkSource.Factory chunkSourceFactory, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, int minLoadableRetryCount, long livePresentationDelayMs, Handler eventHandler, MediaSourceEventListener eventListener) {
        boolean z;
        Uri withAppendedPath;
        if (manifest != null) {
            if (manifest.isLive) {
                z = false;
                Assertions.checkState(z);
                this.manifest = manifest;
                withAppendedPath = manifestUri != null ? null : Util.toLowerInvariant(manifestUri.getLastPathSegment()).matches("manifest(\\(.+\\))?") ? manifestUri : Uri.withAppendedPath(manifestUri, "Manifest");
                this.manifestUri = withAppendedPath;
                this.manifestDataSourceFactory = manifestDataSourceFactory;
                this.manifestParser = manifestParser;
                this.chunkSourceFactory = chunkSourceFactory;
                this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
                this.minLoadableRetryCount = minLoadableRetryCount;
                this.livePresentationDelayMs = livePresentationDelayMs;
                this.eventDispatcher = new EventDispatcher(eventHandler, eventListener);
                this.mediaPeriods = new ArrayList();
            }
        }
        z = true;
        Assertions.checkState(z);
        this.manifest = manifest;
        if (manifestUri != null) {
            if (Util.toLowerInvariant(manifestUri.getLastPathSegment()).matches("manifest(\\(.+\\))?")) {
            }
        }
        this.manifestUri = withAppendedPath;
        this.manifestDataSourceFactory = manifestDataSourceFactory;
        this.manifestParser = manifestParser;
        this.chunkSourceFactory = chunkSourceFactory;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.livePresentationDelayMs = livePresentationDelayMs;
        this.eventDispatcher = new EventDispatcher(eventHandler, eventListener);
        this.mediaPeriods = new ArrayList();
    }

    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, Listener listener) {
        Assertions.checkState(this.sourceListener == null, MediaSource.MEDIA_SOURCE_REUSED_ERROR_MESSAGE);
        this.sourceListener = listener;
        if (this.manifest != null) {
            this.manifestLoaderErrorThrower = new Dummy();
            processManifest();
            return;
        }
        this.manifestDataSource = this.manifestDataSourceFactory.createDataSource();
        this.manifestLoader = new Loader("Loader:Manifest");
        this.manifestLoaderErrorThrower = this.manifestLoader;
        this.manifestRefreshHandler = new Handler();
        startLoadingManifest();
    }

    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.manifestLoaderErrorThrower.maybeThrowError();
    }

    public MediaPeriod createPeriod(MediaPeriodId id, Allocator allocator) {
        Assertions.checkArgument(id.periodIndex == 0);
        SsMediaPeriod ssMediaPeriod = new SsMediaPeriod(this.manifest, this.chunkSourceFactory, this.compositeSequenceableLoaderFactory, this.minLoadableRetryCount, this.eventDispatcher, this.manifestLoaderErrorThrower, allocator);
        this.mediaPeriods.add(ssMediaPeriod);
        return ssMediaPeriod;
    }

    public void releasePeriod(MediaPeriod period) {
        ((SsMediaPeriod) period).release();
        this.mediaPeriods.remove(period);
    }

    public void releaseSource() {
        this.manifest = null;
        this.manifestDataSource = null;
        this.manifestLoadStartTimestamp = 0;
        if (this.manifestLoader != null) {
            this.manifestLoader.release();
            this.manifestLoader = null;
        }
        if (this.manifestRefreshHandler != null) {
            this.manifestRefreshHandler.removeCallbacksAndMessages(null);
            this.manifestRefreshHandler = null;
        }
    }

    public void onLoadCompleted(ParsingLoadable<SsManifest> loadable, long elapsedRealtimeMs, long loadDurationMs) {
        this.eventDispatcher.loadCompleted(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        this.manifest = (SsManifest) loadable.getResult();
        this.manifestLoadStartTimestamp = elapsedRealtimeMs - loadDurationMs;
        processManifest();
        scheduleManifestRefresh();
    }

    public void onLoadCanceled(ParsingLoadable<SsManifest> loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
        this.eventDispatcher.loadCompleted(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
    }

    public int onLoadError(ParsingLoadable<SsManifest> loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        ParsingLoadable<SsManifest> parsingLoadable = loadable;
        IOException iOException = error;
        boolean isFatal = iOException instanceof ParserException;
        this.eventDispatcher.loadError(parsingLoadable.dataSpec, parsingLoadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded(), iOException, isFatal);
        return isFatal ? 3 : 0;
    }

    private void processManifest() {
        int i;
        Timeline timeline;
        SsMediaSource ssMediaSource = this;
        int i2 = 0;
        for (i = 0; i < ssMediaSource.mediaPeriods.size(); i++) {
            ((SsMediaPeriod) ssMediaSource.mediaPeriods.get(i)).updateManifest(ssMediaSource.manifest);
        }
        StreamElement[] streamElementArr = ssMediaSource.manifest.streamElements;
        int length = streamElementArr.length;
        long endTimeUs = Long.MIN_VALUE;
        long startTimeUs = Long.MAX_VALUE;
        i = 0;
        while (i < length) {
            int i3;
            StreamElement element = streamElementArr[i];
            if (element.chunkCount > 0) {
                startTimeUs = Math.min(startTimeUs, element.getStartTimeUs(i2));
                i3 = i;
                endTimeUs = Math.max(endTimeUs, element.getStartTimeUs(element.chunkCount - 1) + element.getChunkDurationUs(element.chunkCount - 1));
            } else {
                i3 = i;
            }
            i = i3 + 1;
            i2 = 0;
        }
        if (startTimeUs == Long.MAX_VALUE) {
            Timeline singlePeriodTimeline = new SinglePeriodTimeline(ssMediaSource.manifest.isLive ? C.TIME_UNSET : 0, 0, 0, 0, true, ssMediaSource.manifest.isLive);
        } else if (ssMediaSource.manifest.isLive) {
            long defaultStartPositionUs;
            if (ssMediaSource.manifest.dvrWindowLengthUs != C.TIME_UNSET && ssMediaSource.manifest.dvrWindowLengthUs > 0) {
                startTimeUs = Math.max(startTimeUs, endTimeUs - ssMediaSource.manifest.dvrWindowLengthUs);
            }
            durationUs = endTimeUs - startTimeUs;
            long defaultStartPositionUs2 = durationUs - C.msToUs(ssMediaSource.livePresentationDelayMs);
            if (defaultStartPositionUs2 < MIN_LIVE_DEFAULT_START_POSITION_US) {
                defaultStartPositionUs = Math.min(MIN_LIVE_DEFAULT_START_POSITION_US, durationUs / 2);
            } else {
                defaultStartPositionUs = defaultStartPositionUs2;
            }
            timeline = new SinglePeriodTimeline(C.TIME_UNSET, durationUs, startTimeUs, defaultStartPositionUs, true, true);
        } else {
            durationUs = ssMediaSource.manifest.durationUs != C.TIME_UNSET ? ssMediaSource.manifest.durationUs : endTimeUs - startTimeUs;
            long j = endTimeUs;
            timeline = new SinglePeriodTimeline(startTimeUs + durationUs, durationUs, startTimeUs, 0, true, false);
            ssMediaSource.sourceListener.onSourceInfoRefreshed(ssMediaSource, timeline, ssMediaSource.manifest);
        }
        ssMediaSource.sourceListener.onSourceInfoRefreshed(ssMediaSource, timeline, ssMediaSource.manifest);
    }

    private void scheduleManifestRefresh() {
        if (this.manifest.isLive) {
            this.manifestRefreshHandler.postDelayed(new Runnable() {
                public void run() {
                    SsMediaSource.this.startLoadingManifest();
                }
            }, Math.max(0, (this.manifestLoadStartTimestamp + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) - SystemClock.elapsedRealtime()));
        }
    }

    private void startLoadingManifest() {
        ParsingLoadable<SsManifest> loadable = new ParsingLoadable(this.manifestDataSource, this.manifestUri, 4, this.manifestParser);
        this.eventDispatcher.loadStarted(loadable.dataSpec, loadable.type, this.manifestLoader.startLoading(loadable, this, this.minLoadableRetryCount));
    }
}
