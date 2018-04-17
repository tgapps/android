package com.coremedia.iso;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.UserBox;
import com.googlecode.mp4parser.DataSource;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public abstract class AbstractBoxParser implements BoxParser {
    private static Logger LOG = Logger.getLogger(AbstractBoxParser.class.getName());
    ThreadLocal<ByteBuffer> header = new ThreadLocal<ByteBuffer>() {
        protected ByteBuffer initialValue() {
            return ByteBuffer.allocate(32);
        }
    };

    public abstract Box createBox(String str, byte[] bArr, String str2);

    public Box parseBox(DataSource byteChannel, Container parent) throws IOException {
        DataSource dataSource = byteChannel;
        Container container = parent;
        long startPos = byteChannel.position();
        ((ByteBuffer) this.header.get()).rewind().limit(8);
        while (true) {
            int read = dataSource.read((ByteBuffer) r6.header.get());
            int b = read;
            if (read == 8) {
                break;
            } else if (b < 0) {
                dataSource.position(startPos);
                throw new EOFException();
            } else {
                int i = b;
            }
        }
        ((ByteBuffer) r6.header.get()).rewind();
        long size = IsoTypeReader.readUInt32((ByteBuffer) r6.header.get());
        if (size >= 8 || size <= 1) {
            long contentSize;
            long contentSize2;
            byte[] usertype;
            byte[] usertype2;
            byte[] usertype3;
            Box box;
            String type = IsoTypeReader.read4cc((ByteBuffer) r6.header.get());
            if (size == 1) {
                ((ByteBuffer) r6.header.get()).limit(16);
                dataSource.read((ByteBuffer) r6.header.get());
                ((ByteBuffer) r6.header.get()).position(8);
                size = IsoTypeReader.readUInt64((ByteBuffer) r6.header.get());
                contentSize = size - 16;
            } else if (size == 0) {
                long contentSize3 = byteChannel.size() - byteChannel.position();
                long j = contentSize3 + 8;
                contentSize = contentSize3;
                contentSize2 = contentSize;
                if (UserBox.TYPE.equals(type)) {
                    usertype = null;
                    contentSize = contentSize2;
                } else {
                    ((ByteBuffer) r6.header.get()).limit(((ByteBuffer) r6.header.get()).limit() + 16);
                    dataSource.read((ByteBuffer) r6.header.get());
                    usertype2 = new byte[16];
                    for (usertype3 = ((ByteBuffer) r6.header.get()).position() - 16; usertype3 < ((ByteBuffer) r6.header.get()).position(); usertype3++) {
                        usertype2[usertype3 - (((ByteBuffer) r6.header.get()).position() - 16)] = ((ByteBuffer) r6.header.get()).get(usertype3);
                    }
                    usertype = usertype2;
                    contentSize = contentSize2 - 16;
                }
                box = createBox(type, usertype, container instanceof Box ? ((Box) container).getType() : TtmlNode.ANONYMOUS_REGION_ID);
                box.setParent(container);
                ((ByteBuffer) r6.header.get()).rewind();
                box.parse(dataSource, (ByteBuffer) r6.header.get(), contentSize, r6);
                return box;
            } else {
                contentSize = size - 8;
            }
            contentSize2 = contentSize;
            if (UserBox.TYPE.equals(type)) {
                usertype = null;
                contentSize = contentSize2;
            } else {
                ((ByteBuffer) r6.header.get()).limit(((ByteBuffer) r6.header.get()).limit() + 16);
                dataSource.read((ByteBuffer) r6.header.get());
                usertype2 = new byte[16];
                for (usertype3 = ((ByteBuffer) r6.header.get()).position() - 16; usertype3 < ((ByteBuffer) r6.header.get()).position(); usertype3++) {
                    usertype2[usertype3 - (((ByteBuffer) r6.header.get()).position() - 16)] = ((ByteBuffer) r6.header.get()).get(usertype3);
                }
                usertype = usertype2;
                contentSize = contentSize2 - 16;
            }
            if (container instanceof Box) {
            }
            box = createBox(type, usertype, container instanceof Box ? ((Box) container).getType() : TtmlNode.ANONYMOUS_REGION_ID);
            box.setParent(container);
            ((ByteBuffer) r6.header.get()).rewind();
            box.parse(dataSource, (ByteBuffer) r6.header.get(), contentSize, r6);
            return box;
        }
        Logger logger = LOG;
        StringBuilder stringBuilder = new StringBuilder("Plausibility check failed: size < 8 (size = ");
        stringBuilder.append(size);
        stringBuilder.append("). Stop parsing!");
        logger.severe(stringBuilder.toString());
        return null;
    }
}
