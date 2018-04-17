package org.telegram.messenger.exoplayer2.ext.flac;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.telegram.messenger.exoplayer2.extractor.Extractor;
import org.telegram.messenger.exoplayer2.extractor.ExtractorInput;
import org.telegram.messenger.exoplayer2.extractor.ExtractorOutput;
import org.telegram.messenger.exoplayer2.extractor.ExtractorsFactory;
import org.telegram.messenger.exoplayer2.extractor.SeekMap;
import org.telegram.messenger.exoplayer2.extractor.SeekMap.SeekPoints;
import org.telegram.messenger.exoplayer2.extractor.SeekPoint;
import org.telegram.messenger.exoplayer2.extractor.TrackOutput;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;

public final class FlacExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() {
        public Extractor[] createExtractors() {
            return new Extractor[]{new FlacExtractor()};
        }
    };
    private static final byte[] FLAC_SIGNATURE = new byte[]{(byte) 102, (byte) 76, (byte) 97, (byte) 67, (byte) 0, (byte) 0, (byte) 0, (byte) 34};
    private FlacDecoderJni decoderJni;
    private ExtractorOutput extractorOutput;
    private boolean metadataParsed;
    private ParsableByteArray outputBuffer;
    private ByteBuffer outputByteBuffer;
    private TrackOutput trackOutput;

    private static final class FlacSeekMap implements SeekMap {
        private final FlacDecoderJni decoderJni;
        private final long durationUs;

        public FlacSeekMap(long durationUs, FlacDecoderJni decoderJni) {
            this.durationUs = durationUs;
            this.decoderJni = decoderJni;
        }

        public boolean isSeekable() {
            return true;
        }

        public SeekPoints getSeekPoints(long timeUs) {
            return new SeekPoints(new SeekPoint(timeUs, this.decoderJni.getSeekPosition(timeUs)));
        }

        public long getDurationUs() {
            return this.durationUs;
        }
    }

    public int read(org.telegram.messenger.exoplayer2.extractor.ExtractorInput r1, org.telegram.messenger.exoplayer2.extractor.PositionHolder r2) throws java.io.IOException, java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.ext.flac.FlacExtractor.read(org.telegram.messenger.exoplayer2.extractor.ExtractorInput, org.telegram.messenger.exoplayer2.extractor.PositionHolder):int
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
        r1 = r22;
        r2 = r23;
        r3 = r1.decoderJni;
        r3.setData(r2);
        r3 = r1.metadataParsed;
        r4 = 0;
        r5 = 0;
        if (r3 != 0) goto L_0x0096;
    L_0x0010:
        r3 = r1.decoderJni;	 Catch:{ IOException -> 0x008b }
        r3 = r3.decodeMetadata();	 Catch:{ IOException -> 0x008b }
        if (r3 != 0) goto L_0x0020;	 Catch:{ IOException -> 0x008b }
    L_0x0018:
        r4 = new java.io.IOException;	 Catch:{ IOException -> 0x008b }
        r7 = "Metadata decoding failed";	 Catch:{ IOException -> 0x008b }
        r4.<init>(r7);	 Catch:{ IOException -> 0x008b }
        throw r4;	 Catch:{ IOException -> 0x008b }
        r7 = 1;
        r1.metadataParsed = r7;
        r8 = r1.decoderJni;
        r8 = r8.getSeekPosition(r5);
        r10 = -1;
        r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r12 == 0) goto L_0x0032;
        goto L_0x0033;
        r7 = r4;
        r8 = r1.extractorOutput;
        if (r7 == 0) goto L_0x0043;
        r9 = new org.telegram.messenger.exoplayer2.ext.flac.FlacExtractor$FlacSeekMap;
        r10 = r3.durationUs();
        r12 = r1.decoderJni;
        r9.<init>(r10, r12);
        goto L_0x004c;
        r9 = new org.telegram.messenger.exoplayer2.extractor.SeekMap$Unseekable;
        r10 = r3.durationUs();
        r9.<init>(r10, r5);
        r8.seekMap(r9);
        r10 = 0;
        r11 = "audio/raw";
        r12 = 0;
        r13 = r3.bitRate();
        r14 = -1;
        r15 = r3.channels;
        r8 = r3.sampleRate;
        r9 = r3.bitsPerSample;
        r17 = org.telegram.messenger.exoplayer2.util.Util.getPcmEncoding(r9);
        r18 = 0;
        r19 = 0;
        r20 = 0;
        r21 = 0;
        r16 = r8;
        r8 = org.telegram.messenger.exoplayer2.Format.createAudioSampleFormat(r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21);
        r9 = r1.trackOutput;
        r9.format(r8);
        r9 = new org.telegram.messenger.exoplayer2.util.ParsableByteArray;
        r10 = r3.maxDecodedFrameSize();
        r9.<init>(r10);
        r1.outputBuffer = r9;
        r9 = r1.outputBuffer;
        r9 = r9.data;
        r9 = java.nio.ByteBuffer.wrap(r9);
        r1.outputByteBuffer = r9;
        goto L_0x0096;
    L_0x008b:
        r0 = move-exception;
        r3 = r0;
        r4 = r1.decoderJni;
        r4.reset(r5);
        r2.setRetryPosition(r5, r3);
        throw r3;
    L_0x0096:
        r3 = r1.outputBuffer;
        r3.reset();
        r3 = r1.decoderJni;
        r7 = r3.getDecodePosition();
        r3 = r1.decoderJni;	 Catch:{ IOException -> 0x00d0 }
        r9 = r1.outputByteBuffer;	 Catch:{ IOException -> 0x00d0 }
        r3 = r3.decodeSample(r9);	 Catch:{ IOException -> 0x00d0 }
        r5 = -1;
        if (r3 > 0) goto L_0x00af;
        return r5;
        r6 = r1.trackOutput;
        r9 = r1.outputBuffer;
        r6.sampleData(r9, r3);
        r9 = r1.trackOutput;
        r6 = r1.decoderJni;
        r10 = r6.getLastSampleTimestamp();
        r12 = 1;
        r14 = 0;
        r15 = 0;
        r13 = r3;
        r9.sampleMetadata(r10, r12, r13, r14, r15);
        r6 = r1.decoderJni;
        r6 = r6.isEndOfData();
        if (r6 == 0) goto L_0x00cf;
        r4 = r5;
        return r4;
    L_0x00d0:
        r0 = move-exception;
        r3 = r0;
        r4 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1));
        if (r4 < 0) goto L_0x00de;
        r4 = r1.decoderJni;
        r4.reset(r7);
        r2.setRetryPosition(r7, r3);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.ext.flac.FlacExtractor.read(org.telegram.messenger.exoplayer2.extractor.ExtractorInput, org.telegram.messenger.exoplayer2.extractor.PositionHolder):int");
    }

    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
        this.trackOutput = this.extractorOutput.track(0, 1);
        this.extractorOutput.endTracks();
        try {
            this.decoderJni = new FlacDecoderJni();
        } catch (FlacDecoderException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        byte[] header = new byte[FLAC_SIGNATURE.length];
        input.peekFully(header, 0, FLAC_SIGNATURE.length);
        return Arrays.equals(header, FLAC_SIGNATURE);
    }

    public void seek(long position, long timeUs) {
        if (position == 0) {
            this.metadataParsed = false;
        }
        if (this.decoderJni != null) {
            this.decoderJni.reset(position);
        }
    }

    public void release() {
        if (this.decoderJni != null) {
            this.decoderJni.release();
            this.decoderJni = null;
        }
    }
}
