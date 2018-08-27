package com.google.android.exoplayer2.source.hls.offline;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.offline.TrackKey;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist.HlsUrl;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HlsDownloadHelper extends DownloadHelper {
    private final Factory manifestDataSourceFactory;
    private HlsPlaylist playlist;
    private int[] renditionGroups;
    private final Uri uri;

    public HlsDownloadHelper(Uri uri, Factory manifestDataSourceFactory) {
        this.uri = uri;
        this.manifestDataSourceFactory = manifestDataSourceFactory;
    }

    protected void prepareInternal() throws IOException {
        this.playlist = (HlsPlaylist) ParsingLoadable.load(this.manifestDataSourceFactory.createDataSource(), new HlsPlaylistParser(), this.uri, 4);
    }

    public HlsPlaylist getPlaylist() {
        Assertions.checkNotNull(this.playlist);
        return this.playlist;
    }

    public int getPeriodCount() {
        Assertions.checkNotNull(this.playlist);
        return 1;
    }

    public TrackGroupArray getTrackGroups(int periodIndex) {
        Assertions.checkNotNull(this.playlist);
        if (this.playlist instanceof HlsMediaPlaylist) {
            this.renditionGroups = new int[0];
            return TrackGroupArray.EMPTY;
        }
        HlsMasterPlaylist masterPlaylist = this.playlist;
        TrackGroup[] trackGroups = new TrackGroup[3];
        this.renditionGroups = new int[3];
        int i = 0;
        if (!masterPlaylist.variants.isEmpty()) {
            this.renditionGroups[0] = 0;
            int trackGroupIndex = 0 + 1;
            trackGroups[0] = new TrackGroup(toFormats(masterPlaylist.variants));
            i = trackGroupIndex;
        }
        if (!masterPlaylist.audios.isEmpty()) {
            this.renditionGroups[i] = 1;
            trackGroupIndex = i + 1;
            trackGroups[i] = new TrackGroup(toFormats(masterPlaylist.audios));
            i = trackGroupIndex;
        }
        if (!masterPlaylist.subtitles.isEmpty()) {
            this.renditionGroups[i] = 2;
            trackGroupIndex = i + 1;
            trackGroups[i] = new TrackGroup(toFormats(masterPlaylist.subtitles));
            i = trackGroupIndex;
        }
        return new TrackGroupArray((TrackGroup[]) Arrays.copyOf(trackGroups, i));
    }

    public HlsDownloadAction getDownloadAction(byte[] data, List<TrackKey> trackKeys) {
        Assertions.checkNotNull(this.renditionGroups);
        return HlsDownloadAction.createDownloadAction(this.uri, data, toStreamKeys(trackKeys, this.renditionGroups));
    }

    public HlsDownloadAction getRemoveAction(byte[] data) {
        return HlsDownloadAction.createRemoveAction(this.uri, data);
    }

    private static Format[] toFormats(List<HlsUrl> hlsUrls) {
        Format[] formats = new Format[hlsUrls.size()];
        for (int i = 0; i < hlsUrls.size(); i++) {
            formats[i] = ((HlsUrl) hlsUrls.get(i)).format;
        }
        return formats;
    }

    private static List<StreamKey> toStreamKeys(List<TrackKey> trackKeys, int[] groups) {
        List<StreamKey> representationKeys = new ArrayList(trackKeys.size());
        for (int i = 0; i < trackKeys.size(); i++) {
            TrackKey trackKey = (TrackKey) trackKeys.get(i);
            representationKeys.add(new StreamKey(groups[trackKey.groupIndex], trackKey.trackIndex));
        }
        return representationKeys;
    }
}
