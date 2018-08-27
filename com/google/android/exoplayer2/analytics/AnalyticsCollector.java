package com.google.android.exoplayer2.analytics;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.analytics.AnalyticsListener.EventTime;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.MediaSourceEventListener.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class AnalyticsCollector implements EventListener, AudioListener, AudioRendererEventListener, DefaultDrmSessionEventListener, MetadataOutput, MediaSourceEventListener, BandwidthMeter.EventListener, VideoListener, VideoRendererEventListener {
    private final Clock clock;
    private final CopyOnWriteArraySet<AnalyticsListener> listeners = new CopyOnWriteArraySet();
    private final MediaPeriodQueueTracker mediaPeriodQueueTracker = new MediaPeriodQueueTracker();
    private Player player;
    private final Window window = new Window();

    public static class Factory {
        public AnalyticsCollector createAnalyticsCollector(Player player, Clock clock) {
            return new AnalyticsCollector(player, clock);
        }
    }

    private static final class MediaPeriodQueueTracker {
        private final ArrayList<WindowAndMediaPeriodId> activeMediaPeriods = new ArrayList();
        private boolean isSeeking;
        private WindowAndMediaPeriodId lastReportedPlayingMediaPeriod;
        private final Period period = new Period();
        private WindowAndMediaPeriodId readingMediaPeriod;
        private Timeline timeline = Timeline.EMPTY;

        public WindowAndMediaPeriodId getPlayingMediaPeriod() {
            if (this.activeMediaPeriods.isEmpty() || this.timeline.isEmpty() || this.isSeeking) {
                return null;
            }
            return (WindowAndMediaPeriodId) this.activeMediaPeriods.get(0);
        }

        public WindowAndMediaPeriodId getLastReportedPlayingMediaPeriod() {
            return this.lastReportedPlayingMediaPeriod;
        }

        public WindowAndMediaPeriodId getReadingMediaPeriod() {
            return this.readingMediaPeriod;
        }

        public WindowAndMediaPeriodId getLoadingMediaPeriod() {
            if (this.activeMediaPeriods.isEmpty()) {
                return null;
            }
            return (WindowAndMediaPeriodId) this.activeMediaPeriods.get(this.activeMediaPeriods.size() - 1);
        }

        public boolean isSeeking() {
            return this.isSeeking;
        }

        public MediaPeriodId tryResolveWindowIndex(int windowIndex) {
            MediaPeriodId match = null;
            if (this.timeline != null) {
                int timelinePeriodCount = this.timeline.getPeriodCount();
                for (int i = 0; i < this.activeMediaPeriods.size(); i++) {
                    WindowAndMediaPeriodId mediaPeriod = (WindowAndMediaPeriodId) this.activeMediaPeriods.get(i);
                    int periodIndex = mediaPeriod.mediaPeriodId.periodIndex;
                    if (periodIndex < timelinePeriodCount && this.timeline.getPeriod(periodIndex, this.period).windowIndex == windowIndex) {
                        if (match != null) {
                            return null;
                        }
                        match = mediaPeriod.mediaPeriodId;
                    }
                }
            }
            MediaPeriodId mediaPeriodId = match;
            return match;
        }

        public void onPositionDiscontinuity(int reason) {
            updateLastReportedPlayingMediaPeriod();
        }

        public void onTimelineChanged(Timeline timeline) {
            for (int i = 0; i < this.activeMediaPeriods.size(); i++) {
                this.activeMediaPeriods.set(i, updateMediaPeriodToNewTimeline((WindowAndMediaPeriodId) this.activeMediaPeriods.get(i), timeline));
            }
            if (this.readingMediaPeriod != null) {
                this.readingMediaPeriod = updateMediaPeriodToNewTimeline(this.readingMediaPeriod, timeline);
            }
            this.timeline = timeline;
            updateLastReportedPlayingMediaPeriod();
        }

        public void onSeekStarted() {
            this.isSeeking = true;
        }

        public void onSeekProcessed() {
            this.isSeeking = false;
            updateLastReportedPlayingMediaPeriod();
        }

        public void onMediaPeriodCreated(int windowIndex, MediaPeriodId mediaPeriodId) {
            this.activeMediaPeriods.add(new WindowAndMediaPeriodId(windowIndex, mediaPeriodId));
            if (this.activeMediaPeriods.size() == 1 && !this.timeline.isEmpty()) {
                updateLastReportedPlayingMediaPeriod();
            }
        }

        public void onMediaPeriodReleased(int windowIndex, MediaPeriodId mediaPeriodId) {
            WindowAndMediaPeriodId mediaPeriod = new WindowAndMediaPeriodId(windowIndex, mediaPeriodId);
            this.activeMediaPeriods.remove(mediaPeriod);
            if (mediaPeriod.equals(this.readingMediaPeriod)) {
                this.readingMediaPeriod = this.activeMediaPeriods.isEmpty() ? null : (WindowAndMediaPeriodId) this.activeMediaPeriods.get(0);
            }
        }

        public void onReadingStarted(int windowIndex, MediaPeriodId mediaPeriodId) {
            this.readingMediaPeriod = new WindowAndMediaPeriodId(windowIndex, mediaPeriodId);
        }

        private void updateLastReportedPlayingMediaPeriod() {
            if (!this.activeMediaPeriods.isEmpty()) {
                this.lastReportedPlayingMediaPeriod = (WindowAndMediaPeriodId) this.activeMediaPeriods.get(0);
            }
        }

        private WindowAndMediaPeriodId updateMediaPeriodToNewTimeline(WindowAndMediaPeriodId mediaPeriod, Timeline newTimeline) {
            if (newTimeline.isEmpty() || this.timeline.isEmpty()) {
                return mediaPeriod;
            }
            int newPeriodIndex = newTimeline.getIndexOfPeriod(this.timeline.getUidOfPeriod(mediaPeriod.mediaPeriodId.periodIndex));
            if (newPeriodIndex != -1) {
                return new WindowAndMediaPeriodId(newTimeline.getPeriod(newPeriodIndex, this.period).windowIndex, mediaPeriod.mediaPeriodId.copyWithPeriodIndex(newPeriodIndex));
            }
            return mediaPeriod;
        }
    }

    private static final class WindowAndMediaPeriodId {
        public final MediaPeriodId mediaPeriodId;
        public final int windowIndex;

        public WindowAndMediaPeriodId(int windowIndex, MediaPeriodId mediaPeriodId) {
            this.windowIndex = windowIndex;
            this.mediaPeriodId = mediaPeriodId;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            WindowAndMediaPeriodId that = (WindowAndMediaPeriodId) other;
            if (this.windowIndex == that.windowIndex && this.mediaPeriodId.equals(that.mediaPeriodId)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.windowIndex * 31) + this.mediaPeriodId.hashCode();
        }
    }

    protected AnalyticsCollector(Player player, Clock clock) {
        this.player = player;
        this.clock = (Clock) Assertions.checkNotNull(clock);
    }

    public void addListener(AnalyticsListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(AnalyticsListener listener) {
        this.listeners.remove(listener);
    }

    public void setPlayer(Player player) {
        Assertions.checkState(this.player == null);
        this.player = (Player) Assertions.checkNotNull(player);
    }

    public final void notifySeekStarted() {
        if (!this.mediaPeriodQueueTracker.isSeeking()) {
            EventTime eventTime = generatePlayingMediaPeriodEventTime();
            this.mediaPeriodQueueTracker.onSeekStarted();
            Iterator it = this.listeners.iterator();
            while (it.hasNext()) {
                ((AnalyticsListener) it.next()).onSeekStarted(eventTime);
            }
        }
    }

    public final void resetForNewMediaSource() {
        for (WindowAndMediaPeriodId mediaPeriod : new ArrayList(this.mediaPeriodQueueTracker.activeMediaPeriods)) {
            onMediaPeriodReleased(mediaPeriod.windowIndex, mediaPeriod.mediaPeriodId);
        }
    }

    public final void onMetadata(Metadata metadata) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onMetadata(eventTime, metadata);
        }
    }

    public final void onAudioEnabled(DecoderCounters counters) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDecoderEnabled(eventTime, 1, counters);
        }
    }

    public final void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDecoderInitialized(eventTime, 1, decoderName, initializationDurationMs);
        }
    }

    public final void onAudioInputFormatChanged(Format format) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDecoderInputFormatChanged(eventTime, 1, format);
        }
    }

    public final void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onAudioUnderrun(eventTime, bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
        }
    }

    public final void onAudioDisabled(DecoderCounters counters) {
        EventTime eventTime = generateLastReportedPlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDecoderDisabled(eventTime, 1, counters);
        }
    }

    public final void onAudioSessionId(int audioSessionId) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onAudioSessionId(eventTime, audioSessionId);
        }
    }

    public void onAudioAttributesChanged(AudioAttributes audioAttributes) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onAudioAttributesChanged(eventTime, audioAttributes);
        }
    }

    public void onVolumeChanged(float audioVolume) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onVolumeChanged(eventTime, audioVolume);
        }
    }

    public final void onVideoEnabled(DecoderCounters counters) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDecoderEnabled(eventTime, 2, counters);
        }
    }

    public final void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDecoderInitialized(eventTime, 2, decoderName, initializationDurationMs);
        }
    }

    public final void onVideoInputFormatChanged(Format format) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDecoderInputFormatChanged(eventTime, 2, format);
        }
    }

    public final void onDroppedFrames(int count, long elapsedMs) {
        EventTime eventTime = generateLastReportedPlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDroppedVideoFrames(eventTime, count, elapsedMs);
        }
    }

    public final void onVideoDisabled(DecoderCounters counters) {
        EventTime eventTime = generateLastReportedPlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDecoderDisabled(eventTime, 2, counters);
        }
    }

    public final void onRenderedFirstFrame(Surface surface) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onRenderedFirstFrame(eventTime, surface);
        }
    }

    public final void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onVideoSizeChanged(eventTime, width, height, unappliedRotationDegrees, pixelWidthHeightRatio);
        }
    }

    public void onSurfaceSizeChanged(int width, int height) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onSurfaceSizeChanged(eventTime, width, height);
        }
    }

    public final void onRenderedFirstFrame() {
    }

    public final void onMediaPeriodCreated(int windowIndex, MediaPeriodId mediaPeriodId) {
        this.mediaPeriodQueueTracker.onMediaPeriodCreated(windowIndex, mediaPeriodId);
        EventTime eventTime = generateEventTime(windowIndex, mediaPeriodId);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onMediaPeriodCreated(eventTime);
        }
    }

    public final void onMediaPeriodReleased(int windowIndex, MediaPeriodId mediaPeriodId) {
        this.mediaPeriodQueueTracker.onMediaPeriodReleased(windowIndex, mediaPeriodId);
        EventTime eventTime = generateEventTime(windowIndex, mediaPeriodId);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onMediaPeriodReleased(eventTime);
        }
    }

    public final void onLoadStarted(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        EventTime eventTime = generateEventTime(windowIndex, mediaPeriodId);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onLoadStarted(eventTime, loadEventInfo, mediaLoadData);
        }
    }

    public final void onLoadCompleted(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        EventTime eventTime = generateEventTime(windowIndex, mediaPeriodId);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onLoadCompleted(eventTime, loadEventInfo, mediaLoadData);
        }
    }

    public final void onLoadCanceled(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        EventTime eventTime = generateEventTime(windowIndex, mediaPeriodId);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onLoadCanceled(eventTime, loadEventInfo, mediaLoadData);
        }
    }

    public final void onLoadError(int windowIndex, MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {
        EventTime eventTime = generateEventTime(windowIndex, mediaPeriodId);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onLoadError(eventTime, loadEventInfo, mediaLoadData, error, wasCanceled);
        }
    }

    public final void onReadingStarted(int windowIndex, MediaPeriodId mediaPeriodId) {
        this.mediaPeriodQueueTracker.onReadingStarted(windowIndex, mediaPeriodId);
        EventTime eventTime = generateEventTime(windowIndex, mediaPeriodId);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onReadingStarted(eventTime);
        }
    }

    public final void onUpstreamDiscarded(int windowIndex, MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
        EventTime eventTime = generateEventTime(windowIndex, mediaPeriodId);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onUpstreamDiscarded(eventTime, mediaLoadData);
        }
    }

    public final void onDownstreamFormatChanged(int windowIndex, MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {
        EventTime eventTime = generateEventTime(windowIndex, mediaPeriodId);
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDownstreamFormatChanged(eventTime, mediaLoadData);
        }
    }

    public final void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        this.mediaPeriodQueueTracker.onTimelineChanged(timeline);
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onTimelineChanged(eventTime, reason);
        }
    }

    public final void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onTracksChanged(eventTime, trackGroups, trackSelections);
        }
    }

    public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public final void onLoadingChanged(boolean isLoading) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onLoadingChanged(eventTime, isLoading);
        }
    }

    public final void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onPlayerStateChanged(eventTime, playWhenReady, playbackState);
        }
    }

    public final void onRepeatModeChanged(int repeatMode) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onRepeatModeChanged(eventTime, repeatMode);
        }
    }

    public final void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onShuffleModeChanged(eventTime, shuffleModeEnabled);
        }
    }

    public final void onPlayerError(ExoPlaybackException error) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onPlayerError(eventTime, error);
        }
    }

    public final void onPositionDiscontinuity(int reason) {
        this.mediaPeriodQueueTracker.onPositionDiscontinuity(reason);
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onPositionDiscontinuity(eventTime, reason);
        }
    }

    public final void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        EventTime eventTime = generatePlayingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onPlaybackParametersChanged(eventTime, playbackParameters);
        }
    }

    public final void onSeekProcessed() {
        if (this.mediaPeriodQueueTracker.isSeeking()) {
            this.mediaPeriodQueueTracker.onSeekProcessed();
            EventTime eventTime = generatePlayingMediaPeriodEventTime();
            Iterator it = this.listeners.iterator();
            while (it.hasNext()) {
                ((AnalyticsListener) it.next()).onSeekProcessed(eventTime);
            }
        }
    }

    public final void onBandwidthSample(int elapsedMs, long bytes, long bitrate) {
        EventTime eventTime = generateLoadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onBandwidthEstimate(eventTime, elapsedMs, bytes, bitrate);
        }
    }

    public final void onDrmKeysLoaded() {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDrmKeysLoaded(eventTime);
        }
    }

    public final void onDrmSessionManagerError(Exception error) {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDrmSessionManagerError(eventTime, error);
        }
    }

    public final void onDrmKeysRestored() {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDrmKeysRestored(eventTime);
        }
    }

    public final void onDrmKeysRemoved() {
        EventTime eventTime = generateReadingMediaPeriodEventTime();
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((AnalyticsListener) it.next()).onDrmKeysRemoved(eventTime);
        }
    }

    protected Set<AnalyticsListener> getListeners() {
        return Collections.unmodifiableSet(this.listeners);
    }

    protected EventTime generateEventTime(int windowIndex, MediaPeriodId mediaPeriodId) {
        Assertions.checkNotNull(this.player);
        long realtimeMs = this.clock.elapsedRealtime();
        Timeline timeline = this.player.getCurrentTimeline();
        long eventPositionMs = windowIndex == this.player.getCurrentWindowIndex() ? (mediaPeriodId == null || !mediaPeriodId.isAd()) ? this.player.getContentPosition() : (this.player.getCurrentAdGroupIndex() == mediaPeriodId.adGroupIndex && this.player.getCurrentAdIndexInAdGroup() == mediaPeriodId.adIndexInAdGroup) ? this.player.getCurrentPosition() : 0 : (windowIndex >= timeline.getWindowCount() || (mediaPeriodId != null && mediaPeriodId.isAd())) ? 0 : timeline.getWindow(windowIndex, this.window).getDefaultPositionMs();
        return new EventTime(realtimeMs, timeline, windowIndex, mediaPeriodId, eventPositionMs, this.player.getCurrentPosition(), this.player.getTotalBufferedDuration());
    }

    private EventTime generateEventTime(WindowAndMediaPeriodId mediaPeriod) {
        if (mediaPeriod != null) {
            return generateEventTime(mediaPeriod.windowIndex, mediaPeriod.mediaPeriodId);
        }
        int windowIndex = ((Player) Assertions.checkNotNull(this.player)).getCurrentWindowIndex();
        return generateEventTime(windowIndex, this.mediaPeriodQueueTracker.tryResolveWindowIndex(windowIndex));
    }

    private EventTime generateLastReportedPlayingMediaPeriodEventTime() {
        return generateEventTime(this.mediaPeriodQueueTracker.getLastReportedPlayingMediaPeriod());
    }

    private EventTime generatePlayingMediaPeriodEventTime() {
        return generateEventTime(this.mediaPeriodQueueTracker.getPlayingMediaPeriod());
    }

    private EventTime generateReadingMediaPeriodEventTime() {
        return generateEventTime(this.mediaPeriodQueueTracker.getReadingMediaPeriod());
    }

    private EventTime generateLoadingMediaPeriodEventTime() {
        return generateEventTime(this.mediaPeriodQueueTracker.getLoadingMediaPeriod());
    }
}
