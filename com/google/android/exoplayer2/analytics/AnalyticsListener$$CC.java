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

public abstract /* synthetic */ class AnalyticsListener$$CC {
    public static void onPlayerStateChanged(AnalyticsListener this_, EventTime eventTime, boolean playWhenReady, int playbackState) {
    }

    public static void onTimelineChanged(AnalyticsListener this_, EventTime eventTime, int reason) {
    }

    public static void onPositionDiscontinuity(AnalyticsListener this_, EventTime eventTime, int reason) {
    }

    public static void onSeekStarted(AnalyticsListener this_, EventTime eventTime) {
    }

    public static void onSeekProcessed(AnalyticsListener this_, EventTime eventTime) {
    }

    public static void onPlaybackParametersChanged(AnalyticsListener this_, EventTime eventTime, PlaybackParameters playbackParameters) {
    }

    public static void onRepeatModeChanged(AnalyticsListener this_, EventTime eventTime, int repeatMode) {
    }

    public static void onShuffleModeChanged(AnalyticsListener this_, EventTime eventTime, boolean shuffleModeEnabled) {
    }

    public static void onLoadingChanged(AnalyticsListener this_, EventTime eventTime, boolean isLoading) {
    }

    public static void onPlayerError(AnalyticsListener this_, EventTime eventTime, ExoPlaybackException error) {
    }

    public static void onTracksChanged(AnalyticsListener this_, EventTime eventTime, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    public static void onLoadStarted(AnalyticsListener this_, EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public static void onLoadCompleted(AnalyticsListener this_, EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public static void onLoadCanceled(AnalyticsListener this_, EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public static void onLoadError(AnalyticsListener this_, EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {
    }

    public static void onDownstreamFormatChanged(AnalyticsListener this_, EventTime eventTime, MediaLoadData mediaLoadData) {
    }

    public static void onUpstreamDiscarded(AnalyticsListener this_, EventTime eventTime, MediaLoadData mediaLoadData) {
    }

    public static void onMediaPeriodCreated(AnalyticsListener this_, EventTime eventTime) {
    }

    public static void onMediaPeriodReleased(AnalyticsListener this_, EventTime eventTime) {
    }

    public static void onReadingStarted(AnalyticsListener this_, EventTime eventTime) {
    }

    public static void onBandwidthEstimate(AnalyticsListener this_, EventTime eventTime, int totalLoadTimeMs, long totalBytesLoaded, long bitrateEstimate) {
    }

    public static void onSurfaceSizeChanged(AnalyticsListener this_, EventTime eventTime, int width, int height) {
    }

    public static void onMetadata(AnalyticsListener this_, EventTime eventTime, Metadata metadata) {
    }

    public static void onDecoderEnabled(AnalyticsListener this_, EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
    }

    public static void onDecoderInitialized(AnalyticsListener this_, EventTime eventTime, int trackType, String decoderName, long initializationDurationMs) {
    }

    public static void onDecoderInputFormatChanged(AnalyticsListener this_, EventTime eventTime, int trackType, Format format) {
    }

    public static void onDecoderDisabled(AnalyticsListener this_, EventTime eventTime, int trackType, DecoderCounters decoderCounters) {
    }

    public static void onAudioSessionId(AnalyticsListener this_, EventTime eventTime, int audioSessionId) {
    }

    public static void onAudioAttributesChanged(AnalyticsListener this_, EventTime eventTime, AudioAttributes audioAttributes) {
    }

    public static void onVolumeChanged(AnalyticsListener this_, EventTime eventTime, float volume) {
    }

    public static void onAudioUnderrun(AnalyticsListener this_, EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
    }

    public static void onDroppedVideoFrames(AnalyticsListener this_, EventTime eventTime, int droppedFrames, long elapsedMs) {
    }

    public static void onVideoSizeChanged(AnalyticsListener this_, EventTime eventTime, int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
    }

    public static void onRenderedFirstFrame(AnalyticsListener this_, EventTime eventTime, Surface surface) {
    }

    public static void onDrmKeysLoaded(AnalyticsListener this_, EventTime eventTime) {
    }

    public static void onDrmSessionManagerError(AnalyticsListener this_, EventTime eventTime, Exception error) {
    }

    public static void onDrmKeysRestored(AnalyticsListener this_, EventTime eventTime) {
    }

    public static void onDrmKeysRemoved(AnalyticsListener this_, EventTime eventTime) {
    }
}
