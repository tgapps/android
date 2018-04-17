package org.telegram.tgnet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.exoplayer2.C;

public class NativeByteBuffer extends AbstractSerializedData {
    private static final ThreadLocal<NativeByteBuffer> addressWrapper = new ThreadLocal<NativeByteBuffer>() {
        protected NativeByteBuffer initialValue() {
            return new NativeByteBuffer(0, true);
        }
    };
    protected long address;
    public ByteBuffer buffer;
    private boolean justCalc;
    private int len;
    public boolean reused;

    public static native long native_getFreeBuffer(int i);

    public static native ByteBuffer native_getJavaByteBuffer(long j);

    public static native int native_limit(long j);

    public static native int native_position(long j);

    public static native void native_reuse(long j);

    public void writeByteArray(byte[] r1, int r2, int r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.NativeByteBuffer.writeByteArray(byte[], int, int):void
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
        r0 = 253; // 0xfd float:3.55E-43 double:1.25E-321;
        r1 = 4;
        r2 = 1;
        if (r8 > r0) goto L_0x0019;
    L_0x0006:
        r3 = r5.justCalc;	 Catch:{ Exception -> 0x0017 }
        if (r3 != 0) goto L_0x0011;	 Catch:{ Exception -> 0x0017 }
    L_0x000a:
        r3 = r5.buffer;	 Catch:{ Exception -> 0x0017 }
        r4 = (byte) r8;	 Catch:{ Exception -> 0x0017 }
        r3.put(r4);	 Catch:{ Exception -> 0x0017 }
        goto L_0x003f;	 Catch:{ Exception -> 0x0017 }
    L_0x0011:
        r3 = r5.len;	 Catch:{ Exception -> 0x0017 }
        r3 = r3 + r2;	 Catch:{ Exception -> 0x0017 }
        r5.len = r3;	 Catch:{ Exception -> 0x0017 }
        goto L_0x003f;	 Catch:{ Exception -> 0x0017 }
    L_0x0017:
        r0 = move-exception;	 Catch:{ Exception -> 0x0017 }
        goto L_0x006c;	 Catch:{ Exception -> 0x0017 }
    L_0x0019:
        r3 = r5.justCalc;	 Catch:{ Exception -> 0x0017 }
        if (r3 != 0) goto L_0x003a;	 Catch:{ Exception -> 0x0017 }
        r3 = r5.buffer;	 Catch:{ Exception -> 0x0017 }
        r4 = -2;	 Catch:{ Exception -> 0x0017 }
        r3.put(r4);	 Catch:{ Exception -> 0x0017 }
        r3 = r5.buffer;	 Catch:{ Exception -> 0x0017 }
        r4 = (byte) r8;	 Catch:{ Exception -> 0x0017 }
        r3.put(r4);	 Catch:{ Exception -> 0x0017 }
        r3 = r5.buffer;	 Catch:{ Exception -> 0x0017 }
        r4 = r8 >> 8;	 Catch:{ Exception -> 0x0017 }
        r4 = (byte) r4;	 Catch:{ Exception -> 0x0017 }
        r3.put(r4);	 Catch:{ Exception -> 0x0017 }
        r3 = r5.buffer;	 Catch:{ Exception -> 0x0017 }
        r4 = r8 >> 16;	 Catch:{ Exception -> 0x0017 }
        r4 = (byte) r4;	 Catch:{ Exception -> 0x0017 }
        r3.put(r4);	 Catch:{ Exception -> 0x0017 }
        goto L_0x003f;	 Catch:{ Exception -> 0x0017 }
        r3 = r5.len;	 Catch:{ Exception -> 0x0017 }
        r3 = r3 + r1;	 Catch:{ Exception -> 0x0017 }
        r5.len = r3;	 Catch:{ Exception -> 0x0017 }
    L_0x003f:
        r3 = r5.justCalc;	 Catch:{ Exception -> 0x0017 }
        if (r3 != 0) goto L_0x0049;	 Catch:{ Exception -> 0x0017 }
        r3 = r5.buffer;	 Catch:{ Exception -> 0x0017 }
        r3.put(r6, r7, r8);	 Catch:{ Exception -> 0x0017 }
        goto L_0x004e;	 Catch:{ Exception -> 0x0017 }
        r3 = r5.len;	 Catch:{ Exception -> 0x0017 }
        r3 = r3 + r8;	 Catch:{ Exception -> 0x0017 }
        r5.len = r3;	 Catch:{ Exception -> 0x0017 }
        if (r8 > r0) goto L_0x0052;	 Catch:{ Exception -> 0x0017 }
        r0 = r2;	 Catch:{ Exception -> 0x0017 }
        goto L_0x0053;	 Catch:{ Exception -> 0x0017 }
        r0 = r1;	 Catch:{ Exception -> 0x0017 }
        r3 = r8 + r0;	 Catch:{ Exception -> 0x0017 }
        r3 = r3 % r1;	 Catch:{ Exception -> 0x0017 }
        if (r3 == 0) goto L_0x006b;	 Catch:{ Exception -> 0x0017 }
        r3 = r5.justCalc;	 Catch:{ Exception -> 0x0017 }
        if (r3 != 0) goto L_0x0063;	 Catch:{ Exception -> 0x0017 }
        r3 = r5.buffer;	 Catch:{ Exception -> 0x0017 }
        r4 = 0;	 Catch:{ Exception -> 0x0017 }
        r3.put(r4);	 Catch:{ Exception -> 0x0017 }
        goto L_0x0068;	 Catch:{ Exception -> 0x0017 }
        r3 = r5.len;	 Catch:{ Exception -> 0x0017 }
        r3 = r3 + r2;	 Catch:{ Exception -> 0x0017 }
        r5.len = r3;	 Catch:{ Exception -> 0x0017 }
        r0 = r0 + 1;
        goto L_0x0053;
        goto L_0x0076;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x0076;
        r1 = "write byte array error";
        org.telegram.messenger.FileLog.e(r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.NativeByteBuffer.writeByteArray(byte[], int, int):void");
    }

    public static NativeByteBuffer wrap(long address) {
        NativeByteBuffer result = (NativeByteBuffer) addressWrapper.get();
        if (address != 0) {
            if (!result.reused && BuildVars.LOGS_ENABLED) {
                FileLog.e("forgot to reuse?");
            }
            result.address = address;
            result.reused = false;
            result.buffer = native_getJavaByteBuffer(address);
            result.buffer.limit(native_limit(address));
            int position = native_position(address);
            if (position <= result.buffer.limit()) {
                result.buffer.position(position);
            }
            result.buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return result;
    }

    private NativeByteBuffer(int address, boolean wrap) {
        this.reused = true;
    }

    public NativeByteBuffer(int size) throws Exception {
        this.reused = true;
        if (size >= 0) {
            this.address = native_getFreeBuffer(size);
            if (this.address != 0) {
                this.buffer = native_getJavaByteBuffer(this.address);
                this.buffer.position(0);
                this.buffer.limit(size);
                this.buffer.order(ByteOrder.LITTLE_ENDIAN);
                return;
            }
            return;
        }
        throw new Exception("invalid NativeByteBuffer size");
    }

    public NativeByteBuffer(boolean calculate) {
        this.reused = true;
        this.justCalc = calculate;
    }

    public int position() {
        return this.buffer.position();
    }

    public void position(int position) {
        this.buffer.position(position);
    }

    public int capacity() {
        return this.buffer.capacity();
    }

    public int limit() {
        return this.buffer.limit();
    }

    public void limit(int limit) {
        this.buffer.limit(limit);
    }

    public void put(ByteBuffer buff) {
        this.buffer.put(buff);
    }

    public void rewind() {
        if (this.justCalc) {
            this.len = 0;
        } else {
            this.buffer.rewind();
        }
    }

    public void compact() {
        this.buffer.compact();
    }

    public boolean hasRemaining() {
        return this.buffer.hasRemaining();
    }

    public void writeInt32(int x) {
        try {
            if (this.justCalc) {
                this.len += 4;
            } else {
                this.buffer.putInt(x);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write int32 error");
            }
        }
    }

    public void writeInt64(long x) {
        try {
            if (this.justCalc) {
                this.len += 8;
            } else {
                this.buffer.putLong(x);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write int64 error");
            }
        }
    }

    public void writeBool(boolean value) {
        if (this.justCalc) {
            this.len += 4;
        } else if (value) {
            writeInt32(-1720552011);
        } else {
            writeInt32(-1132882121);
        }
    }

    public void writeBytes(byte[] b) {
        try {
            if (this.justCalc) {
                this.len += b.length;
            } else {
                this.buffer.put(b);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write raw error");
            }
        }
    }

    public void writeBytes(byte[] b, int offset, int count) {
        try {
            if (this.justCalc) {
                this.len += count;
            } else {
                this.buffer.put(b, offset, count);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write raw error");
            }
        }
    }

    public void writeByte(int i) {
        writeByte((byte) i);
    }

    public void writeByte(byte b) {
        try {
            if (this.justCalc) {
                this.len++;
            } else {
                this.buffer.put(b);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte error");
            }
        }
    }

    public void writeString(String s) {
        try {
            writeByteArray(s.getBytes(C.UTF8_NAME));
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write string error");
            }
        }
    }

    public void writeByteArray(byte[] b) {
        try {
            if (b.length <= 253) {
                if (this.justCalc) {
                    this.len++;
                } else {
                    this.buffer.put((byte) b.length);
                }
            } else if (this.justCalc) {
                this.len += 4;
            } else {
                this.buffer.put((byte) -2);
                this.buffer.put((byte) b.length);
                this.buffer.put((byte) (b.length >> 8));
                this.buffer.put((byte) (b.length >> 16));
            }
            if (this.justCalc) {
                this.len += b.length;
            } else {
                this.buffer.put(b);
            }
            int i = b.length <= 253 ? 1 : 4;
            while ((b.length + i) % 4 != 0) {
                if (this.justCalc) {
                    this.len++;
                } else {
                    this.buffer.put((byte) 0);
                }
                i++;
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte array error");
            }
        }
    }

    public void writeDouble(double d) {
        try {
            writeInt64(Double.doubleToRawLongBits(d));
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write double error");
            }
        }
    }

