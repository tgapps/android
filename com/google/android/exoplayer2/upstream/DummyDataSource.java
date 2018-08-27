package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import java.io.IOException;
import java.util.Map;

public final class DummyDataSource implements DataSource {
    public static final Factory FACTORY = new Factory() {
        public DataSource createDataSource() {
            return new DummyDataSource();
        }
    };
    public static final DummyDataSource INSTANCE = new DummyDataSource();

    public void addTransferListener(TransferListener transferListener) {
        DataSource$$CC.addTransferListener(this, transferListener);
    }

    public Map getResponseHeaders() {
        return DataSource$$CC.getResponseHeaders(this);
    }

    private DummyDataSource() {
    }

    public long open(DataSpec dataSpec) throws IOException {
        throw new IOException("Dummy source");
    }

    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        throw new UnsupportedOperationException();
    }

    public Uri getUri() {
        return null;
    }

    public void close() throws IOException {
    }
}
