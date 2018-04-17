package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class UnknownDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(UnknownDescriptor.class.getName());
    private ByteBuffer data;

    public void parseDetail(ByteBuffer bb) throws IOException {
        this.data = (ByteBuffer) bb.slice().limit(getSizeOfInstance());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UnknownDescriptor");
        sb.append("{tag=");
        sb.append(this.tag);
        sb.append(", sizeOfInstance=");
        sb.append(this.sizeOfInstance);
        sb.append(", data=");
        sb.append(this.data);
        sb.append('}');
        return sb.toString();
    }
}
