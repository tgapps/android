package org.telegram.messenger.audioinfo.m4a;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import org.telegram.messenger.audioinfo.util.PositionInputStream;
import org.telegram.messenger.audioinfo.util.RangeInputStream;

public class MP4Box<I extends PositionInputStream> {
    protected static final String ASCII = "ISO8859_1";
    private MP4Atom child;
    protected final DataInput data;
    private final I input;
    private final MP4Box<?> parent;
    private final String type;

    public MP4Box(I input, MP4Box<?> parent, String type) {
        this.input = input;
        this.parent = parent;
        this.type = type;
        this.data = new DataInputStream(input);
    }

    public String getType() {
        return this.type;
    }

    public MP4Box<?> getParent() {
        return this.parent;
    }

    public long getPosition() {
        return this.input.getPosition();
    }

    public I getInput() {
        return this.input;
    }

    protected MP4Atom getChild() {
        return this.child;
    }

    public MP4Atom nextChild() throws IOException {
        if (this.child != null) {
            this.child.skip();
        }
        int atomLength = this.data.readInt();
        byte[] typeBytes = new byte[4];
        this.data.readFully(typeBytes);
        String atomType = new String(typeBytes, ASCII);
        RangeInputStream rangeInputStream;
        if (atomLength == 1) {
            rangeInputStream = new RangeInputStream(this.input, 16, this.data.readLong() - 16);
        } else {
            rangeInputStream = new RangeInputStream(this.input, 8, (long) (atomLength - 8));
        }
        MP4Atom mP4Atom = new MP4Atom(atomInput, this, atomType);
        this.child = mP4Atom;
        return mP4Atom;
    }

    public MP4Atom nextChild(String expectedTypeExpression) throws IOException {
        MP4Atom atom = nextChild();
        if (atom.getType().matches(expectedTypeExpression)) {
            return atom;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("atom type mismatch, expected ");
        stringBuilder.append(expectedTypeExpression);
        stringBuilder.append(", got ");
        stringBuilder.append(atom.getType());
        throw new IOException(stringBuilder.toString());
    }
}
