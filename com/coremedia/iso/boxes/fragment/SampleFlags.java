package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

public class SampleFlags {
    private byte is_leading;
    private byte reserved;
    private int sampleDegradationPriority;
    private byte sampleDependsOn;
    private byte sampleHasRedundancy;
    private byte sampleIsDependedOn;
    private boolean sampleIsDifferenceSample;
    private byte samplePaddingValue;

    public SampleFlags(ByteBuffer bb) {
        long a = IsoTypeReader.readUInt32(bb);
        this.reserved = (byte) ((int) ((a & -268435456) >> 28));
        this.is_leading = (byte) ((int) ((a & 201326592) >> 26));
        this.sampleDependsOn = (byte) ((int) ((a & 50331648) >> 24));
        this.sampleIsDependedOn = (byte) ((int) ((a & 12582912) >> 22));
        this.sampleHasRedundancy = (byte) ((int) ((a & 3145728) >> 20));
        this.samplePaddingValue = (byte) ((int) ((a & 917504) >> 17));
        this.sampleIsDifferenceSample = ((a & 65536) >> 16) > 0;
        this.sampleDegradationPriority = (int) (a & 65535);
    }

    public void getContent(ByteBuffer os) {
        IsoTypeWriter.writeUInt32(os, (((((((0 | ((long) (this.reserved << 28))) | ((long) (this.is_leading << 26))) | ((long) (this.sampleDependsOn << 24))) | ((long) (this.sampleIsDependedOn << 22))) | ((long) (this.sampleHasRedundancy << 20))) | ((long) (this.samplePaddingValue << 17))) | ((long) (this.sampleIsDifferenceSample << 16))) | ((long) this.sampleDegradationPriority));
    }

    public int getReserved() {
        return this.reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = (byte) reserved;
    }

    public int getSampleDependsOn() {
        return this.sampleDependsOn;
    }

    public void setSampleDependsOn(int sampleDependsOn) {
        this.sampleDependsOn = (byte) sampleDependsOn;
    }

    public int getSampleIsDependedOn() {
        return this.sampleIsDependedOn;
    }

    public void setSampleIsDependedOn(int sampleIsDependedOn) {
        this.sampleIsDependedOn = (byte) sampleIsDependedOn;
    }

    public int getSampleHasRedundancy() {
        return this.sampleHasRedundancy;
    }

    public void setSampleHasRedundancy(int sampleHasRedundancy) {
        this.sampleHasRedundancy = (byte) sampleHasRedundancy;
    }

    public int getSamplePaddingValue() {
        return this.samplePaddingValue;
    }

    public void setSamplePaddingValue(int samplePaddingValue) {
        this.samplePaddingValue = (byte) samplePaddingValue;
    }

    public boolean isSampleIsDifferenceSample() {
        return this.sampleIsDifferenceSample;
    }

    public void setSampleIsDifferenceSample(boolean sampleIsDifferenceSample) {
        this.sampleIsDifferenceSample = sampleIsDifferenceSample;
    }

    public int getSampleDegradationPriority() {
        return this.sampleDegradationPriority;
    }

    public void setSampleDegradationPriority(int sampleDegradationPriority) {
        this.sampleDegradationPriority = sampleDegradationPriority;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("SampleFlags{reserved=");
        stringBuilder.append(this.reserved);
        stringBuilder.append(", isLeading=");
        stringBuilder.append(this.is_leading);
        stringBuilder.append(", depOn=");
        stringBuilder.append(this.sampleDependsOn);
        stringBuilder.append(", isDepOn=");
        stringBuilder.append(this.sampleIsDependedOn);
        stringBuilder.append(", hasRedundancy=");
        stringBuilder.append(this.sampleHasRedundancy);
        stringBuilder.append(", padValue=");
        stringBuilder.append(this.samplePaddingValue);
        stringBuilder.append(", isDiffSample=");
        stringBuilder.append(this.sampleIsDifferenceSample);
        stringBuilder.append(", degradPrio=");
        stringBuilder.append(this.sampleDegradationPriority);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null) {
            if (getClass() == o.getClass()) {
                SampleFlags that = (SampleFlags) o;
                if (this.is_leading == that.is_leading && this.reserved == that.reserved && this.sampleDegradationPriority == that.sampleDegradationPriority && this.sampleDependsOn == that.sampleDependsOn && this.sampleHasRedundancy == that.sampleHasRedundancy && this.sampleIsDependedOn == that.sampleIsDependedOn && this.sampleIsDifferenceSample == that.sampleIsDifferenceSample && this.samplePaddingValue == that.samplePaddingValue) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public int hashCode() {
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * this.reserved) + this.is_leading)) + this.sampleDependsOn)) + this.sampleIsDependedOn)) + this.sampleHasRedundancy)) + this.samplePaddingValue)) + this.sampleIsDifferenceSample)) + this.sampleDegradationPriority;
    }
}
