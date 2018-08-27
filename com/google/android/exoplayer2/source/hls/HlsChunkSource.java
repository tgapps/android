package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.os.SystemClock;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.exoplayer2.source.chunk.DataChunk;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.trackselection.BaseTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

class HlsChunkSource {
    private final DataSource encryptionDataSource;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private HlsUrl expectedPlaylistUrl;
    private final HlsExtractorFactory extractorFactory;
    private IOException fatalError;
    private boolean independentSegments;
    private boolean isTimestampMaster;
    private long liveEdgeInPeriodTimeUs = C.TIME_UNSET;
    private final DataSource mediaDataSource;
    private final List<Format> muxedCaptionFormats;
    private final HlsPlaylistTracker playlistTracker;
    private byte[] scratchSpace;
    private boolean seenExpectedPlaylistError;
    private final TimestampAdjusterProvider timestampAdjusterProvider;
    private final TrackGroup trackGroup;
    private TrackSelection trackSelection;
    private final HlsUrl[] variants;

    public static final class HlsChunkHolder {
        public Chunk chunk;
        public boolean endOfStream;
        public HlsUrl playlist;

        public HlsChunkHolder() {
            clear();
        }

        public void clear() {
            this.chunk = null;
            this.endOfStream = false;
            this.playlist = null;
        }
    }

    private static final class InitializationTrackSelection extends BaseTrackSelection {
        private int selectedIndex;

        public InitializationTrackSelection(TrackGroup group, int[] tracks) {
            super(group, tracks);
            this.selectedIndex = indexOf(group.getFormat(0));
        }

        public void updateSelectedTrack(long playbackPositionUs, long bufferedDurationUs, long availableDurationUs) {
            long nowMs = SystemClock.elapsedRealtime();
            if (isBlacklisted(this.selectedIndex, nowMs)) {
                int i = this.length - 1;
                while (i >= 0) {
                    if (isBlacklisted(i, nowMs)) {
                        i--;
                    } else {
                        this.selectedIndex = i;
                        return;
                    }
                }
                throw new IllegalStateException();
            }
        }

        public int getSelectedIndex() {
            return this.selectedIndex;
        }

        public int getSelectionReason() {
            return 0;
        }

        public Object getSelectionData() {
            return null;
        }
    }

    private static final class EncryptionKeyChunk extends DataChunk {
        public final String iv;
        private byte[] result;

        public EncryptionKeyChunk(DataSource dataSource, DataSpec dataSpec, Format trackFormat, int trackSelectionReason, Object trackSelectionData, byte[] scratchSpace, String iv) {
            super(dataSource, dataSpec, 3, trackFormat, trackSelectionReason, trackSelectionData, scratchSpace);
            this.iv = iv;
        }

        protected void consume(byte[] data, int limit) throws IOException {
            this.result = Arrays.copyOf(data, limit);
        }

        public byte[] getResult() {
            return this.result;
        }
    }

