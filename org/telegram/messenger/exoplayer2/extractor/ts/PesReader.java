package org.telegram.messenger.exoplayer2.extractor.ts;

import android.util.Log;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.extractor.ExtractorOutput;
import org.telegram.messenger.exoplayer2.extractor.ts.TsPayloadReader.TrackIdGenerator;
import org.telegram.messenger.exoplayer2.util.ParsableBitArray;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;
import org.telegram.messenger.exoplayer2.util.TimestampAdjuster;

public final class PesReader implements TsPayloadReader {
    private static final int HEADER_SIZE = 9;
    private static final int MAX_HEADER_EXTENSION_SIZE = 10;
    private static final int PES_SCRATCH_SIZE = 10;
    private static final int STATE_FINDING_HEADER = 0;
    private static final int STATE_READING_BODY = 3;
    private static final int STATE_READING_HEADER = 1;
    private static final int STATE_READING_HEADER_EXTENSION = 2;
    private static final String TAG = "PesReader";
    private int bytesRead;
    private boolean dataAlignmentIndicator;
    private boolean dtsFlag;
    private int extendedHeaderLength;
    private int payloadSize;
    private final ParsableBitArray pesScratch = new ParsableBitArray(new byte[10]);
    private boolean ptsFlag;
    private final ElementaryStreamReader reader;
    private boolean seenFirstDts;
    private int state = 0;
    private long timeUs;
    private TimestampAdjuster timestampAdjuster;

