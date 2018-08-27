package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.util.PriorityTaskManager;

public final class PriorityDataSourceFactory implements Factory {
    private final int priority;
    private final PriorityTaskManager priorityTaskManager;
    private final Factory upstreamFactory;

    public PriorityDataSourceFactory(Factory upstreamFactory, PriorityTaskManager priorityTaskManager, int priority) {
        this.upstreamFactory = upstreamFactory;
        this.priorityTaskManager = priorityTaskManager;
        this.priority = priority;
    }

    public PriorityDataSource createDataSource() {
        return new PriorityDataSource(this.upstreamFactory.createDataSource(), this.priorityTaskManager, this.priority);
    }
}
