package org.telegram.messenger.exoplayer2.extractor.ts;

import android.util.Pair;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.extractor.ExtractorOutput;
import org.telegram.messenger.exoplayer2.extractor.TrackOutput;
import org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader.TrackIdGenerator;
import org.telegram.messenger.exoplayer2.util.CodecSpecificDataUtil;
import org.telegram.messenger.exoplayer2.util.ParsableBitArray;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;

public final class LatmReader implements ElementaryStreamReader {
    private static final int INITIAL_BUFFER_SIZE = 1024;
    private static final int STATE_FINDING_SYNC_1 = 0;
    private static final int STATE_FINDING_SYNC_2 = 1;
    private static final int STATE_READING_HEADER = 2;
    private static final int STATE_READING_SAMPLE = 3;
    private static final int SYNC_BYTE_FIRST = 86;
    private static final int SYNC_BYTE_SECOND = 224;
    private int audioMuxVersionA;
    private int bytesRead;
    private int channelCount;
    private Format format;
    private String formatId;
    private int frameLengthType;
    private final String language;
    private int numSubframes;
    private long otherDataLenBits;
    private boolean otherDataPresent;
    private TrackOutput output;
    private final ParsableBitArray sampleBitArray = new ParsableBitArray(this.sampleDataBuffer.data);
    private final ParsableByteArray sampleDataBuffer = new ParsableByteArray(1024);
    private long sampleDurationUs;
    private int sampleRateHz;
    private int sampleSize;
    private int secondHeaderByte;
    private int state;
    private boolean streamMuxRead;
    private long timeUs;

