package org.telegram.messenger.exoplayer2.source.hls;

import android.net.Uri;
import android.os.SystemClock;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.source.TrackGroup;
import org.telegram.messenger.exoplayer2.source.chunk.Chunk;
import org.telegram.messenger.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import org.telegram.messenger.exoplayer2.source.chunk.DataChunk;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import org.telegram.messenger.exoplayer2.trackselection.BaseTrackSelection;
import org.telegram.messenger.exoplayer2.trackselection.TrackSelection;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.DataSpec;
import org.telegram.messenger.exoplayer2.util.Util;

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
    private long liveEdgeTimeUs = C.TIME_UNSET;
    private final DataSource mediaDataSource;
    private final List<Format> muxedCaptionFormats;
    private final HlsPlaylistTracker playlistTracker;
    private byte[] scratchSpace;
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

    public void getNextChunk(org.telegram.messenger.exoplayer2.source.hls.HlsMediaChunk r1, long r2, long r4, org.telegram.messenger.exoplayer2.source.hls.HlsChunkSource.HlsChunkHolder r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.hls.HlsChunkSource.getNextChunk(org.telegram.messenger.exoplayer2.source.hls.HlsMediaChunk, long, long, org.telegram.messenger.exoplayer2.source.hls.HlsChunkSource$HlsChunkHolder):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r6 = r44;
        r15 = r45;
        r4 = r46;
        r14 = r50;
        if (r15 != 0) goto L_0x000c;
    L_0x000a:
        r0 = -1;
        goto L_0x0014;
    L_0x000c:
        r0 = r6.trackGroup;
        r1 = r15.trackFormat;
        r0 = r0.indexOf(r1);
    L_0x0014:
        r3 = r0;
        r0 = 0;
        r6.expectedPlaylistUrl = r0;
        r0 = r48 - r4;
        r7 = r6.resolveTimeToLiveEdgeUs(r4);
        if (r15 == 0) goto L_0x004b;
    L_0x0020:
        r2 = r6.independentSegments;
        if (r2 != 0) goto L_0x004b;
    L_0x0024:
        r9 = r45.getDurationUs();
        r11 = r0 - r9;
        r28 = r0;
        r0 = 0;
        r11 = java.lang.Math.max(r0, r11);
        r16 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r2 = (r7 > r16 ? 1 : (r7 == r16 ? 0 : -1));
        if (r2 == 0) goto L_0x0046;
    L_0x003b:
        r30 = r11;
        r11 = r7 - r9;
        r0 = java.lang.Math.max(r0, r11);
        r28 = r0;
        goto L_0x0051;
    L_0x0046:
        r30 = r11;
        r28 = r7;
        goto L_0x0051;
    L_0x004b:
        r28 = r0;
        r30 = r28;
        r28 = r7;
    L_0x0051:
        r7 = r6.trackSelection;
        r8 = r4;
        r10 = r30;
        r12 = r28;
        r7.updateSelectedTrack(r8, r10, r12);
        r0 = r6.trackSelection;
        r0 = r0.getSelectedIndexInTrackGroup();
        if (r3 == r0) goto L_0x0065;
    L_0x0063:
        r7 = 1;
        goto L_0x0066;
    L_0x0065:
        r7 = 0;
    L_0x0066:
        r32 = r7;
        r7 = r6.variants;
        r7 = r7[r0];
        r8 = r6.playlistTracker;
        r8 = r8.isSnapshotValid(r7);
        if (r8 != 0) goto L_0x0079;
    L_0x0074:
        r14.playlist = r7;
        r6.expectedPlaylistUrl = r7;
        return;
    L_0x0079:
        r8 = r6.playlistTracker;
        r8 = r8.getPlaylistSnapshot(r7);
        r9 = r8.hasIndependentSegmentsTag;
        r6.independentSegments = r9;
        r6.updateLiveEdgeTimeUs(r8);
        if (r15 == 0) goto L_0x0094;
    L_0x0088:
        if (r32 == 0) goto L_0x008b;
    L_0x008a:
        goto L_0x0094;
    L_0x008b:
        r1 = r45.getNextChunkIndex();
        r34 = r0;
        r12 = r7;
        r13 = r8;
        goto L_0x00ee;
    L_0x0094:
        if (r15 == 0) goto L_0x009e;
    L_0x0096:
        r9 = r6.independentSegments;
        if (r9 == 0) goto L_0x009b;
    L_0x009a:
        goto L_0x009e;
    L_0x009b:
        r9 = r15.startTimeUs;
        goto L_0x00a0;
    L_0x009e:
        r9 = r48;
    L_0x00a0:
        r11 = r8.hasEndTag;
        if (r11 != 0) goto L_0x00b6;
    L_0x00a4:
        r11 = r8.getEndTimeUs();
        r13 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));
        if (r13 < 0) goto L_0x00b6;
    L_0x00ac:
        r1 = r8.mediaSequence;
        r11 = r8.segments;
        r11 = r11.size();
        r1 = r1 + r11;
        goto L_0x00ed;
    L_0x00b6:
        r11 = r8.segments;
        r12 = r8.startTimeUs;
        r1 = r9 - r12;
        r1 = java.lang.Long.valueOf(r1);
        r2 = r6.playlistTracker;
        r2 = r2.isLive();
        if (r2 == 0) goto L_0x00cd;
    L_0x00c8:
        if (r15 != 0) goto L_0x00cb;
        goto L_0x00cd;
        r2 = 0;
        goto L_0x00cf;
        r2 = 1;
        r12 = 1;
        r1 = org.telegram.messenger.exoplayer2.util.Util.binarySearchFloor(r11, r1, r12, r2);
        r2 = r8.mediaSequence;
        r1 = r1 + r2;
        r2 = r8.mediaSequence;
        if (r1 >= r2) goto L_0x00ed;
        if (r15 == 0) goto L_0x00ed;
        r0 = r3;
        r2 = r6.variants;
        r7 = r2[r0];
        r2 = r6.playlistTracker;
        r2 = r2.getPlaylistSnapshot(r7);
        r1 = r45.getNextChunkIndex();
        r8 = r2;
    L_0x00ed:
        goto L_0x008f;
    L_0x00ee:
        r11 = r1;
        r0 = r13.mediaSequence;
        if (r11 >= r0) goto L_0x00fb;
        r0 = new org.telegram.messenger.exoplayer2.source.BehindLiveWindowException;
        r0.<init>();
        r6.fatalError = r0;
        return;
        r0 = r13.mediaSequence;
        r9 = r11 - r0;
        r0 = r13.segments;
        r0 = r0.size();
        if (r9 < r0) goto L_0x0114;
        r0 = r13.hasEndTag;
        if (r0 == 0) goto L_0x010f;
        r0 = 1;
        r14.endOfStream = r0;
        goto L_0x0113;
        r14.playlist = r12;
        r6.expectedPlaylistUrl = r12;
        return;
        r0 = r13.segments;
        r0 = r0.get(r9);
        r8 = r0;
        r8 = (org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment) r8;
        r0 = r8.fullSegmentEncryptionKeyUri;
        if (r0 == 0) goto L_0x0163;
        r0 = r13.baseUri;
        r1 = r8.fullSegmentEncryptionKeyUri;
        r7 = org.telegram.messenger.exoplayer2.util.UriUtil.resolveToUri(r0, r1);
        r0 = r6.encryptionKeyUri;
        r0 = r7.equals(r0);
        if (r0 != 0) goto L_0x014f;
        r2 = r8.encryptionIV;
        r0 = r6.trackSelection;
        r10 = r0.getSelectionReason();
        r0 = r6.trackSelection;
        r16 = r0.getSelectionData();
        r0 = r6;
        r1 = r7;
        r33 = r3;
        r3 = r34;
        r4 = r10;
        r5 = r16;
        r0 = r0.newEncryptionKeyChunk(r1, r2, r3, r4, r5);
        r14.chunk = r0;
        return;
        r33 = r3;
        r0 = r8.encryptionIV;
        r1 = r6.encryptionIvString;
        r0 = org.telegram.messenger.exoplayer2.util.Util.areEqual(r0, r1);
        if (r0 != 0) goto L_0x0162;
        r0 = r8.encryptionIV;
        r1 = r6.encryptionKey;
        r6.setEncryptionData(r7, r0, r1);
        goto L_0x0168;
        r33 = r3;
        r44.clearEncryptionData();
        r0 = 0;
        r1 = r13.initializationSegment;
        if (r1 == 0) goto L_0x018c;
        r2 = r13.baseUri;
        r3 = r1.url;
        r2 = org.telegram.messenger.exoplayer2.util.UriUtil.resolveToUri(r2, r3);
        r3 = new org.telegram.messenger.exoplayer2.upstream.DataSpec;
        r4 = r1.byterangeOffset;
        r35 = r9;
        r9 = r1.byterangeLength;
        r22 = 0;
        r16 = r3;
        r17 = r2;
        r18 = r4;
        r20 = r9;
        r16.<init>(r17, r18, r20, r22);
        r0 = r3;
        goto L_0x018e;
        r35 = r9;
        r2 = r13.startTimeUs;
        r4 = r8.relativeStartTimeUs;
        r36 = r2 + r4;
        r2 = r13.discontinuitySequence;
        r3 = r8.relativeDiscontinuitySequence;
        r2 = r2 + r3;
        r3 = r6.timestampAdjusterProvider;
        r3 = r3.getAdjuster(r2);
        r4 = r13.baseUri;
        r5 = r8.url;
        r4 = org.telegram.messenger.exoplayer2.util.UriUtil.resolveToUri(r4, r5);
        r5 = new org.telegram.messenger.exoplayer2.upstream.DataSpec;
        r9 = r8.byterangeOffset;
        r39 = r11;
        r38 = r12;
        r11 = r8.byterangeLength;
        r22 = 0;
        r16 = r5;
        r17 = r4;
        r18 = r9;
        r20 = r11;
        r16.<init>(r17, r18, r20, r22);
        r10 = r5;
        r5 = new org.telegram.messenger.exoplayer2.source.hls.HlsMediaChunk;
        r9 = r6.extractorFactory;
        r11 = r6.mediaDataSource;
        r12 = r6.muxedCaptionFormats;
        r7 = r6.trackSelection;
        r16 = r7.getSelectionReason();
        r7 = r6.trackSelection;
        r17 = r7.getSelectionData();
        r14 = r8.durationUs;
        r18 = r36 + r14;
        r15 = r6.isTimestampMaster;
        r14 = r13.drmInitData;
        r7 = r6.encryptionKey;
        r40 = r1;
        r1 = r6.encryptionIv;
        r26 = r7;
        r7 = r5;
        r41 = r8;
        r8 = r9;
        r9 = r11;
        r11 = r0;
        r20 = r12;
        r12 = r38;
        r42 = r13;
        r13 = r20;
        r43 = r0;
        r25 = r14;
        r0 = r50;
        r14 = r16;
        r22 = r15;
        r15 = r17;
        r16 = r36;
        r20 = r39;
        r21 = r2;
        r23 = r3;
        r24 = r45;
        r27 = r1;
        r7.<init>(r8, r9, r10, r11, r12, r13, r14, r15, r16, r18, r20, r21, r22, r23, r24, r25, r26, r27);
        r0.chunk = r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.hls.HlsChunkSource.getNextChunk(org.telegram.messenger.exoplayer2.source.hls.HlsMediaChunk, long, long, org.telegram.messenger.exoplayer2.source.hls.HlsChunkSource$HlsChunkHolder):void");
    }

    public HlsChunkSource(HlsExtractorFactory extractorFactory, HlsPlaylistTracker playlistTracker, HlsUrl[] variants, HlsDataSourceFactory dataSourceFactory, TimestampAdjusterProvider timestampAdjusterProvider, List<Format> muxedCaptionFormats) {
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
        this.encryptionDataSource = dataSourceFactory.createDataSource(3);
        this.trackGroup = new TrackGroup(variantFormats);
        this.trackSelection = new InitializationTrackSelection(this.trackGroup, initialTrackSelection);
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        } else if (this.expectedPlaylistUrl != null) {
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

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof EncryptionKeyChunk) {
            EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk) chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.iv, encryptionKeyChunk.getResult());
        }
    }

    public boolean onChunkLoadError(Chunk chunk, boolean cancelable, IOException error) {
        return cancelable && ChunkedTrackBlacklistUtil.maybeBlacklistTrack(this.trackSelection, this.trackSelection.indexOf(this.trackGroup.indexOf(chunk.trackFormat)), error);
    }

    public void onPlaylistBlacklisted(HlsUrl url, long blacklistMs) {
        int trackGroupIndex = this.trackGroup.indexOf(url.format);
        if (trackGroupIndex != -1) {
            int trackSelectionIndex = this.trackSelection.indexOf(trackGroupIndex);
            if (trackSelectionIndex != -1) {
                this.trackSelection.blacklist(trackSelectionIndex, blacklistMs);
            }
        }
    }

    private long resolveTimeToLiveEdgeUs(long playbackPositionUs) {
        if (this.liveEdgeTimeUs != C.TIME_UNSET) {
            return this.liveEdgeTimeUs - playbackPositionUs;
        }
        return C.TIME_UNSET;
    }

    private void updateLiveEdgeTimeUs(HlsMediaPlaylist mediaPlaylist) {
        this.liveEdgeTimeUs = mediaPlaylist.hasEndTag ? C.TIME_UNSET : mediaPlaylist.getEndTimeUs();
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
