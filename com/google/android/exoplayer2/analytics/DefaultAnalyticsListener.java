package com.google.android.exoplayer2.analytics;

import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.analytics.AnalyticsListener.EventTime;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSourceEventListener.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import java.io.IOException;

@Deprecated
public abstract class DefaultAnalyticsListener implements AnalyticsListener {
    public void onAudioAttributesChanged(EventTime eventTime, AudioAttributes audioAttributes) {
        AnalyticsListener$$CC.onAudioAttributesChanged(this, eventTime, audioAttributes);
    }

    public void onAudioSessionId(EventTime eventTime, int i) {
        AnalyticsListener$$CC.onAudioSessionId(this, eventTime, i);
    }

    public void onAudioUnderrun(EventTime eventTime, int i, long j, long j2) {
        AnalyticsListener$$CC.onAudioUnderrun(this, eventTime, i, j, j2);
    }

    public void onBandwidthEstimate(EventTime eventTime, int i, long j, long j2) {
        AnalyticsListener$$CC.onBandwidthEstimate(this, eventTime, i, j, j2);
    }

    public void onDecoderDisabled(EventTime eventTime, int i, DecoderCounters decoderCounters) {
        AnalyticsListener$$CC.onDecoderDisabled(this, eventTime, i, decoderCounters);
    }

    public void onDecoderEnabled(EventTime eventTime, int i, DecoderCounters decoderCounters) {
        AnalyticsListener$$CC.onDecoderEnabled(this, eventTime, i, decoderCounters);
    }

    public void onDecoderInitialized(EventTime eventTime, int i, String str, long j) {
        AnalyticsListener$$CC.onDecoderInitialized(this, eventTime, i, str, j);
    }

    public void onDecoderInputFormatChanged(EventTime eventTime, int i, Format format) {
        AnalyticsListener$$CC.onDecoderInputFormatChanged(this, eventTime, i, format);
    }

    public void onDownstreamFormatChanged(EventTime eventTime, MediaLoadData mediaLoadData) {
        AnalyticsListener$$CC.onDownstreamFormatChanged(this, eventTime, mediaLoadData);
    }

    public void onDrmKeysLoaded(EventTime eventTime) {
        AnalyticsListener$$CC.onDrmKeysLoaded(this, eventTime);
    }

    public void onDrmKeysRemoved(EventTime eventTime) {
        AnalyticsListener$$CC.onDrmKeysRemoved(this, eventTime);
    }

    public void onDrmKeysRestored(EventTime eventTime) {
        AnalyticsListener$$CC.onDrmKeysRestored(this, eventTime);
    }

    public void onDrmSessionManagerError(EventTime eventTime, Exception exception) {
        AnalyticsListener$$CC.onDrmSessionManagerError(this, eventTime, exception);
    }

    public void onDroppedVideoFrames(EventTime eventTime, int i, long j) {
        AnalyticsListener$$CC.onDroppedVideoFrames(this, eventTime, i, j);
    }

    public void onLoadCanceled(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        AnalyticsListener$$CC.onLoadCanceled(this, eventTime, loadEventInfo, mediaLoadData);
    }

    public void onLoadCompleted(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        AnalyticsListener$$CC.onLoadCompleted(this, eventTime, loadEventInfo, mediaLoadData);
    }

    public void onLoadError(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException iOException, boolean z) {
        AnalyticsListener$$CC.onLoadError(this, eventTime, loadEventInfo, mediaLoadData, iOException, z);
    }

    public void onLoadStarted(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        AnalyticsListener$$CC.onLoadStarted(this, eventTime, loadEventInfo, mediaLoadData);
    }

    public void onLoadingChanged(EventTime eventTime, boolean z) {
        AnalyticsListener$$CC.onLoadingChanged(this, eventTime, z);
    }

    public void onMediaPeriodCreated(EventTime eventTime) {
        AnalyticsListener$$CC.onMediaPeriodCreated(this, eventTime);
    }

    public void onMediaPeriodReleased(EventTime eventTime) {
        AnalyticsListener$$CC.onMediaPeriodReleased(this, eventTime);
    }

    public void onMetadata(EventTime eventTime, Metadata metadata) {
        AnalyticsListener$$CC.onMetadata(this, eventTime, metadata);
    }

    public void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters) {
        AnalyticsListener$$CC.onPlaybackParametersChanged(this, eventTime, playbackParameters);
    }

    public void onPlayerError(EventTime eventTime, ExoPlaybackException exoPlaybackException) {
        AnalyticsListener$$CC.onPlayerError(this, eventTime, exoPlaybackException);
    }

    public void onPlayerStateChanged(EventTime eventTime, boolean z, int i) {
        AnalyticsListener$$CC.onPlayerStateChanged(this, eventTime, z, i);
    }

    public void onPositionDiscontinuity(EventTime eventTime, int i) {
        AnalyticsListener$$CC.onPositionDiscontinuity(this, eventTime, i);
    }

    public void onReadingStarted(EventTime eventTime) {
        AnalyticsListener$$CC.onReadingStarted(this, eventTime);
    }

    public void onRenderedFirstFrame(EventTime eventTime, Surface surface) {
        AnalyticsListener$$CC.onRenderedFirstFrame(this, eventTime, surface);
    }

    public void onRepeatModeChanged(EventTime eventTime, int i) {
        AnalyticsListener$$CC.onRepeatModeChanged(this, eventTime, i);
    }

    public void onSeekProcessed(EventTime eventTime) {
        AnalyticsListener$$CC.onSeekProcessed(this, eventTime);
    }

    public void onSeekStarted(EventTime eventTime) {
        AnalyticsListener$$CC.onSeekStarted(this, eventTime);
    }

    public void onShuffleModeChanged(EventTime eventTime, boolean z) {
        AnalyticsListener$$CC.onShuffleModeChanged(this, eventTime, z);
    }

    public void onSurfaceSizeChanged(EventTime eventTime, int i, int i2) {
        AnalyticsListener$$CC.onSurfaceSizeChanged(this, eventTime, i, i2);
    }

    public void onTimelineChanged(EventTime eventTime, int i) {
        AnalyticsListener$$CC.onTimelineChanged(this, eventTime, i);
    }

    public void onTracksChanged(EventTime eventTime, TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
        AnalyticsListener$$CC.onTracksChanged(this, eventTime, trackGroupArray, trackSelectionArray);
    }

    public void onUpstreamDiscarded(EventTime eventTime, MediaLoadData mediaLoadData) {
        AnalyticsListener$$CC.onUpstreamDiscarded(this, eventTime, mediaLoadData);
    }

    public void onVideoSizeChanged(EventTime eventTime, int i, int i2, int i3, float f) {
        AnalyticsListener$$CC.onVideoSizeChanged(this, eventTime, i, i2, i3, f);
    }

    public void onVolumeChanged(EventTime eventTime, float f) {
        AnalyticsListener$$CC.onVolumeChanged(this, eventTime, f);
    }
}
