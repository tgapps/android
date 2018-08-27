package com.google.android.exoplayer2.metadata.emsg;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class EventMessageDecoder implements MetadataDecoder {
    public Metadata decode(MetadataInputBuffer inputBuffer) {
        ByteBuffer buffer = inputBuffer.data;
        ParsableByteArray parsableByteArray = new ParsableByteArray(buffer.array(), buffer.limit());
        String schemeIdUri = parsableByteArray.readNullTerminatedString();
        String value = parsableByteArray.readNullTerminatedString();
        long timescale = parsableByteArray.readUnsignedInt();
        long presentationTimeUs = Util.scaleLargeTimestamp(parsableByteArray.readUnsignedInt(), 1000000, timescale);
        long durationMs = Util.scaleLargeTimestamp(parsableByteArray.readUnsignedInt(), 1000, timescale);
        long id = parsableByteArray.readUnsignedInt();
        int position = parsableByteArray.getPosition();
        return new Metadata(new EventMessage(schemeIdUri, value, durationMs, id, Arrays.copyOfRange(data, position, size), presentationTimeUs));
    }
}
