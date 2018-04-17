package org.telegram.messenger.exoplayer2.source.hls;

import android.util.Pair;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.drm.DrmInitData;
import org.telegram.messenger.exoplayer2.extractor.DefaultExtractorInput;
import org.telegram.messenger.exoplayer2.extractor.Extractor;
import org.telegram.messenger.exoplayer2.extractor.ExtractorInput;
import org.telegram.messenger.exoplayer2.metadata.Metadata;
import org.telegram.messenger.exoplayer2.metadata.Metadata.Entry;
import org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder;
import org.telegram.messenger.exoplayer2.metadata.id3.PrivFrame;
import org.telegram.messenger.exoplayer2.source.chunk.MediaChunk;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.DataSpec;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.TimestampAdjuster;
import org.telegram.messenger.exoplayer2.util.Util;

final class HlsMediaChunk extends MediaChunk {
    private static final String PRIV_TIMESTAMP_FRAME_OWNER = "com.apple.streaming.transportStreamTimestamp";
    private static final AtomicInteger uidSource = new AtomicInteger();
    private int bytesLoaded;
    public final int discontinuitySequenceNumber;
    private final Extractor extractor;
    public final HlsUrl hlsUrl;
    private final ParsableByteArray id3Data;
    private final Id3Decoder id3Decoder;
    private boolean id3TimestampPeeked;
    private final DataSource initDataSource;
    private final DataSpec initDataSpec;
    private boolean initLoadCompleted;
    private int initSegmentBytesLoaded;
    private final boolean isEncrypted = (this.dataSource instanceof Aes128DataSource);
    private final boolean isMasterTimestampSource;
    private final boolean isPackedAudioExtractor;
    private volatile boolean loadCanceled;
    private volatile boolean loadCompleted;
    private HlsSampleStreamWrapper output;
    private final boolean reusingExtractor;
    private final boolean shouldSpliceIn;
    private final TimestampAdjuster timestampAdjuster;
    public final int uid;

