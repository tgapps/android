package com.google.android.exoplayer2;

import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

public abstract /* synthetic */ class Player$EventListener$$CC {
    public static void onTimelineChanged(EventListener this_, Timeline timeline, Object manifest, int reason) {
    }

    public static void onTracksChanged(EventListener this_, TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    public static void onLoadingChanged(EventListener this_, boolean isLoading) {
    }

    public static void onPlayerStateChanged(EventListener this_, boolean playWhenReady, int playbackState) {
    }

    public static void onRepeatModeChanged(EventListener this_, int repeatMode) {
    }

    public static void onShuffleModeEnabledChanged(EventListener this_, boolean shuffleModeEnabled) {
    }

    public static void onPlayerError(EventListener this_, ExoPlaybackException error) {
    }

    public static void onPositionDiscontinuity(EventListener this_, int reason) {
    }

    public static void onPlaybackParametersChanged(EventListener this_, PlaybackParameters playbackParameters) {
    }

    public static void onSeekProcessed(EventListener this_) {
    }
}
