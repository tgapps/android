package com.google.android.exoplayer2.text.dvb;

import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

public final class DvbDecoder extends SimpleSubtitleDecoder {
    private final DvbParser parser;

    public DvbDecoder(List<byte[]> initializationData) {
        super("DvbDecoder");
        ParsableByteArray data = new ParsableByteArray((byte[]) initializationData.get(0));
        this.parser = new DvbParser(data.readUnsignedShort(), data.readUnsignedShort());
    }

    protected DvbSubtitle decode(byte[] data, int length, boolean reset) {
        if (reset) {
            this.parser.reset();
        }
        return new DvbSubtitle(this.parser.decode(data, length));
    }
}