    public void writeByteBuffer(NativeByteBuffer b) {
        try {
            int l = b.limit();
            if (l <= 253) {
                if (this.justCalc) {
                    this.len++;
                } else {
                    this.buffer.put((byte) l);
                }
            } else if (this.justCalc) {
                this.len += 4;
            } else {
                this.buffer.put((byte) -2);
                this.buffer.put((byte) l);
                this.buffer.put((byte) (l >> 8));
                this.buffer.put((byte) (l >> 16));
            }
            if (this.justCalc) {
                this.len += l;
            } else {
                b.rewind();
                this.buffer.put(b.buffer);
            }
            int i = l <= 253 ? 1 : 4;
            while ((l + i) % 4 != 0) {
                if (this.justCalc) {
                    this.len++;
                } else {
                    this.buffer.put((byte) 0);
                }
                i++;
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    public void writeBytes(NativeByteBuffer b) {
        if (this.justCalc) {
            this.len += b.limit();
            return;
        }
        b.rewind();
        this.buffer.put(b.buffer);
    }

    public int getIntFromByte(byte b) {
        return b >= (byte) 0 ? b : b + 256;
    }

    public int length() {
        if (this.justCalc) {
            return this.len;
        }
        return this.buffer.position();
    }

    public void skip(int count) {
        if (count != 0) {
            if (this.justCalc) {
                this.len += count;
            } else {
                this.buffer.position(this.buffer.position() + count);
            }
        }
    }

    public int getPosition() {
        return this.buffer.position();
    }

    public int readInt32(boolean exception) {
        try {
            return this.buffer.getInt();
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read int32 error", e);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read int32 error");
            }
            return 0;
        }
    }

    public boolean readBool(boolean exception) {
        int consructor = readInt32(exception);
        if (consructor == -1720552011) {
            return true;
        }
        if (consructor == -1132882121) {
            return false;
        }
        if (exception) {
            throw new RuntimeException("Not bool value!");
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.e("Not bool value!");
        }
        return false;
    }

    public long readInt64(boolean exception) {
        try {
            return this.buffer.getLong();
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read int64 error", e);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read int64 error");
            }
            return 0;
        }
    }

    public void readBytes(byte[] b, boolean exception) {
        try {
            this.buffer.get(b);
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read raw error", e);
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read raw error");
            }
        }
    }

    public void readBytes(byte[] b, int offset, int count, boolean exception) {
        try {
            this.buffer.get(b, offset, count);
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read raw error", e);
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read raw error");
            }
        }
    }

    public byte[] readData(int count, boolean exception) {
        byte[] arr = new byte[count];
        readBytes(arr, exception);
        return arr;
    }

    public String readString(boolean exception) {
        int startReadPosition = getPosition();
        int sl = 1;
        try {
            int l = getIntFromByte(this.buffer.get());
            if (l >= 254) {
                l = (getIntFromByte(this.buffer.get()) | (getIntFromByte(this.buffer.get()) << 8)) | (getIntFromByte(this.buffer.get()) << 16);
                sl = 4;
            }
            byte[] b = new byte[l];
            this.buffer.get(b);
            for (int i = sl; (l + i) % 4 != 0; i++) {
                this.buffer.get();
            }
            return new String(b, C.UTF8_NAME);
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read string error", e);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read string error");
            }
            position(startReadPosition);
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
    }

    public byte[] readByteArray(boolean exception) {
        int sl = 1;
        try {
            int l = getIntFromByte(this.buffer.get());
            if (l >= 254) {
                l = (getIntFromByte(this.buffer.get()) | (getIntFromByte(this.buffer.get()) << 8)) | (getIntFromByte(this.buffer.get()) << 16);
                sl = 4;
            }
            byte[] b = new byte[l];
            this.buffer.get(b);
            for (int i = sl; (l + i) % 4 != 0; i++) {
                this.buffer.get();
            }
            return b;
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read byte array error", e);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read byte array error");
            }
            return new byte[0];
        }
    }

    public NativeByteBuffer readByteBuffer(boolean exception) {
        int sl = 1;
        try {
            int l = getIntFromByte(this.buffer.get());
            if (l >= 254) {
                l = (getIntFromByte(this.buffer.get()) | (getIntFromByte(this.buffer.get()) << 8)) | (getIntFromByte(this.buffer.get()) << 16);
                sl = 4;
            }
            NativeByteBuffer b = new NativeByteBuffer(l);
            int old = this.buffer.limit();
            this.buffer.limit(this.buffer.position() + l);
            b.buffer.put(this.buffer);
            this.buffer.limit(old);
            b.buffer.position(0);
            for (int i = sl; (l + i) % 4 != 0; i++) {
                this.buffer.get();
            }
            return b;
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read byte array error", e);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read byte array error");
            }
            return null;
        }
    }

    public double readDouble(boolean exception) {
        try {
            return Double.longBitsToDouble(readInt64(exception));
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read double error", e);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read double error");
            }
            return 0.0d;
        }
    }

    public void reuse() {
        if (this.address != 0) {
            this.reused = true;
            native_reuse(this.address);
        }
    }

    public int remaining() {
        return this.buffer.remaining();
    }
}
