package org.telegram.messenger.exoplayer2.decoder;

import java.util.LinkedList;
import org.telegram.messenger.exoplayer2.util.Assertions;

public abstract class SimpleDecoder<I extends DecoderInputBuffer, O extends OutputBuffer, E extends Exception> implements Decoder<I, O, E> {
    private int availableInputBufferCount;
    private final I[] availableInputBuffers;
    private int availableOutputBufferCount;
    private final O[] availableOutputBuffers;
    private final Thread decodeThread;
    private I dequeuedInputBuffer;
    private E exception;
    private boolean flushed;
    private final Object lock = new Object();
    private final LinkedList<I> queuedInputBuffers = new LinkedList();
    private final LinkedList<O> queuedOutputBuffers = new LinkedList();
    private boolean released;
    private int skippedOutputBufferCount;

    protected abstract I createInputBuffer();

    protected abstract O createOutputBuffer();

    protected abstract E createUnexpectedDecodeException(Throwable th);

    protected abstract E decode(I i, O o, boolean z);

    protected SimpleDecoder(I[] inputBuffers, O[] outputBuffers) {
        int i;
        this.availableInputBuffers = inputBuffers;
        this.availableInputBufferCount = inputBuffers.length;
        for (i = 0; i < this.availableInputBufferCount; i++) {
            this.availableInputBuffers[i] = createInputBuffer();
        }
        this.availableOutputBuffers = outputBuffers;
        this.availableOutputBufferCount = outputBuffers.length;
        for (i = 0; i < this.availableOutputBufferCount; i++) {
            this.availableOutputBuffers[i] = createOutputBuffer();
        }
        this.decodeThread = new Thread() {
            public void run() {
                SimpleDecoder.this.run();
            }
        };
        this.decodeThread.start();
    }

    protected final void setInitialInputBufferSize(int size) {
        boolean z;
        int i = 0;
        if (this.availableInputBufferCount == this.availableInputBuffers.length) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        DecoderInputBuffer[] decoderInputBufferArr = this.availableInputBuffers;
        int length = decoderInputBufferArr.length;
        while (i < length) {
            decoderInputBufferArr[i].ensureSpaceForWrite(size);
            i++;
        }
    }

    public final I dequeueInputBuffer() throws Exception {
        I i;
        synchronized (this.lock) {
            DecoderInputBuffer decoderInputBuffer;
            maybeThrowException();
            Assertions.checkState(this.dequeuedInputBuffer == null);
            if (this.availableInputBufferCount == 0) {
                decoderInputBuffer = null;
            } else {
                DecoderInputBuffer[] decoderInputBufferArr = this.availableInputBuffers;
                int i2 = this.availableInputBufferCount - 1;
                this.availableInputBufferCount = i2;
                decoderInputBuffer = decoderInputBufferArr[i2];
            }
            this.dequeuedInputBuffer = decoderInputBuffer;
            i = this.dequeuedInputBuffer;
        }
        return i;
    }

    public final void queueInputBuffer(I inputBuffer) throws Exception {
        synchronized (this.lock) {
            maybeThrowException();
            Assertions.checkArgument(inputBuffer == this.dequeuedInputBuffer);
            this.queuedInputBuffers.addLast(inputBuffer);
            maybeNotifyDecodeLoop();
            this.dequeuedInputBuffer = null;
        }
    }

    public final O dequeueOutputBuffer() throws Exception {
        O o;
        synchronized (this.lock) {
            maybeThrowException();
            if (this.queuedOutputBuffers.isEmpty()) {
                o = null;
            } else {
                OutputBuffer outputBuffer = (OutputBuffer) this.queuedOutputBuffers.removeFirst();
            }
        }
        return o;
    }

    protected void releaseOutputBuffer(O outputBuffer) {
        synchronized (this.lock) {
            releaseOutputBufferInternal(outputBuffer);
            maybeNotifyDecodeLoop();
        }
    }

    public final void flush() {
        synchronized (this.lock) {
            this.flushed = true;
            this.skippedOutputBufferCount = 0;
            if (this.dequeuedInputBuffer != null) {
                releaseInputBufferInternal(this.dequeuedInputBuffer);
                this.dequeuedInputBuffer = null;
            }
            while (!this.queuedInputBuffers.isEmpty()) {
                releaseInputBufferInternal((DecoderInputBuffer) this.queuedInputBuffers.removeFirst());
            }
            while (!this.queuedOutputBuffers.isEmpty()) {
                releaseOutputBufferInternal((OutputBuffer) this.queuedOutputBuffers.removeFirst());
            }
        }
    }

