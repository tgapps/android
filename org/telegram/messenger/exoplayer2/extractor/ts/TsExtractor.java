package org.telegram.messenger.exoplayer2.extractor.ts;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.extractor.Extractor;
import org.telegram.messenger.exoplayer2.extractor.ExtractorInput;
import org.telegram.messenger.exoplayer2.extractor.ExtractorOutput;
import org.telegram.messenger.exoplayer2.extractor.ExtractorsFactory;
import org.telegram.messenger.exoplayer2.extractor.PositionHolder;
import org.telegram.messenger.exoplayer2.extractor.SeekMap.Unseekable;
import org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader.DvbSubtitleInfo;
import org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader.EsInfo;
import org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader.Factory;
import org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader.TrackIdGenerator;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.ParsableBitArray;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.TimestampAdjuster;
import org.telegram.messenger.exoplayer2.util.Util;

public final class TsExtractor implements Extractor {
    private static final long AC3_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("AC-3"));
    private static final int BUFFER_SIZE = 9400;
    private static final long E_AC3_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("EAC3"));
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() {
        public Extractor[] createExtractors() {
            return new Extractor[]{new TsExtractor()};
        }
    };
    private static final long HEVC_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("HEVC"));
    private static final int MAX_PID_PLUS_ONE = 8192;
    public static final int MODE_HLS = 2;
    public static final int MODE_MULTI_PMT = 0;
    public static final int MODE_SINGLE_PMT = 1;
    private static final int SNIFF_TS_PACKET_COUNT = 5;
    private static final int TS_PACKET_SIZE = 188;
    private static final int TS_PAT_PID = 0;
    public static final int TS_STREAM_TYPE_AAC_ADTS = 15;
    public static final int TS_STREAM_TYPE_AAC_LATM = 17;
    public static final int TS_STREAM_TYPE_AC3 = 129;
    public static final int TS_STREAM_TYPE_DTS = 138;
    public static final int TS_STREAM_TYPE_DVBSUBS = 89;
    public static final int TS_STREAM_TYPE_E_AC3 = 135;
    public static final int TS_STREAM_TYPE_H262 = 2;
    public static final int TS_STREAM_TYPE_H264 = 27;
    public static final int TS_STREAM_TYPE_H265 = 36;
    public static final int TS_STREAM_TYPE_HDMV_DTS = 130;
    public static final int TS_STREAM_TYPE_ID3 = 21;
    public static final int TS_STREAM_TYPE_MPA = 3;
    public static final int TS_STREAM_TYPE_MPA_LSF = 4;
    public static final int TS_STREAM_TYPE_SPLICE_INFO = 134;
    private static final int TS_SYNC_BYTE = 71;
    private int bytesSinceLastSync;
    private final SparseIntArray continuityCounters;
    private TsPayloadReader id3Reader;
    private final int mode;
    private ExtractorOutput output;
    private final Factory payloadReaderFactory;
    private int remainingPmts;
    private final List<TimestampAdjuster> timestampAdjusters;
    private final SparseBooleanArray trackIds;
    private boolean tracksEnded;
    private final ParsableByteArray tsPacketBuffer;
    private final SparseArray<TsPayloadReader> tsPayloadReaders;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    private class PatReader implements SectionPayloadReader {
        private final ParsableBitArray patScratch = new ParsableBitArray(new byte[4]);

        public void init(TimestampAdjuster timestampAdjuster, ExtractorOutput extractorOutput, TrackIdGenerator idGenerator) {
        }

        public void consume(ParsableByteArray sectionData) {
            if (sectionData.readUnsignedByte() == 0) {
                sectionData.skipBytes(7);
                int programCount = sectionData.bytesLeft() / 4;
                for (int i = 0; i < programCount; i++) {
                    sectionData.readBytes(this.patScratch, 4);
                    int programNumber = this.patScratch.readBits(16);
                    this.patScratch.skipBits(3);
                    if (programNumber == 0) {
                        this.patScratch.skipBits(13);
                    } else {
                        int pid = this.patScratch.readBits(13);
                        TsExtractor.this.tsPayloadReaders.put(pid, new SectionReader(new PmtReader(pid)));
                        TsExtractor.this.remainingPmts = TsExtractor.this.remainingPmts + 1;
                    }
                }
                if (TsExtractor.this.mode != 2) {
                    TsExtractor.this.tsPayloadReaders.remove(0);
                }
            }
        }
    }

    private class PmtReader implements SectionPayloadReader {
        private static final int TS_PMT_DESC_AC3 = 106;
        private static final int TS_PMT_DESC_DTS = 123;
        private static final int TS_PMT_DESC_DVBSUBS = 89;
        private static final int TS_PMT_DESC_EAC3 = 122;
        private static final int TS_PMT_DESC_ISO639_LANG = 10;
        private static final int TS_PMT_DESC_REGISTRATION = 5;
        private final int pid;
        private final ParsableBitArray pmtScratch = new ParsableBitArray(new byte[5]);
        private final SparseIntArray trackIdToPidScratch = new SparseIntArray();
        private final SparseArray<TsPayloadReader> trackIdToReaderScratch = new SparseArray();

        public void consume(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.PmtReader.consume(org.telegram.messenger.exoplayer2.util.ParsableByteArray):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
            r0 = r17;
            r1 = r18;
            r2 = r18.readUnsignedByte();
            r3 = 2;
            if (r2 == r3) goto L_0x000c;
        L_0x000b:
            return;
        L_0x000c:
            r4 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r4 = r4.mode;
            r5 = 0;
            r6 = 1;
            if (r4 == r6) goto L_0x0046;
        L_0x0016:
            r4 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r4 = r4.mode;
            if (r4 == r3) goto L_0x0046;
        L_0x001e:
            r4 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r4 = r4.remainingPmts;
            if (r4 != r6) goto L_0x0027;
        L_0x0026:
            goto L_0x0046;
        L_0x0027:
            r4 = new org.telegram.messenger.exoplayer2.util.TimestampAdjuster;
            r7 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r7 = r7.timestampAdjusters;
            r7 = r7.get(r5);
            r7 = (org.telegram.messenger.exoplayer2.util.TimestampAdjuster) r7;
            r7 = r7.getFirstSampleTimestampUs();
            r4.<init>(r7);
            r7 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r7 = r7.timestampAdjusters;
            r7.add(r4);
            goto L_0x0052;
        L_0x0046:
            r4 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r4 = r4.timestampAdjusters;
            r4 = r4.get(r5);
            r4 = (org.telegram.messenger.exoplayer2.util.TimestampAdjuster) r4;
        L_0x0052:
            r1.skipBytes(r3);
            r7 = r18.readUnsignedShort();
            r8 = 5;
            r1.skipBytes(r8);
            r9 = r0.pmtScratch;
            r1.readBytes(r9, r3);
            r9 = r0.pmtScratch;
            r10 = 4;
            r9.skipBits(r10);
            r9 = r0.pmtScratch;
            r11 = 12;
            r9 = r9.readBits(r11);
            r1.skipBytes(r9);
            r12 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r12 = r12.mode;
            r13 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r14 = 21;
            if (r12 != r3) goto L_0x00b3;
        L_0x007f:
            r12 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r12 = r12.id3Reader;
            if (r12 != 0) goto L_0x00b3;
        L_0x0087:
            r12 = new org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader$EsInfo;
            r15 = new byte[r5];
            r5 = 0;
            r12.<init>(r14, r5, r5, r15);
            r5 = r12;
            r12 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r15 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r15 = r15.payloadReaderFactory;
            r15 = r15.createPayloadReader(r14, r5);
            r12.id3Reader = r15;
            r12 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r12 = r12.id3Reader;
            r15 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r15 = r15.output;
            r6 = new org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader$TrackIdGenerator;
            r6.<init>(r7, r14, r13);
            r12.init(r4, r15, r6);
        L_0x00b3:
            r5 = r0.trackIdToReaderScratch;
            r5.clear();
            r5 = r0.trackIdToPidScratch;
            r5.clear();
            r5 = r18.bytesLeft();
            if (r5 <= 0) goto L_0x014b;
        L_0x00c3:
            r6 = r0.pmtScratch;
            r1.readBytes(r6, r8);
            r6 = r0.pmtScratch;
            r12 = 8;
            r6 = r6.readBits(r12);
            r12 = r0.pmtScratch;
            r15 = 3;
            r12.skipBits(r15);
            r12 = r0.pmtScratch;
            r15 = 13;
            r12 = r12.readBits(r15);
            r15 = r0.pmtScratch;
            r15.skipBits(r10);
            r15 = r0.pmtScratch;
            r15 = r15.readBits(r11);
            r8 = r0.readEsInfo(r1, r15);
            r10 = 6;
            if (r6 != r10) goto L_0x00f2;
        L_0x00f0:
            r6 = r8.streamType;
        L_0x00f2:
            r10 = r15 + 5;
            r5 = r5 - r10;
            r10 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r10 = r10.mode;
            if (r10 != r3) goto L_0x00ff;
        L_0x00fd:
            r10 = r6;
            goto L_0x0100;
        L_0x00ff:
            r10 = r12;
        L_0x0100:
            r11 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r11 = r11.trackIds;
            r11 = r11.get(r10);
            if (r11 == 0) goto L_0x010d;
        L_0x010c:
            goto L_0x0143;
        L_0x010d:
            r11 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r11 = r11.mode;
            if (r11 != r3) goto L_0x011e;
        L_0x0115:
            if (r6 != r14) goto L_0x011e;
        L_0x0117:
            r11 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r11 = r11.id3Reader;
            goto L_0x0128;
        L_0x011e:
            r11 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r11 = r11.payloadReaderFactory;
            r11 = r11.createPayloadReader(r6, r8);
        L_0x0128:
            r14 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r14 = r14.mode;
            if (r14 != r3) goto L_0x0138;
        L_0x0130:
            r14 = r0.trackIdToPidScratch;
            r14 = r14.get(r10, r13);
            if (r12 >= r14) goto L_0x0142;
        L_0x0138:
            r14 = r0.trackIdToPidScratch;
            r14.put(r10, r12);
            r14 = r0.trackIdToReaderScratch;
            r14.put(r10, r11);
        L_0x0143:
            r8 = 5;
            r10 = 4;
            r11 = 12;
            r14 = 21;
            goto L_0x00c1;
        L_0x014b:
            r6 = r0.trackIdToPidScratch;
            r6 = r6.size();
            r8 = 0;
            if (r8 >= r6) goto L_0x0196;
            r10 = r0.trackIdToPidScratch;
            r10 = r10.keyAt(r8);
            r11 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r11 = r11.trackIds;
            r12 = 1;
            r11.put(r10, r12);
            r11 = r0.trackIdToReaderScratch;
            r11 = r11.valueAt(r8);
            r11 = (org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader) r11;
            if (r11 == 0) goto L_0x0193;
            r12 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r12 = r12.id3Reader;
            if (r11 == r12) goto L_0x0184;
            r12 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r12 = r12.output;
            r14 = new org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader$TrackIdGenerator;
            r14.<init>(r7, r10, r13);
            r11.init(r4, r12, r14);
            r12 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r12 = r12.tsPayloadReaders;
            r14 = r0.trackIdToPidScratch;
            r14 = r14.valueAt(r8);
            r12.put(r14, r11);
            r8 = r8 + 1;
            goto L_0x0152;
            r8 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r8 = r8.mode;
            if (r8 != r3) goto L_0x01bc;
            r3 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r3 = r3.tracksEnded;
            if (r3 != 0) goto L_0x01f5;
            r3 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r3 = r3.output;
            r3.endTracks();
            r3 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r8 = 0;
            r3.remainingPmts = r8;
            r3 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r8 = 1;
            r3.tracksEnded = r8;
            goto L_0x01f5;
            r8 = 0;
            r3 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r3 = r3.tsPayloadReaders;
            r10 = r0.pid;
            r3.remove(r10);
            r3 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r10 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r10 = r10.mode;
            r11 = 1;
            if (r10 != r11) goto L_0x01d4;
            goto L_0x01db;
            r8 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r8 = r8.remainingPmts;
            r8 = r8 - r11;
            r3.remainingPmts = r8;
            r3 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r3 = r3.remainingPmts;
            if (r3 != 0) goto L_0x01f5;
            r3 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r3 = r3.output;
            r3.endTracks();
            r3 = org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.this;
            r8 = 1;
            r3.tracksEnded = r8;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.ts.TsExtractor.PmtReader.consume(org.telegram.messenger.exoplayer2.util.ParsableByteArray):void");
        }

        public PmtReader(int pid) {
            this.pid = pid;
        }

        public void init(TimestampAdjuster timestampAdjuster, ExtractorOutput extractorOutput, TrackIdGenerator idGenerator) {
        }

        private EsInfo readEsInfo(ParsableByteArray data, int length) {
            ParsableByteArray parsableByteArray = data;
            int descriptorsStartPosition = data.getPosition();
            int descriptorsEndPosition = descriptorsStartPosition + length;
            int streamType = -1;
            String language = null;
            List<DvbSubtitleInfo> dvbSubtitleInfos = null;
            while (data.getPosition() < descriptorsEndPosition) {
                int descriptorTag = data.readUnsignedByte();
                int positionOfNextDescriptor = data.getPosition() + data.readUnsignedByte();
                if (descriptorTag == 5) {
                    long formatIdentifier = data.readUnsignedInt();
                    if (formatIdentifier == TsExtractor.AC3_FORMAT_IDENTIFIER) {
                        streamType = TsExtractor.TS_STREAM_TYPE_AC3;
                    } else if (formatIdentifier == TsExtractor.E_AC3_FORMAT_IDENTIFIER) {
                        streamType = TsExtractor.TS_STREAM_TYPE_E_AC3;
                    } else if (formatIdentifier == TsExtractor.HEVC_FORMAT_IDENTIFIER) {
                        streamType = 36;
                    }
                } else if (descriptorTag == TS_PMT_DESC_AC3) {
                    streamType = TsExtractor.TS_STREAM_TYPE_AC3;
                } else if (descriptorTag == TS_PMT_DESC_EAC3) {
                    streamType = TsExtractor.TS_STREAM_TYPE_E_AC3;
                } else if (descriptorTag == TS_PMT_DESC_DTS) {
                    streamType = TsExtractor.TS_STREAM_TYPE_DTS;
                } else if (descriptorTag == 10) {
                    language = parsableByteArray.readString(3).trim();
                } else if (descriptorTag == 89) {
                    streamType = 89;
                    dvbSubtitleInfos = new ArrayList();
                    while (data.getPosition() < positionOfNextDescriptor) {
                        String dvbLanguage = parsableByteArray.readString(3).trim();
                        int dvbSubtitlingType = data.readUnsignedByte();
                        byte[] initializationData = new byte[4];
                        parsableByteArray.readBytes(initializationData, 0, 4);
                        dvbSubtitleInfos.add(new DvbSubtitleInfo(dvbLanguage, dvbSubtitlingType, initializationData));
                    }
                }
                parsableByteArray.skipBytes(positionOfNextDescriptor - data.getPosition());
            }
            parsableByteArray.setPosition(descriptorsEndPosition);
            return new EsInfo(streamType, language, dvbSubtitleInfos, Arrays.copyOfRange(parsableByteArray.data, descriptorsStartPosition, descriptorsEndPosition));
        }
    }

    public TsExtractor() {
        this(0);
    }

    public TsExtractor(int defaultTsPayloadReaderFlags) {
        this(1, defaultTsPayloadReaderFlags);
    }

    public TsExtractor(int mode, int defaultTsPayloadReaderFlags) {
        this(mode, new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory(defaultTsPayloadReaderFlags));
    }

    public TsExtractor(int mode, TimestampAdjuster timestampAdjuster, Factory payloadReaderFactory) {
        this.payloadReaderFactory = (Factory) Assertions.checkNotNull(payloadReaderFactory);
        this.mode = mode;
        if (mode != 1) {
            if (mode != 2) {
                this.timestampAdjusters = new ArrayList();
                this.timestampAdjusters.add(timestampAdjuster);
                this.tsPacketBuffer = new ParsableByteArray(new byte[BUFFER_SIZE], 0);
                this.trackIds = new SparseBooleanArray();
                this.tsPayloadReaders = new SparseArray();
                this.continuityCounters = new SparseIntArray();
                resetPayloadReaders();
            }
        }
        this.timestampAdjusters = Collections.singletonList(timestampAdjuster);
        this.tsPacketBuffer = new ParsableByteArray(new byte[BUFFER_SIZE], 0);
        this.trackIds = new SparseBooleanArray();
        this.tsPayloadReaders = new SparseArray();
        this.continuityCounters = new SparseIntArray();
        resetPayloadReaders();
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        byte[] buffer = this.tsPacketBuffer.data;
        input.peekFully(buffer, 0, 940);
        int j = 0;
        while (j < TS_PACKET_SIZE) {
            int i = 0;
            while (i != 5) {
                if (buffer[(i * TS_PACKET_SIZE) + j] != (byte) 71) {
                    j++;
                } else {
                    i++;
                }
            }
            input.skipFully(j);
            return true;
        }
        return false;
    }

    public void init(ExtractorOutput output) {
        this.output = output;
        output.seekMap(new Unseekable(C.TIME_UNSET));
    }

    public void seek(long position, long timeUs) {
        int timestampAdjustersCount = this.timestampAdjusters.size();
        for (int i = 0; i < timestampAdjustersCount; i++) {
            ((TimestampAdjuster) this.timestampAdjusters.get(i)).reset();
        }
        this.tsPacketBuffer.reset();
        this.continuityCounters.clear();
        resetPayloadReaders();
        this.bytesSinceLastSync = 0;
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        int bytesLeft;
        int read;
        byte[] data = this.tsPacketBuffer.data;
        if (9400 - this.tsPacketBuffer.getPosition() < TS_PACKET_SIZE) {
            bytesLeft = r0.tsPacketBuffer.bytesLeft();
            if (bytesLeft > 0) {
                System.arraycopy(data, r0.tsPacketBuffer.getPosition(), data, 0, bytesLeft);
            }
            r0.tsPacketBuffer.reset(data, bytesLeft);
        }
        while (r0.tsPacketBuffer.bytesLeft() < TS_PACKET_SIZE) {
            bytesLeft = r0.tsPacketBuffer.limit();
            read = input.read(data, bytesLeft, 9400 - bytesLeft);
            if (read == -1) {
                return -1;
            }
            r0.tsPacketBuffer.setLimit(bytesLeft + read);
        }
        ExtractorInput extractorInput = input;
        bytesLeft = r0.tsPacketBuffer.limit();
        int searchStart = r0.tsPacketBuffer.getPosition();
        read = searchStart;
        while (read < bytesLeft && data[read] != (byte) 71) {
            read++;
        }
        r0.tsPacketBuffer.setPosition(read);
        int endOfPacket = read + TS_PACKET_SIZE;
        if (endOfPacket > bytesLeft) {
            r0.bytesSinceLastSync += read - searchStart;
            if (r0.mode != 2 || r0.bytesSinceLastSync <= 376) {
                return 0;
            }
            throw new ParserException("Cannot find sync byte. Most likely not a Transport Stream.");
        }
        r0.bytesSinceLastSync = 0;
        int tsPacketHeader = r0.tsPacketBuffer.readInt();
        if ((8388608 & tsPacketHeader) != 0) {
            r0.tsPacketBuffer.setPosition(endOfPacket);
            return 0;
        }
        boolean payloadExists = true;
        boolean payloadUnitStartIndicator = (4194304 & tsPacketHeader) != 0;
        int pid = (2096896 & tsPacketHeader) >> 8;
        boolean adaptationFieldExists = (tsPacketHeader & 32) != 0;
        if ((tsPacketHeader & 16) == 0) {
            payloadExists = false;
        }
        TsPayloadReader payloadReader = payloadExists ? (TsPayloadReader) r0.tsPayloadReaders.get(pid) : null;
        if (payloadReader == null) {
            r0.tsPacketBuffer.setPosition(endOfPacket);
            return 0;
        }
        if (r0.mode != 2) {
            int continuityCounter = tsPacketHeader & 15;
            int previousCounter = r0.continuityCounters.get(pid, continuityCounter - 1);
            r0.continuityCounters.put(pid, continuityCounter);
            if (previousCounter == continuityCounter) {
                r0.tsPacketBuffer.setPosition(endOfPacket);
                return 0;
            } else if (continuityCounter != ((previousCounter + 1) & 15)) {
                payloadReader.seek();
            }
        }
        if (adaptationFieldExists) {
            r0.tsPacketBuffer.skipBytes(r0.tsPacketBuffer.readUnsignedByte());
        }
        r0.tsPacketBuffer.setLimit(endOfPacket);
        payloadReader.consume(r0.tsPacketBuffer, payloadUnitStartIndicator);
        r0.tsPacketBuffer.setLimit(bytesLeft);
        r0.tsPacketBuffer.setPosition(endOfPacket);
        return 0;
    }

    private void resetPayloadReaders() {
        this.trackIds.clear();
        this.tsPayloadReaders.clear();
        SparseArray<TsPayloadReader> initialPayloadReaders = this.payloadReaderFactory.createInitialPayloadReaders();
        int initialPayloadReadersSize = initialPayloadReaders.size();
        for (int i = 0; i < initialPayloadReadersSize; i++) {
            this.tsPayloadReaders.put(initialPayloadReaders.keyAt(i), initialPayloadReaders.valueAt(i));
        }
        this.tsPayloadReaders.put(0, new SectionReader(new PatReader()));
        this.id3Reader = null;
    }
}
