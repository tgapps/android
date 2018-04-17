package org.telegram.messenger.exoplayer2.metadata.id3;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.metadata.Metadata;
import org.telegram.messenger.exoplayer2.metadata.MetadataDecoder;
import org.telegram.messenger.exoplayer2.metadata.MetadataInputBuffer;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.Util;

public final class Id3Decoder implements MetadataDecoder {
    private static final int FRAME_FLAG_V3_HAS_GROUP_IDENTIFIER = 32;
    private static final int FRAME_FLAG_V3_IS_COMPRESSED = 128;
    private static final int FRAME_FLAG_V3_IS_ENCRYPTED = 64;
    private static final int FRAME_FLAG_V4_HAS_DATA_LENGTH = 1;
    private static final int FRAME_FLAG_V4_HAS_GROUP_IDENTIFIER = 64;
    private static final int FRAME_FLAG_V4_IS_COMPRESSED = 8;
    private static final int FRAME_FLAG_V4_IS_ENCRYPTED = 4;
    private static final int FRAME_FLAG_V4_IS_UNSYNCHRONIZED = 2;
    public static final int ID3_HEADER_LENGTH = 10;
    public static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private static final int ID3_TEXT_ENCODING_ISO_8859_1 = 0;
    private static final int ID3_TEXT_ENCODING_UTF_16 = 1;
    private static final int ID3_TEXT_ENCODING_UTF_16BE = 2;
    private static final int ID3_TEXT_ENCODING_UTF_8 = 3;
    private static final String TAG = "Id3Decoder";
    private final FramePredicate framePredicate;

    public interface FramePredicate {
        boolean evaluate(int i, int i2, int i3, int i4, int i5);
    }

    private static final class Id3Header {
        private final int framesSize;
        private final boolean isUnsynchronized;
        private final int majorVersion;

        public Id3Header(int majorVersion, boolean isUnsynchronized, int framesSize) {
            this.majorVersion = majorVersion;
            this.isUnsynchronized = isUnsynchronized;
            this.framesSize = framesSize;
        }
    }

