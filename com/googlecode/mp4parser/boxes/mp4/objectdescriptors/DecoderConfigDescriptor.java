package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Descriptor(tags = {4})
public class DecoderConfigDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(DecoderConfigDescriptor.class.getName());
    AudioSpecificConfig audioSpecificInfo;
    long avgBitRate;
    int bufferSizeDB;
    byte[] configDescriptorDeadBytes;
    DecoderSpecificInfo decoderSpecificInfo;
    long maxBitRate;
    int objectTypeIndication;
    List<ProfileLevelIndicationDescriptor> profileLevelIndicationDescriptors = new ArrayList();
    int streamType;
    int upStream;

    public void parseDetail(ByteBuffer bb) throws IOException {
        this.objectTypeIndication = IsoTypeReader.readUInt8(bb);
        int data = IsoTypeReader.readUInt8(bb);
        this.streamType = data >>> 2;
        this.upStream = (data >> 1) & 1;
        this.bufferSizeDB = IsoTypeReader.readUInt24(bb);
        this.maxBitRate = IsoTypeReader.readUInt32(bb);
        this.avgBitRate = IsoTypeReader.readUInt32(bb);
        if (bb.remaining() > 2) {
            int begin = bb.position();
            BaseDescriptor descriptor = ObjectDescriptorFactory.createFrom(this.objectTypeIndication, bb);
            int read = bb.position() - begin;
            Logger logger = log;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(descriptor);
            stringBuilder.append(" - DecoderConfigDescr1 read: ");
            stringBuilder.append(read);
            stringBuilder.append(", size: ");
            stringBuilder.append(descriptor != null ? Integer.valueOf(descriptor.getSize()) : null);
            logger.finer(stringBuilder.toString());
            if (descriptor != null) {
                int size = descriptor.getSize();
                if (read < size) {
                    this.configDescriptorDeadBytes = new byte[(size - read)];
                    bb.get(this.configDescriptorDeadBytes);
                }
            }
            if (descriptor instanceof DecoderSpecificInfo) {
                this.decoderSpecificInfo = (DecoderSpecificInfo) descriptor;
            }
            if (descriptor instanceof AudioSpecificConfig) {
                this.audioSpecificInfo = (AudioSpecificConfig) descriptor;
            }
        }
        while (bb.remaining() > 2) {
            long begin2 = (long) bb.position();
            BaseDescriptor descriptor2 = ObjectDescriptorFactory.createFrom(this.objectTypeIndication, bb);
            long read2 = ((long) bb.position()) - begin2;
            logger = log;
            stringBuilder = new StringBuilder();
            stringBuilder.append(descriptor2);
            stringBuilder.append(" - DecoderConfigDescr2 read: ");
            stringBuilder.append(read2);
            stringBuilder.append(", size: ");
            stringBuilder.append(descriptor2 != null ? Integer.valueOf(descriptor2.getSize()) : null);
            logger.finer(stringBuilder.toString());
            if (descriptor2 instanceof ProfileLevelIndicationDescriptor) {
                this.profileLevelIndicationDescriptors.add((ProfileLevelIndicationDescriptor) descriptor2);
            }
        }
    }

    public int serializedSize() {
        return 15 + (this.audioSpecificInfo == null ? 0 : this.audioSpecificInfo.serializedSize());
    }

    public ByteBuffer serialize() {
        ByteBuffer out = ByteBuffer.allocate(serializedSize());
        IsoTypeWriter.writeUInt8(out, 4);
        IsoTypeWriter.writeUInt8(out, serializedSize() - 2);
        IsoTypeWriter.writeUInt8(out, this.objectTypeIndication);
        IsoTypeWriter.writeUInt8(out, ((this.streamType << 2) | (this.upStream << 1)) | 1);
        IsoTypeWriter.writeUInt24(out, this.bufferSizeDB);
        IsoTypeWriter.writeUInt32(out, this.maxBitRate);
        IsoTypeWriter.writeUInt32(out, this.avgBitRate);
        if (this.audioSpecificInfo != null) {
            out.put(this.audioSpecificInfo.serialize().array());
        }
        return out;
    }

    public void setAudioSpecificInfo(AudioSpecificConfig audioSpecificInfo) {
        this.audioSpecificInfo = audioSpecificInfo;
    }

    public void setObjectTypeIndication(int objectTypeIndication) {
        this.objectTypeIndication = objectTypeIndication;
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public void setBufferSizeDB(int bufferSizeDB) {
        this.bufferSizeDB = bufferSizeDB;
    }

    public void setMaxBitRate(long maxBitRate) {
        this.maxBitRate = maxBitRate;
    }

    public void setAvgBitRate(long avgBitRate) {
        this.avgBitRate = avgBitRate;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DecoderConfigDescriptor");
        sb.append("{objectTypeIndication=");
        sb.append(this.objectTypeIndication);
        sb.append(", streamType=");
        sb.append(this.streamType);
        sb.append(", upStream=");
        sb.append(this.upStream);
        sb.append(", bufferSizeDB=");
        sb.append(this.bufferSizeDB);
        sb.append(", maxBitRate=");
        sb.append(this.maxBitRate);
        sb.append(", avgBitRate=");
        sb.append(this.avgBitRate);
        sb.append(", decoderSpecificInfo=");
        sb.append(this.decoderSpecificInfo);
        sb.append(", audioSpecificInfo=");
        sb.append(this.audioSpecificInfo);
        sb.append(", configDescriptorDeadBytes=");
        sb.append(Hex.encodeHex(this.configDescriptorDeadBytes != null ? this.configDescriptorDeadBytes : new byte[0]));
        sb.append(", profileLevelIndicationDescriptors=");
        sb.append(this.profileLevelIndicationDescriptors == null ? "null" : Arrays.asList(new List[]{this.profileLevelIndicationDescriptors}).toString());
        sb.append('}');
        return sb.toString();
    }
}
