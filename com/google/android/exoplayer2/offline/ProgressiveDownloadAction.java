package com.google.android.exoplayer2.offline;

import android.net.Uri;
import com.google.android.exoplayer2.offline.DownloadAction.Deserializer;
import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ProgressiveDownloadAction extends DownloadAction {
    public static final Deserializer DESERIALIZER = new Deserializer(TYPE, 0) {
        public ProgressiveDownloadAction readFromStream(int version, DataInputStream input) throws IOException {
            Uri uri = Uri.parse(input.readUTF());
            boolean isRemoveAction = input.readBoolean();
            byte[] data = new byte[input.readInt()];
            input.readFully(data);
            return new ProgressiveDownloadAction(uri, isRemoveAction, data, input.readBoolean() ? input.readUTF() : null);
        }
    };
    private static final String TYPE = "progressive";
    private static final int VERSION = 0;
    private final String customCacheKey;

    public static ProgressiveDownloadAction createDownloadAction(Uri uri, byte[] data, String customCacheKey) {
        return new ProgressiveDownloadAction(uri, false, data, customCacheKey);
    }

    public static ProgressiveDownloadAction createRemoveAction(Uri uri, byte[] data, String customCacheKey) {
        return new ProgressiveDownloadAction(uri, true, data, customCacheKey);
    }

    @Deprecated
    public ProgressiveDownloadAction(Uri uri, boolean isRemoveAction, byte[] data, String customCacheKey) {
        super(TYPE, 0, uri, isRemoveAction, data);
        this.customCacheKey = customCacheKey;
    }

    public ProgressiveDownloader createDownloader(DownloaderConstructorHelper constructorHelper) {
        return new ProgressiveDownloader(this.uri, this.customCacheKey, constructorHelper);
    }

    protected void writeToStream(DataOutputStream output) throws IOException {
        output.writeUTF(this.uri.toString());
        output.writeBoolean(this.isRemoveAction);
        output.writeInt(this.data.length);
        output.write(this.data);
        boolean customCacheKeySet = this.customCacheKey != null;
        output.writeBoolean(customCacheKeySet);
        if (customCacheKeySet) {
            output.writeUTF(this.customCacheKey);
        }
    }

    public boolean isSameMedia(DownloadAction other) {
        return (other instanceof ProgressiveDownloadAction) && getCacheKey().equals(((ProgressiveDownloadAction) other).getCacheKey());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Util.areEqual(this.customCacheKey, ((ProgressiveDownloadAction) o).customCacheKey);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + (this.customCacheKey != null ? this.customCacheKey.hashCode() : 0);
    }

    private String getCacheKey() {
        return this.customCacheKey != null ? this.customCacheKey : CacheUtil.generateKey(this.uri);
    }
}
