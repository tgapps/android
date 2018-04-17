package org.telegram.messenger.exoplayer2.extractor.mp3;

import org.telegram.messenger.exoplayer2.extractor.SeekMap.SeekPoints;
import org.telegram.messenger.exoplayer2.extractor.SeekPoint;
import org.telegram.messenger.exoplayer2.util.Util;

final class XingSeeker implements Seeker {
    private static final String TAG = "XingSeeker";
    private final long dataSize;
    private final long dataStartPosition;
    private final long durationUs;
    private final long[] tableOfContents;
    private final int xingFrameSize;

    public static org.telegram.messenger.exoplayer2.extractor.mp3.XingSeeker create(long r1, long r3, org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader r5, org.telegram.messenger.exoplayer2.util.ParsableByteArray r6) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp3.XingSeeker.create(long, long, org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader, org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.extractor.mp3.XingSeeker
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
        r11 = r26;
        r12 = r11.samplesPerFrame;
        r13 = r11.sampleRate;
        r14 = r27.readInt();
        r2 = r14 & 1;
        r3 = 1;
        if (r2 != r3) goto L_0x008e;
    L_0x0011:
        r2 = r27.readUnsignedIntToInt();
        r10 = r2;
        if (r2 != 0) goto L_0x001a;
    L_0x0018:
        goto L_0x008e;
        r3 = (long) r10;
        r5 = (long) r12;
        r7 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r5 = r5 * r7;
        r7 = (long) r13;
        r15 = org.telegram.messenger.exoplayer2.util.Util.scaleLargeTimestamp(r3, r5, r7);
        r2 = r14 & 6;
        r3 = 6;
        if (r2 == r3) goto L_0x0037;
        r8 = new org.telegram.messenger.exoplayer2.extractor.mp3.XingSeeker;
        r5 = r11.frameSize;
        r2 = r8;
        r3 = r24;
        r6 = r15;
        r2.<init>(r3, r5, r6);
        return r8;
        r2 = r27.readUnsignedIntToInt();
        r8 = (long) r2;
        r2 = 100;
        r6 = new long[r2];
        r3 = 0;
        if (r3 >= r2) goto L_0x004d;
        r4 = r27.readUnsignedByte();
        r4 = (long) r4;
        r6[r3] = r4;
        r3 = r3 + 1;
        goto L_0x0041;
        r2 = -1;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 == 0) goto L_0x0079;
        r2 = r24 + r8;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 == 0) goto L_0x0079;
        r2 = "XingSeeker";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "XING data size mismatch: ";
        r3.append(r4);
        r3.append(r0);
        r4 = ", ";
        r3.append(r4);
        r4 = r24 + r8;
        r3.append(r4);
        r3 = r3.toString();
        android.util.Log.w(r2, r3);
        r17 = new org.telegram.messenger.exoplayer2.extractor.mp3.XingSeeker;
        r5 = r11.frameSize;
        r2 = r17;
        r3 = r24;
        r18 = r6;
        r6 = r15;
        r19 = r8;
        r21 = r10;
        r10 = r18;
        r2.<init>(r3, r5, r6, r8, r10);
        return r17;
    L_0x008e:
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp3.XingSeeker.create(long, long, org.telegram.messenger.exoplayer2.extractor.MpegAudioHeader, org.telegram.messenger.exoplayer2.util.ParsableByteArray):org.telegram.messenger.exoplayer2.extractor.mp3.XingSeeker");
    }

    private XingSeeker(long dataStartPosition, int xingFrameSize, long durationUs) {
        this(dataStartPosition, xingFrameSize, durationUs, -1, null);
    }

    private XingSeeker(long dataStartPosition, int xingFrameSize, long durationUs, long dataSize, long[] tableOfContents) {
        this.dataStartPosition = dataStartPosition;
        this.xingFrameSize = xingFrameSize;
        this.durationUs = durationUs;
        this.dataSize = dataSize;
        this.tableOfContents = tableOfContents;
    }

    public boolean isSeekable() {
        return this.tableOfContents != null;
    }

    public SeekPoints getSeekPoints(long timeUs) {
        XingSeeker xingSeeker = this;
        if (!isSeekable()) {
            return new SeekPoints(new SeekPoint(0, xingSeeker.dataStartPosition + ((long) xingSeeker.xingFrameSize)));
        }
        double scaledPosition;
        long timeUs2 = Util.constrainValue(timeUs, 0, xingSeeker.durationUs);
        double percent = (((double) timeUs2) * 100.0d) / ((double) xingSeeker.durationUs);
        if (percent <= 0.0d) {
            scaledPosition = 0.0d;
        } else if (percent >= 100.0d) {
            scaledPosition = 256.0d;
        } else {
            int prevTableIndex = (int) percent;
            double prevScaledPosition = (double) xingSeeker.tableOfContents[prevTableIndex];
            scaledPosition = prevScaledPosition + (((prevTableIndex == 99 ? 256.0d : (double) xingSeeker.tableOfContents[prevTableIndex + 1]) - prevScaledPosition) * (percent - ((double) prevTableIndex)));
            return new SeekPoints(new SeekPoint(timeUs2, xingSeeker.dataStartPosition + Util.constrainValue(Math.round((scaledPosition / 256.0d) * ((double) xingSeeker.dataSize)), (long) xingSeeker.xingFrameSize, xingSeeker.dataSize - 1)));
        }
        return new SeekPoints(new SeekPoint(timeUs2, xingSeeker.dataStartPosition + Util.constrainValue(Math.round((scaledPosition / 256.0d) * ((double) xingSeeker.dataSize)), (long) xingSeeker.xingFrameSize, xingSeeker.dataSize - 1)));
    }

    public long getTimeUs(long position) {
        long positionOffset = position - this.dataStartPosition;
        if (isSeekable()) {
            if (positionOffset > ((long) r0.xingFrameSize)) {
                double d;
                double scaledPosition = (((double) positionOffset) * 256.0d) / ((double) r0.dataSize);
                int prevTableIndex = Util.binarySearchFloor(r0.tableOfContents, (long) scaledPosition, true, true);
                long prevTimeUs = getTimeUsForTableIndex(prevTableIndex);
                long prevScaledPosition = r0.tableOfContents[prevTableIndex];
                long nextTimeUs = getTimeUsForTableIndex(prevTableIndex + 1);
                long nextScaledPosition = prevTableIndex == 99 ? 256 : r0.tableOfContents[prevTableIndex + 1];
                if (prevScaledPosition == nextScaledPosition) {
                    d = 0.0d;
                    double d2 = scaledPosition;
                } else {
                    d = (scaledPosition - ((double) prevScaledPosition)) / ((double) (nextScaledPosition - prevScaledPosition));
                }
                return prevTimeUs + Math.round(((double) (nextTimeUs - prevTimeUs)) * d);
            }
        }
        return 0;
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    private long getTimeUsForTableIndex(int tableIndex) {
        return (this.durationUs * ((long) tableIndex)) / 100;
    }
}
