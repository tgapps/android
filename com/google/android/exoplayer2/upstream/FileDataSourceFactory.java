package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.DataSource.Factory;

public final class FileDataSourceFactory implements Factory {
    private final TransferListener listener;

    public FileDataSourceFactory() {
        this(null);
    }

    public FileDataSourceFactory(TransferListener listener) {
        this.listener = listener;
    }

    public DataSource createDataSource() {
        return new FileDataSource(this.listener);
    }
}
