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
import org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader;
import org.telegram.messenger.exoplayer2.extractor.PositionHolder;
import org.telegram.messenger.exoplayer2.extractor.SeekMap;
import org.telegram.messenger.exoplayer2.extractor.TrackOutput;
import org.telegram.messenger.exoplayer2.metadata.Metadata;
import org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder;
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

    private boolean synchronize(org.telegram.messenger.exoplayer2.extractor.ExtractorInput r1, boolean r2) throws java.io.IOException, java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp3.Mp3Extractor.synchronize(org.telegram.messenger.exoplayer2.extractor.ExtractorInput, boolean):boolean
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
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = 0;
        if (r14 == 0) goto L_0x0009;
    L_0x0006:
        r4 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        goto L_0x000b;
    L_0x0009:
        r4 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
    L_0x000b:
        r13.resetPeekPosition();
        r5 = r13.getPosition();
        r7 = 0;
        r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r9 != 0) goto L_0x0025;
    L_0x0018:
        r12.peekId3Data(r13);
        r5 = r13.getPeekPosition();
        r2 = (int) r5;
        if (r14 != 0) goto L_0x0025;
    L_0x0022:
        r13.skipFully(r2);
    L_0x0025:
        r5 = r12.scratch;
        r5 = r5.data;
        r6 = 0;
        r7 = 1;
        if (r0 <= 0) goto L_0x002f;
    L_0x002d:
        r8 = r7;
        goto L_0x0030;
    L_0x002f:
        r8 = r6;
    L_0x0030:
        r9 = 4;
        r5 = r13.peekFully(r5, r6, r9, r8);
        if (r5 != 0) goto L_0x0038;
    L_0x0037:
        goto L_0x0084;
    L_0x0038:
        r5 = r12.scratch;
        r5.setPosition(r6);
        r5 = r12.scratch;
        r5 = r5.readInt();
        if (r1 == 0) goto L_0x004c;
    L_0x0045:
        r10 = (long) r1;
        r8 = headersMatch(r5, r10);
        if (r8 == 0) goto L_0x0054;
    L_0x004c:
        r8 = org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader.getFrameSize(r5);
        r10 = r8;
        r11 = -1;
        if (r8 != r11) goto L_0x0075;
    L_0x0054:
        r8 = r3 + 1;
        if (r3 != r4) goto L_0x0063;
        if (r14 != 0) goto L_0x0062;
        r3 = new org.telegram.messenger.exoplayer2.ParserException;
        r6 = "Searched too many bytes.";
        r3.<init>(r6);
        throw r3;
        return r6;
        r0 = 0;
        r1 = 0;
        if (r14 == 0) goto L_0x0070;
        r13.resetPeekPosition();
        r3 = r2 + r8;
        r13.advancePeekPosition(r3);
        goto L_0x0073;
        r13.skipFully(r7);
        r3 = r8;
        goto L_0x0097;
        r0 = r0 + 1;
        if (r0 != r7) goto L_0x0081;
        r6 = r12.synchronizedHeader;
        org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader.populateHeader(r5, r6);
        r1 = r5;
        goto L_0x0092;
        if (r0 != r9) goto L_0x0092;
    L_0x0084:
        if (r14 == 0) goto L_0x008c;
        r5 = r2 + r3;
        r13.skipFully(r5);
        goto L_0x008f;
        r13.resetPeekPosition();
        r12.synchronizedHeaderData = r1;
        return r7;
        r6 = r10 + -4;
        r13.advancePeekPosition(r6);
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp3.Mp3Extractor.synchronize(org.telegram.messenger.exoplayer2.extractor.ExtractorInput, boolean):boolean");
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
        ExtractorInput extractorInput = input;
        if (r1.seeker == null) {
            r1.seeker = maybeReadSeekFrame(input);
            if (r1.seeker == null || !(r1.seeker.isSeekable() || (r1.flags & 1) == 0)) {
                r1.seeker = getConstantBitrateSeeker(input);
            }
            r1.extractorOutput.seekMap(r1.seeker);
            r1.trackOutput.format(Format.createAudioSampleFormat(null, r1.synchronizedHeader.mimeType, null, -1, 4096, r1.synchronizedHeader.channels, r1.synchronizedHeader.sampleRate, -1, r1.gaplessInfoHolder.encoderDelay, r1.gaplessInfoHolder.encoderPadding, null, null, 0, null, (r1.flags & 2) != 0 ? null : r1.metadata));
        }
        return readSample(input);
    }

    private int readSample(ExtractorInput extractorInput) throws IOException, InterruptedException {
        int sampleHeaderData;
        ExtractorInput extractorInput2 = extractorInput;
        if (this.sampleBytesRemaining == 0) {
            extractorInput.resetPeekPosition();
            if (!extractorInput2.peekFully(r0.scratch.data, 0, 4, true)) {
                return -1;
            }
            r0.scratch.setPosition(0);
            sampleHeaderData = r0.scratch.readInt();
            if (headersMatch(sampleHeaderData, (long) r0.synchronizedHeaderData)) {
                if (MpegAudioHeader.getFrameSize(sampleHeaderData) != -1) {
                    MpegAudioHeader.populateHeader(sampleHeaderData, r0.synchronizedHeader);
                    if (r0.basisTimeUs == C.TIME_UNSET) {
                        r0.basisTimeUs = r0.seeker.getTimeUs(extractorInput.getPosition());
                        if (r0.forcedFirstSampleTimestampUs != C.TIME_UNSET) {
                            r0.basisTimeUs += r0.forcedFirstSampleTimestampUs - r0.seeker.getTimeUs(0);
                        }
                    }
                    r0.sampleBytesRemaining = r0.synchronizedHeader.frameSize;
                }
            }
            extractorInput2.skipFully(1);
            r0.synchronizedHeaderData = 0;
            return 0;
        }
        sampleHeaderData = r0.trackOutput.sampleData(extractorInput2, r0.sampleBytesRemaining, true);
        if (sampleHeaderData == -1) {
            return -1;
        }
        r0.sampleBytesRemaining -= sampleHeaderData;
        if (r0.sampleBytesRemaining > 0) {
            return 0;
        }
        r0.trackOutput.sampleMetadata(r0.basisTimeUs + ((r0.samplesRead * C.MICROS_PER_SECOND) / ((long) r0.synchronizedHeader.sampleRate)), 1, r0.synchronizedHeader.frameSize, 0, null);
        r0.samplesRead += (long) r0.synchronizedHeader.samplesPerFrame;
        r0.sampleBytesRemaining = 0;
        return 0;
    }

    private void peekId3Data(ExtractorInput input) throws IOException, InterruptedException {
        int peekedId3Bytes = 0;
        while (true) {
            input.peekFully(this.scratch.data, 0, 10);
            this.scratch.setPosition(0);
            if (this.scratch.readUnsignedInt24() != Id3Decoder.ID3_TAG) {
                input.resetPeekPosition();
                input.advancePeekPosition(peekedId3Bytes);
                return;
            }
            this.scratch.skipBytes(3);
            int framesLength = this.scratch.readSynchSafeInt();
            int tagLength = 10 + framesLength;
            if (this.metadata == null) {
                byte[] id3Data = new byte[tagLength];
                System.arraycopy(this.scratch.data, 0, id3Data, 0, 10);
                input.peekFully(id3Data, 10, framesLength);
                this.metadata = new Id3Decoder((this.flags & 2) != 0 ? GaplessInfoHolder.GAPLESS_INFO_ID3_FRAME_PREDICATE : null).decode(id3Data, tagLength);
                if (this.metadata != null) {
                    this.gaplessInfoHolder.setFromMetadata(this.metadata);
                }
            } else {
                input.advancePeekPosition(framesLength);
            }
            peekedId3Bytes += tagLength;
        }
    }

    private Seeker maybeReadSeekFrame(ExtractorInput input) throws IOException, InterruptedException {
        int xingBase;
        int seekHeader;
        Seeker seeker;
        ParsableByteArray frame = new ParsableByteArray(this.synchronizedHeader.frameSize);
        input.peekFully(frame.data, 0, this.synchronizedHeader.frameSize);
        int i = 21;
        if ((this.synchronizedHeader.version & 1) != 0) {
            if (this.synchronizedHeader.channels != 1) {
                i = 36;
                xingBase = i;
                seekHeader = getSeekFrameHeader(frame, xingBase);
                if (seekHeader != SEEK_HEADER_XING) {
                    if (seekHeader == SEEK_HEADER_INFO) {
                        if (seekHeader == SEEK_HEADER_VBRI) {
                            seeker = VbriSeeker.create(input.getLength(), input.getPosition(), this.synchronizedHeader, frame);
                            input.skipFully(this.synchronizedHeader.frameSize);
                        } else {
                            seeker = null;
                            input.resetPeekPosition();
                        }
                        return seeker;
                    }
                }
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
                return seeker;
            }
        } else if (this.synchronizedHeader.channels == 1) {
            i = 13;
            xingBase = i;
            seekHeader = getSeekFrameHeader(frame, xingBase);
            if (seekHeader != SEEK_HEADER_XING) {
                if (seekHeader == SEEK_HEADER_INFO) {
                    if (seekHeader == SEEK_HEADER_VBRI) {
                        seeker = null;
                        input.resetPeekPosition();
                    } else {
                        seeker = VbriSeeker.create(input.getLength(), input.getPosition(), this.synchronizedHeader, frame);
                        input.skipFully(this.synchronizedHeader.frameSize);
                    }
                    return seeker;
                }
            }
            seeker = XingSeeker.create(input.getLength(), input.getPosition(), this.synchronizedHeader, frame);
            input.resetPeekPosition();
            input.advancePeekPosition(xingBase + 141);
            input.peekFully(this.scratch.data, 0, 3);
            this.scratch.setPosition(0);
            this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
            input.skipFully(this.synchronizedHeader.frameSize);
            return getConstantBitrateSeeker(input);
        }
        xingBase = i;
        seekHeader = getSeekFrameHeader(frame, xingBase);
        if (seekHeader != SEEK_HEADER_XING) {
            if (seekHeader == SEEK_HEADER_INFO) {
                if (seekHeader == SEEK_HEADER_VBRI) {
                    seeker = VbriSeeker.create(input.getLength(), input.getPosition(), this.synchronizedHeader, frame);
                    input.skipFully(this.synchronizedHeader.frameSize);
                } else {
                    seeker = null;
                    input.resetPeekPosition();
                }
                return seeker;
            }
        }
        seeker = XingSeeker.create(input.getLength(), input.getPosition(), this.synchronizedHeader, frame);
        input.resetPeekPosition();
        input.advancePeekPosition(xingBase + 141);
        input.peekFully(this.scratch.data, 0, 3);
        this.scratch.setPosition(0);
        this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
        input.skipFully(this.synchronizedHeader.frameSize);
        return getConstantBitrateSeeker(input);
    }

    private Seeker getConstantBitrateSeeker(ExtractorInput input) throws IOException, InterruptedException {
        input.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
        return new ConstantBitrateSeeker(input.getLength(), input.getPosition(), this.synchronizedHeader);
    }

    private static boolean headersMatch(int headerA, long headerB) {
        return ((long) (MPEG_AUDIO_HEADER_MASK & headerA)) == (headerB & -128000);
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
