package org.telegram.messenger.exoplayer2.source;

import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.extractor.TrackOutput.CryptoData;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.Util;

final class SampleMetadataQueue {
    private static final int SAMPLE_CAPACITY_INCREMENT = 1000;
    private int absoluteFirstIndex;
    private int capacity = SAMPLE_CAPACITY_INCREMENT;
    private CryptoData[] cryptoDatas = new CryptoData[this.capacity];
    private int[] flags = new int[this.capacity];
    private Format[] formats = new Format[this.capacity];
    private long largestDiscardedTimestampUs = Long.MIN_VALUE;
    private long largestQueuedTimestampUs = Long.MIN_VALUE;
    private int length;
    private long[] offsets = new long[this.capacity];
    private int readPosition;
    private int relativeFirstIndex;
    private int[] sizes = new int[this.capacity];
    private int[] sourceIds = new int[this.capacity];
    private long[] timesUs = new long[this.capacity];
    private Format upstreamFormat;
    private boolean upstreamFormatRequired = true;
    private boolean upstreamKeyframeRequired = true;
    private int upstreamSourceId;

    public static final class SampleExtrasHolder {
        public CryptoData cryptoData;
        public long offset;
        public int size;
    }

    public void reset(boolean resetUpstreamFormat) {
        this.length = 0;
        this.absoluteFirstIndex = 0;
        this.relativeFirstIndex = 0;
        this.readPosition = 0;
        this.upstreamKeyframeRequired = true;
        this.largestDiscardedTimestampUs = Long.MIN_VALUE;
        this.largestQueuedTimestampUs = Long.MIN_VALUE;
        if (resetUpstreamFormat) {
            this.upstreamFormat = null;
            this.upstreamFormatRequired = true;
        }
    }

    public int getWriteIndex() {
        return this.absoluteFirstIndex + this.length;
    }

