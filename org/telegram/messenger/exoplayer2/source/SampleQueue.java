package org.telegram.messenger.exoplayer2.source;

import java.io.EOFException;
import java.io.IOException;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.FormatHolder;
import org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer;
import org.telegram.messenger.exoplayer2.extractor.ExtractorInput;
import org.telegram.messenger.exoplayer2.extractor.TrackOutput;
import org.telegram.messenger.exoplayer2.extractor.TrackOutput.CryptoData;
import org.telegram.messenger.exoplayer2.source.SampleMetadataQueue.SampleExtrasHolder;
import org.telegram.messenger.exoplayer2.upstream.Allocation;
import org.telegram.messenger.exoplayer2.upstream.Allocator;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;

public final class SampleQueue implements TrackOutput {
    public static final int ADVANCE_FAILED = -1;
    private static final int INITIAL_SCRATCH_SIZE = 32;
    private final int allocationLength;
    private final Allocator allocator;
    private Format downstreamFormat;
    private final SampleExtrasHolder extrasHolder = new SampleExtrasHolder();
    private AllocationNode firstAllocationNode = new AllocationNode(0, this.allocationLength);
    private Format lastUnadjustedFormat;
    private final SampleMetadataQueue metadataQueue = new SampleMetadataQueue();
    private boolean pendingFormatAdjustment;
    private boolean pendingSplice;
    private AllocationNode readAllocationNode = this.firstAllocationNode;
    private long sampleOffsetUs;
    private final ParsableByteArray scratch = new ParsableByteArray(32);
    private long totalBytesWritten;
    private UpstreamFormatChangedListener upstreamFormatChangeListener;
    private AllocationNode writeAllocationNode = this.firstAllocationNode;

    private static final class AllocationNode {
        public Allocation allocation;
        public final long endPosition;
        public AllocationNode next;
        public final long startPosition;
        public boolean wasInitialized;

        public AllocationNode(long startPosition, int allocationLength) {
            this.startPosition = startPosition;
            this.endPosition = startPosition + ((long) allocationLength);
        }

        public void initialize(Allocation allocation, AllocationNode next) {
            this.allocation = allocation;
            this.next = next;
            this.wasInitialized = true;
        }

        public int translateOffset(long absolutePosition) {
            return ((int) (absolutePosition - this.startPosition)) + this.allocation.offset;
        }

        public AllocationNode clear() {
            this.allocation = null;
            AllocationNode temp = this.next;
            this.next = null;
            return temp;
        }
    }

    public interface UpstreamFormatChangedListener {
        void onUpstreamFormatChanged(Format format);
    }

