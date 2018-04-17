package org.telegram.messenger.exoplayer2.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.telegram.messenger.exoplayer2.C;

public final class ParsableByteArray {
    public byte[] data;
    private int limit;
    private int position;

    public long readUtf8EncodedLong() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.util.ParsableByteArray.readUtf8EncodedLong():long
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
        r1 = r12.data;
        r2 = r12.position;
        r1 = r1[r2];
        r1 = (long) r1;
        r3 = 7;
        r4 = r3;
    L_0x000a:
        r5 = 6;
        r6 = 1;
        if (r4 < 0) goto L_0x002c;
    L_0x000e:
        r7 = r6 << r4;
        r7 = (long) r7;
        r9 = r1 & r7;
        r7 = 0;
        r11 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
        if (r11 != 0) goto L_0x0029;
    L_0x0019:
        if (r4 >= r5) goto L_0x0025;
    L_0x001b:
        r7 = r6 << r4;
        r7 = r7 - r6;
        r7 = (long) r7;
        r9 = r1 & r7;
        r0 = 7 - r4;
        r1 = r9;
        goto L_0x002c;
    L_0x0025:
        if (r4 != r3) goto L_0x002c;
    L_0x0027:
        r0 = 1;
        goto L_0x002c;
    L_0x0029:
        r4 = r4 + -1;
        goto L_0x000a;
    L_0x002c:
        if (r0 != 0) goto L_0x0045;
    L_0x002e:
        r3 = new java.lang.NumberFormatException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Invalid UTF-8 sequence first byte: ";
        r4.append(r5);
        r4.append(r1);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
        r3 = r6;
        if (r3 >= r0) goto L_0x0077;
        r4 = r12.data;
        r6 = r12.position;
        r6 = r6 + r3;
        r4 = r4[r6];
        r6 = r4 & 192;
        r7 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r6 == r7) goto L_0x006d;
        r5 = new java.lang.NumberFormatException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Invalid UTF-8 sequence continuation byte: ";
        r6.append(r7);
        r6.append(r1);
        r6 = r6.toString();
        r5.<init>(r6);
        throw r5;
        r6 = r1 << r5;
        r8 = r4 & 63;
        r8 = (long) r8;
        r1 = r6 | r8;
        r6 = r3 + 1;
        goto L_0x0046;
        r3 = r12.position;
        r3 = r3 + r0;
        r12.position = r3;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.util.ParsableByteArray.readUtf8EncodedLong():long");
    }

    public ParsableByteArray(int limit) {
        this.data = new byte[limit];
        this.limit = limit;
    }

    public ParsableByteArray(byte[] data) {
        this.data = data;
        this.limit = data.length;
    }

    public ParsableByteArray(byte[] data, int limit) {
        this.data = data;
        this.limit = limit;
    }

    public void reset(int limit) {
        reset(capacity() < limit ? new byte[limit] : this.data, limit);
    }

    public void reset(byte[] data, int limit) {
        this.data = data;
        this.limit = limit;
        this.position = 0;
    }

    public void reset() {
        this.position = 0;
        this.limit = 0;
    }

    public int bytesLeft() {
        return this.limit - this.position;
    }

    public int limit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        boolean z = limit >= 0 && limit <= this.data.length;
        Assertions.checkArgument(z);
        this.limit = limit;
    }

    public int getPosition() {
        return this.position;
    }

    public int capacity() {
        return this.data == null ? 0 : this.data.length;
    }

    public void setPosition(int position) {
        boolean z = position >= 0 && position <= this.limit;
        Assertions.checkArgument(z);
        this.position = position;
    }

    public void skipBytes(int bytes) {
        setPosition(this.position + bytes);
    }

    public void readBytes(ParsableBitArray bitArray, int length) {
        readBytes(bitArray.data, 0, length);
        bitArray.setPosition(0);
    }

    public void readBytes(byte[] buffer, int offset, int length) {
        System.arraycopy(this.data, this.position, buffer, offset, length);
        this.position += length;
    }

    public void readBytes(ByteBuffer buffer, int length) {
        buffer.put(this.data, this.position, length);
        this.position += length;
    }

    public int peekUnsignedByte() {
        return this.data[this.position] & 255;
    }

    public char peekChar() {
        return (char) (((this.data[this.position] & 255) << 8) | (this.data[this.position + 1] & 255));
    }

    public int readUnsignedByte() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & 255;
    }

    public int readUnsignedShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & 255) << 8;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        return i2 | (bArr2[i3] & 255);
    }

    public int readLittleEndianUnsignedShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = bArr[i] & 255;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        return i2 | ((bArr2[i3] & 255) << 8);
    }

    public short readShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & 255) << 8;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        return (short) (i2 | (bArr2[i3] & 255));
    }

    public short readLittleEndianShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = bArr[i] & 255;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        return (short) (i2 | ((bArr2[i3] & 255) << 8));
    }

    public int readUnsignedInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & 255) << 16;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 8;
        bArr2 = this.data;
        i3 = this.position;
        this.position = i3 + 1;
        return i2 | (bArr2[i3] & 255);
    }

    public int readInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = ((bArr[i] & 255) << 24) >> 8;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 8;
        bArr2 = this.data;
        i3 = this.position;
        this.position = i3 + 1;
        return i2 | (bArr2[i3] & 255);
    }

    public int readLittleEndianInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = bArr[i] & 255;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 8;
        bArr2 = this.data;
        i3 = this.position;
        this.position = i3 + 1;
        return i2 | ((bArr2[i3] & 255) << 16);
    }

    public int readLittleEndianUnsignedInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = bArr[i] & 255;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 8;
        bArr2 = this.data;
        i3 = this.position;
        this.position = i3 + 1;
        return i2 | ((bArr2[i3] & 255) << 16);
    }

    public long readUnsignedInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        long j = (((long) bArr[i]) & 255) << 24;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        long j2 = j | ((((long) bArr2[i2]) & 255) << 16);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        long j3 = j2 | ((((long) bArr[i]) & 255) << 8);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        return j3 | (((long) bArr[i]) & 255);
    }

    public long readLittleEndianUnsignedInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        long j = ((long) bArr[i]) & 255;
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        long j2 = j | ((((long) bArr[i]) & 255) << 8);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j = j2 | ((((long) bArr[i]) & 255) << 16);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        return j | ((((long) bArr[i]) & 255) << 24);
    }

    public int readInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & 255) << 24;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 16;
        bArr2 = this.data;
        i3 = this.position;
        this.position = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 8;
        bArr2 = this.data;
        i3 = this.position;
        this.position = i3 + 1;
        return i2 | (bArr2[i3] & 255);
    }

    public int readLittleEndianInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = bArr[i] & 255;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 8;
        bArr2 = this.data;
        i3 = this.position;
        this.position = i3 + 1;
        i2 |= (bArr2[i3] & 255) << 16;
        bArr2 = this.data;
        i3 = this.position;
        this.position = i3 + 1;
        return i2 | ((bArr2[i3] & 255) << 24);
    }

    public long readLong() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        long j = (((long) bArr[i]) & 255) << 56;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        long j2 = j | ((((long) bArr2[i2]) & 255) << 48);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        long j3 = j2 | ((((long) bArr[i]) & 255) << 40);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j2 = j3 | ((((long) bArr[i]) & 255) << 32);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j3 = j2 | ((((long) bArr[i]) & 255) << 24);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j2 = j3 | ((((long) bArr[i]) & 255) << 16);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j3 = j2 | ((((long) bArr[i]) & 255) << 8);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        return j3 | (((long) bArr[i]) & 255);
    }

    public long readLittleEndianLong() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        long j = ((long) bArr[i]) & 255;
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        long j2 = j | ((((long) bArr[i]) & 255) << 8);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j = j2 | ((((long) bArr[i]) & 255) << 16);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j2 = j | ((((long) bArr[i]) & 255) << 24);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j = j2 | ((((long) bArr[i]) & 255) << 32);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j2 = j | ((((long) bArr[i]) & 255) << 40);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        j = j2 | ((((long) bArr[i]) & 255) << 48);
        bArr = this.data;
        i = this.position;
        this.position = i + 1;
        return j | ((((long) bArr[i]) & 255) << 56);
    }

    public int readUnsignedFixedPoint1616() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int result = (bArr[i] & 255) << 8;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        result |= bArr2[i2] & 255;
        this.position += 2;
        return result;
    }

    public int readSynchSafeInt() {
        return (((readUnsignedByte() << 21) | (readUnsignedByte() << 14)) | (readUnsignedByte() << 7)) | readUnsignedByte();
    }

    public int readUnsignedIntToInt() {
        int result = readInt();
        if (result >= 0) {
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Top bit not zero: ");
        stringBuilder.append(result);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public int readLittleEndianUnsignedIntToInt() {
        int result = readLittleEndianInt();
        if (result >= 0) {
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Top bit not zero: ");
        stringBuilder.append(result);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public long readUnsignedLongToLong() {
        long result = readLong();
        if (result >= 0) {
            return result;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Top bit not zero: ");
        stringBuilder.append(result);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public String readString(int length) {
        return readString(length, Charset.forName(C.UTF8_NAME));
    }

    public String readString(int length, Charset charset) {
        String result = new String(this.data, this.position, length, charset);
        this.position += length;
        return result;
    }

    public String readNullTerminatedString(int length) {
        if (length == 0) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        int stringLength = length;
        int lastIndex = (this.position + length) - 1;
        if (lastIndex < this.limit && this.data[lastIndex] == (byte) 0) {
            stringLength--;
        }
        String result = new String(this.data, this.position, stringLength);
        this.position += length;
        return result;
    }

    public String readNullTerminatedString() {
        if (bytesLeft() == 0) {
            return null;
        }
        int stringLimit = this.position;
        while (stringLimit < this.limit && this.data[stringLimit] != (byte) 0) {
            stringLimit++;
        }
        String string = new String(this.data, this.position, stringLimit - this.position);
        this.position = stringLimit;
        if (this.position < this.limit) {
            this.position++;
        }
        return string;
    }

    public String readLine() {
        if (bytesLeft() == 0) {
            return null;
        }
        int lineLimit = this.position;
        while (lineLimit < this.limit && !Util.isLinebreak(this.data[lineLimit])) {
            lineLimit++;
        }
        if (lineLimit - this.position >= 3 && this.data[this.position] == (byte) -17 && this.data[this.position + 1] == (byte) -69 && this.data[this.position + 2] == (byte) -65) {
            this.position += 3;
        }
        String line = new String(this.data, this.position, lineLimit - this.position);
        this.position = lineLimit;
        if (this.position == this.limit) {
            return line;
        }
        if (this.data[this.position] == (byte) 13) {
            this.position++;
            if (this.position == this.limit) {
                return line;
            }
        }
        if (this.data[this.position] == (byte) 10) {
            this.position++;
        }
        return line;
    }
}
