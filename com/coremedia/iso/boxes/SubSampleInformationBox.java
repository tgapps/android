package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.runtime.reflect.Factory;

public class SubSampleInformationBox extends AbstractFullBox {
    public static final String TYPE = "subs";
    private static final /* synthetic */ StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ StaticPart ajc$tjp_2 = null;
    private List<SubSampleEntry> entries = new ArrayList();

    public static class SubSampleEntry {
        private long sampleDelta;
        private List<SubsampleEntry> subsampleEntries = new ArrayList();

        public static class SubsampleEntry {
            private int discardable;
            private long reserved;
            private int subsamplePriority;
            private long subsampleSize;

            public long getSubsampleSize() {
                return this.subsampleSize;
            }

            public void setSubsampleSize(long subsampleSize) {
                this.subsampleSize = subsampleSize;
            }

            public int getSubsamplePriority() {
                return this.subsamplePriority;
            }

            public void setSubsamplePriority(int subsamplePriority) {
                this.subsamplePriority = subsamplePriority;
            }

            public int getDiscardable() {
                return this.discardable;
            }

            public void setDiscardable(int discardable) {
                this.discardable = discardable;
            }

            public long getReserved() {
                return this.reserved;
            }

            public void setReserved(long reserved) {
                this.reserved = reserved;
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder("SubsampleEntry{subsampleSize=");
                stringBuilder.append(this.subsampleSize);
                stringBuilder.append(", subsamplePriority=");
                stringBuilder.append(this.subsamplePriority);
                stringBuilder.append(", discardable=");
                stringBuilder.append(this.discardable);
                stringBuilder.append(", reserved=");
                stringBuilder.append(this.reserved);
                stringBuilder.append('}');
                return stringBuilder.toString();
            }
        }

        public long getSampleDelta() {
            return this.sampleDelta;
        }

        public void setSampleDelta(long sampleDelta) {
            this.sampleDelta = sampleDelta;
        }

        public int getSubsampleCount() {
            return this.subsampleEntries.size();
        }

        public List<SubsampleEntry> getSubsampleEntries() {
            return this.subsampleEntries;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("SampleEntry{sampleDelta=");
            stringBuilder.append(this.sampleDelta);
            stringBuilder.append(", subsampleCount=");
            stringBuilder.append(this.subsampleEntries.size());
            stringBuilder.append(", subsampleEntries=");
            stringBuilder.append(this.subsampleEntries);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    static {
        ajc$preClinit();
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("SubSampleInformationBox.java", SubSampleInformationBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.SubSampleInformationBox", TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, "java.util.List"), 50);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.SubSampleInformationBox", "java.util.List", "entries", TtmlNode.ANONYMOUS_REGION_ID, "void"), 54);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.SubSampleInformationBox", TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, "java.lang.String"), 124);
    }

    public SubSampleInformationBox() {
        super(TYPE);
    }

    public List<SubSampleEntry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<SubSampleEntry> entries) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) entries));
        this.entries = entries;
    }

    protected long getContentSize() {
        long size = 8;
        for (SubSampleEntry entry : this.entries) {
            long size2 = (size + 4) + 2;
            int j = 0;
            while (j < entry.getSubsampleEntries().size()) {
                long size3;
                if (getVersion() == 1) {
                    size3 = size2 + 4;
                } else {
                    size3 = size2 + 2;
                }
                j++;
                size2 = (size3 + 2) + 4;
            }
            size = size2;
        }
        return size;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        long entryCount = IsoTypeReader.readUInt32(content);
        for (int i = 0; ((long) i) < entryCount; i++) {
            SubSampleEntry SubSampleEntry = new SubSampleEntry();
            SubSampleEntry.setSampleDelta(IsoTypeReader.readUInt32(content));
            int subsampleCount = IsoTypeReader.readUInt16(content);
            for (int j = 0; j < subsampleCount; j++) {
                SubsampleEntry subsampleEntry = new SubsampleEntry();
                subsampleEntry.setSubsampleSize(getVersion() == 1 ? IsoTypeReader.readUInt32(content) : (long) IsoTypeReader.readUInt16(content));
                subsampleEntry.setSubsamplePriority(IsoTypeReader.readUInt8(content));
                subsampleEntry.setDiscardable(IsoTypeReader.readUInt8(content));
                subsampleEntry.setReserved(IsoTypeReader.readUInt32(content));
                SubSampleEntry.getSubsampleEntries().add(subsampleEntry);
            }
            this.entries.add(SubSampleEntry);
        }
    }

    protected void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (SubSampleEntry subSampleEntry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, subSampleEntry.getSampleDelta());
            IsoTypeWriter.writeUInt16(byteBuffer, subSampleEntry.getSubsampleCount());
            for (SubsampleEntry subsampleEntry : subSampleEntry.getSubsampleEntries()) {
                if (getVersion() == 1) {
                    IsoTypeWriter.writeUInt32(byteBuffer, subsampleEntry.getSubsampleSize());
                } else {
                    IsoTypeWriter.writeUInt16(byteBuffer, CastUtils.l2i(subsampleEntry.getSubsampleSize()));
                }
                IsoTypeWriter.writeUInt8(byteBuffer, subsampleEntry.getSubsamplePriority());
                IsoTypeWriter.writeUInt8(byteBuffer, subsampleEntry.getDiscardable());
                IsoTypeWriter.writeUInt32(byteBuffer, subsampleEntry.getReserved());
            }
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        StringBuilder stringBuilder = new StringBuilder("SubSampleInformationBox{entryCount=");
        stringBuilder.append(this.entries.size());
        stringBuilder.append(", entries=");
        stringBuilder.append(this.entries);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
