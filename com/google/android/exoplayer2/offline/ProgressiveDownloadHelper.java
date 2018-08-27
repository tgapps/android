package com.google.android.exoplayer2.offline;

import android.net.Uri;
import com.google.android.exoplayer2.source.TrackGroupArray;
import java.util.List;

public final class ProgressiveDownloadHelper extends DownloadHelper {
    private final String customCacheKey;
    private final Uri uri;

    public ProgressiveDownloadHelper(Uri uri) {
        this(uri, null);
    }

    public ProgressiveDownloadHelper(Uri uri, String customCacheKey) {
        this.uri = uri;
        this.customCacheKey = customCacheKey;
    }

    protected void prepareInternal() {
    }

    public int getPeriodCount() {
        return 1;
    }

    public TrackGroupArray getTrackGroups(int periodIndex) {
        return TrackGroupArray.EMPTY;
    }

    public ProgressiveDownloadAction getDownloadAction(byte[] data, List<TrackKey> list) {
        return ProgressiveDownloadAction.createDownloadAction(this.uri, data, this.customCacheKey);
    }

    public ProgressiveDownloadAction getRemoveAction(byte[] data) {
        return ProgressiveDownloadAction.createRemoveAction(this.uri, data, this.customCacheKey);
    }
}
