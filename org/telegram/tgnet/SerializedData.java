package org.telegram.tgnet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.exoplayer2.C;

public class SerializedData extends AbstractSerializedData {
    private DataInputStream in;
    private ByteArrayInputStream inbuf;
    protected boolean isOut;
    private boolean justCalc;
    private int len;
    private DataOutputStream out;
    private ByteArrayOutputStream outbuf;

    private void writeInt32(int r1, java.io.DataOutputStream r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.SerializedData.writeInt32(int, java.io.DataOutputStream):void
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
        r1 = 4;
        if (r0 >= r1) goto L_0x0019;
    L_0x0004:
        r1 = r0 * 8;
        r1 = r3 >> r1;
        r4.write(r1);	 Catch:{ Exception -> 0x000e }
        r0 = r0 + 1;
        goto L_0x0001;
    L_0x000e:
        r0 = move-exception;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x001a;
        r1 = "write int32 error";
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x001a;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.SerializedData.writeInt32(int, java.io.DataOutputStream):void");
    }

    private void writeInt64(long r1, java.io.DataOutputStream r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.SerializedData.writeInt64(long, java.io.DataOutputStream):void
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
        r1 = 8;
        if (r0 >= r1) goto L_0x001b;
    L_0x0005:
        r1 = r0 * 8;
        r1 = r4 >> r1;
        r1 = (int) r1;
        r6.write(r1);	 Catch:{ Exception -> 0x0010 }
        r0 = r0 + 1;
        goto L_0x0001;
    L_0x0010:
        r0 = move-exception;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x001c;
        r1 = "write int64 error";
        org.telegram.messenger.FileLog.e(r1);
        goto L_0x001c;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.SerializedData.writeInt64(long, java.io.DataOutputStream):void");
    }

