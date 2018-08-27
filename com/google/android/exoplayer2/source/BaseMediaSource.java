package com.google.android.exoplayer2.source;

import android.os.Handler;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSource.SourceInfoRefreshListener;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Assertions;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class BaseMediaSource implements MediaSource {
    private final EventDispatcher eventDispatcher = new EventDispatcher();
    private Object manifest;
    private ExoPlayer player;
    private final ArrayList<SourceInfoRefreshListener> sourceInfoListeners = new ArrayList(1);
    private Timeline timeline;

    protected abstract void prepareSourceInternal(ExoPlayer exoPlayer, boolean z, TransferListener transferListener);

    protected abstract void releaseSourceInternal();

    protected final void refreshSourceInfo(Timeline timeline, Object manifest) {
        this.timeline = timeline;
        this.manifest = manifest;
        Iterator it = this.sourceInfoListeners.iterator();
        while (it.hasNext()) {
            ((SourceInfoRefreshListener) it.next()).onSourceInfoRefreshed(this, timeline, manifest);
        }
    }

    protected final EventDispatcher createEventDispatcher(MediaPeriodId mediaPeriodId) {
        return this.eventDispatcher.withParameters(0, mediaPeriodId, 0);
    }

    protected final EventDispatcher createEventDispatcher(MediaPeriodId mediaPeriodId, long mediaTimeOffsetMs) {
        boolean z;
        if (mediaPeriodId != null) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkArgument(z);
        return this.eventDispatcher.withParameters(0, mediaPeriodId, mediaTimeOffsetMs);
    }

    protected final EventDispatcher createEventDispatcher(int windowIndex, MediaPeriodId mediaPeriodId, long mediaTimeOffsetMs) {
        return this.eventDispatcher.withParameters(windowIndex, mediaPeriodId, mediaTimeOffsetMs);
    }

    public final void addEventListener(Handler handler, MediaSourceEventListener eventListener) {
        this.eventDispatcher.addEventListener(handler, eventListener);
    }

    public final void removeEventListener(MediaSourceEventListener eventListener) {
        this.eventDispatcher.removeEventListener(eventListener);
    }

    public final void prepareSource(ExoPlayer player, boolean isTopLevelSource, SourceInfoRefreshListener listener) {
        prepareSource(player, isTopLevelSource, listener, null);
    }

    public final void prepareSource(ExoPlayer player, boolean isTopLevelSource, SourceInfoRefreshListener listener, TransferListener mediaTransferListener) {
        boolean z = this.player == null || this.player == player;
        Assertions.checkArgument(z);
        this.sourceInfoListeners.add(listener);
        if (this.player == null) {
            this.player = player;
            prepareSourceInternal(player, isTopLevelSource, mediaTransferListener);
        } else if (this.timeline != null) {
            listener.onSourceInfoRefreshed(this, this.timeline, this.manifest);
        }
    }

    public final void releaseSource(SourceInfoRefreshListener listener) {
        this.sourceInfoListeners.remove(listener);
        if (this.sourceInfoListeners.isEmpty()) {
            this.player = null;
            this.timeline = null;
            this.manifest = null;
            releaseSourceInternal();
        }
    }
}