    private static org.telegram.messenger.exoplayer2.metadata.id3.ChapterFrame decodeChapterFrame(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1, int r2, int r3, boolean r4, int r5, org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.FramePredicate r6) throws java.io.UnsupportedEncodingException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.decodeChapterFrame(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int, int, boolean, int, org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder$FramePredicate):org.telegram.messenger.exoplayer2.metadata.id3.ChapterFrame
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
        r0 = r22;
        r1 = r22.getPosition();
        r2 = r0.data;
        r2 = indexOfZeroByte(r2, r1);
        r4 = new java.lang.String;
        r3 = r0.data;
        r5 = r2 - r1;
        r6 = "ISO-8859-1";
        r4.<init>(r3, r1, r5, r6);
        r3 = r2 + 1;
        r0.setPosition(r3);
        r12 = r22.readInt();
        r13 = r22.readInt();
        r5 = r22.readUnsignedInt();
        r7 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r3 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r3 != 0) goto L_0x0033;
    L_0x0031:
        r5 = -1;
    L_0x0033:
        r14 = r5;
        r5 = r22.readUnsignedInt();
        r3 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r3 != 0) goto L_0x003e;
    L_0x003c:
        r5 = -1;
    L_0x003e:
        r16 = r5;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r11 = r3;
        r3 = r1 + r23;
        r9 = r3;
        r3 = r22.getPosition();
        if (r3 >= r9) goto L_0x0063;
    L_0x004f:
        r10 = r24;
        r7 = r25;
        r8 = r26;
        r6 = r27;
        r3 = decodeFrame(r10, r0, r7, r8, r6);
        if (r3 == 0) goto L_0x0060;
    L_0x005d:
        r11.add(r3);
        r3 = r9;
        goto L_0x0048;
    L_0x0063:
        r10 = r24;
        r7 = r25;
        r8 = r26;
        r6 = r27;
        r3 = r11.size();
        r5 = new org.telegram.messenger.exoplayer2.metadata.id3.Id3Frame[r3];
        r11.toArray(r5);
        r18 = new org.telegram.messenger.exoplayer2.metadata.id3.ChapterFrame;
        r3 = r18;
        r19 = r5;
        r5 = r12;
        r6 = r13;
        r7 = r14;
        r20 = r9;
        r9 = r16;
        r21 = r11;
        r11 = r19;
        r3.<init>(r4, r5, r6, r7, r9, r11);
        return r18;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.decodeChapterFrame(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int, int, boolean, int, org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder$FramePredicate):org.telegram.messenger.exoplayer2.metadata.id3.ChapterFrame");
    }

    private static org.telegram.messenger.exoplayer2.metadata.id3.ChapterTocFrame decodeChapterTOCFrame(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1, int r2, int r3, boolean r4, int r5, org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.FramePredicate r6) throws java.io.UnsupportedEncodingException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.decodeChapterTOCFrame(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int, int, boolean, int, org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder$FramePredicate):org.telegram.messenger.exoplayer2.metadata.id3.ChapterTocFrame
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
        r0 = r18;
        r1 = r18.getPosition();
        r2 = r0.data;
        r2 = indexOfZeroByte(r2, r1);
        r4 = new java.lang.String;
        r3 = r0.data;
        r5 = r2 - r1;
        r6 = "ISO-8859-1";
        r4.<init>(r3, r1, r5, r6);
        r3 = r2 + 1;
        r0.setPosition(r3);
        r9 = r18.readUnsignedByte();
        r3 = r9 & 2;
        r7 = 0;
        r6 = 1;
        if (r3 == 0) goto L_0x0028;
    L_0x0026:
        r5 = r6;
        goto L_0x0029;
    L_0x0028:
        r5 = r7;
    L_0x0029:
        r3 = r9 & 1;
        if (r3 == 0) goto L_0x002e;
    L_0x002d:
        goto L_0x002f;
    L_0x002e:
        r6 = r7;
    L_0x002f:
        r10 = r18.readUnsignedByte();
        r11 = new java.lang.String[r10];
    L_0x0036:
        r3 = r7;
        if (r3 >= r10) goto L_0x0058;
    L_0x0039:
        r7 = r18.getPosition();
        r8 = r0.data;
        r8 = indexOfZeroByte(r8, r7);
        r12 = new java.lang.String;
        r13 = r0.data;
        r14 = r8 - r7;
        r15 = "ISO-8859-1";
        r12.<init>(r13, r7, r14, r15);
        r11[r3] = r12;
        r12 = r8 + 1;
        r0.setPosition(r12);
        r7 = r3 + 1;
        goto L_0x0036;
    L_0x0058:
        r3 = new java.util.ArrayList;
        r3.<init>();
        r12 = r3;
        r3 = r1 + r19;
        r14 = r3;
        r3 = r18.getPosition();
        if (r3 >= r14) goto L_0x007f;
    L_0x0067:
        r15 = r20;
        r8 = r21;
        r7 = r22;
        r3 = r23;
        r16 = r1;
        r1 = decodeFrame(r15, r0, r8, r7, r3);
        if (r1 == 0) goto L_0x007a;
    L_0x0077:
        r12.add(r1);
        r3 = r14;
        r1 = r16;
        goto L_0x0060;
    L_0x007f:
        r15 = r20;
        r8 = r21;
        r7 = r22;
        r3 = r23;
        r16 = r1;
        r1 = r12.size();
        r1 = new org.telegram.messenger.exoplayer2.metadata.id3.Id3Frame[r1];
        r12.toArray(r1);
        r17 = new org.telegram.messenger.exoplayer2.metadata.id3.ChapterTocFrame;
        r3 = r17;
        r7 = r11;
        r8 = r1;
        r3.<init>(r4, r5, r6, r7, r8);
        return r17;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.decodeChapterTOCFrame(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int, int, boolean, int, org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder$FramePredicate):org.telegram.messenger.exoplayer2.metadata.id3.ChapterTocFrame");
    }

    private static org.telegram.messenger.exoplayer2.metadata.id3.Id3Frame decodeFrame(int r1, org.telegram.messenger.exoplayer2.util.ParsableByteArray r2, boolean r3, int r4, org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.FramePredicate r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.decodeFrame(int, org.telegram.messenger.exoplayer2.util.ParsableByteArray, boolean, int, org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder$FramePredicate):org.telegram.messenger.exoplayer2.metadata.id3.Id3Frame
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
        r7 = r23;
        r8 = r24;
        r9 = r24.readUnsignedByte();
        r10 = r24.readUnsignedByte();
        r11 = r24.readUnsignedByte();
        r12 = 3;
        if (r7 < r12) goto L_0x0018;
    L_0x0013:
        r1 = r24.readUnsignedByte();
        goto L_0x0019;
    L_0x0018:
        r1 = 0;
    L_0x0019:
        r14 = r1;
        r15 = 4;
        if (r7 != r15) goto L_0x003c;
    L_0x001d:
        r1 = r24.readUnsignedIntToInt();
        if (r25 != 0) goto L_0x0047;
    L_0x0023:
        r2 = r1 & 255;
        r3 = r1 >> 8;
        r3 = r3 & 255;
        r3 = r3 << 7;
        r2 = r2 | r3;
        r3 = r1 >> 16;
        r3 = r3 & 255;
        r3 = r3 << 14;
        r2 = r2 | r3;
        r3 = r1 >> 24;
        r3 = r3 & 255;
        r3 = r3 << 21;
        r1 = r2 | r3;
        goto L_0x0047;
    L_0x003c:
        if (r7 != r12) goto L_0x0043;
    L_0x003e:
        r1 = r24.readUnsignedIntToInt();
        goto L_0x0047;
    L_0x0043:
        r1 = r24.readUnsignedInt24();
    L_0x0047:
        r16 = r1;
        if (r7 < r12) goto L_0x0050;
    L_0x004b:
        r1 = r24.readUnsignedShort();
        goto L_0x0051;
    L_0x0050:
        r1 = 0;
    L_0x0051:
        r6 = r1;
        r17 = 0;
        if (r9 != 0) goto L_0x0068;
    L_0x0056:
        if (r10 != 0) goto L_0x0068;
    L_0x0058:
        if (r11 != 0) goto L_0x0068;
    L_0x005a:
        if (r14 != 0) goto L_0x0068;
    L_0x005c:
        if (r16 != 0) goto L_0x0068;
    L_0x005e:
        if (r6 != 0) goto L_0x0068;
    L_0x0060:
        r1 = r24.limit();
        r8.setPosition(r1);
        return r17;
    L_0x0068:
        r1 = r24.getPosition();
        r5 = r1 + r16;
        r1 = r24.limit();
        if (r5 <= r1) goto L_0x0083;
    L_0x0074:
        r1 = "Id3Decoder";
        r2 = "Frame size exceeds remaining tag data";
        android.util.Log.w(r1, r2);
        r1 = r24.limit();
        r8.setPosition(r1);
        return r17;
    L_0x0083:
        if (r27 == 0) goto L_0x0098;
    L_0x0085:
        r1 = r27;
        r2 = r7;
        r3 = r9;
        r4 = r10;
        r13 = r5;
        r5 = r11;
        r15 = r6;
        r6 = r14;
        r1 = r1.evaluate(r2, r3, r4, r5, r6);
        if (r1 != 0) goto L_0x009a;
    L_0x0094:
        r8.setPosition(r13);
        return r17;
    L_0x0098:
        r13 = r5;
        r15 = r6;
    L_0x009a:
        r1 = 0;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 1;
        if (r7 != r12) goto L_0x00bf;
    L_0x00a2:
        r12 = r15 & 128;
        if (r12 == 0) goto L_0x00a8;
    L_0x00a6:
        r12 = r6;
        goto L_0x00a9;
    L_0x00a8:
        r12 = 0;
    L_0x00a9:
        r1 = r12;
        r12 = r15 & 64;
        if (r12 == 0) goto L_0x00b0;
    L_0x00ae:
        r12 = r6;
        goto L_0x00b1;
    L_0x00b0:
        r12 = 0;
    L_0x00b1:
        r2 = r12;
        r12 = r15 & 32;
        if (r12 == 0) goto L_0x00b9;
    L_0x00b6:
        r18 = r6;
        goto L_0x00bb;
    L_0x00b9:
        r18 = 0;
    L_0x00bb:
        r5 = r18;
        r4 = r1;
        goto L_0x00ed;
    L_0x00bf:
        r12 = 4;
        if (r7 != r12) goto L_0x00ed;
    L_0x00c2:
        r12 = r15 & 64;
        if (r12 == 0) goto L_0x00c8;
    L_0x00c6:
        r12 = r6;
        goto L_0x00c9;
    L_0x00c8:
        r12 = 0;
    L_0x00c9:
        r5 = r12;
        r12 = r15 & 8;
        if (r12 == 0) goto L_0x00d0;
    L_0x00ce:
        r12 = r6;
        goto L_0x00d1;
    L_0x00d0:
        r12 = 0;
    L_0x00d1:
        r1 = r12;
        r12 = r15 & 4;
        if (r12 == 0) goto L_0x00d8;
    L_0x00d6:
        r12 = r6;
        goto L_0x00d9;
    L_0x00d8:
        r12 = 0;
    L_0x00d9:
        r2 = r12;
        r12 = r15 & 2;
        if (r12 == 0) goto L_0x00e0;
    L_0x00de:
        r12 = r6;
        goto L_0x00e1;
    L_0x00e0:
        r12 = 0;
    L_0x00e1:
        r3 = r12;
        r12 = r15 & 1;
        if (r12 == 0) goto L_0x00e9;
    L_0x00e6:
        r18 = r6;
        goto L_0x00eb;
    L_0x00e9:
        r18 = 0;
    L_0x00eb:
        r4 = r18;
    L_0x00ed:
        r12 = r1;
        r18 = r2;
        r19 = r3;
        r20 = r4;
        r21 = r5;
        if (r12 != 0) goto L_0x0253;
    L_0x00f8:
        if (r18 == 0) goto L_0x00fc;
    L_0x00fa:
        goto L_0x0253;
    L_0x00fc:
        if (r21 == 0) goto L_0x0103;
    L_0x00fe:
        r16 = r16 + -1;
        r8.skipBytes(r6);
    L_0x0103:
        if (r20 == 0) goto L_0x010b;
    L_0x0105:
        r16 = r16 + -4;
        r1 = 4;
        r8.skipBytes(r1);
    L_0x010b:
        r1 = r16;
        if (r19 == 0) goto L_0x0113;
    L_0x010f:
        r1 = removeUnsynchronization(r8, r1);
    L_0x0113:
        r6 = r1;
        r1 = 84;
        r2 = 88;
        r3 = 2;
        if (r9 != r1) goto L_0x0128;
    L_0x011b:
        if (r10 != r2) goto L_0x0128;
    L_0x011d:
        if (r11 != r2) goto L_0x0128;
    L_0x011f:
        if (r7 == r3) goto L_0x0123;
    L_0x0121:
        if (r14 != r2) goto L_0x0128;
    L_0x0123:
        r1 = decodeTxxxFrame(r8, r6);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0127:
        goto L_0x0134;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0128:
        if (r9 != r1) goto L_0x0141;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x012a:
        r1 = getFrameId(r7, r9, r10, r11, r14);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r2 = decodeTextInformationFrame(r8, r6, r1);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r1 = r2;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0134:
        r2 = r6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        goto L_0x020b;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0137:
        r0 = move-exception;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r1 = r0;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r2 = r6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        goto L_0x024f;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x013c:
        r0 = move-exception;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r1 = r0;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r2 = r6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        goto L_0x0241;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0141:
        r4 = 87;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r9 != r4) goto L_0x0152;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0145:
        if (r10 != r2) goto L_0x0152;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0147:
        if (r11 != r2) goto L_0x0152;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0149:
        if (r7 == r3) goto L_0x014d;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x014b:
        if (r14 != r2) goto L_0x0152;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x014d:
        r1 = decodeWxxxFrame(r8, r6);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        goto L_0x0127;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0152:
        r2 = 87;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r9 != r2) goto L_0x0160;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0156:
        r1 = getFrameId(r7, r9, r10, r11, r14);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r2 = decodeUrlLinkFrame(r8, r6, r1);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r1 = r2;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        goto L_0x0134;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0160:
        r2 = 73;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r4 = 80;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r9 != r4) goto L_0x0175;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0166:
        r5 = 82;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r10 != r5) goto L_0x0175;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x016a:
        if (r11 != r2) goto L_0x0175;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x016c:
        r5 = 86;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r14 != r5) goto L_0x0175;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0170:
        r1 = decodePrivFrame(r8, r6);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        goto L_0x0127;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0175:
        r5 = 71;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        r1 = 79;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r9 != r5) goto L_0x018c;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x017b:
        r5 = 69;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r10 != r5) goto L_0x018c;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x017f:
        if (r11 != r1) goto L_0x018c;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0181:
        r5 = 66;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r14 == r5) goto L_0x0187;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0185:
        if (r7 != r3) goto L_0x018c;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0187:
        r1 = decodeGeobFrame(r8, r6);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        goto L_0x0127;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x018c:
        r5 = 67;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r7 != r3) goto L_0x0197;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0190:
        if (r9 != r4) goto L_0x01a6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0192:
        if (r10 != r2) goto L_0x01a6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0194:
        if (r11 != r5) goto L_0x01a6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0196:
        goto L_0x01a1;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x0197:
        r3 = 65;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r9 != r3) goto L_0x01a6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x019b:
        if (r10 != r4) goto L_0x01a6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x019d:
        if (r11 != r2) goto L_0x01a6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x019f:
        if (r14 != r5) goto L_0x01a6;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x01a1:
        r1 = decodeApicFrame(r8, r6, r7);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        goto L_0x0127;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x01a6:
        if (r9 != r5) goto L_0x01bb;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x01a8:
        if (r10 != r1) goto L_0x01bb;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x01aa:
        r2 = 77;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r11 != r2) goto L_0x01bb;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x01ae:
        r2 = 77;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r14 == r2) goto L_0x01b5;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x01b2:
        r2 = 2;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        if (r7 != r2) goto L_0x01bb;	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
    L_0x01b5:
        r1 = decodeCommentFrame(r8, r6);	 Catch:{ UnsupportedEncodingException -> 0x013c, all -> 0x0137 }
        goto L_0x0127;
    L_0x01bb:
        if (r9 != r5) goto L_0x01e5;
    L_0x01bd:
        r2 = 72;
        if (r10 != r2) goto L_0x01e5;
    L_0x01c1:
        r2 = 65;
        if (r11 != r2) goto L_0x01e5;
    L_0x01c5:
        if (r14 != r4) goto L_0x01e5;
    L_0x01c7:
        r1 = r8;
        r2 = r6;
        r3 = r7;
        r4 = r25;
        r5 = r26;
        r22 = r6;
        r6 = r27;
        r1 = decodeChapterFrame(r1, r2, r3, r4, r5, r6);	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
    L_0x01d6:
        r2 = r22;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        goto L_0x020b;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
    L_0x01d9:
        r0 = move-exception;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r1 = r0;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r2 = r22;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        goto L_0x024f;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
    L_0x01df:
        r0 = move-exception;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r1 = r0;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r2 = r22;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        goto L_0x0241;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
    L_0x01e5:
        r22 = r6;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        if (r9 != r5) goto L_0x0200;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
    L_0x01e9:
        r2 = 84;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        if (r10 != r2) goto L_0x0200;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
    L_0x01ed:
        if (r11 != r1) goto L_0x0200;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
    L_0x01ef:
        if (r14 != r5) goto L_0x0200;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
    L_0x01f1:
        r1 = r8;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r2 = r22;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r3 = r7;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r4 = r25;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r5 = r26;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r6 = r27;	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        r1 = decodeChapterTOCFrame(r1, r2, r3, r4, r5, r6);	 Catch:{ UnsupportedEncodingException -> 0x01df, all -> 0x01d9 }
        goto L_0x01d6;
    L_0x0200:
        r1 = getFrameId(r7, r9, r10, r11, r14);	 Catch:{ UnsupportedEncodingException -> 0x023d, all -> 0x0238 }
        r2 = r22;
        r3 = decodeBinaryFrame(r8, r2, r1);	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r1 = r3;	 Catch:{ UnsupportedEncodingException -> 0x0230 }
    L_0x020b:
        if (r1 != 0) goto L_0x0233;	 Catch:{ UnsupportedEncodingException -> 0x0230 }
    L_0x020d:
        r3 = "Id3Decoder";	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r4 = new java.lang.StringBuilder;	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r4.<init>();	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r5 = "Failed to decode frame: id=";	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r4.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r5 = getFrameId(r7, r9, r10, r11, r14);	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r4.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r5 = ", frameSize=";	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r4.append(r5);	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r4.append(r2);	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        r4 = r4.toString();	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        android.util.Log.w(r3, r4);	 Catch:{ UnsupportedEncodingException -> 0x0230 }
        goto L_0x0233;
    L_0x0230:
        r0 = move-exception;
        r1 = r0;
        goto L_0x0241;
        r8.setPosition(r13);
        return r1;
    L_0x0238:
        r0 = move-exception;
        r2 = r22;
        r1 = r0;
        goto L_0x024f;
    L_0x023d:
        r0 = move-exception;
        r2 = r22;
        r1 = r0;
    L_0x0241:
        r3 = "Id3Decoder";	 Catch:{ all -> 0x024d }
        r4 = "Unsupported character encoding";	 Catch:{ all -> 0x024d }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x024d }
        r8.setPosition(r13);
        return r17;
    L_0x024d:
        r0 = move-exception;
        r1 = r0;
    L_0x024f:
        r8.setPosition(r13);
        throw r1;
    L_0x0253:
        r1 = "Id3Decoder";
        r2 = "Skipping unsupported compressed or encrypted frame";
        android.util.Log.w(r1, r2);
        r8.setPosition(r13);
        return r17;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.decodeFrame(int, org.telegram.messenger.exoplayer2.util.ParsableByteArray, boolean, int, org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder$FramePredicate):org.telegram.messenger.exoplayer2.metadata.id3.Id3Frame");
    }

    private static org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.Id3Header decodeHeader(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.decodeHeader(org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder$Id3Header
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
        r0 = r10.bytesLeft();
        r1 = 0;
        r2 = 10;
        if (r0 >= r2) goto L_0x0011;
    L_0x0009:
        r0 = "Id3Decoder";
        r2 = "Data too short to be an ID3 tag";
        android.util.Log.w(r0, r2);
        return r1;
    L_0x0011:
        r0 = r10.readUnsignedInt24();
        r2 = ID3_TAG;
        if (r0 == r2) goto L_0x0030;
    L_0x0019:
        r2 = "Id3Decoder";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unexpected first three bytes of ID3 tag header: ";
        r3.append(r4);
        r3.append(r0);
        r3 = r3.toString();
        android.util.Log.w(r2, r3);
        return r1;
    L_0x0030:
        r2 = r10.readUnsignedByte();
        r3 = 1;
        r10.skipBytes(r3);
        r4 = r10.readUnsignedByte();
        r5 = r10.readSynchSafeInt();
        r6 = 2;
        r7 = 4;
        r8 = 0;
        if (r2 != r6) goto L_0x0057;
    L_0x0045:
        r6 = r4 & 64;
        if (r6 == 0) goto L_0x004b;
    L_0x0049:
        r6 = r3;
        goto L_0x004c;
    L_0x004b:
        r6 = r8;
    L_0x004c:
        if (r6 == 0) goto L_0x0056;
    L_0x004e:
        r3 = "Id3Decoder";
        r7 = "Skipped ID3 tag with majorVersion=2 and undefined compression scheme";
        android.util.Log.w(r3, r7);
        return r1;
    L_0x0056:
        goto L_0x008f;
    L_0x0057:
        r6 = 3;
        if (r2 != r6) goto L_0x006e;
    L_0x005a:
        r1 = r4 & 64;
        if (r1 == 0) goto L_0x0060;
    L_0x005e:
        r1 = r3;
        goto L_0x0061;
    L_0x0060:
        r1 = r8;
    L_0x0061:
        if (r1 == 0) goto L_0x006d;
    L_0x0063:
        r6 = r10.readInt();
        r10.skipBytes(r6);
        r9 = r6 + 4;
        r5 = r5 - r9;
    L_0x006d:
        goto L_0x008f;
    L_0x006e:
        if (r2 != r7) goto L_0x009e;
    L_0x0070:
        r1 = r4 & 64;
        if (r1 == 0) goto L_0x0076;
    L_0x0074:
        r1 = r3;
        goto L_0x0077;
    L_0x0076:
        r1 = r8;
    L_0x0077:
        if (r1 == 0) goto L_0x0083;
    L_0x0079:
        r6 = r10.readSynchSafeInt();
        r9 = r6 + -4;
        r10.skipBytes(r9);
        r5 = r5 - r6;
    L_0x0083:
        r6 = r4 & 16;
        if (r6 == 0) goto L_0x0089;
    L_0x0087:
        r6 = r3;
        goto L_0x008a;
    L_0x0089:
        r6 = r8;
    L_0x008a:
        if (r6 == 0) goto L_0x008e;
    L_0x008c:
        r5 = r5 + -10;
    L_0x008f:
        if (r2 >= r7) goto L_0x0096;
        r1 = r4 & 128;
        if (r1 == 0) goto L_0x0096;
        goto L_0x0097;
        r3 = r8;
        r1 = r3;
        r3 = new org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder$Id3Header;
        r3.<init>(r2, r1, r5);
        return r3;
    L_0x009e:
        r3 = "Id3Decoder";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Skipped ID3 tag with unsupported majorVersion=";
        r6.append(r7);
        r6.append(r2);
        r6 = r6.toString();
        android.util.Log.w(r3, r6);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.decodeHeader(org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder$Id3Header");
    }

    private static boolean validateFrames(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1, int r2, int r3, boolean r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.validateFrames(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int, int, boolean):boolean
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
        r1 = r22;
        r2 = r23;
        r3 = r22.getPosition();
        r4 = r22.bytesLeft();	 Catch:{ all -> 0x00ce }
        r5 = 1;
        r6 = r24;
        if (r4 < r6) goto L_0x00c9;
    L_0x0011:
        r4 = 3;
        r7 = 0;
        if (r2 < r4) goto L_0x0025;
        r8 = r22.readInt();	 Catch:{ all -> 0x0022 }
        r9 = r22.readUnsignedInt();	 Catch:{ all -> 0x0022 }
        r11 = r22.readUnsignedShort();	 Catch:{ all -> 0x0022 }
        goto L_0x002f;	 Catch:{ all -> 0x0022 }
    L_0x0022:
        r0 = move-exception;	 Catch:{ all -> 0x0022 }
        goto L_0x00d1;	 Catch:{ all -> 0x0022 }
        r8 = r22.readUnsignedInt24();	 Catch:{ all -> 0x0022 }
        r9 = r22.readUnsignedInt24();	 Catch:{ all -> 0x0022 }
        r9 = (long) r9;
        r11 = r7;
        r12 = 0;
        if (r8 != 0) goto L_0x003e;
        r14 = (r9 > r12 ? 1 : (r9 == r12 ? 0 : -1));
        if (r14 != 0) goto L_0x003e;
        if (r11 != 0) goto L_0x003e;
        r1.setPosition(r3);
        return r5;
        r14 = 4;
        if (r2 != r14) goto L_0x0079;
        if (r25 != 0) goto L_0x0079;
        r15 = 8421504; // 0x808080 float:1.180104E-38 double:4.160776E-317;
        r17 = r9 & r15;
        r15 = (r17 > r12 ? 1 : (r17 == r12 ? 0 : -1));
        if (r15 == 0) goto L_0x0051;
        r1.setPosition(r3);
        return r7;
        r12 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r15 = r9 & r12;
        r17 = 8;
        r17 = r9 >> r17;
        r19 = r17 & r12;
        r17 = 7;
        r17 = r19 << r17;
        r19 = r15 | r17;
        r15 = 16;
        r15 = r9 >> r15;
        r17 = r15 & r12;
        r15 = 14;
        r15 = r17 << r15;
        r17 = r19 | r15;
        r15 = 24;
        r15 = r9 >> r15;
        r19 = r15 & r12;
        r12 = 21;
        r12 = r19 << r12;
        r9 = r17 | r12;
        r12 = 0;
        r13 = 0;
        if (r2 != r14) goto L_0x008d;
        r4 = r11 & 64;
        if (r4 == 0) goto L_0x0083;
        r4 = r5;
        goto L_0x0084;
        r4 = r7;
        r12 = r4;
        r4 = r11 & 1;
        if (r4 == 0) goto L_0x008a;
        goto L_0x008b;
        r5 = r7;
        r13 = r5;
        goto L_0x009e;
        if (r2 != r4) goto L_0x009e;
        r4 = r11 & 32;
        if (r4 == 0) goto L_0x0095;
        r4 = r5;
        goto L_0x0096;
        r4 = r7;
        r12 = r4;
        r4 = r11 & 128;
        if (r4 == 0) goto L_0x009c;
        goto L_0x009d;
        r5 = r7;
        r13 = r5;
        r4 = 0;
        if (r12 == 0) goto L_0x00a3;
        r4 = r4 + 1;
        if (r13 == 0) goto L_0x00a7;
        r4 = r4 + 4;
        r21 = r8;
        r7 = (long) r4;
        r5 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
        if (r5 >= 0) goto L_0x00b4;
        r1.setPosition(r3);
        r5 = 0;
        return r5;
        r5 = r22.bytesLeft();	 Catch:{ all -> 0x0022 }
        r7 = (long) r5;
        r5 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r5 >= 0) goto L_0x00c3;
        r1.setPosition(r3);
        r5 = 0;
        return r5;
        r5 = (int) r9;
        r1.skipBytes(r5);	 Catch:{ all -> 0x0022 }
        goto L_0x0008;
        r1.setPosition(r3);
        return r5;
    L_0x00ce:
        r0 = move-exception;
        r6 = r24;
        r4 = r0;
        r1.setPosition(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.metadata.id3.Id3Decoder.validateFrames(org.telegram.messenger.exoplayer2.util.ParsableByteArray, int, int, boolean):boolean");
    }

    public Id3Decoder() {
        this(null);
    }

    public Id3Decoder(FramePredicate framePredicate) {
        this.framePredicate = framePredicate;
    }

    public Metadata decode(MetadataInputBuffer inputBuffer) {
        ByteBuffer buffer = inputBuffer.data;
        return decode(buffer.array(), buffer.limit());
    }

    public Metadata decode(byte[] data, int size) {
        List id3Frames = new ArrayList();
        ParsableByteArray id3Data = new ParsableByteArray(data, size);
        Id3Header id3Header = decodeHeader(id3Data);
        if (id3Header == null) {
            return null;
        }
        int startPosition = id3Data.getPosition();
        int frameHeaderSize = id3Header.majorVersion == 2 ? 6 : 10;
        int framesSize = id3Header.framesSize;
        if (id3Header.isUnsynchronized) {
            framesSize = removeUnsynchronization(id3Data, id3Header.framesSize);
        }
        id3Data.setLimit(startPosition + framesSize);
        boolean unsignedIntFrameSizeHack = false;
        if (!validateFrames(id3Data, id3Header.majorVersion, frameHeaderSize, false)) {
            if (id3Header.majorVersion == 4 && validateFrames(id3Data, 4, frameHeaderSize, true)) {
                unsignedIntFrameSizeHack = true;
            } else {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to validate ID3 tag with majorVersion=");
                stringBuilder.append(id3Header.majorVersion);
                Log.w(str, stringBuilder.toString());
                return null;
            }
        }
        while (id3Data.bytesLeft() >= frameHeaderSize) {
            Id3Frame frame = decodeFrame(id3Header.majorVersion, id3Data, unsignedIntFrameSizeHack, frameHeaderSize, this.framePredicate);
            if (frame != null) {
                id3Frames.add(frame);
            }
        }
        return new Metadata(id3Frames);
    }

    private static TextInformationFrame decodeTxxxFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        if (frameSize < 1) {
            return null;
        }
        String value;
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[(frameSize - 1)];
        id3Data.readBytes(data, 0, frameSize - 1);
        int descriptionEndIndex = indexOfEos(data, 0, encoding);
        String description = new String(data, 0, descriptionEndIndex, charset);
        int valueStartIndex = delimiterLength(encoding) + descriptionEndIndex;
        if (valueStartIndex < data.length) {
            value = new String(data, valueStartIndex, indexOfEos(data, valueStartIndex, encoding) - valueStartIndex, charset);
        } else {
            value = TtmlNode.ANONYMOUS_REGION_ID;
        }
        return new TextInformationFrame("TXXX", description, value);
    }

    private static TextInformationFrame decodeTextInformationFrame(ParsableByteArray id3Data, int frameSize, String id) throws UnsupportedEncodingException {
        if (frameSize < 1) {
            return null;
        }
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[(frameSize - 1)];
        id3Data.readBytes(data, 0, frameSize - 1);
        return new TextInformationFrame(id, null, new String(data, 0, indexOfEos(data, 0, encoding), charset));
    }

    private static UrlLinkFrame decodeWxxxFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        if (frameSize < 1) {
            return null;
        }
        String url;
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[(frameSize - 1)];
        id3Data.readBytes(data, 0, frameSize - 1);
        int descriptionEndIndex = indexOfEos(data, 0, encoding);
        String description = new String(data, 0, descriptionEndIndex, charset);
        int urlStartIndex = delimiterLength(encoding) + descriptionEndIndex;
        if (urlStartIndex < data.length) {
            url = new String(data, urlStartIndex, indexOfZeroByte(data, urlStartIndex) - urlStartIndex, "ISO-8859-1");
        } else {
            url = TtmlNode.ANONYMOUS_REGION_ID;
        }
        return new UrlLinkFrame("WXXX", description, url);
    }

    private static UrlLinkFrame decodeUrlLinkFrame(ParsableByteArray id3Data, int frameSize, String id) throws UnsupportedEncodingException {
        byte[] data = new byte[frameSize];
        id3Data.readBytes(data, 0, frameSize);
        return new UrlLinkFrame(id, null, new String(data, 0, indexOfZeroByte(data, 0), "ISO-8859-1"));
    }

    private static PrivFrame decodePrivFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        byte[] data = new byte[frameSize];
        id3Data.readBytes(data, 0, frameSize);
        int ownerEndIndex = indexOfZeroByte(data, 0);
        return new PrivFrame(new String(data, 0, ownerEndIndex, "ISO-8859-1"), copyOfRangeIfValid(data, ownerEndIndex + 1, data.length));
    }

    private static GeobFrame decodeGeobFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[(frameSize - 1)];
        id3Data.readBytes(data, 0, frameSize - 1);
        int mimeTypeEndIndex = indexOfZeroByte(data, 0);
        String mimeType = new String(data, 0, mimeTypeEndIndex, "ISO-8859-1");
        int filenameStartIndex = mimeTypeEndIndex + 1;
        int filenameEndIndex = indexOfEos(data, filenameStartIndex, encoding);
        String filename = new String(data, filenameStartIndex, filenameEndIndex - filenameStartIndex, charset);
        int descriptionStartIndex = delimiterLength(encoding) + filenameEndIndex;
        int descriptionEndIndex = indexOfEos(data, descriptionStartIndex, encoding);
        return new GeobFrame(mimeType, filename, new String(data, descriptionStartIndex, descriptionEndIndex - descriptionStartIndex, charset), copyOfRangeIfValid(data, delimiterLength(encoding) + descriptionEndIndex, data.length));
    }

    private static ApicFrame decodeApicFrame(ParsableByteArray id3Data, int frameSize, int majorVersion) throws UnsupportedEncodingException {
        int mimeTypeEndIndex;
        String mimeType;
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[(frameSize - 1)];
        id3Data.readBytes(data, 0, frameSize - 1);
        StringBuilder stringBuilder;
        if (majorVersion == 2) {
            mimeTypeEndIndex = 2;
            stringBuilder = new StringBuilder();
            stringBuilder.append("image/");
            stringBuilder.append(Util.toLowerInvariant(new String(data, 0, 3, "ISO-8859-1")));
            mimeType = stringBuilder.toString();
            if (mimeType.equals("image/jpg")) {
                mimeType = "image/jpeg";
            }
        } else {
            mimeTypeEndIndex = indexOfZeroByte(data, 0);
            mimeType = Util.toLowerInvariant(new String(data, 0, mimeTypeEndIndex, "ISO-8859-1"));
            if (mimeType.indexOf(47) == -1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("image/");
                stringBuilder.append(mimeType);
                mimeType = stringBuilder.toString();
            }
        }
        int pictureType = data[mimeTypeEndIndex + 1] & 255;
        int descriptionStartIndex = mimeTypeEndIndex + 2;
        int descriptionEndIndex = indexOfEos(data, descriptionStartIndex, encoding);
        return new ApicFrame(mimeType, new String(data, descriptionStartIndex, descriptionEndIndex - descriptionStartIndex, charset), pictureType, copyOfRangeIfValid(data, delimiterLength(encoding) + descriptionEndIndex, data.length));
    }

    private static CommentFrame decodeCommentFrame(ParsableByteArray id3Data, int frameSize) throws UnsupportedEncodingException {
        if (frameSize < 4) {
            return null;
        }
        String text;
        int encoding = id3Data.readUnsignedByte();
        String charset = getCharsetName(encoding);
        byte[] data = new byte[3];
        id3Data.readBytes(data, 0, 3);
        String language = new String(data, 0, 3);
        data = new byte[(frameSize - 4)];
        id3Data.readBytes(data, 0, frameSize - 4);
        int descriptionEndIndex = indexOfEos(data, 0, encoding);
        String description = new String(data, 0, descriptionEndIndex, charset);
        int textStartIndex = delimiterLength(encoding) + descriptionEndIndex;
        if (textStartIndex < data.length) {
            text = new String(data, textStartIndex, indexOfEos(data, textStartIndex, encoding) - textStartIndex, charset);
        } else {
            text = TtmlNode.ANONYMOUS_REGION_ID;
        }
        return new CommentFrame(language, description, text);
    }

    private static BinaryFrame decodeBinaryFrame(ParsableByteArray id3Data, int frameSize, String id) {
        byte[] frame = new byte[frameSize];
        id3Data.readBytes(frame, 0, frameSize);
        return new BinaryFrame(id, frame);
    }

    private static int removeUnsynchronization(ParsableByteArray data, int length) {
        byte[] bytes = data.data;
        int i = data.getPosition();
        while (i + 1 < length) {
            if ((bytes[i] & 255) == 255 && bytes[i + 1] == (byte) 0) {
                System.arraycopy(bytes, i + 2, bytes, i + 1, (length - i) - 2);
                length--;
            }
            i++;
        }
        return length;
    }

    private static String getCharsetName(int encodingByte) {
        switch (encodingByte) {
            case 0:
                return "ISO-8859-1";
            case 1:
                return C.UTF16_NAME;
            case 2:
                return "UTF-16BE";
            case 3:
                return C.UTF8_NAME;
            default:
                return "ISO-8859-1";
        }
    }

    private static String getFrameId(int majorVersion, int frameId0, int frameId1, int frameId2, int frameId3) {
        if (majorVersion == 2) {
            return String.format(Locale.US, "%c%c%c", new Object[]{Integer.valueOf(frameId0), Integer.valueOf(frameId1), Integer.valueOf(frameId2)});
        }
        return String.format(Locale.US, "%c%c%c%c", new Object[]{Integer.valueOf(frameId0), Integer.valueOf(frameId1), Integer.valueOf(frameId2), Integer.valueOf(frameId3)});
    }

    private static int indexOfEos(byte[] data, int fromIndex, int encoding) {
        int terminationPos = indexOfZeroByte(data, fromIndex);
        if (encoding != 0) {
            if (encoding != 3) {
                while (terminationPos < data.length - 1) {
                    if (terminationPos % 2 == 0 && data[terminationPos + 1] == (byte) 0) {
                        return terminationPos;
                    }
                    terminationPos = indexOfZeroByte(data, terminationPos + 1);
                }
                return data.length;
            }
        }
        return terminationPos;
    }

    private static int indexOfZeroByte(byte[] data, int fromIndex) {
        for (int i = fromIndex; i < data.length; i++) {
            if (data[i] == (byte) 0) {
                return i;
            }
        }
        return data.length;
    }

    private static int delimiterLength(int encodingByte) {
        if (encodingByte != 0) {
            if (encodingByte != 3) {
                return 2;
            }
        }
        return 1;
    }

    private static byte[] copyOfRangeIfValid(byte[] data, int from, int to) {
        if (to <= from) {
            return new byte[0];
        }
        return Arrays.copyOfRange(data, from, to);
    }
}
