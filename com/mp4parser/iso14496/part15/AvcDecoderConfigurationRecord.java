package com.mp4parser.iso14496.part15;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitWriterBuffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AvcDecoderConfigurationRecord {
    public int avcLevelIndication;
    public int avcProfileIndication;
    public int bitDepthChromaMinus8 = 0;
    public int bitDepthChromaMinus8PaddingBits = 31;
    public int bitDepthLumaMinus8 = 0;
    public int bitDepthLumaMinus8PaddingBits = 31;
    public int chromaFormat = 1;
    public int chromaFormatPaddingBits = 31;
    public int configurationVersion;
    public boolean hasExts = true;
    public int lengthSizeMinusOne;
    public int lengthSizeMinusOnePaddingBits = 63;
    public int numberOfSequenceParameterSetsPaddingBits = 7;
    public List<byte[]> pictureParameterSets = new ArrayList();
    public int profileCompatibility;
    public List<byte[]> sequenceParameterSetExts = new ArrayList();
    public List<byte[]> sequenceParameterSets = new ArrayList();

    public AvcDecoderConfigurationRecord(ByteBuffer content) {
        int i;
        this.configurationVersion = IsoTypeReader.readUInt8(content);
        this.avcProfileIndication = IsoTypeReader.readUInt8(content);
        this.profileCompatibility = IsoTypeReader.readUInt8(content);
        this.avcLevelIndication = IsoTypeReader.readUInt8(content);
        BitReaderBuffer brb = new BitReaderBuffer(content);
        this.lengthSizeMinusOnePaddingBits = brb.readBits(6);
        this.lengthSizeMinusOne = brb.readBits(2);
        this.numberOfSequenceParameterSetsPaddingBits = brb.readBits(3);
        int numberOfSeuqenceParameterSets = brb.readBits(5);
        for (i = 0; i < numberOfSeuqenceParameterSets; i++) {
            byte[] sequenceParameterSetNALUnit = new byte[IsoTypeReader.readUInt16(content)];
            content.get(sequenceParameterSetNALUnit);
            this.sequenceParameterSets.add(sequenceParameterSetNALUnit);
        }
        long numberOfPictureParameterSets = (long) IsoTypeReader.readUInt8(content);
        for (i = 0; ((long) i) < numberOfPictureParameterSets; i++) {
            byte[] pictureParameterSetNALUnit = new byte[IsoTypeReader.readUInt16(content)];
            content.get(pictureParameterSetNALUnit);
            this.pictureParameterSets.add(pictureParameterSetNALUnit);
        }
        if (content.remaining() < 4) {
            this.hasExts = false;
        }
        if (this.hasExts && (this.avcProfileIndication == 100 || this.avcProfileIndication == 110 || this.avcProfileIndication == 122 || this.avcProfileIndication == 144)) {
            BitReaderBuffer brb2 = new BitReaderBuffer(content);
            this.chromaFormatPaddingBits = brb2.readBits(6);
            this.chromaFormat = brb2.readBits(2);
            this.bitDepthLumaMinus8PaddingBits = brb2.readBits(5);
            this.bitDepthLumaMinus8 = brb2.readBits(3);
            this.bitDepthChromaMinus8PaddingBits = brb2.readBits(5);
            this.bitDepthChromaMinus8 = brb2.readBits(3);
            long numOfSequenceParameterSetExt = (long) IsoTypeReader.readUInt8(content);
            for (brb = null; ((long) brb) < numOfSequenceParameterSetExt; brb++) {
                byte[] sequenceParameterSetExtNALUnit = new byte[IsoTypeReader.readUInt16(content)];
                content.get(sequenceParameterSetExtNALUnit);
                this.sequenceParameterSetExts.add(sequenceParameterSetExtNALUnit);
            }
            return;
        }
        this.chromaFormat = -1;
        this.bitDepthLumaMinus8 = -1;
        this.bitDepthChromaMinus8 = -1;
        brb2 = brb;
    }

    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt8(byteBuffer, this.configurationVersion);
        IsoTypeWriter.writeUInt8(byteBuffer, this.avcProfileIndication);
        IsoTypeWriter.writeUInt8(byteBuffer, this.profileCompatibility);
        IsoTypeWriter.writeUInt8(byteBuffer, this.avcLevelIndication);
        BitWriterBuffer bwb = new BitWriterBuffer(byteBuffer);
        bwb.writeBits(this.lengthSizeMinusOnePaddingBits, 6);
        bwb.writeBits(this.lengthSizeMinusOne, 2);
        bwb.writeBits(this.numberOfSequenceParameterSetsPaddingBits, 3);
        bwb.writeBits(this.pictureParameterSets.size(), 5);
        for (byte[] sequenceParameterSetNALUnit : this.sequenceParameterSets) {
            IsoTypeWriter.writeUInt16(byteBuffer, sequenceParameterSetNALUnit.length);
            byteBuffer.put(sequenceParameterSetNALUnit);
        }
        IsoTypeWriter.writeUInt8(byteBuffer, this.pictureParameterSets.size());
        for (byte[] pictureParameterSetNALUnit : this.pictureParameterSets) {
            IsoTypeWriter.writeUInt16(byteBuffer, pictureParameterSetNALUnit.length);
            byteBuffer.put(pictureParameterSetNALUnit);
        }
        if (!this.hasExts) {
            return;
        }
        if (this.avcProfileIndication == 100 || this.avcProfileIndication == 110 || this.avcProfileIndication == 122 || this.avcProfileIndication == 144) {
            BitWriterBuffer bwb2 = new BitWriterBuffer(byteBuffer);
            bwb2.writeBits(this.chromaFormatPaddingBits, 6);
            bwb2.writeBits(this.chromaFormat, 2);
            bwb2.writeBits(this.bitDepthLumaMinus8PaddingBits, 5);
            bwb2.writeBits(this.bitDepthLumaMinus8, 3);
            bwb2.writeBits(this.bitDepthChromaMinus8PaddingBits, 5);
            bwb2.writeBits(this.bitDepthChromaMinus8, 3);
            for (byte[] bwb3 : this.sequenceParameterSetExts) {
                IsoTypeWriter.writeUInt16(byteBuffer, bwb3.length);
                byteBuffer.put(bwb3);
            }
            bwb = bwb2;
        }
    }

    public long getContentSize() {
        long size = 5 + 1;
        for (byte[] sequenceParameterSetNALUnit : this.sequenceParameterSets) {
            size = (size + 2) + ((long) sequenceParameterSetNALUnit.length);
        }
        long size2 = size + 1;
        for (byte[] pictureParameterSetNALUnit : this.pictureParameterSets) {
            size2 = (size2 + 2) + ((long) pictureParameterSetNALUnit.length);
        }
        if (this.hasExts && (this.avcProfileIndication == 100 || this.avcProfileIndication == 110 || this.avcProfileIndication == 122 || this.avcProfileIndication == 144)) {
            size = size2 + 4;
            size2 = size;
            for (byte[] sequenceParameterSetExtNALUnit : this.sequenceParameterSetExts) {
                size2 = (size2 + 2) + ((long) sequenceParameterSetExtNALUnit.length);
            }
        }
        return size2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("AvcDecoderConfigurationRecord{configurationVersion=");
        stringBuilder.append(this.configurationVersion);
        stringBuilder.append(", avcProfileIndication=");
        stringBuilder.append(this.avcProfileIndication);
        stringBuilder.append(", profileCompatibility=");
        stringBuilder.append(this.profileCompatibility);
        stringBuilder.append(", avcLevelIndication=");
        stringBuilder.append(this.avcLevelIndication);
        stringBuilder.append(", lengthSizeMinusOne=");
        stringBuilder.append(this.lengthSizeMinusOne);
        stringBuilder.append(", hasExts=");
        stringBuilder.append(this.hasExts);
        stringBuilder.append(", chromaFormat=");
        stringBuilder.append(this.chromaFormat);
        stringBuilder.append(", bitDepthLumaMinus8=");
        stringBuilder.append(this.bitDepthLumaMinus8);
        stringBuilder.append(", bitDepthChromaMinus8=");
        stringBuilder.append(this.bitDepthChromaMinus8);
        stringBuilder.append(", lengthSizeMinusOnePaddingBits=");
        stringBuilder.append(this.lengthSizeMinusOnePaddingBits);
        stringBuilder.append(", numberOfSequenceParameterSetsPaddingBits=");
        stringBuilder.append(this.numberOfSequenceParameterSetsPaddingBits);
        stringBuilder.append(", chromaFormatPaddingBits=");
        stringBuilder.append(this.chromaFormatPaddingBits);
        stringBuilder.append(", bitDepthLumaMinus8PaddingBits=");
        stringBuilder.append(this.bitDepthLumaMinus8PaddingBits);
        stringBuilder.append(", bitDepthChromaMinus8PaddingBits=");
        stringBuilder.append(this.bitDepthChromaMinus8PaddingBits);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
