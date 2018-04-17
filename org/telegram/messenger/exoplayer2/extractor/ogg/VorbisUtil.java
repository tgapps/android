package org.telegram.messenger.exoplayer2.extractor.ogg;

import java.util.Arrays;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.extractor.ts.PsExtractor;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;

final class VorbisUtil {
    private static final String TAG = "VorbisUtil";

    public static final class CodeBook {
        public final int dimensions;
        public final int entries;
        public final boolean isOrdered;
        public final long[] lengthMap;
        public final int lookupType;

        public CodeBook(int dimensions, int entries, long[] lengthMap, int lookupType, boolean isOrdered) {
            this.dimensions = dimensions;
            this.entries = entries;
            this.lengthMap = lengthMap;
            this.lookupType = lookupType;
            this.isOrdered = isOrdered;
        }
    }

    public static final class CommentHeader {
        public final String[] comments;
        public final int length;
        public final String vendor;

        public CommentHeader(String vendor, String[] comments, int length) {
            this.vendor = vendor;
            this.comments = comments;
            this.length = length;
        }
    }

    public static final class Mode {
        public final boolean blockFlag;
        public final int mapping;
        public final int transformType;
        public final int windowType;

        public Mode(boolean blockFlag, int windowType, int transformType, int mapping) {
            this.blockFlag = blockFlag;
            this.windowType = windowType;
            this.transformType = transformType;
            this.mapping = mapping;
        }
    }

    public static final class VorbisIdHeader {
        public final int bitrateMax;
        public final int bitrateMin;
        public final int bitrateNominal;
        public final int blockSize0;
        public final int blockSize1;
        public final int channels;
        public final byte[] data;
        public final boolean framingFlag;
        public final long sampleRate;
        public final long version;

        public VorbisIdHeader(long version, int channels, long sampleRate, int bitrateMax, int bitrateNominal, int bitrateMin, int blockSize0, int blockSize1, boolean framingFlag, byte[] data) {
            this.version = version;
            this.channels = channels;
            this.sampleRate = sampleRate;
            this.bitrateMax = bitrateMax;
            this.bitrateNominal = bitrateNominal;
            this.bitrateMin = bitrateMin;
            this.blockSize0 = blockSize0;
            this.blockSize1 = blockSize1;
            this.framingFlag = framingFlag;
            this.data = data;
        }

        public int getApproximateBitrate() {
            return this.bitrateNominal == 0 ? (this.bitrateMin + this.bitrateMax) / 2 : this.bitrateNominal;
        }
    }

