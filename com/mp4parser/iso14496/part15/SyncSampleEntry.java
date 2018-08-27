package com.mp4parser.iso14496.part15;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import java.nio.ByteBuffer;

public class SyncSampleEntry extends GroupEntry {
    int nalUnitType;
    int reserved;

    public void parse(ByteBuffer byteBuffer) {
        int a = IsoTypeReader.readUInt8(byteBuffer);
        this.reserved = (a & PsExtractor.AUDIO_STREAM) >> 6;
        this.nalUnitType = a & 63;
    }

    public ByteBuffer get() {
        ByteBuffer b = ByteBuffer.allocate(1);
        IsoTypeWriter.writeUInt8(b, this.nalUnitType + (this.reserved << 6));
        return (ByteBuffer) b.rewind();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SyncSampleEntry that = (SyncSampleEntry) o;
        if (this.nalUnitType != that.nalUnitType) {
            return false;
        }
        if (this.reserved != that.reserved) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.reserved * 31) + this.nalUnitType;
    }

    public String getType() {
        return "sync";
    }

    public String toString() {
        return "SyncSampleEntry{reserved=" + this.reserved + ", nalUnitType=" + this.nalUnitType + '}';
    }
}