    private void parseAudioMuxElement(org.telegram.messenger.exoplayer2.util.ParsableBitArray r1) throws org.telegram.messenger.exoplayer2.ParserException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.ts.LatmReader.parseAudioMuxElement(org.telegram.messenger.exoplayer2.util.ParsableBitArray):void
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
        r0 = r5.readBit();
        if (r0 != 0) goto L_0x000d;
    L_0x0006:
        r1 = 1;
        r4.streamMuxRead = r1;
        r4.parseStreamMuxConfig(r5);
        goto L_0x0012;
    L_0x000d:
        r1 = r4.streamMuxRead;
        if (r1 != 0) goto L_0x0012;
    L_0x0011:
        return;
    L_0x0012:
        r1 = r4.audioMuxVersionA;
        if (r1 != 0) goto L_0x0033;
    L_0x0016:
        r1 = r4.numSubframes;
        if (r1 == 0) goto L_0x0020;
    L_0x001a:
        r1 = new org.telegram.messenger.exoplayer2.ParserException;
        r1.<init>();
        throw r1;
    L_0x0020:
        r1 = r4.parsePayloadLengthInfo(r5);
        r4.parsePayloadMux(r5, r1);
        r2 = r4.otherDataPresent;
        if (r2 == 0) goto L_0x0031;
    L_0x002b:
        r2 = r4.otherDataLenBits;
        r2 = (int) r2;
        r5.skipBits(r2);
        return;
    L_0x0033:
        r1 = new org.telegram.messenger.exoplayer2.ParserException;
        r1.<init>();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.ts.LatmReader.parseAudioMuxElement(org.telegram.messenger.exoplayer2.util.ParsableBitArray):void");
    }

    private void parseStreamMuxConfig(org.telegram.messenger.exoplayer2.util.ParsableBitArray r1) throws org.telegram.messenger.exoplayer2.ParserException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.ts.LatmReader.parseStreamMuxConfig(org.telegram.messenger.exoplayer2.util.ParsableBitArray):void
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
        r0 = r23;
        r1 = r24;
        r2 = 1;
        r3 = r1.readBits(r2);
        r4 = 0;
        if (r3 != r2) goto L_0x0011;
    L_0x000c:
        r5 = r1.readBits(r2);
        goto L_0x0012;
    L_0x0011:
        r5 = r4;
    L_0x0012:
        r0.audioMuxVersionA = r5;
        r5 = r0.audioMuxVersionA;
        if (r5 != 0) goto L_0x00df;
    L_0x0018:
        if (r3 != r2) goto L_0x001d;
    L_0x001a:
        latmGetValue(r24);
    L_0x001d:
        r5 = r24.readBit();
        if (r5 != 0) goto L_0x0029;
    L_0x0023:
        r2 = new org.telegram.messenger.exoplayer2.ParserException;
        r2.<init>();
        throw r2;
    L_0x0029:
        r5 = 6;
        r5 = r1.readBits(r5);
        r0.numSubframes = r5;
        r5 = 4;
        r5 = r1.readBits(r5);
        r6 = 3;
        r6 = r1.readBits(r6);
        if (r5 != 0) goto L_0x00d9;
    L_0x003c:
        if (r6 == 0) goto L_0x0040;
    L_0x003e:
        goto L_0x00d9;
    L_0x0040:
        r7 = 8;
        if (r3 != 0) goto L_0x008f;
    L_0x0044:
        r8 = r24.getPosition();
        r9 = r23.parseAudioSpecificConfig(r24);
        r1.setPosition(r8);
        r10 = r9 + 7;
        r10 = r10 / r7;
        r10 = new byte[r10];
        r1.readBits(r10, r4, r9);
        r11 = r0.formatId;
        r12 = "audio/mp4a-latm";
        r13 = 0;
        r14 = -1;
        r15 = -1;
        r4 = r0.channelCount;
        r7 = r0.sampleRateHz;
        r18 = java.util.Collections.singletonList(r10);
        r19 = 0;
        r20 = 0;
        r2 = r0.language;
        r16 = r4;
        r17 = r7;
        r21 = r2;
        r2 = org.telegram.messenger.exoplayer2.Format.createAudioSampleFormat(r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21);
        r4 = r0.format;
        r4 = r2.equals(r4);
        if (r4 != 0) goto L_0x008e;
    L_0x007e:
        r0.format = r2;
        r11 = 1024000000; // 0x3d090000 float:0.033447266 double:5.059232213E-315;
        r4 = r2.sampleRate;
        r13 = (long) r4;
        r11 = r11 / r13;
        r0.sampleDurationUs = r11;
        r4 = r0.output;
        r4.format(r2);
    L_0x008e:
        goto L_0x009d;
    L_0x008f:
        r7 = latmGetValue(r24);
        r2 = (int) r7;
        r4 = r23.parseAudioSpecificConfig(r24);
        r7 = r2 - r4;
        r1.skipBits(r7);
    L_0x009d:
        r23.parseFrameLength(r24);
        r2 = r24.readBit();
        r0.otherDataPresent = r2;
        r7 = 0;
        r0.otherDataLenBits = r7;
        r2 = r0.otherDataPresent;
        if (r2 == 0) goto L_0x00cc;
    L_0x00ae:
        r2 = 1;
        if (r3 != r2) goto L_0x00b8;
    L_0x00b1:
        r7 = latmGetValue(r24);
        r0.otherDataLenBits = r7;
        goto L_0x00cc;
    L_0x00b8:
        r2 = r24.readBit();
        r7 = r0.otherDataLenBits;
        r4 = 8;
        r7 = r7 << r4;
        r9 = r1.readBits(r4);
        r9 = (long) r9;
        r11 = r7 + r9;
        r0.otherDataLenBits = r11;
        if (r2 != 0) goto L_0x00b8;
    L_0x00cc:
        r2 = r24.readBit();
        if (r2 == 0) goto L_0x00d7;
    L_0x00d2:
        r4 = 8;
        r1.skipBits(r4);
        return;
    L_0x00d9:
        r2 = new org.telegram.messenger.exoplayer2.ParserException;
        r2.<init>();
        throw r2;
    L_0x00df:
        r2 = new org.telegram.messenger.exoplayer2.ParserException;
        r2.<init>();
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.ts.LatmReader.parseStreamMuxConfig(org.telegram.messenger.exoplayer2.util.ParsableBitArray):void");
    }

    public LatmReader(String language) {
        this.language = language;
    }

    public void seek() {
        this.state = 0;
        this.streamMuxRead = false;
    }

    public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator idGenerator) {
        idGenerator.generateNewId();
        this.output = extractorOutput.track(idGenerator.getTrackId(), 1);
        this.formatId = idGenerator.getFormatId();
    }

    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        this.timeUs = pesTimeUs;
    }

    public void consume(ParsableByteArray data) throws ParserException {
        while (data.bytesLeft() > 0) {
            int secondByte;
            switch (this.state) {
                case 0:
                    if (data.readUnsignedByte() != SYNC_BYTE_FIRST) {
                        break;
                    }
                    this.state = 1;
                    break;
                case 1:
                    secondByte = data.readUnsignedByte();
                    if ((secondByte & 224) != 224) {
                        if (secondByte == SYNC_BYTE_FIRST) {
                            break;
                        }
                        this.state = 0;
                        break;
                    }
                    this.secondHeaderByte = secondByte;
                    this.state = 2;
                    break;
                case 2:
                    this.sampleSize = ((this.secondHeaderByte & -225) << 8) | data.readUnsignedByte();
                    if (this.sampleSize > this.sampleDataBuffer.data.length) {
                        resetBufferForSize(this.sampleSize);
                    }
                    this.bytesRead = 0;
                    this.state = 3;
                    break;
                case 3:
                    secondByte = Math.min(data.bytesLeft(), this.sampleSize - this.bytesRead);
                    data.readBytes(this.sampleBitArray.data, this.bytesRead, secondByte);
                    this.bytesRead += secondByte;
                    if (this.bytesRead != this.sampleSize) {
                        break;
                    }
                    this.sampleBitArray.setPosition(0);
                    parseAudioMuxElement(this.sampleBitArray);
                    this.state = 0;
                    break;
                default:
                    break;
            }
        }
    }

    public void packetFinished() {
    }

    private void parseFrameLength(ParsableBitArray data) {
        this.frameLengthType = data.readBits(3);
        switch (this.frameLengthType) {
            case 0:
                data.skipBits(8);
                return;
            case 1:
                data.skipBits(9);
                return;
            case 3:
            case 4:
            case 5:
                data.skipBits(6);
                return;
            case 6:
            case 7:
                data.skipBits(1);
                return;
            default:
                return;
        }
    }

    private int parseAudioSpecificConfig(ParsableBitArray data) throws ParserException {
        int bitsLeft = data.bitsLeft();
        Pair<Integer, Integer> config = CodecSpecificDataUtil.parseAacAudioSpecificConfig(data, true);
        this.sampleRateHz = ((Integer) config.first).intValue();
        this.channelCount = ((Integer) config.second).intValue();
        return bitsLeft - data.bitsLeft();
    }

    private int parsePayloadLengthInfo(ParsableBitArray data) throws ParserException {
        int muxSlotLengthBytes = 0;
        if (this.frameLengthType == 0) {
            int tmp;
            do {
                tmp = data.readBits(8);
                muxSlotLengthBytes += tmp;
            } while (tmp == 255);
            return muxSlotLengthBytes;
        }
        throw new ParserException();
    }

    private void parsePayloadMux(ParsableBitArray data, int muxLengthBytes) {
        int bitPosition = data.getPosition();
        if ((bitPosition & 7) == 0) {
            this.sampleDataBuffer.setPosition(bitPosition >> 3);
        } else {
            data.readBits(this.sampleDataBuffer.data, 0, muxLengthBytes * 8);
            this.sampleDataBuffer.setPosition(0);
        }
        this.output.sampleData(this.sampleDataBuffer, muxLengthBytes);
        this.output.sampleMetadata(this.timeUs, 1, muxLengthBytes, 0, null);
        this.timeUs += this.sampleDurationUs;
    }

    private void resetBufferForSize(int newSize) {
        this.sampleDataBuffer.reset(newSize);
        this.sampleBitArray.reset(this.sampleDataBuffer.data);
    }

    private static long latmGetValue(ParsableBitArray data) {
        return (long) data.readBits((data.readBits(2) + 1) * 8);
    }
}
