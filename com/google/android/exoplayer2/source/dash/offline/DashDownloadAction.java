package com.google.android.exoplayer2.source.dash.offline;

import android.net.Uri;
import com.google.android.exoplayer2.offline.DownloadAction;
import com.google.android.exoplayer2.offline.DownloadAction.Deserializer;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.offline.SegmentDownloadAction;
import com.google.android.exoplayer2.offline.StreamKey;
import java.util.Collections;
import java.util.List;

public final class DashDownloadAction extends SegmentDownloadAction {
    public static final Deserializer DESERIALIZER = new SegmentDownloadActionDeserializer(TYPE, 0) {
        protected DownloadAction createDownloadAction(Uri uri, boolean isRemoveAction, byte[] data, List<StreamKey> keys) {
            return new DashDownloadAction(uri, isRemoveAction, data, keys);
        }
    };
    private static final String TYPE = "dash";
    private static final int VERSION = 0;

    public static DashDownloadAction createDownloadAction(Uri uri, byte[] data, List<StreamKey> keys) {
        return new DashDownloadAction(uri, false, data, keys);
    }

    public static DashDownloadAction createRemoveAction(Uri uri, byte[] data) {
        return new DashDownloadAction(uri, true, data, Collections.emptyList());
    }

    @Deprecated
    public DashDownloadAction(Uri uri, boolean isRemoveAction, byte[] data, List<StreamKey> keys) {
        super(TYPE, 0, uri, isRemoveAction, data, keys);
    }

    public DashDownloader createDownloader(DownloaderConstructorHelper constructorHelper) {
        return new DashDownloader(this.uri, this.keys, constructorHelper);
    }
}
