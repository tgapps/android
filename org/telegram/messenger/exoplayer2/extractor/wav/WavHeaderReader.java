package org.telegram.messenger.exoplayer2.extractor.wav;

import android.util.Log;
import java.io.IOException;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.extractor.ExtractorInput;
import org.telegram.messenger.exoplayer2.upstream.DataSchemeDataSource;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.Util;

final class WavHeaderReader {
    private static final String TAG = "WavHeaderReader";
    private static final int TYPE_FLOAT = 3;
    private static final int TYPE_PCM = 1;
    private static final int TYPE_WAVE_FORMAT_EXTENSIBLE = 65534;

    private static final class ChunkHeader {
        public static final int SIZE_IN_BYTES = 8;
        public final int id;
        public final long size;

        private ChunkHeader(int id, long size) {
            this.id = id;
            this.size = size;
        }

        public static ChunkHeader peek(ExtractorInput input, ParsableByteArray scratch) throws IOException, InterruptedException {
            input.peekFully(scratch.data, 0, 8);
            scratch.setPosition(0);
            return new ChunkHeader(scratch.readInt(), scratch.readLittleEndianUnsignedInt());
        }
    }

    public static org.telegram.messenger.exoplayer2.extractor.wav.WavHeader peek(org.telegram.messenger.exoplayer2.extractor.ExtractorInput r1) throws java.io.IOException, java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.wav.WavHeaderReader.peek(org.telegram.messenger.exoplayer2.extractor.ExtractorInput):org.telegram.messenger.exoplayer2.extractor.wav.WavHeader
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r20;
        org.telegram.messenger.exoplayer2.util.Assertions.checkNotNull(r20);
        r1 = new org.telegram.messenger.exoplayer2.util.ParsableByteArray;
        r2 = 16;
        r1.<init>(r2);
        r3 = org.telegram.messenger.exoplayer2.extractor.wav.WavHeaderReader.ChunkHeader.peek(r0, r1);
        r4 = r3.id;
        r5 = "RIFF";
        r5 = org.telegram.messenger.exoplayer2.util.Util.getIntegerCodeForString(r5);
        r6 = 0;
        if (r4 == r5) goto L_0x001c;
    L_0x001b:
        return r6;
    L_0x001c:
        r4 = r1.data;
        r5 = 4;
        r7 = 0;
        r0.peekFully(r4, r7, r5);
        r1.setPosition(r7);
        r4 = r1.readInt();
        r8 = "WAVE";
        r8 = org.telegram.messenger.exoplayer2.util.Util.getIntegerCodeForString(r8);
        if (r4 == r8) goto L_0x0049;
    L_0x0032:
        r2 = "WavHeaderReader";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r7 = "Unsupported RIFF format: ";
        r5.append(r7);
        r5.append(r4);
        r5 = r5.toString();
        android.util.Log.e(r2, r5);
        return r6;
    L_0x0049:
        r3 = org.telegram.messenger.exoplayer2.extractor.wav.WavHeaderReader.ChunkHeader.peek(r0, r1);
    L_0x004d:
        r8 = r3.id;
        r9 = "fmt ";
        r9 = org.telegram.messenger.exoplayer2.util.Util.getIntegerCodeForString(r9);
        if (r8 == r9) goto L_0x0062;
    L_0x0057:
        r8 = r3.size;
        r8 = (int) r8;
        r0.advancePeekPosition(r8);
        r3 = org.telegram.messenger.exoplayer2.extractor.wav.WavHeaderReader.ChunkHeader.peek(r0, r1);
        goto L_0x004d;
    L_0x0062:
        r8 = r3.size;
        r10 = 16;
        r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        r8 = 1;
        if (r12 < 0) goto L_0x006d;
    L_0x006b:
        r9 = r8;
        goto L_0x006e;
    L_0x006d:
        r9 = r7;
    L_0x006e:
        org.telegram.messenger.exoplayer2.util.Assertions.checkState(r9);
        r9 = r1.data;
        r0.peekFully(r9, r7, r2);
        r1.setPosition(r7);
        r9 = r1.readLittleEndianUnsignedShort();
        r17 = r1.readLittleEndianUnsignedShort();
        r18 = r1.readLittleEndianUnsignedIntToInt();
        r19 = r1.readLittleEndianUnsignedIntToInt();
        r15 = r1.readLittleEndianUnsignedShort();
        r14 = r1.readLittleEndianUnsignedShort();
        r10 = r17 * r14;
        r13 = r10 / 8;
        if (r15 == r13) goto L_0x00b6;
    L_0x0097:
        r2 = new org.telegram.messenger.exoplayer2.ParserException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Expected block alignment: ";
        r5.append(r6);
        r5.append(r13);
        r6 = "; got: ";
        r5.append(r6);
        r5.append(r15);
        r5 = r5.toString();
        r2.<init>(r5);
        throw r2;
    L_0x00b6:
        if (r9 == r8) goto L_0x00de;
    L_0x00b8:
        r8 = 3;
        if (r9 == r8) goto L_0x00d7;
    L_0x00bb:
        r5 = 65534; // 0xfffe float:9.1833E-41 double:3.2378E-319;
        if (r9 == r5) goto L_0x00de;
    L_0x00c0:
        r2 = "WavHeaderReader";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r7 = "Unsupported WAV format type: ";
        r5.append(r7);
        r5.append(r9);
        r5 = r5.toString();
        android.util.Log.e(r2, r5);
        return r6;
    L_0x00d7:
        r8 = 32;
        if (r14 != r8) goto L_0x00dc;
    L_0x00db:
        goto L_0x00dd;
    L_0x00dc:
        r5 = r7;
    L_0x00dd:
        goto L_0x00e3;
    L_0x00de:
        r5 = org.telegram.messenger.exoplayer2.util.Util.getPcmEncoding(r14);
        if (r5 != 0) goto L_0x0105;
        r2 = "WavHeaderReader";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Unsupported WAV bit depth ";
        r7.append(r8);
        r7.append(r14);
        r8 = " for type ";
        r7.append(r8);
        r7.append(r9);
        r7 = r7.toString();
        android.util.Log.e(r2, r7);
        return r6;
        r6 = r3.size;
        r6 = (int) r6;
        r6 = r6 - r2;
        r0.advancePeekPosition(r6);
        r2 = new org.telegram.messenger.exoplayer2.extractor.wav.WavHeader;
        r10 = r2;
        r11 = r17;
        r12 = r18;
        r6 = r13;
        r13 = r19;
        r7 = r14;
        r14 = r15;
        r8 = r15;
        r15 = r7;
        r16 = r5;
        r10.<init>(r11, r12, r13, r14, r15, r16);
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.wav.WavHeaderReader.peek(org.telegram.messenger.exoplayer2.extractor.ExtractorInput):org.telegram.messenger.exoplayer2.extractor.wav.WavHeader");
    }

    WavHeaderReader() {
    }

    public static void skipToData(ExtractorInput input, WavHeader wavHeader) throws IOException, InterruptedException {
        Assertions.checkNotNull(input);
        Assertions.checkNotNull(wavHeader);
        input.resetPeekPosition();
        ParsableByteArray scratch = new ParsableByteArray(8);
        ChunkHeader chunkHeader = ChunkHeader.peek(input, scratch);
        while (chunkHeader.id != Util.getIntegerCodeForString(DataSchemeDataSource.SCHEME_DATA)) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignoring unknown WAV chunk: ");
            stringBuilder.append(chunkHeader.id);
            Log.w(str, stringBuilder.toString());
            long bytesToSkip = 8 + chunkHeader.size;
            if (chunkHeader.id == Util.getIntegerCodeForString("RIFF")) {
                bytesToSkip = 12;
            }
            if (bytesToSkip > 2147483647L) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Chunk is too large (~2GB+) to skip; id: ");
                stringBuilder2.append(chunkHeader.id);
                throw new ParserException(stringBuilder2.toString());
            }
            input.skipFully((int) bytesToSkip);
            chunkHeader = ChunkHeader.peek(input, scratch);
        }
        input.skipFully(8);
        wavHeader.setDataBounds(input.getPosition(), chunkHeader.size);
    }
}
