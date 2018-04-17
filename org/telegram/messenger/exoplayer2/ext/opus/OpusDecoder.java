package org.telegram.messenger.exoplayer2.ext.opus;

import java.nio.ByteBuffer;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.decoder.CryptoInfo;
import org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer;
import org.telegram.messenger.exoplayer2.decoder.SimpleDecoder;
import org.telegram.messenger.exoplayer2.decoder.SimpleOutputBuffer;
import org.telegram.messenger.exoplayer2.drm.DecryptionException;
import org.telegram.messenger.exoplayer2.drm.ExoMediaCrypto;

final class OpusDecoder extends SimpleDecoder<DecoderInputBuffer, SimpleOutputBuffer, OpusDecoderException> {
    private static final int DECODE_ERROR = -1;
    private static final int DEFAULT_SEEK_PRE_ROLL_SAMPLES = 3840;
    private static final int DRM_ERROR = -2;
    private static final int NO_ERROR = 0;
    private static final int SAMPLE_RATE = 48000;
    private final int channelCount;
    private final ExoMediaCrypto exoMediaCrypto;
    private final int headerSeekPreRollSamples;
    private final int headerSkipSamples;
    private final long nativeDecoderContext;
    private int skipSamples;

    public OpusDecoder(int r1, int r2, int r3, java.util.List<byte[]> r4, org.telegram.messenger.exoplayer2.drm.ExoMediaCrypto r5) throws org.telegram.messenger.exoplayer2.ext.opus.OpusDecoderException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.ext.opus.OpusDecoder.<init>(int, int, int, java.util.List, org.telegram.messenger.exoplayer2.drm.ExoMediaCrypto):void
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
        r7 = r18;
        r8 = r22;
        r9 = r23;
        r10 = r19;
        r0 = new org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer[r10];
        r11 = r20;
        r1 = new org.telegram.messenger.exoplayer2.decoder.SimpleOutputBuffer[r11];
        r7.<init>(r0, r1);
        r7.exoMediaCrypto = r9;
        if (r9 == 0) goto L_0x0023;
    L_0x0015:
        r0 = org.telegram.messenger.exoplayer2.ext.opus.OpusLibrary.opusIsSecureDecodeSupported();
        if (r0 != 0) goto L_0x0023;
    L_0x001b:
        r0 = new org.telegram.messenger.exoplayer2.ext.opus.OpusDecoderException;
        r1 = "Opus decoder does not support secure decode.";
        r0.<init>(r1);
        throw r0;
    L_0x0023:
        r0 = 0;
        r1 = r8.get(r0);
        r12 = r1;
        r12 = (byte[]) r12;
        r1 = 19;
        r2 = r12.length;
        if (r2 >= r1) goto L_0x0038;
    L_0x0030:
        r0 = new org.telegram.messenger.exoplayer2.ext.opus.OpusDecoderException;
        r1 = "Header size is too small.";
        r0.<init>(r1);
        throw r0;
    L_0x0038:
        r2 = 9;
        r2 = r12[r2];
        r2 = r2 & 255;
        r7.channelCount = r2;
        r2 = r7.channelCount;
        r3 = 8;
        if (r2 <= r3) goto L_0x005f;
    L_0x0046:
        r0 = new org.telegram.messenger.exoplayer2.ext.opus.OpusDecoderException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Invalid channel count: ";
        r1.append(r2);
        r2 = r7.channelCount;
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x005f:
        r2 = 10;
        r13 = readLittleEndian16(r12, r2);
        r2 = 16;
        r14 = readLittleEndian16(r12, r2);
        r15 = new byte[r3];
        r2 = 18;
        r2 = r12[r2];
        r4 = 2;
        r5 = 1;
        if (r2 != 0) goto L_0x0092;
    L_0x0075:
        r1 = r7.channelCount;
        if (r1 <= r4) goto L_0x0081;
    L_0x0079:
        r0 = new org.telegram.messenger.exoplayer2.ext.opus.OpusDecoderException;
        r1 = "Invalid Header, missing stream map.";
        r0.<init>(r1);
        throw r0;
    L_0x0081:
        r1 = 1;
        r2 = r7.channelCount;
        if (r2 != r4) goto L_0x0088;
    L_0x0086:
        r2 = r5;
        goto L_0x0089;
    L_0x0088:
        r2 = r0;
    L_0x0089:
        r15[r0] = r0;
        r15[r5] = r5;
        r16 = r1;
        r17 = r2;
        goto L_0x00b5;
    L_0x0092:
        r2 = 21;
        r6 = r12.length;
        r4 = r7.channelCount;
        r4 = r4 + r2;
        if (r6 >= r4) goto L_0x00a2;
    L_0x009a:
        r0 = new org.telegram.messenger.exoplayer2.ext.opus.OpusDecoderException;
        r1 = "Header size is too small.";
        r0.<init>(r1);
        throw r0;
    L_0x00a2:
        r1 = r12[r1];
        r1 = r1 & 255;
        r4 = 20;
        r4 = r12[r4];
        r4 = r4 & 255;
        r6 = r7.channelCount;
        java.lang.System.arraycopy(r12, r2, r15, r0, r6);
        r16 = r1;
        r17 = r4;
    L_0x00b5:
        r0 = r22.size();
        r1 = 3;
        if (r0 != r1) goto L_0x0114;
    L_0x00bc:
        r0 = r8.get(r5);
        r0 = (byte[]) r0;
        r0 = r0.length;
        if (r0 != r3) goto L_0x010c;
    L_0x00c5:
        r0 = 2;
        r1 = r8.get(r0);
        r1 = (byte[]) r1;
        r0 = r1.length;
        if (r0 == r3) goto L_0x00d0;
    L_0x00cf:
        goto L_0x010c;
        r0 = r8.get(r5);
        r0 = (byte[]) r0;
        r0 = java.nio.ByteBuffer.wrap(r0);
        r1 = java.nio.ByteOrder.nativeOrder();
        r0 = r0.order(r1);
        r0 = r0.getLong();
        r2 = 2;
        r2 = r8.get(r2);
        r2 = (byte[]) r2;
        r2 = java.nio.ByteBuffer.wrap(r2);
        r3 = java.nio.ByteOrder.nativeOrder();
        r2 = r2.order(r3);
        r2 = r2.getLong();
        r4 = nsToSamples(r0);
        r7.headerSkipSamples = r4;
        r4 = nsToSamples(r2);
        r7.headerSeekPreRollSamples = r4;
        goto L_0x011a;
    L_0x010c:
        r0 = new org.telegram.messenger.exoplayer2.ext.opus.OpusDecoderException;
        r1 = "Invalid Codec Delay or Seek Preroll";
        r0.<init>(r1);
        throw r0;
    L_0x0114:
        r7.headerSkipSamples = r13;
        r0 = 3840; // 0xf00 float:5.381E-42 double:1.897E-320;
        r7.headerSeekPreRollSamples = r0;
        r1 = 48000; // 0xbb80 float:6.7262E-41 double:2.3715E-319;
        r2 = r7.channelCount;
        r0 = r7;
        r3 = r16;
        r4 = r17;
        r5 = r14;
        r6 = r15;
        r0 = r0.opusInit(r1, r2, r3, r4, r5, r6);
        r7.nativeDecoderContext = r0;
        r0 = r7.nativeDecoderContext;
        r2 = 0;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 != 0) goto L_0x013c;
        r0 = new org.telegram.messenger.exoplayer2.ext.opus.OpusDecoderException;
        r1 = "Failed to initialize decoder";
        r0.<init>(r1);
        throw r0;
        r0 = r21;
        r7.setInitialInputBufferSize(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.ext.opus.OpusDecoder.<init>(int, int, int, java.util.List, org.telegram.messenger.exoplayer2.drm.ExoMediaCrypto):void");
    }

    private native void opusClose(long j);

    private native int opusDecode(long j, long j2, ByteBuffer byteBuffer, int i, SimpleOutputBuffer simpleOutputBuffer);

    private native int opusGetErrorCode(long j);

    private native String opusGetErrorMessage(long j);

    private native long opusInit(int i, int i2, int i3, int i4, int i5, byte[] bArr);

    private native void opusReset(long j);

    private native int opusSecureDecode(long j, long j2, ByteBuffer byteBuffer, int i, SimpleOutputBuffer simpleOutputBuffer, int i2, ExoMediaCrypto exoMediaCrypto, int i3, byte[] bArr, byte[] bArr2, int i4, int[] iArr, int[] iArr2);

    public String getName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("libopus");
        stringBuilder.append(OpusLibrary.getVersion());
        return stringBuilder.toString();
    }

    protected DecoderInputBuffer createInputBuffer() {
        return new DecoderInputBuffer(2);
    }

    protected SimpleOutputBuffer createOutputBuffer() {
        return new SimpleOutputBuffer(this);
    }

    protected OpusDecoderException createUnexpectedDecodeException(Throwable error) {
        return new OpusDecoderException("Unexpected decode error", error);
    }

    protected OpusDecoderException decode(DecoderInputBuffer inputBuffer, SimpleOutputBuffer outputBuffer, boolean reset) {
        int result;
        OpusDecoder opusDecoder = this;
        DecoderInputBuffer decoderInputBuffer = inputBuffer;
        SimpleOutputBuffer simpleOutputBuffer = outputBuffer;
        if (reset) {
            opusReset(opusDecoder.nativeDecoderContext);
            opusDecoder.skipSamples = decoderInputBuffer.timeUs == 0 ? opusDecoder.headerSkipSamples : opusDecoder.headerSeekPreRollSamples;
        }
        ByteBuffer inputData = decoderInputBuffer.data;
        CryptoInfo cryptoInfo = decoderInputBuffer.cryptoInfo;
        ByteBuffer byteBuffer;
        if (inputBuffer.isEncrypted()) {
            long j = opusDecoder.nativeDecoderContext;
            long j2 = decoderInputBuffer.timeUs;
            int limit = inputData.limit();
            ExoMediaCrypto exoMediaCrypto = opusDecoder.exoMediaCrypto;
            int i = cryptoInfo.mode;
            byte[] bArr = cryptoInfo.key;
            byte[] bArr2 = cryptoInfo.iv;
            byte[] bArr3 = bArr2;
            byte[] bArr4 = bArr;
            ByteBuffer inputData2 = inputData;
            result = opusSecureDecode(j, j2, inputData, limit, simpleOutputBuffer, SAMPLE_RATE, exoMediaCrypto, i, bArr4, bArr3, cryptoInfo.numSubSamples, cryptoInfo.numBytesOfClearData, cryptoInfo.numBytesOfEncryptedData);
            byteBuffer = inputData2;
            OpusDecoder opusDecoder2 = this;
            DecoderInputBuffer decoderInputBuffer2 = inputBuffer;
        } else {
            byteBuffer = inputData;
            result = opusDecode(this.nativeDecoderContext, inputBuffer.timeUs, byteBuffer, byteBuffer.limit(), outputBuffer);
        }
        if (result >= 0) {
            SimpleOutputBuffer simpleOutputBuffer2 = outputBuffer;
            ByteBuffer outputData = simpleOutputBuffer2.data;
            outputData.position(0);
            outputData.limit(result);
            if (opusDecoder2.skipSamples > 0) {
                int bytesPerSample = opusDecoder2.channelCount * 2;
                int skipBytes = opusDecoder2.skipSamples * bytesPerSample;
                if (result <= skipBytes) {
                    opusDecoder2.skipSamples -= result / bytesPerSample;
                    simpleOutputBuffer2.addFlag(Integer.MIN_VALUE);
                    outputData.position(result);
                } else {
                    opusDecoder2.skipSamples = 0;
                    outputData.position(skipBytes);
                }
            }
            return null;
        } else if (result == -2) {
            String message = new StringBuilder();
            message.append("Drm error: ");
            message.append(opusDecoder2.opusGetErrorMessage(opusDecoder2.nativeDecoderContext));
            message = message.toString();
            return new OpusDecoderException(message, new DecryptionException(opusDecoder2.opusGetErrorCode(opusDecoder2.nativeDecoderContext), message));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Decode error: ");
            stringBuilder.append(opusDecoder2.opusGetErrorMessage((long) result));
            return new OpusDecoderException(stringBuilder.toString());
        }
    }

    public void release() {
        super.release();
        opusClose(this.nativeDecoderContext);
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public int getSampleRate() {
        return SAMPLE_RATE;
    }

    private static int nsToSamples(long ns) {
        return (int) ((48000 * ns) / C.NANOS_PER_SECOND);
    }

    private static int readLittleEndian16(byte[] input, int offset) {
        return (input[offset] & 255) | ((input[offset + 1] & 255) << 8);
    }
}