    private void readData(long r1, java.nio.ByteBuffer r3, int r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.SampleQueue.readData(long, java.nio.ByteBuffer, int):void
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
        r7.advanceReadTo(r8);
        r0 = r8;
        r8 = r11;
        if (r8 <= 0) goto L_0x0036;
    L_0x0007:
        r9 = r7.readAllocationNode;
        r2 = r9.endPosition;
        r4 = r2 - r0;
        r9 = (int) r4;
        r9 = java.lang.Math.min(r8, r9);
        r2 = r7.readAllocationNode;
        r2 = r2.allocation;
        r3 = r2.data;
        r4 = r7.readAllocationNode;
        r4 = r4.translateOffset(r0);
        r10.put(r3, r4, r9);
        r8 = r8 - r9;
        r3 = (long) r9;
        r5 = r0 + r3;
        r0 = r7.readAllocationNode;
        r0 = r0.endPosition;
        r3 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1));
        if (r3 != 0) goto L_0x0033;
    L_0x002d:
        r0 = r7.readAllocationNode;
        r0 = r0.next;
        r7.readAllocationNode = r0;
        r0 = r5;
        goto L_0x0005;
    L_0x0036:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.SampleQueue.readData(long, java.nio.ByteBuffer, int):void");
    }

    private void readData(long r1, byte[] r3, int r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.SampleQueue.readData(long, byte[], int):void
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
        r7.advanceReadTo(r8);
        r0 = r8;
        r8 = r11;
        if (r8 <= 0) goto L_0x0038;
    L_0x0007:
        r9 = r7.readAllocationNode;
        r2 = r9.endPosition;
        r4 = r2 - r0;
        r9 = (int) r4;
        r9 = java.lang.Math.min(r8, r9);
        r2 = r7.readAllocationNode;
        r2 = r2.allocation;
        r3 = r2.data;
        r4 = r7.readAllocationNode;
        r4 = r4.translateOffset(r0);
        r5 = r11 - r8;
        java.lang.System.arraycopy(r3, r4, r10, r5, r9);
        r8 = r8 - r9;
        r3 = (long) r9;
        r5 = r0 + r3;
        r0 = r7.readAllocationNode;
        r0 = r0.endPosition;
        r3 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1));
        if (r3 != 0) goto L_0x0035;
    L_0x002f:
        r0 = r7.readAllocationNode;
        r0 = r0.next;
        r7.readAllocationNode = r0;
        r0 = r5;
        goto L_0x0005;
    L_0x0038:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.SampleQueue.readData(long, byte[], int):void");
    }

    private void readEncryptionData(org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer r1, org.telegram.messenger.exoplayer2.source.SampleMetadataQueue.SampleExtrasHolder r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.SampleQueue.readEncryptionData(org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer, org.telegram.messenger.exoplayer2.source.SampleMetadataQueue$SampleExtrasHolder):void
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
        r0 = r28;
        r1 = r29;
        r2 = r30;
        r3 = r2.offset;
        r5 = r0.scratch;
        r6 = 1;
        r5.reset(r6);
        r5 = r0.scratch;
        r5 = r5.data;
        r0.readData(r3, r5, r6);
        r7 = 1;
        r9 = r3 + r7;
        r3 = r0.scratch;
        r3 = r3.data;
        r4 = 0;
        r3 = r3[r4];
        r5 = r3 & 128;
        if (r5 == 0) goto L_0x0026;
    L_0x0024:
        r5 = r6;
        goto L_0x0027;
    L_0x0026:
        r5 = r4;
    L_0x0027:
        r7 = r3 & 127;
        r8 = r1.cryptoInfo;
        r8 = r8.iv;
        if (r8 != 0) goto L_0x0037;
    L_0x002f:
        r8 = r1.cryptoInfo;
        r11 = 16;
        r11 = new byte[r11];
        r8.iv = r11;
    L_0x0037:
        r8 = r1.cryptoInfo;
        r8 = r8.iv;
        r0.readData(r9, r8, r7);
        r11 = (long) r7;
        r13 = r9 + r11;
        if (r5 == 0) goto L_0x005c;
    L_0x0043:
        r6 = r0.scratch;
        r8 = 2;
        r6.reset(r8);
        r6 = r0.scratch;
        r6 = r6.data;
        r0.readData(r13, r6, r8);
        r8 = 2;
        r10 = r13 + r8;
        r6 = r0.scratch;
        r6 = r6.readUnsignedShort();
        r13 = r10;
    L_0x005c:
        r8 = r1.cryptoInfo;
        r8 = r8.numBytesOfClearData;
        if (r8 == 0) goto L_0x0065;
    L_0x0062:
        r9 = r8.length;
        if (r9 >= r6) goto L_0x0067;
    L_0x0065:
        r8 = new int[r6];
    L_0x0067:
        r9 = r1.cryptoInfo;
        r9 = r9.numBytesOfEncryptedData;
        if (r9 == 0) goto L_0x0070;
    L_0x006d:
        r10 = r9.length;
        if (r10 >= r6) goto L_0x0072;
    L_0x0070:
        r9 = new int[r6];
    L_0x0072:
        if (r5 == 0) goto L_0x00a5;
    L_0x0074:
        r10 = 6;
        r10 = r10 * r6;
        r11 = r0.scratch;
        r11.reset(r10);
        r11 = r0.scratch;
        r11 = r11.data;
        r0.readData(r13, r11, r10);
        r11 = (long) r10;
        r15 = r13 + r11;
        r11 = r0.scratch;
        r11.setPosition(r4);
        if (r4 >= r6) goto L_0x00a0;
    L_0x008d:
        r11 = r0.scratch;
        r11 = r11.readUnsignedShort();
        r8[r4] = r11;
        r11 = r0.scratch;
        r11 = r11.readUnsignedIntToInt();
        r9[r4] = r11;
        r4 = r4 + 1;
        goto L_0x008b;
        r24 = r5;
        r13 = r15;
        goto L_0x00b4;
    L_0x00a5:
        r8[r4] = r4;
        r10 = r2.size;
        r11 = r2.offset;
        r24 = r5;
        r4 = r13 - r11;
        r4 = (int) r4;
        r10 = r10 - r4;
        r4 = 0;
        r9[r4] = r10;
        r4 = r2.cryptoData;
        r15 = r1.cryptoInfo;
        r5 = r4.encryptionKey;
        r10 = r1.cryptoInfo;
        r10 = r10.iv;
        r11 = r4.cryptoMode;
        r12 = r4.encryptedBlocks;
        r0 = r4.clearBlocks;
        r16 = r6;
        r17 = r8;
        r18 = r9;
        r19 = r5;
        r20 = r10;
        r21 = r11;
        r22 = r12;
        r23 = r0;
        r15.set(r16, r17, r18, r19, r20, r21, r22, r23);
        r10 = r2.offset;
        r0 = r13 - r10;
        r0 = (int) r0;
        r10 = r2.offset;
        r25 = r3;
        r26 = r4;
        r3 = (long) r0;
        r27 = r6;
        r5 = r10 + r3;
        r2.offset = r5;
        r1 = r2.size;
        r1 = r1 - r0;
        r2.size = r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.SampleQueue.readEncryptionData(org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer, org.telegram.messenger.exoplayer2.source.SampleMetadataQueue$SampleExtrasHolder):void");
    }

    public SampleQueue(Allocator allocator) {
        this.allocator = allocator;
        this.allocationLength = allocator.getIndividualAllocationLength();
    }

    public void reset() {
        reset(false);
    }

    public void reset(boolean resetUpstreamFormat) {
        this.metadataQueue.reset(resetUpstreamFormat);
        clearAllocationNodes(this.firstAllocationNode);
        this.firstAllocationNode = new AllocationNode(0, this.allocationLength);
        this.readAllocationNode = this.firstAllocationNode;
        this.writeAllocationNode = this.firstAllocationNode;
        this.totalBytesWritten = 0;
        this.allocator.trim();
    }

    public void sourceId(int sourceId) {
        this.metadataQueue.sourceId(sourceId);
    }

    public void splice() {
        this.pendingSplice = true;
    }

    public int getWriteIndex() {
        return this.metadataQueue.getWriteIndex();
    }

    public void discardUpstreamSamples(int discardFromIndex) {
        this.totalBytesWritten = this.metadataQueue.discardUpstreamSamples(discardFromIndex);
        if (this.totalBytesWritten != 0) {
            if (this.totalBytesWritten != this.firstAllocationNode.startPosition) {
                AllocationNode lastNodeToKeep = this.firstAllocationNode;
                while (this.totalBytesWritten > lastNodeToKeep.endPosition) {
                    lastNodeToKeep = lastNodeToKeep.next;
                }
                AllocationNode firstNodeToDiscard = lastNodeToKeep.next;
                clearAllocationNodes(firstNodeToDiscard);
                lastNodeToKeep.next = new AllocationNode(lastNodeToKeep.endPosition, this.allocationLength);
                this.writeAllocationNode = this.totalBytesWritten == lastNodeToKeep.endPosition ? lastNodeToKeep.next : lastNodeToKeep;
                if (this.readAllocationNode == firstNodeToDiscard) {
                    this.readAllocationNode = lastNodeToKeep.next;
                    return;
                }
                return;
            }
        }
        clearAllocationNodes(this.firstAllocationNode);
        this.firstAllocationNode = new AllocationNode(this.totalBytesWritten, this.allocationLength);
        this.readAllocationNode = this.firstAllocationNode;
        this.writeAllocationNode = this.firstAllocationNode;
    }

    public boolean hasNextSample() {
        return this.metadataQueue.hasNextSample();
    }

    public int getFirstIndex() {
        return this.metadataQueue.getFirstIndex();
    }

    public int getReadIndex() {
        return this.metadataQueue.getReadIndex();
    }

    public int peekSourceId() {
        return this.metadataQueue.peekSourceId();
    }

    public Format getUpstreamFormat() {
        return this.metadataQueue.getUpstreamFormat();
    }

    public long getLargestQueuedTimestampUs() {
        return this.metadataQueue.getLargestQueuedTimestampUs();
    }

    public long getFirstTimestampUs() {
        return this.metadataQueue.getFirstTimestampUs();
    }

    public void rewind() {
        this.metadataQueue.rewind();
        this.readAllocationNode = this.firstAllocationNode;
    }

    public void discardTo(long timeUs, boolean toKeyframe, boolean stopAtReadPosition) {
        discardDownstreamTo(this.metadataQueue.discardTo(timeUs, toKeyframe, stopAtReadPosition));
    }

    public void discardToRead() {
        discardDownstreamTo(this.metadataQueue.discardToRead());
    }

    public void discardToEnd() {
        discardDownstreamTo(this.metadataQueue.discardToEnd());
    }

    public int advanceToEnd() {
        return this.metadataQueue.advanceToEnd();
    }

    public int advanceTo(long timeUs, boolean toKeyframe, boolean allowTimeBeyondBuffer) {
        return this.metadataQueue.advanceTo(timeUs, toKeyframe, allowTimeBeyondBuffer);
    }

    public boolean setReadPosition(int sampleIndex) {
        return this.metadataQueue.setReadPosition(sampleIndex);
    }

    public int read(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired, boolean loadingFinished, long decodeOnlyUntilUs) {
        switch (this.metadataQueue.read(formatHolder, buffer, formatRequired, loadingFinished, this.downstreamFormat, this.extrasHolder)) {
            case -5:
                this.downstreamFormat = formatHolder.format;
                return -5;
            case -4:
                if (!buffer.isEndOfStream()) {
                    if (buffer.timeUs < decodeOnlyUntilUs) {
                        buffer.addFlag(Integer.MIN_VALUE);
                    }
                    if (buffer.isEncrypted()) {
                        readEncryptionData(buffer, this.extrasHolder);
                    }
                    buffer.ensureSpaceForWrite(this.extrasHolder.size);
                    readData(this.extrasHolder.offset, buffer.data, this.extrasHolder.size);
                }
                return -4;
            case -3:
                return -3;
            default:
                throw new IllegalStateException();
        }
    }

    private void advanceReadTo(long absolutePosition) {
        while (absolutePosition >= this.readAllocationNode.endPosition) {
            this.readAllocationNode = this.readAllocationNode.next;
        }
    }

    private void discardDownstreamTo(long absolutePosition) {
        if (absolutePosition != -1) {
            while (absolutePosition >= this.firstAllocationNode.endPosition) {
                this.allocator.release(this.firstAllocationNode.allocation);
                this.firstAllocationNode = this.firstAllocationNode.clear();
            }
            if (this.readAllocationNode.startPosition < this.firstAllocationNode.startPosition) {
                this.readAllocationNode = this.firstAllocationNode;
            }
        }
    }

    public void setUpstreamFormatChangeListener(UpstreamFormatChangedListener listener) {
        this.upstreamFormatChangeListener = listener;
    }

    public void setSampleOffsetUs(long sampleOffsetUs) {
        if (this.sampleOffsetUs != sampleOffsetUs) {
            this.sampleOffsetUs = sampleOffsetUs;
            this.pendingFormatAdjustment = true;
        }
    }

    public void format(Format format) {
        Format adjustedFormat = getAdjustedSampleFormat(format, this.sampleOffsetUs);
        boolean formatChanged = this.metadataQueue.format(adjustedFormat);
        this.lastUnadjustedFormat = format;
        this.pendingFormatAdjustment = false;
        if (this.upstreamFormatChangeListener != null && formatChanged) {
            this.upstreamFormatChangeListener.onUpstreamFormatChanged(adjustedFormat);
        }
    }

    public int sampleData(ExtractorInput input, int length, boolean allowEndOfInput) throws IOException, InterruptedException {
        int bytesAppended = input.read(this.writeAllocationNode.allocation.data, this.writeAllocationNode.translateOffset(this.totalBytesWritten), preAppend(length));
        if (bytesAppended != -1) {
            postAppend(bytesAppended);
            return bytesAppended;
        } else if (allowEndOfInput) {
            return -1;
        } else {
            throw new EOFException();
        }
    }

    public void sampleData(ParsableByteArray buffer, int length) {
        while (length > 0) {
            int bytesAppended = preAppend(length);
            buffer.readBytes(this.writeAllocationNode.allocation.data, this.writeAllocationNode.translateOffset(this.totalBytesWritten), bytesAppended);
            length -= bytesAppended;
            postAppend(bytesAppended);
        }
    }

    public void sampleMetadata(long timeUs, int flags, int size, int offset, CryptoData cryptoData) {
        long j = timeUs;
        if (this.pendingFormatAdjustment) {
            format(r0.lastUnadjustedFormat);
        }
        if (r0.pendingSplice) {
            if ((flags & 1) != 0) {
                if (r0.metadataQueue.attemptSplice(j)) {
                    r0.pendingSplice = false;
                }
            }
            return;
        }
        int i = size;
        r0.metadataQueue.commitSample(j + r0.sampleOffsetUs, flags, (r0.totalBytesWritten - ((long) i)) - ((long) offset), i, cryptoData);
    }

    private void clearAllocationNodes(AllocationNode fromNode) {
        if (fromNode.wasInitialized) {
            Allocation[] allocationsToRelease = new Allocation[(this.writeAllocationNode.wasInitialized + (((int) (this.writeAllocationNode.startPosition - fromNode.startPosition)) / this.allocationLength))];
            AllocationNode currentNode = fromNode;
            for (int i = 0; i < allocationsToRelease.length; i++) {
                allocationsToRelease[i] = currentNode.allocation;
                currentNode = currentNode.clear();
            }
            this.allocator.release(allocationsToRelease);
        }
    }

    private int preAppend(int length) {
        if (!this.writeAllocationNode.wasInitialized) {
            this.writeAllocationNode.initialize(this.allocator.allocate(), new AllocationNode(this.writeAllocationNode.endPosition, this.allocationLength));
        }
        return Math.min(length, (int) (this.writeAllocationNode.endPosition - this.totalBytesWritten));
    }

    private void postAppend(int length) {
        this.totalBytesWritten += (long) length;
        if (this.totalBytesWritten == this.writeAllocationNode.endPosition) {
            this.writeAllocationNode = this.writeAllocationNode.next;
        }
    }

    private static Format getAdjustedSampleFormat(Format format, long sampleOffsetUs) {
        if (format == null) {
            return null;
        }
        if (!(sampleOffsetUs == 0 || format.subsampleOffsetUs == Long.MAX_VALUE)) {
            format = format.copyWithSubsampleOffsetUs(format.subsampleOffsetUs + sampleOffsetUs);
        }
        return format;
    }
}
