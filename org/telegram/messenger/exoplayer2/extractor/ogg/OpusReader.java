package org.telegram.messenger.exoplayer2.extractor.ogg;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.Util;

final class OpusReader extends StreamReader {
    private static final int DEFAULT_SEEK_PRE_ROLL_SAMPLES = 3840;
    private static final int OPUS_CODE = Util.getIntegerCodeForString("Opus");
    private static final byte[] OPUS_SIGNATURE = new byte[]{(byte) 79, (byte) 112, (byte) 117, (byte) 115, (byte) 72, (byte) 101, (byte) 97, (byte) 100};
    private static final int SAMPLE_RATE = 48000;
    private boolean headerRead;

    private long getPacketDurationUs(byte[] r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.ogg.OpusReader.getPacketDurationUs(byte[]):long
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
        r0 = r7[r0];
        r0 = r0 & 255;
        r1 = r0 & 3;
        switch(r1) {
            case 0: goto L_0x0012;
            case 1: goto L_0x0010;
            case 2: goto L_0x0010;
            default: goto L_0x000a;
        };
    L_0x000a:
        r1 = 1;
        r1 = r7[r1];
        r1 = r1 & 63;
        goto L_0x0014;
    L_0x0010:
        r1 = 2;
        goto L_0x0014;
    L_0x0012:
        r1 = 1;
        r2 = r0 >> 3;
        r3 = r2 & 3;
        r4 = 16;
        if (r2 < r4) goto L_0x0022;
        r4 = 2500; // 0x9c4 float:3.503E-42 double:1.235E-320;
        r3 = r4 << r3;
        goto L_0x0036;
        r4 = 12;
        r5 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        if (r2 < r4) goto L_0x002d;
        r4 = r3 & 1;
        r3 = r5 << r4;
        goto L_0x0036;
        r4 = 3;
        if (r3 != r4) goto L_0x0034;
        r3 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
        goto L_0x0036;
        r3 = r5 << r3;
        r4 = r1 * r3;
        r4 = (long) r4;
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.ogg.OpusReader.getPacketDurationUs(byte[]):long");
    }

    OpusReader() {
    }

    public static boolean verifyBitstreamType(ParsableByteArray data) {
        if (data.bytesLeft() < OPUS_SIGNATURE.length) {
            return false;
        }
        byte[] header = new byte[OPUS_SIGNATURE.length];
        data.readBytes(header, 0, OPUS_SIGNATURE.length);
        return Arrays.equals(header, OPUS_SIGNATURE);
    }

    protected void reset(boolean headerData) {
        super.reset(headerData);
        if (headerData) {
            this.headerRead = false;
        }
    }

    protected long preparePayload(ParsableByteArray packet) {
        return convertTimeToGranule(getPacketDurationUs(packet.data));
    }

    protected boolean readHeaders(ParsableByteArray packet, long position, SetupData setupData) throws IOException, InterruptedException {
        ParsableByteArray parsableByteArray = packet;
        boolean z = true;
        if (this.headerRead) {
            SetupData setupData2 = setupData;
            if (packet.readInt() != OPUS_CODE) {
                z = false;
            }
            boolean headerPacket = z;
            parsableByteArray.setPosition(0);
            return headerPacket;
        }
        byte[] metadata = Arrays.copyOf(parsableByteArray.data, packet.limit());
        int channelCount = metadata[9] & 255;
        int preskip = ((metadata[11] & 255) << 8) | (metadata[10] & 255);
        List<byte[]> initializationData = new ArrayList(3);
        initializationData.add(metadata);
        putNativeOrderLong(initializationData, preskip);
        putNativeOrderLong(initializationData, DEFAULT_SEEK_PRE_ROLL_SAMPLES);
        setupData.format = Format.createAudioSampleFormat(null, MimeTypes.AUDIO_OPUS, null, -1, -1, channelCount, SAMPLE_RATE, initializationData, null, 0, null);
        r0.headerRead = true;
        return true;
    }

    private void putNativeOrderLong(List<byte[]> initializationData, int samples) {
        initializationData.add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong((((long) samples) * C.NANOS_PER_SECOND) / 48000).array());
    }
}
