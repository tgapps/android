package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;

public final class ByteArrayDataSource extends BaseDataSource {
    private int bytesRemaining;
    private final byte[] data;
    private boolean opened;
    private int readPosition;
    private Uri uri;

    public ByteArrayDataSource(byte[] data) {
        boolean z = false;
        super(false);
        Assertions.checkNotNull(data);
        if (data.length > 0) {
            z = true;
        }
        Assertions.checkArgument(z);
        this.data = data;
    }

    public long open(DataSpec dataSpec) throws IOException {
        this.uri = dataSpec.uri;
        transferInitializing(dataSpec);
        this.readPosition = (int) dataSpec.position;
        this.bytesRemaining = (int) (dataSpec.length == -1 ? ((long) this.data.length) - dataSpec.position : dataSpec.length);
        if (this.bytesRemaining <= 0 || this.readPosition + this.bytesRemaining > this.data.length) {
            throw new IOException("Unsatisfiable range: [" + this.readPosition + ", " + dataSpec.length + "], length: " + this.data.length);
        }
        this.opened = true;
        transferStarted(dataSpec);
        return (long) this.bytesRemaining;
    }

    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        if (readLength == 0) {
            return 0;
        } else if (this.bytesRemaining == 0) {
            r0 = readLength;
            return -1;
        } else {
            readLength = Math.min(readLength, this.bytesRemaining);
            System.arraycopy(this.data, this.readPosition, buffer, offset, readLength);
            this.readPosition += readLength;
            this.bytesRemaining -= readLength;
            bytesTransferred(readLength);
            r0 = readLength;
            return readLength;
        }
    }

    public Uri getUri() {
        return this.uri;
    }

    public void close() throws IOException {
        if (this.opened) {
            this.opened = false;
            transferEnded();
        }
        this.uri = null;
    }
}
