package com.coremedia.iso;

import java.nio.ByteBuffer;

public final class IsoTypeReaderVariable {
    public static long read(ByteBuffer bb, int bytes) {
        if (bytes == 8) {
            return IsoTypeReader.readUInt64(bb);
        }
        switch (bytes) {
            case 1:
                return (long) IsoTypeReader.readUInt8(bb);
            case 2:
                return (long) IsoTypeReader.readUInt16(bb);
            case 3:
                return (long) IsoTypeReader.readUInt24(bb);
            case 4:
                return IsoTypeReader.readUInt32(bb);
            default:
                StringBuilder stringBuilder = new StringBuilder("I don't know how to read ");
                stringBuilder.append(bytes);
                stringBuilder.append(" bytes");
                throw new RuntimeException(stringBuilder.toString());
        }
    }
}