    public HlsChunkSource(HlsExtractorFactory extractorFactory, HlsPlaylistTracker playlistTracker, HlsUrl[] variants, HlsDataSourceFactory dataSourceFactory, TransferListener mediaTransferListener, TimestampAdjusterProvider timestampAdjusterProvider, List<Format> muxedCaptionFormats) {
        this.extractorFactory = extractorFactory;
        this.playlistTracker = playlistTracker;
        this.variants = variants;
        this.timestampAdjusterProvider = timestampAdjusterProvider;
        this.muxedCaptionFormats = muxedCaptionFormats;
        Format[] variantFormats = new Format[variants.length];
        int[] initialTrackSelection = new int[variants.length];
        for (int i = 0; i < variants.length; i++) {
            variantFormats[i] = variants[i].format;
            initialTrackSelection[i] = i;
        }
        this.mediaDataSource = dataSourceFactory.createDataSource(1);
        if (mediaTransferListener != null) {
            this.mediaDataSource.addTransferListener(mediaTransferListener);
        }
        this.encryptionDataSource = dataSourceFactory.createDataSource(3);
        this.trackGroup = new TrackGroup(variantFormats);
        this.trackSelection = new InitializationTrackSelection(this.trackGroup, initialTrackSelection);
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        } else if (this.expectedPlaylistUrl != null && this.seenExpectedPlaylistError) {
            this.playlistTracker.maybeThrowPlaylistRefreshError(this.expectedPlaylistUrl);
        }
    }

    public TrackGroup getTrackGroup() {
        return this.trackGroup;
    }

    public void selectTracks(TrackSelection trackSelection) {
        this.trackSelection = trackSelection;
    }

    public TrackSelection getTrackSelection() {
        return this.trackSelection;
    }

    public void reset() {
        this.fatalError = null;
    }

    public void setIsTimestampMaster(boolean isTimestampMaster) {
        this.isTimestampMaster = isTimestampMaster;
    }

    public void getNextChunk(long playbackPositionUs, long loadPositionUs, List<HlsMediaChunk> queue, HlsChunkHolder out) {
        int oldVariantIndex;
        HlsMediaChunk previous = queue.isEmpty() ? null : (HlsMediaChunk) queue.get(queue.size() - 1);
        if (previous == null) {
            oldVariantIndex = -1;
        } else {
            oldVariantIndex = this.trackGroup.indexOf(previous.trackFormat);
        }
        long bufferedDurationUs = loadPositionUs - playbackPositionUs;
        long timeToLiveEdgeUs = resolveTimeToLiveEdgeUs(playbackPositionUs);
        if (!(previous == null || this.independentSegments)) {
            long subtractedDurationUs = previous.getDurationUs();
            bufferedDurationUs = Math.max(0, bufferedDurationUs - subtractedDurationUs);
            if (timeToLiveEdgeUs != C.TIME_UNSET) {
                timeToLiveEdgeUs = Math.max(0, timeToLiveEdgeUs - subtractedDurationUs);
            }
        }
        this.trackSelection.updateSelectedTrack(playbackPositionUs, bufferedDurationUs, timeToLiveEdgeUs);
        int selectedVariantIndex = this.trackSelection.getSelectedIndexInTrackGroup();
        boolean switchingVariant = oldVariantIndex != selectedVariantIndex;
        HlsUrl selectedUrl = this.variants[selectedVariantIndex];
        if (this.playlistTracker.isSnapshotValid(selectedUrl)) {
            long chunkMediaSequence;
            HlsMediaPlaylist mediaPlaylist = this.playlistTracker.getPlaylistSnapshot(selectedUrl);
            this.independentSegments = mediaPlaylist.hasIndependentSegments;
            updateLiveEdgeTimeUs(mediaPlaylist);
            long startOfPlaylistInPeriodUs = mediaPlaylist.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
            if (previous == null || switchingVariant) {
                long endOfPlaylistInPeriodUs = startOfPlaylistInPeriodUs + mediaPlaylist.durationUs;
                long targetPositionInPeriodUs = (previous == null || this.independentSegments) ? loadPositionUs : previous.startTimeUs;
                if (mediaPlaylist.hasEndTag || targetPositionInPeriodUs < endOfPlaylistInPeriodUs) {
                    long targetPositionInPlaylistUs = targetPositionInPeriodUs - startOfPlaylistInPeriodUs;
                    List list = mediaPlaylist.segments;
                    Comparable valueOf = Long.valueOf(targetPositionInPlaylistUs);
                    boolean z = !this.playlistTracker.isLive() || previous == null;
                    chunkMediaSequence = ((long) Util.binarySearchFloor(list, valueOf, true, z)) + mediaPlaylist.mediaSequence;
                    if (chunkMediaSequence < mediaPlaylist.mediaSequence && previous != null) {
                        selectedVariantIndex = oldVariantIndex;
                        selectedUrl = this.variants[selectedVariantIndex];
                        mediaPlaylist = this.playlistTracker.getPlaylistSnapshot(selectedUrl);
                        startOfPlaylistInPeriodUs = mediaPlaylist.startTimeUs - this.playlistTracker.getInitialStartTimeUs();
                        chunkMediaSequence = previous.getNextChunkIndex();
                    }
                } else {
                    chunkMediaSequence = mediaPlaylist.mediaSequence + ((long) mediaPlaylist.segments.size());
                }
            } else {
                chunkMediaSequence = previous.getNextChunkIndex();
            }
            if (chunkMediaSequence < mediaPlaylist.mediaSequence) {
                this.fatalError = new BehindLiveWindowException();
                return;
            }
            int chunkIndex = (int) (chunkMediaSequence - mediaPlaylist.mediaSequence);
            if (chunkIndex < mediaPlaylist.segments.size()) {
                this.seenExpectedPlaylistError = false;
                this.expectedPlaylistUrl = null;
                Segment segment = (Segment) mediaPlaylist.segments.get(chunkIndex);
                if (segment.fullSegmentEncryptionKeyUri != null) {
                    Uri keyUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.fullSegmentEncryptionKeyUri);
                    if (!keyUri.equals(this.encryptionKeyUri)) {
                        out.chunk = newEncryptionKeyChunk(keyUri, segment.encryptionIV, selectedVariantIndex, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData());
                        return;
                    } else if (!Util.areEqual(segment.encryptionIV, this.encryptionIvString)) {
                        setEncryptionData(keyUri, segment.encryptionIV, this.encryptionKey);
                    }
                } else {
                    clearEncryptionData();
                }
                DataSpec initDataSpec = null;
                Segment initSegment = segment.initializationSegment;
                if (initSegment != null) {
                    initDataSpec = new DataSpec(UriUtil.resolveToUri(mediaPlaylist.baseUri, initSegment.url), initSegment.byterangeOffset, initSegment.byterangeLength, null);
                }
                long segmentStartTimeInPeriodUs = startOfPlaylistInPeriodUs + segment.relativeStartTimeUs;
                int discontinuitySequence = mediaPlaylist.discontinuitySequence + segment.relativeDiscontinuitySequence;
                out.chunk = new HlsMediaChunk(this.extractorFactory, this.mediaDataSource, new DataSpec(UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.url), segment.byterangeOffset, segment.byterangeLength, null), initDataSpec, selectedUrl, this.muxedCaptionFormats, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), segmentStartTimeInPeriodUs, segmentStartTimeInPeriodUs + segment.durationUs, chunkMediaSequence, discontinuitySequence, segment.hasGapTag, this.isTimestampMaster, this.timestampAdjusterProvider.getAdjuster(discontinuitySequence), previous, mediaPlaylist.drmInitData, this.encryptionKey, this.encryptionIv);
                return;
            } else if (mediaPlaylist.hasEndTag) {
                out.endOfStream = true;
                return;
            } else {
                out.playlist = selectedUrl;
                this.seenExpectedPlaylistError = (this.expectedPlaylistUrl == selectedUrl ? 1 : 0) & this.seenExpectedPlaylistError;
                this.expectedPlaylistUrl = selectedUrl;
                return;
            }
        }
        out.playlist = selectedUrl;
        this.seenExpectedPlaylistError = (this.expectedPlaylistUrl == selectedUrl ? 1 : 0) & this.seenExpectedPlaylistError;
        this.expectedPlaylistUrl = selectedUrl;
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof EncryptionKeyChunk) {
            EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk) chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.iv, encryptionKeyChunk.getResult());
        }
    }

    public boolean maybeBlacklistTrack(Chunk chunk, long blacklistDurationMs) {
        return this.trackSelection.blacklist(this.trackSelection.indexOf(this.trackGroup.indexOf(chunk.trackFormat)), blacklistDurationMs);
    }

    public boolean onPlaylistError(HlsUrl url, boolean shouldBlacklist) {
        boolean z = false;
        int trackGroupIndex = this.trackGroup.indexOf(url.format);
        if (trackGroupIndex == -1) {
            return true;
        }
        int trackSelectionIndex = this.trackSelection.indexOf(trackGroupIndex);
        if (trackSelectionIndex == -1) {
            return true;
        }
        int i;
        boolean z2 = this.seenExpectedPlaylistError;
        if (this.expectedPlaylistUrl == url) {
            i = 1;
        } else {
            i = 0;
        }
        this.seenExpectedPlaylistError = i | z2;
        if (!shouldBlacklist || this.trackSelection.blacklist(trackSelectionIndex, ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS)) {
            z = true;
        }
        return z;
    }

    private long resolveTimeToLiveEdgeUs(long playbackPositionUs) {
        if (this.liveEdgeInPeriodTimeUs != C.TIME_UNSET) {
            return this.liveEdgeInPeriodTimeUs - playbackPositionUs;
        }
        return C.TIME_UNSET;
    }

    private void updateLiveEdgeTimeUs(HlsMediaPlaylist mediaPlaylist) {
        long j;
        if (mediaPlaylist.hasEndTag) {
            j = C.TIME_UNSET;
        } else {
            j = mediaPlaylist.getEndTimeUs() - this.playlistTracker.getInitialStartTimeUs();
        }
        this.liveEdgeInPeriodTimeUs = j;
    }

    private EncryptionKeyChunk newEncryptionKeyChunk(Uri keyUri, String iv, int variantIndex, int trackSelectionReason, Object trackSelectionData) {
        return new EncryptionKeyChunk(this.encryptionDataSource, new DataSpec(keyUri, 0, -1, null, 1), this.variants[variantIndex].format, trackSelectionReason, trackSelectionData, this.scratchSpace, iv);
    }

    private void setEncryptionData(Uri keyUri, String iv, byte[] secretKey) {
        String trimmedIv;
        if (Util.toLowerInvariant(iv).startsWith("0x")) {
            trimmedIv = iv.substring(2);
        } else {
            trimmedIv = iv;
        }
        byte[] ivData = new BigInteger(trimmedIv, 16).toByteArray();
        byte[] ivDataWithPadding = new byte[16];
        int offset = ivData.length > 16 ? ivData.length - 16 : 0;
        System.arraycopy(ivData, offset, ivDataWithPadding, (ivDataWithPadding.length - ivData.length) + offset, ivData.length - offset);
        this.encryptionKeyUri = keyUri;
        this.encryptionKey = secretKey;
        this.encryptionIvString = iv;
        this.encryptionIv = ivDataWithPadding;
    }

    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }
}
