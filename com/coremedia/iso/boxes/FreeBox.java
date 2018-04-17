package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.LinkedList;
import java.util.List;

public class FreeBox implements Box {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TYPE = "free";
    ByteBuffer data;
    private long offset;
    private Container parent;
    List<Box> replacers;

    public void parse(com.googlecode.mp4parser.DataSource r1, java.nio.ByteBuffer r2, long r3, com.coremedia.iso.BoxParser r5) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.coremedia.iso.boxes.FreeBox.parse(com.googlecode.mp4parser.DataSource, java.nio.ByteBuffer, long, com.coremedia.iso.BoxParser):void
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
        r0 = r7.position();
        r2 = r8.remaining();
        r2 = (long) r2;
        r4 = r0 - r2;
        r6.offset = r4;
        r0 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r2 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1));
        if (r2 <= 0) goto L_0x0028;
    L_0x0014:
        r0 = r7.position();
        r0 = r7.map(r0, r9);
        r6.data = r0;
        r0 = r7.position();
        r2 = r0 + r9;
        r7.position(r2);
        goto L_0x0038;
        r0 = com.googlecode.mp4parser.util.CastUtils.l2i(r9);
        r0 = java.nio.ByteBuffer.allocate(r0);
        r6.data = r0;
        r0 = r6.data;
        r7.read(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.coremedia.iso.boxes.FreeBox.parse(com.googlecode.mp4parser.DataSource, java.nio.ByteBuffer, long, com.coremedia.iso.BoxParser):void");
    }

    public FreeBox() {
        this.replacers = new LinkedList();
        this.data = ByteBuffer.wrap(new byte[0]);
    }

    public FreeBox(int size) {
        this.replacers = new LinkedList();
        this.data = ByteBuffer.allocate(size);
    }

    public long getOffset() {
        return this.offset;
    }

    public ByteBuffer getData() {
        if (this.data != null) {
            return (ByteBuffer) this.data.duplicate().rewind();
        }
        return null;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    public void getBox(WritableByteChannel os) throws IOException {
        for (Box replacer : this.replacers) {
            replacer.getBox(os);
        }
        ByteBuffer header = ByteBuffer.allocate(8);
        IsoTypeWriter.writeUInt32(header, (long) (8 + this.data.limit()));
        header.put(TYPE.getBytes());
        header.rewind();
        os.write(header);
        header.rewind();
        this.data.rewind();
        os.write(this.data);
        this.data.rewind();
    }

    public Container getParent() {
        return this.parent;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public long getSize() {
        long size = 8;
        for (Box replacer : this.replacers) {
            size += replacer.getSize();
        }
        return size + ((long) this.data.limit());
    }

    public String getType() {
        return TYPE;
    }

    public void addAndReplace(Box box) {
        this.data.position(CastUtils.l2i(box.getSize()));
        this.data = this.data.slice();
        this.replacers.add(box);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null) {
            if (getClass() == o.getClass()) {
                FreeBox freeBox = (FreeBox) o;
                if (getData() != null) {
                    if (!getData().equals(freeBox.getData())) {
                    }
                    return true;
                } else if (freeBox.getData() != null) {
                    return false;
                } else {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.data != null ? this.data.hashCode() : 0;
    }
}
