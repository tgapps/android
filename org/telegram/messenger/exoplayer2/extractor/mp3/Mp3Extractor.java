package org.telegram.messenger.exoplayer2.extractor.mp3;

import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.extractor.Extractor;
import org.telegram.messenger.exoplayer2.extractor.ExtractorInput;
import org.telegram.messenger.exoplayer2.extractor.ExtractorOutput;
import org.telegram.messenger.exoplayer2.extractor.ExtractorsFactory;
import org.telegram.messenger.exoplayer2.extractor.GaplessInfoHolder;
import org.telegram.messenger.exoplayer2.extractor.Id3Peeker;
import org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader;
import org.telegram.messenger.exoplayer2.extractor.PositionHolder;
import org.telegram.messenger.exoplayer2.extractor.SeekMap;
import org.telegram.messenger.exoplayer2.extractor.TrackOutput;
import org.telegram.messenger.exoplayer2.metadata.Metadata;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.Util;

public final class Mp3Extractor implements Extractor {
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() {
        public Extractor[] createExtractors() {
            return new Extractor[]{new Mp3Extractor()};
        }
    };
    public static final int FLAG_DISABLE_ID3_METADATA = 2;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING = 1;
    private static final int MAX_SNIFF_BYTES = 16384;
    private static final int MAX_SYNC_BYTES = 131072;
    private static final int MPEG_AUDIO_HEADER_MASK = -128000;
    private static final int SCRATCH_LENGTH = 10;
    private static final int SEEK_HEADER_INFO = Util.getIntegerCodeForString("Info");
    private static final int SEEK_HEADER_UNSET = 0;
    private static final int SEEK_HEADER_VBRI = Util.getIntegerCodeForString("VBRI");
    private static final int SEEK_HEADER_XING = Util.getIntegerCodeForString("Xing");
    private long basisTimeUs;
    private ExtractorOutput extractorOutput;
    private final int flags;
    private final long forcedFirstSampleTimestampUs;
    private final GaplessInfoHolder gaplessInfoHolder;
    private final Id3Peeker id3Peeker;
    private Metadata metadata;
    private int sampleBytesRemaining;
    private long samplesRead;
    private final ParsableByteArray scratch;
    private Seeker seeker;
    private final MpegAudioHeader synchronizedHeader;
    private int synchronizedHeaderData;
    private TrackOutput trackOutput;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    interface Seeker extends SeekMap {
        long getTimeUs(long j);
    }

    public Mp3Extractor() {
        this(0);
    }

    public Mp3Extractor(int flags) {
        this(flags, C.TIME_UNSET);
    }

