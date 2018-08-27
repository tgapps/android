package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSource.Factory;

public final class DefaultHlsDataSourceFactory implements HlsDataSourceFactory {
    private final Factory dataSourceFactory;

    public DefaultHlsDataSourceFactory(Factory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public DataSource createDataSource(int dataType) {
        return this.dataSourceFactory.createDataSource();
    }
}
