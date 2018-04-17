package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Descriptor(tags = {3})
public class ESDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(ESDescriptor.class.getName());
    int URLFlag;
    int URLLength = 0;
    String URLString;
    DecoderConfigDescriptor decoderConfigDescriptor;
    int dependsOnEsId;
    int esId;
    int oCREsId;
    int oCRstreamFlag;
    List<BaseDescriptor> otherDescriptors = new ArrayList();
    int remoteODFlag;
    SLConfigDescriptor slConfigDescriptor;
    int streamDependenceFlag;
    int streamPriority;

    public void parseDetail(ByteBuffer bb) throws IOException {
        BaseDescriptor descriptor;
        long read;
        this.esId = IsoTypeReader.readUInt16(bb);
        int data = IsoTypeReader.readUInt8(bb);
        this.streamDependenceFlag = data >>> 7;
        this.URLFlag = (data >>> 6) & 1;
        this.oCRstreamFlag = (data >>> 5) & 1;
        this.streamPriority = data & 31;
        if (this.streamDependenceFlag == 1) {
            this.dependsOnEsId = IsoTypeReader.readUInt16(bb);
        }
        if (this.URLFlag == 1) {
            this.URLLength = IsoTypeReader.readUInt8(bb);
            this.URLString = IsoTypeReader.readString(bb, this.URLLength);
        }
        if (this.oCRstreamFlag == 1) {
            this.oCREsId = IsoTypeReader.readUInt16(bb);
        }
        int i = 0;
        int baseSize = ((((getSizeBytes() + 1) + 2) + 1) + (this.streamDependenceFlag == 1 ? 2 : 0)) + (this.URLFlag == 1 ? this.URLLength + 1 : 0);
        if (this.oCRstreamFlag == 1) {
            i = 2;
        }
        baseSize += i;
        int begin = bb.position();
        if (getSize() > baseSize + 2) {
            descriptor = ObjectDescriptorFactory.createFrom(-1, bb);
            read = (long) (bb.position() - begin);
            Logger logger = log;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(descriptor);
            stringBuilder.append(" - ESDescriptor1 read: ");
            stringBuilder.append(read);
            stringBuilder.append(", size: ");
            stringBuilder.append(descriptor != null ? Integer.valueOf(descriptor.getSize()) : null);
            logger.finer(stringBuilder.toString());
            if (descriptor != null) {
                i = descriptor.getSize();
                bb.position(begin + i);
                baseSize += i;
            } else {
                baseSize = (int) (((long) baseSize) + read);
            }
            if (descriptor instanceof DecoderConfigDescriptor) {
                this.decoderConfigDescriptor = (DecoderConfigDescriptor) descriptor;
            }
        }
        begin = bb.position();
        if (getSize() > baseSize + 2) {
            descriptor = ObjectDescriptorFactory.createFrom(-1, bb);
            read = (long) (bb.position() - begin);
            logger = log;
            stringBuilder = new StringBuilder();
            stringBuilder.append(descriptor);
            stringBuilder.append(" - ESDescriptor2 read: ");
            stringBuilder.append(read);
            stringBuilder.append(", size: ");
            stringBuilder.append(descriptor != null ? Integer.valueOf(descriptor.getSize()) : null);
            logger.finer(stringBuilder.toString());
            if (descriptor != null) {
                i = descriptor.getSize();
                bb.position(begin + i);
                baseSize += i;
            } else {
                baseSize = (int) (((long) baseSize) + read);
            }
            if (descriptor instanceof SLConfigDescriptor) {
                this.slConfigDescriptor = (SLConfigDescriptor) descriptor;
            }
        } else {
            log.warning("SLConfigDescriptor is missing!");
        }
        while (getSize() - baseSize > 2) {
            begin = bb.position();
            descriptor = ObjectDescriptorFactory.createFrom(-1, bb);
            read = (long) (bb.position() - begin);
            logger = log;
            stringBuilder = new StringBuilder();
            stringBuilder.append(descriptor);
            stringBuilder.append(" - ESDescriptor3 read: ");
            stringBuilder.append(read);
            stringBuilder.append(", size: ");
            stringBuilder.append(descriptor != null ? Integer.valueOf(descriptor.getSize()) : null);
            logger.finer(stringBuilder.toString());
            if (descriptor != null) {
                i = descriptor.getSize();
                bb.position(begin + i);
                baseSize += i;
            } else {
                baseSize = (int) (((long) baseSize) + read);
            }
            this.otherDescriptors.add(descriptor);
        }
    }

    public int serializedSize() {
        int out = 5;
        if (this.streamDependenceFlag > 0) {
            out = 5 + 2;
        }
        if (this.URLFlag > 0) {
            out += 1 + this.URLLength;
        }
        if (this.oCRstreamFlag > 0) {
            out += 2;
        }
        return (out + this.decoderConfigDescriptor.serializedSize()) + this.slConfigDescriptor.serializedSize();
    }

    public ByteBuffer serialize() {
        ByteBuffer out = ByteBuffer.allocate(serializedSize());
        IsoTypeWriter.writeUInt8(out, 3);
        IsoTypeWriter.writeUInt8(out, serializedSize() - 2);
        IsoTypeWriter.writeUInt16(out, this.esId);
        IsoTypeWriter.writeUInt8(out, (((this.streamDependenceFlag << 7) | (this.URLFlag << 6)) | (this.oCRstreamFlag << 5)) | (this.streamPriority & 31));
        if (this.streamDependenceFlag > 0) {
            IsoTypeWriter.writeUInt16(out, this.dependsOnEsId);
        }
        if (this.URLFlag > 0) {
            IsoTypeWriter.writeUInt8(out, this.URLLength);
            IsoTypeWriter.writeUtf8String(out, this.URLString);
        }
        if (this.oCRstreamFlag > 0) {
            IsoTypeWriter.writeUInt16(out, this.oCREsId);
        }
        ByteBuffer dec = this.decoderConfigDescriptor.serialize();
        ByteBuffer sl = this.slConfigDescriptor.serialize();
        out.put(dec.array());
        out.put(sl.array());
        return out;
    }

    public void setDecoderConfigDescriptor(DecoderConfigDescriptor decoderConfigDescriptor) {
        this.decoderConfigDescriptor = decoderConfigDescriptor;
    }

    public void setSlConfigDescriptor(SLConfigDescriptor slConfigDescriptor) {
        this.slConfigDescriptor = slConfigDescriptor;
    }

    public void setEsId(int esId) {
        this.esId = esId;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ESDescriptor");
        sb.append("{esId=");
        sb.append(this.esId);
        sb.append(", streamDependenceFlag=");
        sb.append(this.streamDependenceFlag);
        sb.append(", URLFlag=");
        sb.append(this.URLFlag);
        sb.append(", oCRstreamFlag=");
        sb.append(this.oCRstreamFlag);
        sb.append(", streamPriority=");
        sb.append(this.streamPriority);
        sb.append(", URLLength=");
        sb.append(this.URLLength);
        sb.append(", URLString='");
        sb.append(this.URLString);
        sb.append('\'');
        sb.append(", remoteODFlag=");
        sb.append(this.remoteODFlag);
        sb.append(", dependsOnEsId=");
        sb.append(this.dependsOnEsId);
        sb.append(", oCREsId=");
        sb.append(this.oCREsId);
        sb.append(", decoderConfigDescriptor=");
        sb.append(this.decoderConfigDescriptor);
        sb.append(", slConfigDescriptor=");
        sb.append(this.slConfigDescriptor);
        sb.append('}');
        return sb.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r6) {
        /*
        r5 = this;
        r0 = 1;
        if (r5 != r6) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        if (r6 == 0) goto L_0x00a6;
    L_0x0007:
        r2 = r5.getClass();
        r3 = r6.getClass();
        if (r2 == r3) goto L_0x0013;
    L_0x0011:
        goto L_0x00a6;
    L_0x0013:
        r2 = r6;
        r2 = (com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor) r2;
        r3 = r5.URLFlag;
        r4 = r2.URLFlag;
        if (r3 == r4) goto L_0x001d;
    L_0x001c:
        return r1;
    L_0x001d:
        r3 = r5.URLLength;
        r4 = r2.URLLength;
        if (r3 == r4) goto L_0x0024;
    L_0x0023:
        return r1;
    L_0x0024:
        r3 = r5.dependsOnEsId;
        r4 = r2.dependsOnEsId;
        if (r3 == r4) goto L_0x002b;
    L_0x002a:
        return r1;
    L_0x002b:
        r3 = r5.esId;
        r4 = r2.esId;
        if (r3 == r4) goto L_0x0032;
    L_0x0031:
        return r1;
    L_0x0032:
        r3 = r5.oCREsId;
        r4 = r2.oCREsId;
        if (r3 == r4) goto L_0x0039;
    L_0x0038:
        return r1;
    L_0x0039:
        r3 = r5.oCRstreamFlag;
        r4 = r2.oCRstreamFlag;
        if (r3 == r4) goto L_0x0040;
    L_0x003f:
        return r1;
    L_0x0040:
        r3 = r5.remoteODFlag;
        r4 = r2.remoteODFlag;
        if (r3 == r4) goto L_0x0047;
    L_0x0046:
        return r1;
    L_0x0047:
        r3 = r5.streamDependenceFlag;
        r4 = r2.streamDependenceFlag;
        if (r3 == r4) goto L_0x004e;
    L_0x004d:
        return r1;
    L_0x004e:
        r3 = r5.streamPriority;
        r4 = r2.streamPriority;
        if (r3 == r4) goto L_0x0055;
    L_0x0054:
        return r1;
    L_0x0055:
        r3 = r5.URLString;
        if (r3 == 0) goto L_0x0064;
    L_0x0059:
        r3 = r5.URLString;
        r4 = r2.URLString;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x0069;
    L_0x0063:
        goto L_0x0068;
    L_0x0064:
        r3 = r2.URLString;
        if (r3 == 0) goto L_0x0069;
    L_0x0068:
        return r1;
    L_0x0069:
        r3 = r5.decoderConfigDescriptor;
        if (r3 == 0) goto L_0x0078;
    L_0x006d:
        r3 = r5.decoderConfigDescriptor;
        r4 = r2.decoderConfigDescriptor;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x007d;
    L_0x0077:
        goto L_0x007c;
    L_0x0078:
        r3 = r2.decoderConfigDescriptor;
        if (r3 == 0) goto L_0x007d;
    L_0x007c:
        return r1;
    L_0x007d:
        r3 = r5.otherDescriptors;
        if (r3 == 0) goto L_0x008c;
    L_0x0081:
        r3 = r5.otherDescriptors;
        r4 = r2.otherDescriptors;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x0091;
    L_0x008b:
        goto L_0x0090;
    L_0x008c:
        r3 = r2.otherDescriptors;
        if (r3 == 0) goto L_0x0091;
    L_0x0090:
        return r1;
    L_0x0091:
        r3 = r5.slConfigDescriptor;
        if (r3 == 0) goto L_0x00a0;
    L_0x0095:
        r3 = r5.slConfigDescriptor;
        r4 = r2.slConfigDescriptor;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x00a5;
    L_0x009f:
        goto L_0x00a4;
    L_0x00a0:
        r3 = r2.slConfigDescriptor;
        if (r3 == 0) goto L_0x00a5;
    L_0x00a4:
        return r1;
    L_0x00a5:
        return r0;
    L_0x00a6:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i = 0;
        int result = 31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * this.esId) + this.streamDependenceFlag)) + this.URLFlag)) + this.oCRstreamFlag)) + this.streamPriority)) + this.URLLength)) + (this.URLString != null ? this.URLString.hashCode() : 0))) + this.remoteODFlag)) + this.dependsOnEsId)) + this.oCREsId)) + (this.decoderConfigDescriptor != null ? this.decoderConfigDescriptor.hashCode() : 0))) + (this.slConfigDescriptor != null ? this.slConfigDescriptor.hashCode() : 0));
        if (this.otherDescriptors != null) {
            i = this.otherDescriptors.hashCode();
        }
        return result + i;
    }
}