    public final void consume(org.telegram.messenger.exoplayer2.util.ParsableByteArray r1, boolean r2) throws org.telegram.messenger.exoplayer2.ParserException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.ts.PesReader.consume(org.telegram.messenger.exoplayer2.util.ParsableByteArray, boolean):void
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
        r0 = 1;
        r1 = -1;
        if (r9 == 0) goto L_0x003d;
    L_0x0004:
        r2 = r7.state;
        switch(r2) {
            case 0: goto L_0x0039;
            case 1: goto L_0x0039;
            case 2: goto L_0x0031;
            case 3: goto L_0x000a;
            default: goto L_0x0009;
        };
        goto L_0x003a;
        r2 = r7.payloadSize;
        if (r2 == r1) goto L_0x002b;
        r2 = "PesReader";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unexpected start indicator: expected ";
        r3.append(r4);
        r4 = r7.payloadSize;
        r3.append(r4);
        r4 = " more bytes";
        r3.append(r4);
        r3 = r3.toString();
        android.util.Log.w(r2, r3);
        r2 = r7.reader;
        r2.packetFinished();
        goto L_0x003a;
        r2 = "PesReader";
        r3 = "Unexpected start indicator reading extended header";
        android.util.Log.w(r2, r3);
        goto L_0x003a;
        r7.setState(r0);
    L_0x003d:
        r2 = r8.bytesLeft();
        if (r2 <= 0) goto L_0x00cc;
        r2 = r7.state;
        r3 = 0;
        switch(r2) {
            case 0: goto L_0x00c2;
            case 1: goto L_0x00aa;
            case 2: goto L_0x007e;
            case 3: goto L_0x004b;
            default: goto L_0x0049;
        };
        goto L_0x00ca;
        r2 = r8.bytesLeft();
        r4 = r7.payloadSize;
        if (r4 != r1) goto L_0x0054;
        goto L_0x0058;
        r3 = r7.payloadSize;
        r3 = r2 - r3;
        if (r3 <= 0) goto L_0x0063;
        r2 = r2 - r3;
        r4 = r8.getPosition();
        r4 = r4 + r2;
        r8.setLimit(r4);
        r4 = r7.reader;
        r4.consume(r8);
        r4 = r7.payloadSize;
        if (r4 == r1) goto L_0x00ca;
        r4 = r7.payloadSize;
        r4 = r4 - r2;
        r7.payloadSize = r4;
        r4 = r7.payloadSize;
        if (r4 != 0) goto L_0x00ca;
        r4 = r7.reader;
        r4.packetFinished();
        r7.setState(r0);
        goto L_0x00ca;
        r2 = 10;
        r3 = r7.extendedHeaderLength;
        r2 = java.lang.Math.min(r2, r3);
        r3 = r7.pesScratch;
        r3 = r3.data;
        r3 = r7.continueRead(r8, r3, r2);
        if (r3 == 0) goto L_0x00ca;
        r3 = 0;
        r4 = r7.extendedHeaderLength;
        r3 = r7.continueRead(r8, r3, r4);
        if (r3 == 0) goto L_0x00ca;
        r7.parseHeaderExtension();
        r3 = r7.reader;
        r4 = r7.timeUs;
        r6 = r7.dataAlignmentIndicator;
        r3.packetStarted(r4, r6);
        r3 = 3;
        r7.setState(r3);
        goto L_0x00ca;
        r2 = r7.pesScratch;
        r2 = r2.data;
        r4 = 9;
        r2 = r7.continueRead(r8, r2, r4);
        if (r2 == 0) goto L_0x00ca;
        r2 = r7.parseHeader();
        if (r2 == 0) goto L_0x00be;
        r3 = 2;
        r7.setState(r3);
        goto L_0x00ca;
        r2 = r8.bytesLeft();
        r8.skipBytes(r2);
        goto L_0x003d;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.ts.PesReader.consume(org.telegram.messenger.exoplayer2.util.ParsableByteArray, boolean):void");
    }

    public PesReader(ElementaryStreamReader reader) {
        this.reader = reader;
    }

    public void init(TimestampAdjuster timestampAdjuster, ExtractorOutput extractorOutput, TrackIdGenerator idGenerator) {
        this.timestampAdjuster = timestampAdjuster;
        this.reader.createTracks(extractorOutput, idGenerator);
    }

    public final void seek() {
        this.state = 0;
        this.bytesRead = 0;
        this.seenFirstDts = false;
        this.reader.seek();
    }

    private void setState(int state) {
        this.state = state;
        this.bytesRead = 0;
    }

    private boolean continueRead(ParsableByteArray source, byte[] target, int targetLength) {
        int bytesToRead = Math.min(source.bytesLeft(), targetLength - this.bytesRead);
        boolean z = true;
        if (bytesToRead <= 0) {
            return true;
        }
        if (target == null) {
            source.skipBytes(bytesToRead);
        } else {
            source.readBytes(target, this.bytesRead, bytesToRead);
        }
        this.bytesRead += bytesToRead;
        if (this.bytesRead != targetLength) {
            z = false;
        }
        return z;
    }

    private boolean parseHeader() {
        this.pesScratch.setPosition(0);
        int startCodePrefix = this.pesScratch.readBits(24);
        if (startCodePrefix != 1) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected start code prefix: ");
            stringBuilder.append(startCodePrefix);
            Log.w(str, stringBuilder.toString());
            this.payloadSize = -1;
            return false;
        }
        this.pesScratch.skipBits(8);
        int packetLength = this.pesScratch.readBits(16);
        this.pesScratch.skipBits(5);
        this.dataAlignmentIndicator = this.pesScratch.readBit();
        this.pesScratch.skipBits(2);
        this.ptsFlag = this.pesScratch.readBit();
        this.dtsFlag = this.pesScratch.readBit();
        this.pesScratch.skipBits(6);
        this.extendedHeaderLength = this.pesScratch.readBits(8);
        if (packetLength == 0) {
            this.payloadSize = -1;
        } else {
            this.payloadSize = ((packetLength + 6) - 9) - this.extendedHeaderLength;
        }
        return true;
    }

    private void parseHeaderExtension() {
        this.pesScratch.setPosition(0);
        this.timeUs = C.TIME_UNSET;
        if (this.ptsFlag) {
            this.pesScratch.skipBits(4);
            long pts = ((long) this.pesScratch.readBits(3)) << 30;
            this.pesScratch.skipBits(1);
            long pts2 = pts | ((long) (this.pesScratch.readBits(15) << 15));
            this.pesScratch.skipBits(1);
            long pts3 = pts2 | ((long) this.pesScratch.readBits(15));
            this.pesScratch.skipBits(1);
            if (!this.seenFirstDts && this.dtsFlag) {
                this.pesScratch.skipBits(4);
                long dts = ((long) this.pesScratch.readBits(3)) << 30;
                this.pesScratch.skipBits(1);
                long dts2 = dts | ((long) (this.pesScratch.readBits(15) << 15));
                this.pesScratch.skipBits(1);
                long dts3 = dts2 | ((long) this.pesScratch.readBits(15));
                this.pesScratch.skipBits(1);
                this.timestampAdjuster.adjustTsTimestamp(dts3);
                this.seenFirstDts = true;
            }
            this.timeUs = this.timestampAdjuster.adjustTsTimestamp(pts3);
        }
    }
}