    private void loadMedia() throws java.io.IOException, java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.hls.HlsMediaChunk.loadMedia():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r13.isEncrypted;
        r1 = 0;
        r2 = 1;
        if (r0 == 0) goto L_0x0010;
    L_0x0006:
        r0 = r13.dataSpec;
        r3 = r13.bytesLoaded;
        if (r3 == 0) goto L_0x000e;
    L_0x000c:
        r3 = r2;
        goto L_0x000f;
    L_0x000e:
        r3 = r1;
    L_0x000f:
        goto L_0x001a;
    L_0x0010:
        r0 = r13.dataSpec;
        r3 = r13.bytesLoaded;
        r3 = (long) r3;
        r0 = r0.subrange(r3);
        r3 = r1;
    L_0x001a:
        r4 = r13.isMasterTimestampSource;
        if (r4 != 0) goto L_0x0024;
    L_0x001e:
        r4 = r13.timestampAdjuster;
        r4.waitUntilInitialized();
        goto L_0x003a;
    L_0x0024:
        r4 = r13.timestampAdjuster;
        r4 = r4.getFirstSampleTimestampUs();
        r6 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r8 != 0) goto L_0x003a;
    L_0x0033:
        r4 = r13.timestampAdjuster;
        r5 = r13.startTimeUs;
        r4.setFirstSampleTimestampUs(r5);
    L_0x003a:
        r4 = new org.telegram.messenger.exoplayer2.extractor.DefaultExtractorInput;	 Catch:{ all -> 0x00ac }
        r8 = r13.dataSource;	 Catch:{ all -> 0x00ac }
        r9 = r0.absoluteStreamPosition;	 Catch:{ all -> 0x00ac }
        r5 = r13.dataSource;	 Catch:{ all -> 0x00ac }
        r11 = r5.open(r0);	 Catch:{ all -> 0x00ac }
        r7 = r4;	 Catch:{ all -> 0x00ac }
        r7.<init>(r8, r9, r11);	 Catch:{ all -> 0x00ac }
        r5 = r13.isPackedAudioExtractor;	 Catch:{ all -> 0x00ac }
        if (r5 == 0) goto L_0x006f;	 Catch:{ all -> 0x00ac }
    L_0x004e:
        r5 = r13.id3TimestampPeeked;	 Catch:{ all -> 0x00ac }
        if (r5 != 0) goto L_0x006f;	 Catch:{ all -> 0x00ac }
    L_0x0052:
        r5 = r13.peekId3PrivTimestamp(r4);	 Catch:{ all -> 0x00ac }
        r13.id3TimestampPeeked = r2;	 Catch:{ all -> 0x00ac }
        r7 = r13.output;	 Catch:{ all -> 0x00ac }
        r8 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;	 Catch:{ all -> 0x00ac }
        r10 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));	 Catch:{ all -> 0x00ac }
        if (r10 == 0) goto L_0x006a;	 Catch:{ all -> 0x00ac }
    L_0x0063:
        r8 = r13.timestampAdjuster;	 Catch:{ all -> 0x00ac }
        r8 = r8.adjustTsTimestamp(r5);	 Catch:{ all -> 0x00ac }
        goto L_0x006c;	 Catch:{ all -> 0x00ac }
    L_0x006a:
        r8 = r13.startTimeUs;	 Catch:{ all -> 0x00ac }
    L_0x006c:
        r7.setSampleOffsetUs(r8);	 Catch:{ all -> 0x00ac }
    L_0x006f:
        if (r3 == 0) goto L_0x0076;	 Catch:{ all -> 0x00ac }
    L_0x0071:
        r5 = r13.bytesLoaded;	 Catch:{ all -> 0x00ac }
        r4.skipFully(r5);	 Catch:{ all -> 0x00ac }
        if (r1 != 0) goto L_0x0095;
        r5 = r13.loadCanceled;	 Catch:{ all -> 0x0086 }
        if (r5 != 0) goto L_0x0095;	 Catch:{ all -> 0x0086 }
        r5 = r13.extractor;	 Catch:{ all -> 0x0086 }
        r6 = 0;	 Catch:{ all -> 0x0086 }
        r5 = r5.read(r4, r6);	 Catch:{ all -> 0x0086 }
        r1 = r5;
        goto L_0x0077;
    L_0x0086:
        r1 = move-exception;
        r5 = r4.getPosition();	 Catch:{ all -> 0x00ac }
        r2 = r13.dataSpec;	 Catch:{ all -> 0x00ac }
        r7 = r2.absoluteStreamPosition;	 Catch:{ all -> 0x00ac }
        r9 = r5 - r7;	 Catch:{ all -> 0x00ac }
        r2 = (int) r9;	 Catch:{ all -> 0x00ac }
        r13.bytesLoaded = r2;	 Catch:{ all -> 0x00ac }
        throw r1;	 Catch:{ all -> 0x00ac }
        r5 = r4.getPosition();	 Catch:{ all -> 0x00ac }
        r1 = r13.dataSpec;	 Catch:{ all -> 0x00ac }
        r7 = r1.absoluteStreamPosition;	 Catch:{ all -> 0x00ac }
        r9 = r5 - r7;	 Catch:{ all -> 0x00ac }
        r1 = (int) r9;	 Catch:{ all -> 0x00ac }
        r13.bytesLoaded = r1;	 Catch:{ all -> 0x00ac }
        r1 = r13.dataSource;
        org.telegram.messenger.exoplayer2.util.Util.closeQuietly(r1);
        r13.loadCompleted = r2;
        return;
    L_0x00ac:
        r1 = move-exception;
        r2 = r13.dataSource;
        org.telegram.messenger.exoplayer2.util.Util.closeQuietly(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.hls.HlsMediaChunk.loadMedia():void");
    }

    public HlsMediaChunk(HlsExtractorFactory extractorFactory, DataSource dataSource, DataSpec dataSpec, DataSpec initDataSpec, HlsUrl hlsUrl, List<Format> muxedCaptionFormats, int trackSelectionReason, Object trackSelectionData, long startTimeUs, long endTimeUs, int chunkIndex, int discontinuitySequenceNumber, boolean isMasterTimestampSource, TimestampAdjuster timestampAdjuster, HlsMediaChunk previousChunk, DrmInitData drmInitData, byte[] fullSegmentEncryptionKey, byte[] encryptionIv) {
        DataSpec dataSpec2 = initDataSpec;
        HlsUrl hlsUrl2 = hlsUrl;
        int i = discontinuitySequenceNumber;
        HlsMediaChunk hlsMediaChunk = previousChunk;
        super(buildDataSource(dataSource, fullSegmentEncryptionKey, encryptionIv), dataSpec, hlsUrl2.format, trackSelectionReason, trackSelectionData, startTimeUs, endTimeUs, chunkIndex);
        this.discontinuitySequenceNumber = i;
        this.initDataSpec = dataSpec2;
        this.hlsUrl = hlsUrl2;
        this.isMasterTimestampSource = isMasterTimestampSource;
        TimestampAdjuster timestampAdjuster2 = timestampAdjuster;
        this.timestampAdjuster = timestampAdjuster2;
        Extractor previousExtractor = null;
        if (hlsMediaChunk != null) {
            Extractor extractor;
            r11.shouldSpliceIn = hlsMediaChunk.hlsUrl != hlsUrl2;
            if (hlsMediaChunk.discontinuitySequenceNumber == i) {
                if (!r11.shouldSpliceIn) {
                    extractor = hlsMediaChunk.extractor;
                    previousExtractor = extractor;
                }
            }
            extractor = null;
            previousExtractor = extractor;
        } else {
            r11.shouldSpliceIn = false;
        }
        Extractor previousExtractor2 = previousExtractor;
        Extractor previousExtractor3 = previousExtractor2;
        boolean z = false;
        Pair<Extractor, Boolean> extractorData = extractorFactory.createExtractor(previousExtractor2, dataSpec.uri, r11.trackFormat, muxedCaptionFormats, drmInitData, timestampAdjuster2);
        r11.extractor = (Extractor) extractorData.first;
        r11.isPackedAudioExtractor = ((Boolean) extractorData.second).booleanValue();
        r11.reusingExtractor = r11.extractor == previousExtractor3 ? true : z;
        boolean z2 = (!r11.reusingExtractor || dataSpec2 == null) ? z : true;
        r11.initLoadCompleted = z2;
        if (!r11.isPackedAudioExtractor) {
            r11.id3Decoder = null;
            r11.id3Data = null;
        } else if (hlsMediaChunk == null || hlsMediaChunk.id3Data == null) {
            r11.id3Decoder = new Id3Decoder();
            r11.id3Data = new ParsableByteArray(10);
        } else {
            r11.id3Decoder = hlsMediaChunk.id3Decoder;
            r11.id3Data = hlsMediaChunk.id3Data;
        }
        r11.initDataSource = dataSource;
        r11.uid = uidSource.getAndIncrement();
    }

    public void init(HlsSampleStreamWrapper output) {
        this.output = output;
        output.init(this.uid, this.shouldSpliceIn, this.reusingExtractor);
        if (!this.reusingExtractor) {
            this.extractor.init(output);
        }
    }

    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    public long bytesLoaded() {
        return (long) this.bytesLoaded;
    }

    public void cancelLoad() {
        this.loadCanceled = true;
    }

    public boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    public void load() throws IOException, InterruptedException {
        maybeLoadInitData();
        if (!this.loadCanceled) {
            loadMedia();
        }
    }

    private void maybeLoadInitData() throws IOException, InterruptedException {
        if (!this.initLoadCompleted) {
            if (this.initDataSpec != null) {
                DataSpec initSegmentDataSpec = this.initDataSpec.subrange((long) this.initSegmentBytesLoaded);
                DefaultExtractorInput input;
                try {
                    input = new DefaultExtractorInput(this.initDataSource, initSegmentDataSpec.absoluteStreamPosition, this.initDataSource.open(initSegmentDataSpec));
                    int result = 0;
                    while (result == 0) {
                        if (this.loadCanceled) {
                            break;
                        }
                        result = this.extractor.read(input, null);
                    }
                    this.initSegmentBytesLoaded = (int) (input.getPosition() - this.initDataSpec.absoluteStreamPosition);
                    Util.closeQuietly(this.dataSource);
                    this.initLoadCompleted = true;
                } catch (Throwable th) {
                    Util.closeQuietly(this.dataSource);
                }
            }
        }
    }

    private long peekId3PrivTimestamp(ExtractorInput input) throws IOException, InterruptedException {
        ExtractorInput extractorInput = input;
        input.resetPeekPosition();
        if (!extractorInput.peekFully(this.id3Data.data, 0, 10, true)) {
            return C.TIME_UNSET;
        }
        r0.id3Data.reset(10);
        if (r0.id3Data.readUnsignedInt24() != Id3Decoder.ID3_TAG) {
            return C.TIME_UNSET;
        }
        r0.id3Data.skipBytes(3);
        int id3Size = r0.id3Data.readSynchSafeInt();
        int requiredCapacity = id3Size + 10;
        if (requiredCapacity > r0.id3Data.capacity()) {
            byte[] data = r0.id3Data.data;
            r0.id3Data.reset(requiredCapacity);
            System.arraycopy(data, 0, r0.id3Data.data, 0, 10);
        }
        if (!extractorInput.peekFully(r0.id3Data.data, 10, id3Size, true)) {
            return C.TIME_UNSET;
        }
        Metadata metadata = r0.id3Decoder.decode(r0.id3Data.data, id3Size);
        if (metadata == null) {
            return C.TIME_UNSET;
        }
        int metadataLength = metadata.length();
        for (int i = 0; i < metadataLength; i++) {
            Entry frame = metadata.get(i);
            if (frame instanceof PrivFrame) {
                PrivFrame privFrame = (PrivFrame) frame;
                if (PRIV_TIMESTAMP_FRAME_OWNER.equals(privFrame.owner)) {
                    System.arraycopy(privFrame.privateData, 0, r0.id3Data.data, 0, 8);
                    r0.id3Data.reset(8);
                    return r0.id3Data.readLong() & 8589934591L;
                }
            }
        }
        return C.TIME_UNSET;
    }

    private static DataSource buildDataSource(DataSource dataSource, byte[] fullSegmentEncryptionKey, byte[] encryptionIv) {
        if (fullSegmentEncryptionKey != null) {
            return new Aes128DataSource(dataSource, fullSegmentEncryptionKey, encryptionIv);
        }
        return dataSource;
    }
}
