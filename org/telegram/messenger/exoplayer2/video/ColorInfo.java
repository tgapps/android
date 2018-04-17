package org.telegram.messenger.exoplayer2.video;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class ColorInfo implements Parcelable {
    public static final Creator<ColorInfo> CREATOR = new Creator<ColorInfo>() {
        public ColorInfo createFromParcel(Parcel in) {
            return new ColorInfo(in);
        }

        public ColorInfo[] newArray(int size) {
            return new ColorInfo[0];
        }
    };
    public final int colorRange;
    public final int colorSpace;
    public final int colorTransfer;
    private int hashCode;
    public final byte[] hdrStaticInfo;

    public ColorInfo(int colorSpace, int colorRange, int colorTransfer, byte[] hdrStaticInfo) {
        this.colorSpace = colorSpace;
        this.colorRange = colorRange;
        this.colorTransfer = colorTransfer;
        this.hdrStaticInfo = hdrStaticInfo;
    }

    ColorInfo(Parcel in) {
        this.colorSpace = in.readInt();
        this.colorRange = in.readInt();
        this.colorTransfer = in.readInt();
        this.hdrStaticInfo = in.readInt() != 0 ? in.createByteArray() : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj != null) {
            if (getClass() == obj.getClass()) {
                ColorInfo other = (ColorInfo) obj;
                if (this.colorSpace != other.colorSpace || this.colorRange != other.colorRange || this.colorTransfer != other.colorTransfer || !Arrays.equals(this.hdrStaticInfo, other.hdrStaticInfo)) {
                    z = false;
                }
                return z;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ColorInfo(");
        stringBuilder.append(this.colorSpace);
        stringBuilder.append(", ");
        stringBuilder.append(this.colorRange);
        stringBuilder.append(", ");
        stringBuilder.append(this.colorTransfer);
        stringBuilder.append(", ");
        stringBuilder.append(this.hdrStaticInfo != null);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = (31 * ((31 * ((31 * ((31 * 17) + this.colorSpace)) + this.colorRange)) + this.colorTransfer)) + Arrays.hashCode(this.hdrStaticInfo);
        }
        return this.hashCode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.colorSpace);
        dest.writeInt(this.colorRange);
        dest.writeInt(this.colorTransfer);
        dest.writeInt(this.hdrStaticInfo != null ? 1 : 0);
        if (this.hdrStaticInfo != null) {
            dest.writeByteArray(this.hdrStaticInfo);
        }
    }
}
