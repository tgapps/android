package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper.Callback;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PlaylistEventListener;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

public final class HlsMediaPeriod implements MediaPeriod, Callback, PlaylistEventListener {
    private final Allocator allocator;
    private final boolean allowChunklessPreparation;
    private MediaPeriod.Callback callback;
    private final LoadErrorHandlingPolicy<Chunk> chunkLoadErrorHandlingPolicy;
    private SequenceableLoader compositeSequenceableLoader;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;
    private final HlsDataSourceFactory dataSourceFactory;
    private HlsSampleStreamWrapper[] enabledSampleStreamWrappers = new HlsSampleStreamWrapper[0];
    private final EventDispatcher eventDispatcher;
    private final HlsExtractorFactory extractorFactory;
    private final TransferListener mediaTransferListener;
    private final int minLoadableRetryCount;
    private boolean notifiedReadingStarted;
    private int pendingPrepareCount;
    private final HlsPlaylistTracker playlistTracker;
    private HlsSampleStreamWrapper[] sampleStreamWrappers = new HlsSampleStreamWrapper[0];
    private final IdentityHashMap<SampleStream, Integer> streamWrapperIndices = new IdentityHashMap();
    private final TimestampAdjusterProvider timestampAdjusterProvider = new TimestampAdjusterProvider();
    private TrackGroupArray trackGroups;

    public HlsMediaPeriod(HlsExtractorFactory extractorFactory, HlsPlaylistTracker playlistTracker, HlsDataSourceFactory dataSourceFactory, TransferListener mediaTransferListener, LoadErrorHandlingPolicy<Chunk> chunkLoadErrorHandlingPolicy, int minLoadableRetryCount, EventDispatcher eventDispatcher, Allocator allocator, CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory, boolean allowChunklessPreparation) {
        this.extractorFactory = extractorFactory;
        this.playlistTracker = playlistTracker;
        this.dataSourceFactory = dataSourceFactory;
        this.mediaTransferListener = mediaTransferListener;
        this.chunkLoadErrorHandlingPolicy = chunkLoadErrorHandlingPolicy;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventDispatcher = eventDispatcher;
        this.allocator = allocator;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;
        this.allowChunklessPreparation = allowChunklessPreparation;
        this.compositeSequenceableLoader = compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(new SequenceableLoader[0]);
        eventDispatcher.mediaPeriodCreated();
    }

    public void release() {
        this.playlistTracker.removeListener(this);
        for (HlsSampleStreamWrapper sampleStreamWrapper : this.sampleStreamWrappers) {
            sampleStreamWrapper.release();
        }
        this.callback = null;
        this.eventDispatcher.mediaPeriodReleased();
    }

    public void prepare(MediaPeriod.Callback callback, long positionUs) {
        this.callback = callback;
        this.playlistTracker.addListener(this);
        buildAndPrepareSampleStreamWrappers(positionUs);
    }

