package org.telegram.messenger.exoplayer2.extractor.mp4;

import android.util.Pair;
import com.coremedia.iso.boxes.MetaBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.audio.Ac3Util;
import org.telegram.messenger.exoplayer2.drm.DrmInitData;
import org.telegram.messenger.exoplayer2.extractor.ts.PsExtractor;
import org.telegram.messenger.exoplayer2.metadata.Metadata;
import org.telegram.messenger.exoplayer2.metadata.Metadata.Entry;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.CodecSpecificDataUtil;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.Util;
import org.telegram.messenger.exoplayer2.video.AvcConfig;
import org.telegram.messenger.exoplayer2.video.HevcConfig;

final class AtomParsers {
    private static final String TAG = "AtomParsers";
    private static final int TYPE_cenc = Util.getIntegerCodeForString(C.CENC_TYPE_cenc);
    private static final int TYPE_clcp = Util.getIntegerCodeForString("clcp");
    private static final int TYPE_meta = Util.getIntegerCodeForString(MetaBox.TYPE);
    private static final int TYPE_sbtl = Util.getIntegerCodeForString("sbtl");
    private static final int TYPE_soun = Util.getIntegerCodeForString("soun");
    private static final int TYPE_subt = Util.getIntegerCodeForString("subt");
    private static final int TYPE_text = Util.getIntegerCodeForString(MimeTypes.BASE_TYPE_TEXT);
    private static final int TYPE_vide = Util.getIntegerCodeForString("vide");

    private static final class ChunkIterator {
        private final ParsableByteArray chunkOffsets;
        private final boolean chunkOffsetsAreLongs;
        public int index;
        public final int length;
        private int nextSamplesPerChunkChangeIndex;
        public int numSamples;
        public long offset;
        private int remainingSamplesPerChunkChanges;
        private final ParsableByteArray stsc;

        public ChunkIterator(ParsableByteArray stsc, ParsableByteArray chunkOffsets, boolean chunkOffsetsAreLongs) {
            this.stsc = stsc;
            this.chunkOffsets = chunkOffsets;
            this.chunkOffsetsAreLongs = chunkOffsetsAreLongs;
            chunkOffsets.setPosition(12);
            this.length = chunkOffsets.readUnsignedIntToInt();
            stsc.setPosition(12);
            this.remainingSamplesPerChunkChanges = stsc.readUnsignedIntToInt();
            boolean z = true;
            if (stsc.readInt() != 1) {
                z = false;
            }
            Assertions.checkState(z, "first_chunk must be 1");
            this.index = -1;
        }

        public boolean moveNext() {
            int i = this.index + 1;
            this.index = i;
            if (i == this.length) {
                return false;
            }
            long readUnsignedLongToLong;
            if (this.chunkOffsetsAreLongs) {
                readUnsignedLongToLong = this.chunkOffsets.readUnsignedLongToLong();
            } else {
                readUnsignedLongToLong = this.chunkOffsets.readUnsignedInt();
            }
            this.offset = readUnsignedLongToLong;
            if (this.index == this.nextSamplesPerChunkChangeIndex) {
                this.numSamples = this.stsc.readUnsignedIntToInt();
                this.stsc.skipBytes(4);
                i = this.remainingSamplesPerChunkChanges - 1;
                this.remainingSamplesPerChunkChanges = i;
                this.nextSamplesPerChunkChangeIndex = i > 0 ? this.stsc.readUnsignedIntToInt() - 1 : -1;
            }
            return true;
        }
    }

    private interface SampleSizeBox {
        int getSampleCount();

        boolean isFixedSampleSize();

        int readNextSampleSize();
    }

    private static final class StsdData {
        public static final int STSD_HEADER_SIZE = 8;
        public Format format;
        public int nalUnitLengthFieldLength;
        public int requiredSampleTransformation = 0;
        public final TrackEncryptionBox[] trackEncryptionBoxes;

        public StsdData(int numberOfEntries) {
            this.trackEncryptionBoxes = new TrackEncryptionBox[numberOfEntries];
        }
    }

    private static final class TkhdData {
        private final long duration;
        private final int id;
        private final int rotationDegrees;

        public TkhdData(int id, long duration, int rotationDegrees) {
            this.id = id;
            this.duration = duration;
            this.rotationDegrees = rotationDegrees;
        }
    }

    static final class StszSampleSizeBox implements SampleSizeBox {
        private final ParsableByteArray data;
        private final int fixedSampleSize = this.data.readUnsignedIntToInt();
        private final int sampleCount = this.data.readUnsignedIntToInt();

        public StszSampleSizeBox(LeafAtom stszAtom) {
            this.data = stszAtom.data;
            this.data.setPosition(12);
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public int readNextSampleSize() {
            return this.fixedSampleSize == 0 ? this.data.readUnsignedIntToInt() : this.fixedSampleSize;
        }

        public boolean isFixedSampleSize() {
            return this.fixedSampleSize != 0;
        }
    }

    static final class Stz2SampleSizeBox implements SampleSizeBox {
        private int currentByte;
        private final ParsableByteArray data;
        private final int fieldSize = (this.data.readUnsignedIntToInt() & 255);
        private final int sampleCount = this.data.readUnsignedIntToInt();
        private int sampleIndex;

        public Stz2SampleSizeBox(LeafAtom stz2Atom) {
            this.data = stz2Atom.data;
            this.data.setPosition(12);
        }

        public int getSampleCount() {
            return this.sampleCount;
        }

        public int readNextSampleSize() {
            if (this.fieldSize == 8) {
                return this.data.readUnsignedByte();
            }
            if (this.fieldSize == 16) {
                return this.data.readUnsignedShort();
            }
            int i = this.sampleIndex;
            this.sampleIndex = i + 1;
            if (i % 2 != 0) {
                return this.currentByte & 15;
            }
            this.currentByte = this.data.readUnsignedByte();
            return (this.currentByte & PsExtractor.VIDEO_STREAM_MASK) >> 4;
        }

        public boolean isFixedSampleSize() {
            return false;
        }
    }

    private static android.util.Pair<long[], long[]> parseEdts(org.telegram.messenger.exoplayer2.extractor.mp4.Atom.ContainerAtom r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseEdts(org.telegram.messenger.exoplayer2.extractor.mp4.Atom$ContainerAtom):android.util.Pair<long[], long[]>
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        if (r11 == 0) goto L_0x005d;
    L_0x0002:
        r0 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_elst;
        r0 = r11.getLeafAtomOfType(r0);
        r1 = r0;
        if (r0 != 0) goto L_0x000c;
    L_0x000b:
        goto L_0x005d;
        r0 = r1.data;
        r2 = 8;
        r0.setPosition(r2);
        r2 = r0.readInt();
        r3 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.parseFullAtomVersion(r2);
        r4 = r0.readUnsignedIntToInt();
        r5 = new long[r4];
        r6 = new long[r4];
        r7 = 0;
        if (r7 >= r4) goto L_0x0058;
        r8 = 1;
        if (r3 != r8) goto L_0x002f;
        r9 = r0.readUnsignedLongToLong();
        goto L_0x0033;
        r9 = r0.readUnsignedInt();
        r5[r7] = r9;
        if (r3 != r8) goto L_0x003c;
        r9 = r0.readLong();
        goto L_0x0041;
        r9 = r0.readInt();
        r9 = (long) r9;
        r6[r7] = r9;
        r9 = r0.readShort();
        if (r9 == r8) goto L_0x0051;
        r8 = new java.lang.IllegalArgumentException;
        r10 = "Unsupported media rate.";
        r8.<init>(r10);
        throw r8;
        r8 = 2;
        r0.skipBytes(r8);
        r7 = r7 + 1;
        goto L_0x0025;
        r7 = android.util.Pair.create(r5, r6);
        return r7;
    L_0x005d:
        r0 = 0;
        r0 = android.util.Pair.create(r0, r0);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseEdts(org.telegram.messenger.exoplayer2.extractor.mp4.Atom$ContainerAtom):android.util.Pair<long[], long[]>");
    }

    private static android.util.Pair<java.lang.String, byte[]> parseEsdsFromParent(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseEsdsFromParent(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int):android.util.Pair<java.lang.String, byte[]>
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r7 + 8;
        r0 = r0 + 4;
        r6.setPosition(r0);
        r0 = 1;
        r6.skipBytes(r0);
        parseExpandableClassSize(r6);
        r1 = 2;
        r6.skipBytes(r1);
        r2 = r6.readUnsignedByte();
        r3 = r2 & 128;
        if (r3 == 0) goto L_0x001d;
    L_0x001a:
        r6.skipBytes(r1);
    L_0x001d:
        r3 = r2 & 64;
        if (r3 == 0) goto L_0x0028;
    L_0x0021:
        r3 = r6.readUnsignedShort();
        r6.skipBytes(r3);
    L_0x0028:
        r3 = r2 & 32;
        if (r3 == 0) goto L_0x002f;
    L_0x002c:
        r6.skipBytes(r1);
    L_0x002f:
        r6.skipBytes(r0);
        parseExpandableClassSize(r6);
        r1 = r6.readUnsignedByte();
        r3 = 0;
        switch(r1) {
            case 32: goto L_0x0065;
            case 33: goto L_0x0062;
            case 35: goto L_0x005f;
            case 64: goto L_0x005c;
            case 96: goto L_0x0059;
            case 97: goto L_0x0059;
            case 102: goto L_0x005c;
            case 103: goto L_0x005c;
            case 104: goto L_0x005c;
            case 107: goto L_0x0052;
            case 165: goto L_0x004f;
            case 166: goto L_0x004c;
            case 169: goto L_0x0045;
            case 170: goto L_0x003e;
            case 171: goto L_0x003e;
            case 172: goto L_0x0045;
            default: goto L_0x003d;
        };
    L_0x003d:
        goto L_0x0068;
    L_0x003e:
        r0 = "audio/vnd.dts.hd";
        r3 = android.util.Pair.create(r0, r3);
        return r3;
    L_0x0045:
        r0 = "audio/vnd.dts";
        r3 = android.util.Pair.create(r0, r3);
        return r3;
    L_0x004c:
        r3 = "audio/eac3";
        goto L_0x0068;
    L_0x004f:
        r3 = "audio/ac3";
        goto L_0x0068;
    L_0x0052:
        r0 = "audio/mpeg";
        r3 = android.util.Pair.create(r0, r3);
        return r3;
    L_0x0059:
        r3 = "video/mpeg2";
        goto L_0x0068;
    L_0x005c:
        r3 = "audio/mp4a-latm";
        goto L_0x0068;
    L_0x005f:
        r3 = "video/hevc";
        goto L_0x0068;
    L_0x0062:
        r3 = "video/avc";
        goto L_0x0068;
    L_0x0065:
        r3 = "video/mp4v-es";
        r4 = 12;
        r6.skipBytes(r4);
        r6.skipBytes(r0);
        r0 = parseExpandableClassSize(r6);
        r4 = new byte[r0];
        r5 = 0;
        r6.readBytes(r4, r5, r0);
        r5 = android.util.Pair.create(r3, r4);
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseEsdsFromParent(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int):android.util.Pair<java.lang.String, byte[]>");
    }

