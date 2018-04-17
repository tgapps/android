package org.telegram.messenger.exoplayer2.extractor.mp3;

import org.telegram.messenger.exoplayer2.extractor.SeekMap.SeekPoints;
import org.telegram.messenger.exoplayer2.extractor.SeekPoint;
import org.telegram.messenger.exoplayer2.util.Util;

final class VbriSeeker implements Seeker {
    private static final String TAG = "VbriSeeker";
    private final long durationUs;
    private final long[] positions;
    private final long[] timesUs;

    public static org.telegram.messenger.exoplayer2.extractor.mp3.VbriSeeker create(long r1, long r3, org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader r5, org.telegram.messenger.exoplayer2.util.ParsableByteArray r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp3.VbriSeeker.create(long, long, org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader, org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.extractor.mp3.VbriSeeker
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
        r0 = r21;
        r2 = r25;
        r3 = r26;
        r4 = 10;
        r3.skipBytes(r4);
        r4 = r26.readInt();
        r5 = 0;
        if (r4 > 0) goto L_0x0013;
    L_0x0012:
        return r5;
    L_0x0013:
        r6 = r2.sampleRate;
        r7 = (long) r4;
        r9 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r11 = 32000; // 0x7d00 float:4.4842E-41 double:1.581E-319;
        if (r6 < r11) goto L_0x0020;
    L_0x001d:
        r11 = 1152; // 0x480 float:1.614E-42 double:5.69E-321;
        goto L_0x0022;
    L_0x0020:
        r11 = 576; // 0x240 float:8.07E-43 double:2.846E-321;
    L_0x0022:
        r11 = (long) r11;
        r9 = r9 * r11;
        r11 = (long) r6;
        r7 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r7, r9, r11);
        r9 = r26.readUnsignedShort();
        r10 = r26.readUnsignedShort();
        r11 = r26.readUnsignedShort();
        r12 = 2;
        r3.skipBytes(r12);
        r12 = r2.frameSize;
        r12 = (long) r12;
        r16 = r6;
        r5 = r23 + r12;
        r12 = new long[r9];
        r13 = new long[r9];
        r17 = 0;
        r14 = r23;
        r18 = r17;
        r2 = r18;
        if (r2 >= r9) goto L_0x0085;
    L_0x004e:
        r0 = (long) r2;
        r0 = r0 * r7;
        r19 = r7;
        r7 = (long) r9;
        r0 = r0 / r7;
        r12[r2] = r0;
        r0 = java.lang.Math.max(r14, r5);
        r13[r2] = r0;
        switch(r11) {
            case 1: goto L_0x0070;
            case 2: goto L_0x006b;
            case 3: goto L_0x0066;
            case 4: goto L_0x0061;
            default: goto L_0x005f;
        };
    L_0x005f:
        r0 = 0;
        return r0;
    L_0x0061:
        r0 = r26.readUnsignedIntToInt();
        goto L_0x0075;
    L_0x0066:
        r0 = r26.readUnsignedInt24();
        goto L_0x0075;
    L_0x006b:
        r0 = r26.readUnsignedShort();
        goto L_0x0075;
    L_0x0070:
        r0 = r26.readUnsignedByte();
        r1 = r0 * r10;
        r7 = (long) r1;
        r0 = r14 + r7;
        r17 = r2 + 1;
        r14 = r0;
        r7 = r19;
        r0 = r21;
        r2 = r25;
        goto L_0x0048;
    L_0x0085:
        r19 = r7;
        r0 = -1;
        r7 = r21;
        r2 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1));
        if (r2 == 0) goto L_0x00b1;
        r0 = (r7 > r14 ? 1 : (r7 == r14 ? 0 : -1));
        if (r0 == 0) goto L_0x00b1;
        r0 = "VbriSeeker";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "VBRI data size mismatch: ";
        r1.append(r2);
        r1.append(r7);
        r2 = ", ";
        r1.append(r2);
        r1.append(r14);
        r1 = r1.toString();
        android.util.Log.w(r0, r1);
        r0 = new org.telegram.messenger.exoplayer2.extractor.mp3.VbriSeeker;
        r1 = r19;
        r0.<init>(r12, r13, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp3.VbriSeeker.create(long, long, org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader, org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.extractor.mp3.VbriSeeker");
    }

    private VbriSeeker(long[] timesUs, long[] positions, long durationUs) {
        this.timesUs = timesUs;
        this.positions = positions;
        this.durationUs = durationUs;
    }

    public boolean isSeekable() {
        return true;
    }

    public SeekPoints getSeekPoints(long timeUs) {
        int tableIndex = Util.binarySearchFloor(this.timesUs, timeUs, true, true);
        SeekPoint seekPoint = new SeekPoint(this.timesUs[tableIndex], this.positions[tableIndex]);
        if (seekPoint.timeUs < timeUs) {
            if (tableIndex != this.timesUs.length - 1) {
                return new SeekPoints(seekPoint, new SeekPoint(this.timesUs[tableIndex + 1], this.positions[tableIndex + 1]));
            }
        }
        return new SeekPoints(seekPoint);
    }

    public long getTimeUs(long position) {
        return this.timesUs[Util.binarySearchFloor(this.positions, position, true, true)];
    }

    public long getDurationUs() {
        return this.durationUs;
    }
}
