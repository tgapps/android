package org.telegram.messenger.audioinfo.m4a;

import java.io.IOException;
import java.io.InputStream;
import org.telegram.messenger.audioinfo.util.PositionInputStream;

public final class MP4Input extends MP4Box<PositionInputStream> {
    public MP4Input(InputStream delegate) {
        super(new PositionInputStream(delegate), null, TtmlNode.ANONYMOUS_REGION_ID);
    }

    public MP4Atom nextChildUpTo(String expectedTypeExpression) throws IOException {
        while (true) {
            MP4Atom atom = nextChild();
            if (atom.getType().matches(expectedTypeExpression)) {
                return atom;
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mp4[pos=");
        stringBuilder.append(getPosition());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