    public long discardUpstreamSamples(int discardFromIndex) {
        int discardCount = getWriteIndex() - discardFromIndex;
        boolean z = discardCount >= 0 && discardCount <= this.length - this.readPosition;
        Assertions.checkArgument(z);
        this.length -= discardCount;
        this.largestQueuedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(this.length));
        if (this.length == 0) {
            return 0;
        }
        int relativeLastWriteIndex = getRelativeIndex(this.length - 1);
        return this.offsets[relativeLastWriteIndex] + ((long) this.sizes[relativeLastWriteIndex]);
    }

    public void sourceId(int sourceId) {
        this.upstreamSourceId = sourceId;
    }

    public int getFirstIndex() {
        return this.absoluteFirstIndex;
    }

    public int getReadIndex() {
        return this.absoluteFirstIndex + this.readPosition;
    }

    public int peekSourceId() {
        return hasNextSample() ? this.sourceIds[getRelativeIndex(this.readPosition)] : this.upstreamSourceId;
    }

    public synchronized boolean hasNextSample() {
        return this.readPosition != this.length;
    }

    public synchronized Format getUpstreamFormat() {
        return this.upstreamFormatRequired ? null : this.upstreamFormat;
    }

    public synchronized long getLargestQueuedTimestampUs() {
        return this.largestQueuedTimestampUs;
    }

    public synchronized long getFirstTimestampUs() {
        return this.length == 0 ? Long.MIN_VALUE : this.timesUs[this.relativeFirstIndex];
    }

    public synchronized void rewind() {
        this.readPosition = 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read(org.telegram.messenger.exoplayer2.FormatHolder r7, org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer r8, boolean r9, boolean r10, org.telegram.messenger.exoplayer2.Format r11, org.telegram.messenger.exoplayer2.source.SampleMetadataQueue.SampleExtrasHolder r12) {
        /*
        r6 = this;
        monitor-enter(r6);
        r0 = r6.hasNextSample();	 Catch:{ all -> 0x006a }
        r1 = -3;
        r2 = -5;
        r3 = -4;
        if (r0 != 0) goto L_0x0024;
    L_0x000a:
        if (r10 == 0) goto L_0x0012;
    L_0x000c:
        r0 = 4;
        r8.setFlags(r0);	 Catch:{ all -> 0x006a }
        monitor-exit(r6);
        return r3;
    L_0x0012:
        r0 = r6.upstreamFormat;	 Catch:{ all -> 0x006a }
        if (r0 == 0) goto L_0x0022;
    L_0x0016:
        if (r9 != 0) goto L_0x001c;
    L_0x0018:
        r0 = r6.upstreamFormat;	 Catch:{ all -> 0x006a }
        if (r0 == r11) goto L_0x0022;
    L_0x001c:
        r0 = r6.upstreamFormat;	 Catch:{ all -> 0x006a }
        r7.format = r0;	 Catch:{ all -> 0x006a }
        monitor-exit(r6);
        return r2;
    L_0x0022:
        monitor-exit(r6);
        return r1;
    L_0x0024:
        r0 = r6.readPosition;	 Catch:{ all -> 0x006a }
        r0 = r6.getRelativeIndex(r0);	 Catch:{ all -> 0x006a }
        if (r9 != 0) goto L_0x0062;
    L_0x002c:
        r4 = r6.formats;	 Catch:{ all -> 0x006a }
        r4 = r4[r0];	 Catch:{ all -> 0x006a }
        if (r4 == r11) goto L_0x0033;
    L_0x0032:
        goto L_0x0062;
    L_0x0033:
        r2 = r8.isFlagsOnly();	 Catch:{ all -> 0x006a }
        if (r2 == 0) goto L_0x003b;
    L_0x0039:
        monitor-exit(r6);
        return r1;
    L_0x003b:
        r1 = r6.timesUs;	 Catch:{ all -> 0x006a }
        r4 = r1[r0];	 Catch:{ all -> 0x006a }
        r8.timeUs = r4;	 Catch:{ all -> 0x006a }
        r1 = r6.flags;	 Catch:{ all -> 0x006a }
        r1 = r1[r0];	 Catch:{ all -> 0x006a }
        r8.setFlags(r1);	 Catch:{ all -> 0x006a }
        r1 = r6.sizes;	 Catch:{ all -> 0x006a }
        r1 = r1[r0];	 Catch:{ all -> 0x006a }
        r12.size = r1;	 Catch:{ all -> 0x006a }
        r1 = r6.offsets;	 Catch:{ all -> 0x006a }
        r4 = r1[r0];	 Catch:{ all -> 0x006a }
        r12.offset = r4;	 Catch:{ all -> 0x006a }
        r1 = r6.cryptoDatas;	 Catch:{ all -> 0x006a }
        r1 = r1[r0];	 Catch:{ all -> 0x006a }
        r12.cryptoData = r1;	 Catch:{ all -> 0x006a }
        r1 = r6.readPosition;	 Catch:{ all -> 0x006a }
        r1 = r1 + 1;
        r6.readPosition = r1;	 Catch:{ all -> 0x006a }
        monitor-exit(r6);
        return r3;
    L_0x0062:
        r1 = r6.formats;	 Catch:{ all -> 0x006a }
        r1 = r1[r0];	 Catch:{ all -> 0x006a }
        r7.format = r1;	 Catch:{ all -> 0x006a }
        monitor-exit(r6);
        return r2;
    L_0x006a:
        r7 = move-exception;
        monitor-exit(r6);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.SampleMetadataQueue.read(org.telegram.messenger.exoplayer2.FormatHolder, org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer, boolean, boolean, org.telegram.messenger.exoplayer2.Format, org.telegram.messenger.exoplayer2.source.SampleMetadataQueue$SampleExtrasHolder):int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int advanceTo(long r9, boolean r11, boolean r12) {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = r8.readPosition;	 Catch:{ all -> 0x003a }
        r0 = r8.getRelativeIndex(r0);	 Catch:{ all -> 0x003a }
        r1 = r8.hasNextSample();	 Catch:{ all -> 0x003a }
        r7 = -1;
        if (r1 == 0) goto L_0x0038;
    L_0x000e:
        r1 = r8.timesUs;	 Catch:{ all -> 0x003a }
        r2 = r1[r0];	 Catch:{ all -> 0x003a }
        r1 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1));
        if (r1 < 0) goto L_0x0038;
    L_0x0016:
        r1 = r8.largestQueuedTimestampUs;	 Catch:{ all -> 0x003a }
        r3 = (r9 > r1 ? 1 : (r9 == r1 ? 0 : -1));
        if (r3 <= 0) goto L_0x001f;
    L_0x001c:
        if (r12 != 0) goto L_0x001f;
    L_0x001e:
        goto L_0x0038;
    L_0x001f:
        r1 = r8.length;	 Catch:{ all -> 0x003a }
        r2 = r8.readPosition;	 Catch:{ all -> 0x003a }
        r3 = r1 - r2;
        r1 = r8;
        r2 = r0;
        r4 = r9;
        r6 = r11;
        r1 = r1.findSampleBefore(r2, r3, r4, r6);	 Catch:{ all -> 0x003a }
        if (r1 != r7) goto L_0x0031;
    L_0x002f:
        monitor-exit(r8);
        return r7;
    L_0x0031:
        r2 = r8.readPosition;	 Catch:{ all -> 0x003a }
        r2 = r2 + r1;
        r8.readPosition = r2;	 Catch:{ all -> 0x003a }
        monitor-exit(r8);
        return r1;
    L_0x0038:
        monitor-exit(r8);
        return r7;
    L_0x003a:
        r9 = move-exception;
        monitor-exit(r8);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.SampleMetadataQueue.advanceTo(long, boolean, boolean):int");
    }

    public synchronized int advanceToEnd() {
        int skipCount;
        skipCount = this.length - this.readPosition;
        this.readPosition = this.length;
        return skipCount;
    }

    public synchronized boolean setReadPosition(int sampleIndex) {
        if (this.absoluteFirstIndex > sampleIndex || sampleIndex > this.absoluteFirstIndex + this.length) {
            return false;
        }
        this.readPosition = sampleIndex - this.absoluteFirstIndex;
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized long discardTo(long r10, boolean r12, boolean r13) {
        /*
        r9 = this;
        monitor-enter(r9);
        r0 = r9.length;	 Catch:{ all -> 0x0038 }
        r1 = -1;
        if (r0 == 0) goto L_0x0036;
    L_0x0007:
        r0 = r9.timesUs;	 Catch:{ all -> 0x0038 }
        r3 = r9.relativeFirstIndex;	 Catch:{ all -> 0x0038 }
        r3 = r0[r3];	 Catch:{ all -> 0x0038 }
        r0 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1));
        if (r0 >= 0) goto L_0x0012;
    L_0x0011:
        goto L_0x0036;
    L_0x0012:
        if (r13 == 0) goto L_0x001f;
    L_0x0014:
        r0 = r9.readPosition;	 Catch:{ all -> 0x0038 }
        r3 = r9.length;	 Catch:{ all -> 0x0038 }
        if (r0 == r3) goto L_0x001f;
    L_0x001a:
        r0 = r9.readPosition;	 Catch:{ all -> 0x0038 }
        r0 = r0 + 1;
        goto L_0x0021;
    L_0x001f:
        r0 = r9.length;	 Catch:{ all -> 0x0038 }
    L_0x0021:
        r5 = r0;
        r4 = r9.relativeFirstIndex;	 Catch:{ all -> 0x0038 }
        r3 = r9;
        r6 = r10;
        r8 = r12;
        r0 = r3.findSampleBefore(r4, r5, r6, r8);	 Catch:{ all -> 0x0038 }
        r3 = -1;
        if (r0 != r3) goto L_0x0030;
    L_0x002e:
        monitor-exit(r9);
        return r1;
    L_0x0030:
        r1 = r9.discardSamples(r0);	 Catch:{ all -> 0x0038 }
        monitor-exit(r9);
        return r1;
    L_0x0036:
        monitor-exit(r9);
        return r1;
    L_0x0038:
        r10 = move-exception;
        monitor-exit(r9);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.SampleMetadataQueue.discardTo(long, boolean, boolean):long");
    }

    public synchronized long discardToRead() {
        if (this.readPosition == 0) {
            return -1;
        }
        return discardSamples(this.readPosition);
    }

    public synchronized long discardToEnd() {
        if (this.length == 0) {
            return -1;
        }
        return discardSamples(this.length);
    }

    public synchronized boolean format(Format format) {
        if (format == null) {
            this.upstreamFormatRequired = true;
            return false;
        }
        this.upstreamFormatRequired = false;
        if (Util.areEqual(format, this.upstreamFormat)) {
            return false;
        }
        this.upstreamFormat = format;
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void commitSample(long r17, int r19, long r20, int r22, org.telegram.messenger.exoplayer2.extractor.TrackOutput.CryptoData r23) {
        /*
        r16 = this;
        r1 = r16;
        monitor-enter(r16);
        r3 = r1.upstreamKeyframeRequired;	 Catch:{ all -> 0x00d6 }
        r4 = 0;
        if (r3 == 0) goto L_0x0010;
    L_0x0008:
        r3 = r19 & 1;
        if (r3 != 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r16);
        return;
    L_0x000e:
        r1.upstreamKeyframeRequired = r4;	 Catch:{ all -> 0x00d6 }
    L_0x0010:
        r3 = r1.upstreamFormatRequired;	 Catch:{ all -> 0x00d6 }
        r3 = r3 ^ 1;
        org.telegram.messenger.exoplayer2.util.Assertions.checkState(r3);	 Catch:{ all -> 0x00d6 }
        r16.commitSampleTimestamp(r17);	 Catch:{ all -> 0x00d6 }
        r3 = r1.length;	 Catch:{ all -> 0x00d6 }
        r3 = r1.getRelativeIndex(r3);	 Catch:{ all -> 0x00d6 }
        r5 = r1.timesUs;	 Catch:{ all -> 0x00d6 }
        r5[r3] = r17;	 Catch:{ all -> 0x00d6 }
        r5 = r1.offsets;	 Catch:{ all -> 0x00d6 }
        r5[r3] = r20;	 Catch:{ all -> 0x00d6 }
        r5 = r1.sizes;	 Catch:{ all -> 0x00d6 }
        r5[r3] = r22;	 Catch:{ all -> 0x00d6 }
        r5 = r1.flags;	 Catch:{ all -> 0x00d6 }
        r5[r3] = r19;	 Catch:{ all -> 0x00d6 }
        r5 = r1.cryptoDatas;	 Catch:{ all -> 0x00d6 }
        r5[r3] = r23;	 Catch:{ all -> 0x00d6 }
        r5 = r1.formats;	 Catch:{ all -> 0x00d6 }
        r10 = r1.upstreamFormat;	 Catch:{ all -> 0x00d6 }
        r5[r3] = r10;	 Catch:{ all -> 0x00d6 }
        r5 = r1.sourceIds;	 Catch:{ all -> 0x00d6 }
        r10 = r1.upstreamSourceId;	 Catch:{ all -> 0x00d6 }
        r5[r3] = r10;	 Catch:{ all -> 0x00d6 }
        r5 = r1.length;	 Catch:{ all -> 0x00d6 }
        r5 = r5 + 1;
        r1.length = r5;	 Catch:{ all -> 0x00d6 }
        r5 = r1.length;	 Catch:{ all -> 0x00d6 }
        r10 = r1.capacity;	 Catch:{ all -> 0x00d6 }
        if (r5 != r10) goto L_0x00d3;
    L_0x004c:
        r5 = r1.capacity;	 Catch:{ all -> 0x00d6 }
        r5 = r5 + 1000;
        r10 = new int[r5];	 Catch:{ all -> 0x00d6 }
        r11 = new long[r5];	 Catch:{ all -> 0x00d6 }
        r12 = new long[r5];	 Catch:{ all -> 0x00d6 }
        r13 = new int[r5];	 Catch:{ all -> 0x00d6 }
        r4 = new int[r5];	 Catch:{ all -> 0x00d6 }
        r2 = new org.telegram.messenger.exoplayer2.extractor.TrackOutput.CryptoData[r5];	 Catch:{ all -> 0x00d6 }
        r14 = r3;
        r3 = new org.telegram.messenger.exoplayer2.Format[r5];	 Catch:{ all -> 0x00d6 }
        r6 = r1.capacity;	 Catch:{ all -> 0x00d6 }
        r7 = r1.relativeFirstIndex;	 Catch:{ all -> 0x00d6 }
        r6 = r6 - r7;
        r7 = r1.offsets;	 Catch:{ all -> 0x00d6 }
        r8 = r1.relativeFirstIndex;	 Catch:{ all -> 0x00d6 }
        r9 = 0;
        java.lang.System.arraycopy(r7, r8, r11, r9, r6);	 Catch:{ all -> 0x00d6 }
        r7 = r1.timesUs;	 Catch:{ all -> 0x00d6 }
        r8 = r1.relativeFirstIndex;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r7, r8, r12, r9, r6);	 Catch:{ all -> 0x00d6 }
        r7 = r1.flags;	 Catch:{ all -> 0x00d6 }
        r8 = r1.relativeFirstIndex;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r7, r8, r13, r9, r6);	 Catch:{ all -> 0x00d6 }
        r7 = r1.sizes;	 Catch:{ all -> 0x00d6 }
        r8 = r1.relativeFirstIndex;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r7, r8, r4, r9, r6);	 Catch:{ all -> 0x00d6 }
        r7 = r1.cryptoDatas;	 Catch:{ all -> 0x00d6 }
        r8 = r1.relativeFirstIndex;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r7, r8, r2, r9, r6);	 Catch:{ all -> 0x00d6 }
        r7 = r1.formats;	 Catch:{ all -> 0x00d6 }
        r8 = r1.relativeFirstIndex;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r7, r8, r3, r9, r6);	 Catch:{ all -> 0x00d6 }
        r7 = r1.sourceIds;	 Catch:{ all -> 0x00d6 }
        r8 = r1.relativeFirstIndex;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r7, r8, r10, r9, r6);	 Catch:{ all -> 0x00d6 }
        r7 = r1.relativeFirstIndex;	 Catch:{ all -> 0x00d6 }
        r8 = r1.offsets;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r8, r9, r11, r6, r7);	 Catch:{ all -> 0x00d6 }
        r8 = r1.timesUs;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r8, r9, r12, r6, r7);	 Catch:{ all -> 0x00d6 }
        r8 = r1.flags;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r8, r9, r13, r6, r7);	 Catch:{ all -> 0x00d6 }
        r8 = r1.sizes;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r8, r9, r4, r6, r7);	 Catch:{ all -> 0x00d6 }
        r8 = r1.cryptoDatas;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r8, r9, r2, r6, r7);	 Catch:{ all -> 0x00d6 }
        r8 = r1.formats;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r8, r9, r3, r6, r7);	 Catch:{ all -> 0x00d6 }
        r8 = r1.sourceIds;	 Catch:{ all -> 0x00d6 }
        java.lang.System.arraycopy(r8, r9, r10, r6, r7);	 Catch:{ all -> 0x00d6 }
        r1.offsets = r11;	 Catch:{ all -> 0x00d6 }
        r1.timesUs = r12;	 Catch:{ all -> 0x00d6 }
        r1.flags = r13;	 Catch:{ all -> 0x00d6 }
        r1.sizes = r4;	 Catch:{ all -> 0x00d6 }
        r1.cryptoDatas = r2;	 Catch:{ all -> 0x00d6 }
        r1.formats = r3;	 Catch:{ all -> 0x00d6 }
        r1.sourceIds = r10;	 Catch:{ all -> 0x00d6 }
        r8 = 0;
        r1.relativeFirstIndex = r8;	 Catch:{ all -> 0x00d6 }
        r8 = r1.capacity;	 Catch:{ all -> 0x00d6 }
        r1.length = r8;	 Catch:{ all -> 0x00d6 }
        r1.capacity = r5;	 Catch:{ all -> 0x00d6 }
        goto L_0x00d4;
    L_0x00d3:
        r14 = r3;
    L_0x00d4:
        monitor-exit(r16);
        return;
    L_0x00d6:
        r0 = move-exception;
        r2 = r0;
        monitor-exit(r16);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.SampleMetadataQueue.commitSample(long, int, long, int, org.telegram.messenger.exoplayer2.extractor.TrackOutput$CryptoData):void");
    }

    public synchronized void commitSampleTimestamp(long timeUs) {
        this.largestQueuedTimestampUs = Math.max(this.largestQueuedTimestampUs, timeUs);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean attemptSplice(long r9) {
        /*
        r8 = this;
        monitor-enter(r8);
        r0 = r8.length;	 Catch:{ all -> 0x004c }
        r1 = 0;
        r2 = 1;
        if (r0 != 0) goto L_0x0011;
    L_0x0007:
        r3 = r8.largestDiscardedTimestampUs;	 Catch:{ all -> 0x004c }
        r0 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r0 <= 0) goto L_0x000f;
    L_0x000d:
        r1 = r2;
    L_0x000f:
        monitor-exit(r8);
        return r1;
    L_0x0011:
        r3 = r8.largestDiscardedTimestampUs;	 Catch:{ all -> 0x004c }
        r0 = r8.readPosition;	 Catch:{ all -> 0x004c }
        r5 = r8.getLargestTimestamp(r0);	 Catch:{ all -> 0x004c }
        r3 = java.lang.Math.max(r3, r5);	 Catch:{ all -> 0x004c }
        r0 = (r3 > r9 ? 1 : (r3 == r9 ? 0 : -1));
        if (r0 < 0) goto L_0x0023;
    L_0x0021:
        monitor-exit(r8);
        return r1;
    L_0x0023:
        r0 = r8.length;	 Catch:{ all -> 0x004c }
        r1 = r8.length;	 Catch:{ all -> 0x004c }
        r1 = r1 - r2;
        r1 = r8.getRelativeIndex(r1);	 Catch:{ all -> 0x004c }
    L_0x002c:
        r5 = r8.readPosition;	 Catch:{ all -> 0x004c }
        if (r0 <= r5) goto L_0x0044;
    L_0x0030:
        r5 = r8.timesUs;	 Catch:{ all -> 0x004c }
        r6 = r5[r1];	 Catch:{ all -> 0x004c }
        r5 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1));
        if (r5 < 0) goto L_0x0044;
    L_0x0038:
        r0 = r0 + -1;
        r1 = r1 + -1;
        r5 = -1;
        if (r1 != r5) goto L_0x002c;
    L_0x003f:
        r5 = r8.capacity;	 Catch:{ all -> 0x004c }
        r1 = r5 + -1;
        goto L_0x002c;
    L_0x0044:
        r5 = r8.absoluteFirstIndex;	 Catch:{ all -> 0x004c }
        r5 = r5 + r0;
        r8.discardUpstreamSamples(r5);	 Catch:{ all -> 0x004c }
        monitor-exit(r8);
        return r2;
    L_0x004c:
        r9 = move-exception;
        monitor-exit(r8);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.SampleMetadataQueue.attemptSplice(long):boolean");
    }

    private int findSampleBefore(int relativeStartIndex, int length, long timeUs, boolean keyframe) {
        int sampleCountToTarget = -1;
        int searchIndex = relativeStartIndex;
        for (int i = 0; i < length && this.timesUs[searchIndex] <= timeUs; i++) {
            if (!(keyframe && (this.flags[searchIndex] & 1) == 0)) {
                sampleCountToTarget = i;
            }
            searchIndex++;
            if (searchIndex == this.capacity) {
                searchIndex = 0;
            }
        }
        return sampleCountToTarget;
    }

    private long discardSamples(int discardCount) {
        this.largestDiscardedTimestampUs = Math.max(this.largestDiscardedTimestampUs, getLargestTimestamp(discardCount));
        this.length -= discardCount;
        this.absoluteFirstIndex += discardCount;
        this.relativeFirstIndex += discardCount;
        if (this.relativeFirstIndex >= this.capacity) {
            this.relativeFirstIndex -= this.capacity;
        }
        this.readPosition -= discardCount;
        if (this.readPosition < 0) {
            this.readPosition = 0;
        }
        if (this.length != 0) {
            return this.offsets[this.relativeFirstIndex];
        }
        int relativeLastDiscardIndex = (this.relativeFirstIndex == 0 ? this.capacity : this.relativeFirstIndex) - 1;
        return this.offsets[relativeLastDiscardIndex] + ((long) this.sizes[relativeLastDiscardIndex]);
    }

    private long getLargestTimestamp(int length) {
        if (length == 0) {
            return Long.MIN_VALUE;
        }
        long largestTimestampUs = Long.MIN_VALUE;
        int relativeSampleIndex = getRelativeIndex(length - 1);
        for (int i = 0; i < length; i++) {
            largestTimestampUs = Math.max(largestTimestampUs, this.timesUs[relativeSampleIndex]);
            if ((this.flags[relativeSampleIndex] & 1) != 0) {
                break;
            }
            relativeSampleIndex--;
            if (relativeSampleIndex == -1) {
                relativeSampleIndex = this.capacity - 1;
            }
        }
        return largestTimestampUs;
    }

    private int getRelativeIndex(int offset) {
        int relativeIndex = this.relativeFirstIndex + offset;
        return relativeIndex < this.capacity ? relativeIndex : relativeIndex - this.capacity;
    }
}
