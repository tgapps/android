package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class ApicFrame extends Id3Frame {
    public static final Creator<ApicFrame> CREATOR = new Creator<ApicFrame>() {
        public ApicFrame createFromParcel(Parcel in) {
            return new ApicFrame(in);
        }

        public ApicFrame[] newArray(int size) {
            return new ApicFrame[size];
        }
    };
    public static final String ID = "APIC";
    public final String description;
    public final String mimeType;
    public final byte[] pictureData;
    public final int pictureType;

    public ApicFrame(String mimeType, String description, int pictureType, byte[] pictureData) {
        super(ID);
        this.mimeType = mimeType;
        this.description = description;
        this.pictureType = pictureType;
        this.pictureData = pictureData;
    }

    ApicFrame(Parcel in) {
        super(ID);
        this.mimeType = (String) Util.castNonNull(in.readString());
        this.description = (String) Util.castNonNull(in.readString());
        this.pictureType = in.readInt();
        this.pictureData = (byte[]) Util.castNonNull(in.createByteArray());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ApicFrame other = (ApicFrame) obj;
        if (this.pictureType == other.pictureType && Util.areEqual(this.mimeType, other.mimeType) && Util.areEqual(this.description, other.description) && Arrays.equals(this.pictureData, other.pictureData)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int i2 = (this.pictureType + 527) * 31;
        if (this.mimeType != null) {
            hashCode = this.mimeType.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.description != null) {
            i = this.description.hashCode();
        }
        return ((hashCode + i) * 31) + Arrays.hashCode(this.pictureData);
    }

    public String toString() {
        return this.id + ": mimeType=" + this.mimeType + ", description=" + this.description;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mimeType);
        dest.writeString(this.description);
        dest.writeInt(this.pictureType);
        dest.writeByteArray(this.pictureData);
    }
}