    public void writeByteArray(byte[] r1, int r2, int r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.tgnet.SerializedData.writeByteArray(byte[], int, int):void
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
        if (r8 > r0) goto L_0x0018;
    L_0x0006:
        r3 = r5.justCalc;	 Catch:{ Exception -> 0x0016 }
        if (r3 != 0) goto L_0x0010;	 Catch:{ Exception -> 0x0016 }
    L_0x000a:
        r3 = r5.out;	 Catch:{ Exception -> 0x0016 }
        r3.write(r8);	 Catch:{ Exception -> 0x0016 }
        goto L_0x003c;	 Catch:{ Exception -> 0x0016 }
    L_0x0010:
        r3 = r5.len;	 Catch:{ Exception -> 0x0016 }
        r3 = r3 + r2;	 Catch:{ Exception -> 0x0016 }
        r5.len = r3;	 Catch:{ Exception -> 0x0016 }
        goto L_0x003c;	 Catch:{ Exception -> 0x0016 }
    L_0x0016:
        r0 = move-exception;	 Catch:{ Exception -> 0x0016 }
        goto L_0x0069;	 Catch:{ Exception -> 0x0016 }
    L_0x0018:
        r3 = r5.justCalc;	 Catch:{ Exception -> 0x0016 }
        if (r3 != 0) goto L_0x0037;	 Catch:{ Exception -> 0x0016 }
        r3 = r5.out;	 Catch:{ Exception -> 0x0016 }
        r4 = 254; // 0xfe float:3.56E-43 double:1.255E-321;	 Catch:{ Exception -> 0x0016 }
        r3.write(r4);	 Catch:{ Exception -> 0x0016 }
        r3 = r5.out;	 Catch:{ Exception -> 0x0016 }
        r3.write(r8);	 Catch:{ Exception -> 0x0016 }
        r3 = r5.out;	 Catch:{ Exception -> 0x0016 }
        r4 = r8 >> 8;	 Catch:{ Exception -> 0x0016 }
        r3.write(r4);	 Catch:{ Exception -> 0x0016 }
        r3 = r5.out;	 Catch:{ Exception -> 0x0016 }
        r4 = r8 >> 16;	 Catch:{ Exception -> 0x0016 }
        r3.write(r4);	 Catch:{ Exception -> 0x0016 }
        goto L_0x003c;	 Catch:{ Exception -> 0x0016 }
        r3 = r5.len;	 Catch:{ Exception -> 0x0016 }
        r3 = r3 + r1;	 Catch:{ Exception -> 0x0016 }
        r5.len = r3;	 Catch:{ Exception -> 0x0016 }
    L_0x003c:
        r3 = r5.justCalc;	 Catch:{ Exception -> 0x0016 }
        if (r3 != 0) goto L_0x0046;	 Catch:{ Exception -> 0x0016 }
        r3 = r5.out;	 Catch:{ Exception -> 0x0016 }
        r3.write(r6, r7, r8);	 Catch:{ Exception -> 0x0016 }
        goto L_0x004b;	 Catch:{ Exception -> 0x0016 }
        r3 = r5.len;	 Catch:{ Exception -> 0x0016 }
        r3 = r3 + r8;	 Catch:{ Exception -> 0x0016 }
        r5.len = r3;	 Catch:{ Exception -> 0x0016 }
        if (r8 > r0) goto L_0x004f;	 Catch:{ Exception -> 0x0016 }
        r0 = r2;	 Catch:{ Exception -> 0x0016 }
        goto L_0x0050;	 Catch:{ Exception -> 0x0016 }
        r0 = r1;	 Catch:{ Exception -> 0x0016 }
        r3 = r8 + r0;	 Catch:{ Exception -> 0x0016 }
        r3 = r3 % r1;	 Catch:{ Exception -> 0x0016 }
        if (r3 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x0016 }
        r3 = r5.justCalc;	 Catch:{ Exception -> 0x0016 }
        if (r3 != 0) goto L_0x0060;	 Catch:{ Exception -> 0x0016 }
        r3 = r5.out;	 Catch:{ Exception -> 0x0016 }
        r4 = 0;	 Catch:{ Exception -> 0x0016 }
        r3.write(r4);	 Catch:{ Exception -> 0x0016 }
        goto L_0x0065;	 Catch:{ Exception -> 0x0016 }
        r3 = r5.len;	 Catch:{ Exception -> 0x0016 }
        r3 = r3 + r2;	 Catch:{ Exception -> 0x0016 }
        r5.len = r3;	 Catch:{ Exception -> 0x0016 }
        r0 = r0 + 1;
        goto L_0x0050;
        goto L_0x0073;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x0073;
        r1 = "write byte array error";
        org.telegram.messenger.FileLog.e(r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.tgnet.SerializedData.writeByteArray(byte[], int, int):void");
    }

    public SerializedData() {
        this.isOut = true;
        this.justCalc = false;
        this.outbuf = new ByteArrayOutputStream();
        this.out = new DataOutputStream(this.outbuf);
    }

    public SerializedData(boolean calculate) {
        this.isOut = true;
        this.justCalc = false;
        if (!calculate) {
            this.outbuf = new ByteArrayOutputStream();
            this.out = new DataOutputStream(this.outbuf);
        }
        this.justCalc = calculate;
        this.len = 0;
    }

    public SerializedData(int size) {
        this.isOut = true;
        this.justCalc = false;
        this.outbuf = new ByteArrayOutputStream(size);
        this.out = new DataOutputStream(this.outbuf);
    }

    public SerializedData(byte[] data) {
        this.isOut = true;
        this.justCalc = false;
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(data);
        this.in = new DataInputStream(this.inbuf);
        this.len = 0;
    }

    public void cleanup() {
        try {
            if (this.inbuf != null) {
                this.inbuf.close();
                this.inbuf = null;
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
        try {
            if (this.in != null) {
                this.in.close();
                this.in = null;
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        try {
            if (this.outbuf != null) {
                this.outbuf.close();
                this.outbuf = null;
            }
        } catch (Throwable e22) {
            FileLog.e(e22);
        }
        try {
            if (this.out != null) {
                this.out.close();
                this.out = null;
            }
        } catch (Throwable e3) {
            FileLog.e(e3);
        }
    }

    public SerializedData(File file) throws Exception {
        this.isOut = true;
        this.justCalc = false;
        FileInputStream is = new FileInputStream(file);
        byte[] data = new byte[((int) file.length())];
        new DataInputStream(is).readFully(data);
        is.close();
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(data);
        this.in = new DataInputStream(this.inbuf);
    }

    public void writeInt32(int x) {
        if (this.justCalc) {
            this.len += 4;
        } else {
            writeInt32(x, this.out);
        }
    }

    public void writeInt64(long i) {
        if (this.justCalc) {
            this.len += 8;
        } else {
            writeInt64(i, this.out);
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
                this.out.write(b);
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
                this.out.write(b, offset, count);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write bytes error");
            }
        }
    }

    public void writeByte(int i) {
        try {
            if (this.justCalc) {
                this.len++;
            } else {
                this.out.writeByte((byte) i);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte error");
            }
        }
    }

    public void writeByte(byte b) {
        try {
            if (this.justCalc) {
                this.len++;
            } else {
                this.out.writeByte(b);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte error");
            }
        }
    }

    public void writeByteArray(byte[] b) {
        try {
            if (b.length <= 253) {
                if (this.justCalc) {
                    this.len++;
                } else {
                    this.out.write(b.length);
                }
            } else if (this.justCalc) {
                this.len += 4;
            } else {
                this.out.write(254);
                this.out.write(b.length);
                this.out.write(b.length >> 8);
                this.out.write(b.length >> 16);
            }
            if (this.justCalc) {
                this.len += b.length;
            } else {
                this.out.write(b);
            }
            int i = b.length <= 253 ? 1 : 4;
            while ((b.length + i) % 4 != 0) {
                if (this.justCalc) {
                    this.len++;
                } else {
                    this.out.write(0);
                }
                i++;
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte array error");
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

    public void writeDouble(double d) {
        try {
            writeInt64(Double.doubleToRawLongBits(d));
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write double error");
            }
        }
    }

    public int length() {
        if (this.justCalc) {
            return this.len;
        }
        return this.isOut ? this.outbuf.size() : this.inbuf.available();
    }

    protected void set(byte[] newData) {
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(newData);
        this.in = new DataInputStream(this.inbuf);
    }

    public byte[] toByteArray() {
        return this.outbuf.toByteArray();
    }

    public void skip(int count) {
        if (count != 0) {
            if (this.justCalc) {
                this.len += count;
            } else if (this.in != null) {
                try {
                    this.in.skipBytes(count);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public int getPosition() {
        return this.len;
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

    public void readBytes(byte[] b, boolean exception) {
        try {
            this.in.read(b);
            this.len += b.length;
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read bytes error", e);
            } else if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read bytes error");
            }
        }
    }

    public byte[] readData(int count, boolean exception) {
        byte[] arr = new byte[count];
        readBytes(arr, exception);
        return arr;
    }

    public String readString(boolean exception) {
        int sl = 1;
        try {
            int l = this.in.read();
            this.len++;
            if (l >= 254) {
                l = (this.in.read() | (this.in.read() << 8)) | (this.in.read() << 16);
                this.len += 3;
                sl = 4;
            }
            byte[] b = new byte[l];
            this.in.read(b);
            this.len++;
            for (int i = sl; (l + i) % 4 != 0; i++) {
                this.in.read();
                this.len++;
            }
            return new String(b, C.UTF8_NAME);
        } catch (Exception e) {
            if (exception) {
                throw new RuntimeException("read string error", e);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read string error");
            }
            return null;
        }
    }

    public byte[] readByteArray(boolean exception) {
        int sl = 1;
        try {
            int l = this.in.read();
            this.len++;
            if (l >= 254) {
                l = (this.in.read() | (this.in.read() << 8)) | (this.in.read() << 16);
                this.len += 3;
                sl = 4;
            }
            byte[] b = new byte[l];
            this.in.read(b);
            this.len++;
            for (int i = sl; (l + i) % 4 != 0; i++) {
                this.in.read();
                this.len++;
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

    public int readInt32(boolean exception) {
        int i = 0;
        int j = 0;
        while (j < 4) {
            try {
                i |= this.in.read() << (j * 8);
                this.len++;
                j++;
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
        return i;
    }

    public long readInt64(boolean exception) {
        long i = 0;
        int j = 0;
        while (j < 8) {
            try {
                long i2 = i | (((long) this.in.read()) << (j * 8));
                this.len++;
                j++;
                i = i2;
            } catch (long i3) {
                if (exception) {
                    throw new RuntimeException("read int64 error", i3);
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read int64 error");
                }
                return 0;
            }
        }
        return i3;
    }

    public void writeByteBuffer(NativeByteBuffer buffer) {
    }

    public NativeByteBuffer readByteBuffer(boolean exception) {
        return null;
    }

    public int remaining() {
        try {
            return this.in.available();
        } catch (Exception e) {
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
    }
}