    public static org.telegram.messenger.exoplayer2.extractor.mp4.TrackSampleTable parseStbl(org.telegram.messenger.exoplayer2.extractor.mp4.Track r1, org.telegram.messenger.exoplayer2.extractor.mp4.Atom.ContainerAtom r2, org.telegram.messenger.exoplayer2.extractor.GaplessInfoHolder r3) throws org.telegram.messenger.exoplayer2.ParserException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseStbl(org.telegram.messenger.exoplayer2.extractor.mp4.Track, org.telegram.messenger.exoplayer2.extractor.mp4.Atom$ContainerAtom, org.telegram.messenger.exoplayer2.extractor.GaplessInfoHolder):org.telegram.messenger.exoplayer2.extractor.mp4.TrackSampleTable
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r86;
        r1 = r87;
        r2 = r88;
        r3 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_stsz;
        r3 = r1.getLeafAtomOfType(r3);
        if (r3 == 0) goto L_0x0014;
    L_0x000e:
        r4 = new org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers$StszSampleSizeBox;
        r4.<init>(r3);
        goto L_0x002a;
    L_0x0014:
        r4 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_stz2;
        r4 = r1.getLeafAtomOfType(r4);
        if (r4 != 0) goto L_0x0024;
    L_0x001c:
        r5 = new org.telegram.messenger.exoplayer2.ParserException;
        r6 = "Track has no sample table size information";
        r5.<init>(r6);
        throw r5;
    L_0x0024:
        r5 = new org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers$Stz2SampleSizeBox;
        r5.<init>(r4);
        r4 = r5;
    L_0x002a:
        r5 = r4.getSampleCount();
        r6 = 0;
        if (r5 != 0) goto L_0x0041;
    L_0x0031:
        r13 = new org.telegram.messenger.exoplayer2.extractor.mp4.TrackSampleTable;
        r8 = new long[r6];
        r9 = new int[r6];
        r10 = 0;
        r11 = new long[r6];
        r12 = new int[r6];
        r7 = r13;
        r7.<init>(r8, r9, r10, r11, r12);
        return r13;
    L_0x0041:
        r7 = 0;
        r8 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_stco;
        r8 = r1.getLeafAtomOfType(r8);
        if (r8 != 0) goto L_0x0051;
    L_0x004a:
        r7 = 1;
        r9 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_co64;
        r8 = r1.getLeafAtomOfType(r9);
    L_0x0051:
        r9 = r8.data;
        r10 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_stsc;
        r10 = r1.getLeafAtomOfType(r10);
        r10 = r10.data;
        r11 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_stts;
        r11 = r1.getLeafAtomOfType(r11);
        r11 = r11.data;
        r12 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_stss;
        r12 = r1.getLeafAtomOfType(r12);
        r13 = 0;
        if (r12 == 0) goto L_0x006f;
    L_0x006c:
        r14 = r12.data;
        goto L_0x0070;
    L_0x006f:
        r14 = r13;
    L_0x0070:
        r15 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_ctts;
        r15 = r1.getLeafAtomOfType(r15);
        if (r15 == 0) goto L_0x007b;
    L_0x0078:
        r13 = r15.data;
    L_0x007b:
        r6 = new org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers$ChunkIterator;
        r6.<init>(r10, r9, r7);
        r1 = 12;
        r11.setPosition(r1);
        r17 = r11.readUnsignedIntToInt();
        r1 = 1;
        r17 = r17 + -1;
        r19 = r11.readUnsignedIntToInt();
        r1 = r11.readUnsignedIntToInt();
        r21 = 0;
        r22 = 0;
        r23 = 0;
        if (r13 == 0) goto L_0x00a8;
    L_0x009c:
        r24 = r3;
        r3 = 12;
        r13.setPosition(r3);
        r22 = r13.readUnsignedIntToInt();
        goto L_0x00aa;
    L_0x00a8:
        r24 = r3;
    L_0x00aa:
        r3 = -1;
        r25 = 0;
        if (r14 == 0) goto L_0x00c9;
    L_0x00af:
        r26 = r3;
        r3 = 12;
        r14.setPosition(r3);
        r25 = r14.readUnsignedIntToInt();
        if (r25 <= 0) goto L_0x00c7;
    L_0x00bc:
        r3 = r14.readUnsignedIntToInt();
        r18 = 1;
        r3 = r3 + -1;
        r26 = r3;
        goto L_0x00cb;
    L_0x00c7:
        r14 = 0;
        goto L_0x00cb;
    L_0x00c9:
        r26 = r3;
    L_0x00cb:
        r3 = r4.isFixedSampleSize();
        if (r3 == 0) goto L_0x00e7;
    L_0x00d1:
        r3 = "audio/raw";
        r27 = r7;
        r7 = r0.format;
        r7 = r7.sampleMimeType;
        r3 = r3.equals(r7);
        if (r3 == 0) goto L_0x00e9;
    L_0x00df:
        if (r17 != 0) goto L_0x00e9;
    L_0x00e1:
        if (r22 != 0) goto L_0x00e9;
    L_0x00e3:
        if (r25 != 0) goto L_0x00e9;
    L_0x00e5:
        r3 = 1;
        goto L_0x00ea;
    L_0x00e7:
        r27 = r7;
    L_0x00e9:
        r3 = 0;
    L_0x00ea:
        r7 = 0;
        r28 = 0;
        if (r3 != 0) goto L_0x023f;
    L_0x00ef:
        r30 = r3;
        r3 = new long[r5];
        r31 = r7;
        r7 = new int[r5];
        r32 = r8;
        r8 = new long[r5];
        r33 = r9;
        r9 = new int[r5];
        r34 = 0;
        r18 = 0;
        r36 = r10;
        r42 = r11;
        r37 = r12;
        r38 = r15;
        r11 = r17;
        r41 = r18;
        r0 = r19;
        r2 = r25;
        r15 = r26;
        r12 = r31;
        r10 = r1;
        r1 = 0;
    L_0x0119:
        if (r1 >= r5) goto L_0x01b9;
    L_0x011b:
        if (r41 != 0) goto L_0x0139;
    L_0x011d:
        r43 = r5;
        r5 = r6.moveNext();
        org.telegram.messenger.exoplayer2.util.Assertions.checkState(r5);
        r44 = r10;
        r45 = r11;
        r10 = r6.offset;
        r5 = r6.numSamples;
        r41 = r5;
        r34 = r10;
        r5 = r43;
        r10 = r44;
        r11 = r45;
        goto L_0x011b;
    L_0x0139:
        r43 = r5;
        r44 = r10;
        r45 = r11;
        if (r13 == 0) goto L_0x0152;
    L_0x0141:
        if (r21 != 0) goto L_0x0150;
    L_0x0143:
        if (r22 <= 0) goto L_0x0150;
    L_0x0145:
        r21 = r13.readUnsignedIntToInt();
        r23 = r13.readInt();
        r22 = r22 + -1;
        goto L_0x0141;
    L_0x0150:
        r21 = r21 + -1;
    L_0x0152:
        r5 = r23;
        r3[r1] = r34;
        r10 = r4.readNextSampleSize();
        r7[r1] = r10;
        r10 = r7[r1];
        if (r10 <= r12) goto L_0x0163;
    L_0x0160:
        r10 = r7[r1];
        r12 = r10;
    L_0x0163:
        r10 = (long) r5;
        r17 = r28 + r10;
        r8[r1] = r17;
        if (r14 != 0) goto L_0x016c;
    L_0x016a:
        r10 = 1;
        goto L_0x016d;
    L_0x016c:
        r10 = 0;
    L_0x016d:
        r9[r1] = r10;
        if (r1 != r15) goto L_0x017e;
    L_0x0171:
        r10 = 1;
        r9[r1] = r10;
        r2 = r2 + -1;
        if (r2 <= 0) goto L_0x017e;
    L_0x0178:
        r11 = r14.readUnsignedIntToInt();
        r15 = r11 + -1;
    L_0x017e:
        r47 = r2;
        r46 = r3;
        r10 = r44;
        r2 = (long) r10;
        r17 = r28 + r2;
        r0 = r0 + -1;
        if (r0 != 0) goto L_0x019a;
    L_0x018b:
        if (r45 <= 0) goto L_0x019a;
    L_0x018d:
        r3 = r42;
        r0 = r3.readUnsignedIntToInt();
        r10 = r3.readInt();
        r11 = r45 + -1;
        goto L_0x019e;
    L_0x019a:
        r3 = r42;
        r11 = r45;
    L_0x019e:
        r2 = r7[r1];
        r48 = r3;
        r2 = (long) r2;
        r25 = r34 + r2;
        r41 = r41 + -1;
        r1 = r1 + 1;
        r23 = r5;
        r28 = r17;
        r34 = r25;
        r5 = r43;
        r3 = r46;
        r2 = r47;
        r42 = r48;
        goto L_0x0119;
    L_0x01b9:
        r46 = r3;
        r43 = r5;
        r45 = r11;
        r48 = r42;
        if (r21 != 0) goto L_0x01c5;
    L_0x01c3:
        r1 = 1;
        goto L_0x01c6;
    L_0x01c5:
        r1 = 0;
    L_0x01c6:
        org.telegram.messenger.exoplayer2.util.Assertions.checkArgument(r1);
    L_0x01c9:
        if (r22 <= 0) goto L_0x01dd;
    L_0x01cb:
        r1 = r13.readUnsignedIntToInt();
        if (r1 != 0) goto L_0x01d3;
    L_0x01d1:
        r1 = 1;
        goto L_0x01d4;
    L_0x01d3:
        r1 = 0;
    L_0x01d4:
        org.telegram.messenger.exoplayer2.util.Assertions.checkArgument(r1);
        r13.readInt();
        r22 = r22 + -1;
        goto L_0x01c9;
    L_0x01dd:
        if (r2 != 0) goto L_0x01ee;
    L_0x01df:
        if (r0 != 0) goto L_0x01ee;
    L_0x01e1:
        if (r41 != 0) goto L_0x01ee;
    L_0x01e3:
        if (r45 == 0) goto L_0x01e6;
    L_0x01e5:
        goto L_0x01ee;
    L_0x01e6:
        r5 = r0;
        r49 = r2;
        r2 = r45;
        r0 = r86;
        goto L_0x022f;
    L_0x01ee:
        r1 = "AtomParsers";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r5 = "Inconsistent stbl box for track ";
        r3.append(r5);
        r5 = r0;
        r0 = r86;
        r11 = r0.id;
        r3.append(r11);
        r11 = ": remainingSynchronizationSamples ";
        r3.append(r11);
        r3.append(r2);
        r11 = ", remainingSamplesAtTimestampDelta ";
        r3.append(r11);
        r3.append(r5);
        r11 = ", remainingSamplesInChunk ";
        r3.append(r11);
        r11 = r41;
        r3.append(r11);
        r49 = r2;
        r2 = ", remainingTimestampDeltaChanges ";
        r3.append(r2);
        r2 = r45;
        r3.append(r2);
        r3 = r3.toString();
        android.util.Log.w(r1, r3);
        r17 = r2;
        r19 = r5;
        r5 = r7;
        r1 = r8;
        r2 = r9;
        r18 = r10;
        r26 = r15;
        r3 = r46;
        r15 = r12;
        goto L_0x0287;
    L_0x023f:
        r30 = r3;
        r43 = r5;
        r31 = r7;
        r32 = r8;
        r33 = r9;
        r36 = r10;
        r48 = r11;
        r37 = r12;
        r38 = r15;
        r2 = r6.length;
        r2 = new long[r2];
        r3 = r6.length;
        r3 = new int[r3];
        r5 = r6.moveNext();
        if (r5 == 0) goto L_0x026c;
        r5 = r6.index;
        r7 = r6.offset;
        r2[r5] = r7;
        r5 = r6.index;
        r7 = r6.numSamples;
        r3[r5] = r7;
        goto L_0x0259;
        r5 = r4.readNextSampleSize();
        r7 = (long) r1;
        r7 = org.telegram.messenger.exoplayer2.extractor.mp4.FixedSampleSizeRechunker.rechunk(r5, r2, r3, r7);
        r8 = r7.offsets;
        r9 = r7.sizes;
        r10 = r7.maximumSize;
        r11 = r7.timestamps;
        r2 = r7.flags;
        r18 = r1;
        r3 = r8;
        r5 = r9;
        r15 = r10;
        r1 = r11;
        r49 = r25;
        r7 = r0.editListDurations;
        if (r7 == 0) goto L_0x0571;
        r12 = r88;
        r7 = r88.hasGaplessInfo();
        if (r7 == 0) goto L_0x02a5;
        r80 = r2;
        r2 = r3;
        r64 = r4;
        r68 = r6;
        r60 = r13;
        r61 = r14;
        r71 = r15;
        r75 = r43;
        r13 = r5;
        goto L_0x0581;
        r7 = r0.editListDurations;
        r7 = r7.length;
        r8 = 1;
        if (r7 != r8) goto L_0x0345;
        r7 = r0.type;
        if (r7 != r8) goto L_0x0345;
        r7 = r1.length;
        r8 = 2;
        if (r7 < r8) goto L_0x0345;
        r7 = r0.editListMediaTimes;
        r8 = 0;
        r34 = r7[r8];
        r7 = r0.editListDurations;
        r52 = r7[r8];
        r10 = r0.timescale;
        r8 = r0.movieTimescale;
        r54 = r10;
        r56 = r8;
        r7 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r52, r54, r56);
        r39 = r34 + r7;
        r41 = r28;
        r7 = 0;
        r8 = r1[r7];
        r7 = (r8 > r34 ? 1 : (r8 == r34 ? 0 : -1));
        if (r7 > 0) goto L_0x0345;
        r7 = 1;
        r8 = r1[r7];
        r10 = (r34 > r8 ? 1 : (r34 == r8 ? 0 : -1));
        if (r10 >= 0) goto L_0x0345;
        r8 = r1.length;
        r8 = r8 - r7;
        r7 = r1[r8];
        r9 = (r7 > r39 ? 1 : (r7 == r39 ? 0 : -1));
        if (r9 >= 0) goto L_0x0345;
        r7 = (r39 > r41 ? 1 : (r39 == r41 ? 0 : -1));
        if (r7 > 0) goto L_0x0345;
        r44 = r41 - r39;
        r7 = 0;
        r8 = r1[r7];
        r52 = r34 - r8;
        r7 = r0.format;
        r7 = r7.sampleRate;
        r7 = (long) r7;
        r9 = r0.timescale;
        r54 = r7;
        r56 = r9;
        r10 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r52, r54, r56);
        r7 = r0.format;
        r7 = r7.sampleRate;
        r7 = (long) r7;
        r60 = r13;
        r61 = r14;
        r13 = r0.timescale;
        r52 = r44;
        r54 = r7;
        r56 = r13;
        r13 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r52, r54, r56);
        r7 = 0;
        r9 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1));
        if (r9 != 0) goto L_0x031b;
        r9 = (r13 > r7 ? 1 : (r13 == r7 ? 0 : -1));
        if (r9 == 0) goto L_0x0349;
        r7 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r9 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1));
        if (r9 > 0) goto L_0x0349;
        r9 = (r13 > r7 ? 1 : (r13 == r7 ? 0 : -1));
        if (r9 > 0) goto L_0x0349;
        r7 = (int) r10;
        r12.encoderDelay = r7;
        r7 = (int) r13;
        r12.encoderPadding = r7;
        r7 = r0.timescale;
        r62 = r10;
        r9 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestampsInPlace(r1, r9, r7);
        r16 = new org.telegram.messenger.exoplayer2.extractor.mp4.TrackSampleTable;
        r7 = r16;
        r8 = r3;
        r9 = r5;
        r46 = r62;
        r10 = r15;
        r11 = r1;
        r12 = r2;
        r7.<init>(r8, r9, r10, r11, r12);
        return r16;
        r60 = r13;
        r61 = r14;
        r7 = r0.editListDurations;
        r7 = r7.length;
        r8 = 1;
        if (r7 != r8) goto L_0x0383;
        r7 = r0.editListDurations;
        r8 = 0;
        r9 = r7[r8];
        r7 = 0;
        r11 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
        if (r11 != 0) goto L_0x0383;
        r7 = 0;
        r8 = r1.length;
        if (r7 >= r8) goto L_0x0377;
        r8 = r1[r7];
        r10 = r0.editListMediaTimes;
        r11 = 0;
        r12 = r10[r11];
        r50 = r8 - r12;
        r52 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r8 = r0.timescale;
        r54 = r8;
        r8 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r50, r52, r54);
        r1[r7] = r8;
        r7 = r7 + 1;
        goto L_0x035b;
        r13 = new org.telegram.messenger.exoplayer2.extractor.mp4.TrackSampleTable;
        r7 = r13;
        r8 = r3;
        r9 = r5;
        r10 = r15;
        r11 = r1;
        r12 = r2;
        r7.<init>(r8, r9, r10, r11, r12);
        return r13;
        r7 = r0.type;
        r8 = 1;
        if (r7 != r8) goto L_0x038a;
        r7 = 1;
        goto L_0x038b;
        r7 = 0;
        r13 = r7;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r14 = r7;
        r12 = r8;
        r7 = 0;
        r8 = r0.editListDurations;
        r10 = -1;
        r8 = r8.length;
        if (r7 >= r8) goto L_0x03eb;
        r8 = r0.editListMediaTimes;
        r64 = r4;
        r65 = r5;
        r4 = r8[r7];
        r8 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r8 == 0) goto L_0x03d6;
        r8 = r0.editListDurations;
        r52 = r8[r7];
        r10 = r0.timescale;
        r67 = r2;
        r66 = r3;
        r2 = r0.movieTimescale;
        r54 = r10;
        r56 = r2;
        r2 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r52, r54, r56);
        r8 = 1;
        r10 = org.telegram.messenger.exoplayer2.util.Util.binarySearchCeil(r1, r4, r8, r8);
        r68 = r6;
        r69 = r7;
        r6 = r4 + r2;
        r8 = 0;
        r6 = org.telegram.messenger.exoplayer2.util.Util.binarySearchCeil(r1, r6, r13, r8);
        r7 = r6 - r10;
        r14 = r14 + r7;
        if (r12 == r10) goto L_0x03d0;
        r7 = 1;
        goto L_0x03d1;
        r7 = 0;
        r7 = r7 | r9;
        r2 = r6;
        r12 = r2;
        r9 = r7;
        goto L_0x03de;
        r67 = r2;
        r66 = r3;
        r68 = r6;
        r69 = r7;
        r7 = r69 + 1;
        r4 = r64;
        r5 = r65;
        r3 = r66;
        r2 = r67;
        r6 = r68;
        goto L_0x0392;
        r67 = r2;
        r66 = r3;
        r64 = r4;
        r65 = r5;
        r68 = r6;
        r2 = r43;
        if (r14 == r2) goto L_0x03fb;
        r3 = 1;
        goto L_0x03fc;
        r3 = 0;
        r3 = r3 | r9;
        if (r3 == 0) goto L_0x0402;
        r4 = new long[r14];
        goto L_0x0404;
        r4 = r66;
        if (r3 == 0) goto L_0x0409;
        r5 = new int[r14];
        goto L_0x040b;
        r5 = r65;
        if (r3 == 0) goto L_0x040f;
        r6 = 0;
        goto L_0x0410;
        r6 = r15;
        if (r3 == 0) goto L_0x0415;
        r7 = new int[r14];
        goto L_0x0417;
        r7 = r67;
        r9 = r7;
        r8 = new long[r14];
        r34 = 0;
        r7 = 0;
        r25 = r6;
        r6 = 0;
        r10 = r0.editListDurations;
        r10 = r10.length;
        if (r6 >= r10) goto L_0x0502;
        r10 = r0.editListMediaTimes;
        r70 = r12;
        r11 = r10[r6];
        r10 = r0.editListDurations;
        r45 = r10[r6];
        r52 = -1;
        r10 = (r11 > r52 ? 1 : (r11 == r52 ? 0 : -1));
        if (r10 == 0) goto L_0x04d0;
        r72 = r14;
        r71 = r15;
        r14 = r0.timescale;
        r74 = r8;
        r73 = r9;
        r8 = r0.movieTimescale;
        r39 = r45;
        r41 = r14;
        r43 = r8;
        r8 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r39, r41, r43);
        r14 = r11 + r8;
        r8 = 1;
        r9 = org.telegram.messenger.exoplayer2.util.Util.binarySearchCeil(r1, r11, r8, r8);
        r8 = 0;
        r10 = org.telegram.messenger.exoplayer2.util.Util.binarySearchCeil(r1, r14, r13, r8);
        if (r3 == 0) goto L_0x0473;
        r8 = r10 - r9;
        r75 = r2;
        r2 = r66;
        java.lang.System.arraycopy(r2, r9, r4, r7, r8);
        r76 = r13;
        r13 = r65;
        java.lang.System.arraycopy(r13, r9, r5, r7, r8);
        r77 = r14;
        r14 = r67;
        r15 = r73;
        java.lang.System.arraycopy(r14, r9, r15, r7, r8);
        goto L_0x0481;
        r75 = r2;
        r76 = r13;
        r77 = r14;
        r13 = r65;
        r2 = r66;
        r14 = r67;
        r15 = r73;
        r8 = r7;
        r7 = r9;
        r79 = r7;
        r9 = r25;
        if (r7 >= r10) goto L_0x04c6;
        r41 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r80 = r14;
        r81 = r15;
        r14 = r0.movieTimescale;
        r39 = r34;
        r43 = r14;
        r14 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r39, r41, r43);
        r39 = r1[r7];
        r54 = r39 - r11;
        r56 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r84 = r10;
        r82 = r11;
        r10 = r0.timescale;
        r58 = r10;
        r10 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r54, r56, r58);
        r39 = r14 + r10;
        r74[r8] = r39;
        if (r3 == 0) goto L_0x04b9;
        r12 = r5[r8];
        if (r12 <= r9) goto L_0x04b9;
        r9 = r13[r7];
        r8 = r8 + 1;
        r7 = r7 + 1;
        r14 = r80;
        r15 = r81;
        r11 = r82;
        r10 = r84;
        goto L_0x0487;
        r82 = r11;
        r80 = r14;
        r81 = r15;
        r7 = r8;
        r25 = r9;
        goto L_0x04e4;
        r75 = r2;
        r74 = r8;
        r81 = r9;
        r82 = r11;
        r76 = r13;
        r72 = r14;
        r71 = r15;
        r13 = r65;
        r2 = r66;
        r80 = r67;
        r8 = r34 + r45;
        r6 = r6 + 1;
        r66 = r2;
        r34 = r8;
        r65 = r13;
        r10 = r52;
        r12 = r70;
        r15 = r71;
        r14 = r72;
        r8 = r74;
        r2 = r75;
        r13 = r76;
        r67 = r80;
        r9 = r81;
        goto L_0x0420;
        r75 = r2;
        r74 = r8;
        r81 = r9;
        r70 = r12;
        r76 = r13;
        r72 = r14;
        r71 = r15;
        r13 = r65;
        r2 = r66;
        r80 = r67;
        r6 = 0;
        r14 = r6;
        r6 = 0;
        r9 = r81;
        r8 = r9.length;
        if (r6 >= r8) goto L_0x052f;
        if (r14 != 0) goto L_0x052f;
        r8 = r9[r6];
        r10 = 1;
        r8 = r8 & r10;
        if (r8 == 0) goto L_0x0528;
        r8 = r10;
        goto L_0x0529;
        r8 = 0;
        r14 = r14 | r8;
        r6 = r6 + 1;
        r81 = r9;
        goto L_0x0519;
        if (r14 != 0) goto L_0x0558;
        r6 = "AtomParsers";
        r8 = "Ignoring edit list: Edited sample sequence does not contain a sync sample.";
        android.util.Log.w(r6, r8);
        r10 = r0.timescale;
        r85 = r7;
        r6 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestampsInPlace(r1, r6, r10);
        r6 = new org.telegram.messenger.exoplayer2.extractor.mp4.TrackSampleTable;
        r15 = r85;
        r7 = r6;
        r16 = r74;
        r8 = r2;
        r20 = r9;
        r9 = r13;
        r10 = r71;
        r11 = r1;
        r31 = r70;
        r12 = r80;
        r7.<init>(r8, r9, r10, r11, r12);
        return r6;
        r15 = r7;
        r20 = r9;
        r31 = r70;
        r16 = r74;
        r6 = new org.telegram.messenger.exoplayer2.extractor.mp4.TrackSampleTable;
        r39 = r6;
        r40 = r4;
        r41 = r5;
        r42 = r25;
        r43 = r16;
        r44 = r20;
        r39.<init>(r40, r41, r42, r43, r44);
        return r6;
        r80 = r2;
        r2 = r3;
        r64 = r4;
        r68 = r6;
        r60 = r13;
        r61 = r14;
        r71 = r15;
        r75 = r43;
        r13 = r5;
        r3 = r0.timescale;
        r5 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestampsInPlace(r1, r5, r3);
        r3 = new org.telegram.messenger.exoplayer2.extractor.mp4.TrackSampleTable;
        r7 = r3;
        r8 = r2;
        r9 = r13;
        r10 = r71;
        r11 = r1;
        r12 = r80;
        r7.<init>(r8, r9, r10, r11, r12);
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseStbl(org.telegram.messenger.exoplayer2.extractor.mp4.Track, org.telegram.messenger.exoplayer2.extractor.mp4.Atom$ContainerAtom, org.telegram.messenger.exoplayer2.extractor.GaplessInfoHolder):org.telegram.messenger.exoplayer2.extractor.mp4.TrackSampleTable");
    }

    private static void parseTextSampleEntry(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1, int r2, int r3, int r4, int r5, java.lang.String r6, org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.StsdData r7) throws org.telegram.messenger.exoplayer2.ParserException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseTextSampleEntry(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int, int, int, int, java.lang.String, org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers$StsdData):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r19;
        r1 = r20;
        r2 = r25;
        r3 = r21 + 8;
        r3 = r3 + 8;
        r0.setPosition(r3);
        r3 = 0;
        r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r6 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_TTML;
        if (r1 != r6) goto L_0x001b;
    L_0x0017:
        r6 = "application/ttml+xml";
        r9 = r6;
        goto L_0x004a;
    L_0x001b:
        r6 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_tx3g;
        if (r1 != r6) goto L_0x0030;
        r6 = "application/x-quicktime-tx3g";
        r7 = r22 + -8;
        r7 = r7 + -8;
        r8 = new byte[r7];
        r9 = 0;
        r0.readBytes(r8, r9, r7);
        r3 = java.util.Collections.singletonList(r8);
        goto L_0x0019;
        r6 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_wvtt;
        if (r1 != r6) goto L_0x0037;
        r6 = "application/x-mp4-vtt";
        goto L_0x0019;
        r6 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_stpp;
        if (r1 != r6) goto L_0x0040;
        r6 = "application/ttml+xml";
        r4 = 0;
        goto L_0x0019;
        r6 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_c608;
        if (r1 != r6) goto L_0x0061;
        r6 = "application/x-mp4-cea-608";
        r7 = 1;
        r2.requiredSampleTransformation = r7;
        goto L_0x0019;
        r8 = java.lang.Integer.toString(r23);
        r10 = 0;
        r11 = -1;
        r12 = 0;
        r14 = -1;
        r15 = 0;
        r13 = r24;
        r16 = r4;
        r18 = r3;
        r6 = org.telegram.messenger.exoplayer2.Format.createTextSampleFormat(r8, r9, r10, r11, r12, r13, r14, r15, r16, r18);
        r2.format = r6;
        return;
        r6 = new java.lang.IllegalStateException;
        r6.<init>();
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseTextSampleEntry(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int, int, int, int, java.lang.String, org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers$StsdData):void");
    }

    private static org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.TkhdData parseTkhd(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseTkhd(org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers$TkhdData
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 8;
        r15.setPosition(r0);
        r1 = r15.readInt();
        r2 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.parseFullAtomVersion(r1);
        r3 = 16;
        if (r2 != 0) goto L_0x0013;
    L_0x0011:
        r4 = r0;
        goto L_0x0014;
    L_0x0013:
        r4 = r3;
    L_0x0014:
        r15.skipBytes(r4);
        r4 = r15.readInt();
        r5 = 4;
        r15.skipBytes(r5);
        r6 = 1;
        r7 = r15.getPosition();
        if (r2 != 0) goto L_0x0028;
    L_0x0026:
        r0 = r5;
    L_0x0028:
        r8 = 0;
        r9 = r8;
    L_0x002a:
        if (r9 >= r0) goto L_0x003a;
    L_0x002c:
        r10 = r15.data;
        r11 = r7 + r9;
        r10 = r10[r11];
        r11 = -1;
        if (r10 == r11) goto L_0x0037;
    L_0x0035:
        r6 = 0;
        goto L_0x003a;
    L_0x0037:
        r9 = r9 + 1;
        goto L_0x002a;
    L_0x003a:
        if (r6 == 0) goto L_0x0045;
    L_0x003c:
        r15.skipBytes(r0);
        r9 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        goto L_0x005b;
    L_0x0045:
        if (r2 != 0) goto L_0x004c;
    L_0x0047:
        r9 = r15.readUnsignedInt();
        goto L_0x0050;
    L_0x004c:
        r9 = r15.readUnsignedLongToLong();
    L_0x0050:
        r11 = 0;
        r13 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));
        if (r13 != 0) goto L_0x005b;
    L_0x0056:
        r9 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
    L_0x005b:
        r15.skipBytes(r3);
        r3 = r15.readInt();
        r11 = r15.readInt();
        r15.skipBytes(r5);
        r5 = r15.readInt();
        r12 = r15.readInt();
        r13 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        if (r3 != 0) goto L_0x007f;
    L_0x0075:
        if (r11 != r13) goto L_0x007f;
    L_0x0077:
        r14 = -r13;
        if (r5 != r14) goto L_0x007f;
    L_0x007a:
        if (r12 != 0) goto L_0x007f;
    L_0x007c:
        r8 = 90;
    L_0x007e:
        goto L_0x0099;
    L_0x007f:
        if (r3 != 0) goto L_0x008b;
    L_0x0081:
        r14 = -r13;
        if (r11 != r14) goto L_0x008b;
    L_0x0084:
        if (r5 != r13) goto L_0x008b;
    L_0x0086:
        if (r12 != 0) goto L_0x008b;
    L_0x0088:
        r8 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        goto L_0x007e;
    L_0x008b:
        r14 = -r13;
        if (r3 != r14) goto L_0x0098;
    L_0x008e:
        if (r11 != 0) goto L_0x0098;
        if (r5 != 0) goto L_0x0098;
        r14 = -r13;
        if (r12 != r14) goto L_0x0098;
        r8 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        goto L_0x007e;
    L_0x0099:
        r14 = new org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers$TkhdData;
        r14.<init>(r4, r9, r8);
        return r14;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers.parseTkhd(org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.extractor.mp4.AtomParsers$TkhdData");
    }

    public static Track parseTrak(ContainerAtom trak, LeafAtom mvhd, long duration, DrmInitData drmInitData, boolean ignoreEditLists, boolean isQuickTime) throws ParserException {
        ContainerAtom containerAtom = trak;
        ContainerAtom mdia = containerAtom.getContainerAtomOfType(Atom.TYPE_mdia);
        int trackType = parseHdlr(mdia.getLeafAtomOfType(Atom.TYPE_hdlr).data);
        if (trackType == -1) {
            return null;
        }
        long duration2;
        long j;
        Track track;
        TkhdData tkhdData = parseTkhd(containerAtom.getLeafAtomOfType(Atom.TYPE_tkhd).data);
        if (duration == C.TIME_UNSET) {
            duration2 = tkhdData.duration;
        } else {
            duration2 = duration;
        }
        long movieTimescale = parseMvhd(mvhd.data);
        if (duration2 == C.TIME_UNSET) {
            j = C.TIME_UNSET;
        } else {
            j = Util.scaleLargeTimestamp(duration2, C.MICROS_PER_SECOND, movieTimescale);
        }
        long durationUs = j;
        ContainerAtom stbl = mdia.getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
        Pair<Long, String> mdhdData = parseMdhd(mdia.getLeafAtomOfType(Atom.TYPE_mdhd).data);
        StsdData stsdData = parseStsd(stbl.getLeafAtomOfType(Atom.TYPE_stsd).data, tkhdData.id, tkhdData.rotationDegrees, (String) mdhdData.second, drmInitData, isQuickTime);
        long[] editListDurations = null;
        long[] editListMediaTimes = null;
        if (!ignoreEditLists) {
            Pair<long[], long[]> edtsData = parseEdts(containerAtom.getContainerAtomOfType(Atom.TYPE_edts));
            editListDurations = (long[]) edtsData.first;
            editListMediaTimes = (long[]) edtsData.second;
        }
        long[] editListDurations2 = editListDurations;
        long[] editListMediaTimes2 = editListMediaTimes;
        if (stsdData.format == null) {
            track = null;
            StsdData stsdData2 = stsdData;
            Pair<Long, String> pair = mdhdData;
            ContainerAtom containerAtom2 = stbl;
            TkhdData tkhdData2 = tkhdData;
        } else {
            int access$100 = tkhdData.id;
            long longValue = ((Long) mdhdData.first).longValue();
            Format format = stsdData.format;
            int i = stsdData.requiredSampleTransformation;
            int i2 = i;
            Format format2 = format;
            Track track2 = new Track(access$100, trackType, longValue, movieTimescale, durationUs, format2, i2, stsdData.trackEncryptionBoxes, stsdData.nalUnitLengthFieldLength, editListDurations2, editListMediaTimes2);
        }
        return track;
    }

    public static Metadata parseUdta(LeafAtom udtaAtom, boolean isQuickTime) {
        if (isQuickTime) {
            return null;
        }
        ParsableByteArray udtaData = udtaAtom.data;
        udtaData.setPosition(8);
        while (udtaData.bytesLeft() >= 8) {
            int atomPosition = udtaData.getPosition();
            int atomSize = udtaData.readInt();
            if (udtaData.readInt() == Atom.TYPE_meta) {
                udtaData.setPosition(atomPosition);
                return parseMetaAtom(udtaData, atomPosition + atomSize);
            }
            udtaData.skipBytes(atomSize - 8);
        }
        return null;
    }

    private static Metadata parseMetaAtom(ParsableByteArray meta, int limit) {
        meta.skipBytes(12);
        while (meta.getPosition() < limit) {
            int atomPosition = meta.getPosition();
            int atomSize = meta.readInt();
            if (meta.readInt() == Atom.TYPE_ilst) {
                meta.setPosition(atomPosition);
                return parseIlst(meta, atomPosition + atomSize);
            }
            meta.skipBytes(atomSize - 8);
        }
        return null;
    }

    private static Metadata parseIlst(ParsableByteArray ilst, int limit) {
        ilst.skipBytes(8);
        List entries = new ArrayList();
        while (ilst.getPosition() < limit) {
            Entry entry = MetadataUtil.parseIlstElement(ilst);
            if (entry != null) {
                entries.add(entry);
            }
        }
        return entries.isEmpty() ? null : new Metadata(entries);
    }

    private static long parseMvhd(ParsableByteArray mvhd) {
        int i = 8;
        mvhd.setPosition(8);
        if (Atom.parseFullAtomVersion(mvhd.readInt()) != 0) {
            i = 16;
        }
        mvhd.skipBytes(i);
        return mvhd.readUnsignedInt();
    }

    private static int parseHdlr(ParsableByteArray hdlr) {
        hdlr.setPosition(16);
        int trackType = hdlr.readInt();
        if (trackType == TYPE_soun) {
            return 1;
        }
        if (trackType == TYPE_vide) {
            return 2;
        }
        if (!(trackType == TYPE_text || trackType == TYPE_sbtl || trackType == TYPE_subt)) {
            if (trackType != TYPE_clcp) {
                if (trackType == TYPE_meta) {
                    return 4;
                }
                return -1;
            }
        }
        return 3;
    }

    private static Pair<Long, String> parseMdhd(ParsableByteArray mdhd) {
        int i = 8;
        mdhd.setPosition(8);
        int version = Atom.parseFullAtomVersion(mdhd.readInt());
        mdhd.skipBytes(version == 0 ? 8 : 16);
        long timescale = mdhd.readUnsignedInt();
        if (version == 0) {
            i = 4;
        }
        mdhd.skipBytes(i);
        i = mdhd.readUnsignedShort();
        String language = new StringBuilder();
        language.append(TtmlNode.ANONYMOUS_REGION_ID);
        language.append((char) (((i >> 10) & 31) + 96));
        language.append((char) (((i >> 5) & 31) + 96));
        language.append((char) ((i & 31) + 96));
        return Pair.create(Long.valueOf(timescale), language.toString());
    }

    private static StsdData parseStsd(ParsableByteArray stsd, int trackId, int rotationDegrees, String language, DrmInitData drmInitData, boolean isQuickTime) throws ParserException {
        ParsableByteArray parsableByteArray = stsd;
        parsableByteArray.setPosition(12);
        int numberOfEntries = stsd.readInt();
        StsdData out = new StsdData(numberOfEntries);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= numberOfEntries) {
                return out;
            }
            int childAtomType;
            int childStartPosition = stsd.getPosition();
            int childAtomSize = stsd.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType2 = stsd.readInt();
            if (childAtomType2 == Atom.TYPE_avc1 || childAtomType2 == Atom.TYPE_avc3 || childAtomType2 == Atom.TYPE_encv || childAtomType2 == Atom.TYPE_mp4v || childAtomType2 == Atom.TYPE_hvc1 || childAtomType2 == Atom.TYPE_hev1 || childAtomType2 == Atom.TYPE_s263 || childAtomType2 == Atom.TYPE_vp08) {
                childAtomType = childAtomType2;
            } else if (childAtomType2 == Atom.TYPE_vp09) {
                childAtomType = childAtomType2;
            } else {
                if (!(childAtomType2 == Atom.TYPE_mp4a || childAtomType2 == Atom.TYPE_enca || childAtomType2 == Atom.TYPE_ac_3 || childAtomType2 == Atom.TYPE_ec_3 || childAtomType2 == Atom.TYPE_dtsc || childAtomType2 == Atom.TYPE_dtse || childAtomType2 == Atom.TYPE_dtsh || childAtomType2 == Atom.TYPE_dtsl || childAtomType2 == Atom.TYPE_samr || childAtomType2 == Atom.TYPE_sawb || childAtomType2 == Atom.TYPE_lpcm || childAtomType2 == Atom.TYPE_sowt || childAtomType2 == Atom.TYPE__mp3)) {
                    if (childAtomType2 != Atom.TYPE_alac) {
                        if (!(childAtomType2 == Atom.TYPE_TTML || childAtomType2 == Atom.TYPE_tx3g || childAtomType2 == Atom.TYPE_wvtt || childAtomType2 == Atom.TYPE_stpp)) {
                            if (childAtomType2 != Atom.TYPE_c608) {
                                if (childAtomType2 == Atom.TYPE_camm) {
                                    out.format = Format.createSampleFormat(Integer.toString(trackId), MimeTypes.APPLICATION_CAMERA_MOTION, null, -1, null);
                                }
                                parsableByteArray.setPosition(childStartPosition + childAtomSize);
                                i = i2 + 1;
                            }
                        }
                        parseTextSampleEntry(parsableByteArray, childAtomType2, childStartPosition, childAtomSize, trackId, language, out);
                        parsableByteArray.setPosition(childStartPosition + childAtomSize);
                        i = i2 + 1;
                    }
                }
                childAtomType = childAtomType2;
                parseAudioSampleEntry(parsableByteArray, childAtomType2, childStartPosition, childAtomSize, trackId, language, isQuickTime, drmInitData, out, i2);
                parsableByteArray.setPosition(childStartPosition + childAtomSize);
                i = i2 + 1;
            }
            parseVideoSampleEntry(parsableByteArray, childAtomType, childStartPosition, childAtomSize, trackId, rotationDegrees, drmInitData, out, i2);
            parsableByteArray.setPosition(childStartPosition + childAtomSize);
            i = i2 + 1;
        }
    }

    private static void parseVideoSampleEntry(ParsableByteArray parent, int atomType, int position, int size, int trackId, int rotationDegrees, DrmInitData drmInitData, StsdData out, int entryIndex) throws ParserException {
        ParsableByteArray parsableByteArray = parent;
        int i = position;
        int i2 = size;
        DrmInitData drmInitData2 = drmInitData;
        StsdData stsdData = out;
        parsableByteArray.setPosition((i + 8) + 8);
        parsableByteArray.skipBytes(16);
        int width = parent.readUnsignedShort();
        int height = parent.readUnsignedShort();
        parsableByteArray.skipBytes(50);
        int childPosition = parent.getPosition();
        int atomType2 = atomType;
        if (atomType2 == Atom.TYPE_encv) {
            Pair<Integer, TrackEncryptionBox> sampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray, i, i2);
            if (sampleEntryEncryptionData != null) {
                DrmInitData drmInitData3;
                atomType2 = ((Integer) sampleEntryEncryptionData.first).intValue();
                if (drmInitData2 == null) {
                    drmInitData3 = null;
                } else {
                    drmInitData3 = drmInitData2.copyWithSchemeType(((TrackEncryptionBox) sampleEntryEncryptionData.second).schemeType);
                }
                drmInitData2 = drmInitData3;
                stsdData.trackEncryptionBoxes[entryIndex] = (TrackEncryptionBox) sampleEntryEncryptionData.second;
            }
            parsableByteArray.setPosition(childPosition);
        }
        int atomType3 = atomType2;
        boolean pixelWidthHeightRatioFromPasp = false;
        float pixelWidthHeightRatio = 1.0f;
        int childPosition2 = childPosition;
        List<byte[]> initializationData = null;
        String mimeType = null;
        byte[] projectionData = null;
        int stereoMode = -1;
        while (childPosition2 - i < i2) {
            parsableByteArray.setPosition(childPosition2);
            int childStartPosition = parent.getPosition();
            int childAtomSize = parent.readInt();
            if (childAtomSize != 0 || parent.getPosition() - i != i2) {
                List<byte[]> initializationData2;
                boolean z = false;
                Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
                atomType2 = parent.readInt();
                if (atomType2 == Atom.TYPE_avcC) {
                    if (mimeType == null) {
                        z = true;
                    }
                    Assertions.checkState(z);
                    mimeType = "video/avc";
                    parsableByteArray.setPosition(childStartPosition + 8);
                    float pixelWidthHeightRatio2 = AvcConfig.parse(parent);
                    initializationData2 = pixelWidthHeightRatio2.initializationData;
                    stsdData.nalUnitLengthFieldLength = pixelWidthHeightRatio2.nalUnitLengthFieldLength;
                    if (!pixelWidthHeightRatioFromPasp) {
                        pixelWidthHeightRatio = pixelWidthHeightRatio2.pixelWidthAspectRatio;
                    }
                } else if (atomType2 == Atom.TYPE_hvcC) {
                    if (mimeType == null) {
                        z = true;
                    }
                    Assertions.checkState(z);
                    mimeType = MimeTypes.VIDEO_H265;
                    parsableByteArray.setPosition(childStartPosition + 8);
                    HevcConfig hevcConfig = HevcConfig.parse(parent);
                    initializationData2 = hevcConfig.initializationData;
                    stsdData.nalUnitLengthFieldLength = hevcConfig.nalUnitLengthFieldLength;
                } else {
                    if (atomType2 == Atom.TYPE_vpcC) {
                        if (mimeType == null) {
                            z = true;
                        }
                        Assertions.checkState(z);
                        mimeType = atomType3 == Atom.TYPE_vp08 ? MimeTypes.VIDEO_VP8 : MimeTypes.VIDEO_VP9;
                    } else if (atomType2 == Atom.TYPE_d263) {
                        if (mimeType == null) {
                            z = true;
                        }
                        Assertions.checkState(z);
                        mimeType = MimeTypes.VIDEO_H263;
                    } else if (atomType2 == Atom.TYPE_esds) {
                        if (mimeType == null) {
                            z = true;
                        }
                        Assertions.checkState(z);
                        Pair<String, byte[]> mimeTypeAndInitializationData = parseEsdsFromParent(parsableByteArray, childStartPosition);
                        mimeType = mimeTypeAndInitializationData.first;
                        initializationData = Collections.singletonList(mimeTypeAndInitializationData.second);
                    } else if (atomType2 == Atom.TYPE_pasp) {
                        pixelWidthHeightRatio = parsePaspFromParent(parsableByteArray, childStartPosition);
                        pixelWidthHeightRatioFromPasp = true;
                    } else if (atomType2 == Atom.TYPE_sv3d) {
                        projectionData = parseProjFromParent(parsableByteArray, childStartPosition, childAtomSize);
                    } else if (atomType2 == Atom.TYPE_st3d) {
                        childPosition = parent.readUnsignedByte();
                        parsableByteArray.skipBytes(3);
                        if (childPosition == 0) {
                            int stereoMode2;
                            switch (parent.readUnsignedByte()) {
                                case 0:
                                    stereoMode2 = 0;
                                    break;
                                case 1:
                                    stereoMode2 = 1;
                                    break;
                                case 2:
                                    stereoMode2 = 2;
                                    break;
                                case 3:
                                    stereoMode2 = 3;
                                    break;
                                default:
                                    continue;
                            }
                            stereoMode = stereoMode2;
                        }
                    }
                    childPosition2 += childAtomSize;
                }
                initializationData = initializationData2;
                childPosition2 += childAtomSize;
            } else if (mimeType == null) {
                stsdData.format = Format.createVideoSampleFormat(Integer.toString(trackId), mimeType, null, -1, -1, width, height, -1.0f, initializationData, rotationDegrees, pixelWidthHeightRatio, projectionData, stereoMode, null, drmInitData2);
            }
        }
        if (mimeType == null) {
            stsdData.format = Format.createVideoSampleFormat(Integer.toString(trackId), mimeType, null, -1, -1, width, height, -1.0f, initializationData, rotationDegrees, pixelWidthHeightRatio, projectionData, stereoMode, null, drmInitData2);
        }
    }

    private static float parsePaspFromParent(ParsableByteArray parent, int position) {
        parent.setPosition(position + 8);
        return ((float) parent.readUnsignedIntToInt()) / ((float) parent.readUnsignedIntToInt());
    }

    private static void parseAudioSampleEntry(ParsableByteArray parent, int atomType, int position, int size, int trackId, String language, boolean isQuickTime, DrmInitData drmInitData, StsdData out, int entryIndex) throws ParserException {
        int channelCount;
        int childPosition;
        List list;
        int i;
        Pair<Integer, TrackEncryptionBox> sampleEntryEncryptionData;
        DrmInitData drmInitData2;
        int atomType2;
        String mimeType;
        String mimeType2;
        int sampleRate;
        int channelCount2;
        byte[] initializationData;
        byte[] initializationData2;
        byte[] initializationData3;
        String mimeType3;
        DrmInitData drmInitData3;
        int atomType3;
        int quickTimeSoundDescriptionVersion;
        int i2;
        int i3;
        int childAtomSize;
        int childPosition2;
        int esdsAtomPosition;
        byte[] initializationData4;
        String mimeType4;
        byte[] initializationData5;
        StsdData stsdData;
        byte[] bArr;
        ParsableByteArray parsableByteArray = parent;
        int i4 = position;
        int i5 = size;
        String str = language;
        DrmInitData drmInitData4 = drmInitData;
        StsdData stsdData2 = out;
        int i6 = 8;
        parsableByteArray.setPosition((i4 + 8) + 8);
        int quickTimeSoundDescriptionVersion2 = 0;
        if (isQuickTime) {
            quickTimeSoundDescriptionVersion2 = parent.readUnsignedShort();
            parsableByteArray.skipBytes(6);
        } else {
            parsableByteArray.skipBytes(8);
        }
        int quickTimeSoundDescriptionVersion3 = quickTimeSoundDescriptionVersion2;
        int i7 = 2;
        if (quickTimeSoundDescriptionVersion3 != 0) {
            if (quickTimeSoundDescriptionVersion3 != 1) {
                if (quickTimeSoundDescriptionVersion3 == 2) {
                    parsableByteArray.skipBytes(16);
                    quickTimeSoundDescriptionVersion2 = (int) Math.round(parent.readDouble());
                    channelCount = parent.readUnsignedIntToInt();
                    parsableByteArray.skipBytes(20);
                    childPosition = parent.getPosition();
                    list = null;
                    i = atomType;
                    if (i == Atom.TYPE_enca) {
                        sampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray, i4, i5);
                        if (sampleEntryEncryptionData != null) {
                            i = ((Integer) sampleEntryEncryptionData.first).intValue();
                            drmInitData4 = drmInitData4 != null ? null : drmInitData4.copyWithSchemeType(((TrackEncryptionBox) sampleEntryEncryptionData.second).schemeType);
                            stsdData2.trackEncryptionBoxes[entryIndex] = (TrackEncryptionBox) sampleEntryEncryptionData.second;
                        }
                        parsableByteArray.setPosition(childPosition);
                    }
                    drmInitData2 = drmInitData4;
                    atomType2 = i;
                    mimeType = null;
                    if (atomType2 == Atom.TYPE_ac_3) {
                        mimeType = MimeTypes.AUDIO_AC3;
                    } else if (atomType2 == Atom.TYPE_ec_3) {
                        mimeType = MimeTypes.AUDIO_E_AC3;
                    } else if (atomType2 != Atom.TYPE_dtsc) {
                        mimeType = MimeTypes.AUDIO_DTS;
                    } else {
                        if (atomType2 != Atom.TYPE_dtsh) {
                            if (atomType2 == Atom.TYPE_dtsl) {
                                if (atomType2 == Atom.TYPE_dtse) {
                                    mimeType = MimeTypes.AUDIO_DTS_EXPRESS;
                                } else if (atomType2 == Atom.TYPE_samr) {
                                    mimeType = MimeTypes.AUDIO_AMR_NB;
                                } else if (atomType2 != Atom.TYPE_sawb) {
                                    mimeType = MimeTypes.AUDIO_AMR_WB;
                                } else {
                                    if (atomType2 != Atom.TYPE_lpcm) {
                                        if (atomType2 == Atom.TYPE_sowt) {
                                            if (atomType2 == Atom.TYPE__mp3) {
                                                mimeType = MimeTypes.AUDIO_MPEG;
                                            } else if (atomType2 == Atom.TYPE_alac) {
                                                mimeType = MimeTypes.AUDIO_ALAC;
                                            }
                                        }
                                    }
                                    mimeType = MimeTypes.AUDIO_RAW;
                                }
                            }
                        }
                        mimeType = MimeTypes.AUDIO_DTS_HD;
                    }
                    mimeType2 = mimeType;
                    sampleRate = quickTimeSoundDescriptionVersion2;
                    channelCount2 = channelCount;
                    i = childPosition;
                    initializationData = null;
                    while (true) {
                        initializationData2 = initializationData;
                        if (i - i4 < i5) {
                            break;
                        }
                        parsableByteArray.setPosition(i);
                        channelCount = parent.readInt();
                        Assertions.checkArgument(channelCount <= 0, "childAtomSize should be positive");
                        i7 = parent.readInt();
                        if (i7 != Atom.TYPE_esds) {
                            quickTimeSoundDescriptionVersion2 = channelCount;
                            initializationData3 = initializationData2;
                            mimeType3 = mimeType2;
                            drmInitData3 = drmInitData2;
                            atomType3 = atomType2;
                            channelCount = i7;
                            quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                            i2 = i6;
                            i3 = 2;
                            i6 = i;
                        } else if (isQuickTime || i7 != Atom.TYPE_wave) {
                            if (i7 == Atom.TYPE_dac3) {
                                parsableByteArray.setPosition(i6 + i);
                                stsdData2.format = Ac3Util.parseAc3AnnexFFormat(parsableByteArray, Integer.toString(trackId), str, drmInitData2);
                            } else if (i7 != Atom.TYPE_dec3) {
                                parsableByteArray.setPosition(i6 + i);
                                stsdData2.format = Ac3Util.parseEAc3AnnexFFormat(parsableByteArray, Integer.toString(trackId), str, drmInitData2);
                            } else {
                                if (i7 != Atom.TYPE_ddts) {
                                    childAtomSize = channelCount;
                                    initializationData3 = initializationData2;
                                    mimeType3 = mimeType2;
                                    childPosition2 = i;
                                    drmInitData3 = drmInitData2;
                                    atomType3 = atomType2;
                                    int childAtomType = i7;
                                    i3 = 2;
                                    quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                                    i2 = i6;
                                    stsdData2.format = Format.createAudioSampleFormat(Integer.toString(trackId), mimeType2, null, -1, -1, channelCount2, sampleRate, null, drmInitData3, 0, str);
                                    quickTimeSoundDescriptionVersion2 = childAtomSize;
                                    i6 = childPosition2;
                                    channelCount = childAtomType;
                                } else {
                                    childAtomSize = channelCount;
                                    initializationData3 = initializationData2;
                                    mimeType3 = mimeType2;
                                    childPosition2 = i;
                                    drmInitData3 = drmInitData2;
                                    atomType3 = atomType2;
                                    quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                                    i2 = i6;
                                    i3 = 2;
                                    if (i7 != Atom.TYPE_alac) {
                                        quickTimeSoundDescriptionVersion2 = childAtomSize;
                                        initializationData2 = new byte[quickTimeSoundDescriptionVersion2];
                                        i6 = childPosition2;
                                        parsableByteArray.setPosition(i6);
                                        parsableByteArray.readBytes(initializationData2, 0, quickTimeSoundDescriptionVersion2);
                                        initializationData = initializationData2;
                                        mimeType2 = mimeType3;
                                        i = i6 + quickTimeSoundDescriptionVersion2;
                                        atomType2 = atomType3;
                                        i7 = i3;
                                        drmInitData2 = drmInitData3;
                                        quickTimeSoundDescriptionVersion3 = quickTimeSoundDescriptionVersion;
                                        i6 = i2;
                                    } else {
                                        quickTimeSoundDescriptionVersion2 = childAtomSize;
                                        i6 = childPosition2;
                                    }
                                }
                                initializationData = initializationData3;
                                mimeType2 = mimeType3;
                                i = i6 + quickTimeSoundDescriptionVersion2;
                                atomType2 = atomType3;
                                i7 = i3;
                                drmInitData2 = drmInitData3;
                                quickTimeSoundDescriptionVersion3 = quickTimeSoundDescriptionVersion;
                                i6 = i2;
                            }
                            quickTimeSoundDescriptionVersion2 = channelCount;
                            initializationData3 = initializationData2;
                            mimeType3 = mimeType2;
                            drmInitData3 = drmInitData2;
                            atomType3 = atomType2;
                            channelCount = i7;
                            quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                            i2 = i6;
                            i3 = 2;
                            i6 = i;
                            initializationData = initializationData3;
                            mimeType2 = mimeType3;
                            i = i6 + quickTimeSoundDescriptionVersion2;
                            atomType2 = atomType3;
                            i7 = i3;
                            drmInitData2 = drmInitData3;
                            quickTimeSoundDescriptionVersion3 = quickTimeSoundDescriptionVersion;
                            i6 = i2;
                        } else {
                            quickTimeSoundDescriptionVersion2 = channelCount;
                            initializationData3 = initializationData2;
                            mimeType3 = mimeType2;
                            drmInitData3 = drmInitData2;
                            atomType3 = atomType2;
                            channelCount = i7;
                            quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                            i2 = i6;
                            i3 = 2;
                            i6 = i;
                        }
                        esdsAtomPosition = channelCount != Atom.TYPE_esds ? i6 : findEsdsPosition(parsableByteArray, i6, quickTimeSoundDescriptionVersion2);
                        if (esdsAtomPosition == -1) {
                            initializationData2 = parseEsdsFromParent(parsableByteArray, esdsAtomPosition);
                            mimeType2 = (String) initializationData2.first;
                            initializationData4 = initializationData2.second;
                            if (MimeTypes.AUDIO_AAC.equals(mimeType2)) {
                                Pair<Integer, Integer> audioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(initializationData4);
                                sampleRate = ((Integer) audioSpecificConfig.first).intValue();
                                channelCount2 = ((Integer) audioSpecificConfig.second).intValue();
                            }
                        } else {
                            initializationData4 = initializationData3;
                            mimeType2 = mimeType3;
                        }
                        initializationData = initializationData4;
                        i = i6 + quickTimeSoundDescriptionVersion2;
                        atomType2 = atomType3;
                        i7 = i3;
                        drmInitData2 = drmInitData3;
                        quickTimeSoundDescriptionVersion3 = quickTimeSoundDescriptionVersion;
                        i6 = i2;
                    }
                    initializationData3 = initializationData2;
                    mimeType3 = mimeType2;
                    i6 = i;
                    drmInitData3 = drmInitData2;
                    atomType3 = atomType2;
                    i3 = i7;
                    quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                    if (stsdData2.format != null) {
                        mimeType4 = mimeType3;
                        if (mimeType4 == null) {
                            atomType2 = MimeTypes.AUDIO_RAW.equals(mimeType4) ? i3 : -1;
                            mimeType = Integer.toString(trackId);
                            initializationData5 = initializationData3;
                            if (initializationData5 == null) {
                                list = Collections.singletonList(initializationData5);
                            }
                            List list2 = list;
                            stsdData2.format = Format.createAudioSampleFormat(mimeType, mimeType4, null, -1, -1, channelCount2, sampleRate, atomType2, list2, drmInitData3, 0, str);
                        } else {
                            int i8 = i6;
                            stsdData = stsdData2;
                            bArr = initializationData3;
                        }
                    } else {
                        stsdData = stsdData2;
                        bArr = initializationData3;
                        String str2 = mimeType3;
                    }
                }
                return;
            }
        }
        childPosition = parent.readUnsignedShort();
        parsableByteArray.skipBytes(6);
        channelCount = parent.readUnsignedFixedPoint1616();
        if (quickTimeSoundDescriptionVersion3 == 1) {
            parsableByteArray.skipBytes(16);
        }
        quickTimeSoundDescriptionVersion2 = channelCount;
        channelCount = childPosition;
        childPosition = parent.getPosition();
        list = null;
        i = atomType;
        if (i == Atom.TYPE_enca) {
            sampleEntryEncryptionData = parseSampleEntryEncryptionData(parsableByteArray, i4, i5);
            if (sampleEntryEncryptionData != null) {
                i = ((Integer) sampleEntryEncryptionData.first).intValue();
                if (drmInitData4 != null) {
                }
                drmInitData4 = drmInitData4 != null ? null : drmInitData4.copyWithSchemeType(((TrackEncryptionBox) sampleEntryEncryptionData.second).schemeType);
                stsdData2.trackEncryptionBoxes[entryIndex] = (TrackEncryptionBox) sampleEntryEncryptionData.second;
            }
            parsableByteArray.setPosition(childPosition);
        }
        drmInitData2 = drmInitData4;
        atomType2 = i;
        mimeType = null;
        if (atomType2 == Atom.TYPE_ac_3) {
            mimeType = MimeTypes.AUDIO_AC3;
        } else if (atomType2 == Atom.TYPE_ec_3) {
            mimeType = MimeTypes.AUDIO_E_AC3;
        } else if (atomType2 != Atom.TYPE_dtsc) {
            if (atomType2 != Atom.TYPE_dtsh) {
                if (atomType2 == Atom.TYPE_dtsl) {
                    if (atomType2 == Atom.TYPE_dtse) {
                        mimeType = MimeTypes.AUDIO_DTS_EXPRESS;
                    } else if (atomType2 == Atom.TYPE_samr) {
                        mimeType = MimeTypes.AUDIO_AMR_NB;
                    } else if (atomType2 != Atom.TYPE_sawb) {
                        if (atomType2 != Atom.TYPE_lpcm) {
                            if (atomType2 == Atom.TYPE_sowt) {
                                if (atomType2 == Atom.TYPE__mp3) {
                                    mimeType = MimeTypes.AUDIO_MPEG;
                                } else if (atomType2 == Atom.TYPE_alac) {
                                    mimeType = MimeTypes.AUDIO_ALAC;
                                }
                            }
                        }
                        mimeType = MimeTypes.AUDIO_RAW;
                    } else {
                        mimeType = MimeTypes.AUDIO_AMR_WB;
                    }
                }
            }
            mimeType = MimeTypes.AUDIO_DTS_HD;
        } else {
            mimeType = MimeTypes.AUDIO_DTS;
        }
        mimeType2 = mimeType;
        sampleRate = quickTimeSoundDescriptionVersion2;
        channelCount2 = channelCount;
        i = childPosition;
        initializationData = null;
        while (true) {
            initializationData2 = initializationData;
            if (i - i4 < i5) {
                break;
            }
            parsableByteArray.setPosition(i);
            channelCount = parent.readInt();
            if (channelCount <= 0) {
            }
            Assertions.checkArgument(channelCount <= 0, "childAtomSize should be positive");
            i7 = parent.readInt();
            if (i7 != Atom.TYPE_esds) {
                quickTimeSoundDescriptionVersion2 = channelCount;
                initializationData3 = initializationData2;
                mimeType3 = mimeType2;
                drmInitData3 = drmInitData2;
                atomType3 = atomType2;
                channelCount = i7;
                quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                i2 = i6;
                i3 = 2;
                i6 = i;
            } else {
                if (isQuickTime) {
                }
                if (i7 == Atom.TYPE_dac3) {
                    parsableByteArray.setPosition(i6 + i);
                    stsdData2.format = Ac3Util.parseAc3AnnexFFormat(parsableByteArray, Integer.toString(trackId), str, drmInitData2);
                } else if (i7 != Atom.TYPE_dec3) {
                    if (i7 != Atom.TYPE_ddts) {
                        childAtomSize = channelCount;
                        initializationData3 = initializationData2;
                        mimeType3 = mimeType2;
                        childPosition2 = i;
                        drmInitData3 = drmInitData2;
                        atomType3 = atomType2;
                        quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                        i2 = i6;
                        i3 = 2;
                        if (i7 != Atom.TYPE_alac) {
                            quickTimeSoundDescriptionVersion2 = childAtomSize;
                            i6 = childPosition2;
                        } else {
                            quickTimeSoundDescriptionVersion2 = childAtomSize;
                            initializationData2 = new byte[quickTimeSoundDescriptionVersion2];
                            i6 = childPosition2;
                            parsableByteArray.setPosition(i6);
                            parsableByteArray.readBytes(initializationData2, 0, quickTimeSoundDescriptionVersion2);
                            initializationData = initializationData2;
                            mimeType2 = mimeType3;
                            i = i6 + quickTimeSoundDescriptionVersion2;
                            atomType2 = atomType3;
                            i7 = i3;
                            drmInitData2 = drmInitData3;
                            quickTimeSoundDescriptionVersion3 = quickTimeSoundDescriptionVersion;
                            i6 = i2;
                        }
                    } else {
                        childAtomSize = channelCount;
                        initializationData3 = initializationData2;
                        mimeType3 = mimeType2;
                        childPosition2 = i;
                        drmInitData3 = drmInitData2;
                        atomType3 = atomType2;
                        int childAtomType2 = i7;
                        i3 = 2;
                        quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                        i2 = i6;
                        stsdData2.format = Format.createAudioSampleFormat(Integer.toString(trackId), mimeType2, null, -1, -1, channelCount2, sampleRate, null, drmInitData3, 0, str);
                        quickTimeSoundDescriptionVersion2 = childAtomSize;
                        i6 = childPosition2;
                        channelCount = childAtomType2;
                    }
                    initializationData = initializationData3;
                    mimeType2 = mimeType3;
                    i = i6 + quickTimeSoundDescriptionVersion2;
                    atomType2 = atomType3;
                    i7 = i3;
                    drmInitData2 = drmInitData3;
                    quickTimeSoundDescriptionVersion3 = quickTimeSoundDescriptionVersion;
                    i6 = i2;
                } else {
                    parsableByteArray.setPosition(i6 + i);
                    stsdData2.format = Ac3Util.parseEAc3AnnexFFormat(parsableByteArray, Integer.toString(trackId), str, drmInitData2);
                }
                quickTimeSoundDescriptionVersion2 = channelCount;
                initializationData3 = initializationData2;
                mimeType3 = mimeType2;
                drmInitData3 = drmInitData2;
                atomType3 = atomType2;
                channelCount = i7;
                quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
                i2 = i6;
                i3 = 2;
                i6 = i;
                initializationData = initializationData3;
                mimeType2 = mimeType3;
                i = i6 + quickTimeSoundDescriptionVersion2;
                atomType2 = atomType3;
                i7 = i3;
                drmInitData2 = drmInitData3;
                quickTimeSoundDescriptionVersion3 = quickTimeSoundDescriptionVersion;
                i6 = i2;
            }
            if (channelCount != Atom.TYPE_esds) {
            }
            esdsAtomPosition = channelCount != Atom.TYPE_esds ? i6 : findEsdsPosition(parsableByteArray, i6, quickTimeSoundDescriptionVersion2);
            if (esdsAtomPosition == -1) {
                initializationData4 = initializationData3;
                mimeType2 = mimeType3;
            } else {
                initializationData2 = parseEsdsFromParent(parsableByteArray, esdsAtomPosition);
                mimeType2 = (String) initializationData2.first;
                initializationData4 = initializationData2.second;
                if (MimeTypes.AUDIO_AAC.equals(mimeType2)) {
                    Pair<Integer, Integer> audioSpecificConfig2 = CodecSpecificDataUtil.parseAacAudioSpecificConfig(initializationData4);
                    sampleRate = ((Integer) audioSpecificConfig2.first).intValue();
                    channelCount2 = ((Integer) audioSpecificConfig2.second).intValue();
                }
            }
            initializationData = initializationData4;
            i = i6 + quickTimeSoundDescriptionVersion2;
            atomType2 = atomType3;
            i7 = i3;
            drmInitData2 = drmInitData3;
            quickTimeSoundDescriptionVersion3 = quickTimeSoundDescriptionVersion;
            i6 = i2;
        }
        initializationData3 = initializationData2;
        mimeType3 = mimeType2;
        i6 = i;
        drmInitData3 = drmInitData2;
        atomType3 = atomType2;
        i3 = i7;
        quickTimeSoundDescriptionVersion = quickTimeSoundDescriptionVersion3;
        if (stsdData2.format != null) {
            stsdData = stsdData2;
            bArr = initializationData3;
            String str22 = mimeType3;
        } else {
            mimeType4 = mimeType3;
            if (mimeType4 == null) {
                int i82 = i6;
                stsdData = stsdData2;
                bArr = initializationData3;
            } else {
                if (MimeTypes.AUDIO_RAW.equals(mimeType4)) {
                }
                mimeType = Integer.toString(trackId);
                initializationData5 = initializationData3;
                if (initializationData5 == null) {
                    list = Collections.singletonList(initializationData5);
                }
                List list22 = list;
                stsdData2.format = Format.createAudioSampleFormat(mimeType, mimeType4, null, -1, -1, channelCount2, sampleRate, atomType2, list22, drmInitData3, 0, str);
            }
        }
    }

    private static int findEsdsPosition(ParsableByteArray parent, int position, int size) {
        int childAtomPosition = parent.getPosition();
        while (childAtomPosition - position < size) {
            parent.setPosition(childAtomPosition);
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            if (parent.readInt() == Atom.TYPE_esds) {
                return childAtomPosition;
            }
            childAtomPosition += childAtomSize;
        }
        return -1;
    }

    private static Pair<Integer, TrackEncryptionBox> parseSampleEntryEncryptionData(ParsableByteArray parent, int position, int size) {
        int childPosition = parent.getPosition();
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            if (parent.readInt() == Atom.TYPE_sinf) {
                Pair<Integer, TrackEncryptionBox> result = parseCommonEncryptionSinfFromParent(parent, childPosition, childAtomSize);
                if (result != null) {
                    return result;
                }
            }
            childPosition += childAtomSize;
        }
        return null;
    }

    static Pair<Integer, TrackEncryptionBox> parseCommonEncryptionSinfFromParent(ParsableByteArray parent, int position, int size) {
        String schemeType = null;
        int schemeInformationBoxSize = 0;
        int schemeInformationBoxPosition = -1;
        int childPosition = position + 8;
        Integer dataFormat = null;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_frma) {
                dataFormat = Integer.valueOf(parent.readInt());
            } else if (childAtomType == Atom.TYPE_schm) {
                parent.skipBytes(4);
                schemeType = parent.readString(4);
            } else if (childAtomType == Atom.TYPE_schi) {
                schemeInformationBoxPosition = childPosition;
                schemeInformationBoxSize = childAtomSize;
            }
            childPosition += childAtomSize;
        }
        if (!(C.CENC_TYPE_cenc.equals(schemeType) || C.CENC_TYPE_cbc1.equals(schemeType) || C.CENC_TYPE_cens.equals(schemeType))) {
            if (!C.CENC_TYPE_cbcs.equals(schemeType)) {
                return null;
            }
        }
        boolean z = false;
        Assertions.checkArgument(dataFormat != null, "frma atom is mandatory");
        Assertions.checkArgument(schemeInformationBoxPosition != -1, "schi atom is mandatory");
        TrackEncryptionBox encryptionBox = parseSchiFromParent(parent, schemeInformationBoxPosition, schemeInformationBoxSize, schemeType);
        if (encryptionBox != null) {
            z = true;
        }
        Assertions.checkArgument(z, "tenc atom is mandatory");
        return Pair.create(dataFormat, encryptionBox);
    }

    private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray parent, int position, int size, String schemeType) {
        ParsableByteArray parsableByteArray = parent;
        int childPosition = position + 8;
        while (childPosition - position < size) {
            parsableByteArray.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            if (parent.readInt() == Atom.TYPE_tenc) {
                int version = Atom.parseFullAtomVersion(parent.readInt());
                boolean defaultIsProtected = true;
                parsableByteArray.skipBytes(1);
                int defaultCryptByteBlock = 0;
                int defaultSkipByteBlock = 0;
                if (version == 0) {
                    parsableByteArray.skipBytes(1);
                } else {
                    int patternByte = parent.readUnsignedByte();
                    defaultCryptByteBlock = (patternByte & PsExtractor.VIDEO_STREAM_MASK) >> 4;
                    defaultSkipByteBlock = patternByte & 15;
                }
                if (parent.readUnsignedByte() != 1) {
                    defaultIsProtected = false;
                }
                int defaultPerSampleIvSize = parent.readUnsignedByte();
                byte[] bArr = new byte[16];
                parsableByteArray.readBytes(bArr, 0, bArr.length);
                byte[] constantIv = null;
                if (defaultIsProtected && defaultPerSampleIvSize == 0) {
                    int constantIvSize = parent.readUnsignedByte();
                    constantIv = new byte[constantIvSize];
                    parsableByteArray.readBytes(constantIv, 0, constantIvSize);
                }
                byte[] defaultKeyId = bArr;
                return new TrackEncryptionBox(defaultIsProtected, schemeType, defaultPerSampleIvSize, bArr, defaultCryptByteBlock, defaultSkipByteBlock, constantIv);
            }
            childPosition += childAtomSize;
        }
        return null;
    }

    private static byte[] parseProjFromParent(ParsableByteArray parent, int position, int size) {
        int childPosition = position + 8;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            if (parent.readInt() == Atom.TYPE_proj) {
                return Arrays.copyOfRange(parent.data, childPosition, childPosition + childAtomSize);
            }
            childPosition += childAtomSize;
        }
        return null;
    }

    private static int parseExpandableClassSize(ParsableByteArray data) {
        int currentByte = data.readUnsignedByte();
        int size = currentByte & 127;
        while ((currentByte & 128) == 128) {
            currentByte = data.readUnsignedByte();
            size = (size << 7) | (currentByte & 127);
        }
        return size;
    }

    private AtomParsers() {
    }
}