    private static void readFloors(org.telegram.messenger.exoplayer2.extractor.ogg.VorbisBitArray r1) throws org.telegram.messenger.exoplayer2.ParserException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.ogg.VorbisUtil.readFloors(org.telegram.messenger.exoplayer2.extractor.ogg.VorbisBitArray):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r17;
        r1 = 6;
        r2 = r0.readBits(r1);
        r3 = 1;
        r2 = r2 + r3;
        r5 = 0;
        if (r5 >= r2) goto L_0x00b2;
    L_0x000c:
        r6 = 16;
        r7 = r0.readBits(r6);
        r8 = 4;
        r9 = 8;
        switch(r7) {
            case 0: goto L_0x0090;
            case 1: goto L_0x002f;
            default: goto L_0x0018;
        };
    L_0x0018:
        r1 = new org.telegram.messenger.exoplayer2.ParserException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "floor type greater than 1 not decodable: ";
        r3.append(r4);
        r3.append(r7);
        r3 = r3.toString();
        r1.<init>(r3);
        throw r1;
    L_0x002f:
        r6 = 5;
        r6 = r0.readBits(r6);
        r10 = -1;
        r11 = new int[r6];
        r12 = r10;
        r10 = 0;
    L_0x0039:
        if (r10 >= r6) goto L_0x004a;
    L_0x003b:
        r13 = r0.readBits(r8);
        r11[r10] = r13;
        r13 = r11[r10];
        if (r13 <= r12) goto L_0x0047;
    L_0x0045:
        r12 = r11[r10];
    L_0x0047:
        r10 = r10 + 1;
        goto L_0x0039;
    L_0x004a:
        r10 = r12 + 1;
        r10 = new int[r10];
        r13 = 0;
    L_0x004f:
        r14 = 2;
        r15 = r10.length;
        if (r13 >= r15) goto L_0x0072;
    L_0x0053:
        r15 = 3;
        r15 = r0.readBits(r15);
        r15 = r15 + r3;
        r10[r13] = r15;
        r14 = r0.readBits(r14);
        if (r14 <= 0) goto L_0x0064;
    L_0x0061:
        r0.skipBits(r9);
    L_0x0064:
        r15 = 0;
    L_0x0065:
        r4 = r3 << r14;
        if (r15 >= r4) goto L_0x006f;
    L_0x0069:
        r0.skipBits(r9);
        r15 = r15 + 1;
        goto L_0x0065;
    L_0x006f:
        r13 = r13 + 1;
        goto L_0x004f;
    L_0x0072:
        r0.skipBits(r14);
        r4 = r0.readBits(r8);
        r8 = 0;
        r9 = 0;
        r13 = r8;
        r8 = 0;
    L_0x007d:
        if (r9 >= r6) goto L_0x008f;
    L_0x007f:
        r14 = r11[r9];
        r15 = r10[r14];
        r13 = r13 + r15;
    L_0x0084:
        if (r8 >= r13) goto L_0x008c;
    L_0x0086:
        r0.skipBits(r4);
        r8 = r8 + 1;
        goto L_0x0084;
    L_0x008c:
        r9 = r9 + 1;
        goto L_0x007d;
    L_0x008f:
        goto L_0x00ae;
    L_0x0090:
        r0.skipBits(r9);
        r0.skipBits(r6);
        r0.skipBits(r6);
        r0.skipBits(r1);
        r0.skipBits(r9);
        r4 = r0.readBits(r8);
        r4 = r4 + r3;
        r6 = 0;
        if (r6 >= r4) goto L_0x00ad;
    L_0x00a7:
        r0.skipBits(r9);
        r6 = r6 + 1;
        goto L_0x00a5;
    L_0x00ae:
        r5 = r5 + 1;
        goto L_0x000a;
    L_0x00b2:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.ogg.VorbisUtil.readFloors(org.telegram.messenger.exoplayer2.extractor.ogg.VorbisBitArray):void");
    }

    private static void readMappings(int r1, org.telegram.messenger.exoplayer2.extractor.ogg.VorbisBitArray r2) throws org.telegram.messenger.exoplayer2.ParserException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.ogg.VorbisUtil.readMappings(int, org.telegram.messenger.exoplayer2.extractor.ogg.VorbisBitArray):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 6;
        r0 = r12.readBits(r0);
        r1 = 1;
        r0 = r0 + r1;
        r2 = 0;
        r3 = r2;
        if (r3 >= r0) goto L_0x008b;
    L_0x000b:
        r4 = 16;
        r4 = r12.readBits(r4);
        if (r4 == 0) goto L_0x002a;
    L_0x0013:
        r5 = "VorbisUtil";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "mapping type other than 0 not supported: ";
        r6.append(r7);
        r6.append(r4);
        r6 = r6.toString();
        android.util.Log.e(r5, r6);
        goto L_0x0087;
    L_0x002a:
        r5 = r12.readBit();
        r6 = 4;
        if (r5 == 0) goto L_0x0037;
    L_0x0031:
        r5 = r12.readBits(r6);
        r5 = r5 + r1;
        goto L_0x0038;
    L_0x0037:
        r5 = r1;
    L_0x0038:
        r7 = r12.readBit();
        r8 = 8;
        if (r7 == 0) goto L_0x005d;
    L_0x0040:
        r7 = r12.readBits(r8);
        r7 = r7 + r1;
        r9 = r2;
    L_0x0046:
        if (r9 >= r7) goto L_0x005d;
    L_0x0048:
        r10 = r11 + -1;
        r10 = iLog(r10);
        r12.skipBits(r10);
        r10 = r11 + -1;
        r10 = iLog(r10);
        r12.skipBits(r10);
        r9 = r9 + 1;
        goto L_0x0046;
    L_0x005d:
        r7 = 2;
        r7 = r12.readBits(r7);
        if (r7 == 0) goto L_0x006c;
    L_0x0064:
        r1 = new org.telegram.messenger.exoplayer2.ParserException;
        r2 = "to reserved bits must be zero after mapping coupling steps";
        r1.<init>(r2);
        throw r1;
    L_0x006c:
        if (r5 <= r1) goto L_0x0077;
    L_0x006e:
        r7 = r2;
    L_0x006f:
        if (r7 >= r11) goto L_0x0077;
    L_0x0071:
        r12.skipBits(r6);
        r7 = r7 + 1;
        goto L_0x006f;
    L_0x0077:
        r6 = r2;
        if (r6 >= r5) goto L_0x0086;
    L_0x007a:
        r12.skipBits(r8);
        r12.skipBits(r8);
        r12.skipBits(r8);
        r6 = r6 + 1;
        goto L_0x0078;
    L_0x0087:
        r3 = r3 + 1;
        goto L_0x0009;
    L_0x008b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.ogg.VorbisUtil.readMappings(int, org.telegram.messenger.exoplayer2.extractor.ogg.VorbisBitArray):void");
    }

    VorbisUtil() {
    }

    public static int iLog(int x) {
        int val = 0;
        while (x > 0) {
            val++;
            x >>>= 1;
        }
        return val;
    }

    public static VorbisIdHeader readVorbisIdentificationHeader(ParsableByteArray headerData) throws ParserException {
        ParsableByteArray parsableByteArray = headerData;
        verifyVorbisHeaderCapturePattern(1, parsableByteArray, false);
        long version = headerData.readLittleEndianUnsignedInt();
        int channels = headerData.readUnsignedByte();
        long sampleRate = headerData.readLittleEndianUnsignedInt();
        int bitrateMax = headerData.readLittleEndianInt();
        int bitrateNominal = headerData.readLittleEndianInt();
        int bitrateMin = headerData.readLittleEndianInt();
        int blockSize = headerData.readUnsignedByte();
        int blockSize0 = (int) Math.pow(2.0d, (double) (blockSize & 15));
        return new VorbisIdHeader(version, channels, sampleRate, bitrateMax, bitrateNominal, bitrateMin, blockSize0, (int) Math.pow(2.0d, (double) ((blockSize & PsExtractor.VIDEO_STREAM_MASK) >> 4)), (headerData.readUnsignedByte() & 1) > 0, Arrays.copyOf(parsableByteArray.data, headerData.limit()));
    }

    public static CommentHeader readVorbisCommentHeader(ParsableByteArray headerData) throws ParserException {
        int i = 0;
        verifyVorbisHeaderCapturePattern(3, headerData, false);
        int length = 7 + 4;
        String vendor = headerData.readString((int) headerData.readLittleEndianUnsignedInt());
        length += vendor.length();
        long commentListLen = headerData.readLittleEndianUnsignedInt();
        String[] comments = new String[((int) commentListLen)];
        length += 4;
        while (((long) i) < commentListLen) {
            length += 4;
            comments[i] = headerData.readString((int) headerData.readLittleEndianUnsignedInt());
            length += comments[i].length();
            i++;
        }
        if ((headerData.readUnsignedByte() & 1) != 0) {
            return new CommentHeader(vendor, comments, length + 1);
        }
        throw new ParserException("framing bit expected to be set");
    }

    public static boolean verifyVorbisHeaderCapturePattern(int headerType, ParsableByteArray header, boolean quiet) throws ParserException {
        StringBuilder stringBuilder;
        if (header.bytesLeft() < 7) {
            if (quiet) {
                return false;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("too short header: ");
            stringBuilder.append(header.bytesLeft());
            throw new ParserException(stringBuilder.toString());
        } else if (header.readUnsignedByte() == headerType) {
            if (header.readUnsignedByte() == 118 && header.readUnsignedByte() == 111 && header.readUnsignedByte() == 114 && header.readUnsignedByte() == 98 && header.readUnsignedByte() == 105) {
                if (header.readUnsignedByte() == 115) {
                    return true;
                }
            }
            if (quiet) {
                return false;
            }
            throw new ParserException("expected characters 'vorbis'");
        } else if (quiet) {
            return false;
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("expected header type ");
            stringBuilder.append(Integer.toHexString(headerType));
            throw new ParserException(stringBuilder.toString());
        }
    }

    public static Mode[] readVorbisModes(ParsableByteArray headerData, int channels) throws ParserException {
        int i;
        int i2 = 0;
        verifyVorbisHeaderCapturePattern(5, headerData, false);
        int numberOfBooks = headerData.readUnsignedByte() + 1;
        VorbisBitArray bitArray = new VorbisBitArray(headerData.data);
        bitArray.skipBits(headerData.getPosition() * 8);
        for (i = 0; i < numberOfBooks; i++) {
            readBook(bitArray);
        }
        i = bitArray.readBits(6) + 1;
        while (i2 < i) {
            if (bitArray.readBits(16) != 0) {
                throw new ParserException("placeholder of time domain transforms not zeroed out");
            }
            i2++;
        }
        readFloors(bitArray);
        readResidues(bitArray);
        readMappings(channels, bitArray);
        Mode[] modes = readModes(bitArray);
        if (bitArray.readBit()) {
            return modes;
        }
        throw new ParserException("framing bit after modes not set as expected");
    }

    private static Mode[] readModes(VorbisBitArray bitArray) {
        int modeCount = bitArray.readBits(6) + 1;
        Mode[] modes = new Mode[modeCount];
        for (int i = 0; i < modeCount; i++) {
            modes[i] = new Mode(bitArray.readBit(), bitArray.readBits(16), bitArray.readBits(16), bitArray.readBits(8));
        }
        return modes;
    }

    private static void readResidues(VorbisBitArray bitArray) throws ParserException {
        int residueCount = bitArray.readBits(6) + 1;
        for (int i = 0; i < residueCount; i++) {
            if (bitArray.readBits(16) > 2) {
                throw new ParserException("residueType greater than 2 is not decodable");
            }
            int j;
            bitArray.skipBits(24);
            bitArray.skipBits(24);
            bitArray.skipBits(24);
            int classifications = bitArray.readBits(6) + 1;
            bitArray.skipBits(8);
            int[] cascade = new int[classifications];
            for (j = 0; j < classifications; j++) {
                int highBits = 0;
                int lowBits = bitArray.readBits(3);
                if (bitArray.readBit()) {
                    highBits = bitArray.readBits(5);
                }
                cascade[j] = (highBits * 8) + lowBits;
            }
            for (j = 0; j < classifications; j++) {
                for (highBits = 0; highBits < 8; highBits++) {
                    if ((cascade[j] & (1 << highBits)) != 0) {
                        bitArray.skipBits(8);
                    }
                }
            }
        }
    }

    private static CodeBook readBook(VorbisBitArray bitArray) throws ParserException {
        if (bitArray.readBits(24) != 5653314) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("expected code book to start with [0x56, 0x43, 0x42] at ");
            stringBuilder.append(bitArray.getPosition());
            throw new ParserException(stringBuilder.toString());
        }
        int i;
        int dimensions = bitArray.readBits(16);
        int entries = bitArray.readBits(24);
        long[] lengthMap = new long[entries];
        boolean isOrdered = bitArray.readBit();
        int i2 = 0;
        if (isOrdered) {
            int length = bitArray.readBits(5) + 1;
            i = 0;
            while (i < lengthMap.length) {
                int num = bitArray.readBits(iLog(entries - i));
                int i3 = i;
                for (i = 0; i < num && i3 < lengthMap.length; i++) {
                    lengthMap[i3] = (long) length;
                    i3++;
                }
                length++;
                i = i3;
            }
        } else {
            boolean isSparse = bitArray.readBit();
            while (i2 < lengthMap.length) {
                if (!isSparse) {
                    lengthMap[i2] = (long) (bitArray.readBits(5) + 1);
                } else if (bitArray.readBit()) {
                    lengthMap[i2] = (long) (bitArray.readBits(5) + 1);
                } else {
                    lengthMap[i2] = 0;
                }
                i2++;
            }
        }
        int lookupType = bitArray.readBits(4);
        if (lookupType > 2) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("lookup type greater than 2 not decodable: ");
            stringBuilder2.append(lookupType);
            throw new ParserException(stringBuilder2.toString());
        }
        if (lookupType == 1 || lookupType == 2) {
            long lookupValuesCount;
            bitArray.skipBits(32);
            bitArray.skipBits(32);
            i = bitArray.readBits(4) + 1;
            bitArray.skipBits(1);
            if (lookupType != 1) {
                lookupValuesCount = (long) (entries * dimensions);
            } else if (dimensions != 0) {
                lookupValuesCount = mapType1QuantValues((long) entries, (long) dimensions);
            } else {
                lookupValuesCount = 0;
            }
            bitArray.skipBits((int) (((long) i) * lookupValuesCount));
        }
        return new CodeBook(dimensions, entries, lengthMap, lookupType, isOrdered);
    }

    private static long mapType1QuantValues(long entries, long dimension) {
        return (long) Math.floor(Math.pow((double) entries, 1.0d / ((double) dimension)));
    }
}
