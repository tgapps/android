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

    private boolean decode() throws java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.decoder.SimpleDecoder.decode():boolean
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
        r0 = r7.lock;
        monitor-enter(r0);
    L_0x0003:
        r1 = r7.released;	 Catch:{ all -> 0x0097 }
        if (r1 != 0) goto L_0x0013;	 Catch:{ all -> 0x0097 }
    L_0x0007:
        r1 = r7.canDecodeBuffer();	 Catch:{ all -> 0x0097 }
        if (r1 != 0) goto L_0x0013;	 Catch:{ all -> 0x0097 }
    L_0x000d:
        r1 = r7.lock;	 Catch:{ all -> 0x0097 }
        r1.wait();	 Catch:{ all -> 0x0097 }
        goto L_0x0003;	 Catch:{ all -> 0x0097 }
    L_0x0013:
        r1 = r7.released;	 Catch:{ all -> 0x0097 }
        r2 = 0;	 Catch:{ all -> 0x0097 }
        if (r1 == 0) goto L_0x001a;	 Catch:{ all -> 0x0097 }
    L_0x0018:
        monitor-exit(r0);	 Catch:{ all -> 0x0097 }
        return r2;	 Catch:{ all -> 0x0097 }
    L_0x001a:
        r1 = r7.queuedInputBuffers;	 Catch:{ all -> 0x0097 }
        r1 = r1.removeFirst();	 Catch:{ all -> 0x0097 }
        r1 = (org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer) r1;	 Catch:{ all -> 0x0097 }
        r3 = r7.availableOutputBuffers;	 Catch:{ all -> 0x0097 }
        r4 = r7.availableOutputBufferCount;	 Catch:{ all -> 0x0097 }
        r5 = 1;	 Catch:{ all -> 0x0097 }
        r4 = r4 - r5;	 Catch:{ all -> 0x0097 }
        r7.availableOutputBufferCount = r4;	 Catch:{ all -> 0x0097 }
        r3 = r3[r4];	 Catch:{ all -> 0x0097 }
        r4 = r7.flushed;	 Catch:{ all -> 0x0097 }
        r7.flushed = r2;	 Catch:{ all -> 0x0097 }
        monitor-exit(r0);	 Catch:{ all -> 0x0097 }
        r0 = r1.isEndOfStream();
        if (r0 == 0) goto L_0x003c;
    L_0x0037:
        r0 = 4;
        r3.addFlag(r0);
        goto L_0x006a;
    L_0x003c:
        r0 = r1.isDecodeOnly();
        if (r0 == 0) goto L_0x0047;
    L_0x0042:
        r0 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r3.addFlag(r0);
    L_0x0047:
        r0 = r7.decode(r1, r3, r4);	 Catch:{ RuntimeException -> 0x0056, OutOfMemoryError -> 0x004e }
        r7.exception = r0;	 Catch:{ RuntimeException -> 0x0056, OutOfMemoryError -> 0x004e }
        goto L_0x005d;
    L_0x004e:
        r0 = move-exception;
        r6 = r7.createUnexpectedDecodeException(r0);
        r7.exception = r6;
        goto L_0x005e;
    L_0x0056:
        r0 = move-exception;
        r6 = r7.createUnexpectedDecodeException(r0);
        r7.exception = r6;
        r0 = r7.exception;
        if (r0 == 0) goto L_0x006a;
        r0 = r7.lock;
        monitor-enter(r0);
        monitor-exit(r0);	 Catch:{ all -> 0x0067 }
        return r2;	 Catch:{ all -> 0x0067 }
    L_0x0067:
        r2 = move-exception;	 Catch:{ all -> 0x0067 }
        monitor-exit(r0);	 Catch:{ all -> 0x0067 }
        throw r2;
    L_0x006a:
        r6 = r7.lock;
        monitor-enter(r6);
        r0 = r7.flushed;	 Catch:{ all -> 0x0094 }
        if (r0 == 0) goto L_0x0075;	 Catch:{ all -> 0x0094 }
        r7.releaseOutputBufferInternal(r3);	 Catch:{ all -> 0x0094 }
        goto L_0x008f;	 Catch:{ all -> 0x0094 }
        r0 = r3.isDecodeOnly();	 Catch:{ all -> 0x0094 }
        if (r0 == 0) goto L_0x0084;	 Catch:{ all -> 0x0094 }
        r0 = r7.skippedOutputBufferCount;	 Catch:{ all -> 0x0094 }
        r0 = r0 + r5;	 Catch:{ all -> 0x0094 }
        r7.skippedOutputBufferCount = r0;	 Catch:{ all -> 0x0094 }
        r7.releaseOutputBufferInternal(r3);	 Catch:{ all -> 0x0094 }
        goto L_0x008f;	 Catch:{ all -> 0x0094 }
        r0 = r7.skippedOutputBufferCount;	 Catch:{ all -> 0x0094 }
        r3.skippedOutputBufferCount = r0;	 Catch:{ all -> 0x0094 }
        r7.skippedOutputBufferCount = r2;	 Catch:{ all -> 0x0094 }
        r0 = r7.queuedOutputBuffers;	 Catch:{ all -> 0x0094 }
        r0.addLast(r3);	 Catch:{ all -> 0x0094 }
        r7.releaseInputBufferInternal(r1);	 Catch:{ all -> 0x0094 }
        monitor-exit(r6);	 Catch:{ all -> 0x0094 }
        return r5;	 Catch:{ all -> 0x0094 }
    L_0x0094:
        r0 = move-exception;	 Catch:{ all -> 0x0094 }
        monitor-exit(r6);	 Catch:{ all -> 0x0094 }
        throw r0;
    L_0x0097:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0097 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.decoder.SimpleDecoder.decode():boolean");
    }

    private void run() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.decoder.SimpleDecoder.run():void
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
        r0 = r2.decode();	 Catch:{ InterruptedException -> 0x0009 }
        if (r0 == 0) goto L_0x0007;
    L_0x0006:
        goto L_0x0000;
        return;
    L_0x0009:
        r0 = move-exception;
        r1 = new java.lang.IllegalStateException;
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.decoder.SimpleDecoder.run():void");
    }

    protected abstract I createInputBuffer();

    protected abstract O createOutputBuffer();

    protected abstract E createUnexpectedDecodeException(Throwable th);

    protected abstract E decode(I i, O o, boolean z);

    protected SimpleDecoder(I[] inputBuffers, O[] outputBuffers) {
        this.availableInputBuffers = inputBuffers;
        int i = 0;
        this.availableInputBufferCount = inputBuffers.length;
        for (int i2 = 0; i2 < this.availableInputBufferCount; i2++) {
            this.availableInputBuffers[i2] = createInputBuffer();
        }
        this.availableOutputBuffers = outputBuffers;
        this.availableOutputBufferCount = outputBuffers.length;
        while (i < this.availableOutputBufferCount) {
            this.availableOutputBuffers[i] = createOutputBuffer();
            i++;
        }
        this.decodeThread = new Thread() {
            public void run() {
                SimpleDecoder.this.run();
            }
        };
        this.decodeThread.start();
    }

    protected final void setInitialInputBufferSize(int size) {
        int i = 0;
        Assertions.checkState(this.availableInputBufferCount == this.availableInputBuffers.length);
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
        synchronized (this.lock) {
            maybeThrowException();
            if (this.queuedOutputBuffers.isEmpty()) {
                return null;
            }
            OutputBuffer outputBuffer = (OutputBuffer) this.queuedOutputBuffers.removeFirst();
            return outputBuffer;
        }
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