    public Mp3Extractor(int flags, long forcedFirstSampleTimestampUs) {
        this.flags = flags;
        this.forcedFirstSampleTimestampUs = forcedFirstSampleTimestampUs;
        this.scratch = new ParsableByteArray(10);
        this.synchronizedHeader = new MpegAudioHeader();
        this.gaplessInfoHolder = new GaplessInfoHolder();
        this.basisTimeUs = C.TIME_UNSET;
        this.id3Peeker = new Id3Peeker();
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        return synchronize(input, true);
    }

    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
        this.trackOutput = this.extractorOutput.track(0, 1);
        this.extractorOutput.endTracks();
    }

    public void seek(long position, long timeUs) {
        this.synchronizedHeaderData = 0;
        this.basisTimeUs = C.TIME_UNSET;
        this.samplesRead = 0;
        this.sampleBytesRemaining = 0;
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        if (this.synchronizedHeaderData == 0) {
            try {
                synchronize(input, false);
            } catch (EOFException e) {
                return -1;
            }
        }
        if (this.seeker == null) {
            this.seeker = maybeReadSeekFrame(input);
            if (this.seeker == null || !(this.seeker.isSeekable() || (this.flags & 1) == 0)) {
                this.seeker = getConstantBitrateSeeker(input);
            }
            this.extractorOutput.seekMap(this.seeker);
            this.trackOutput.format(Format.createAudioSampleFormat(null, this.synchronizedHeader.mimeType, null, -1, 4096, this.synchronizedHeader.channels, this.synchronizedHeader.sampleRate, -1, this.gaplessInfoHolder.encoderDelay, this.gaplessInfoHolder.encoderPadding, null, null, 0, null, (this.flags & 2) != 0 ? null : this.metadata));
        }
        return readSample(input);
    }

    private int readSample(ExtractorInput extractorInput) throws IOException, InterruptedException {
        if (this.sampleBytesRemaining == 0) {
            extractorInput.resetPeekPosition();
            if (!extractorInput.peekFully(this.scratch.data, 0, 4, true)) {
                return -1;
            }
            this.scratch.setPosition(0);
            int sampleHeaderData = this.scratch.readInt();
            if (!headersMatch(sampleHeaderData, (long) this.synchronizedHeaderData) || MpegAudioHeader.getFrameSize(sampleHeaderData) == -1) {
                extractorInput.skipFully(1);
                this.synchronizedHeaderData = 0;
                return 0;
            }
            MpegAudioHeader.populateHeader(sampleHeaderData, this.synchronizedHeader);
            if (this.basisTimeUs == C.TIME_UNSET) {
                this.basisTimeUs = this.seeker.getTimeUs(extractorInput.getPosition());
                if (this.forcedFirstSampleTimestampUs != C.TIME_UNSET) {
                    this.basisTimeUs += this.forcedFirstSampleTimestampUs - this.seeker.getTimeUs(0);
                }
            }
            this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
        }
        int bytesAppended = this.trackOutput.sampleData(extractorInput, this.sampleBytesRemaining, true);
        if (bytesAppended == -1) {
            return -1;
        }
        this.sampleBytesRemaining -= bytesAppended;
        if (this.sampleBytesRemaining > 0) {
            return 0;
        }
        this.trackOutput.sampleMetadata(this.basisTimeUs + ((this.samplesRead * 1000000) / ((long) this.synchronizedHeader.sampleRate)), 1, this.synchronizedHeader.frameSize, 0, null);
        this.samplesRead += (long) this.synchronizedHeader.samplesPerFrame;
        this.sampleBytesRemaining = 0;
        return 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean synchronize(org.telegram.messenger.exoplayer2.extractor.ExtractorInput r15, boolean r16) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r14 = this;
        r9 = 0;
        r0 = 0;
        r5 = 0;
        r7 = 0;
        if (r16 == 0) goto L_0x0057;
    L_0x0006:
        r6 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
    L_0x0008:
        r15.resetPeekPosition();
        r10 = r15.getPosition();
        r12 = 0;
        r10 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r10 != 0) goto L_0x003d;
    L_0x0015:
        r10 = r14.flags;
        r10 = r10 & 2;
        if (r10 == 0) goto L_0x005a;
    L_0x001b:
        r4 = 1;
    L_0x001c:
        if (r4 == 0) goto L_0x005c;
    L_0x001e:
        r3 = org.telegram.messenger.exoplayer2.extractor.GaplessInfoHolder.GAPLESS_INFO_ID3_FRAME_PREDICATE;
    L_0x0020:
        r10 = r14.id3Peeker;
        r10 = r10.peekId3Data(r15, r3);
        r14.metadata = r10;
        r10 = r14.metadata;
        if (r10 == 0) goto L_0x0033;
    L_0x002c:
        r10 = r14.gaplessInfoHolder;
        r11 = r14.metadata;
        r10.setFromMetadata(r11);
    L_0x0033:
        r10 = r15.getPeekPosition();
        r5 = (int) r10;
        if (r16 != 0) goto L_0x003d;
    L_0x003a:
        r15.skipFully(r5);
    L_0x003d:
        r10 = r14.scratch;
        r11 = r10.data;
        r12 = 0;
        r13 = 4;
        if (r9 <= 0) goto L_0x005e;
    L_0x0045:
        r10 = 1;
    L_0x0046:
        r10 = r15.peekFully(r11, r12, r13, r10);
        if (r10 != 0) goto L_0x0060;
    L_0x004c:
        if (r16 == 0) goto L_0x00b7;
    L_0x004e:
        r10 = r5 + r7;
        r15.skipFully(r10);
    L_0x0053:
        r14.synchronizedHeaderData = r0;
        r10 = 1;
    L_0x0056:
        return r10;
    L_0x0057:
        r6 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        goto L_0x0008;
    L_0x005a:
        r4 = 0;
        goto L_0x001c;
    L_0x005c:
        r3 = 0;
        goto L_0x0020;
    L_0x005e:
        r10 = 0;
        goto L_0x0046;
    L_0x0060:
        r10 = r14.scratch;
        r11 = 0;
        r10.setPosition(r11);
        r10 = r14.scratch;
        r2 = r10.readInt();
        if (r0 == 0) goto L_0x0075;
    L_0x006e:
        r10 = (long) r0;
        r10 = headersMatch(r2, r10);
        if (r10 == 0) goto L_0x007c;
    L_0x0075:
        r1 = org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader.getFrameSize(r2);
        r10 = -1;
        if (r1 != r10) goto L_0x00a2;
    L_0x007c:
        r8 = r7 + 1;
        if (r7 != r6) goto L_0x008e;
    L_0x0080:
        if (r16 != 0) goto L_0x008b;
    L_0x0082:
        r10 = new org.telegram.messenger.exoplayer2.ParserException;
        r11 = "Searched too many bytes.";
        r10.<init>(r11);
        throw r10;
    L_0x008b:
        r10 = 0;
        r7 = r8;
        goto L_0x0056;
    L_0x008e:
        r9 = 0;
        r0 = 0;
        if (r16 == 0) goto L_0x009c;
    L_0x0092:
        r15.resetPeekPosition();
        r10 = r5 + r8;
        r15.advancePeekPosition(r10);
        r7 = r8;
        goto L_0x003d;
    L_0x009c:
        r10 = 1;
        r15.skipFully(r10);
        r7 = r8;
        goto L_0x003d;
    L_0x00a2:
        r9 = r9 + 1;
        r10 = 1;
        if (r9 != r10) goto L_0x00b3;
    L_0x00a7:
        r10 = r14.synchronizedHeader;
        org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader.populateHeader(r2, r10);
        r0 = r2;
    L_0x00ad:
        r10 = r1 + -4;
        r15.advancePeekPosition(r10);
        goto L_0x003d;
    L_0x00b3:
        r10 = 4;
        if (r9 != r10) goto L_0x00ad;
    L_0x00b6:
        goto L_0x004c;
    L_0x00b7:
        r15.resetPeekPosition();
        goto L_0x0053;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp3.Mp3Extractor.synchronize(org.telegram.messenger.exoplayer2.extractor.ExtractorInput, boolean):boolean");
    }

    private Seeker maybeReadSeekFrame(ExtractorInput input) throws IOException, InterruptedException {
        Seeker seeker;
        int xingBase = 21;
        ParsableByteArray frame = new ParsableByteArray(this.synchronizedHeader.frameSize);
        input.peekFully(frame.data, 0, this.synchronizedHeader.frameSize);
        if ((this.synchronizedHeader.version & 1) != 0) {
            if (this.synchronizedHeader.channels != 1) {
                xingBase = 36;
            }
        } else if (this.synchronizedHeader.channels == 1) {
            xingBase = 13;
        }
        int seekHeader = getSeekFrameHeader(frame, xingBase);
        if (seekHeader == SEEK_HEADER_XING || seekHeader == SEEK_HEADER_INFO) {
            seeker = XingSeeker.create(input.getLength(), input.getPosition(), this.synchronizedHeader, frame);
            if (!(seeker == null || this.gaplessInfoHolder.hasGaplessInfo())) {
                input.resetPeekPosition();
                input.advancePeekPosition(xingBase + 141);
                input.peekFully(this.scratch.data, 0, 3);
                this.scratch.setPosition(0);
                this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
            }
            input.skipFully(this.synchronizedHeader.frameSize);
            if (!(seeker == null || seeker.isSeekable() || seekHeader != SEEK_HEADER_INFO)) {
                return getConstantBitrateSeeker(input);
            }
        } else if (seekHeader == SEEK_HEADER_VBRI) {
            seeker = VbriSeeker.create(input.getLength(), input.getPosition(), this.synchronizedHeader, frame);
            input.skipFully(this.synchronizedHeader.frameSize);
        } else {
            seeker = null;
            input.resetPeekPosition();
        }
        Seeker seeker2 = seeker;
        return seeker;
    }

    private Seeker getConstantBitrateSeeker(ExtractorInput input) throws IOException, InterruptedException {
        input.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
        return new ConstantBitrateSeeker(input.getLength(), input.getPosition(), this.synchronizedHeader);
    }

    private static boolean headersMatch(int headerA, long headerB) {
        return ((long) (MPEG_AUDIO_HEADER_MASK & headerA)) == (-128000 & headerB);
    }

    private static int getSeekFrameHeader(ParsableByteArray frame, int xingBase) {
        if (frame.limit() >= xingBase + 4) {
            frame.setPosition(xingBase);
            int headerData = frame.readInt();
            if (headerData == SEEK_HEADER_XING || headerData == SEEK_HEADER_INFO) {
                return headerData;
            }
        }
        if (frame.limit() >= 40) {
            frame.setPosition(36);
            if (frame.readInt() == SEEK_HEADER_VBRI) {
                return SEEK_HEADER_VBRI;
            }
        }
        return 0;
    }
}
