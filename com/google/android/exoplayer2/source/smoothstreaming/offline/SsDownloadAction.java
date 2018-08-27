package com.google.android.exoplayer2.source.smoothstreaming.offline;

import android.net.Uri;
import com.google.android.exoplayer2.offline.DownloadAction;
import com.google.android.exoplayer2.offline.DownloadAction.Deserializer;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.offline.SegmentDownloadAction;
import com.google.android.exoplayer2.offline.StreamKey;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class SsDownloadAction extends SegmentDownloadAction {
    public static final Deserializer DESERIALIZER = new SegmentDownloadActionDeserializer(TYPE, 1) {
        protected StreamKey readKey(int version, DataInputStream input) throws IOException {
            if (version > 0) {
                return super.readKey(version, input);
            }
            return new StreamKey(input.readInt(), input.readInt());
        }

        protected DownloadAction createDownloadAction(Uri uri, boolean isRemoveAction, byte[] data, List<StreamKey> keys) {
            return new SsDownloadAction(uri, isRemoveAction, data, keys);
        }
    };
    private static final String TYPE = "ss";
    private static final int VERSION = 1;

    public static SsDownloadAction createDownloadAction(Uri uri, byte[] data, List<StreamKey> keys) {
        return new SsDownloadAction(uri, false, data, keys);
    }

    public static SsDownloadAction createRemoveAction(Uri uri, byte[] data) {
        return new SsDownloadAction(uri, true, data, Collections.emptyList());
    }

    @Deprecated
    public SsDownloadAction(Uri uri, boolean isRemoveAction, byte[] data, List<StreamKey> keys) {
        super(TYPE, 1, uri, isRemoveAction, data, keys);
    }

    public SsDownloader createDownloader(DownloaderConstructorHelper constructorHelper) {
        return new SsDownloader(this.uri, this.keys, constructorHelper);
    }
}