    public void maybeThrowPrepareError() throws IOException {
        for (HlsSampleStreamWrapper sampleStreamWrapper : this.sampleStreamWrappers) {
            sampleStreamWrapper.maybeThrowPrepareError();
        }
    }

    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }

    public long selectTracks(TrackSelection[] selections, boolean[] mayRetainStreamFlags, SampleStream[] streams, boolean[] streamResetFlags, long positionUs) {
        int i;
        int j;
        int[] streamChildIndices = new int[selections.length];
        int[] selectionChildIndices = new int[selections.length];
        for (i = 0; i < selections.length; i++) {
            int i2;
            if (streams[i] == null) {
                i2 = -1;
            } else {
                i2 = ((Integer) this.streamWrapperIndices.get(streams[i])).intValue();
            }
            streamChildIndices[i] = i2;
            selectionChildIndices[i] = -1;
            if (selections[i] != null) {
                TrackGroup trackGroup = selections[i].getTrackGroup();
                for (j = 0; j < this.sampleStreamWrappers.length; j++) {
                    if (this.sampleStreamWrappers[j].getTrackGroups().indexOf(trackGroup) != -1) {
                        selectionChildIndices[i] = j;
                        break;
                    }
                }
            }
        }
        boolean forceReset = false;
        this.streamWrapperIndices.clear();
        SampleStream[] newStreams = new SampleStream[selections.length];
        SampleStream[] childStreams = new SampleStream[selections.length];
        TrackSelection[] childSelections = new TrackSelection[selections.length];
        int newEnabledSampleStreamWrapperCount = 0;
        HlsSampleStreamWrapper[] newEnabledSampleStreamWrappers = new HlsSampleStreamWrapper[this.sampleStreamWrappers.length];
        i = 0;
        while (i < this.sampleStreamWrappers.length) {
            j = 0;
            while (j < selections.length) {
                childStreams[j] = streamChildIndices[j] == i ? streams[j] : null;
                childSelections[j] = selectionChildIndices[j] == i ? selections[j] : null;
                j++;
            }
            HlsSampleStreamWrapper sampleStreamWrapper = this.sampleStreamWrappers[i];
            boolean wasReset = sampleStreamWrapper.selectTracks(childSelections, mayRetainStreamFlags, childStreams, streamResetFlags, positionUs, forceReset);
            boolean wrapperEnabled = false;
            for (j = 0; j < selections.length; j++) {
                if (selectionChildIndices[j] == i) {
                    Assertions.checkState(childStreams[j] != null);
                    newStreams[j] = childStreams[j];
                    wrapperEnabled = true;
                    this.streamWrapperIndices.put(childStreams[j], Integer.valueOf(i));
                } else if (streamChildIndices[j] == i) {
                    Assertions.checkState(childStreams[j] == null);
                }
            }
            if (wrapperEnabled) {
                newEnabledSampleStreamWrappers[newEnabledSampleStreamWrapperCount] = sampleStreamWrapper;
                int newEnabledSampleStreamWrapperCount2 = newEnabledSampleStreamWrapperCount + 1;
                if (newEnabledSampleStreamWrapperCount == 0) {
                    sampleStreamWrapper.setIsTimestampMaster(true);
                    if (wasReset || this.enabledSampleStreamWrappers.length == 0 || sampleStreamWrapper != this.enabledSampleStreamWrappers[0]) {
                        this.timestampAdjusterProvider.reset();
                        forceReset = true;
                        newEnabledSampleStreamWrapperCount = newEnabledSampleStreamWrapperCount2;
                    }
                } else {
                    sampleStreamWrapper.setIsTimestampMaster(false);
                }
                newEnabledSampleStreamWrapperCount = newEnabledSampleStreamWrapperCount2;
            }
            i++;
        }
        System.arraycopy(newStreams, 0, streams, 0, newStreams.length);
        this.enabledSampleStreamWrappers = (HlsSampleStreamWrapper[]) Arrays.copyOf(newEnabledSampleStreamWrappers, newEnabledSampleStreamWrapperCount);
        this.compositeSequenceableLoader = this.compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(this.enabledSampleStreamWrappers);
        return positionUs;
    }

    public void discardBuffer(long positionUs, boolean toKeyframe) {
        for (HlsSampleStreamWrapper sampleStreamWrapper : this.enabledSampleStreamWrappers) {
            sampleStreamWrapper.discardBuffer(positionUs, toKeyframe);
        }
    }

    public void reevaluateBuffer(long positionUs) {
        this.compositeSequenceableLoader.reevaluateBuffer(positionUs);
    }

    public boolean continueLoading(long positionUs) {
        if (this.trackGroups != null) {
            return this.compositeSequenceableLoader.continueLoading(positionUs);
        }
        for (HlsSampleStreamWrapper wrapper : this.sampleStreamWrappers) {
            wrapper.continuePreparing();
        }
        return false;
    }

    public long getNextLoadPositionUs() {
        return this.compositeSequenceableLoader.getNextLoadPositionUs();
    }

    public long readDiscontinuity() {
        if (!this.notifiedReadingStarted) {
            this.eventDispatcher.readingStarted();
            this.notifiedReadingStarted = true;
        }
        return C.TIME_UNSET;
    }

    public long getBufferedPositionUs() {
        return this.compositeSequenceableLoader.getBufferedPositionUs();
    }

    public long seekToUs(long positionUs) {
        if (this.enabledSampleStreamWrappers.length > 0) {
            boolean forceReset = this.enabledSampleStreamWrappers[0].seekToUs(positionUs, false);
            for (int i = 1; i < this.enabledSampleStreamWrappers.length; i++) {
                this.enabledSampleStreamWrappers[i].seekToUs(positionUs, forceReset);
            }
            if (forceReset) {
                this.timestampAdjusterProvider.reset();
            }
        }
        return positionUs;
    }

    public long getAdjustedSeekPositionUs(long positionUs, SeekParameters seekParameters) {
        return positionUs;
    }

    public void onPrepared() {
        int i = 0;
        int i2 = this.pendingPrepareCount - 1;
        this.pendingPrepareCount = i2;
        if (i2 <= 0) {
            HlsSampleStreamWrapper sampleStreamWrapper;
            int totalTrackGroupCount = 0;
            for (HlsSampleStreamWrapper sampleStreamWrapper2 : this.sampleStreamWrappers) {
                totalTrackGroupCount += sampleStreamWrapper2.getTrackGroups().length;
            }
            TrackGroup[] trackGroupArray = new TrackGroup[totalTrackGroupCount];
            int trackGroupIndex = 0;
            HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr = this.sampleStreamWrappers;
            int length = hlsSampleStreamWrapperArr.length;
            while (i < length) {
                sampleStreamWrapper2 = hlsSampleStreamWrapperArr[i];
                int wrapperTrackGroupCount = sampleStreamWrapper2.getTrackGroups().length;
                int j = 0;
                int trackGroupIndex2 = trackGroupIndex;
                while (j < wrapperTrackGroupCount) {
                    trackGroupIndex = trackGroupIndex2 + 1;
                    trackGroupArray[trackGroupIndex2] = sampleStreamWrapper2.getTrackGroups().get(j);
                    j++;
                    trackGroupIndex2 = trackGroupIndex;
                }
                i++;
                trackGroupIndex = trackGroupIndex2;
            }
            this.trackGroups = new TrackGroupArray(trackGroupArray);
            this.callback.onPrepared(this);
        }
    }

    public void onPlaylistRefreshRequired(HlsUrl url) {
        this.playlistTracker.refreshPlaylist(url);
    }

    public void onContinueLoadingRequested(HlsSampleStreamWrapper sampleStreamWrapper) {
        this.callback.onContinueLoadingRequested(this);
    }

    public void onPlaylistChanged() {
        this.callback.onContinueLoadingRequested(this);
    }

    public boolean onPlaylistError(HlsUrl url, boolean shouldBlacklist) {
        boolean noBlacklistingFailure = true;
        for (HlsSampleStreamWrapper streamWrapper : this.sampleStreamWrappers) {
            noBlacklistingFailure &= streamWrapper.onPlaylistError(url, shouldBlacklist);
        }
        this.callback.onContinueLoadingRequested(this);
        return noBlacklistingFailure;
    }

    private void buildAndPrepareSampleStreamWrappers(long positionUs) {
        HlsMasterPlaylist masterPlaylist = this.playlistTracker.getMasterPlaylist();
        List<HlsUrl> audioRenditions = masterPlaylist.audios;
        List<HlsUrl> subtitleRenditions = masterPlaylist.subtitles;
        int wrapperCount = (audioRenditions.size() + 1) + subtitleRenditions.size();
        this.sampleStreamWrappers = new HlsSampleStreamWrapper[wrapperCount];
        this.pendingPrepareCount = wrapperCount;
        buildAndPrepareMainSampleStreamWrapper(masterPlaylist, positionUs);
        int currentWrapperIndex = 1;
        int i = 0;
        while (i < audioRenditions.size()) {
            HlsSampleStreamWrapper sampleStreamWrapper = buildSampleStreamWrapper(1, new HlsUrl[]{(HlsUrl) audioRenditions.get(i)}, null, Collections.emptyList(), positionUs);
            int currentWrapperIndex2 = currentWrapperIndex + 1;
            this.sampleStreamWrappers[currentWrapperIndex] = sampleStreamWrapper;
            Format renditionFormat = audioRendition.format;
            if (!this.allowChunklessPreparation || renditionFormat.codecs == null) {
                sampleStreamWrapper.continuePreparing();
            } else {
                TrackGroup[] trackGroupArr = new TrackGroup[1];
                trackGroupArr[0] = new TrackGroup(audioRendition.format);
                sampleStreamWrapper.prepareWithMasterPlaylistInfo(new TrackGroupArray(trackGroupArr), 0, TrackGroupArray.EMPTY);
            }
            i++;
            currentWrapperIndex = currentWrapperIndex2;
        }
        i = 0;
        while (i < subtitleRenditions.size()) {
            HlsUrl url = (HlsUrl) subtitleRenditions.get(i);
            sampleStreamWrapper = buildSampleStreamWrapper(3, new HlsUrl[]{url}, null, Collections.emptyList(), positionUs);
            currentWrapperIndex2 = currentWrapperIndex + 1;
            this.sampleStreamWrappers[currentWrapperIndex] = sampleStreamWrapper;
            trackGroupArr = new TrackGroup[1];
            trackGroupArr[0] = new TrackGroup(url.format);
            sampleStreamWrapper.prepareWithMasterPlaylistInfo(new TrackGroupArray(trackGroupArr), 0, TrackGroupArray.EMPTY);
            i++;
            currentWrapperIndex = currentWrapperIndex2;
        }
        this.enabledSampleStreamWrappers = this.sampleStreamWrappers;
    }

    private void buildAndPrepareMainSampleStreamWrapper(HlsMasterPlaylist masterPlaylist, long positionUs) {
        int i;
        List<HlsUrl> selectedVariants;
        List<HlsUrl> arrayList = new ArrayList(masterPlaylist.variants);
        ArrayList<HlsUrl> definiteVideoVariants = new ArrayList();
        ArrayList<HlsUrl> definiteAudioOnlyVariants = new ArrayList();
        for (i = 0; i < arrayList.size(); i++) {
            HlsUrl variant = (HlsUrl) arrayList.get(i);
            Format format = variant.format;
            if (format.height > 0 || Util.getCodecsOfType(format.codecs, 2) != null) {
                definiteVideoVariants.add(variant);
            } else if (Util.getCodecsOfType(format.codecs, 1) != null) {
                definiteAudioOnlyVariants.add(variant);
            }
        }
        if (!definiteVideoVariants.isEmpty()) {
            selectedVariants = definiteVideoVariants;
        } else if (definiteAudioOnlyVariants.size() < arrayList.size()) {
            arrayList.removeAll(definiteAudioOnlyVariants);
        }
        Assertions.checkArgument(!selectedVariants.isEmpty());
        HlsUrl[] variants = (HlsUrl[]) selectedVariants.toArray(new HlsUrl[0]);
        String codecs = variants[0].format.codecs;
        HlsSampleStreamWrapper sampleStreamWrapper = buildSampleStreamWrapper(0, variants, masterPlaylist.muxedAudioFormat, masterPlaylist.muxedCaptionFormats, positionUs);
        this.sampleStreamWrappers[0] = sampleStreamWrapper;
        if (!this.allowChunklessPreparation || codecs == null) {
            sampleStreamWrapper.setIsTimestampMaster(true);
            sampleStreamWrapper.continuePreparing();
            return;
        }
        boolean variantsContainVideoCodecs = Util.getCodecsOfType(codecs, 2) != null;
        boolean variantsContainAudioCodecs = Util.getCodecsOfType(codecs, 1) != null;
        List<TrackGroup> muxedTrackGroups = new ArrayList();
        if (variantsContainVideoCodecs) {
            Format[] videoFormats = new Format[selectedVariants.size()];
            for (i = 0; i < videoFormats.length; i++) {
                videoFormats[i] = deriveVideoFormat(variants[i].format);
            }
            muxedTrackGroups.add(new TrackGroup(videoFormats));
            if (variantsContainAudioCodecs && (masterPlaylist.muxedAudioFormat != null || masterPlaylist.audios.isEmpty())) {
                muxedTrackGroups.add(new TrackGroup(deriveAudioFormat(variants[0].format, masterPlaylist.muxedAudioFormat, false)));
            }
            List<Format> ccFormats = masterPlaylist.muxedCaptionFormats;
            if (ccFormats != null) {
                for (i = 0; i < ccFormats.size(); i++) {
                    muxedTrackGroups.add(new TrackGroup((Format) ccFormats.get(i)));
                }
            }
        } else if (variantsContainAudioCodecs) {
            Format[] audioFormats = new Format[selectedVariants.size()];
            for (i = 0; i < audioFormats.length; i++) {
                audioFormats[i] = deriveAudioFormat(variants[i].format, masterPlaylist.muxedAudioFormat, true);
            }
            muxedTrackGroups.add(new TrackGroup(audioFormats));
        } else {
            throw new IllegalArgumentException("Unexpected codecs attribute: " + codecs);
        }
        muxedTrackGroups.add(new TrackGroup(Format.createSampleFormat("ID3", MimeTypes.APPLICATION_ID3, null, -1, null)));
        sampleStreamWrapper.prepareWithMasterPlaylistInfo(new TrackGroupArray((TrackGroup[]) muxedTrackGroups.toArray(new TrackGroup[0])), 0, new TrackGroupArray(r0));
    }

    private HlsSampleStreamWrapper buildSampleStreamWrapper(int trackType, HlsUrl[] variants, Format muxedAudioFormat, List<Format> muxedCaptionFormats, long positionUs) {
        return new HlsSampleStreamWrapper(trackType, this, new HlsChunkSource(this.extractorFactory, this.playlistTracker, variants, this.dataSourceFactory, this.mediaTransferListener, this.timestampAdjusterProvider, muxedCaptionFormats), this.allocator, positionUs, muxedAudioFormat, this.chunkLoadErrorHandlingPolicy, this.minLoadableRetryCount, this.eventDispatcher);
    }

    private static Format deriveVideoFormat(Format variantFormat) {
        String codecs = Util.getCodecsOfType(variantFormat.codecs, 2);
        return Format.createVideoContainerFormat(variantFormat.id, variantFormat.label, variantFormat.containerMimeType, MimeTypes.getMediaMimeType(codecs), codecs, variantFormat.bitrate, variantFormat.width, variantFormat.height, variantFormat.frameRate, null, variantFormat.selectionFlags);
    }

    private static Format deriveAudioFormat(Format variantFormat, Format mediaTagFormat, boolean isPrimaryTrackInVariant) {
        String codecs;
        int bitrate;
        int channelCount = -1;
        int selectionFlags = 0;
        String language = null;
        String label = null;
        if (mediaTagFormat != null) {
            codecs = mediaTagFormat.codecs;
            channelCount = mediaTagFormat.channelCount;
            selectionFlags = mediaTagFormat.selectionFlags;
            language = mediaTagFormat.language;
            label = mediaTagFormat.label;
        } else {
            codecs = Util.getCodecsOfType(variantFormat.codecs, 1);
            if (isPrimaryTrackInVariant) {
                channelCount = variantFormat.channelCount;
                selectionFlags = variantFormat.selectionFlags;
                language = variantFormat.label;
                label = variantFormat.label;
            }
        }
        String sampleMimeType = MimeTypes.getMediaMimeType(codecs);
        if (isPrimaryTrackInVariant) {
            bitrate = variantFormat.bitrate;
        } else {
            bitrate = -1;
        }
        return Format.createAudioContainerFormat(variantFormat.id, label, variantFormat.containerMimeType, sampleMimeType, codecs, bitrate, channelCount, -1, null, selectionFlags, language);
    }
}