    public void release() {
        synchronized (this.lock) {
            this.released = true;
            this.lock.notify();
        }
        try {
            this.decodeThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void maybeThrowException() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
    }

    private void maybeNotifyDecodeLoop() {
        if (canDecodeBuffer()) {
            this.lock.notify();
        }
    }

    private void run() {
        do {
            try {
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        } while (decode());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean decode() throws java.lang.InterruptedException {
        /*
        r8 = this;
        r4 = 0;
        r5 = r8.lock;
        monitor-enter(r5);
    L_0x0004:
        r6 = r8.released;	 Catch:{ all -> 0x0014 }
        if (r6 != 0) goto L_0x0017;
    L_0x0008:
        r6 = r8.canDecodeBuffer();	 Catch:{ all -> 0x0014 }
        if (r6 != 0) goto L_0x0017;
    L_0x000e:
        r6 = r8.lock;	 Catch:{ all -> 0x0014 }
        r6.wait();	 Catch:{ all -> 0x0014 }
        goto L_0x0004;
    L_0x0014:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0014 }
        throw r4;
    L_0x0017:
        r6 = r8.released;	 Catch:{ all -> 0x0014 }
        if (r6 == 0) goto L_0x001d;
    L_0x001b:
        monitor-exit(r5);	 Catch:{ all -> 0x0014 }
    L_0x001c:
        return r4;
    L_0x001d:
        r6 = r8.queuedInputBuffers;	 Catch:{ all -> 0x0014 }
        r1 = r6.removeFirst();	 Catch:{ all -> 0x0014 }
        r1 = (org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer) r1;	 Catch:{ all -> 0x0014 }
        r6 = r8.availableOutputBuffers;	 Catch:{ all -> 0x0014 }
        r7 = r8.availableOutputBufferCount;	 Catch:{ all -> 0x0014 }
        r7 = r7 + -1;
        r8.availableOutputBufferCount = r7;	 Catch:{ all -> 0x0014 }
        r2 = r6[r7];	 Catch:{ all -> 0x0014 }
        r3 = r8.flushed;	 Catch:{ all -> 0x0014 }
        r6 = 0;
        r8.flushed = r6;	 Catch:{ all -> 0x0014 }
        monitor-exit(r5);	 Catch:{ all -> 0x0014 }
        r5 = r1.isEndOfStream();
        if (r5 == 0) goto L_0x004f;
    L_0x003b:
        r4 = 4;
        r2.addFlag(r4);
    L_0x003f:
        r5 = r8.lock;
        monitor-enter(r5);
        r4 = r8.flushed;	 Catch:{ all -> 0x008c }
        if (r4 == 0) goto L_0x007c;
    L_0x0046:
        r8.releaseOutputBufferInternal(r2);	 Catch:{ all -> 0x008c }
    L_0x0049:
        r8.releaseInputBufferInternal(r1);	 Catch:{ all -> 0x008c }
        monitor-exit(r5);	 Catch:{ all -> 0x008c }
        r4 = 1;
        goto L_0x001c;
    L_0x004f:
        r5 = r1.isDecodeOnly();
        if (r5 == 0) goto L_0x005a;
    L_0x0055:
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r2.addFlag(r5);
    L_0x005a:
        r5 = r8.decode(r1, r2, r3);	 Catch:{ RuntimeException -> 0x006c, OutOfMemoryError -> 0x0074 }
        r8.exception = r5;	 Catch:{ RuntimeException -> 0x006c, OutOfMemoryError -> 0x0074 }
    L_0x0060:
        r5 = r8.exception;
        if (r5 == 0) goto L_0x003f;
    L_0x0064:
        r5 = r8.lock;
        monitor-enter(r5);
        monitor-exit(r5);	 Catch:{ all -> 0x0069 }
        goto L_0x001c;
    L_0x0069:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0069 }
        throw r4;
    L_0x006c:
        r0 = move-exception;
        r5 = r8.createUnexpectedDecodeException(r0);
        r8.exception = r5;
        goto L_0x0060;
    L_0x0074:
        r0 = move-exception;
        r5 = r8.createUnexpectedDecodeException(r0);
        r8.exception = r5;
        goto L_0x0060;
    L_0x007c:
        r4 = r2.isDecodeOnly();	 Catch:{ all -> 0x008c }
        if (r4 == 0) goto L_0x008f;
    L_0x0082:
        r4 = r8.skippedOutputBufferCount;	 Catch:{ all -> 0x008c }
        r4 = r4 + 1;
        r8.skippedOutputBufferCount = r4;	 Catch:{ all -> 0x008c }
        r8.releaseOutputBufferInternal(r2);	 Catch:{ all -> 0x008c }
        goto L_0x0049;
    L_0x008c:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x008c }
        throw r4;
    L_0x008f:
        r4 = r8.skippedOutputBufferCount;	 Catch:{ all -> 0x008c }
        r2.skippedOutputBufferCount = r4;	 Catch:{ all -> 0x008c }
        r4 = 0;
        r8.skippedOutputBufferCount = r4;	 Catch:{ all -> 0x008c }
        r4 = r8.queuedOutputBuffers;	 Catch:{ all -> 0x008c }
        r4.addLast(r2);	 Catch:{ all -> 0x008c }
        goto L_0x0049;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.decoder.SimpleDecoder.decode():boolean");
    }

    private boolean canDecodeBuffer() {
        return !this.queuedInputBuffers.isEmpty() && this.availableOutputBufferCount > 0;
    }

    private void releaseInputBufferInternal(I inputBuffer) {
        inputBuffer.clear();
        DecoderInputBuffer[] decoderInputBufferArr = this.availableInputBuffers;
        int i = this.availableInputBufferCount;
        this.availableInputBufferCount = i + 1;
        decoderInputBufferArr[i] = inputBuffer;
    }

    private void releaseOutputBufferInternal(O outputBuffer) {
        outputBuffer.clear();
        OutputBuffer[] outputBufferArr = this.availableOutputBuffers;
        int i = this.availableOutputBufferCount;
        this.availableOutputBufferCount = i + 1;
        outputBufferArr[i] = outputBuffer;
    }
}
