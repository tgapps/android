package com.google.android.exoplayer2.offline;

import android.net.Uri;
import com.google.android.exoplayer2.offline.DownloadAction.Deserializer;
import com.google.android.exoplayer2.util.Assertions;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SegmentDownloadAction extends DownloadAction {
    public final List<StreamKey> keys;

    protected static abstract class SegmentDownloadActionDeserializer extends Deserializer {
        protected abstract DownloadAction createDownloadAction(Uri uri, boolean z, byte[] bArr, List<StreamKey> list);

        public SegmentDownloadActionDeserializer(String type, int version) {
            super(type, version);
        }

        public final DownloadAction readFromStream(int version, DataInputStream input) throws IOException {
            Uri uri = Uri.parse(input.readUTF());
            boolean isRemoveAction = input.readBoolean();
            byte[] data = new byte[input.readInt()];
            input.readFully(data);
            int keyCount = input.readInt();
            List<StreamKey> keys = new ArrayList();
            for (int i = 0; i < keyCount; i++) {
                keys.add(readKey(version, input));
            }
            return createDownloadAction(uri, isRemoveAction, data, keys);
        }

        protected StreamKey readKey(int version, DataInputStream input) throws IOException {
            return new StreamKey(input.readInt(), input.readInt(), input.readInt());
        }
    }

    protected SegmentDownloadAction(String type, int version, Uri uri, boolean isRemoveAction, byte[] data, List<StreamKey> keys) {
        super(type, version, uri, isRemoveAction, data);
        if (isRemoveAction) {
            Assertions.checkArgument(keys.isEmpty());
            this.keys = Collections.emptyList();
            return;
        }
        ArrayList<StreamKey> mutableKeys = new ArrayList(keys);
        Collections.sort(mutableKeys);
        this.keys = Collections.unmodifiableList(mutableKeys);
    }

    public List<StreamKey> getKeys() {
        return this.keys;
    }

    public final void writeToStream(DataOutputStream output) throws IOException {
        output.writeUTF(this.uri.toString());
        output.writeBoolean(this.isRemoveAction);
        output.writeInt(this.data.length);
        output.write(this.data);
        output.writeInt(this.keys.size());
        for (int i = 0; i < this.keys.size(); i++) {
            writeKey(output, (StreamKey) this.keys.get(i));
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!super.equals(o)) {
            return false;
        }
        return this.keys.equals(((SegmentDownloadAction) o).keys);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + this.keys.hashCode();
    }

    private void writeKey(DataOutputStream output, StreamKey key) throws IOException {
        output.writeInt(key.periodIndex);
        output.writeInt(key.groupIndex);
        output.writeInt(key.trackIndex);
    }
}
