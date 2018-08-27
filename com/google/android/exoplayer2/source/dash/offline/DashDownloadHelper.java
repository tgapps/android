package com.google.android.exoplayer2.source.dash.offline;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.offline.TrackKey;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class DashDownloadHelper extends DownloadHelper {
    private DashManifest manifest;
    private final Factory manifestDataSourceFactory;
    private final Uri uri;

    public DashDownloadHelper(Uri uri, Factory manifestDataSourceFactory) {
        this.uri = uri;
        this.manifestDataSourceFactory = manifestDataSourceFactory;
    }

    protected void prepareInternal() throws IOException {
        this.manifest = (DashManifest) ParsingLoadable.load(this.manifestDataSourceFactory.createDataSource(), new DashManifestParser(), this.uri, 4);
    }

    public DashManifest getManifest() {
        Assertions.checkNotNull(this.manifest);
        return this.manifest;
    }

    public int getPeriodCount() {
        Assertions.checkNotNull(this.manifest);
        return this.manifest.getPeriodCount();
    }

    public TrackGroupArray getTrackGroups(int periodIndex) {
        Assertions.checkNotNull(this.manifest);
        List<AdaptationSet> adaptationSets = this.manifest.getPeriod(periodIndex).adaptationSets;
        TrackGroup[] trackGroups = new TrackGroup[adaptationSets.size()];
        for (int i = 0; i < trackGroups.length; i++) {
            List<Representation> representations = ((AdaptationSet) adaptationSets.get(i)).representations;
            Format[] formats = new Format[representations.size()];
            int representationsCount = representations.size();
            for (int j = 0; j < representationsCount; j++) {
                formats[j] = ((Representation) representations.get(j)).format;
            }
            trackGroups[i] = new TrackGroup(formats);
        }
        return new TrackGroupArray(trackGroups);
    }

    public DashDownloadAction getDownloadAction(byte[] data, List<TrackKey> trackKeys) {
        return DashDownloadAction.createDownloadAction(this.uri, data, toStreamKeys(trackKeys));
    }

    public DashDownloadAction getRemoveAction(byte[] data) {
        return DashDownloadAction.createRemoveAction(this.uri, data);
    }

    private static List<StreamKey> toStreamKeys(List<TrackKey> trackKeys) {
        List<StreamKey> streamKeys = new ArrayList(trackKeys.size());
        for (int i = 0; i < trackKeys.size(); i++) {
            TrackKey trackKey = (TrackKey) trackKeys.get(i);
            streamKeys.add(new StreamKey(trackKey.periodIndex, trackKey.groupIndex, trackKey.trackIndex));
        }
        return streamKeys;
    }
}
