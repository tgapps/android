package org.telegram.messenger.exoplayer2.audio;

import java.nio.ByteBuffer;
import org.telegram.messenger.exoplayer2.audio.AudioProcessor.UnhandledFormatException;

final class ResamplingAudioProcessor implements AudioProcessor {
    private ByteBuffer buffer = EMPTY_BUFFER;
    private int channelCount = -1;
    private int encoding = 0;
    private boolean inputEnded;
    private ByteBuffer outputBuffer = EMPTY_BUFFER;
    private int sampleRateHz = -1;

    public void queueInput(java.nio.ByteBuffer r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.audio.ResamplingAudioProcessor.queueInput(java.nio.ByteBuffer):void
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
        r0 = r10.position();
        r1 = r10.limit();
        r2 = r1 - r0;
        r3 = r9.encoding;
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r6 = 3;
        if (r3 == r5) goto L_0x0023;
    L_0x0013:
        if (r3 == r6) goto L_0x0020;
    L_0x0015:
        if (r3 == r4) goto L_0x001d;
    L_0x0017:
        r3 = new java.lang.IllegalStateException;
        r3.<init>();
        throw r3;
    L_0x001d:
        r3 = r2 / 2;
        goto L_0x0028;
    L_0x0020:
        r3 = r2 * 2;
        goto L_0x0028;
    L_0x0023:
        r3 = r2 / 3;
        r3 = r3 * 2;
        r7 = r9.buffer;
        r7 = r7.capacity();
        if (r7 >= r3) goto L_0x0040;
        r7 = java.nio.ByteBuffer.allocateDirect(r3);
        r8 = java.nio.ByteOrder.nativeOrder();
        r7 = r7.order(r8);
        r9.buffer = r7;
        goto L_0x0045;
        r7 = r9.buffer;
        r7.clear();
        r7 = r9.encoding;
        if (r7 == r5) goto L_0x008b;
        if (r7 == r6) goto L_0x0070;
        if (r7 == r4) goto L_0x0053;
        r4 = new java.lang.IllegalStateException;
        r4.<init>();
        throw r4;
        r4 = r0;
        if (r4 >= r1) goto L_0x006f;
        r5 = r9.buffer;
        r6 = r4 + 2;
        r6 = r10.get(r6);
        r5.put(r6);
        r5 = r9.buffer;
        r6 = r4 + 3;
        r6 = r10.get(r6);
        r5.put(r6);
        r4 = r4 + 4;
        goto L_0x0054;
        goto L_0x00a8;
        r4 = r0;
        if (r4 >= r1) goto L_0x008a;
        r5 = r9.buffer;
        r6 = 0;
        r5.put(r6);
        r5 = r9.buffer;
        r6 = r10.get(r4);
        r6 = r6 & 255;
        r6 = r6 + -128;
        r6 = (byte) r6;
        r5.put(r6);
        r4 = r4 + 1;
        goto L_0x0071;
        goto L_0x00a8;
        r4 = r0;
        if (r4 >= r1) goto L_0x00a7;
        r5 = r9.buffer;
        r6 = r4 + 1;
        r6 = r10.get(r6);
        r5.put(r6);
        r5 = r9.buffer;
        r6 = r4 + 2;
        r6 = r10.get(r6);
        r5.put(r6);
        r4 = r4 + 3;
        goto L_0x008c;
        r4 = r10.limit();
        r10.position(r4);
        r4 = r9.buffer;
        r4.flip();
        r4 = r9.buffer;
        r9.outputBuffer = r4;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.audio.ResamplingAudioProcessor.queueInput(java.nio.ByteBuffer):void");
    }

    public boolean configure(int sampleRateHz, int channelCount, int encoding) throws UnhandledFormatException {
        if (encoding != 3 && encoding != 2 && encoding != Integer.MIN_VALUE && encoding != 1073741824) {
            throw new UnhandledFormatException(sampleRateHz, channelCount, encoding);
        } else if (this.sampleRateHz == sampleRateHz && this.channelCount == channelCount && this.encoding == encoding) {
            return false;
        } else {
            this.sampleRateHz = sampleRateHz;
            this.channelCount = channelCount;
            this.encoding = encoding;
            if (encoding == 2) {
                this.buffer = EMPTY_BUFFER;
            }
            return true;
        }
    }

    public boolean isActive() {
        return (this.encoding == 0 || this.encoding == 2) ? false : true;
    }

    public int getOutputChannelCount() {
        return this.channelCount;
    }

    public int getOutputEncoding() {
        return 2;
    }

    public int getOutputSampleRateHz() {
        return this.sampleRateHz;
    }

    public void queueEndOfStream() {
        this.inputEnded = true;
    }

    public ByteBuffer getOutput() {
        ByteBuffer outputBuffer = this.outputBuffer;
        this.outputBuffer = EMPTY_BUFFER;
        return outputBuffer;
    }

    public boolean isEnded() {
        return this.inputEnded && this.outputBuffer == EMPTY_BUFFER;
    }

    public void flush() {
        this.outputBuffer = EMPTY_BUFFER;
        this.inputEnded = false;
    }

    public void reset() {
        flush();
        this.buffer = EMPTY_BUFFER;
        this.sampleRateHz = -1;
        this.channelCount = -1;
        this.encoding = 0;
    }
}
