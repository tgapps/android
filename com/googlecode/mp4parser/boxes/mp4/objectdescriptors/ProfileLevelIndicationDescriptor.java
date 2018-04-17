package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.IsoTypeReader;
import java.io.IOException;
import java.nio.ByteBuffer;

@Descriptor(tags = {20})
public class ProfileLevelIndicationDescriptor extends BaseDescriptor {
    int profileLevelIndicationIndex;

    public void parseDetail(ByteBuffer bb) throws IOException {
        this.profileLevelIndicationIndex = IsoTypeReader.readUInt8(bb);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProfileLevelIndicationDescriptor");
        sb.append("{profileLevelIndicationIndex=");
        sb.append(Integer.toHexString(this.profileLevelIndicationIndex));
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o != null) {
            if (getClass() == o.getClass()) {
                if (this.profileLevelIndicationIndex != ((ProfileLevelIndicationDescriptor) o).profileLevelIndicationIndex) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.profileLevelIndicationIndex;
    }
}
