package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.UserBox;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import com.googlecode.mp4parser.util.Path;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractBox implements Box {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static Logger LOG = Logger.getLogger(AbstractBox.class);
    private ByteBuffer content;
    long contentStartPosition;
    DataSource dataSource;
    private ByteBuffer deadBytes = null;
    boolean isParsed;
    boolean isRead;
    long memMapSize = -1;
    long offset;
    private Container parent;
    protected String type;
    private byte[] userType;

    protected abstract void _parseDetails(ByteBuffer byteBuffer);

    protected abstract void getContent(ByteBuffer byteBuffer);

    protected abstract long getContentSize();

    public long getSize() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.googlecode.mp4parser.AbstractBox.getSize():long
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
        r0 = r8.isRead;
        r1 = 0;
        if (r0 == 0) goto L_0x001c;
    L_0x0005:
        r0 = r8.isParsed;
        if (r0 == 0) goto L_0x000e;
    L_0x0009:
        r2 = r8.getContentSize();
        goto L_0x001e;
    L_0x000e:
        r0 = r8.content;
        if (r0 == 0) goto L_0x0019;
        r0 = r8.content;
        r0 = r0.limit();
        goto L_0x001a;
        r0 = r1;
        r2 = (long) r0;
        goto L_0x001e;
    L_0x001c:
        r2 = r8.memMapSize;
        r4 = 4294967288; // 0xfffffff8 float:NaN double:2.121995787E-314;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        r4 = 8;
        if (r0 < 0) goto L_0x002c;
        r0 = r4;
        goto L_0x002e;
        r0 = r1;
        r4 = r4 + r0;
        r0 = "uuid";
        r5 = r8.getType();
        r0 = r0.equals(r5);
        if (r0 == 0) goto L_0x003e;
        r0 = 16;
        goto L_0x003f;
        r0 = r1;
        r4 = r4 + r0;
        r4 = (long) r4;
        r6 = r2 + r4;
        r0 = r8.deadBytes;
        if (r0 != 0) goto L_0x0048;
        goto L_0x004e;
        r0 = r8.deadBytes;
        r1 = r0.limit();
        r0 = (long) r1;
        r2 = r6 + r0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.AbstractBox.getSize():long");
    }

    private synchronized void readContent() {
        if (!this.isRead) {
            try {
                Logger logger = LOG;
                StringBuilder stringBuilder = new StringBuilder("mem mapping ");
                stringBuilder.append(getType());
                logger.logDebug(stringBuilder.toString());
                this.content = this.dataSource.map(this.contentStartPosition, this.memMapSize);
                this.isRead = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public long getOffset() {
        return this.offset;
    }

    protected AbstractBox(String type) {
        this.type = type;
        this.isRead = true;
        this.isParsed = true;
    }

    protected AbstractBox(String type, byte[] userType) {
        this.type = type;
        this.userType = userType;
        this.isRead = true;
        this.isParsed = true;
    }

    public void parse(DataSource dataSource, ByteBuffer header, long contentSize, BoxParser boxParser) throws IOException {
        this.contentStartPosition = dataSource.position();
        this.offset = this.contentStartPosition - ((long) header.remaining());
        this.memMapSize = contentSize;
        this.dataSource = dataSource;
        dataSource.position(dataSource.position() + contentSize);
        this.isRead = false;
        this.isParsed = false;
    }

    public void getBox(WritableByteChannel os) throws IOException {
        int i = 8;
        int i2 = 0;
        int i3 = 16;
        ByteBuffer header;
        if (!this.isRead) {
            if (!isSmallBox()) {
                i = 16;
            }
            if (UserBox.TYPE.equals(getType())) {
                i2 = 16;
            }
            header = ByteBuffer.allocate(i + i2);
            getHeader(header);
            os.write((ByteBuffer) header.rewind());
            this.dataSource.transferTo(this.contentStartPosition, this.memMapSize, os);
        } else if (this.isParsed) {
            header = ByteBuffer.allocate(CastUtils.l2i(getSize()));
            getHeader(header);
            getContent(header);
            if (this.deadBytes != null) {
                this.deadBytes.rewind();
                while (this.deadBytes.remaining() > 0) {
                    header.put(this.deadBytes);
                }
            }
            os.write((ByteBuffer) header.rewind());
        } else {
            if (!isSmallBox()) {
                i = 16;
            }
            if (!UserBox.TYPE.equals(getType())) {
                i3 = 0;
            }
            header = ByteBuffer.allocate(i + i3);
            getHeader(header);
            os.write((ByteBuffer) header.rewind());
            os.write((ByteBuffer) this.content.position(0));
        }
    }

    public final synchronized void parseDetails() {
        readContent();
        Logger logger = LOG;
        StringBuilder stringBuilder = new StringBuilder("parsing details of ");
        stringBuilder.append(getType());
        logger.logDebug(stringBuilder.toString());
        if (this.content != null) {
            ByteBuffer content = this.content;
            this.isParsed = true;
            content.rewind();
            _parseDetails(content);
            if (content.remaining() > 0) {
                this.deadBytes = content.slice();
            }
            this.content = null;
        }
    }

    protected void setDeadBytes(ByteBuffer newDeadBytes) {
        this.deadBytes = newDeadBytes;
    }

    public String getType() {
        return this.type;
    }

    public byte[] getUserType() {
        return this.userType;
    }

    public Container getParent() {
        return this.parent;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public boolean isParsed() {
        return this.isParsed;
    }

    private boolean verify(ByteBuffer content) {
        ByteBuffer bb = ByteBuffer.allocate(CastUtils.l2i(getContentSize() + ((long) (this.deadBytes != null ? this.deadBytes.limit() : 0))));
        getContent(bb);
        if (this.deadBytes != null) {
            this.deadBytes.rewind();
            while (this.deadBytes.remaining() > 0) {
                bb.put(this.deadBytes);
            }
        }
        content.rewind();
        bb.rewind();
        if (content.remaining() != bb.remaining()) {
            PrintStream printStream = System.err;
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(getType()));
            stringBuilder.append(": remaining differs ");
            stringBuilder.append(content.remaining());
            stringBuilder.append(" vs. ");
            stringBuilder.append(bb.remaining());
            printStream.print(stringBuilder.toString());
            Logger logger = LOG;
            stringBuilder = new StringBuilder(String.valueOf(getType()));
            stringBuilder.append(": remaining differs ");
            stringBuilder.append(content.remaining());
            stringBuilder.append(" vs. ");
            stringBuilder.append(bb.remaining());
            logger.logError(stringBuilder.toString());
            return false;
        }
        int p = content.position();
        int i = content.limit() - 1;
        int j = bb.limit() - 1;
        while (i >= p) {
            if (content.get(i) != bb.get(j)) {
                LOG.logError(String.format("%s: buffers differ at %d: %2X/%2X", new Object[]{getType(), Integer.valueOf(i), Byte.valueOf(content.get(i)), Byte.valueOf(bb.get(j))}));
                byte[] b1 = new byte[content.remaining()];
                byte[] b2 = new byte[bb.remaining()];
                content.get(b1);
                bb.get(b2);
                PrintStream printStream2 = System.err;
                StringBuilder stringBuilder2 = new StringBuilder("original      : ");
                stringBuilder2.append(Hex.encodeHex(b1, 4));
                printStream2.println(stringBuilder2.toString());
                printStream2 = System.err;
                stringBuilder2 = new StringBuilder("reconstructed : ");
                stringBuilder2.append(Hex.encodeHex(b2, 4));
                printStream2.println(stringBuilder2.toString());
                return false;
            }
            i--;
            j--;
        }
        return true;
    }

    private boolean isSmallBox() {
        int baseSize = 8;
        if (UserBox.TYPE.equals(getType())) {
            baseSize = 8 + 16;
        }
        if (!this.isRead) {
            return this.memMapSize + ((long) baseSize) < 4294967296L;
        } else {
            if (!this.isParsed) {
                return ((long) (this.content.limit() + baseSize)) < 4294967296L;
            } else {
                return (getContentSize() + ((long) (this.deadBytes != null ? this.deadBytes.limit() : 0))) + ((long) baseSize) < 4294967296L;
            }
        }
    }

    private void getHeader(ByteBuffer byteBuffer) {
        if (isSmallBox()) {
            IsoTypeWriter.writeUInt32(byteBuffer, getSize());
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, 1);
            byteBuffer.put(IsoFile.fourCCtoBytes(getType()));
            IsoTypeWriter.writeUInt64(byteBuffer, getSize());
        }
        if (UserBox.TYPE.equals(getType())) {
            byteBuffer.put(getUserType());
        }
    }

    public String getPath() {
        return Path.createPath(this);
    }
}
