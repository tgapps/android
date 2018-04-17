package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.nio.ByteBuffer;

public class BitWriterBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private ByteBuffer buffer;
    int initialPos;
    int position = 0;

    public BitWriterBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
        this.initialPos = buffer.position();
    }

    public void writeBits(int i, int numBits) {
        int left = 8 - (this.position % 8);
        int i2 = 1;
        int current;
        if (numBits <= left) {
            current = this.buffer.get(this.initialPos + (this.position / 8));
            current = (current < 0 ? current + 256 : current) + (i << (left - numBits));
            this.buffer.put(this.initialPos + (this.position / 8), (byte) (current > 127 ? current - 256 : current));
            this.position += numBits;
        } else {
            current = numBits - left;
            writeBits(i >> current, left);
            writeBits(((1 << current) - 1) & i, current);
        }
        ByteBuffer byteBuffer = this.buffer;
        int i3 = this.initialPos + (this.position / 8);
        if (this.position % 8 <= 0) {
            i2 = 0;
        }
        byteBuffer.position(i3 + i2);
    }
}
