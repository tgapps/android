package com.coremedia.iso;

import java.nio.ByteBuffer;

public final class IsoTypeWriterVariable {
    public static void write(long v, ByteBuffer bb, int bytes) {
        if (bytes != 8) {
            switch (bytes) {
                case 1:
                    IsoTypeWriter.writeUInt8(bb, (int) (v & 255));
                    return;
                case 2:
                    IsoTypeWriter.writeUInt16(bb, (int) (v & 65535));
                    return;
                case 3:
                    IsoTypeWriter.writeUInt24(bb, (int) (v & 16777215));
                    return;
                case 4:
                    IsoTypeWriter.writeUInt32(bb, v);
                    return;
                default:
                    StringBuilder stringBuilder = new StringBuilder("I don't know how to read ");
                    stringBuilder.append(bytes);
                    stringBuilder.append(" bytes");
                    throw new RuntimeException(stringBuilder.toString());
            }
        }
        IsoTypeWriter.writeUInt64(bb, v);
    }
}
